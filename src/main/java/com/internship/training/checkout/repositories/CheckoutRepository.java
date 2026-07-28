package com.internship.training.checkout.repositories;

import com.internship.training.checkout.models.entities.Checkout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends MongoRepository<Checkout, String> {
}
