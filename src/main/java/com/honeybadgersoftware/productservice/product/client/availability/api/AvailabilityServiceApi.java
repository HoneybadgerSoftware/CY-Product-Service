package com.honeybadgersoftware.productservice.product.client.availability.api;


import com.honeybadgersoftware.productservice.product.client.availability.model.ProductIdsResponse;
import com.honeybadgersoftware.productservice.product.model.dto.GetProductsFromSpecificShopRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "availabilityService", url = "http://localhost:8081/availability")
public interface AvailabilityServiceApi {

    @PostMapping("/check/random")
    ResponseEntity<ProductIdsResponse> getRandomProductsByLocation(@RequestBody GetProductsFromSpecificShopRequest getRandomProductsByShops);
}
