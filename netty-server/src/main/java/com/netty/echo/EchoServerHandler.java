package com.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 入
 * @author     : longchuan
 * @date       : 2022/3/26 7:27 下午
 * @description:
 * @version    :
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 管道读事件
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //将消息记录到控制台
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Server received: " + in.toString(CharsetUtil.UTF_8));
        //将接收到的消息写给发送者，而不冲刷出站消息

        int readableBytes = in.readableBytes();
        //可读长度
        byte[] content = new byte[readableBytes];
        in.getBytes(0,content);


        in.writeBytes((
                "当前时间:" + LocalDateTime.now() + ",client:" + (ctx.channel().remoteAddress()) + ",server:" +
                        (ctx.channel().localAddress()) + "\n"
        ).getBytes(StandardCharsets.UTF_8));
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //将未 决消息冲刷到远程节点，并且关闭该 Channel
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }


    /**
     * 每个 Channel 都拥有一个与之相关联的 ChannelPipeline，其持有一个 ChannelHandler 的
     * 实例链。在默认的情况下，ChannelHandler 会把对它的方法的调用转发给链中的下一个 Channel-
     * Handler。因此，如果 exceptionCaught()方法没有被该链中的某处实现，那么所接收的异常将会被
     * 传递到 ChannelPipeline 的尾端并被记录。为此，你的应用程序应该提供至少有一个实现了
     * exceptionCaught()方法的 ChannelHandler。
     * */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
