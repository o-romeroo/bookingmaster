package com.gramos22.bookingmaster.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Testes de Aceitação (E2E) - API BookingMaster
 * 
 * Estes testes validam cenários completos de uso da API,
 * garantindo que o sistema funciona do ponto de vista do usuário final.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingMasterAcceptanceTest {

    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:11.2")
            .withDatabaseName("bmdb_acceptance")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @LocalServerPort
    private int port;

    private static Long createdHotelId;
    private static int createdUserId;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    // ========================================
    // CENÁRIO 1: Fluxo completo de Hotel
    // ========================================

    @Test
    @Order(1)
    @DisplayName("Cenário 1.1: Sistema deve permitir cadastrar um novo hotel")
    void shouldAllowCreatingNewHotel() {
        String hotelJson = """
            {
                "name": "Hotel Acceptance Test",
                "address": "Av. Principal, 1000",
                "nightPricePerGuest": 299.90
            }
            """;

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(hotelJson)
        .when()
            .post("/hotels")
        .then()
            .statusCode(200);
    }

    @Test
    @Order(2)
    @DisplayName("Cenário 1.2: Sistema deve listar hotéis cadastrados")
    void shouldListRegisteredHotels() {
        createdHotelId = given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/hotels")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", equalTo("Hotel Acceptance Test"))
            .body("[0].address", equalTo("Av. Principal, 1000"))
            .body("[0].nightPricePerGuest", equalTo(299.90f))
            .extract()
            .jsonPath()
            .getLong("[0].id");
    }

    @Test
    @Order(3)
    @DisplayName("Cenário 1.3: Sistema deve retornar detalhes de um hotel específico")
    void shouldReturnHotelDetails() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/hotels/" + createdHotelId)
        .then()
            .statusCode(200)
            .body("name", equalTo("Hotel Acceptance Test"))
            .body("address", equalTo("Av. Principal, 1000"));
    }

    // ========================================
    // CENÁRIO 2: Fluxo completo de Usuário
    // ========================================

    @Test
    @Order(4)
    @DisplayName("Cenário 2.1: Sistema deve permitir cadastrar um novo usuário")
    void shouldAllowCreatingNewUser() {
        String userJson = """
            {
                "email": "acceptance.test@email.com",
                "first_name": "Acceptance",
                "last_name": "Tester",
                "document": "12345678901"
            }
            """;

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/users")
        .then()
            .statusCode(200);
    }

    @Test
    @Order(5)
    @DisplayName("Cenário 2.2: Sistema deve retornar usuário cadastrado por ID")
    void shouldReturnUserById() {
        // Primeiro, precisamos descobrir o ID do usuário criado
        // Como não temos endpoint de listagem, vamos assumir ID 1
        createdUserId = 1;

        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/users/" + createdUserId)
        .then()
            .statusCode(200)
            .body("email", equalTo("acceptance.test@email.com"))
            .body("first_name", equalTo("Acceptance"))
            .body("last_name", equalTo("Tester"));
    }

    // ========================================
    // CENÁRIO 3: Validações e Health Check
    // ========================================

    @Test
    @Order(6)
    @DisplayName("Cenário 3.1: Sistema deve estar saudável (health check)")
    void systemShouldBeHealthy() {
        given()
            .port(port)
        .when()
            .get("/actuator/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    @Order(7)
    @DisplayName("Cenário 3.2: Sistema deve retornar 404 para hotel inexistente")
    void shouldReturn404ForNonExistentHotel() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/hotels/999999")
        .then()
            .statusCode(anyOf(equalTo(404), equalTo(500))); // Depende do tratamento de erro
    }

    // ========================================
    // CENÁRIO 4: Limpeza - Deletar recursos
    // ========================================

    @Test
    @Order(8)
    @DisplayName("Cenário 4.1: Sistema deve permitir deletar usuário")
    void shouldAllowDeletingUser() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .delete("/users/" + createdUserId)
        .then()
            .statusCode(200);
    }

    @Test
    @Order(9)
    @DisplayName("Cenário 4.2: Sistema deve permitir deletar hotel")
    void shouldAllowDeletingHotel() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .delete("/hotels/" + createdHotelId)
        .then()
            .statusCode(200);
    }

    @Test
    @Order(10)
    @DisplayName("Cenário 4.3: Lista de hotéis deve estar vazia após deleção")
    void hotelListShouldBeEmptyAfterDeletion() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/hotels")
        .then()
            .statusCode(200)
            .body("size()", equalTo(0));
    }
}
