package com.eojin.realtimechat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatServer {
    private final int port = 9090; // WebSocket 포트 (백엔드 Rest와 분리)

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    @PostConstruct
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer());

        ChannelFuture future = bootstrap.bind(port).sync();
        serverChannel = future.channel();
        log.info("Netty ChatServer stared on port:{}",port);
    }
    @PreDestroy
    public void stop() throws InterruptedException {
        if(serverChannel != null){
            serverChannel.close().sync();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("Netty ChatServer stopped");
    }
}
