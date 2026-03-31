# Docker 部署指南

## 1. 环境准备

### 1.1 安装 Docker

#### Ubuntu
```bash
sudo apt update
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
```

#### CentOS
```bash
sudo yum install docker -y
sudo systemctl start docker
sudo systemctl enable docker
```

#### Windows / macOS
- 下载并安装 [Docker Desktop](https://www.docker.com/products/docker-desktop)

### 1.2 安装 Docker Compose

#### Ubuntu
```bash
sudo apt install docker-compose -y
```

#### CentOS
```bash
sudo yum install docker-compose -y
```

#### Windows / macOS
Docker Desktop 已包含 Docker Compose

### 1.3 验证安装
```bash
docker --version
docker-compose --version
```

## 2. 构建和部署

### 2.1 构建后端项目
```bash
cd backend
mvn clean package -DskipTests
```

### 2.2 启动服务
```bash
# 在项目根目录执行
cd ..
docker-compose up -d
```

### 2.3 查看服务状态
```bash
docker-compose ps
```

### 2.4 查看日志
```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql
```

## 3. 服务访问

- **前端**：http://localhost
- **后端 API**：http://localhost:8080/api
- **数据库**：localhost:3306 (用户名: travel, 密码: travel123)
- **Redis**：localhost:6379

## 4. 配置说明

### 4.1 环境变量

在 `docker-compose.yml` 文件中，您可以修改以下环境变量：

- **MySQL**：
  - `MYSQL_ROOT_PASSWORD`：MySQL 根密码
  - `MYSQL_DATABASE`：数据库名称
  - `MYSQL_USER`：数据库用户
  - `MYSQL_PASSWORD`：数据库密码

- **后端**：
  - `SPRING_DATASOURCE_URL`：数据库连接 URL
  - `SPRING_DATASOURCE_USERNAME`：数据库用户名
  - `SPRING_DATASOURCE_PASSWORD`：数据库密码
  - `SPRING_REDIS_HOST`：Redis 主机
  - `SPRING_REDIS_PORT`：Redis 端口

### 4.2 数据持久化

- **MySQL 数据**：存储在 `mysql-data` 卷中
- **Redis 数据**：存储在 `redis-data` 卷中
- **上传文件**：映射到主机的 `./uploads` 目录

### 4.3 网络配置

服务之间通过 `travel-network` 网络通信，容器名称作为主机名使用：
- 后端连接数据库：`mysql:3306`
- 后端连接 Redis：`redis:6379`
- 前端连接后端：`backend:8080`

## 5. 常见操作

### 5.1 停止服务
```bash
docker-compose down
```

### 5.2 重启服务
```bash
docker-compose restart
```

### 5.3 重建服务
```bash
docker-compose up -d --build
```

### 5.4 进入容器
```bash
# 进入后端容器
docker exec -it travel-backend bash

# 进入前端容器
docker exec -it travel-frontend bash

# 进入 MySQL 容器
docker exec -it travel-mysql bash
```

### 5.5 查看容器资源使用情况
```bash
docker stats
```

## 6. 问题排查

### 6.1 服务启动失败
```bash
# 查看详细日志
docker-compose logs --tail=100 backend
```

### 6.2 数据库连接问题
- 检查 MySQL 容器是否正常运行
- 检查环境变量配置是否正确
- 检查网络连接是否正常

### 6.3 前端无法访问后端 API
- 检查后端服务是否正常运行
- 检查 Nginx 配置是否正确
- 检查网络连接是否正常

### 6.4 上传文件问题
- 检查上传目录权限
- 检查文件大小限制
- 检查后端配置是否正确

## 7. 性能优化

### 7.1 资源限制
在 `docker-compose.yml` 中添加资源限制：

```yaml
services:
  backend:
    # ...
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: '4G'
```

### 7.2 缓存优化
- 启用 Docker 层缓存
- 使用多阶段构建减小镜像体积
- 配置 Nginx 缓存静态资源

### 7.3 网络优化
- 使用桥接网络提高容器间通信速度
- 配置合适的网络 MTU

## 8. 生产环境部署建议

### 8.1 安全配置
- 使用私有镜像仓库
- 配置 HTTPS
- 限制容器网络访问
- 使用 secrets 管理敏感信息

### 8.2 监控和日志
- 集成 ELK 或 Prometheus 监控
- 配置集中式日志管理
- 设置告警机制

### 8.3 高可用性
- 使用 Docker Swarm 或 Kubernetes
- 配置服务副本
- 实现负载均衡

### 8.4 备份策略
- 定期备份数据库
- 备份上传文件
- 备份配置文件

## 9. 升级和更新

### 9.1 更新后端代码
```bash
# 构建新的后端代码
cd backend
mvn clean package -DskipTests

# 重启服务
docker-compose up -d --build backend
```

### 9.2 更新前端代码
```bash
# 构建新的前端代码
cd frontend/bishe
npm install
npm run build

# 重启服务
docker-compose up -d --build frontend
```

### 9.3 更新依赖
- 更新 Docker 镜像版本
- 更新项目依赖
- 测试兼容性

## 10. 部署流程图

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  环境准备       │────>│   构建后端项目   │────>│   启动服务      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   服务访问      │<────│   查看服务状态   │<────│   查看日志      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   问题排查      │────>│   性能优化      │────>│   生产环境部署   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

通过以上步骤，您可以使用 Docker 快速部署和管理个性化旅游推荐平台项目。Docker 部署方式简化了环境配置和依赖管理，提高了部署的一致性和可重复性。