package com.eojin.realtimechat.web.vo;

import com.eojin.realtimechat.web.entity.ConsultationCategory;
import com.eojin.realtimechat.web.entity.ConsultationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Consultation {
    private Long id;
    private String customerName;
    private String issueContent;
    private ConsultationStatus consultationStatus;
    private ConsultationCategory consultationCategory;
    private String priority;
    private String aiSummary;
    private Date createDate;
    private Date cloasedDate;
}
