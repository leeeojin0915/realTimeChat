package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
@Getter
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    private LocalDateTime createdAt;

    protected Friendship() {}

    public static Friendship create(Member member, Member friend) {
        Friendship fs = new Friendship();
        fs.member = member;
        fs.friend = friend;
        fs.createdAt = LocalDateTime.now();
        return fs;
    }
}
