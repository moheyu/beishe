import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const routes = [
  // 首页
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home/index.vue'),
    meta: { title: '首页' }
  },
  // 登录注册
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Auth/Login.vue'),
    meta: { title: '登录', public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Auth/Register.vue'),
    meta: { title: '注册', public: true }
  },
  // 景点
  {
    path: '/scenic/list',
    name: 'ScenicList',
    component: () => import('@/views/Scenic/List.vue'),
    meta: { title: '景点列表' }
  },
  {
    path: '/scenic/:id',
    name: 'ScenicDetail',
    component: () => import('@/views/Scenic/Detail.vue'),
    meta: { title: '景点详情' }
  },
  // 路线
  {
    path: '/route/list',
    name: 'RouteList',
    component: () => import('@/views/Route/List.vue'),
    meta: { title: '路线列表' }
  },
  {
    path: '/route/:id',
    name: 'RouteDetail',
    component: () => import('@/views/Route/Detail.vue'),
    meta: { title: '路线详情' }
  },
  // 论坛
  {
    path: '/forum',
    name: 'Forum',
    component: () => import('@/views/Forum/index.vue'),
    meta: { title: '论坛' }
  },
  {
    path: '/forum/create',
    name: 'ForumCreate',
    component: () => import('@/views/Forum/PostForm.vue'),
    meta: { title: '发布帖子', requireAuth: true }
  },
  {
    path: '/forum/:id/edit',
    name: 'ForumEdit',
    component: () => import('@/views/Forum/PostForm.vue'),
    meta: { title: '编辑帖子', requireAuth: true }
  },
  {
    path: '/forum/:id',
    name: 'ForumDetail',
    component: () => import('@/views/Forum/Detail.vue'),
    meta: { title: '帖子详情' }
  },
  // 公告
  {
    path: '/announcements',
    name: 'Announcements',
    component: () => import('@/views/Announcement/List.vue'),
    meta: { title: '公告列表' }
  },
  {
    path: '/announcements/:id',
    name: 'AnnouncementDetail',
    component: () => import('@/views/Announcement/Detail.vue'),
    meta: { title: '公告详情' }
  },
  // 用户中心
  {
    path: '/user',
    component: () => import('@/views/User/Layout.vue'),
    meta: { requireAuth: true },
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/User/Profile.vue'),
        meta: { title: '个人资料' }
      },
      {
        path: 'preferences',
        name: 'UserPreferences',
        component: () => import('@/views/User/Preferences.vue'),
        meta: { title: '偏好设置' }
      },
      {
        path: 'collections',
        name: 'UserCollections',
        component: () => import('@/views/User/Collections.vue'),
        meta: { title: '我的收藏' }
      },
      {
        path: 'comments',
        name: 'UserComments',
        component: () => import('@/views/User/Comments.vue'),
        meta: { title: '我的评论' }
      },
      {
        path: 'history',
        name: 'UserHistory',
        component: () => import('@/views/User/History.vue'),
        meta: { title: '浏览历史' }
      },
      {
        path: 'posts',
        name: 'UserPosts',
        component: () => import('@/views/User/Posts.vue'),
        meta: { title: '我的帖子' }
      }
    ]
  },
  // 管理后台
  {
    path: '/admin',
    component: () => import('@/views/Admin/Layout.vue'),
    meta: { requireAuth: true, requireAdmin: true },
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('@/views/Admin/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'scenic',
        name: 'AdminScenic',
        component: () => import('@/views/Admin/Scenic.vue'),
        meta: { title: '景点管理' }
      },
      {
        path: 'scenic/add',
        name: 'AdminScenicAdd',
        component: () => import('@/views/Admin/ScenicForm.vue'),
        meta: { title: '新增景点' }
      },
      {
        path: 'scenic/:id/edit',
        name: 'AdminScenicEdit',
        component: () => import('@/views/Admin/ScenicForm.vue'),
        meta: { title: '编辑景点' }
      },
      {
        path: 'route',
        name: 'AdminRoute',
        component: () => import('@/views/Admin/Route.vue'),
        meta: { title: '路线管理' }
      },
      {
        path: 'route/add',
        name: 'AdminRouteAdd',
        component: () => import('@/views/Admin/RouteForm.vue'),
        meta: { title: '新增路线' }
      },
      {
        path: 'route/:id/edit',
        name: 'AdminRouteEdit',
        component: () => import('@/views/Admin/RouteForm.vue'),
        meta: { title: '编辑路线' }
      },
      {
        path: 'category/scenic',
        name: 'AdminScenicCategory',
        component: () => import('@/views/Admin/ScenicCategory.vue'),
        meta: { title: '景点分类' }
      },
      {
        path: 'category/route',
        name: 'AdminRouteCategory',
        component: () => import('@/views/Admin/RouteCategory.vue'),
        meta: { title: '路线分类' }
      },
      {
        path: 'tag',
        name: 'AdminTag',
        component: () => import('@/views/Admin/Tag.vue'),
        meta: { title: '标签管理' }
      },
      {
        path: 'announcement',
        name: 'AdminAnnouncement',
        component: () => import('@/views/Admin/Announcement.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('@/views/Admin/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/Admin/Statistics.vue'),
        meta: { title: '数据统计' }
      }
    ]
  },
  // 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 旅游推荐平台`
  }
  
  // 公共页面直接放行
  if (to.meta.public) {
    return true
  }
  
  // 需要登录的页面
  if (to.meta.requireAuth) {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      return {
        path: '/login',
        query: { redirect: to.fullPath }
      }
    }
    
    // 需要管理员权限
    if (to.meta.requireAdmin && !userStore.isAdmin) {
      ElMessage.error('权限不足')
      return '/'
    }
  }
  
  return true
})

export default router
