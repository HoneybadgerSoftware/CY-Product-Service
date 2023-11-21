package com.honeybadgersoftware.productservice.product.model.productexistence;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductExistenceResponse {

    private List<ProductExistenceData> productsExistenceData;
}
