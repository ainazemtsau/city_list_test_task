package com.example.citylist.handler;

import com.example.citylist.model.City;
import com.example.citylist.repositories.CityRepository;
import com.example.citylist.utils.PageableUtils;
import com.example.citylist.validation.Validation;
import com.example.citylist.validation.ValidationErrors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CityHandler {
    public static final String CITY_FIELD_ID = "id";
    private final CityRepository cityRepository;
    private final Validation<City> cityValidator;

    public CityHandler(CityRepository cityRepository, Validation<City> cityValidator) {
        this.cityRepository = cityRepository;
        this.cityValidator = cityValidator;
    }

    public Mono<ServerResponse> getAllCities(ServerRequest serverRequest) {
        Flux<City> cityFlux;
        Sort sort = Sort.by(CITY_FIELD_ID);
        String filterParam = serverRequest.queryParam("name").orElse(null);
        if (Strings.isNotEmpty(filterParam)) {
            City city = new City();
            city.setName(filterParam);
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withIgnorePaths("id")
                    .withIgnoreNullValues()
                    .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                    .withIgnoreCase();
            Example<City> example = Example.of(city, matcher);
            cityFlux = cityRepository.findAll(example, sort);
        } else {
            cityFlux = cityRepository.findAll(sort);
        }
        cityFlux = PageableUtils.getPageableFlux(serverRequest, cityFlux);
        return ServerResponse.ok().body(cityFlux, City.class);
    }

    public Mono<ServerResponse> getCitiesTotalNumber(ServerRequest serverRequest) {
        String filterParam = serverRequest.queryParam("name").orElse(null);
        City city = new City();
        city.setName(filterParam);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("id")
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        Example<City> example = Example.of(city, matcher);
        return ServerResponse.ok().body(cityRepository.count(example), Integer.class);
    }

    public Mono<ServerResponse> updateCity(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(City.class)
                .flatMap(city -> {
                    List<ValidationErrors> validationErrors = cityValidator.validate(city);
                    if (validationErrors.isEmpty()) {
                        return ServerResponse.ok()
                                .body(cityRepository.save(city), City.class);
                    } else {
                        return ServerResponse.badRequest().bodyValue(validationErrors);
                    }
                });
    }
}
