package com.honeybadgersoftware.productservice.product.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductExistenceResponse {

    private List<Long> existingProductsIds;
    private List<Long> newProductsIds;
}
