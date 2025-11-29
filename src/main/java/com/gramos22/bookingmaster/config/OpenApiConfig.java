package com.gramos22.bookingmaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * 
 * Acesse a documentação em:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI bookingMasterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookingMaster API")
                        .description("API REST para gerenciamento de reservas de hotéis. " +
                                "Permite criar, consultar, atualizar e cancelar reservas, " +
                                "gerenciar hotéis e usuários.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BookingMaster Team")
                                .email("contato@bookingmaster.com")
                                .url("https://github.com/o-romeroo/bookingmaster"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor Local"),
                        new Server()
                                .url("http://localhost:8090")
                                .description("Servidor de Testes")
                ));
    }
}
