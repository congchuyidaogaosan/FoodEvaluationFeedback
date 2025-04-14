import request from '@/utils/request'

// 获取评价列表
export function getEvaluationList(params) {
  return request({
    url: '/evaluations',
    method: 'get',
    params
  })
}

// 获取评价详情
export function getEvaluationDetail(id) {
  return request({
    url: `/evaluations/${id}`,
    method: 'get'
  })
}

// 回复评价
export function replyEvaluation(id, data) {
  return request({
    url: `/evaluations/${id}/reply`,
    method: 'post',
    data
  })
}

// 删除评价
export function deleteEvaluation(id) {
  return request({
    url: `/evaluations/${id}`,
    method: 'delete'
  })
} 