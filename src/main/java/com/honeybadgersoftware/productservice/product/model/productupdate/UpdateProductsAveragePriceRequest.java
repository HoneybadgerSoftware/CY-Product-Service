package com.honeybadgersoftware.productservice.product.model.productupdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePriceData> data;
}
