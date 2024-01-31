package com.netty.test;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.HashMap;
import java.util.Map;

/**********************
 *
 * @Description TODO
 * @Date 2024/1/18 11:55 AM
 * @Created by 龙川
 ***********************/
public class FastThreadTest {
    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        Map<Integer,FastThreadLocal<String>> map = new HashMap<>();

        Integer num = 34;

        for (int i = 0; i < num; i++) {
            map.put(i,new FastThreadLocal<>());
        }


        FastThreadLocalThread fastThreadLocalThread = new FastThreadLocalThread(() -> {
            for (int i = 0; i < num; i++) {
                FastThreadLocal<String> fastThreadLocal1 = map.get(i);
                fastThreadLocal1.set("11");
            }

            threadLocal.set("333");

            System.out.println(map.get(0));
            System.out.println(threadLocal.get());
        },"t-1");
        fastThreadLocalThread.start();

        FastThreadLocalThread fastThreadLocalThread1 = new FastThreadLocalThread(() -> {
            map.get(32).set("111-1");
            threadLocal.set("333-1");

            System.out.println(map.get(0).get());
            System.out.println(threadLocal.get());
        },"t-2");
        fastThreadLocalThread1.start();
    }
}
