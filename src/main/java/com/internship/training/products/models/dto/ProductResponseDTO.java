package com.internship.training.products.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
    String id,
    String name,
    String description,
    String imageURL,
    BigDecimal price,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
