package com.example.citylist.router;

import com.example.citylist.handler.SecurityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SecurityRouters {
    @Bean
    public RouterFunction<ServerResponse> securityRouter(SecurityHandler handler) {
        return RouterFunctions.route()
                .path("v1/login", builder -> builder
                        .POST("", handler::login))
                .build();
    }
}
