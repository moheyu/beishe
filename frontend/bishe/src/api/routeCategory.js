import request from '@/utils/request'

// 获取路线分类列表（公开接口）
export const getRouteCategoryList = () => {
  return request.get('/route/categories')
}

// 添加路线分类
export const addRouteCategory = (data) => {
  return request.post('/admin/route/category', data)
}

// 更新路线分类
export const updateRouteCategory = (id, data) => {
  return request.put(`/admin/route/category/${id}`, data)
}

// 删除路线分类
export const deleteRouteCategory = (id) => {
  return request.delete(`/admin/route/category/${id}`)
}
