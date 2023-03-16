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