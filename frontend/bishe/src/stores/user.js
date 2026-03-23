import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, getUserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  
  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => {
    return userInfo.value?.role === 1 || userInfo.value?.role === 2
  })
  const username = computed(() => userInfo.value?.username || '')
  const nickname = computed(() => userInfo.value?.nickname || '')
  const avatar = computed(() => userInfo.value?.avatar || '')
  
  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }
  
  const login = async (credentials) => {
    const data = await loginApi(credentials)
    setToken(data.token)
    // 合并 userInfo 和 token 到 userInfo 中存储
    const userData = {
      ...data.userInfo,
      token: data.token
    }
    setUserInfo(userData)
    return userData
  }
  
  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }
  
  const fetchUserInfo = async () => {
    try {
      const data = await getUserInfo()
      setUserInfo(data)
      return data
    } catch (error) {
      logout()
      throw error
    }
  }
  
  const updateUserInfo = (data) => {
    userInfo.value = { ...userInfo.value, ...data }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }
  
  // 初始化时从localStorage恢复
  const initFromStorage = () => {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')
    
    if (storedToken) {
      token.value = storedToken
    }
    
    if (storedUserInfo) {
      try {
        userInfo.value = JSON.parse(storedUserInfo)
      } catch (e) {
        console.error('解析用户信息失败', e)
      }
    }
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    isAdmin,
    username,
    nickname,
    avatar,
    login,
    logout,
    fetchUserInfo,
    updateUserInfo,
    initFromStorage,
    setToken,
    setUserInfo
  }
})
