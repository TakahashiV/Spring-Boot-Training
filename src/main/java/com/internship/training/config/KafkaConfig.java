package com.internship.training.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String CHECKOUT_TOPIC = "checkout-topic";

    @Bean
    public NewTopic checkoutTopic() {
        // Cria automaticamente o tópico "checkout-topic" no Kafka na inicialização da aplicação
        return TopicBuilder.name(CHECKOUT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
