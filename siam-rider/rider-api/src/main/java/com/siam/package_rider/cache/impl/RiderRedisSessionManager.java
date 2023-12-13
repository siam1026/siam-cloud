package com.siam.package_rider.cache.impl;

import com.siam.package_common.util.JsonUtils;
import com.siam.package_rider.cache.RiderSessionManager;
import com.siam.package_rider.entity.Rider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RiderRedisSessionManager implements RiderSessionManager {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void createSession(String token, Rider rider) {
        //设置7天过期
        redisTemplate.opsForValue().set(SESSION_PREFIX + token, JsonUtils.toJson(rider), 7 * 24, TimeUnit.HOURS);
    }

    @Override
    public Rider getSession(String token) {
        Object o = redisTemplate.opsForValue().get(SESSION_PREFIX + token);
        if(o != null){
            Rider rider = (Rider) JsonUtils.toObject(o.toString(), Rider.class);
            return rider;
        }
        return null;
    }

    @Override
    public void removeSession(String token) {
        redisTemplate.delete(SESSION_PREFIX + token);
    }

    @Override
    public boolean haveSession(String token) {
        return redisTemplate.hasKey(SESSION_PREFIX + token);
    }
}