package com.gramos22.bookingmaster;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Testes de Aceitação E2E (End-to-End) para a API BookingMaster.
 * 
 * Estes testes validam os cenários de negócio completos da aplicação,
 * executando contra uma instância real da API em ambiente Docker.
 * 
 * Utilizados na Etapa de Teste de Aceitação do pipeline CI/CD.
 * 
 * Endpoints da API:
 * - /hotels (CRUD de hotéis)
 * - /users (CRUD de usuários)
 * - /bookings (CRUD de reservas)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingMasterAcceptanceTest {

    private static Long createdHotelId;
    private static Integer createdUserId;
    private static Long createdBookingId;

    @BeforeAll
    void setup() {
        // Porta configurável via propriedade do sistema ou variável de ambiente
        String port = System.getProperty("test.server.port", 
                       System.getenv().getOrDefault("TEST_SERVER_PORT", "8090"));
        String host = System.getProperty("test.server.host", 
                       System.getenv().getOrDefault("TEST_SERVER_HOST", "localhost"));
        
        RestAssured.baseURI = "http://" + host;
        RestAssured.port = Integer.parseInt(port);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        System.out.println("🧪 Testes de Aceitação - Conectando em: " + RestAssured.baseURI + ":" + RestAssured.port);
    }

    // ========================================
    // CENÁRIO 1: Verificação de Saúde
    // ========================================
    
    @Test
    @Order(1)
    @DisplayName("1. API deve estar saudável e respondendo")
    void apiDeveEstarSaudavel() {
        given()
            .when()
                .get("/actuator/health")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
        
        System.out.println("✅ Health check passou - API está saudável");
    }

    // ========================================
    // CENÁRIO 2: CRUD de Hotéis
    // ========================================
    
    @Test
    @Order(10)
    @DisplayName("2.1. Deve criar um hotel com sucesso")
    void deveCriarHotelComSucesso() {
        // HotelRequest: name, address, nightPricePerGuest
        String hotelJson = """
            {
                "name": "Hotel Acceptance Test",
                "address": "Rua dos Testes, 123",
                "nightPricePerGuest": 150.00
            }
            """;

        // POST /hotels retorna void (200 OK sem body)
        given()
            .contentType(ContentType.JSON)
            .body(hotelJson)
        .when()
            .post("/hotels")
        .then()
            .statusCode(200);

        System.out.println("✅ Hotel criado com sucesso");
    }

    @Test
    @Order(11)
    @DisplayName("2.2. Deve listar hotéis contendo o hotel criado")
    void deveListarHoteis() {
        Response response = given()
            .when()
                .get("/hotels")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("name", hasItem("Hotel Acceptance Test"))
                .extract().response();
        
        // Pegar o ID do hotel criado para testes subsequentes
        createdHotelId = response.jsonPath().getLong("[0].id");
        System.out.println("✅ Lista de hotéis retornada. Hotel ID: " + createdHotelId);
    }

    @Test
    @Order(12)
    @DisplayName("2.3. Deve buscar hotel por ID")
    void deveBuscarHotelPorId() {
        given()
            .pathParam("id", createdHotelId)
        .when()
            .get("/hotels/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(createdHotelId.intValue()))
            .body("name", equalTo("Hotel Acceptance Test"));
        
        System.out.println("✅ Hotel encontrado por ID: " + createdHotelId);
    }

    // ========================================
    // CENÁRIO 3: CRUD de Usuários
    // ========================================
    
    @Test
    @Order(20)
    @DisplayName("3.1. Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // UserRequest: email, first_name, last_name, document
        String userJson = """
            {
                "email": "acceptance@test.com",
                "first_name": "Usuário",
                "last_name": "Teste",
                "document": "12345678901"
            }
            """;

        // POST /users retorna void (200 OK sem body)
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/users")
        .then()
            .statusCode(200);

        System.out.println("✅ Usuário criado com sucesso");
    }

    @Test
    @Order(21)
    @DisplayName("3.2. Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() {
        // Assumindo que o primeiro usuário criado tem ID 1
        createdUserId = 1;
        
        Response response = given()
            .pathParam("id", createdUserId)
        .when()
            .get("/users/{id}")
        .then()
            .statusCode(200)
            .body("email", equalTo("acceptance@test.com"))
            .extract().response();
        
        // Atualizar o ID se necessário
        createdUserId = response.jsonPath().getInt("id");
        System.out.println("✅ Usuário encontrado por ID: " + createdUserId);
    }

    // ========================================
    // CENÁRIO 4: CRUD de Reservas
    // ========================================
    
    @Test
    @Order(30)
    @DisplayName("4.1. Deve criar uma reserva com sucesso")
    void deveCriarReservaComSucesso() {
        // BookingRequest: userId, hotelId, checkInDate, checkOutDate, companions
        String bookingJson = String.format("""
            {
                "userId": %d,
                "hotelId": %d,
                "checkInDate": "2025-12-01",
                "checkOutDate": "2025-12-05",
                "companions": ["Acompanhante 1", "Acompanhante 2"]
            }
            """, createdUserId.longValue(), createdHotelId);

        // POST /bookings retorna void (200 OK sem body)
        given()
            .contentType(ContentType.JSON)
            .body(bookingJson)
        .when()
            .post("/bookings")
        .then()
            .statusCode(200);

        System.out.println("✅ Reserva criada com sucesso");
    }

    @Test
    @Order(31)
    @DisplayName("4.2. Deve listar reservas")
    void deveListarReservas() {
        Response response = given()
            .when()
                .get("/bookings")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
        
        createdBookingId = response.jsonPath().getLong("[0].id");
        System.out.println("✅ Lista de reservas retornada. Booking ID: " + createdBookingId);
    }

    @Test
    @Order(32)
    @DisplayName("4.3. Deve buscar reserva por ID")
    void deveBuscarReservaPorId() {
        given()
            .pathParam("id", createdBookingId)
        .when()
            .get("/bookings/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(createdBookingId.intValue()));
        
        System.out.println("✅ Reserva encontrada por ID: " + createdBookingId);
    }

    // ========================================
    // CENÁRIO 5: Limpeza (Cleanup)
    // ========================================
    
    @Test
    @Order(90)
    @DisplayName("5.1. Deve deletar a reserva criada")
    void deveDeletarReserva() {
        if (createdBookingId != null) {
            given()
                .pathParam("id", createdBookingId)
            .when()
                .delete("/bookings/{id}")
            .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
            
            System.out.println("✅ Reserva deletada: " + createdBookingId);
        }
    }

    @Test
    @Order(91)
    @DisplayName("5.2. Deve deletar o usuário criado")
    void deveDeletarUsuario() {
        if (createdUserId != null) {
            given()
                .pathParam("id", createdUserId)
            .when()
                .delete("/users/{id}")
            .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
            
            System.out.println("✅ Usuário deletado: " + createdUserId);
        }
    }

    @Test
    @Order(92)
    @DisplayName("5.3. Deve deletar o hotel criado")
    void deveDeletarHotel() {
        if (createdHotelId != null) {
            given()
                .pathParam("id", createdHotelId)
            .when()
                .delete("/hotels/{id}")
            .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
            
            System.out.println("✅ Hotel deletado: " + createdHotelId);
        }
    }

    // ========================================
    // CENÁRIO 6: Testes de Validação
    // ========================================
    
    @Test
    @Order(100)
    @DisplayName("6.1. Deve retornar erro para hotel inexistente")
    void deveRetornarErroParaHotelInexistente() {
        given()
            .pathParam("id", 999999)
        .when()
            .get("/hotels/{id}")
        .then()
            .statusCode(anyOf(is(404), is(500)));
        
        System.out.println("✅ Tratamento de hotel inexistente validado");
    }

    @Test
    @Order(101)
    @DisplayName("6.2. Deve retornar erro para usuário inexistente")
    void deveRetornarErroParaUsuarioInexistente() {
        given()
            .pathParam("id", 999999)
        .when()
            .get("/users/{id}")
        .then()
            .statusCode(anyOf(is(404), is(500)));
        
        System.out.println("✅ Tratamento de usuário inexistente validado");
    }
}
