import axios from 'axios'
import { Message } from 'element-ui'
// 移除 Pinia 引入 → 改为引入 Vuex store（如果 store 抽成单独文件）
import store from '@/store'

const service = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 15000
})

service.interceptors.request.use(
    config => {
        // 从本地存储取token，和登录时存的key完全一致
        const token = localStorage.getItem('token')
        if (token) {
            // 加到请求头里，格式和后端要求的一致
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error('请求错误:', error)
        return Promise.reject(error)
    }
)

service.interceptors.response.use(
    response => {
        const res = response.data
        if (res.code !== 200) {
            Message({
                message: res.message || '请求失败',
                type: 'error',
                duration: 5 * 1000
            })
            if (res.code === 401) {
                // token过期，清空存储跳登录页
                localStorage.clear()
                window.location.href = '/login'
            }
            return Promise.reject(new Error(res.message || '请求失败'))
        } else {
            return res
        }
    },
    error => {
        console.error('响应错误:', error)
        if (error.response?.status === 401) {
            // token过期，清空存储跳登录页
            localStorage.clear()
            window.location.href = '/login'
        }
        Message({
            message: error.message || '网络错误',
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service