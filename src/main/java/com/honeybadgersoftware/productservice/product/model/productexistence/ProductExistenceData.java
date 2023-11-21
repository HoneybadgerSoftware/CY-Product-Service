package com.honeybadgersoftware.productservice.product.model.productexistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductExistenceData {
    private Long id;
    private boolean existedInDb;
    private String name;
    private String manufacturer;
}
