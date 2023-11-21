package com.honeybadgersoftware.productservice.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePriceData> data;
}
