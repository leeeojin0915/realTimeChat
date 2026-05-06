package com.eojin.realtimechat.web.mapper;

import com.eojin.realtimechat.web.domain.dto.MessengerRoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MessengerMapper {
    List<MessengerRoomDTO> findRoomsByMemberId(@Param("memberId") Long memberId);
    Long findExistingRoom(@Param("memberId") Long memberId, @Param("friendId") Long friendId);
}
