package com.eojin.realtimechat.web.domain.repository.messenger;

import com.eojin.realtimechat.web.domain.entity.messenger.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByMemberId(Long memberId);
    boolean existsByMember_IdAndFriend_Id(Long memberId, Long friendId);
}
