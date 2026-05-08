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

    @NonNull
    public static Friendship create(@NonNull Member member, @NonNull Member friend) {
        Friendship fs = new Friendship();
        fs.member = member;
        fs.friend = friend;
        fs.createdAt = LocalDateTime.now();
        return fs;
    }
}
