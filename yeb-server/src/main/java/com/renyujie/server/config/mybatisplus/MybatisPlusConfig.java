package com.renyujie.server.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName MybatisPlusConfig.java
 * @Description 分页配置
 * @createTime 2021年12月28日 20:28:00
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
