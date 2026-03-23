import request from '@/utils/request'

// 分页获取标签列表
export const getTagList = (params) => {
  return request.get('/tag/list', { params })
}

// 获取所有标签（不分页）
export const getAllTags = () => {
  return request.get('/tag/all')
}

// 根据ID获取标签
export const getTagById = (id) => {
  return request.get(`/tag/${id}`)
}

// 新增标签
export const addTag = (data) => {
  return request.post('/tag', data)
}

// 更新标签
export const updateTag = (id, data) => {
  return request.put(`/tag/${id}`, data)
}

// 删除标签
export const deleteTag = (id) => {
  return request.delete(`/tag/${id}`)
}
