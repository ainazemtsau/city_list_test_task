package com.example.citylist.router;

import com.example.citylist.handler.CityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CityRouter {
    @Bean
    public RouterFunction<ServerResponse> citiesRouter(CityHandler handler) {
        return RouterFunctions.route()
                .path("v1/city", builder -> builder
                        .GET("", handler::getAllCities)
                        .GET("/number", handler::getCitiesTotalNumber)
                        .PUT("", handler::updateCity))
                .build();
    }
}