package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.*;
import lombok.Getter;

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

    public static MessengerMember create(MessengerRoom room, Member member) {
        MessengerMember mm = new MessengerMember();
        mm.room = room;
        mm.member = member;
        mm.joinedAt = LocalDateTime.now();
        return mm;
    }
}
