package com.sky.config;

/**
 * @Author Tians
 * @Date 2023/9/5 18:02
 * @Version 17
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置Redis
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建Redis模板对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 获得Redis的连接工厂对象 用于创建Redis的连接
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置RedisTemplate对象的key序列化器，用于将Redis的key序列化为字符串。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
        //
    }
}
