package com.honeybadgersoftware.productservice.product.service;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.dto.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.dto.SimplifiedProductData;
import com.honeybadgersoftware.productservice.utils.pagination.ProductPage;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> findById(Long id);

    ProductPage<ProductDto> findAll(Pageable pageable);

    ProductDto saveProduct(ProductDto productDto);

    Optional<ProductDto> updateProduct(Long id, ProductDto productDto);

    int deleteById(Long id);

    ProductExistenceResponse checkProductsInDb(List<SimplifiedProductData> simplifiedProductData);

}
