package com.eojin.realtimechat.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String nickname;
    private String profileImageUrl;
}
