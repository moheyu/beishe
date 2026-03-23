import request from '@/utils/request'

// ===== 公开接口（游客可访问） =====

// 获取景点列表（公开接口，无需登录）
export const getScenicList = (params) => {
  return request.get('/scenic/list', { params })
}

// 分页查询接口（公开接口，无需登录）
export const getScenicPage = (params) => {
  return request.get('/scenic/page', { params })
}

// 获取景点详情（公开接口，无需登录）
export const getScenicDetail = (id) => {
  return request.get(`/scenic/${id}`)
}

// 获取推荐景点（公开接口，无需登录）
export const getRecommendScenic = (params) => {
  return request.get('/scenic/recommend', { params })
}

// 获取景点分类下的景点（公开接口，无需登录）
export const getScenicByCategory = (categoryId) => {
  return request.get(`/scenic/category/${categoryId}`)
}

// 获取标签下的景点（公开接口，无需登录）
export const getScenicByTag = (tagId) => {
  return request.get(`/scenic/tag/${tagId}`)
}

// ===== 管理端接口（需要登录） =====

// 管理端景点列表（需要管理员权限）
export const getAdminScenicList = (params) => {
  return request.get('/admin/scenic/list', { params })
}

// 管理端景点分页（需要管理员权限）
export const getAdminScenicPage = (params) => {
  return request.get('/admin/scenic/page', { params })
}

// 管理端景点详情（需要管理员权限）
export const getAdminScenicDetail = (id) => {
  return request.get(`/admin/scenic/${id}`)
}

// 新增景点
export const addScenic = (data) => {
  return request.post('/admin/scenic', data)
}

// 更新景点
export const updateScenic = (id, data) => {
  return request.put(`/admin/scenic/${id}`, data)
}

// 删除景点
export const deleteScenic = (id) => {
  return request.delete(`/admin/scenic/${id}`)
}

// 审核景点
export const auditScenic = (id, status) => {
  return request.put(`/admin/scenic/${id}/audit`, null, {
    params: { status }
  })
}

// 设置推荐等级
export const setRecommendLevel = (id, recommendLevel) => {
  return request.put(`/admin/scenic/${id}/recommend-level`, null, {
    params: { recommendLevel }
  })
}

// ====== 从 scenicCategory.js 重新导出 ======
export { getScenicCategoryList, addScenicCategory, updateScenicCategory, deleteScenicCategory } from './scenicCategory'
