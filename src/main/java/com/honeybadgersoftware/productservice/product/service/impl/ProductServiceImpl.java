package com.honeybadgersoftware.productservice.product.service.impl;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.product.repository.ProductRepository;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import com.honeybadgersoftware.productservice.utils.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<ProductDto> findById(Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        return product.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return new PageImpl<>(
                products.stream().map(productMapper::toDto).toList(),
                products.getPageable(),
                products.getTotalElements());
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        productRepository.save(productMapper.toEntity(productDto));
        return productDto;
    }

    @Override
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.save(productMapper.toEntity(productDto));
        }
        return Optional.empty();
    }

    @Override
    public int deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return 1;
        }
        return 0;
    }
}
