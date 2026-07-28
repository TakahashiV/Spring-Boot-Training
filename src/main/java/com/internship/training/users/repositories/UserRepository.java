package com.internship.training.users.repositories;

import com.internship.training.users.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // A interface MongoRepository fornece automaticamente os métodos padrão de CRUD:
    // save(), findById(), findAll(), deleteById(), etc.
}
