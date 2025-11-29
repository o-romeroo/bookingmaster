package com.gramos22.bookingmaster.integration;

import com.gramos22.bookingmaster.dtos.HotelRequest;
import com.gramos22.bookingmaster.entities.Hotel;
import com.gramos22.bookingmaster.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Integração para Hotel
 * Utiliza Testcontainers para criar um banco MariaDB real
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class HotelIntegrationTest {

    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:11.2")
            .withDatabaseName("bmdb_test")
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

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HotelRepository hotelRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/hotels";
        hotelRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um hotel e persistir no banco de dados")
    void shouldCreateHotelAndPersistInDatabase() {
        // Arrange
        HotelRequest request = new HotelRequest();
        request.setName("Hotel Teste Integração");
        request.setAddress("Rua dos Testes, 123");
        request.setNightPricePerGuest(150.0);

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, request, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Verificar se foi persistido no banco
        assertThat(hotelRepository.count()).isEqualTo(1);
        Hotel savedHotel = hotelRepository.findAll().get(0);
        assertThat(savedHotel.getName()).isEqualTo("Hotel Teste Integração");
        assertThat(savedHotel.getAddress()).isEqualTo("Rua dos Testes, 123");
    }

    @Test
    @DisplayName("Deve listar todos os hotéis do banco de dados")
    void shouldListAllHotelsFromDatabase() {
        // Arrange - Criar hotéis diretamente no banco
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel Alpha");
        hotel1.setAddress("Endereço Alpha");
        hotel1.setNight_price_per_guest(100.0);
        hotelRepository.save(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel Beta");
        hotel2.setAddress("Endereço Beta");
        hotel2.setNight_price_per_guest(200.0);
        hotelRepository.save(hotel2);

        // Act
        ResponseEntity<Object[]> response = restTemplate.getForEntity(baseUrl, Object[].class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar hotel por ID")
    void shouldGetHotelById() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Específico");
        hotel.setAddress("Endereço Específico");
        hotel.setNight_price_per_guest(250.0);
        Hotel savedHotel = hotelRepository.save(hotel);

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/" + savedHotel.getId(), 
                String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hotel Específico");
    }

    @Test
    @DisplayName("Deve deletar hotel do banco de dados")
    void shouldDeleteHotelFromDatabase() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Para Deletar");
        hotel.setAddress("Endereço");
        hotel.setNight_price_per_guest(100.0);
        Hotel savedHotel = hotelRepository.save(hotel);
        
        assertThat(hotelRepository.count()).isEqualTo(1);

        // Act
        restTemplate.delete(baseUrl + "/" + savedHotel.getId());

        // Assert
        assertThat(hotelRepository.count()).isEqualTo(0);
    }
}
