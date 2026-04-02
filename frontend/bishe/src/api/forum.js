import request from '@/utils/request'

// 获取论坛帖子列表
export const getForumPostList = (params) => {
  return request.get('/forum/list', { params })
}

// 获取帖子详情
export const getForumPostDetail = (id) => {
  return request.get(`/forum/${id}`)
}

// 获取推荐帖子
export const getRecommendForumPost = (params) => {
  return request.get('/forum/recommend', { params })
}

// 发布帖子
export const addForumPost = (data) => {
  return request.post('/forum', data)
}

// 更新帖子
export const updateForumPost = (id, data) => {
  return request.put(`/forum/${id}`, data)
}

// 删除帖子
export const deleteForumPost = (id) => {
  return request.delete(`/forum/${id}`)
}

// 获取帖子回复列表
export const getForumReplyList = (postId, params) => {
  return request.get(`/forum/${postId}/replies`, { params })
}

// 发布回复
export const addForumReply = (postId, data) => {
  return request.post(`/forum/${postId}/reply`, data)
}

// 删除回复
export const deleteForumReply = (id) => {
  return request.delete(`/forum/reply/${id}`)
}
