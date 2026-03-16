import request from '@/utils/request'

export function addCollection(scenicId) {
  return request({
    url: `/collection/${scenicId}`,
    method: 'post'
  })
}

export function deleteCollection(scenicId) {
  return request({
    url: `/collection/${scenicId}`,
    method: 'delete'
  })
}

export function getCollectionList() {
  return request({
    url: '/collection/list',
    method: 'get'
  })
}
