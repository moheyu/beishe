<template>
  <div class="user-layout">
    <div class="user-container">
      <el-aside class="user-sidebar" width="220px">
        <div class="user-info-card">
          <el-avatar :size="64" :src="avatarUrl" />
          <h3>{{ userStore.nickname || userStore.username }}</h3>
          <p>{{ userStore.userInfo?.email || '' }}</p>
        </div>
        <el-menu
          :default-active="$route.path"
          router
          class="user-menu"
        >
          <el-menu-item index="/user/profile">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="/user/preferences">
            <el-icon><Setting /></el-icon>
            <span>偏好设置</span>
          </el-menu-item>
          <el-menu-item index="/user/collections">
            <el-icon><Star /></el-icon>
            <span>我的收藏</span>
          </el-menu-item>
          <el-menu-item index="/user/comments">
            <el-icon><ChatDotRound /></el-icon>
            <span>我的评论</span>
          </el-menu-item>
          <el-menu-item index="/user/history">
            <el-icon><Clock /></el-icon>
            <span>浏览历史</span>
          </el-menu-item>
          <el-menu-item index="/user/posts">
            <el-icon><Document /></el-icon>
            <span>我的帖子</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-main class="user-main">
        <router-view />
      </el-main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { User, Setting, Star, ChatDotRound, Clock, Document } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getImageUrl } from '@/utils/image'

const userStore = useUserStore()
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 处理后的头像 URL
const avatarUrl = computed(() => {
  if (!userStore.avatar) return defaultAvatar
  return getImageUrl(userStore.avatar)
})
</script>

<style scoped>
.user-layout {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.user-container {
  display: flex;
  gap: 20px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  min-height: calc(100vh - 140px);
}

.user-sidebar {
  background: #f5f7fa;
  padding: 20px 0;
}

.user-info-card {
  text-align: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 10px;
}

.user-info-card h3 {
  margin: 12px 0 4px;
  font-size: 18px;
  color: #333;
}

.user-info-card p {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.user-menu {
  border-right: none;
  background: transparent;
}

.user-main {
  flex: 1;
  padding: 30px;
}
</style>
