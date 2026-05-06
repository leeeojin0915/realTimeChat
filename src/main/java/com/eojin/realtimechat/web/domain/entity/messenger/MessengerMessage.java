package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.*;
import lombok.Getter;

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

    public static MessengerMessage createText(MessengerRoom room, Member sender, String content) {
        MessengerMessage msg = new MessengerMessage();
        msg.room = room;
        msg.sender = sender;
        msg.content = content;
        msg.messageType = MessageType.TEXT;
        msg.sentAt = LocalDateTime.now();
        return msg;
    }

    public static MessengerMessage createFile(MessengerRoom room, Member sender, String content, String fileUrl, MessageType type) {
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
