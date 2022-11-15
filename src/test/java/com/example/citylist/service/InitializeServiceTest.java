package com.example.citylist.service;

import com.example.citylist.AbstractIntegrationTest;
import com.example.citylist.repositories.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class InitializeServiceTest extends AbstractIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void test(){
        StepVerifier.create(cityRepository.findAll())
                .expectNextCount(100).expectComplete().verify();
    }

}
