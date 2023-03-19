package com.coding.shiro.springboot.example01.login.common.shiro.cache.serializer;


import com.coding.shiro.springboot.example01.login.common.shiro.cache.exception.SerializationException;

public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
}
