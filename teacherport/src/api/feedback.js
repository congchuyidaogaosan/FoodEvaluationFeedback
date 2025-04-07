import request from '@/utils/request'

export function getFeedbackList(params) {
  return request({
    url: '/feedback/list',
    method: 'get',
    params
  })
}

export function replyFeedback(id, data) {
  return request({
    url: `/feedback/${id}/reply`,
    method: 'post',
    data
  })
}

export function deleteFeedback(id) {
  return request({
    url: `/feedback/${id}`,
    method: 'delete'
  })
} 