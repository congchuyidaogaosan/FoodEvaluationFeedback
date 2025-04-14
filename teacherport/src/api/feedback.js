import request from '@/utils/request'

export function getFeedbackList(params) {
  return request({
    url: '/notices',
    method: 'get',
    params
  })
}

export function replyFeedback(id, data) {
  return request({
    url: `/notices/${id}/reply`,
    method: 'post',
    data
  })
}

export function deleteFeedback(id) {
  return request({
    url: `/notices/${id}`,
    method: 'delete'
  })
} 