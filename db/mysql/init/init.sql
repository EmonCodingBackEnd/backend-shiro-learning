-- --------------------------------------------------------------------------------
--   ___         _  _
--  |_ _| _ __  (_)| |_
--   | | | '_ \ | || __|
--   | | | | | || || |_
--  |___||_| |_||_| \__|
--
-- 初始化数据脚本
-- 要求：
--    1、保证sql可以重复执行，也就是insert之前先delete（删除表还是某一条，根据需要而定）
--    2、sql关键字推荐大写
-- --------------------------------------------------------------------------------

-- 数据文件
INSERT INTO shirodb.users
(id, name, pwd, rid)
VALUES(1, 'emon', 'c1eab7f9d657c8ec2863a3f8168e3fcb', NULL);
INSERT INTO shirodb.users
(id, name, pwd, rid)
VALUES(2, 'lisi', '140213090f7dbf01de5e6abb4b97d877', NULL);

INSERT INTO shirodb.roles
(id, name, `desc`, realname)
VALUES(1, 'admin', '所有权限', '管理员');
INSERT INTO shirodb.roles
(id, name, `desc`, realname)
VALUES(2, 'userMgr', '用户管理权限', '用户管理');

INSERT INTO shirodb.role_user
(id, uid, rid)
VALUES(1, 1, 1);
INSERT INTO shirodb.role_user
(id, uid, rid)
VALUES(2, 1, 2);
INSERT INTO shirodb.role_user
(id, uid, rid)
VALUES(3, 2, 2);

INSERT INTO shirodb.permissions
(id, name, info, `desc`)
VALUES(1, '删除用户', 'user:delete', '删除用户');
INSERT INTO shirodb.permissions
(id, name, info, `desc`)
VALUES(2, '新增用户', 'user:add', '新增用户');
INSERT INTO shirodb.permissions
(id, name, info, `desc`)
VALUES(3, '修改用户', 'user:edit', '修改用户');

INSERT INTO shirodb.role_ps
(id, rid, pid)
VALUES(1, 1, 1);
INSERT INTO shirodb.role_ps
(id, rid, pid)
VALUES(2, 1, 2);
INSERT INTO shirodb.role_ps
(id, rid, pid)
VALUES(3, 1, 3);




