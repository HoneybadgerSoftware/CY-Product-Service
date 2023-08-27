package com.honeybadgersoftware.productservice.product.service;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> findById(Long id);

    Page<ProductDto> findAll(Pageable pageable);

    ProductDto saveProduct(ProductDto productDto);

    Optional<ProductDto> updateProduct(Long id, ProductDto productDto);

    int deleteById(Long id);

}
