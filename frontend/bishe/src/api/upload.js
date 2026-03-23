import request from '@/utils/request'

// 上传景点图片
export const uploadScenicImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload/scenic-image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 上传头像
export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 批量上传景点图片
export const uploadScenicImages = (files) => {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  return request.post('/upload/scenic-images', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
