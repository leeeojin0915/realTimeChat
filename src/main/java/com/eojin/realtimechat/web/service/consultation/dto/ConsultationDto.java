package com.eojin.realtimechat.web.service.consultation;

import com.eojin.realtimechat.web.domain.entity.consultation.Consultation;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class ConsultationDto {
    /*
    * 상담 요청 생성 시, 클라이언트에서 보내는 request body
    * */
    public record CreateConsultationRequest(
            @NotBlank(message = "고객명을 입력해주세요.")
            String customerName,
            @NotBlank(message = "문의 내용을 입력해주세요.")
            String issueContent
    ){}
    /*
    * 상담 상세 응답 DTO
    * 생성 직후 또는 상담 상세 조회 등에 사용
    * */
    public record ConsultationResponse(
            Long id,
            String customerName,
            String issueContent,
            String status,
            String category,
            String priority,
            String aiSummary,
            Date createDate,
            Date closedDate
    ){
        /*
        * Consultation 엔티티 -> Response DTO 변환
        * */
        public static ConsultationResponse from(Consultation consultation){
            return new ConsultationResponse(
                    consultation.getId(),
                    consultation.getCustomerName(),
                    consultation.getIssueContent(),
                    consultation.getConsultationStatus() != null ? consultation.getConsultationStatus().name() : null,
                    consultation.getConsultationCategory() != null ? consultation.getConsultationCategory().name() : null,
                    consultation.getPriority(),
                    consultation.getAiSummary(),
                    consultation.getCreateDate(),
                    consultation.getCloasedDate()
            );
        }
    }

    /*
    * 상담 리스트용 응답 DTO
    * 목록 화면에서 필요한 필드만 노출
    * */
    public record ConsultationList(
            Long id,
            String customerName,
            String status,
            String category,
            String priority,
            String aiSummary,
            Date createDate
    ){
        public static ConsultationResponse from(Consultation consultation){
            return new ConsultationResponse(
                    consultation.getId(),
                    consultation.getCustomerName(),
                    consultation.getConsultationStatus().name(),
                    consultation.getConsultationCategory() != null ? consultation.getConsultationCategory().name() : null,
                    consultation.getPriority(),
                    consultation.getAiSummary(),
                    consultation.getCreateDate()
            );
        }
    }


}
