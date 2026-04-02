import request from '@/utils/request'

// 添加浏览记录
export const addViewRecord = (scenicId) => {
  return request.post(`/view-record/${scenicId}`)
}

// 获取浏览记录列表
export const getViewRecordList = () => {
  return request.get('/view-record/list')
}

// 获取当前用户的浏览记录（别名）
export const getUserViewRecords = () => {
  return request.get('/view-record/list')
}

// 清空当前用户的浏览记录
export const clearUserViewRecords = () => {
  return request.delete('/view-record/clear')
}
