package com.honeybadgersoftware.productservice.product.factory;

import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.utils.factory.ManyToOneFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdatedProductEntityFactory implements ManyToOneFactory<ProductEntity, ProductAveragePriceData> {

    @Override
    public ProductEntity map(ProductEntity product, ProductAveragePriceData productAveragePriceData) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .manufacturer(product.getManufacturer())
                .averagePrice(productAveragePriceData.getAveragePrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
