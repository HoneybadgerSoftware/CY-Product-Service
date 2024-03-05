package com.honeybadgersoftware.productservice.product.client.availability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductIdsResponse {
    private List<Long> data;
}
