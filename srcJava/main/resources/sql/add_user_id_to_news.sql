-- 为 news 表添加 user_id 字段
ALTER TABLE `news` ADD COLUMN `user_id` INT NOT NULL DEFAULT 1 COMMENT '发布人ID' AFTER `img`;