package com.renyujie.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName ChatMsg.java
 * @Description 聊天消息类
 * @createTime 2022年01月05日 22:12:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChatMsg {
    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String fromNickName;
}
