package com.internship.training.products.repositories;

import com.internship.training.products.models.dto.PageRequestDTO;
import com.internship.training.products.models.dto.ProductSearchCriteriaDTO;
import com.internship.training.products.models.dto.SortOrder;
import com.internship.training.products.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public ProductRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Product> findProductsCustom(ProductSearchCriteriaDTO criteria, PageRequestDTO pageRequest) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // 1. Construção dinâmica dos filtros (Criteria) baseada nos atributos não nulos
        if (criteria != null) {
            if (criteria.name() != null && !criteria.name().isBlank()) {
                // Regex "i" para fazer busca case-insensitive (como o "LIKE" com ignoring case)
                criteriaList.add(Criteria.where("name").regex(criteria.name(), "i"));
            }
            if (criteria.description() != null && !criteria.description().isBlank()) {
                criteriaList.add(Criteria.where("description").regex(criteria.description(), "i"));
            }
            if (criteria.imageURL() != null && !criteria.imageURL().isBlank()) {
                criteriaList.add(Criteria.where("imageURL").regex(criteria.imageURL(), "i"));
            }
            if (criteria.minPrice() != null && criteria.maxPrice() != null) {
                criteriaList.add(Criteria.where("price").gte(criteria.minPrice()).lte(criteria.maxPrice()));
            } else if (criteria.minPrice() != null) {
                criteriaList.add(Criteria.where("price").gte(criteria.minPrice()));
            } else if (criteria.maxPrice() != null) {
                criteriaList.add(Criteria.where("price").lte(criteria.maxPrice()));
            }
        }

        // Adiciona a lista de critérios na query combinando-os com operador AND
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // 2. Conta o total de registros que batem com os filtros ANTES de aplicar limite/paginação
        long total = mongoTemplate.count(query, Product.class);

        // 3. Construção do Pageable do Spring a partir do nosso PageRequestDTO
        int page = (pageRequest != null && pageRequest.pageNum() != null) ? pageRequest.pageNum() : 0;
        int size = (pageRequest != null && pageRequest.pageSize() != null) ? pageRequest.pageSize() : 10;
        
        Pageable pageable;
        if (pageRequest != null && pageRequest.sortBy() != null && !pageRequest.sortBy().isBlank()) {
            Sort.Direction direction = (pageRequest.sortOrder() == SortOrder.DESCENDING) 
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, pageRequest.sortBy()));
        } else {
            pageable = PageRequest.of(page, size);
        }

        // Associa a paginação e ordenação à consulta
        query.with(pageable);

        // 4. Executa a busca paginada
        List<Product> products = mongoTemplate.find(query, Product.class);

        // 5. Retorna o resultado envelopado no PageImpl do Spring
        return new PageImpl<>(products, pageable, total);
    }
}
