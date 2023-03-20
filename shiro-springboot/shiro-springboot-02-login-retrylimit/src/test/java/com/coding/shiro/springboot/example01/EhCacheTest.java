package com.coding.shiro.springboot.example01;

import java.io.InputStream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EhCacheTest {
    @Test
    public void test() {
        // 获取编译目录下的资源的流对象
        InputStream inputStream = EhCacheTest.class.getClassLoader().getResourceAsStream("ehcache.xml");
        // 获取EhCache的缓存管理对象
        CacheManager cacheManager = new CacheManager(inputStream);
        // 获取缓存对象
        Cache helloWorldCache = cacheManager.getCache("HelloWorldCache");
        // 创建缓存数据
        Element element = new Element("name", "zhangsan");
        // 存入缓存
        helloWorldCache.put(element);
        // 从缓存中读取数据输出
        Element nameCache = helloWorldCache.get("name");
        System.out.println("nameCache = " + nameCache.getObjectValue());
    }
}
