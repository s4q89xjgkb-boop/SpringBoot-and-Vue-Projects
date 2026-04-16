-- 为点赞表添加type字段
ALTER TABLE `like` ADD COLUMN `type` VARCHAR(20) NOT NULL DEFAULT 'article' COMMENT '类型：article（文章）、post（帖子）' AFTER `introduction_id`;

-- 为收藏表添加type字段
ALTER TABLE `collect` ADD COLUMN `type` VARCHAR(20) NOT NULL DEFAULT 'article' COMMENT '类型：article（文章）、post（帖子）' AFTER `introduction_id`;

-- 为点赞表添加type字段的索引
ALTER TABLE `like` ADD INDEX `idx_type` (`type`);

-- 为收藏表添加type字段的索引
ALTER TABLE `collect` ADD INDEX `idx_type` (`type`);
