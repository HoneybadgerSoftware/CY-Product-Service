package com.honeybadgersoftware.productservice.product.factory.one;

import com.honeybadgersoftware.productservice.product.model.dto.ProductDto;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.utils.factory.OneToOneFactory;
import com.honeybadgersoftware.productservice.utils.mapper.ProductMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@DependsOn("productMapperImpl")
@Component
@RequiredArgsConstructor
public class ProductEntityToDtoFactory implements OneToOneFactory<ProductEntity, ProductDto> {

    private final ProductMapperImpl productMapper;

    @Override
    public ProductDto map(ProductEntity productEntity) {
        return productMapper.toDto(productEntity);
    }
}
