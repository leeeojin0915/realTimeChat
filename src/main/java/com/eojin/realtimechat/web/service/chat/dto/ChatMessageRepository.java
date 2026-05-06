package com.eojin.realtimechat.web.service.chat;

import com.eojin.realtimechat.web.domain.entity.chat.ChatMessage;
import com.eojin.realtimechat.web.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderBySendDateAsc(ChatRoom chatRoom);
}
