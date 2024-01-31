package com.netty.test;

/**********************
 *
 * @Description TODO
 * @Date 2024/1/19 3:44 PM
 * @Created by 龙川
 ***********************/
public class TestArrayLen {


    public static void main(String[] args) {

        Integer index = 10086;

        Integer newCapacity = index;
        newCapacity |= newCapacity >>>  1;
        newCapacity |= newCapacity >>>  2;
        newCapacity |= newCapacity >>>  4;
        newCapacity |= newCapacity >>>  8;
        newCapacity |= newCapacity >>> 16;
        newCapacity ++;

        System.out.println(newCapacity);
    }
}
