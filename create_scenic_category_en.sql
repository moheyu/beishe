CREATE TABLE IF NOT EXISTS scenic_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    sort INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO scenic_category (id, name, description, sort) VALUES
(1, 'Natural Scenery', 'Beautiful natural landscapes', 1),
(2, 'Historical Sites', 'Historical and cultural attractions', 2),
(3, 'Theme Parks', 'Amusement parks and entertainment venues', 3),
(4, 'City Landmarks', 'Iconic city buildings and attractions', 4),
(5, 'Rural Tourism', 'Farm stays and rural scenery', 5);
