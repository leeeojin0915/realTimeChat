/*
package com.eojin.realtimechat.netty;

import com.eojin.realtimechat.web.domain.entity.chat.SenderType;
import com.eojin.realtimechat.web.service.chat.ChatService;
import com.eojin.realtimechat.web.service.chat.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ChannelHandler.Sharable
public class ChatWebSocketHandler_2 extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // roomId별 채널 모음
    private static final Map<Long, ChannelGroup> ROOM_CHANNELS = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private ChatService chatService; // Spring Bean 주입
    @Override
    public void handlerAdded(ChannelHandlerContext context){
        log.info("Client connected: {}", context.channel().id().asShortText());
    }
    @Override
    public void handleRemoved(ChannelHandlerContext context){
        log.info("Client disconnected: {}",context.channel().id().asShortText());
        // 채널 그룹에서 제거는 broadcast 시 자동 정리되게 두거나
        // 여기서 ROOM_CHANNELS 돌면서 제거 로직 넣어도됨
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String payload = msg.text();
        log.info("Received Message:{}",payload);

        ChatMessagePayload request = OBJECT_MAPPER.readValue(payload, ChatMessagePayload.class);

        Long roomId = request.roomId;
        ChannelGroup group = ROOM_CHANNELS.computeIfAbsent(
                roomId,
                id->new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
        );
        group.add(ctx.channel());

        // DB 저장
        if(chatService != null){
            chatService.sendMessage(new ChatDto.SendMessageRequest(
                    roomId,
                    request.senderType,
                    request.senderName,
                    request.message
            ));
        }

        // 같은 room에 브로드 캐스트
        String responseJson = OBJECT_MAPPER.writeValueAsString(request);
        group.writeAndFlush(new TextWebSocketFrame(responseJson));
    }

    public record ChatMessagePayload(
            Long roomId,
            SenderType senderType,
            String senderName,
            String message
    ){}
}
*/
