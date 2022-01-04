package com.renyujie.server.pojo;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MailConstants.java
 * @Description 邮件功能  消息状态常量表
 * @createTime 2022年01月04日 22:01:00
 */
public class MailConstants {
    //消息投递中
    public static final Integer MSG_DELIVERING = 0;
    //消息投递成功
    public static final Integer MSG_SUCCESS = 1;
    //消息投递失败
    public static final Integer MSG_FAILURE = 2;
    //最大重试次数
    public static final Integer MAX_TRY_COUNT = 3;
    //消息超时时间
    public static final Integer MSG_TIMEOUT = 1;
    //队列
    public static final String MAIL_QUEUE_NAME = "yuneban-mail.queue";
    //交换机
    public static final String MAIL_EXCHANGE_NAME = "yuneban-mail.exchange";
    //路由键
    public static final String MAIL_ROUTING_KEY_NAME = "yuneban-mail.routing.key";
}
