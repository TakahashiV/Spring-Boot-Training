package com.internship.training.products.services;

import com.internship.training.products.models.dto.ProductRequestDTO;
import com.internship.training.products.models.dto.ProductResponseDTO;
import com.internship.training.products.models.entities.Product;
import com.internship.training.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Criar Produto
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = convertToEntity(request);
        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    // Buscar todos os Produtos
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Buscar Produto por ID
    public Optional<ProductResponseDTO> getProductById(String id) {
        return productRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    // Atualizar Produto
    public Optional<ProductResponseDTO> updateProduct(String id, ProductRequestDTO request) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(request.name());
            existingProduct.setDescription(request.description());
            existingProduct.setImageURL(request.imageURL());
            existingProduct.setPrice(request.price());
            
            Product updatedProduct = productRepository.save(existingProduct);
            return convertToResponseDTO(updatedProduct);
        });
    }

    // Deletar Produto
    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Métodos de Mapeamento (Mappers) ---

    private Product convertToEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setImageURL(dto.imageURL());
        product.setPrice(dto.price());
        return product;
    }

    private ProductResponseDTO convertToResponseDTO(Product entity) {
        return new ProductResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageURL(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
