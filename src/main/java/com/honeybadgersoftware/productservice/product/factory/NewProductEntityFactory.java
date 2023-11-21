package com.honeybadgersoftware.productservice.product.factory;

import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.entity.ProductEntity;
import com.honeybadgersoftware.productservice.utils.factory.ManyToOneFactory;
import org.springframework.stereotype.Component;

@Component
public class NewProductEntityFactory implements ManyToOneFactory<ProductEntity, NewProductUpdateData> {


    @Override
    public ProductEntity map(ProductEntity product, NewProductUpdateData newProductUpdateData) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .manufacturer(product.getManufacturer())
                .averagePrice(newProductUpdateData.getAveragePrice())
                .description(newProductUpdateData.getDescription())
                .imageUrl(newProductUpdateData.getUrl())
                .build();
    }
}
