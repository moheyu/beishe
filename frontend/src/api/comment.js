import request from '@/utils/request'

export function addComment(data) {
  return request({
    url: '/comment',
    method: 'post',
    data
  })
}

export function getCommentsByScenicId(scenicId) {
  return request({
    url: `/comment/scenic/${scenicId}`,
    method: 'get'
  })
}
