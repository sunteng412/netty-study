package com.netty.utils;

import java.lang.management.ManagementFactory;

public class SystemUtils {

    public static long getPid(){
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }
}
