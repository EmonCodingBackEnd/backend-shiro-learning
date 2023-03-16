# backend-shiro-learning

Shiro学习：

Shiro1.9：https://www.bilibili.com/video/BV11e4y1n7BH?p=13&spm_id_from=pageDriver&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

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
| realm          | 领域                  | relm           |

## 授权

也叫访问控制，即在应用中控制谁访问哪些资源（如访问页面/编辑数据/页面操作等），在授权中需了解的几个关键对象：主体（Subject）、资源（Resource）、权限（Permission）、角色（Role）。

## 主体（Subject）

​		代表了当前“用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，如网络爬虫，机器人等；即一个抽象概念；所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；可以把Subject认为是一个门面；SecurityManager才是实际的执行者。

## 安全管理器（SecurityManager）

即所有与安全有关的操作都会与SecurityManager交互；且它管理着所有Subject；可以看出它是Shiro的核心，它负责与后边介绍的其他组件进行交互，如果学习过SpringMVC，你可以把它看成DispatcherServlet前端控制器。

包含：

- 认证器（Authenticator）

- 授权器（Authorizer）

- Session Manager

  Web应用中一般是用Web容器（中间件tomcat）对session进行管理，shiro也提供一套session管理方式。Shiro不仅可以用于Web管理也可以用于CS管理，所以他不用Web容器的session管理。

- SessionDao

  通过SessionDao管理session数据，针对个性化的session数据存储需要使用sessionDao（如果用tomcat管理session就不用sessionDao，如果要分布式的统一管理session就要用到sessionDao）。

- 缓存管理器（Cache Manager）

  主要对session和授权数据进行缓存（权限管理框架主要就是对认证和授权进行管理，session是在服务器缓存中的），比如将授权数据通过cacheManager进行缓存管理，和ehcache整合对缓存数据进行管理（redis是缓存框架）。

- 域（realm）

- 密码管理（cryptography）

  比如MD5加密，提供了一套加密/解密的足迹，方便开发。比如提供常用的散列、加/解密等功能。比如MD5散列算法（MD5只有加密没有解密）。

## 域（Realm）

Shiro从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确认用户身份是否合法；也需要从Realm得到用户相关的角色/权限进行验证用户是否能进行操作；可以把Realm看成DataSource，即安全数据源。

## 资源（Resource）

在应用中用户科院访问的URL，比如访问JSP页面、查看/编辑某些数据、访问某个业务方法、打印文本等等都是资源。用户只有授权后才能访问。

## 权限（Permission）

安全策略中的原子授权单位，通过权限我们可以表示在应用中用户有没有操作某个资源的权利。即权限表示在应用中用户能不能访问某个资源。如：访问用户列表页面查看/新增/修改/删除用户数据（即很多时候都是CRUD式权限控制）等。权限代表了用户有没有操作某个资源的权利，即反映在某个资源上的操作允不允许。

## Shiro支持的粒度

Shiro支持粗粒度权限（如用户模块的所有权限）和细粒度权限（操作某个用户的权限，即实例级别的）。

## 角色（Role）

权限的集合，一般情况下会赋予用户角色而不是权限，即这样用户可以拥有一组权限，赋予权限时比较方便。典型的如：项目经理、技术总监、CTO、开发工程师等都是角色，不同的角色拥有一组不同的权限。

