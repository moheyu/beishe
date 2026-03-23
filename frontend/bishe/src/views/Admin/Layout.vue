<template>
  <el-container class="admin-layout">
    <AppSidebar />
    <el-container class="main-container">
      <el-header class="admin-header">
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="avatarUrl" />
              <span>{{ userStore.nickname || userStore.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const userStore = useUserStore()

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
.admin-layout {
  min-height: 100vh;
  display: flex;
}

.main-container {
  margin-left: 220px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.admin-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 64px;
  padding: 0 24px;
  flex-shrink: 0;
}

.header-right {
  display: flex;
  align-items: center;
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

.admin-main {
  background: #f5f7fa;
  flex: 1;
  padding: 24px;
  overflow-x: auto;
  display: block;
}

</style>
