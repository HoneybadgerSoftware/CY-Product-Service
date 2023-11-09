package com.honeybadgersoftware.productservice.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPreUpdateData {

    private Long productId;
    private String productName;
    private String manufacturer;
}
