import request from '@/utils/request'

// 获取公告列表
export const getAnnouncementList = (params) => {
  return request.get('/announcement/list', { params })
}

// 获取公告详情
export const getAnnouncementDetail = (id) => {
  return request.get(`/announcement/${id}`)
}

// 发布公告（管理员）
export const addAnnouncement = (data) => {
  return request.post('/announcement', data)
}

// 更新公告（管理员）
export const updateAnnouncement = (id, data) => {
  return request.put(`/announcement/${id}`, data)
}

// 删除公告（管理员）
export const deleteAnnouncement = (id) => {
  return request.delete(`/announcement/${id}`)
}

// 设置公告置顶
export const setAnnouncementTop = (id, isTop) => {
  return request.put(`/announcement/${id}/top`, null, {
    params: { isTop }
  })
}

// 发布公告（管理员）
export const publishAnnouncement = (id) => {
  return request.put(`/announcement/${id}/publish`)
}
