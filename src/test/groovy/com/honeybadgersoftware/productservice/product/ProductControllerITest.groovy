package com.honeybadgersoftware.productservice.product

import com.honeybadgersoftware.productservice.base.BaseIntegrationTest
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ProductControllerITest extends BaseIntegrationTest{

    def "getProduct returns product when found"() {
        given:
        Long existingProductId = 2L

        when:
        ResponseEntity<ProductDto> response = restTemplate.getForEntity(addressToUseForTests + "/product/${existingProductId}", ProductDto.class)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id == existingProductId
    }

    def "getProduct returns 404 when product not found"() {
        given:
        Long nonExistingProductId = 999L

        when:
        ResponseEntity<ProductDto> response = restTemplate.getForEntity("http://localhost:${port}/product/${nonExistingProductId}", ProductDto.class)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }
}
