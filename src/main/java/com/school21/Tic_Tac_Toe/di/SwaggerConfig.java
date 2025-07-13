package com.school21.Tic_Tac_Toe.di;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tic-Tac-Toe API",
                version = "1.0",
                description = "API для игры в крестики-нолики"
        )
)
public class SwaggerConfig {
}
