package com.coding.shiro.spring.example04.tools;

import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DigestUtilTest {

    @Test
    public void testDigest() {
        Map<String, String> map = DigestUtil.entryptPassword("emon123");
        System.out.println("map = " + map);
    }

}