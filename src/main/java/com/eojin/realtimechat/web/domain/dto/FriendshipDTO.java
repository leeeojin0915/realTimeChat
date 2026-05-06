package com.eojin.realtimechat.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipDTO {
    private Long id;
    private FriendInfo friend;
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FriendInfo {
        private Long id;
        private String username;
        private String nickname;
        private String profileImageUrl;
    }
}
