package com.honeybadgersoftware.productservice.product.factory.configuration;

import com.honeybadgersoftware.productservice.product.factory.many.NewProductEntityFactory;
import com.honeybadgersoftware.productservice.product.factory.many.UpdatedProductEntityFactory;
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData;
import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData;
import com.honeybadgersoftware.productservice.utils.factory.ManyToOneFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FactoryConfiguration {

    private final NewProductEntityFactory newProductEntityFactory;
    private final UpdatedProductEntityFactory updatedProductEntityFactory;

    @Bean
    @DependsOn(value = {"newProductEntityFactory", "updatedProductEntityFactory"})
    public Map<Class<?>, ManyToOneFactory<?, ?>> twoToOneFactoryMap() { //NOSONAR
        return Map.of(
                NewProductUpdateData.class, newProductEntityFactory,
                ProductAveragePriceData.class, updatedProductEntityFactory);
    }
}
