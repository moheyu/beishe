<template>
  <div class="layout-container">
    <el-container>
      <el-header class="header" :class="{ 'header-hidden': isHeaderHidden }">
        <div class="header-left">
          <div class="logo" @click="$router.push('/home')">
            <img src="/logo.png" alt="智游推荐" class="logo-img">
          </div>
        </div>
        <div class="header-center">
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            router
            class="nav-menu"
          >
            <el-menu-item index="/home">首页</el-menu-item>
            <el-menu-item index="/scenic">景点</el-menu-item>
            <el-menu-item index="/recommend">推荐</el-menu-item>
            <el-menu-item index="/user">个人中心</el-menu-item>
          </el-menu>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :src="userInfo.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" :size="40"></el-avatar>
              <span class="username">{{ userInfo.nickname || userInfo.username }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人资料</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { logout } from '@/api/auth'

export default {
  name: 'Layout',
  data() {
    return {
      isHeaderHidden: false,
      lastScrollTop: 0
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    userInfo() {
      return this.$store.state.userInfo || {}
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    handleScroll() {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop
      if (scrollTop > this.lastScrollTop && scrollTop > 100) {
        this.isHeaderHidden = true
      } else {
        this.isHeaderHidden = false
      }
      this.lastScrollTop = scrollTop
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
        }).catch(() => {
          this.$message.info('已取消退出')
        })
      } else if (command === 'profile') {
        this.$router.push('/user')
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.el-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
  z-index: 100;
  height: 70px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  transition: transform 0.3s ease;
}

.header-hidden {
  transform: translateY(-100%);
}

.header-left {
  display: flex;
  align-items: center;
  min-width: 200px;
}

.logo {
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 16px;
  border-radius: 25px;
  background: transparent;
}

.logo:hover {
  transform: scale(1.05);
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.logo-img {
  height: 45px;
  width: auto;
  display: block;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu {
  border: none;
  background: transparent;
}

.nav-menu .el-menu-item {
  font-size: 16px;
  padding: 0 25px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
  transition: all 0.3s ease;
  border-radius: 20px;
  margin: 0 5px;
}

.nav-menu .el-menu-item:hover {
  color: white;
  background: rgba(255, 255, 255, 0.15);
}

.nav-menu .el-menu-item.is-active {
  color: white;
  background: rgba(255, 255, 255, 0.2);
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  min-width: 200px;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.username {
  margin: 0 10px;
  font-size: 15px;
  color: white;
  font-weight: 500;
}

.user-info i {
  color: white;
  font-size: 14px;
}

.el-main {
  flex:1;
  overflow-y: auto;
  padding-top: 70px;
}

.main-content {
  padding: 0;
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
}
</style>
