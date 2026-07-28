package com.internship.training.checkout.services;

import com.internship.training.checkout.models.dto.CheckoutEvent;
import com.internship.training.checkout.models.dto.CheckoutRequestDTO;
import com.internship.training.checkout.models.dto.CheckoutResponseDTO;
import com.internship.training.checkout.models.entities.Checkout;
import com.internship.training.checkout.producer.CheckoutProducer;
import com.internship.training.checkout.repositories.CheckoutRepository;
import com.internship.training.products.models.dto.ProductResponseDTO;
import com.internship.training.products.models.entities.Product;
import com.internship.training.products.repositories.ProductRepository;
import com.internship.training.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CheckoutProducer checkoutProducer;

    public CheckoutService(CheckoutRepository checkoutRepository, 
                           ProductRepository productRepository, 
                           UserRepository userRepository, 
                           CheckoutProducer checkoutProducer) {
        this.checkoutRepository = checkoutRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.checkoutProducer = checkoutProducer;
    }

    // Criar um Checkout e disparar o evento no Kafka
    public Optional<CheckoutResponseDTO> createCheckout(CheckoutRequestDTO request) {
        // 1. Validar se o usuário existe
        if (!userRepository.existsById(request.userId())) {
            return Optional.empty(); // Usuário não encontrado
        }

        // 2. Buscar a lista de produtos solicitados
        List<Product> products = productRepository.findAllById(request.productIds());
        if (products.isEmpty()) {
            return Optional.empty(); // Nenhum produto válido informado
        }

        // 3. Calcular preço total
        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Instanciar e salvar o Checkout com isCompleted = false
        Checkout checkout = new Checkout();
        checkout.setUserId(request.userId());
        checkout.setProducts(products);
        checkout.setTotalPrice(totalPrice);
        checkout.setIsCompleted(false);

        Checkout savedCheckout = checkoutRepository.save(checkout);

        // 5. Enviar o evento de Checkout para o Kafka
        CheckoutEvent event = new CheckoutEvent(
                savedCheckout.getId(),
                savedCheckout.getUserId(),
                savedCheckout.getTotalPrice()
        );
        checkoutProducer.sendCheckoutEvent(event);

        return Optional.of(convertToResponseDTO(savedCheckout));
    }

    // Método chamado pelo Consumidor para finalizar o Checkout
    public void completeCheckout(String checkoutId) {
        checkoutRepository.findById(checkoutId).ifPresent(checkout -> {
            checkout.setIsCompleted(true);
            checkoutRepository.save(checkout);
            System.out.println("Checkout finalizado com sucesso no banco: ID " + checkoutId);
        });
    }

    // Buscar Checkout por ID
    public Optional<CheckoutResponseDTO> getCheckoutById(String id) {
        return checkoutRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    // Listar todos os Checkouts
    public List<CheckoutResponseDTO> getAllCheckouts() {
        return checkoutRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // --- Métodos de Mapeamento (Mappers) ---

    private CheckoutResponseDTO convertToResponseDTO(Checkout entity) {
        List<ProductResponseDTO> productDTOs = entity.getProducts().stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getImageURL(),
                        p.getPrice(),
                        p.getCreatedAt(),
                        p.getUpdatedAt()
                )).toList();

        return new CheckoutResponseDTO(
                entity.getId(),
                entity.getUserId(),
                productDTOs,
                entity.getTotalPrice(),
                entity.getIsCompleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
