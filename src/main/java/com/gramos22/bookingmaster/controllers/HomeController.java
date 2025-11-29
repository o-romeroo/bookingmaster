package com.gramos22.bookingmaster.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para redirecionar a URL raiz para o Swagger UI.
 */
@Controller
public class HomeController {

    /**
     * Redireciona a página inicial para a documentação Swagger.
     * Acesse http://localhost:8080/ para ser redirecionado automaticamente.
     */
    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
