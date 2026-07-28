package com.internship.training.checkout.consumer;

import com.internship.training.config.KafkaConfig;
import com.internship.training.checkout.models.dto.CheckoutEvent;
import com.internship.training.checkout.services.CheckoutService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckoutConsumer {

    private final CheckoutService checkoutService;

    public CheckoutConsumer(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @KafkaListener(topics = KafkaConfig.CHECKOUT_TOPIC, groupId = "training-group")
    public void consumeCheckoutEvent(CheckoutEvent event) {
        // Consome a mensagem que chega na fila
        System.out.println("Evento de Checkout recebido no Kafka Consumer: " + event);
        
        // Finaliza a compra marcando isCompleted = true no banco de dados
        checkoutService.completeCheckout(event.checkoutId());
    }
}
