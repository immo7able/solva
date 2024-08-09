package org.example.solva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Solva API")
                        .version("1.0.0")
                        .description("API для управления транзакциями и лимитами")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
