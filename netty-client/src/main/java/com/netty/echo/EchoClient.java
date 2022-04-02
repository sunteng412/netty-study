package com.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

public class EchoClient {

    @SneakyThrows
    public static void main(String[] args) {
        String port = System.getProperty("netty.port", "10000");
        String host = System.getProperty("netty.host", "127.0.0.1");
        System.out.println("启动端口:" + port);

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    //指 定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, Integer.parseInt(port)))
                    //适用于 NIO 传输的Channel 类型
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler 实例
                            ch.pipeline().addLast(
                                    new EchoClientHandler());
                        }
                    });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞，直到Channel 关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭线程池并且
            //释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
