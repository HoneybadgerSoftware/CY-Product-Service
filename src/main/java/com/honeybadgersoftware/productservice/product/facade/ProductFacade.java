package com.honeybadgersoftware.productservice.product.facade;

import com.honeybadgersoftware.productservice.product.client.availability.api.AvailabilityServiceApi;
import com.honeybadgersoftware.productservice.product.client.availability.model.ProductIdsResponse;
import com.honeybadgersoftware.productservice.product.model.dto.GetProductsFromSpecificShopRequest;
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData;
import com.honeybadgersoftware.productservice.product.model.synchronize.SynchronizeProductsRequest;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import com.honeybadgersoftware.productservice.utils.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final AvailabilityServiceApi availabilityServiceApi;

    public Optional<ProductDto> findById(Long id) {
        return productService.findById(id);
    }

    public Page<ProductDto> getProducts(Pageable pageable) {
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

    public ProductExistenceResponse preSynchronizationCheck(SynchronizeProductsRequest synchronizeProductsRequest) {
        return productService.checkProductsInDb(synchronizeProductsRequest.getData());
    }

    public void updateNewProducts(List<NewProductUpdateData> data) {
        productService.updateNewProducts(data);
    }

    public void updateProductsAveragePrice(List<ProductAveragePriceData> data) {
        productService.updateProductsAveragePrice(data);
    }

    public Page<ProductDto> getProductsFromSpecificShops(GetProductsFromSpecificShopRequest requestedShops) {
        ResponseEntity<ProductIdsResponse> randomProductsByLocation = availabilityServiceApi.getRandomProductsByLocation(requestedShops);

        List<Long> productIds = randomProductsByLocation.getBody().getData();
        if (productIds.isEmpty()) {
            return Page.<ProductDto>builder()
                    .content(Collections.emptyList())
                    .build();
        }
        return productService.getRandomProductsFromSpecificShops(productIds);
    }

    public List<ProductDto> findProductsByNameOrManufacturer(Optional<String> name, Optional<String> manufacturer) {
        return productService.findProductByNameOrManufacturer(name.orElse(null), manufacturer.orElse(null));
    }
}
