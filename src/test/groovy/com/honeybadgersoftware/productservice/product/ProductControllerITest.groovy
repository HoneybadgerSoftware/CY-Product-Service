package com.honeybadgersoftware.productservice.product

import com.honeybadgersoftware.productservice.base.BaseIntegrationTest
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto
import com.honeybadgersoftware.productservice.utils.pagination.ProductPage
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ProductControllerITest extends BaseIntegrationTest {

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


    def "getProducts returns 200 OK with a page of products"() {

        when:
        ResponseEntity<ProductPage<ProductDto>> response = restTemplate.exchange(
                "http://localhost:${port}/product",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ProductPage<ProductDto>>() {}
        )

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.size() == 2
    }

    def "saveProduct return 201 Created and creates a new product"() {
        given:
        ProductDto productDto = new ProductDto(
                name: 'New Product',
                manufacturer: 'New Manufacturer',
                description: 'New Description',
                averagePrice: new BigDecimal('19.99'),
                imageUrl: 'http://new.url/img.jpg')

        when:
        ResponseEntity<ProductDto> response = restTemplate.postForEntity("http://localhost:$port/product", productDto, ProductDto)

        then:
        with(response) {
            statusCode == HttpStatus.CREATED
            with(body) {
                name == productDto.name
                manufacturer == productDto.manufacturer
                description == productDto.description
                averagePrice == productDto.averagePrice
                imageUrl == productDto.imageUrl
            }
        }

    }

    def "saveProduct returns 404 when bad request body is being passed"() {
        given:
        ProductDto productDto = new ProductDto()

        when:
        ResponseEntity<ProductDto> response = restTemplate.postForEntity("http://localhost:$port/product", productDto, ProductDto)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        response.body == null
    }


    def "updateProduct returns 200, updates an existing product and returns the updated product"() {
        given:
        Long existingProductId = 2L

        and:
        ProductDto updateInfo = new ProductDto(
                id: 2L,
                name: 'Updated Product',
                manufacturer: 'Updated Manufacturer',
                description: 'Updated Description',
                averagePrice: new BigDecimal('29.99'),
                imageUrl: 'http://updated.url/img.jpg'
        )
        HttpEntity<ProductDto> requestEntity = new HttpEntity<>(updateInfo)


        and: 'Check if product exists in db'
        ProductDto originalProduct = restTemplate.getForObject("http://localhost:$port/product/$existingProductId", ProductDto)


        when:
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "http://localhost:$port/product/$existingProductId",
                HttpMethod.PUT,
                requestEntity,
                ProductDto
        )

        then:
        assert originalProduct != null

        with(response) {
            statusCode == HttpStatus.OK
            with(body) {
                name == updateInfo.name
                manufacturer == updateInfo.manufacturer
                description == updateInfo.description
                averagePrice == updateInfo.averagePrice
                imageUrl == updateInfo.imageUrl
            }
        }
    }


    def "updateProduct returns 404 when product doesnt exists"() {
        given:
        Long nonExistingProductId = 69L

        and:
        ProductDto updateInfo = new ProductDto(
                id: 69L,
                name: 'Updated Product',
                manufacturer: 'Updated Manufacturer',
                description: 'Updated Description',
                averagePrice: new BigDecimal('29.99'),
                imageUrl: 'http://updated.url/img.jpg'
        )
        HttpEntity<ProductDto> requestEntity = new HttpEntity<>(updateInfo)


        and: 'Check if product exists in db'
        ProductDto originalProduct = restTemplate.getForObject("http://localhost:$port/product/$nonExistingProductId", ProductDto)


        when:
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "http://localhost:$port/product/$nonExistingProductId",
                HttpMethod.PUT,
                requestEntity,
                ProductDto
        )

        then:

        assert originalProduct == null
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "deleteProduct returns 200 and successfully deletes an existing product"() {
        given:
        Long existingProductId = 2L

        and: 'ensure product is in db'
        ProductDto existingProduct = restTemplate.getForObject("http://localhost:$port/product/$existingProductId", ProductDto)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:$port/product/$existingProductId",
                HttpMethod.DELETE,
                null,
                String.class
        )

        then:
        assert existingProduct != null
        response.statusCode == HttpStatus.OK
        response.body.contains("Succesfully deleted entity with id: $existingProductId")
    }

    def "deleteProduct returns NOT_FOUND when trying to delete a non-existing product"() {
        given:
        Long existingProductId = 999L

        and: 'ensure product is not in db'
        ProductDto existingProduct = restTemplate.getForObject("http://localhost:$port/product/$existingProductId", ProductDto)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:$port/product/$existingProductId",
                HttpMethod.DELETE,
                null,
                String.class
        )

        then:
        assert existingProduct == null
        response.statusCode == HttpStatus.NOT_FOUND
        response.body.contains("Entity not found")
    }
}
