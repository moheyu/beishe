-- 创建景点分类表
CREATE TABLE IF NOT EXISTS scenic_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点分类表';

-- 插入测试数据
INSERT INTO scenic_category (id, name, description, sort) VALUES
(1, '自然风光', '美丽的自然景观，如山川、湖泊、森林等', 1),
(2, '人文历史', '具有历史文化价值的古迹和博物馆', 2),
(3, '主题乐园', '游乐场、主题公园等娱乐场所', 3),
(4, '城市地标', '城市标志性建筑和景点', 4),
(5, '乡村田园', '农家乐、田园风光、乡村旅游', 5)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), sort=VALUES(sort);
