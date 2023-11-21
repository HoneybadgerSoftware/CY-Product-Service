package com.honeybadgersoftware.productservice.product.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductAveragePriceData {

    private Long productId;
    private BigDecimal averagePrice;
}
