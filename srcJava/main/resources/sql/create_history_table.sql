-- 创建观看历史表
CREATE TABLE IF NOT EXISTS history (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    content_id INT NOT NULL COMMENT '内容ID（文章或帖子ID）',
    type VARCHAR(20) NOT NULL COMMENT '内容类型（article:文章, post:帖子）',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    img VARCHAR(255) COMMENT '图片URL',
    time DATETIME NOT NULL COMMENT '观看时间',
    INDEX idx_user_id (user_id),
    INDEX idx_time (time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='观看历史表';
