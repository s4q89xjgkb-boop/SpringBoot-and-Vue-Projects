-- 为 user 表添加 gender 字段
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(10) COMMENT '性别 male-男 female-女 secret-保密' AFTER `email`;

-- 为 admin 表添加 gender 字段
ALTER TABLE `admin` ADD COLUMN `gender` VARCHAR(10) COMMENT '性别 male-男 female-女 secret-保密' AFTER `email`;