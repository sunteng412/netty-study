package com.netty.http.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * SimpleChannelInboundHandler 与 ChannelInboundHandler
 * 你可能会想：为什么我们在客户端使用的是 SimpleChannelInboundHandler，而不是在 Echo-
 * ServerHandler 中所使用的 ChannelInboundHandlerAdapter 呢？这和两个因素的相互作用有
 * 关：业务逻辑如何处理消息以及 Netty 如何管理资源。
 * 在客户端，当 channelRead0()方法完成时，你已经有了传入消息，并且已经处理完它了。当该方
 * 法返回时，SimpleChannelInboundHandler 负责释放指向保存该消息的 ByteBuf 的内存引用。
 * 在 EchoServerHandler 中，你仍然需要将传入消息回送给发送者，而 write()操作是异步的，直
 * 到 channelRead()方法返回后可能仍然没有完成（如代码清单 2-1 所示）。为此，EchoServerHandler
 * 扩展了 ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
 * 消息在 EchoServerHandler 的 channelReadComplete()方法中，当 writeAndFlush()方
 * 法被调用时被释放
 * @author     : longchuan
 * @date       : 2022/3/27 7:00 下午
 * @description:
 * @version    :
 */
@ChannelHandler.Sharable
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse in) {
        System.out.println(
                "Client received: " + in.content().toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 当被通知 Channel
     * 是活跃的时候，发
     * 送一条消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive");
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,
                HttpResponseStatus.CREATED);
        ctx.writeAndFlush(fullHttpResponse);
    }
}
