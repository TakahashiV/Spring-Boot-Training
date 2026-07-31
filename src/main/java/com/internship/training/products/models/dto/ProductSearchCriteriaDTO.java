package com.internship.training.products.models.dto;

import java.math.BigDecimal;

public record ProductSearchCriteriaDTO(
    String name,
    String description,
    String imageURL,
    BigDecimal minPrice,
    BigDecimal maxPrice
) {}
