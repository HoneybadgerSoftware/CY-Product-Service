package com.honeybadgersoftware.productservice.product.model.productupdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePriceData> data;
}
