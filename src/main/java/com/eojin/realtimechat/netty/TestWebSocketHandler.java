package com.eojin.realtimechat.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class TestWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg){
        String text = msg.text();
        ctx.channel().writeAndFlush(new TextWebSocketFrame("ECHO: " + text));
    }
}
