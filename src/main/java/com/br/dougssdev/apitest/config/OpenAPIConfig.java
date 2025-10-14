package com.br.dougssdev.apitest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("Api Testing")
                        .version("v1")
                        .description("This API is all about testing.")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("")));
    }

}
