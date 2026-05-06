package com.eojin.realtimechat.web.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessengerRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    // For 1:1 chats, the other person's name and profile image
    private String otherMemberName;
    private String otherMemberProfileImage;
}
