package com.example.citylist.utils;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

public class PageableUtils {
    public static final String REQUEST_QUERY_SIZE = "size";
    public static final String REQUEST_QUERY_PAGE = "page";

    public static <T> Flux<T> getPageableFlux(ServerRequest serverRequest, Flux<T> flux) {
        long size = Long.parseLong(serverRequest.queryParam(REQUEST_QUERY_SIZE).orElse(String.valueOf(10)));
        if(serverRequest.queryParam(REQUEST_QUERY_PAGE).isPresent()){
            long page = Math.abs(Long.parseLong(serverRequest.queryParam(REQUEST_QUERY_PAGE).get()));
            page = page == 0 ? 1 : page;
            flux = flux.skip((page - 1) * size).take(size);
        }
        return flux;
    }

}
