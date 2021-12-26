package com.renyujie.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName YebApplication.java
 * @Description 启动类
 * @createTime 2021年12月20日 17:03:00
 */
@SpringBootApplication(scanBasePackages = {"com.renyujie.server","com.renyujie.server.exception"})
@MapperScan("com.renyujie.server.mapper")
public class YebApplication {
    public static void main(String[] args) {
        SpringApplication.run(YebApplication.class, args);
    }
}
