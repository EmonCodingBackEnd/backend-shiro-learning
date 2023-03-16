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
alter table users comment '用户表';