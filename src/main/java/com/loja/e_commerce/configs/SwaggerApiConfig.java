package com.loja.e_commerce.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce API",
                description = "API de loja virtual",
                version = "v1"
        )
)
public class SwaggerApiConfig {
}
