import request from '@/utils/request'

// 添加偏好
export const addPreference = (data) => {
  return request.post('/preference', data)
}

// 删除偏好
export const deletePreference = (id) => {
  return request.delete(`/preference/${id}`)
}

// 获取偏好列表
export const getPreferenceList = () => {
  return request.get('/preference/list')
}

// 获取推荐景点
export const getRecommendScenic = () => {
  return request.get('/preference/recommend')
}

// 获取混合推荐
export const getHybridRecommend = (params) => {
  return request.get('/preference/recommend/hybrid', { params })
}

// 获取用户偏好
export const getUserPreferences = (userId) => {
  return request.get(`/preference/user/${userId}`)
}

// 获取当前用户偏好（别名）
export const getUserPreference = () => {
  return request.get('/preference/list')
}

// 获取偏好统计
export const getPreferenceStatistics = () => {
  return request.get('/preference/statistics')
}

// 更新用户偏好
export const updateUserPreference = (data) => {
  return request.put('/preference', data)
}
