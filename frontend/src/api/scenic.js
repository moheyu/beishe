import request from '@/utils/request'

export function getScenicList(params) {
  return request({
    url: '/scenic/list',
    method: 'get',
    params
  })
}

export function getScenicDetail(id) {
  return request({
    url: `/scenic/${id}`,
    method: 'get'
  })
}

export function getRecommendScenic(params) {
  return request({
    url: '/scenic/recommend',
    method: 'get',
    params
  })
}

export function getScenicByCategory(categoryId) {
  return request({
    url: `/scenic/category/${categoryId}`,
    method: 'get'
  })
}

export function getScenicByTag(tagId) {
  return request({
    url: `/scenic/tag/${tagId}`,
    method: 'get'
  })
}
