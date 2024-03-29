package com.LP2.EventScheduler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiSwaggerConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Master Event Planner API")
                        .description("The REST API of the Master Event Planner application (owned by EventPlan Corp.)")
                        .version("1.0.0"));
    }
}
