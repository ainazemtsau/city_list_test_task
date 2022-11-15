package com.example.citylist.handler;

import com.example.citylist.dto.UserLoginDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class SecurityHandler {
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityHandler(ReactiveUserDetailsService reactiveUserDetailsService, PasswordEncoder passwordEncoder) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserLoginDto.class)
                .flatMap(user -> reactiveUserDetailsService.findByUsername(user.username())
                        .filter(ud -> passwordEncoder.matches(user.password(), ud.getPassword()))
                        .map(ud -> new UserLoginDto(ud.getUsername(), null, ud.isEnabled()))
                        .switchIfEmpty(Mono.error(new Exception("Wrong login or password")))
                        .flatMap(u -> ServerResponse.ok().body(Mono.just(u), UserLoginDto.class)))
                .onErrorResume(err -> Mono.just("Error " + err.getMessage())
                        .flatMap(e -> ServerResponse.badRequest().contentType(MediaType.TEXT_PLAIN)
                                .body(Mono.just("Invalid username or password"), String.class)));
    }
}
