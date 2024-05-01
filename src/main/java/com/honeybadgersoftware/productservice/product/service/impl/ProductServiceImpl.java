package com.honeybadgersoftware.productservice.product.service.impl;

import com.honeybadgersoftware.productservice.product.factory.context.FactoryContext;
import com.honeybadgersoftware.productservice.product.factory.one.ProductEntityToDtoFactory;
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceData;
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceResponse;
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData;
import com.honeybadgersoftware.productservice.product.model.synchronize.SimplifiedProductData;
import com.honeybadgersoftware.productservice.product.repository.ProductRepository;
import com.honeybadgersoftware.productservice.product.service.ProductService;
import com.honeybadgersoftware.productservice.utils.factory.ManyToOneFactory;
import com.honeybadgersoftware.productservice.utils.mapper.ProductMapper;
import com.honeybadgersoftware.productservice.utils.pagination.Page;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductEntityToDtoFactory productEntityToDtoFactory;
    private final FactoryContext twoToFactoryContext;

    @Override
    public Optional<ProductDto> findById(Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        return product.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        org.springframework.data.domain.Page<ProductEntity> products = productRepository.findAll(pageable);
        return new Page<>(
                productEntityToDtoFactory.map(products.toList()),
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
                    Optional<Long> productId = productRepository.findIdByNameAndManufacturer(
                            productData.getProductName(),
                            productData.getManufacturer());
                    if (productId.isEmpty()) {

                        ProductEntity newProduct = productRepository.save(ProductEntity.builder()
                                .name(productData.getProductName())
                                .manufacturer(productData.getManufacturer())
                                .build());

                        productsExistenceData.add(buildNewProductExistenceData(newProduct));
                        return;
                    }
                    productsExistenceData.add(buildExistingProductExistenceData(productId.get(), productData));
                }
        );
        return new ProductExistenceResponse(productsExistenceData);
    }

    @Override
    @Transactional
    public void updateNewProducts(List<NewProductUpdateData> productData) {
        List<ProductEntity> productEntities = productRepository.findAllById(getProductIds(productData));
        ManyToOneFactory<ProductEntity, NewProductUpdateData> factory =
                twoToFactoryContext.getFactory(NewProductUpdateData.class);

        List<ProductEntity> updatedEntities = productEntities.stream()
                .flatMap(productEntity -> productData.stream()
                        .filter(newData -> newData.getId().equals(productEntity.getId()))
                        .map(newData -> factory.map(productEntity, newData))
                ).collect(Collectors.toList()); //NOSONAR

        productRepository.saveAll(updatedEntities);
    }

    @Override
    public void updateProductsAveragePrice(List<ProductAveragePriceData> data) {
        ManyToOneFactory<ProductEntity, ProductAveragePriceData> factory =
                twoToFactoryContext.getFactory(ProductAveragePriceData.class);

        data.forEach(productAveragePriceData -> {
            Optional<ProductEntity> productEntity = productRepository.findById(productAveragePriceData.getProductId());
            if (productEntity.isEmpty()) {
                throw new EntityNotFoundException("Did not found entity with id:" + productAveragePriceData.getProductId());
            }
            ProductEntity product = productEntity.get();
            productRepository.save(factory.map(product, productAveragePriceData));
        });
    }


    @Override
    public Page<ProductDto> getRandomProductsFromSpecificShops(List<Long> productIds) {
        return pageBuilder(productRepository.findAllById(productIds));
    }

    @Override
    public List<ProductDto> findProductByNameOrManufacturer(String name, String manufacturer) {

        List<ProductEntity> allProducts = productRepository.findAllByNameOrManufacturer(name, manufacturer);

        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return allProducts.stream().map(productMapper::toDto).toList();
    }

    private Page<ProductDto> pageBuilder(List<ProductEntity> entities) {
        return entities.isEmpty() ?
                Page.<ProductDto>builder()
                        .content(Collections.emptyList())
                        .build() : Page.<ProductDto>builder()
                .content(productEntityToDtoFactory.map(entities))
                .build();
    }

    private List<Long> getProductIds(List<NewProductUpdateData> productData) {
        return productData.stream().map(NewProductUpdateData::getId).toList();
    }

    private ProductExistenceData buildNewProductExistenceData(ProductEntity newProduct) {
        return ProductExistenceData.builder()
                .id(newProduct.getId())
                .existsInDb(false)
                .name(newProduct.getName())
                .manufacturer(newProduct.getManufacturer())
                .build();
    }

    private ProductExistenceData buildExistingProductExistenceData(
            Long id,
            SimplifiedProductData productData) {
        return ProductExistenceData.builder()
                .id(id)
                .existsInDb(true)
                .name(productData.getProductName())
                .manufacturer(productData.getManufacturer())
                .build();
    }
}
