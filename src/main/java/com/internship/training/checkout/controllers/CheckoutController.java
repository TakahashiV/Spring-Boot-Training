package com.internship.training.checkout.controllers;

import com.internship.training.checkout.models.dto.CheckoutRequestDTO;
import com.internship.training.checkout.models.dto.CheckoutResponseDTO;
import com.internship.training.checkout.services.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkouts")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    // Iniciar um Checkout (POST)
    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> createCheckout(@RequestBody CheckoutRequestDTO request) {
        return checkoutService.createCheckout(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElse(ResponseEntity.badRequest().build()); // Retorna 400 se o usuário ou produtos forem inválidos
    }

    // Obter todos os Checkouts (GET)
    @GetMapping
    public ResponseEntity<List<CheckoutResponseDTO>> getAllCheckouts() {
        return ResponseEntity.ok(checkoutService.getAllCheckouts());
    }

    // Obter um Checkout por ID (GET com ID)
    @GetMapping("/{id}")
    public ResponseEntity<CheckoutResponseDTO> getCheckoutById(@PathVariable String id) {
        return checkoutService.getCheckoutById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
