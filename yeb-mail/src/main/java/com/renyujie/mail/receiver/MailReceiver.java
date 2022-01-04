package com.renyujie.mail.receiver;

import com.rabbitmq.client.Channel;
import com.renyujie.server.pojo.Employee;
import com.renyujie.server.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MailReceiver.java
 * @Description 邮件接受者（mq的消费端）
 * @createTime 2022年01月04日 22:41:00
 */
@Component
public class MailReceiver {
    public static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 邮件发送（mq消费者）
     */
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {

        //获取消息体中的员工信息
        Employee employee = (Employee) message.getPayload();
        System.out.println("MailReceiver:  employee = " + employee);
        //获取Header中的消息序号tag
        MessageHeaders headers = message.getHeaders();
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("tag = " + tag);
        //获取Header中的消息id msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");
        System.out.println("msgId = " + msgId);

        //hash  自己准备了rediskey  hashkey是msgId(最重要) hashvalue无所谓
        HashOperations hash = redisTemplate.opsForHash();

        try {
            //1.首先去redis中判断下有没有过对应的msgId，如果有代表消费者再次接收到一样的消息 那就直接手动确认消息（代表之前已消费）并return
            if (hash.entries("mail_log").containsKey(msgId)) {
                //redis中包含key，说明消息已经被消费
                logger.info("消息已经被消费========>{}", msgId);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否多条
                 */
                channel.basicAck(tag, false);
                return;
            }

            //2.redis中没有就正常消费
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(employee.getEmail());
            helper.setSubject("入职邮件");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            //发送邮件
            javaMailSender.send(msg);
            logger.info("邮件发送成功");

            //3.消费完的同时将消息msgId存入redis，也要手动确认消息（代表消费完）
            hash.put("mail_log", msgId, "OK");
            System.out.println("MailReceiver: redis---> msgId = " + msgId);
            /**
             * 手动确认消息
             * tag:消息序号
             * multiple:是否多条
             */
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                //捕捉到异常 就退回消息，也手动确认下（代表不消费 直接退回队列）
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否多条
                 * requeue:消息没有被确认是否退回到队列
                 */
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                //ioException.printStackTrace();
                logger.error("消息确认失败=====>{}", ioException.getMessage());
            }
            logger.error("MailReceiver + 邮件发送失败========{}", e.getMessage());
        }
    }

    /*
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Employee employee) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(employee.getEmail());
            helper.setSubject("入职邮件");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("MailReceiver + 邮件发送失败========{}", e.getMessage());
        }
    }
     */
}
