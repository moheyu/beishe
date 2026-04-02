import request from '@/utils/request'

// 添加评论
export const addComment = (data) => {
  return request.post('/comment', data)
}

// 获取景点评论
export const getScenicComments = (scenicId) => {
  return request.get(`/comment/scenic/${scenicId}`)
}

// 获取路线评论
export const getRouteComments = (routeId) => {
  return request.get(`/comment/route/${routeId}`)
}

// 获取当前用户的评论列表
export const getUserComments = () => {
  return request.get('/comment/user/list')
}

// 删除评论
export const removeComment = (id) => {
  return request.delete(`/comment/${id}`)
}
