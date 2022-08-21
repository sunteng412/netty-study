package com.netty.http.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("test")
@Slf4j
public class TestCController {

    @GetMapping("/http")
    public Mono<String> testHttp(){
        log.info("sdsds");
        return Mono.just("试试");
    }
}
