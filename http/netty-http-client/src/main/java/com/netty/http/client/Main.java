package com.netty.http.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        String port = System.getProperty("netty.port", "8080");
        String host = System.getProperty("netty.host", "127.0.0.1");
        System.out.println("启动端口:" + port);

        EventLoopGroup group;
        Class<? extends Channel> channelClass;

        if (Epoll.isAvailable()) {
            group = new EpollEventLoopGroup();
            channelClass = EpollSocketChannel.class;
        } else if (KQueue.isAvailable()) {
            group = new KQueueEventLoopGroup();
            channelClass = KQueueSocketChannel.class;
        } else {
            group = new NioEventLoopGroup();
            channelClass = NioSocketChannel.class;
        }


        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(channelClass)
                    .remoteAddress(new InetSocketAddress(host, Integer.parseInt(port)))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //聚合http消息片段
//                            pipeline .addLast(new HttpClientCodec())
//                                    .addLast(new HttpObjectAggregator(10 * 1024 * 1024))
//                                    //支持压缩
//                                    .addLast(new HttpContentDecompressor())
//                                    .addLast(new ChunkedWriteHandler());

                            //聚合 HTTP 消息
                            pipeline.addLast("codec", new HttpClientCodec());
                            //客户端可以通过提供以下头部信息来指示服务器它所支持的压缩格式：
                            //GET /encrypted-area HTTP/1.1
                            //Host: www.example.com
                            //Accept-Encoding: gzip, deflate
                            pipeline.addLast("decompressor", new HttpContentDecompressor());
                            //解码
                            pipeline.addLast("decoder", new HttpResponseDecoder());
                            pipeline.addLast("aggregator",new HttpObjectAggregator(10 * 1024 * 1024));
                            //pipeline.addLast("encoder", new HttpRequestEncoder());
                            pipeline.addLast("bizHandler", new HttpClientHandler());
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
