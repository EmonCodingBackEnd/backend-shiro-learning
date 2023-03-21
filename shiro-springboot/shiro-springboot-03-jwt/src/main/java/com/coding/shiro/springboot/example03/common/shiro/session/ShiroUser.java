
package com.coding.shiro.springboot.example03.common.shiro.session;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 */
@Data
public class ShiroUser implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5024855628064590607L;

    /**
     * 主键
     */
    private String id;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 加密因子
     */
    private String salt;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String zipcode;

    /**
     * 地址
     */
    private String address;

    /**
     * 固定电话
     */
    private String tel;

    /**
     * 电话
     */
    private String mobil;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职务
     */
    private String duties;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 是否有效
     */
    private String enableFlag;

    private List<String> resourceIds;

    public ShiroUser() {
        super();
    }

    public ShiroUser(String id, String loginName) {
        super();
        this.id = id;
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "ShiroUser{" + "id='" + id + '\'' + '}';
    }
}
