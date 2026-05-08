package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "messenger_message")
@Getter
public class MessengerMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private MessengerRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Column(length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType messageType;

    @Column(length = 1000)
    private String fileUrl;

    private LocalDateTime sentAt;

    protected MessengerMessage() {}

    @NonNull
    public static MessengerMessage createText(@NonNull MessengerRoom room, @NonNull Member sender, @NonNull String content) {
        MessengerMessage msg = new MessengerMessage();
        msg.room = room;
        msg.sender = sender;
        msg.content = content;
        msg.messageType = MessageType.TEXT;
        msg.sentAt = LocalDateTime.now();
        return msg;
    }

    @NonNull
    public static MessengerMessage createFile(@NonNull MessengerRoom room, @NonNull Member sender, @NonNull String content, String fileUrl, @NonNull MessageType type) {
        MessengerMessage msg = new MessengerMessage();
        msg.room = room;
        msg.sender = sender;
        msg.content = content;
        msg.fileUrl = fileUrl;
        msg.messageType = type;
        msg.sentAt = LocalDateTime.now();
        return msg;
    }
}
