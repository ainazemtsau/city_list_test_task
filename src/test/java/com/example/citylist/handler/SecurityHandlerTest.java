package com.example.citylist.handler;

import com.example.citylist.AbstractIntegrationTest;
import com.example.citylist.dto.UserLoginDto;
import com.example.citylist.model.City;
import com.example.citylist.repositories.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SecurityHandlerTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("When try login user")
    public void testLoginUser() {
        var loginUrl = "/v1/login";
        var userLoginDto = new UserLoginDto("user", "user", null);
        webTestClient.post()
                .uri(loginUrl)
                .bodyValue(userLoginDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserLoginDto.class);
    }

    @Test
    @DisplayName("When try login user with invalid login or password")
    public void testLoginUserWithInvalidCredentials() {
        var loginUrl = "/v1/login";
        var userLoginDto = new UserLoginDto("user", "userInvalidPassword", null);
        webTestClient.post()
                .uri(loginUrl)
                .bodyValue(userLoginDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);
    }

}
