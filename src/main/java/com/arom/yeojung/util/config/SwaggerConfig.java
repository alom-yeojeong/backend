package com.arom.yeojung.util.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class SwaggerConfig {
    SecurityScheme apiKey = new SecurityScheme()
            .type(Type.HTTP)
            .in(In.HEADER)
            .name("Authorization")
            .scheme("bearer")
            .bearerFormat("JWT");

    SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Bearer Token");

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("My API Docs")
                        .version("1.0")
                        .description("API 문서 설명"));
//                .servers(List.of(
//                        new Server().url("http://localhost:9090").description("Local 개발 서버"),
//                        new Server().url("https://3.35.38.147:9090").description("운영 서버")
//                ));
    }
}




