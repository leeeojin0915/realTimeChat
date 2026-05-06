package com.eojin.realtimechat.web.domain.entity.chat;

import com.eojin.realtimechat.web.domain.entity.consultation.Consultation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;
    private String roomName;
    private boolean closed;
    private Date createDate;
    private Date closedDate;
}
