import request from '@/utils/request'

// 获取用户信息
export const getUserInfo = () => {
  return request.get('/user/info')
}

// 更新用户信息
export const updateUserInfo = (data) => {
  return request.put('/user/update', data)
}

// 获取用户列表（管理员）
export const getUserList = (params) => {
  return request.get('/user/list', { params })
}

// 根据ID获取用户（管理员）
export const getUserById = (id) => {
  return request.get(`/user/${id}`)
}

// 更新用户（管理员）
export const updateUserById = (id, data) => {
  return request.put(`/user/${id}`, data)
}

// 删除用户（管理员）
export const deleteUser = (id) => {
  return request.delete(`/user/${id}`)
}
