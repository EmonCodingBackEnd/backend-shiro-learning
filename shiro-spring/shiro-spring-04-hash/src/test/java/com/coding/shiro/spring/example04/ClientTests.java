package com.coding.shiro.spring.example04;

import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.coding.shiro.spring.example04.client.DigestUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientTests {

    @Test
    public void testDigest() {
        Map<String, String> map = DigestUtil.entryptPassword("emon123");
        System.out.println("map = " + map);
    }
}
