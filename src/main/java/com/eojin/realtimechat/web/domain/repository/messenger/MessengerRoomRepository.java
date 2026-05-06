package com.eojin.realtimechat.web.domain.repository.messenger;

import com.eojin.realtimechat.web.domain.entity.messenger.MessengerRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessengerRoomRepository extends JpaRepository<MessengerRoom, Long> {
}
