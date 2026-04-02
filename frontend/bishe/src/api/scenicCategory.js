import request from '@/utils/request'

// 获取景点分类列表（公开接口）
export const getScenicCategoryList = () => {
  return request.get('/scenic/categories')
}

// 添加景点分类
export const addScenicCategory = (data) => {
  return request.post('/admin/scenic/category', data)
}

// 更新景点分类
export const updateScenicCategory = (id, data) => {
  return request.put(`/admin/scenic/category/${id}`, data)
}

// 删除景点分类
export const deleteScenicCategory = (id) => {
  return request.delete(`/admin/scenic/category/${id}`)
}
