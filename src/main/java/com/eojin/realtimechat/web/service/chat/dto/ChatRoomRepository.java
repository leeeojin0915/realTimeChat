package com.eojin.realtimechat.web.service.chat;

import com.eojin.realtimechat.web.domain.entity.chat.ChatRoom;
import com.eojin.realtimechat.web.domain.entity.consultation.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByConsultation(Consultation consultation);
}
