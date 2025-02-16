package com.uniquindio.api.crud.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de CRUD - Uniquindio")
                        .version("1.0")
                        .description("Esta API permite gestionar usuarios (estudiantes y profesores) y trabajos (envíos y calificaciones).\n\n" +
                                "Los endpoints permiten realizar operaciones CRUD sobre usuarios y gestionar el envío y calificación de trabajos.")
                        .contact(new Contact()
                                .name("Diego Alejandro Pastrana Fernandez, Santiago Fernandez, Andres Felipe Rendon y Juan Camilo Valbuena")
                                .email("diegoa.pastranaf@uqvirtual.edu.co, andresf.rendonn@uqvirtual.edu.co")
                                .url("https://www.uniquindio.edu.co/")

                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))

                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación completa de la API")
                        .url("https://app.swaggerhub.com/apis-docs/universidaddelquindi/api-de_crud_uniquindio/1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio GitHub")
                        .url("https://github.com/alejandropastrana58152559/Api_RESTfull_Crud_Uniquindio"))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080/CRUD").description("Servidor Local"),
                        new Server().url("https://api.uniquindio.edu/CRUD").description("Servidor de Producción")
                ));

    }
}
