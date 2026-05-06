package com.eojin.realtimechat.web.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Chat {
    private String roomId;
    private String sender;
    private String message;
    private Date time;
}
