package com.honeybadgersoftware.productservice.product.model.synchronize;

import lombok.Data;

import java.util.List;

@Data
public class SynchronizeProductsRequest {
    private List<SimplifiedProductData> data;
}
