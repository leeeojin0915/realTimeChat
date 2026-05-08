package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.lang.NonNull;

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

    @NonNull
    public static MessengerRoom create(@NonNull String name) {
        MessengerRoom room = new MessengerRoom();
        room.name = name;
        room.createdAt = LocalDateTime.now();
        return room;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
}
