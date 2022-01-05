package com.renyujie.server.controller;

import com.renyujie.server.pojo.Admin;
import com.renyujie.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName WenSocketController.java
 * @Description WenSocket的接口
 * @createTime 2022年01月05日 22:18:00
 */
@Controller
//注意 别用restcontroller  因为只用到了MessageMapping  并没有用到restful风格的posttmaping,getmapping...
public class WenSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg){
        //先获取当前用户对象
        Admin admin = (Admin) authentication.getPrincipal();
        //登录用户名做来源名称
        chatMsg.setFrom(admin.getUsername());
        //前端显示用户名做昵称
        chatMsg.setFromNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        /**
         * 发送消息
         * 1.消息接收者
         * 2.消息队列（这里面的/queue是之前在WebSocketConfig.configureMessageBroker设置的）
         * 3.消息对象
         */
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
    }
}
