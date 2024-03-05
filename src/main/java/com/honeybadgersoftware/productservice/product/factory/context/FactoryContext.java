package com.honeybadgersoftware.productservice.product.factory.context;

import com.honeybadgersoftware.productservice.utils.factory.ManyToOneFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class FactoryContext {

    private final Map<Class<?>, ManyToOneFactory<?, ?>> factories;

    public FactoryContext(Map<Class<?>, ManyToOneFactory<?, ?>> factories) {
        this.factories = factories;
    }

    @SuppressWarnings("unchecked")
    public <T, R> ManyToOneFactory<T, R> getFactory(Class<R> type) {
        return (ManyToOneFactory<T, R>) factories.get(type);
    }
}


