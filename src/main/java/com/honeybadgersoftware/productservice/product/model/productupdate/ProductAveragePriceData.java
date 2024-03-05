package com.honeybadgersoftware.productservice.product.model.productupdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAveragePriceData {

    private Long productId;
    private BigDecimal averagePrice;
}
