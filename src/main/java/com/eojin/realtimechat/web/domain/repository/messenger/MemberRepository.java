package com.eojin.realtimechat.web.domain.repository.messenger;

import com.eojin.realtimechat.web.domain.entity.messenger.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
