package com.eojin.realtimechat.web.mapper;

import com.eojin.realtimechat.web.domain.dto.MessengerRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.NonNull;
import java.util.List;

@Mapper
public interface MessengerMapper {
    List<MessengerRoomDTO> findRoomsByMemberId(@Param("memberId") @NonNull Long memberId);
    Long findExistingRoom(@Param("memberId") @NonNull Long memberId, @Param("friendId") @NonNull Long friendId);
}
