package com.eojin.realtimechat.web.domain.entity.messenger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.lang.NonNull;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    private LocalDateTime createdAt;

    protected Member() {}

    @NonNull
    public static Member create(@NonNull String username, @NonNull String password, @NonNull String nickname, String profileImageUrl) {
        Member member = new Member();
        member.username = username;
        member.password = password;
        member.nickname = nickname;
        member.profileImageUrl = profileImageUrl;
        member.createdAt = LocalDateTime.now();
        return member;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        if(nickname != null) this.nickname = nickname;
        if(profileImageUrl != null) this.profileImageUrl = profileImageUrl;
    }
}
