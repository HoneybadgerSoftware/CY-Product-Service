package com.honeybadgersoftware.productservice.base

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
abstract class BaseIntegrationTest extends Specification {

    @LocalServerPort
    protected int port

    @Shared
    protected String addressToUseForTests

    @Autowired
    protected TestRestTemplate restTemplate

    @Container
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.3")

    WireMockServer wireMock = new WireMockServer(8081)


    def setup() {
        postgreSQLContainer.start()
        addressToUseForTests = 'http://localhost:' + port
        wireMock.start()
    }

    def cleanup(){
        postgreSQLContainer.stop()
        wireMock.stop()
    }

    protected void restartContainer() {
        if (postgreSQLContainer.isRunning()) {
            postgreSQLContainer.stop()
        }
        postgreSQLContainer.start()
    }
}
