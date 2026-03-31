<template>
  <el-footer class="app-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section">
          <h4>关于我们</h4>
          <p>旅游推荐平台致力于为用户提供个性化的旅游推荐服务，帮助您发现最美的风景。</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/scenic/list">热门景点</router-link></li>
            <li><router-link to="/route/list">精选路线</router-link></li>
            <li><router-link to="/forum">旅游论坛</router-link></li>
            <li><router-link to="/announcements">最新公告</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系方式</h4>
          <p>邮箱：2973908609@qq.com</p>
          <p>电话：18907136495</p>

        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 旅游推荐平台 版权所有</p>
      </div>
    </div>
  </el-footer>
</template>

<style scoped>
.app-footer {
  background: #2c3e50;
  color: #fff;
  padding: 40px 0 20px;
  height: auto;
}

.footer-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h4 {
  font-size: 16px;
  margin-bottom: 16px;
  color: #fff;
}

.footer-section p {
  color: #bdc3c7;
  line-height: 1.8;
  font-size: 14px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin-bottom: 10px;
}

.footer-section a {
  color: #bdc3c7;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #409eff;
}

.footer-bottom {
  border-top: 1px solid #34495e;
  padding-top: 20px;
  text-align: center;
}

.footer-bottom p {
  color: #95a5a6;
  font-size: 14px;
  margin: 0;
}
</style>
