package com.netty.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import javassist.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class WatchServerFd {
    static File file;

    static {
        OsInfo osInfo = SystemUtil.getOsInfo();
        System.out.println("os:" + osInfo.getName());
        if (osInfo.isMac()){
            file = new File("~/docker/local/log/fdWatch.txt");
        } else {
            file = new File("/strace/fdWatch.txt");
        }

        //判断有没有路径

        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get("com.netty.echo.EchoServerHandler");
            //解冻
            cc.defrost();
            CtMethod m = cc.getDeclaredMethod("channelRead");
            m.insertBefore("com.netty.utils.WatchServerFd.metric(ctx,msg);");
            cc.toClass(WatchServerFd.class.getClassLoader());
        } catch (Exception e) {
            System.out.println(Throwables.getStackTraceAsString(e));
        }

        //开辟accept对应的fd
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get("io.netty.channel.nio.AbstractNioChannel");
            //解冻
            cc.defrost();

            CtConstructor[] declaredConstructors = cc.getDeclaredConstructors();
            Arrays.asList(declaredConstructors).forEach(ctConstructor->{
                try {
                    ctConstructor.insertBefore("com.netty.utils.WatchServerFd.getFd(parent,ch,readInterestOp);");
                } catch (CannotCompileException e) {
                    System.out.println(Throwables.getStackTraceAsString(e));
                }
            });


            cc.toClass(WatchServerFd.class.getClassLoader());
        } catch (Exception e) {
            System.out.println(Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 获取accept的fd及权限16-代表accept
     * @author     : longchuan
     * @date       : 2022/3/30 9:25 下午
     * @description:
     * @version    :
     */
    @SneakyThrows
    public static void getFd(Channel parent, SelectableChannel ch,int readInterestOp){
        Object parentFd = 0;
        String format;

        if(Objects.nonNull(parent)){
            parentFd = FieldUtils.readField(FieldUtils.readField(parent, "ch", Boolean.TRUE),
                    "fdVal", Boolean.TRUE);
        }
        switch (readInterestOp){
            case 16:
                format = String.format("获取到accept的fd,对应符号:%s,操作符:%s\n",
                        FieldUtils.readField(ch, "fdVal", Boolean.TRUE),readInterestOp);
                break;
            case 1:
                //SelectionKey.OP_READ
                format = String.format("获取到read的fd,对应符号:%s,操作符:%s,它从哪来:%s\n",
                        FieldUtils.readField(ch, "fdVal", Boolean.TRUE),readInterestOp,parentFd);
                break;
            case 4:
                //SelectionKey.OP_WRITE
                format = String.format("获取到write的fd,对应符号:%s,操作符:%s,它从哪来:%s\n",
                        FieldUtils.readField(ch, "fdVal", Boolean.TRUE),readInterestOp,parentFd);
                break;
            case 8:
                //SelectionKey.OP_CONNECT
                format = String.format("获取到connect的fd,对应符号:%s,操作符:%s,它从哪来:%s\n",
                        FieldUtils.readField(ch, "fdVal", Boolean.TRUE),readInterestOp,parentFd);
                break;
            default:
                format = String.format("获取到未知的fd,对应符号:%s,操作符:%s,它从哪来:%s\n",
                        FieldUtils.readField(ch, "fdVal", Boolean.TRUE),readInterestOp,parentFd);

        }

        FileUtil.appendUtf8String(format,file);

    }

    /**
     * 获取read 时的fd
     * @param
     * @return
     * @description:
     */
    @SneakyThrows
    public static void metric(ChannelHandlerContext ctx,Object msg){
        Object fdVal = FieldUtils.readField(FieldUtils.readField(ctx.channel(), "ch", Boolean.TRUE),
                "fdVal", Boolean.TRUE);
        ByteBuf in = (ByteBuf) msg;
        //去除空格

        FileUtil.appendUtf8String("fd:" + fdVal.toString() +
                        "，时间:" + LocalDateTime.now() +
                "，四元组:" +( (ctx.channel().remoteAddress()) + "=>" + (ctx.channel().localAddress()))
                + "，内容:" + new String(getBytes(in.nioBuffer()), StandardCharsets.UTF_8).replace("\n","")
                        + "\n"
                ,file);
    }

    public static byte[] getBytes(ByteBuffer byteBuffer) {
        ByteBuffer duplicate = byteBuffer.duplicate();
        byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);
        return bytes;
    }

}
