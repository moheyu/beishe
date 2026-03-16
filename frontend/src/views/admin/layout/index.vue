<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px">
        <div class="admin-logo">管理后台</div>
        <el-menu
          :default-active="activeMenu"
          router
          class="admin-menu"
        >
          <el-menu-item index="/admin/dashboard">
            <i class="el-icon-s-data"></i>
            <span slot="title">数据概览</span>
          </el-menu-item>
          <el-menu-item index="/admin/scenic-manage">
            <i class="el-icon-picture-outline"></i>
            <span slot="title">景区管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/user-manage">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32"></el-avatar>
                <span class="username">管理员</span>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'AdminLayout',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 清空用户状态
          this.$store.commit('CLEAR_USER_STATE')
          this.$message.success('退出成功')
          this.$router.push('/login')
        })
      }
    }
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-container {
  height: 100%;
}

.el-aside {
  background: #304156;
  color: white;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.admin-menu {
  flex: 1;
  overflow-y: auto;
}

.admin-logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: white;
  border-bottom: 1px solid #1f2d3d;
}

.admin-menu {
  border: none;
  background: #304156;
}

.admin-menu .el-menu-item {
  color: #bfcbd9;
}

.admin-menu .el-menu-item:hover {
  background: #263445;
}

.admin-menu .el-menu-item.is-active {
  color: #409eff;
}

.el-header {
  background: white;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 10px;
  font-size: 14px;
  color: #333;
}

.el-container > .el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.el-main {
  background: #f0f2f5;
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}
</style>
