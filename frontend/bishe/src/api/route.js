import request from '@/utils/request'

// ===== 公开接口（游客可访问） =====

// 获取路线列表（公开接口，无需登录）
export const getRouteList = (params) => {
  return request.get('/route/list', { params })
}

// 分页查询接口（公开接口，无需登录）
export const getRoutePage = (params) => {
  return request.get('/route/page', { params })
}

// 获取路线详情（公开接口，无需登录）
export const getRouteDetail = (id) => {
  return request.get(`/route/${id}`)
}

// 获取路线详细信息（公开接口，无需登录）
export const getRouteFullDetail = (id) => {
  return request.get(`/route/detail/${id}`)
}

// 获取推荐路线（公开接口，无需登录）
export const getRecommendRoute = (params) => {
  return request.get('/route/recommend', { params })
}

// 获取分类下的路线（公开接口，无需登录）
export const getRouteByCategory = (categoryId) => {
  return request.get(`/route/category/${categoryId}`)
}

// ===== 管理端接口（需要登录） =====

// 管理端路线列表（需要管理员权限）
export const getAdminRouteList = (params) => {
  return request.get('/admin/route/list', { params })
}

// 管理端路线分页（需要管理员权限）
export const getAdminRoutePage = (params) => {
  return request.get('/admin/route/page', { params })
}

// 管理端路线详情（需要管理员权限）
export const getAdminRouteDetail = (id) => {
  return request.get(`/admin/route/${id}`)
}

// 管理端路线详细信息（需要管理员权限）
export const getAdminRouteFullDetail = (id) => {
  return request.get(`/admin/route/detail/${id}`)
}

// 新增路线
export const addRoute = (data) => {
  return request.post('/admin/route', data)
}

// 更新路线
export const updateRoute = (id, data) => {
  return request.put(`/admin/route/${id}`, data)
}

// 删除路线
export const deleteRoute = (id) => {
  return request.delete(`/admin/route/${id}`)
}

// 审核路线
export const auditRoute = (id, status) => {
  return request.put(`/admin/route/${id}/audit`, null, {
    params: { status }
  })
}

// ====== 从 routeCategory.js 重新导出 ======
export { getRouteCategoryList, addRouteCategory, updateRouteCategory, deleteRouteCategory } from './routeCategory'
