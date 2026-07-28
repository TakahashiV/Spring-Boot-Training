package com.internship.training.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Esta classe ativa a auditoria automática do MongoDB.
    // Isso fará com que as anotações @CreatedDate e @LastModifiedDate
    // funcionem e preencham as datas de criação/atualização automaticamente.
}
