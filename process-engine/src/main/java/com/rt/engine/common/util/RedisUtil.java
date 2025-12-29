package com.rt.engine.common.util;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据key获取值
     *
     * @param key
     *            键
     * @return 值
     */
    public Object get(final String key) {
        if (key == null) {
            return null;
        }
        if (redisTemplate.opsForValue() != null) {
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    /**
     * 查询string类型的值
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param expireTime
     *            过期时间
     * @param timeUnit
     *            过期时间单位 (SECONDS：秒 MINUTES：分钟 HOURS：小时)
     */
    public void set(final String key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    /**
     * 删除键
     *
     * @param key
     *            键
     */
    public void delete(final String key) {
        redisTemplate.delete(key);
    }
}
