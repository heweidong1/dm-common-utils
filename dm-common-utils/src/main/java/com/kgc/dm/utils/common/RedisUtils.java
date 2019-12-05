package com.kgc.dm.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * RedisAPI
 *
 * @author bdqn_shang
 * @date 2018-1-10
 */
@Component
public class RedisUtils {

    private Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * set key and value to redis
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
        return true;
    }

    /**
     * set key and value to redis
     *
     * @param key
     * @param seconds 有效期
     * @param value
     * @return
     */
    public boolean set(String key,String value, long seconds ) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key,value,seconds,TimeUnit.MINUTES);
        return true;
    }

    /**
     * 判断某个key是否存在
     * 不存在返回false
     * 存在返回true
     * @param key
     * @return
     */
    public boolean exist(String key) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        Object value = vo.get(key);
        return EmptyUtils.isEmpty(value) ? false : true;
    }

    public Object get(String key) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	 //获取值的过期时间
    public Long ttl(String key)
    {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Long expire = redisTemplate.getExpire(key);
        return expire;
    }
    //使用redis的setnx命令实现分布式锁

    /***
     *
     * @param key 商品对应的唯一的用于分布式锁的key值
     * @return 持有锁
     */
    public boolean lock(final String key){
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();
                byte keyByte[]=stringRedisSerializer.serialize(key);
                //value可以随意设置
                byte valueByte[]=stringRedisSerializer.serialize("lock");
                boolean flag=redisConnection.setNX(keyByte,valueByte);
                //防止死锁，设置最大处理的超时时间
                if(flag) {
                    redisConnection.expire(keyByte,Constants.Redis_Expire.DEFAULT_EXPIRE);
                }
                return flag;
            }
        });
    }
    /***
     * 释放锁
     * @param key
     * @return
     */
    public void unLock(String key){
        redisTemplate.setKeySerializer(new StringRedisSerializer());//设置序列化
        redisTemplate.delete(key);
    }
    public boolean validate(String token) {
        return exist(token);
    }
}
