package com.honeybadgersoftware.productservice.product.client.availability.api;


import com.honeybadgersoftware.productservice.product.client.availability.model.ProductIdsResponse;
import com.honeybadgersoftware.productservice.product.model.dto.GetProductsFromSpecificShopRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "availabilityService", url = "http://localhost:8082/availability")
public interface AvailabilityServiceApi {

    @PostMapping("/check/random")
    @Headers("Content-Type: application/json")
    ResponseEntity<ProductIdsResponse> getRandomProductsByLocation(@RequestBody GetProductsFromSpecificShopRequest getRandomProductsByShops);
}
