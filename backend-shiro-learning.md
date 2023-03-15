# backend-shiro-learning

Shiro学习：

Shiro1.9：https://www.bilibili.com/video/BV11e4y1n7BH/?spm_id_from=333.337.search-card.all.click&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

Netty：https://www.bilibili.com/video/BV1DJ411m7NR/?spm_id_from=333.337.search-card.all.click&vd_source=b850b3a29a70c8eb888ce7dff776a5d1



# 概念整理

| 名称           | 含义                  |                |
| -------------- | --------------------- | -------------- |
| authentication | 认证                  | ɔːˌθentɪˈkeɪʃn |
| authenticator  | 认证者                | ɔːˈθɛntɪkeɪtə  |
| authorization  | 授权                  | ˌɔːθəraɪˈzeɪʃn |
| authorizer     | 授权人                |                |
| principals     | 身份，比如：用户名    | ˈprɪnsəpəlz    |
| credentials    | 证明/凭证，比如：密码 | krəˈdenʃlz     |

## 授权

也叫访问控制，即在应用中控制谁访问哪些资源（如访问页面/编辑数据/页面操作等），在授权中需了解的几个关键对象：主体（Subject）、资源（Resource）、权限（Permission）、角色（Role）。

## 主体（Subject）

访问应用的用户，在Shiro中使用Subject代表盖用户。用户只有授权后才允许访问相应的资源。

## 资源（Resource）

在应用中用户科院访问的URL，比如访问JSP页面、查看/编辑某些数据、访问某个业务方法、打印文本等等都是资源。用户只有授权后才能访问。

## 权限（Permission）

安全策略中的原子授权单位，通过权限我们可以表示在应用中用户有没有操作某个资源的权利。即权限表示在应用中用户能不能访问某个资源。如：访问用户列表页面查看/新增/修改/删除用户数据（即很多时候都是CRUD式权限控制）等。权限代表了用户有没有操作某个资源的权利，即反映在某个资源上的操作允不允许。

## Shiro支持的粒度

Shiro支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权限，即实例级别的）。

## 角色（Role）

权限的集合，一般情况下会赋予用户角色而不是权限，即这样用户可以拥有一组权限，赋予权限时比较方便。典型的如：项目经理、技术总监、CTO、开发工程师等都是角色，不同的角色拥有一组不同的权限。

