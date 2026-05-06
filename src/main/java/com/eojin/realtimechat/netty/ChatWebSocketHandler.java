package com.eojin.realtimechat.netty;

import com.eojin.realtimechat.config.ApplicationContextHolder;
import com.eojin.realtimechat.web.domain.dto.MessageResponseDTO;
import com.eojin.realtimechat.web.domain.entity.messenger.MessageType;
import com.eojin.realtimechat.web.service.MessengerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // roomId별 채널 모음
    private static final Map<Long, ChannelGroup> ROOM_CHANNELS = new ConcurrentHashMap<>();

    public void handlerRemoved(ChannelHandlerContext ctx) {
        ROOM_CHANNELS.values().forEach(group -> group.remove(ctx.channel()));
        log.info("Client disconnected: {}", ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        try {
            String payload = msg.text();
            log.info("Received Message: {}", payload);

            ChatPayload req = OBJECT_MAPPER.readValue(payload, ChatPayload.class);

            if (req.roomId == null) {
                ctx.writeAndFlush(new TextWebSocketFrame("{\"error\":\"roomId is required\"}"));
                return;
            }

            ChannelGroup group = ROOM_CHANNELS.computeIfAbsent(
                    req.roomId,
                    id -> new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
            );
            group.add(ctx.channel());

            if ("join".equalsIgnoreCase(req.type)) {
                // Return immediately without saving for join messages
                return;
            }

            // DB 저장 및 응답 DTO 생성 (트랜잭션 내에서 모든 데이터 로드 완료)
            MessengerService messengerService = ApplicationContextHolder.getBean(MessengerService.class);
            MessageType messageType = MessageType.TEXT;
            if (req.fileType != null) {
                try {
                    messageType = MessageType.valueOf(req.fileType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    messageType = MessageType.FILE;
                }
            }

            MessageResponseDTO responseDto = messengerService.saveMessage(
                    req.roomId,
                    req.senderId,
                    req.content,
                    req.fileUrl,
                    messageType
            );

            // 브로드캐스트
            String responseJson = OBJECT_MAPPER.writeValueAsString(responseDto);
            group.writeAndFlush(new TextWebSocketFrame(responseJson));

        } catch (Exception e) {
            log.error("ChatWebSocketHandler error", e);
            ctx.writeAndFlush(new TextWebSocketFrame("{\"error\":\"" + e.getMessage() + "\"}"));
        }
    }

    public static class ChatPayload {
        public Long roomId;
        public Long senderId;
        public String content;
        public String fileUrl;
        public String fileType;
        public String type; // "join" or null
    }
}

