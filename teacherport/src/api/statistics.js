import request from '@/utils/request'

// 获取评价统计数据
export function getStatistics() {
  return request({
    url: '/statistics/evaluation',
    method: 'get'
  })
}

// 获取菜品评价统计
export function getDishStatistics(params) {
  return request({
    url: '/statistics/dish',
    method: 'get',
    params
  })
}

// 获取每日评价趋势
export function getDailyTrend(params) {
  return request({
    url: '/statistics/daily',
    method: 'get',
    params
  })
}

// 获取评价统计概览
export function getEvaluationOverview() {
  return request({
    url: '/statistics/evaluation/overview',
    method: 'get'
  })
}

// 获取评价维度分析
export function getEvaluationDimension() {
  return request({
    url: '/statistics/evaluation/dimension',
    method: 'get'
  })
}

// 获取菜品评价统计数据
export function getEvaluationStatistics(dishId) {
  return request({
    url: `/statistics/evaluation/dish/${dishId}`,
    method: 'get'
  })
}

// 获取评价趋势数据
export function getEvaluationTrend() {
  return request({
    url: '/statistics/evaluation/trend',
    method: 'get'
  })
}

// 获取反馈统计数据
export function getFeedbackStatistics() {
  return request({
    url: '/statistics/feedback',
    method: 'get'
  })
} 