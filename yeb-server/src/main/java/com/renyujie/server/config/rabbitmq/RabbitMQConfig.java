package com.renyujie.server.config.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.renyujie.server.pojo.MailConstants;
import com.renyujie.server.pojo.MailLog;
import com.renyujie.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName RabbitMQConfig.java
 * @Description RabbitMQ配置类
 * @createTime 2022年01月04日 22:43:00
 */
@Configuration
public class RabbitMQConfig {
    public static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调,确认消息是否到达broker 确认成功,更新投递状态为消息投递成功
         * data:消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            System.out.println("RabbitMQConfig: msgId = " + msgId);
            if (ack){
                logger.info("{}==========>消息发送成功",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",MailConstants.MSG_SUCCESS).eq("msgId",msgId));
            }else {
                logger.info("{}==========>消息发送失败",msgId);
            }
        });
        /**
         * 消息失败回调，比如router不到queue时回调
         * 这里消息失败并没有开启定时任务  仅仅只是打印日志  定时任务在task.MailTask中实现
         * msg:消息主题
         * repCode:响应码
         * repText:响应描述
         * exchange:交换机
         * routingKey:路由键
         * */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            logger.info("{}=======================>消息发送到queue时失败",msg.getBody());
        });
        return rabbitTemplate;
    }

    /**
     * @Description: 队列
     */
    @Bean
    public Queue queue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true);
    }

    /**
     * @Description: 交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    /**
     * @Description: 队列与交换机用路由键绑定
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }
}
