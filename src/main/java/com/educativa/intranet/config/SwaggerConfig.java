package com.educativa.intranet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Intranet Educativa",
                version = "1.0",
                description = "Documentación interactiva Swagger para la API de la Intranet conectada a WordPress. Configurado con Seguridad JWT.",
                contact = @Contact(name = "Soporte TI", email = "soporte@colegio.edu")
        ),
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Ingresa tu Token JWT (generado en /auth/login). Solo pega el código del token, la palabra **Bearer** se agrega automáticamente.",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
