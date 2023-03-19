package com.coding.shiro.spring.example07.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 生成摘要
 */
public class DigestUtil {

    public static final String SHA1 = "SHA-1";
    public static final Integer ITERATIONS = 512;

    /**
     * SHA1摘要算法
     * 
     * @param input - 明文字符串
     * @param salt - 干扰因子
     * @return - 摘要
     */
    public static String sha1(String input, String salt) {
        return new SimpleHash(SHA1, input, salt, ITERATIONS).toString();
    }

    /**
     * 随机生成干扰因子salt
     * 
     * @return hex编码的salt
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 生成密码和salt的密文
     * 
     * @param rawPassword 明文密码
     * @return Map->salt和密文密码
     */
    public static Map<String, String> entryptPassword(String rawPassword) {
        Map<String, String> map = new HashMap<>();
        String salt = generateSalt();
        String password = sha1(rawPassword, salt);
        map.put("salt", salt);
        map.put("password", password);
        return map;
    }

}
