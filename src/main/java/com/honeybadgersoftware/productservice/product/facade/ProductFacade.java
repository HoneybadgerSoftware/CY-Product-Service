package com.honeybadgersoftware.productservice.product.facade;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.dto.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.dto.SynchronizeProductsRequest;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import com.honeybadgersoftware.productservice.utils.pagination.ProductPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;

    public Optional<ProductDto> findById(Long id) {
        return productService.findById(id);
    }

    public ProductPage<ProductDto> getProducts(Pageable pageable) {
        return productService.findAll(pageable);
    }

    public ProductDto saveProduct(ProductDto productDto) {
        return productService.saveProduct(productDto);
    }

    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }


    public int deleteProduct(Long id) {
        return productService.deleteById(id);
    }

    public void synchronizeProducts(SynchronizeProductsRequest synchronizeProductsRequest) {
        ProductExistenceResponse productExistenceResponse = productService.checkProductsInDb(synchronizeProductsRequest.getData());

    }

}
