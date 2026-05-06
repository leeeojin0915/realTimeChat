package com.eojin.realtimechat.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileUrl;
    private String content;
    private String fileUrl;
    private String messageType;
    private String sentAt;
}
