import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

import Layout from '@/views/layout/index.vue'
import Home from '@/views/home/index.vue'
import Login from '@/views/login/index.vue'
import Scenic from '@/views/scenic/index.vue'
import ScenicDetail from '@/views/scenic/detail.vue'
import Recommend from '@/views/recommend/index.vue'
import Collection from '@/views/collection/index.vue'
import User from '@/views/user/index.vue'
import Route from '@/views/route/index.vue'
import RouteDetail from '@/views/route/detail.vue'
import RouteManage from '@/views/admin/route-manage/index.vue'
import AdminLayout from '@/views/admin/layout/index.vue'
import AdminDashboard from '@/views/admin/dashboard/index.vue'
import ScenicManage from '@/views/admin/scenic-manage/index.vue'
import UserManage from '@/views/admin/user-manage/index.vue'
import NotFound from '@/views/error/404.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        redirect: '/home'
      },
      {
        path: 'home',
        name: 'Home',
        component: Home,
        meta: { title: '首页' }
      },
      {
        path: 'scenic',
        name: 'Scenic',
        component: Scenic,
        meta: { title: '景点列表' }
      },
      {
        path: 'scenic/:id',
        name: 'ScenicDetail',
        component: ScenicDetail,
        meta: { title: '景点详情' }
      },
      {
        path: 'recommend',
        name: 'Recommend',
        component: Recommend,
        meta: { title: '智能推荐', requiresAuth: true }
      },
      {
        path: 'collection',
        name: 'Collection',
        component: Collection,
        meta: { title: '我的收藏', requiresAuth: true }
      },
      {
        path: 'user',
        name: 'User',
        component: User,
        meta: { title: '个人中心', requiresAuth: true }
      },
      {
        path: 'route',
        name: 'Route',
        component: Route,
        meta: { title: '旅游路线', requiresAuth: false }
      },
      {
        path: 'route/:id',
        name: 'RouteDetail',
        component: RouteDetail,
        meta: { title: '路线详情', requiresAuth: false }
      }
    ]
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, role: 'admin' },
    children: [
      {
        path: '',
        redirect: 'dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: AdminDashboard,
        meta: { title: '管理后台', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'scenic-manage',
        name: 'ScenicManage',
        component: ScenicManage,
        meta: { title: '景点管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'user-manage',
        name: 'UserManage',
        component: UserManage,
        meta: { title: '用户管理', requiresAuth: true, role: 'admin' }
      },
      {
        path: 'route-manage',
        name: 'RouteManage',
        component: RouteManage,
        meta: { title: '路线管理', requiresAuth: true, role: 'admin' }
      }
    ]
  },
  {
    path: '*',
    component: NotFound,
    meta: { title: '404' }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: '/',
  routes
})

router.beforeEach((to, from, next) => {
  // 1. 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }

  // 2. 核心修改：直接读取 Vuex 实时状态（无异步延迟）
  const isAuthenticated = store.getters.isAuthenticated // 正确使用 store 实例
  const userInfo = store.state.userInfo

  // 3. 处理登录页逻辑
  if (to.path === '/login') {
    if (isAuthenticated) {
      // 已登录 → 跳首页，避免重复重定向
      if (from.path !== '/home') {
        next('/home')
        return
      }
    }
    next()
    return
  }

  // 4. 鉴权逻辑
  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      // 未登录：清空状态 + 跳登录页
      store.commit('CLEAR_USER_STATE')
      next('/login')
      return
    }

    // 角色权限校验
    if (to.meta.role) {
      const userRole = userInfo?.role || ''
      const roleMatch =
          (userRole === 1 && to.meta.role === 'admin') ||
          (userRole === 2 && to.meta.role === 'admin') ||
          (String(userRole) === to.meta.role)
      if (!roleMatch) {
        Vue.prototype.$message.error('无权限访问') // 替换 alert 为 ElementUI 提示
        next('/home')
        return
      }
    } else {
      // 普通用户页面：管理员禁止访问
      const userRole = userInfo?.role || ''
      if (userRole === 1 || userRole === 2 || String(userRole) === 'admin') {
        // 管理员访问普通用户页面，跳转到管理后台
        Vue.prototype.$message.warning('管理员禁止访问用户页面')
        next('/admin')
        return
      }
    }
  }

  next()
})

export default router