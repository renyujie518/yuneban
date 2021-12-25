package com.renyujie.server.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName RedisConfig.java
 * @Description redis配置类（序列化）
 * @createTime 2021年12月25日 16:48:00
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //String类型 key序列器
        template.setKeySerializer(new StringRedisSerializer());
        //String类型 value序列器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //Hash类型 key序列器
        template.setHashKeySerializer(new StringRedisSerializer());
        //Hash类型 value序列器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //放置连接工厂
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
