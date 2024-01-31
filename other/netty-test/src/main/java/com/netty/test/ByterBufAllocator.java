package com.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**********************
 *
 * @Description TODO
 * @Date 2024/1/31 5:48 PM
 * @Created by 龙川
 ***********************/
public class ByterBufAllocator {

    public static void main(String[] args) {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(124);
        buffer.writeByte(1);

        System.out.println(buffer.writerIndex());

    }
}
