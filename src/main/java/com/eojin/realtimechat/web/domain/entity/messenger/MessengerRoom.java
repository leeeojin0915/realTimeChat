package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messenger_room")
@Getter
public class MessengerRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    protected MessengerRoom() {}

    public static MessengerRoom create(String name) {
        MessengerRoom room = new MessengerRoom();
        room.name = name;
        room.createdAt = LocalDateTime.now();
        return room;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
}
