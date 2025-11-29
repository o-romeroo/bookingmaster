package com.gramos22.bookingmaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * 
 * Acesse a documentação em:
 * - Swagger UI: /swagger-ui.html
 * - OpenAPI JSON: /api-docs
 */
@Configuration
public class OpenApiConfig {

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
                                .url("https://oviferous-unabashedly-sherwood.ngrok-free.dev")
                                .description("Servidor de Produção (Ngrok)")
                ));
    }
}
