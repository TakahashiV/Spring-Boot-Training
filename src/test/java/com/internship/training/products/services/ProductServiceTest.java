package com.internship.training.products.services;

import com.internship.training.products.models.dto.PageRequestDTO;
import com.internship.training.products.models.dto.ProductSearchCriteriaDTO;
import com.internship.training.products.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldThrowBadRequestWhenMinPriceIsGreaterThanMaxPrice() {
        ProductService productService = new ProductService(productRepository);
        ProductSearchCriteriaDTO criteria = new ProductSearchCriteriaDTO(
                null,
                null,
                null,
                new BigDecimal("200.00"),
                new BigDecimal("100.00")
        );

        assertThatThrownBy(() -> productService.searchProducts(criteria, new PageRequestDTO(0, 10, null, null)))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    }
}
