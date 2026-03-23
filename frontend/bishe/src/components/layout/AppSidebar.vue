<template>
  <el-aside class="app-sidebar" width="220px">
    <el-menu
      :default-active="$route.path"
      router
      class="sidebar-menu"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409eff"
    >
      <div class="sidebar-logo">
        <el-icon size="24"><Setting /></el-icon>
        <span>管理后台</span>
      </div>
      
      <el-menu-item index="/admin">
        <el-icon><Odometer /></el-icon>
        <span>仪表盘</span>
      </el-menu-item>
      
      <el-sub-menu index="/admin/scenic">
        <template #title>
          <el-icon><Picture /></el-icon>
          <span>景点管理</span>
        </template>
        <el-menu-item index="/admin/scenic">景点列表</el-menu-item>
        <el-menu-item index="/admin/scenic/add">新增景点</el-menu-item>
        <el-menu-item index="/admin/category/scenic">景点分类</el-menu-item>
      </el-sub-menu>
      
      <el-sub-menu index="/admin/route">
        <template #title>
          <el-icon><MapLocation /></el-icon>
          <span>路线管理</span>
        </template>
        <el-menu-item index="/admin/route">路线列表</el-menu-item>
        <el-menu-item index="/admin/route/add">新增路线</el-menu-item>
        <el-menu-item index="/admin/category/route">路线分类</el-menu-item>
      </el-sub-menu>
      
      <el-menu-item index="/admin/tag">
        <el-icon><CollectionTag /></el-icon>
        <span>标签管理</span>
      </el-menu-item>
      
      <el-menu-item index="/admin/announcement">
        <el-icon><Bell /></el-icon>
        <span>公告管理</span>
      </el-menu-item>
      
      <el-menu-item index="/admin/user">
        <el-icon><User /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      
      <el-menu-item index="/admin/statistics">
        <el-icon><TrendCharts /></el-icon>
        <span>数据统计</span>
      </el-menu-item>
      
      <el-divider />
      
      <el-menu-item index="/">
        <el-icon><HomeFilled /></el-icon>
        <span>返回前台</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script setup>
import {
  Setting,
  Odometer,
  Picture,
  MapLocation,
  CollectionTag,
  Bell,
  User,
  TrendCharts,
  HomeFilled
} from '@element-plus/icons-vue'
</script>

<style scoped>
.app-sidebar {
  background: #001529;
  min-height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1001;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
}

.sidebar-menu {
  border-right: none;
  min-height: 100vh;
  background: #001529 !important;
}

.sidebar-logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  background: #002140;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  height: 48px;
  line-height: 48px;
  font-size: 14px;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background: #1890ff !important;
}

:deep(.el-menu-item.is-active) {
  background: #1890ff !important;
  border-right: 3px solid #fff;
}

:deep(.el-divider) {
  background-color: rgba(255, 255, 255, 0.1);
  margin: 12px 16px;
  width: calc(100% - 32px);
}
</style>
