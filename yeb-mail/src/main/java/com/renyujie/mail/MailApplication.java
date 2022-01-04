package com.renyujie.mail;

import com.renyujie.server.pojo.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MailApplication.java
 * @Description 邮件启动类
 * @createTime 2022年01月04日 21:18:00
 */
//在yeb-mail.pom中引入了yeb-server的依赖 而yeb-server本身是有数据库依赖的 这样会导致直接启动MailApplication报DataSource的错误  所以去掉这个依赖
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class,args);
    }

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

}


