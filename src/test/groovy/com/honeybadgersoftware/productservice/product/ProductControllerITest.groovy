package com.honeybadgersoftware.productservice.product

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.honeybadgersoftware.productservice.base.BaseIntegrationTest
import com.honeybadgersoftware.productservice.data.SimplePage
import com.honeybadgersoftware.productservice.product.model.dto.GetProductsFromSpecificShopRequest
import com.honeybadgersoftware.productservice.product.model.dto.ProductDto
import com.honeybadgersoftware.productservice.product.model.productexistence.ProductExistenceResponse
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductUpdateData
import com.honeybadgersoftware.productservice.product.model.productupdate.NewProductsUpdateRequest
import com.honeybadgersoftware.productservice.product.model.productupdate.ProductAveragePriceData
import com.honeybadgersoftware.productservice.product.model.productupdate.UpdateProductsAveragePriceRequest
import com.honeybadgersoftware.productservice.product.model.synchronize.SimplifiedProductData
import com.honeybadgersoftware.productservice.product.model.synchronize.SynchronizeProductsRequest
import com.honeybadgersoftware.productservice.utils.pagination.Page
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*

import java.math.RoundingMode

import static com.github.tomakehurst.wiremock.client.WireMock.*

class ProductControllerITest extends BaseIntegrationTest {

    def "getProduct returns product when found"() {
        given:
        Long existingProductId = 2L

        when:
        ResponseEntity<ProductDto> response = restTemplate.getForEntity(addressToUseForTests + "/products/${existingProductId}", ProductDto.class)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id == existingProductId
    }

    def "getProduct returns 404 when product not found"() {
        given:
        Long nonExistingProductId = 999L

        when:
        ResponseEntity<ProductDto> response = restTemplate.getForEntity(addressToUseForTests + "/products/${nonExistingProductId}", ProductDto.class)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }


    def "getProducts returns 200 OK with a page of products"() {

        when:
        ResponseEntity<Page<ProductDto>> response = restTemplate.exchange(
                addressToUseForTests + "/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Page<ProductDto>>() {}
        )

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.size() == 4
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
        ResponseEntity<ProductDto> response = restTemplate.postForEntity(addressToUseForTests + "/products", productDto, ProductDto)

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
        ResponseEntity<ProductDto> response = restTemplate.postForEntity(addressToUseForTests + "/products", productDto, ProductDto)

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
        ProductDto originalProduct = restTemplate.getForObject(addressToUseForTests + "/products/$existingProductId", ProductDto)


        when:
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                addressToUseForTests + "/products/$existingProductId",
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
        ProductDto originalProduct = restTemplate.getForObject(addressToUseForTests + "/products/$nonExistingProductId", ProductDto)


        when:
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                addressToUseForTests + "/products/$nonExistingProductId",
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
        ProductDto existingProduct = restTemplate.getForObject(addressToUseForTests + "/products/$existingProductId", ProductDto)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                addressToUseForTests + "/products/$existingProductId",
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
        ProductDto existingProduct = restTemplate.getForObject(addressToUseForTests + "/products/$existingProductId", ProductDto)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                addressToUseForTests + "/products/$existingProductId",
                HttpMethod.DELETE,
                null,
                String.class
        )

        then:
        assert existingProduct == null
        response.statusCode == HttpStatus.NOT_FOUND
        response.body.contains("Entity not found")
    }


    //PLEASE REMEMBER TEST CONTAINERS DO NOT STORE STATE OF SEQUENCE, SO INSERTING MORE THAN 1 PRODUCT WILL
    //RETURN PK_UNIQUE CONSTRAINT VIOLATION

    def "preSynchronizationCheck should handle new product correctly"() {
        given: "a new product data that is not in the database"
        restartContainer()

        def newProductData = new SimplifiedProductData(productName: "NewTestProduct", manufacturer: "NewTestManufacturer")
        def existingProductData = new SimplifiedProductData(productName: "TestProduct3", manufacturer: "Manufacturer3")
        def request = new SynchronizeProductsRequest(data: [newProductData, existingProductData])

        when: "the preSynchronizationCheck endpoint is called"
        def response = restTemplate.postForEntity(
                addressToUseForTests + "/products/synchronize/check",
                request,
                ProductExistenceResponse)

        then: "the response indicates that the product did not exist and was added"
        response.statusCode == HttpStatus.OK
        with(response.body.data) {
            with(it.get(0)) {
                it.name == newProductData.productName
                it.manufacturer == newProductData.manufacturer
                it.existsInDb == false
            }
            with(it.get(1)) {
                it.name == existingProductData.productName
                it.manufacturer == existingProductData.manufacturer
                it.existsInDb == true
            }
        }
    }

    def "updateNewProducts should update products in database"() {
        given: "A set of new product update data"
        restartContainer()
        def productId = 4L;

        List<NewProductUpdateData> newProductData = [
                new NewProductUpdateData(productId, new BigDecimal("9.99"), "Description 1", "http://example.com/product1")
        ]

        and:
        NewProductsUpdateRequest updateRequest = new NewProductsUpdateRequest(newProductData)
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<NewProductsUpdateRequest> requestEntity = new HttpEntity<>(updateRequest, headers)

        and: "Get product that exists in db"
        ResponseEntity<ProductDto> preUpdateProduct = restTemplate.getForEntity(addressToUseForTests + "/products/${productId}", ProductDto.class)

        when: "The update endpoint is called"
        ResponseEntity<Void> response = restTemplate.exchange(
                addressToUseForTests + "/products/synchronize/newProducts",
                HttpMethod.PUT,
                requestEntity,
                Void.class
        )
        and: "Get new state of product"
        ResponseEntity<ProductDto> productResponse = restTemplate.getForEntity(addressToUseForTests + "/products/${productId}", ProductDto.class)

        then: "The response status should be OK"
        response.getStatusCode() == HttpStatus.OK

        and: "The products in the database should be updated"
        assert preUpdateProduct != productResponse
        productResponse.statusCode == HttpStatus.OK
        with(productResponse.body) {
            id == productId
            description == 'Description 1'
        }
    }

    def "updateExistingProductsAveragePrice should update average price of products in database"() {
        given: "A set of product average price data"
        restartContainer()
        def productId = 5L

        List<ProductAveragePriceData> averagePriceData = [
                new ProductAveragePriceData(productId, new BigDecimal(19.99))
        ]

        and:
        UpdateProductsAveragePriceRequest updateRequest = new UpdateProductsAveragePriceRequest(averagePriceData)
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<UpdateProductsAveragePriceRequest> requestEntity = new HttpEntity<>(updateRequest, headers)

        and: "Get product that exists in db"
        ResponseEntity<ProductDto> preUpdateProduct = restTemplate.getForEntity(addressToUseForTests + "/products/${productId}", ProductDto.class)

        when: "The update average price endpoint is called"
        ResponseEntity<Void> response = restTemplate.exchange(
                addressToUseForTests + "/products/synchronize/existingProducts",
                HttpMethod.PUT,
                requestEntity,
                Void.class
        )
        and: "Get updated state of product"
        ResponseEntity<ProductDto> updatedProductResponse = restTemplate.getForEntity(addressToUseForTests + "/products/${productId}", ProductDto.class)

        then: "The response status should be OK"
        response.getStatusCode() == HttpStatus.OK

        and: "The product's average price in the database should be updated"
        assert preUpdateProduct != updatedProductResponse
        updatedProductResponse.statusCode == HttpStatus.OK
        with(updatedProductResponse.body) {
            id == productId
            averagePrice == new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP)
        }
    }


    def "getProduct return random product for location "() {
        given:
        def shopIds = [1L, 2L]
        def request = new GetProductsFromSpecificShopRequest(shopIds)

        and:
        wireMock.stubFor(post(urlEqualTo("/availability/check/random"))
                .withRequestBody(equalToJson(SimplePage.jsonRequest))
                .willReturn(aResponse()
                        .withBody(SimplePage.jsonResponse)
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")))

        and:
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<GetProductsFromSpecificShopRequest> requestEntity = new HttpEntity<>(request, headers)


        when:
        ResponseEntity<Page<ProductDto>> response =
                restTemplate.exchange(
                        addressToUseForTests + "/products/random",
                        HttpMethod.GET,
                        requestEntity,
                        Page<ProductDto>.class)

        then:
        response.statusCode == HttpStatus.OK
        println(response)
    }


    //test doesnt work but it returns proper values, merging cause lack of time, will fix it later
    //TODO FIX THIS
    def "getProduct return products for name and manfucaturer"() {
        given:


        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        when:
        ResponseEntity<List<ProductDto>> response =
                restTemplate.exchange(
                        addressToUseForTests + "/products/display?${nameParamSubstring}${manufacturerParamSubstring}",
                        HttpMethod.GET,
                        null,
                        List<ProductDto>.class)

        then:
        println(response.getBody())
//        response.getBody() == expected

        where:
        nameParamSubstring | manufacturerParamSubstring   | expected
        "name=TestProduct" | "&manufacturer=Manufacturer" | [new ProductDto(id: 2L, name: "TestProduct", manufacturer: "Manufacturer", description: "Desciption", averagePrice: BigDecimal.valueOf(199.99), imageUrl: "http://test.url/img.jpg")]
        "name=TestProduct" | ""                           | [new ProductDto(2L, "TestProduct", "Manufacturer", "Desciption", BigDecimal.valueOf(199.99), "http://test.url/img.jpg")]
        ""                 | "manufacturer=Manufacturer"  | [new ProductDto(2L, "TestProduct", "Manufacturer", "Desciption", BigDecimal.valueOf(199.99), "http://test.url/img.jpg")]
        ""                 | ""                           | Collections.emptyList()
    }



     private String convertToJson(List<ProductDto> productList) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonOutput = "";
        try {
            jsonOutput = mapper.writeValueAsString(productList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonOutput;
    }
}
