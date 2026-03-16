import request from '@/utils/request'

// 获取管理员统计数据
export function getAdminStats() {
  return request({
    url: '/admin/stats',
    method: 'get'
  })
}

// 获取景点列表（管理员版）
export function getAdminScenicList(params) {
  return request({
    url: '/admin/scenic/list', // 最终请求路径：baseURL(/api) + url(/admin/scenic/list) = /api/admin/scenic/list
    method: 'get',
    params
  })
}

// 添加景点
export function addScenic(data) {
  return request({
    url: '/admin/scenic', // 最终路径：/api/admin/scenic
    method: 'post',
    data
  })
}

// 更新景点
export function updateScenic(id, data) {
  return request({
    url: `/admin/scenic/${id}`, // 最终路径：/api/admin/scenic/8
    method: 'put',
    data
  })
}

// 删除景区接口（和后端@DeleteMapping("/{id}")匹配）
export function deleteScenic(id) {
  return request({
    url: `/admin/scenic/${id}`, // 最终路径：/api/admin/scenic/8
    method: 'delete' // 请求方法：DELETE
  })
}

// 获取用户列表（管理员版）
export function getAdminUserList(params) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  })
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/user/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 更新用户信息（管理员）
export function updateUser(id, data) {
  return request({
    url: `/user/${id}`,
    method: 'put',
    data
  })
}