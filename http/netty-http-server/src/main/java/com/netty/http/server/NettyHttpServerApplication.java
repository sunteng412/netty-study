package com.netty.http.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动
 * @author     : MrFox
 * @date       : 2022/8/21 10:45 PM
 * @description:
 * @version    :
 */
@SpringBootApplication
public class NettyHttpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyHttpServerApplication.class,args);
    }
}
