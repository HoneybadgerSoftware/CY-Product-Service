package com.honeybadgersoftware.productservice.product.controller;

import com.honeybadgersoftware.productservice.product.facade.ProductFacade;
import com.honeybadgersoftware.productservice.product.model.dto.GetProductsFromSpecificShopRequest;
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductsUpdateRequest;
import com.honeybadgersoftware.productservice.product.model.productupdate.UpdateProductsAveragePriceRequest;
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.synchronize.SynchronizeProductsRequest;
import com.honeybadgersoftware.productservice.utils.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Optional<ProductDto> product = productFacade.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    ResponseEntity<Page<ProductDto>> getProducts(
            @PageableDefault(size = 50, page = 0) Pageable pageable) {
        return ResponseEntity.ok(productFacade.getProducts(pageable));
    }

    @GetMapping("/random")
    ResponseEntity<Page<ProductDto>> getProductsFromSpecificShops(
            @RequestBody @Valid GetProductsFromSpecificShopRequest requestedShops
            ) {
        return ResponseEntity.ok(productFacade.getProductsFromSpecificShops(requestedShops));
    }

    @PostMapping
    ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productFacade.saveProduct(productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productFacade.updateProduct(id, productDto).isPresent() ?
                ResponseEntity.ok(productDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productFacade.deleteProduct(id) == 1 ?
                ResponseEntity.status(HttpStatus.OK).body("Succesfully deleted entity with id: " + id)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
    }

    @PostMapping("/synchronize/check")
    ResponseEntity<ProductExistenceResponse> preSynchronizationCheck(@RequestBody SynchronizeProductsRequest synchronizeProductsRequest) {
        return ResponseEntity.ok(productFacade.preSynchronizationCheck(synchronizeProductsRequest));
    }

    @PutMapping("/synchronize/newProducts")
    ResponseEntity<Void> updateNewProducts(
            @RequestBody NewProductsUpdateRequest checkProductsExistenceRequest) {
        productFacade.updateNewProducts(checkProductsExistenceRequest.getData());
        return ResponseEntity.ok().build();
    }


    @PutMapping("/synchronize/existingProducts")
    ResponseEntity<Void> updateExistingProductsAveragePrice(
            @RequestBody UpdateProductsAveragePriceRequest productsAveragePriceRequest) {
        productFacade.updateProductsAveragePrice(productsAveragePriceRequest.getData());
        return ResponseEntity.ok().build();
    }

}
