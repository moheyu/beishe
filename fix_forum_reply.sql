-- 修复 forum_reply 表的 parent_id 字段，允许 NULL 并设置默认值为 NULL
ALTER TABLE forum_reply MODIFY COLUMN parent_id BIGINT NULL DEFAULT NULL COMMENT '父回复ID，NULL表示为顶级回复';
