package com.netty.http.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
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
        DefaultFullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET,
                "/test/testMaxHttpGet");
        HttpHeaders headers = fullHttpRequest.headers();
        headers.set(HttpHeaderNames.USER_AGENT, "RocketClient");
        headers.set(HttpHeaderNames.ACCEPT, "*/*");
        ctx.writeAndFlush(fullHttpRequest);
    }




}
