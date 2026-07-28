package com.internship.training.checkout.models.dto;

import java.math.BigDecimal;

public record CheckoutEvent(
    String checkoutId,
    String userId,
    BigDecimal totalPrice
) {}
