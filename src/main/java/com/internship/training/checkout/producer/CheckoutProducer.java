package com.internship.training.checkout.producer;

import com.internship.training.config.KafkaConfig;
import com.internship.training.checkout.models.dto.CheckoutEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckoutProducer {

    private final KafkaTemplate<String, CheckoutEvent> kafkaTemplate;

    public CheckoutProducer(KafkaTemplate<String, CheckoutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCheckoutEvent(CheckoutEvent event) {
        // Envia o evento de checkout para o tópico do Kafka.
        // A chave da mensagem é o ID do checkout para garantir que eventos de um mesmo checkout fiquem na mesma partição.
        kafkaTemplate.send(KafkaConfig.CHECKOUT_TOPIC, event.checkoutId(), event);
        System.out.println("Evento de Checkout enviado para o Kafka: " + event);
    }
}
