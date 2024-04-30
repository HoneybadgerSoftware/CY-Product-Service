package com.honeybadgersoftware.productservice.product.model.productexistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductExistenceData {
    private Long id;
    private boolean existsInDb;
    private String name;
    private String manufacturer;
}
