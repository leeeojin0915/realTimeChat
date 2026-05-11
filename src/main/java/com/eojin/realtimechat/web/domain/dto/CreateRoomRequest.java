package com.eojin.realtimechat.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    @NonNull
    private Long memberId;
    @NonNull
    private List<Long> friendIds;
}
