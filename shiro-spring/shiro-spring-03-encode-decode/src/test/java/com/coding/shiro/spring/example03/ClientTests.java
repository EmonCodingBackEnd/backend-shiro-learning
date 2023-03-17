package com.coding.shiro.spring.example03;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.coding.shiro.spring.example03.client.EncodeUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientTests {

    @Test
    public void testHex() {
        String val = "hello shiro";
        String flag = EncodeUtil.encodeHex(val.getBytes(StandardCharsets.UTF_8));
        String valHandler = new String(EncodeUtil.decodeHex(flag));
        Assertions.assertEquals(val, valHandler);
    }

    @Test
    public void testBase64() {
        String val = "hello shiro";
        String flag = EncodeUtil.encodeBase64(val.getBytes(StandardCharsets.UTF_8));
        String valHandler = new String(EncodeUtil.decodeBase64(flag));
        Assertions.assertEquals(val, valHandler);
    }
}
