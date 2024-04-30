package com.honeybadgersoftware.productservice.product.service;

import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData;
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.synchronize.SimplifiedProductData;
import com.honeybadgersoftware.productservice.utils.pagination.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> findById(Long id);

    Page<ProductDto> findAll(Pageable pageable);

    ProductDto saveProduct(ProductDto productDto);

    Optional<ProductDto> updateProduct(Long id, ProductDto productDto);

    List<ProductDto> findProductByNameOrManufacturer(String name, String manufacturer);

    int deleteById(Long id);

    ProductExistenceResponse checkProductsInDb(List<SimplifiedProductData> simplifiedProductData);

    void updateNewProducts(List<NewProductUpdateData> productData);

    void updateProductsAveragePrice(List<ProductAveragePriceData> data);

    Page<ProductDto> getRandomProductsFromSpecificShops(List<Long> productIds);
}
