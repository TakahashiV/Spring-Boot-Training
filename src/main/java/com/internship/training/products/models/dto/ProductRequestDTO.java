package com.internship.training.products.models.dto;

import java.math.BigDecimal;

public record ProductRequestDTO(
    String name,
    String description,
    String imageURL,
    BigDecimal price
) {}
