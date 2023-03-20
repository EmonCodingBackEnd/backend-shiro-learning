package com.coding.shiro.springboot.example02.common.shiro.cache.exception;

public class SerializationException extends Exception {
    public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
