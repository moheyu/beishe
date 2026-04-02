<script setup>
import { RouterView, useRoute } from 'vue-router'
import { computed } from 'vue'
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'

const route = useRoute()

// 判断是否显示头部和底部（登录注册页和后台管理页不显示）
const showHeader = computed(() => {
  return !route.path.startsWith('/admin') && 
         !route.path.startsWith('/login') && 
         !route.path.startsWith('/register')
})

const showFooter = computed(() => {
  return !route.path.startsWith('/admin') && 
         !route.path.startsWith('/login') && 
         !route.path.startsWith('/register')
})

// 判断是否使用前台布局（添加padding-top避免被固定头部遮挡）
const useMainLayout = computed(() => {
  return showHeader.value
})
</script>

<template>
  <div class="app-wrapper">
    <AppHeader v-if="showHeader" />
    
    <main class="main-content" :class="{ 'with-header': useMainLayout }">
      <RouterView />
    </main>
    
    <AppFooter v-if="showFooter" />
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  background: #f5f7fa;
}

.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

.main-content.with-header {
  padding-top: 60px;
}
</style>
