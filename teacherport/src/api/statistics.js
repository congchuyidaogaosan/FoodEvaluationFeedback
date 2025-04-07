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