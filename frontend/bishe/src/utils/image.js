/**
 * 图片 URL 处理工具函数
 * 将后端返回的相对路径拼接为完整 URL
 */

const BASE_URL = '/api'

/**
 * 获取完整的图片 URL
 * @param {string} url - 图片路径（相对或绝对）
 * @returns {string} 完整的图片 URL
 */
export const getImageUrl = (url) => {
  if (!url) return ''
  // 如果已经是完整 URL 或 blob URL，检查并修复/api前缀问题
  if (url.startsWith('http') || url.startsWith('blob:') || url.startsWith('data:')) {
    // 修复包含/api/uploads的错误URL
    if (url.includes('/api/uploads')) {
      return url.replace('/api/uploads', '/uploads')
    }
    return url
  }
  // 拼接完整 URL
  return `${BASE_URL}${url}`
}

/**
 * 获取图片数组中的第一张图片 URL
 * @param {Array|string} images - 图片数组或单张图片路径
 * @returns {string} 完整的图片 URL
 */
export const getFirstImageUrl = (images) => {
  if (!images) return ''
  // 如果是数组，取第一张
  const url = Array.isArray(images) ? images[0] : images
  return getImageUrl(url)
}
