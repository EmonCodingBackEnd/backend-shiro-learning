package com.coding.shiro.springboot.example03.common.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "shiro.jwt")
public class JwtProperties {

    // @Value("${shiro.jwt.hexEncodedSecretKey:simpleSecret}")
    private String hexEncodedSecretKey = "simpleSecret";
    private String issuer = "system"; // jwt签发者
}
