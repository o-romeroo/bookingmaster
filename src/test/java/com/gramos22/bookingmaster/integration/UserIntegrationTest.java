package com.gramos22.bookingmaster.integration;

import com.gramos22.bookingmaster.dtos.UserRequest;
import com.gramos22.bookingmaster.entities.User;
import com.gramos22.bookingmaster.repositories.UserRepository;
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
 * Testes de Integração para User
 * Utiliza Testcontainers para criar um banco MariaDB real
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {

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
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/users";
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um usuário e persistir no banco de dados")
    void shouldCreateUserAndPersistInDatabase() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setEmail("teste@email.com");
        request.setFirst_name("João");
        request.setLast_name("Silva");
        request.setDocument("12345678901");

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, request, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Verificar se foi persistido no banco
        assertThat(userRepository.count()).isEqualTo(1);
        User savedUser = userRepository.findAll().get(0);
        assertThat(savedUser.getEmail()).isEqualTo("teste@email.com");
        assertThat(savedUser.getFirst_name()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void shouldGetUserById() {
        // Arrange
        User user = new User();
        user.setEmail("busca@email.com");
        user.setFirst_name("Maria");
        user.setLast_name("Santos");
        user.setDocument("98765432100");
        User savedUser = userRepository.save(user);

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/" + savedUser.getId(), 
                String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Maria");
        assertThat(response.getBody()).contains("busca@email.com");
    }

    @Test
    @DisplayName("Deve deletar usuário do banco de dados")
    void shouldDeleteUserFromDatabase() {
        // Arrange
        User user = new User();
        user.setEmail("deletar@email.com");
        user.setFirst_name("Carlos");
        user.setLast_name("Oliveira");
        user.setDocument("11122233344");
        User savedUser = userRepository.save(user);
        
        assertThat(userRepository.count()).isEqualTo(1);

        // Act
        restTemplate.delete(baseUrl + "/" + savedUser.getId());

        // Assert
        assertThat(userRepository.count()).isEqualTo(0);
    }
}
