-- 论坛板块表
CREATE TABLE IF NOT EXISTS `forum_section` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '板块ID',
    `title` VARCHAR(50) NOT NULL COMMENT '板块名称',
    `description` VARCHAR(200) COMMENT '板块描述',
    `icon` VARCHAR(255) COMMENT '板块图标',
    `post_count` INT DEFAULT 0 COMMENT '帖子数量',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    `time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛板块表';

-- 插入通用板块数据（游戏相关的板块会根据游戏分类自动创建）
INSERT INTO `forum_section` (`title`, `description`, `icon`, `sort`, `status`) VALUES
('闲聊板块', '日常闲聊交流', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 1, 1),
('吐槽板块', '游戏吐槽专区', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', 2, 1),
('其他游戏', '其他游戏讨论', 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png', 3, 1);

-- 帖子表
CREATE TABLE IF NOT EXISTS `post` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    `title` VARCHAR(200) NOT NULL COMMENT '帖子标题',
    `content` TEXT NOT NULL COMMENT '帖子内容',
    `section_id` INT NOT NULL COMMENT '板块ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `status` INT DEFAULT 0 COMMENT '审核状态 0-待审核 1-已通过 2-已拒绝',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏数',
    `reply_count` INT DEFAULT 0 COMMENT '评论数',
    `views` INT DEFAULT 0 COMMENT '浏览量',
    `time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    FOREIGN KEY (`section_id`) REFERENCES `forum_section`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';
