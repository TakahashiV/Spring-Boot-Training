package com.internship.training.checkout.models.dto;

import java.util.List;

public record CheckoutRequestDTO(
    String userId,
    List<String> productIds
) {}
