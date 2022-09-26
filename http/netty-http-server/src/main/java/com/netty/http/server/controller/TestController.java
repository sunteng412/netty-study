package com.netty.http.server.controller;

import com.netty.http.server.config.ReactiveHttpContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @GetMapping("/http")
    @SneakyThrows
    public Mono<String> testHttp() {
        Mono<ServerHttpRequest> request = ReactiveHttpContextHolder.getRequest();
        return request.flatMap((Function<ServerHttpRequest, Mono<String>>)
                serverHttpRequest -> Mono.just(serverHttpRequest.getRemoteAddress().toString()));
    }


    @PostMapping("/httpPost")
    @SneakyThrows
    //public Mono<String> testHttpPost(@RequestBody Map<String,Object> params){
    public Mono<String> testHttpPost() {
        Mono<ServerHttpRequest> request = ReactiveHttpContextHolder.getRequest();
        return request.flatMap((Function<ServerHttpRequest, Mono<String>>)
                serverHttpRequest -> Mono.just(serverHttpRequest.getRemoteAddress().toString()));
    }

    @GetMapping ("/testMaxHttpGet")
    @SneakyThrows
    //public Mono<String> testHttpPost(@RequestBody Map<String,Object> params){
    public Mono<String> testMaxHttpGet() {
        Mono<ServerHttpRequest> request = ReactiveHttpContextHolder.getRequest();
        return request.flatMap((Function<ServerHttpRequest, Mono<String>>)
                serverHttpRequest ->{

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 1000000; i++) {
                        System.out.println(i);
                        stringBuilder.append(Objects.requireNonNull(serverHttpRequest.getRemoteAddress()));
                    }
                    return Mono.just(stringBuilder.toString());
                });
    }
}
