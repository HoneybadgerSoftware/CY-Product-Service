package com.honeybadgersoftware.productservice.product.controller;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Optional<ProductDto> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    ResponseEntity<Page<ProductDto>> getProducts(
            @PageableDefault(size = 50, page = 0) Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @PostMapping
    ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto).isPresent() ?
                ResponseEntity.ok(productDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteById(id) == 1 ?
                ResponseEntity.status(HttpStatus.OK).body("Succesfully deleted entity with id: " + id)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
    }


}
