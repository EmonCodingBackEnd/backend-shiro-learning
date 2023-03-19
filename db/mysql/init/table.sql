-- -------------------------------------------------
--   _____       _      _
--  |_   _|__ _ | |__  | |  ___
--    | | / _` || '_ \ | | / _ \
--    | || (_| || |_) || ||  __/
--    |_| \__,_||_.__/ |_| \___|
--
-- 表
-- -------------------------------------------------

drop table if exists users;

/*==============================================================*/
/* Table: users                                                */
/*==============================================================*/
create table users
(
    id     bigint(20) not null auto_increment comment '编号',
    `name` varchar(30) default null comment '用户名',
    pwd    varchar(50) default null comment '密码',
    rid    bigint(20)  default null comment '角色编号',
    primary key (id)
);
alter table users
    comment '用户表';

drop table if exists roles;

/*==============================================================*/
/* Table: roles                                                */
/*==============================================================*/
create table roles
(
    id       bigint(20) not null auto_increment comment '编号',
    `name`   varchar(30) default null comment '角色名',
    `desc`   varchar(50) default null comment '描述',
    realname varchar(20) default null comment '角色显示名',
    primary key (id)
);
alter table roles
    comment '角色表';

drop table if exists role_user;

/*==============================================================*/
/* Table: role_user                                                */
/*==============================================================*/
create table role_user
(
    id    bigint(20) not null auto_increment comment '编号',
    `uid` bigint(20) default null comment '用户id',
    `rid` bigint(20) default null comment '角色id',
    primary key (id)
);
alter table role_user
    comment '角色用户映射表';

drop table if exists permissions;

/*==============================================================*/
/* Table: permissions                                                */
/*==============================================================*/
create table permissions
(
    id     bigint(20) not null auto_increment comment '编号',
    `name` varchar(30) default null comment '权限名',
    `info` varchar(30) default null comment '权限信息',
    `desc` varchar(50) default null comment '描述',
    primary key (id)
);
alter table permissions
    comment '权限表';

drop table if exists role_ps;

/*==============================================================*/
/* Table: role_ps                                                */
/*==============================================================*/
create table role_ps
(
    id    bigint(20) not null auto_increment comment '编号',
    `rid` bigint(20) default null comment '角色id',
    `pid` bigint(20) default null comment '权限id',
    primary key (id)
);
alter table role_ps
    comment '角色权限映射表';

-- ==================================================华丽的分割线==================================================
drop table if exists sh_user;
create table sh_user
(
    id          varchar(36) not null comment '主键',
    login_name  varchar(36)  default null comment '登录名称',
    real_name   varchar(36)  default null comment '真实姓名',
    nick_name   varchar(36)  default null comment '昵称',
    pass_word   varchar(150) default null comment '密码',
    salt        varchar(36)  default null comment '加密因子',
    sex         int(11)      default null comment '性别',
    zipcode     varchar(36)  default null comment '邮箱',
    address     varchar(36)  default null comment '地址',
    tel         varchar(36)  default null comment '固定电话',
    mobile      varchar(36)  default null comment '电话',
    email       varchar(36)  default null comment '邮箱',
    duties      varchar(36)  default null comment '职务',
    sort_no     int(11)      default null comment '排序',
    enable_flag varchar(18)  default null comment '是否有效',
    primary key (id)
) comment = '用户表';

drop table if exists sh_role;
create table sh_role
(
    id            varchar(36) not null comment '主键',
    role_name     varchar(36)  default null comment '角色名称',
    label         varchar(36)  default null comment '角色标识',
    `description` varchar(200) default null comment '角色描述',
    sort_no       int(11)      default null comment '排序',
    enable_flag   varchar(18)  default null comment '是否有效',
    primary key (id)
) comment '角色表';

drop table if exists sh_resource;
create table sh_resource
(
    id             varchar(36) not null comment '主键',
    parent_id      varchar(36)  default null comment '父资源',
    resource_name  varchar(36)  default null comment '资源名称',
    request_path   varchar(200) default null comment '资源路径',
    label          varchar(200) default null comment '资源标签',
    icon           varchar(20)  default null comment '图标',
    is_leaf        varchar(18)  default null comment '是否叶子节点',
    resource_type  varchar(36)  default null comment '资源类型',
    `description`  varchar(200) default null comment '角色描述',
    system_code    varchar(36)  default null comment '系统code',
    is_system_root varchar(18)  default null comment '是否根节点',
    sort_no        int(11)      default null comment '排序',
    enable_flag    varchar(18)  default null comment '是否有效',
    primary key (id)
) comment '资源表';

drop table if exists sh_user_role;
create table sh_user_role
(
    id          varchar(36) not null comment '主键',
    enable_flag varchar(18) default null comment '是否有效',
    user_id     varchar(36) default null comment '用户ID',
    role_id     varchar(36) default null comment '角色ID',
    primary key (id)
) comment '用户角色表';

drop table if exists sh_role_resource;
create table sh_role_resource
(
    id          varchar(36) not null comment '主键',
    enable_flag varchar(18) default null comment '是否有效',
    role_id     varchar(36) default null comment '角色ID',
    resource_id varchar(36) default null comment '资源ID',
    primary key (id)
) comment '角色资源表';