package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.Entity;
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
@Table(name = "messenger_member")
@Getter
public class MessengerMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private MessengerRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime joinedAt;

    protected MessengerMember() {}

    @NonNull
    public static MessengerMember create(@NonNull MessengerRoom room, @NonNull Member member) {
        MessengerMember mm = new MessengerMember();
        mm.room = room;
        mm.member = member;
        mm.joinedAt = LocalDateTime.now();
        return mm;
    }
}
