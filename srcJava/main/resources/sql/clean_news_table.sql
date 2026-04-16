-- 清理 news 表中的重复字段和冗余字段

-- 删除重复的 userld 字段
ALTER TABLE `news` DROP COLUMN `userld`;

-- 删除冗余的用户信息字段
ALTER TABLE `news` DROP COLUMN `userName`;
ALTER TABLE `news` DROP COLUMN `userAvatar`;