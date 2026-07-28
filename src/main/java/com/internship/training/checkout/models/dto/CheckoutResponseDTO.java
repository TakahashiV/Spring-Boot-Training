package com.internship.training.checkout.models.dto;

import com.internship.training.products.models.dto.ProductResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CheckoutResponseDTO(
    String id,
    String userId,
    List<ProductResponseDTO> products,
    BigDecimal totalPrice,
    Boolean isCompleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
