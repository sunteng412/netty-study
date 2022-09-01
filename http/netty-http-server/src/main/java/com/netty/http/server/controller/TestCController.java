package com.netty.http.server.controller;

import com.netty.http.server.config.ReactiveHttpContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RestController
@RequestMapping("test")
@Slf4j
public class TestCController {

    @GetMapping("/http")
    @SneakyThrows
    public Mono<String> testHttp(){
        Mono<ServerHttpRequest> request = ReactiveHttpContextHolder.getRequest();
        return request.flatMap((Function<ServerHttpRequest, Mono<String>>)
                serverHttpRequest -> Mono.just(serverHttpRequest.getRemoteAddress().toString()));
    }
}
