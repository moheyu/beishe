import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token（避免 Pinia 未初始化问题）
    const token = localStorage.getItem('token')
    
    console.log('=== 请求拦截器 ===')
    console.log('请求 URL:', config.url)
    console.log('Token 存在:', !!token)
    console.log('Token 前 20 位:', token ? token.substring(0, 20) + '...' : '无')
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('已添加 Authorization header')
    } else {
      console.warn('警告：Token 不存在！')
    }
    
    console.log('最终 headers:', config.headers)
    console.log('==================')
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 如果是 FormData，删除 Content-Type 让浏览器自动设置（包含 boundary）
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type']
    }
    
    // 添加时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data
    
    // 业务成功
    if (res.code === 200) {
      return res.data
    }
    
    // 业务错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    const { response } = error
    
    console.log('=== 响应错误详情 ===')
    console.log('HTTP 状态码:', response?.status)
    console.log('响应数据:', response?.data)
    console.log('错误消息:', response?.data?.message)
    console.log('==================')
    
    if (response) {
      switch (response.status) {
        case 401:
          // 从响应体中获取错误信息
          const errorMsg = response.data?.message || '登录已过期，请重新登录'
          ElMessage.error(errorMsg)
          const userStore = useUserStore()
          userStore.logout()
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(response.data?.message || '网络错误')
      }
    } else {
      ElMessage.error('网络连接失败')
    }
    
    return Promise.reject(error)
  }
)

export default request
