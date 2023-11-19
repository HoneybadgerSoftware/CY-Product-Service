package com.honeybadgersoftware.productservice.product.service.impl;

import com.honeybadgersoftware.productservice.product.model.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.dto.ProductExistenceData;
import com.honeybadgersoftware.productservice.product.model.dto.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.dto.SimplifiedProductData;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.product.repository.ProductRepository;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import com.honeybadgersoftware.productservice.utils.mapper.ProductMapper;
import com.honeybadgersoftware.productservice.utils.pagination.ProductPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
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
    public ProductPage<ProductDto> findAll(Pageable pageable) {
        org.springframework.data.domain.Page<ProductEntity> products = productRepository.findAll(pageable);
        return new ProductPage<>(
                products.stream().map(productMapper::toDto).toList(),
                products.getPageable().getPageSize(),
                products.getPageable().getPageNumber(),
                products.getTotalPages()

        );

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
            return Optional.of(productDto);
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

    @Override
    public ProductExistenceResponse checkProductsInDb(List<SimplifiedProductData> simplifiedProductData) {

        ArrayList<ProductExistenceData> productsExistenceData = new ArrayList<>();

        simplifiedProductData.forEach(productData -> {
                    Optional<Long> productId = productRepository.findIdByNameAndManufacturer(productData.getProductName(), productData.getManufacturer());
                    if (productId.isEmpty()) {

                        ProductEntity newProduct = productRepository.save(ProductEntity.builder()
                                .name(productData.getProductName())
                                .manufacturer(productData.getManufacturer())
                                .build());

                        productsExistenceData.add(ProductExistenceData.builder()
                                .id(newProduct.getId())
                                .existedInDb(false)
                                .name(newProduct.getName())
                                .manufacturer(newProduct.getManufacturer())
                                .build());
                        return;
                    }
                    productsExistenceData.add(ProductExistenceData.builder()
                            .id(productId.get())
                            .existedInDb(true)
                            .name(productData.getProductName())
                            .manufacturer(productData.getManufacturer())
                            .build());
                }
        );

        return new ProductExistenceResponse(productsExistenceData);
    }

    @Override
    @Transactional
    public void updateNewProducts(List<NewProductUpdateData> productData) {
        List<ProductEntity> productEntities = productRepository.findAllById(getProductIds(productData));

        List<ProductEntity> updatedEntities = productEntities.stream()
                .flatMap(productEntity -> productData.stream()
                        .filter(newData -> newData.getId().equals(productEntity.getId()))
                        .map(newData -> map(productEntity, newData))
                ).collect(Collectors.toList());

        productRepository.saveAll(updatedEntities);
    }


    private List<Long> getProductIds(List<NewProductUpdateData> productData) {
        return productData.stream().map(NewProductUpdateData::getId).collect(Collectors.toList());
    }

    private ProductEntity map(ProductEntity productEntity, NewProductUpdateData newProductUpdateData) {
        return ProductEntity.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .manufacturer(productEntity.getManufacturer())
                .averagePrice(newProductUpdateData.getAveragePrice())
                .description(newProductUpdateData.getDescription())
                .imageUrl(newProductUpdateData.getUrl())
                .build();
    }
}
