package com.eojin.realtimechat.web.service.consultation;

import com.eojin.realtimechat.web.domain.entity.consultation.Consultation;
import com.eojin.realtimechat.web.domain.entity.consultation.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByStatusOrderByCreatedAtAsc(ConsultationStatus consultationStatus);

}
