package com.coding.shiro.springboot.example02.common.shiro.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroRedisCache<K, V> implements Cache<K, V> {

    private final String keyPrefix;
    private final int expire;

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public ShiroRedisCache(String keyPrefix, int expire, RedissonClient redissonClient, ObjectMapper objectMapper) {
        this.keyPrefix = keyPrefix;
        this.expire = expire;
        this.redissonClient = redissonClient;
        this.objectMapper = objectMapper;
    }

    private String getKey(K k) {
        if (null == k) {
            return null;
        }
        if (k instanceof PrincipalCollection) {
            Object primaryPrincipal = ((PrincipalCollection)k).getPrimaryPrincipal();
            return keyPrefix + primaryPrincipal.toString();
        }
        try {
            return keyPrefix + objectMapper.writeValueAsString(k);
        } catch (JsonProcessingException e) {
            log.error("", e);
            return null;
        }
    }

    private String getKeyPattern() {
        return keyPrefix + "*";
    }

    @Override
    public V get(K k) throws CacheException {
        String key = getKey(k);
        RBucket<V> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        String key = getKey(k);
        RBucket<V> bucket = redissonClient.getBucket(key);
        bucket.set(v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        String key = getKey(k);
        RBucket<V> bucket = redissonClient.getBucket(key);
        return bucket.getAndDelete();
    }

    @Override
    public void clear() throws CacheException {
        String key = keyPrefix + ":" + "*";
        redissonClient.getKeys().deleteByPattern(key);
    }

    @Override
    public int size() {
        String key = keyPrefix + ":" + "*";
        return (int)redissonClient.getKeys().getKeysStreamByPattern(key).count();
    }

    @Override
    public Set<K> keys() {
        String key = getKeyPattern();
        return redissonClient.getKeys().getKeysStreamByPattern(key).map(e -> {
            try {
                return objectMapper.readValue(e, new TypeReference<K>() {});
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        String key = getKeyPattern();
        String[] objects = (String[])redissonClient.getKeys().getKeysStreamByPattern(key).toArray();
        Map<String, V> stringObjectMap = redissonClient.getBuckets().get(objects);
        return stringObjectMap.values();
    }
}
