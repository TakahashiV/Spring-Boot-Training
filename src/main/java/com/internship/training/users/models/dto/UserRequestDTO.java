package com.internship.training.users.models.dto;

import java.util.List;

public record UserRequestDTO(
    String name,
    List<AddressDTO> addresses
) {}
