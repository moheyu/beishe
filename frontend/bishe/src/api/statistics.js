import request from '@/utils/request'

// 获取整体统计数据
export const getOverallStatistics = () => {
  return request.get('/admin/statistics/overall')
}

// 获取景点统计排行
export const getTopScenicStatistics = (limit = 10) => {
  return request.get('/admin/statistics/scenic/top', { params: { limit } })
}

// 获取路线统计排行
export const getTopRouteStatistics = (limit = 10) => {
  return request.get('/admin/statistics/route/top', { params: { limit } })
}

// 获取推荐统计数据
export const getRecommendationStatistics = () => {
  return request.get('/admin/statistics/recommendation')
}
