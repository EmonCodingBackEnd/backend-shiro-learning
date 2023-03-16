package com.coding.shiro.spring.example01.login;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroMD5 {
    public static void main(String[] args) {
        // 密码明文
        String password = "emon123";
        // 使用MD5加密
        Md5Hash md5Hash = new Md5Hash(password);
        System.out.println("md5Hash加密结果 = " + md5Hash);
        // 使用MD5加盐加密，盐就是在密码明文后拼接新字符串，然后再进行加密
        Md5Hash md5Hash2 = new Md5Hash(password, "salt");
        System.out.println("md5Hash2加盐加密结果 = " + md5Hash2);
        // 为了保证安全，避免被破解还可以多次迭代加密，保证数据安全
        Md5Hash md5Hash3 = new Md5Hash(password, "salt", 3);
        System.out.println("md5Hash3加盐迭代3次加密结果 = " + md5Hash3);
        // 使用父类进行加密
        SimpleHash simpleHash = new SimpleHash("MD5", password, "salt", 1);
        System.out.println("simpleHash = " + simpleHash);
        System.out.println(simpleHash.equals(md5Hash2));

    }
}
