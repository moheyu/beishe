# 项目部署指南

## 1. 服务器环境准备

### 1.1 服务器选择
- **云服务器**：推荐使用阿里云、腾讯云、华为云等
- **配置要求**：
  - CPU：至少2核
  - 内存：至少4GB
  - 存储：至少50GB
  - 操作系统：Ubuntu 20.04 LTS 或 CentOS 7+

### 1.2 安装必要软件

#### 更新系统
```bash
# Ubuntu
sudo apt update && sudo apt upgrade -y

# CentOS
sudo yum update -y
```

#### 安装 JDK 17
```bash
# Ubuntu - 方法 1：使用 PPA
sudo apt install openjdk-17-jdk -y

# Ubuntu - 方法 2：使用 SDKMAN（推荐）
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.14-ms

# CentOS
sudo yum install java-17-openjdk -y

# 验证安装
java -version
# 应该显示：openjdk version "17.x.x"
```

#### 安装 Node.js (v20+)
```bash
# 使用 NodeSource 安装
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs

# 验证安装
node -v
npm -v
```

#### 安装 MySQL 8.0
```bash
# Ubuntu
sudo apt install mysql-server -y
sudo mysql_secure_installation

# CentOS
sudo yum install mysql-server -y
sudo systemctl start mysqld
sudo mysql_secure_installation

# 验证安装
mysql -u root -p
```

#### 安装 Redis
```bash
# Ubuntu
sudo apt install redis-server -y

# CentOS
sudo yum install redis -y
sudo systemctl start redis
sudo systemctl enable redis

# 验证安装
redis-cli ping
# 应该返回：PONG
```

**Redis 配置（可选）**：
```bash
sudo nano /etc/redis/redis.conf
# 修改以下配置：
bind 127.0.0.1  # 只允许本地访问
requirepass your_redis_password  # 设置密码
```

#### 安装 Nginx
```bash
# Ubuntu
sudo apt install nginx -y

# CentOS
sudo yum install nginx -y
sudo systemctl start nginx

# 验证安装
sudo systemctl status nginx
```

## 2. 数据库配置

### 2.1 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS travel_recommendation 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.2 创建用户并授权
```sql
-- 创建用户（限制只能本地访问更安全）
CREATE USER IF NOT EXISTS 'travel'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON travel_recommendation.* TO 'travel'@'localhost';
FLUSH PRIVILEGES;
```

### 2.3 导入数据
1. 先执行数据库结构脚本（如果有）
2. 再执行 `insert_test_data.sql`

## 3. 后端部署

### 3.1 构建后端项目
```bash
cd backend
mvn clean package -DskipTests
```

### 3.2 配置生产环境
- 复制配置文件：
```bash
cp src/main/resources/application-prod.yml src/main/resources/application-prod-local.yml
```

- 修改 `application-prod.yml` 文件：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/travel_recommendation?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: travel
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password  # 如果设置了密码
    database: 0

jwt:
  secret: your-secret-key-here-use-long-string
  expiration: 86400000

file:
  upload:
    path: /opt/travel/uploads
    domain: https://your-domain.com/api
```

### 3.3 部署后端服务
```bash
# 创建部署目录
sudo mkdir -p /opt/travel
sudo mkdir -p /opt/travel/uploads

# 复制 jar 包到服务器
scp target/travel-recommendation-1.0.0.jar user@server_ip:/opt/travel/

# 复制上传目录（如果有）
scp -r uploads/* user@server_ip:/opt/travel/uploads/

# 启动服务
cd /opt/travel/
nohup java -jar travel-recommendation-1.0.0.jar --spring.profiles.active=prod > backend.log 2>&1 &

# 查看启动状态
tail -f backend.log

# 检查端口是否监听
netstat -tlnp | grep 8080
```

### 3.4 设置系统服务
创建专用用户：
```bash
sudo useradd -r -s /bin/false travel-user
sudo chown -R travel-user:travel-user /opt/travel
```

创建 `/etc/systemd/system/travel-backend.service` 文件：

```ini
[Unit]
Description=Travel Recommendation Backend
After=network.target
Wants=mysqld.service redis.service

[Service]
Type=simple
User=travel-user
Group=travel-user
WorkingDirectory=/opt/travel
ExecStart=/usr/bin/java -jar travel-recommendation-1.0.0.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10
LimitNOFILE=65535

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable travel-backend
sudo systemctl start travel-backend
sudo systemctl status travel-backend
```

## 4. 前端部署

### 4.1 构建前端项目
```bash
cd frontend/bishe
npm install
npm run build
```

### 4.2 配置前端 API 地址
- 修改 `src/utils/request.js` 文件中的 `baseURL`：
```javascript
// 开发环境
baseURL: 'http://localhost:8080/api'

// 生产环境（推荐）
baseURL: '/api'  // 通过 Nginx 反向代理

// 或直接使用绝对路径
baseURL: 'https://your-domain.com/api'
```

### 4.3 部署前端静态文件
```bash
# 构建前端
npm run build

# 复制构建产物到服务器
scp -r dist/* user@server_ip:/var/www/travel/
```

**注意**：确保 Nginx 配置了正确的 root 路径和 try_files 指令

## 5. Nginx 配置

### 5.1 基础配置
创建 `/etc/nginx/sites-available/travel` 文件：

```nginx
server {
    listen 80;
    server_name your_domain.com;
    
    # 日志配置
    access_log /var/log/nginx/travel-access.log;
    error_log /var/log/nginx/travel-error.log;
    
    # 前端静态文件
    location / {
        root /var/www/travel;
        index index.html;
        try_files $uri $uri/ /index.html;
        
        # 缓存静态资源
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$ {
            expires 1y;
            add_header Cache-Control "public, immutable";
        }
    }

    # 后端 API 反向代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket 支持（如果需要）
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # 超时设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 上传文件
    location /uploads {
        alias /opt/travel/uploads;
        autoindex off;
        
        # 缓存图片
        location ~* \.(jpg|jpeg|png|gif|webp)$ {
            expires 30d;
            add_header Cache-Control "public";
        }
    }
    
    # 禁止访问隐藏文件
    location ~ /\. {
        deny all;
    }
}
```

### 5.2 启用配置
```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/travel /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重新加载 Nginx
sudo systemctl reload nginx

# 检查状态
sudo systemctl status nginx
```

## 6. SSL 配置（可选）

### 6.1 使用 Let's Encrypt 获取 SSL 证书
```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d your_domain.com
```

### 6.2 自动续期设置
```bash
sudo crontab -e
# 添加以下行
0 12 * * * /usr/bin/certbot renew --quiet
```

## 7. 防火墙配置

```bash
# Ubuntu
sudo ufw allow 80
sudo ufw allow 443
sudo ufw allow 8080

# CentOS
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

## 8. 监控和维护

### 8.1 日志查看
```bash
# 后端日志
tail -f /opt/travel/backend.log
journalctl -u travel-backend -f

# Nginx 日志
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log

# MySQL 日志
tail -f /var/log/mysql/error.log

# Redis 日志
tail -f /var/log/redis/redis-server.log
```

### 8.2 服务状态检查
```bash
sudo systemctl status travel-backend
sudo systemctl status nginx
sudo systemctl status mysql
sudo systemctl status redis

# 检查端口
netstat -tlnp | grep -E '80|443|8080|3306|6379'
```

### 8.3 数据库备份
创建备份脚本 `/opt/travel/backup_db.sh`：
```bash
#!/bin/bash
BACKUP_DIR="/opt/backups/database"
mkdir -p $BACKUP_DIR
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u travel -p'your_password' travel_recommendation > $BACKUP_DIR/travel_$DATE.sql

# 删除 7 天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

```bash
chmod +x /opt/travel/backup_db.sh

# 添加定时任务
crontab -e
# 每天凌晨 2 点备份
0 2 * * * /opt/travel/backup_db.sh
```

### 8.4 健康检查
```bash
# 检查后端 API
curl http://localhost:8080/api/health

# 检查前端
curl http://localhost/

# 检查数据库连接
mysql -u travel -p'your_password' -e "SHOW STATUS LIKE 'Threads_connected';"
```

## 9. 常见问题处理

### 9.1 后端服务无法启动
```bash
# 查看详细日志
journalctl -u travel-backend -n 50 --no-pager

# 检查端口占用
lsof -i :8080

# 检查数据库连接
cat /opt/travel/application-prod.yml

# 检查 Redis 连接
redis-cli ping

# 检查 JVM 内存
free -h
```

### 9.2 前端页面无法访问
```bash
# 检查 Nginx 配置
sudo nginx -t

# 检查前端文件是否存在
ls -la /var/www/travel/index.html

# 检查防火墙
sudo ufw status

# 清除浏览器缓存后重试
```

### 9.3 API 调用失败
```bash
# 检查后端服务
curl http://localhost:8080/api/health

# 检查 Nginx 代理
curl -I http://your-domain.com/api/health

# 查看 Nginx 错误日志
tail -100 /var/log/nginx/error.log

# 检查跨域配置（Nginx CORS）
```

### 9.4 上传文件无法访问
```bash
# 检查文件权限
ls -la /opt/travel/uploads/

# 检查 Nginx 配置中的 alias 路径
cat /etc/nginx/sites-available/travel | grep -A 5 "location /uploads"

# 重启 Nginx
sudo systemctl restart nginx
```

## 10. 性能优化建议

### 10.1 JVM 参数优化
在 systemd 服务中添加：
```ini
ExecStart=/usr/bin/java -Xms512m -Xmx1g -XX:+UseG1GC -jar travel-recommendation-1.0.0.jar --spring.profiles.active=prod
```

### 10.2 数据库优化
```sql
-- 添加索引
ALTER TABLE scenic ADD INDEX idx_category (category_id);
ALTER TABLE user_collection ADD INDEX idx_user (user_id, type);

-- 慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

### 10.3 Redis 缓存
```yaml
# application-prod.yml
spring:
  cache:
    type: redis
  redis:
    time-to-live: 3600000  # 1 小时过期
```

### 10.4 Nginx 优化
```nginx
# 启用 gzip 压缩
gzip on;
gzip_types text/plain text/css application/json application/javascript;
gzip_min_length 1000;

# 客户端请求体大小限制
client_max_body_size 10M;
```

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  服务器环境准备  │────>│   数据库配置    │────>│   后端部署      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   前端部署      │<────│   Nginx 配置    │<────│   SSL 配置      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  防火墙配置     │────>│   监控和维护    │────>│   部署完成      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## 11. 注意事项

1. **安全配置**：
   - 不要在配置文件中硬编码敏感信息
   - 使用环境变量或配置中心管理敏感配置
   - 定期更新密码和证书

2. **性能优化**：
   - 配置合适的 JVM 参数
   - 优化数据库连接池
   - 启用 Nginx 缓存

3. **高可用性**：
   - 考虑使用负载均衡
   - 配置数据库主从复制
   - 定期备份数据

4. **扩展性**：
   - 设计合理的目录结构
   - 考虑使用容器化部署
   - 制定清晰的部署流程

---

通过以上步骤，您可以成功部署个性化旅游推荐平台项目。如果在部署过程中遇到问题，请参考常见问题处理部分，或联系技术支持。