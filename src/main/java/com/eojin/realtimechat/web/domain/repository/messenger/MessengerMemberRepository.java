package com.eojin.realtimechat.web.domain.repository.messenger;

import com.eojin.realtimechat.web.domain.entity.messenger.MessengerMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessengerMemberRepository extends JpaRepository<MessengerMember, Long> {
    List<MessengerMember> findByRoomId(Long roomId);
    List<MessengerMember> findByMemberId(Long memberId);
    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);
    void deleteByRoomIdAndMemberId(Long roomId, Long memberId);
}
