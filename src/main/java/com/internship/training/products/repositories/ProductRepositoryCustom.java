package com.internship.training.products.repositories;

import com.internship.training.products.models.dto.PageRequestDTO;
import com.internship.training.products.models.dto.ProductSearchCriteriaDTO;
import com.internship.training.products.models.entities.Product;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    Page<Product> findProductsCustom(ProductSearchCriteriaDTO criteria, PageRequestDTO pageRequest);
}
