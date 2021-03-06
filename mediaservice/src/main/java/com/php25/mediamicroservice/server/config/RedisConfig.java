package com.php25.mediamicroservice.server.config;

import com.php25.common.redis.RedisManager;
import com.php25.common.redis.RedisManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * Created by penghuiping on 16/8/26.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisManager redisService(@Autowired StringRedisTemplate stringRedisTemplate) {
        return new RedisManagerImpl(stringRedisTemplate);
    }


    @Bean
    public LockRegistry lockRegistry(@Autowired RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "media-microservice");
    }
}

