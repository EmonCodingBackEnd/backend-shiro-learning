package com.coding.shiro.springboot.example01.common.shiro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroRedisCacheManager implements CacheManager {

    public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
    private final String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;

    public static final int DEFAULT_EXPIRE = 1800;
    private final int expire = DEFAULT_EXPIRE;

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public ShiroRedisCacheManager(RedissonClient redissonClient, ObjectMapper objectMapper) {
        this.redissonClient = redissonClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.info("get cache, name=" + name);

        Cache<K, V> cache = caches.get(name);

        if (cache == null) {
            cache = new ShiroRedisCache<K, V>(keyPrefix + name + ":", expire, redissonClient, objectMapper);
            caches.put(name, cache);
        }
        return cache;
    }

}
