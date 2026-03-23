<template>
  <el-header class="app-header">
    <div class="header-container">
      <div class="logo">
        <router-link to="/">
          <el-icon size="24"><Compass /></el-icon>
          <span class="logo-text">旅游推荐平台</span>
        </router-link>
      </div>
      
      <!-- 桌面端导航 -->
      <nav class="nav-menu desktop-nav">
        <router-link to="/" :class="{ active: $route.path === '/' }">首页</router-link>
        <router-link to="/scenic/list" :class="{ active: $route.path.startsWith('/scenic') }">景点</router-link>
        <router-link to="/route/list" :class="{ active: $route.path.startsWith('/route') }">路线</router-link>
        <router-link to="/forum" :class="{ active: $route.path.startsWith('/forum') }">论坛</router-link>
        <router-link to="/announcements" :class="{ active: $route.path.startsWith('/announcement') }">公告</router-link>
      </nav>
      
      <!-- 移动端菜单按钮 -->
      <el-button class="mobile-menu-btn" text @click="mobileMenuVisible = true">
        <el-icon size="24"><Menu /></el-icon>
      </el-button>
      
      <div class="user-actions">
        <template v-if="userStore.isLoggedIn">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="avatarUrl" />
              <span class="username desktop-only">{{ userStore.nickname || userStore.username }}</span>
              <el-icon class="desktop-only"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item command="preferences">偏好设置</el-dropdown-item>
                <el-dropdown-item command="collections">我的收藏</el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" command="admin">管理后台</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <router-link to="/login">
            <el-button type="primary" size="small">登录</el-button>
          </router-link>
          <router-link to="/register" class="desktop-only">
            <el-button size="small">注册</el-button>
          </router-link>
        </template>
      </div>
    </div>
    
    <!-- 移动端抽屉菜单 -->
    <el-drawer v-model="mobileMenuVisible" title="菜单" direction="ltr" size="250px" class="mobile-drawer">
      <nav class="mobile-nav">
        <router-link to="/" @click="mobileMenuVisible = false" :class="{ active: $route.path === '/' }">
          <el-icon><HomeFilled /></el-icon> 首页
        </router-link>
        <router-link to="/scenic/list" @click="mobileMenuVisible = false" :class="{ active: $route.path.startsWith('/scenic') }">
          <el-icon><Picture /></el-icon> 景点
        </router-link>
        <router-link to="/route/list" @click="mobileMenuVisible = false" :class="{ active: $route.path.startsWith('/route') }">
          <el-icon><MapLocation /></el-icon> 路线
        </router-link>
        <router-link to="/forum" @click="mobileMenuVisible = false" :class="{ active: $route.path.startsWith('/forum') }">
          <el-icon><ChatDotRound /></el-icon> 论坛
        </router-link>
        <router-link to="/announcements" @click="mobileMenuVisible = false" :class="{ active: $route.path.startsWith('/announcement') }">
          <el-icon><Bell /></el-icon> 公告
        </router-link>
      </nav>
    </el-drawer>
  </el-header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Compass, ArrowDown, Menu, HomeFilled, Picture, MapLocation, ChatDotRound, Bell } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const userStore = useUserStore()
const mobileMenuVisible = ref(false)

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 处理后的头像 URL
const avatarUrl = computed(() => {
  if (!userStore.avatar) return defaultAvatar
  return getImageUrl(userStore.avatar)
})

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'preferences':
      router.push('/user/preferences')
      break
    case 'collections':
      router.push('/user/collections')
      break
    case 'admin':
      router.push('/admin')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/')
      })
      break
  }
}
</script>

<style scoped>
.app-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 60px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo a {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  text-decoration: none;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-menu a {
  color: #333;
  text-decoration: none;
  font-size: 15px;
  padding: 8px 0;
  position: relative;
  transition: color 0.3s;
}

.nav-menu a:hover,
.nav-menu a.active {
  color: #409eff;
}

.nav-menu a.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: #409eff;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 响应式设计 */
.mobile-menu-btn {
  display: none;
}

.mobile-drawer {
  display: none;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.mobile-nav a {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  color: #333;
  text-decoration: none;
  border-radius: 8px;
  transition: background 0.3s;
}

.mobile-nav a:hover,
.mobile-nav a.active {
  background: #ecf5ff;
  color: #409eff;
}

@media (max-width: 768px) {
  .desktop-nav {
    display: none;
  }
  
  .mobile-menu-btn {
    display: flex;
    margin-right: auto;
    margin-left: 16px;
  }
  
  .mobile-drawer {
    display: block;
  }
  
  .desktop-only {
    display: none !important;
  }
  
  .logo-text {
    display: none;
  }
  
  .header-container {
    padding: 0 12px;
  }
  
  .user-actions {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .app-header {
    height: 50px;
  }
  
  .main-content.with-header {
    padding-top: 50px;
  }
}
</style>
