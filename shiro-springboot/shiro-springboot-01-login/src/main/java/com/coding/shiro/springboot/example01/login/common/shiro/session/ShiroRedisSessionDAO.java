package com.coding.shiro.springboot.example01.login.common.shiro.session;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final ObjectMapper objectMapper;

    public ShiroRedisSessionDAO(RedissonClient redissonClient, ObjectMapper objectMapper) {
        this.redissonClient = redissonClient;
        this.objectMapper = objectMapper;
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
        RBucket<Session> bucket = redissonClient.getBucket(key);
        bucket.setIfAbsent(session, Duration.ofSeconds(globalSessionTimeoutInMills / 1000));
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = getKey(sessionId.toString());
        log.info("doReadSession=====>{}", key);
        RBucket<Session> bucket = redissonClient.getBucket(key);
        Session session = bucket.get();
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        String key = getKey(session.getId().toString());
        log.info("update=====>{}", key);
        RBucket<Session> bucket = redissonClient.getBucket(key);
        bucket.setIfAbsent(session, Duration.ofSeconds(globalSessionTimeoutInMills / 1000));
    }

    @Override
    public void delete(Session session) {
        String key = getKey(session.getId().toString());
        log.info("delete=====>{}", key);
        RBucket<Session> bucket = redissonClient.getBucket(key);
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
