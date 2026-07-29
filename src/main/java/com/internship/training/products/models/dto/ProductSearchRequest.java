package com.internship.training.products.models.dto;

public record ProductSearchRequest(
    ProductSearchCriteriaDTO criteria,
    PageRequestDTO pageRequest
) {}
