-- =====================================================
-- 个性化旅游推荐平台 - 完整数据库脚本
-- 数据库：travel_recommend
-- 字符集：utf8mb4
-- 生成时间：2026-03-25
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS travel_recommend 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_recommend;

-- =====================================================
-- 1. 用户表
-- =====================================================
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt 加密）',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像 URL',
  `role` tinyint NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 景点分类表
-- =====================================================
CREATE TABLE `scenic_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称：自然风光/人文历史/主题乐园',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '分类描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点分类表';

-- =====================================================
-- 3. 景点标签表
-- =====================================================
CREATE TABLE `scenic_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称：如适合亲子、网红打卡',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点标签表';

-- =====================================================
-- 4. 景点表
-- =====================================================
CREATE TABLE `scenic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '景点 ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '景点名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '官方介绍',
  `images` longtext COLLATE utf8mb4_unicode_ci COMMENT '图片 URL（JSON 数组）',
  `price` decimal(10,2) DEFAULT 0.00 COMMENT '价格',
  `score` decimal(3,2) NOT NULL DEFAULT 0.00 COMMENT '评分（0-5 分）',
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地点',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `category_id` bigint NOT NULL COMMENT '关联分类 ID',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `recommend_level` tinyint DEFAULT 0 COMMENT '推荐等级：0-普通，1-推荐，2-热门，3-必游',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_location` (`location`),
  KEY `idx_score` (`score`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点表';

-- =====================================================
-- 5. 景点 - 标签关联表
-- =====================================================
CREATE TABLE `scenic_tag_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联 ID',
  `scenic_id` bigint NOT NULL COMMENT '景点 ID',
  `tag_id` bigint NOT NULL COMMENT '标签 ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scenic_tag` (`scenic_id`,`tag_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点 - 标签关联表';

-- =====================================================
-- 6. 路线分类表
-- =====================================================
CREATE TABLE `route_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类 ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='路线分类表';

-- =====================================================
-- 7. 旅游路线表
-- =====================================================
CREATE TABLE `travel_route` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '路线 ID',
  `name` varchar(100) NOT NULL COMMENT '路线名称',
  `description` text COMMENT '路线描述',
  `category_id` bigint DEFAULT NULL COMMENT '路线分类 ID',
  `budget` decimal(10,2) DEFAULT 0.00 COMMENT '预算估算（元）',
  `duration_days` int DEFAULT '1' COMMENT '行程天数',
  `best_season` varchar(50) DEFAULT NULL COMMENT '最佳季节，如：春季，夏季，秋季，冬季',
  `images` json DEFAULT NULL COMMENT '路线图片 URL 数组',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='旅游路线表';

-- =====================================================
-- 8. 路线 - 景点关联表
-- =====================================================
CREATE TABLE `route_scenic_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联 ID',
  `route_id` bigint NOT NULL COMMENT '路线 ID',
  `scenic_id` bigint NOT NULL COMMENT '景点 ID',
  `day_number` int NOT NULL COMMENT '第几天（1-based）',
  `sort_order` int DEFAULT 0 COMMENT '当天内排序',
  `description` text COMMENT '景点说明/推荐理由',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_route_scenic_day` (`route_id`,`scenic_id`,`day_number`),
  KEY `idx_route_id` (`route_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  CONSTRAINT `fk_route_scenic_route` FOREIGN KEY (`route_id`) REFERENCES `travel_route` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_route_scenic_scenic` FOREIGN KEY (`scenic_id`) REFERENCES `scenic` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='路线 - 景点关联表';

-- =====================================================
-- 9. 公告表
-- =====================================================
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告 ID',
  `title` varchar(100) NOT NULL COMMENT '公告标题',
  `content` text NOT NULL COMMENT '公告内容',
  `type` tinyint DEFAULT '1' COMMENT '公告类型：1-系统公告，2-活动通知，3-维护通知',
  `is_top` tinyint DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表';

-- =====================================================
-- 10. 论坛帖子表
-- =====================================================
CREATE TABLE `forum_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子 ID',
  `title` varchar(100) NOT NULL COMMENT '帖子标题',
  `content` text NOT NULL COMMENT '帖子内容',
  `user_id` bigint NOT NULL COMMENT '发帖用户 ID',
  `scenic_id` bigint DEFAULT NULL COMMENT '关联景点 ID（可选）',
  `route_id` bigint DEFAULT NULL COMMENT '关联路线 ID（可选）',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `reply_count` int DEFAULT 0 COMMENT '回复数',
  `is_top` tinyint DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
  `is_essence` tinyint DEFAULT 0 COMMENT '是否精华：0-否，1-是',
  `is_audit` tinyint DEFAULT '1' COMMENT '审核状态：0-未审核，1-已通过，2-已拒绝',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_route_id` (`route_id`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_is_essence` (`is_essence`),
  KEY `idx_is_audit` (`is_audit`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_forum_post_route` FOREIGN KEY (`route_id`) REFERENCES `travel_route` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_forum_post_scenic` FOREIGN KEY (`scenic_id`) REFERENCES `scenic` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_forum_post_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='论坛帖子表';

-- =====================================================
-- 11. 论坛回复表
-- =====================================================
CREATE TABLE `forum_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复 ID',
  `post_id` bigint NOT NULL COMMENT '帖子 ID',
  `user_id` bigint NOT NULL COMMENT '回复用户 ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父回复 ID，NULL 表示为顶级回复',
  `content` text NOT NULL COMMENT '回复内容',
  `is_audit` tinyint DEFAULT '1' COMMENT '审核状态：0-未审核，1-已通过，2-已拒绝',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_is_audit` (`is_audit`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_forum_reply_parent` FOREIGN KEY (`parent_id`) REFERENCES `forum_reply` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_forum_reply_post` FOREIGN KEY (`post_id`) REFERENCES `forum_post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_forum_reply_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='论坛回复表';

-- =====================================================
-- 12. 用户收藏表
-- =====================================================
CREATE TABLE `user_collection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注（管理端可填）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除）',
  `type` int DEFAULT '1' COMMENT '收藏类型：1-景点，2-路线',
  `target_id` bigint DEFAULT NULL COMMENT '目标 ID（景点 ID 或路线 ID）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_type_target` (`user_id`,`type`,`target_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_id` (`target_id`),
  CONSTRAINT `fk_user_collection_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- =====================================================
-- 13. 用户评论表
-- =====================================================
CREATE TABLE `user_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `is_audit` tinyint NOT NULL DEFAULT '1' COMMENT '是否审核：0-未审核，1-已通过（测试版默认 1）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除 0-未删除，1-已删除）',
  `type` int DEFAULT '1' COMMENT '评论类型：1-景点，2-路线',
  `target_id` bigint DEFAULT NULL COMMENT '目标 ID（景点 ID 或路线 ID）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user_target` (`user_id`,`type`,`target_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_audit` (`is_audit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户评论表';

-- =====================================================
-- 14. 用户偏好表
-- =====================================================
CREATE TABLE `user_preference` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '偏好 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `category_id` bigint DEFAULT NULL COMMENT '偏好分类 ID（可空）',
  `tag_id` bigint DEFAULT NULL COMMENT '偏好标签 ID（可空）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_category_tag` (`user_id`,`category_id`,`tag_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好表';

-- =====================================================
-- 15. 浏览记录表
-- =====================================================
CREATE TABLE `view_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '浏览记录 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `scenic_id` bigint NOT NULL COMMENT '景点 ID',
  `view_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  `deleted` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_view_time` (`view_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览记录表';

-- =====================================================
-- 初始数据
-- =====================================================

-- 插入景点分类
INSERT INTO scenic_category (id, name, description, sort) VALUES
(1, '自然风光', '美丽的自然景观，如山川、湖泊、森林等', 1),
(2, '人文历史', '具有历史文化价值的古迹和博物馆', 2),
(3, '主题乐园', '游乐场、主题公园等娱乐场所', 3),
(4, '城市地标', '城市标志性建筑和景点', 4),
(5, '乡村田园', '农家乐、田园风光、乡村旅游', 5);

-- 插入路线分类
INSERT INTO route_category (id, name, description, sort) VALUES
(1, '文化之旅', '探索历史文化景点', 1),
(2, '自然之旅', '欣赏自然风光', 2),
(3, '美食之旅', '品尝当地美食', 3);

-- 插入测试用户（密码：123456，BCrypt 加密后）
INSERT INTO user (id, username, password, nickname, role, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WnUgVZqPvKzGmXhR5FjLcHwYdD2C', '管理员', 1, 1),
(2, 'user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WnUgVZqPvKzGmXhR5FjLcHwYdD2C', '游客 1', 0, 1);

-- 插入测试景点
INSERT INTO scenic (id, name, description, images, price, score, location, view_count, category_id, status, recommend_level) VALUES
(1, '故宫', '明清两代的皇家宫殿，又称紫禁城', '["/uploads/scenic/forbidden-city.jpg"]', 60.00, 4.80, '北京市东城区', 1000, 2, 1, 3),
(2, '天坛', '明清两代皇帝祭天和祈谷的场所', '["/uploads/scenic/temple-of-heaven.jpg"]', 15.00, 4.60, '北京市东城区', 800, 2, 1, 2),
(3, '颐和园', '清代皇家园林，被誉为"皇家园林博物馆"', '["/uploads/scenic/summer-palace.jpg"]', 30.00, 4.70, '北京市海淀区', 900, 1, 1, 2),
(4, '长城', '世界文化遗产，古代军事防御工程', '["/uploads/scenic/great-wall.jpg"]', 40.00, 4.90, '北京市延庆区', 1200, 2, 1, 3),
(5, '天安门广场', '世界上最大的城市中心广场', '["/uploads/scenic/tiananmen.jpg"]', 0.00, 4.50, '北京市东城区', 1500, 4, 1, 1);

-- 插入测试路线
INSERT INTO travel_route (id, name, description, category_id, budget, duration_days, best_season, images, status, view_count) VALUES
(1, '北京经典三日游', '游览故宫、天坛、颐和园等经典景点', 1, 1500.00, 3, '春秋', '["/uploads/route/beijing-classic.jpg"]', 1, 500),
(2, '长城一日游', '攀登八达岭长城，感受古代军事防御工程的雄伟', 1, 300.00, 1, '全年', '["/uploads/route/great-wall-tour.jpg"]', 1, 300);

-- 插入路线 - 景点关联
INSERT INTO route_scenic_relation (id, route_id, scenic_id, day_number, sort_order, description) VALUES
(1, 1, 1, 1, 1, '上午游览故宫，感受皇家宫殿的宏伟'),
(2, 1, 2, 1, 2, '下午游览天坛，了解古代祭祀文化'),
(3, 1, 3, 2, 1, '全天游览颐和园，欣赏皇家园林美景'),
(4, 2, 4, 1, 1, '全天游览八达岭长城');

-- 插入测试公告
INSERT INTO announcement (id, title, content, type, is_top, status) VALUES
(1, '系统维护通知', '本站将于 2026 年 3 月 20 日凌晨 2:00-6:00 进行系统维护，请提前保存数据。', 3, 0, 1),
(2, '新功能上线通知', '个性化推荐功能已上线，欢迎体验！', 1, 1, 1);

-- 插入测试帖子
INSERT INTO forum_post (id, title, content, user_id, scenic_id, route_id, view_count, reply_count, is_audit) VALUES
(1, '北京三日游攻略分享', '这次北京三日游玩得很开心，景点都很棒，推荐给大家！', 1, 1, 1, 100, 2, 1),
(2, '长城一日游心得', '长城真的很壮观，建议大家早点出发，避开人流高峰。', 1, 4, 2, 80, 1, 1);

-- 插入测试回复
INSERT INTO forum_reply (id, post_id, user_id, parent_id, content, is_audit) VALUES
(1, 1, 1, NULL, '感谢分享，很有用的攻略！', 1),
(2, 2, 1, NULL, '确实，早去是个好建议。', 1);

-- 插入测试收藏
INSERT INTO user_collection (id, user_id, target_id, type, remark) VALUES
(1, 1, 1, 1, '很美的地方'),
(2, 1, 2, 1, '不错的寺庙'),
(3, 1, 1, 2, '推荐的路线');

-- 插入测试评论
INSERT INTO user_comment (id, user_id, target_id, type, content) VALUES
(1, 1, 1, 1, '故宫真的很震撼，值得一看！'),
(2, 1, 2, 1, '天坛建筑精美，推荐参观'),
(3, 1, 1, 2, '这条路线设计得很好，推荐大家试试！');

-- 插入测试偏好
INSERT INTO user_preference (id, user_id, category_id) VALUES
(1, 1, 2),
(2, 1, 1);

-- 插入测试浏览记录
INSERT INTO view_record (id, user_id, scenic_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4);
