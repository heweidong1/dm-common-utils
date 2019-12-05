package com.kgc.dm.utils.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurationSelector
{
    //开启缓存

    @Bean
    public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate)
    {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

    //配置注册RedisTemplate 到 Spring

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory)
    {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

}
