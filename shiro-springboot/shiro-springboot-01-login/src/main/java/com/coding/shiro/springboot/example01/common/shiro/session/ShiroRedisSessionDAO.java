package com.coding.shiro.springboot.example01.common.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import com.coding.shiro.springboot.example01.util.ShiroRedissionSerialize;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroRedisSessionDAO extends AbstractSessionDAO {

    private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";
    private final String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;

    private long globalSessionTimeoutInMills;

    public void setGlobalSessionTimeoutInMills(long globalSessionTimeoutInMills) {
        this.globalSessionTimeoutInMills = globalSessionTimeoutInMills;
    }

    private final RedissonClient redissonClient;

    public ShiroRedisSessionDAO(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String getKey(Serializable sessionId) {
        return this.keyPrefix + sessionId;
    }

    private String getKeyPattern() {
        return keyPrefix + "*";
    }

    @Override
    protected Serializable doCreate(Session session) {
        // 创建唯一标识的sessionId
        Serializable sessionId = this.generateSessionId(session);
        // 为session会话指定唯一的sessionId
        this.assignSessionId(session, sessionId);
        // 放入缓存中
        String key = getKey(sessionId.toString());
        log.info("doCreate=====>{}", key);
        // key = ShiroRedissionSerialize.serialize(key);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(ShiroRedissionSerialize.serialize(session), globalSessionTimeoutInMills / 1000, TimeUnit.SECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = getKey(sessionId.toString());
        log.info("doReadSession=====>{}", key);
        // key = ShiroRedissionSerialize.serialize(key);
        RBucket<String> bucket = redissonClient.getBucket(key);
        String sessionString = bucket.get();
        Session session = (Session)ShiroRedissionSerialize.deserialize(sessionString);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        String key = getKey(session.getId().toString());
        log.info("update=====>{}", key);
        // key = ShiroRedissionSerialize.serialize(key);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(ShiroRedissionSerialize.serialize(session), globalSessionTimeoutInMills / 1000, TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        String key = getKey(session.getId().toString());
        log.info("delete=====>{}", key);
        // key = ShiroRedissionSerialize.serialize(key);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    @Override
    public Collection<Session> getActiveSessions() {
        /*String key = getKeyPattern();
        String[] objects = (String[])redissonClient.getKeys().getKeysStreamByPattern(key).toArray();
        Map<String, Session> stringObjectMap = redissonClient.getBuckets().get(objects);
        return stringObjectMap.values();*/
        return Collections.emptySortedSet();
    }
}
