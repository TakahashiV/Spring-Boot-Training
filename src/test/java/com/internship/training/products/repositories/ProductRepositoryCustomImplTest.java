package com.internship.training.products.repositories;

import com.internship.training.products.models.dto.PageRequestDTO;
import com.internship.training.products.models.dto.ProductSearchCriteriaDTO;
import com.internship.training.products.models.entities.Product;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryCustomImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    private ProductRepositoryCustomImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProductRepositoryCustomImpl(mongoTemplate);
        when(mongoTemplate.count(any(Query.class), eq(Product.class))).thenReturn(0L);
        when(mongoTemplate.find(any(Query.class), eq(Product.class))).thenReturn(Collections.emptyList());
    }

    @Test
    void shouldApplyMinPriceOnlyFilter() {
        ProductSearchCriteriaDTO criteria = new ProductSearchCriteriaDTO(null, null, null, new BigDecimal("100.00"), null);

        repository.findProductsCustom(criteria, new PageRequestDTO(0, 10, null, null));

        Document queryObject = captureQueryObject();
        assertThat(queryObject.toJson()).contains("$gte");
        assertThat(queryObject.toJson()).doesNotContain("$lte");
    }

    @Test
    void shouldApplyMaxPriceOnlyFilter() {
        ProductSearchCriteriaDTO criteria = new ProductSearchCriteriaDTO(null, null, null, null, new BigDecimal("500.00"));

        repository.findProductsCustom(criteria, new PageRequestDTO(0, 10, null, null));

        Document queryObject = captureQueryObject();
        assertThat(queryObject.toJson()).contains("$lte");
        assertThat(queryObject.toJson()).doesNotContain("$gte");
    }

    @Test
    void shouldApplyPriceRangeFilterWhenMinAndMaxProvided() {
        ProductSearchCriteriaDTO criteria = new ProductSearchCriteriaDTO(null, null, null, new BigDecimal("100.00"), new BigDecimal("500.00"));

        repository.findProductsCustom(criteria, new PageRequestDTO(0, 10, null, null));

        Document queryObject = captureQueryObject();
        assertThat(queryObject.toJson()).contains("$gte");
        assertThat(queryObject.toJson()).contains("$lte");
    }

    @Test
    void shouldNotApplyPriceFilterWhenRangeNotProvided() {
        ProductSearchCriteriaDTO criteria = new ProductSearchCriteriaDTO(null, null, null, null, null);

        repository.findProductsCustom(criteria, new PageRequestDTO(0, 10, null, null));

        Document queryObject = captureQueryObject();
        assertThat(queryObject.toJson()).doesNotContain("price");
    }

    private Document captureQueryObject() {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).count(queryCaptor.capture(), eq(Product.class));
        return queryCaptor.getValue().getQueryObject();
    }
}
