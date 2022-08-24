package com.netty.http.client;


import lombok.SneakyThrows;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.util.concurrent.TimeUnit;

public class NettyHttpClientMain {
    @SneakyThrows
    public static void main(String[] args) {
        HttpClient client = HttpClient.create();

        client.warmup() //<1>
                .block();

        client.get()
                .uri("http://127.0.0.1:8080/test/http")
                .responseContent()
                .aggregate()//
                .asString()
                .doOnSuccess(s -> {
                    System.out.println("s1=" + s);
                }).block();

        TimeUnit.SECONDS.sleep(10);
    }
}
