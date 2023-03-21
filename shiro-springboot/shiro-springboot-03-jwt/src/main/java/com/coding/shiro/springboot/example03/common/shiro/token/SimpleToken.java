
package com.coding.shiro.springboot.example03.common.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义token
 */
@Getter
@Setter
public class SimpleToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -4849823851197352099L;

    private String tokenType;

    public SimpleToken(String username, String password, String tokenType) {
        super(username, password);
        this.tokenType = tokenType;
    }
}
