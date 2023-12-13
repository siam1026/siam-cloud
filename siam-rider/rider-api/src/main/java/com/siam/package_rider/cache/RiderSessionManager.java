package com.siam.package_rider.cache;

import com.siam.package_rider.entity.Rider;

/**
 * 会话管理
 */
public interface RiderSessionManager {

    //缓存前缀
    String SESSION_PREFIX = "LOGIN_MERCHANT:";

    /**
     * 创建会话
     */
    void createSession(String token, Rider rider);

    /**
     * 获取会话
     */
    Rider getSession(String token);

    /**
     * 删除会话
     */
    void removeSession(String token);

    /**
     * 是否已经登录
     */
    boolean haveSession(String token);
}