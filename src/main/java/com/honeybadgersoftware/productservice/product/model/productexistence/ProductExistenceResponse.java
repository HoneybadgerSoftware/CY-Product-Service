package com.honeybadgersoftware.productservice.product.model.productexistence;


import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductExistenceResponse {

    private List<ProductExistenceData> data;
}
