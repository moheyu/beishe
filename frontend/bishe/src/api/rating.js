import request from '@/utils/request'

export const rateScenic = (scenicId, score) => {
  return request.post(`/rating/${scenicId}`, null, { params: { score } })
}

export const getScenicRating = (scenicId) => {
  return request.get(`/rating/${scenicId}`)
}
