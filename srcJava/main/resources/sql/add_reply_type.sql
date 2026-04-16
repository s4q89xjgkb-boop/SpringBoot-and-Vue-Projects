-- 为reply表添加type字段，用于区分评论类型（news、post、article）
ALTER TABLE `reply` ADD COLUMN `type` VARCHAR(20) NOT NULL DEFAULT 'post' COMMENT '评论类型：news（新闻评论）、post（帖子评论）、article（文章评论）' AFTER `time`;