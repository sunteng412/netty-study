package com.netty.echo;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
/**
 * 打包docker 详见
 * https://www.yuque.com/u2213645/cqbs2c/urygq0
 * */
public class EchoEpollServer {

    @SneakyThrows
    public static void main(String[] args) {
        String port = System.getProperty("netty.port", "19999");
        System.out.println("启动端口:" + port);

        final EchoServerHandler serverHandler = new EchoServerHandler();
        //创建EventLoopGroup
        EpollEventLoopGroup group = new EpollEventLoopGroup(1, r -> {
            return new Thread(r,"boss-1");
        });
        EpollEventLoopGroup workGroup = new EpollEventLoopGroup(1, r -> {
            return new Thread(r,"work-1");
        });

        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group,workGroup)
                    .channel(EpollServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(Integer.parseInt(port)))
                    //添加一个EchoServerHandler 到子Channel的 ChannelPipeline
                    /**
                     * 当一个新的连接
                     * 被接受时，一个新的子 Channel 将会被创建，而 ChannelInitializer 将会把一个你的
                     * EchoServerHandler 的实例添加到该 Channel 的 ChannelPipeline 中。正如我们之前所
                     * 解释的，这个 ChannelHandler 将会收到有关入站消息的通知。
                     * */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println("start netty success!!!");
            //获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }
}
