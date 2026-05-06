package com.eojin.realtimechat.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch){
        var pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64*1024));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws-chat"));
        pipeline.addLast(new ChatWebSocketHandler());
    }
}
