package com.honeybadgersoftware.productservice.product.model.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private String manufacturer;
    private String description;

    private BigDecimal averagePrice;
    private String imageUrl;
}
