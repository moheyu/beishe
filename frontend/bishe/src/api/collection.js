import request from '@/utils/request'

// 添加景点收藏
export const addScenicCollection = (scenicId) => {
  return request.post(`/collection/scenic/${scenicId}`)
}

// 添加路线收藏
export const addRouteCollection = (routeId) => {
  return request.post(`/collection/route/${routeId}`)
}

// 取消景点收藏
export const removeScenicCollection = (scenicId) => {
  return request.delete(`/collection/scenic/${scenicId}`)
}

// 取消路线收藏
export const removeRouteCollection = (routeId) => {
  return request.delete(`/collection/route/${routeId}`)
}

// 获取收藏列表
export const getCollectionList = () => {
  return request.get('/collection/list')
}

// 获取用户收藏列表
export const getUserCollections = (userId) => {
  return request.get(`/collection/user/${userId}`)
}
