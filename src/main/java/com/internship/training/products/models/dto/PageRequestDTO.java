package com.internship.training.products.models.dto;

public record PageRequestDTO(
    Integer pageNum,
    Integer pageSize,
    SortOrder sortOrder,
    String sortBy
) {}
