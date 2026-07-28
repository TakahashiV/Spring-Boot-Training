package com.internship.training.users.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDTO(
    String id,
    String name,
    List<AddressDTO> addresses,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
