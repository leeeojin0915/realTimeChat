package com.eojin.realtimechat.web.domain.repository.messenger;

import com.eojin.realtimechat.web.domain.entity.messenger.MessengerMessage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessengerMessageRepository extends JpaRepository<MessengerMessage, Long> {
    @EntityGraph(attributePaths = {"sender"})
    List<MessengerMessage> findByRoomIdOrderBySentAtAsc(Long roomId);
    void deleteByRoomId(Long roomId);
}
