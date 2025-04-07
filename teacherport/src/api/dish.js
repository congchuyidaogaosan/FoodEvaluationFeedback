import request from '@/utils/request'

// 获取菜品列表
export function getDishList(params) {
  return request({
    url: '/dish/list',
    method: 'get',
    params
  })
}

// 添加菜品
export function addDish(data) {
  return request({
    url: '/dish',
    method: 'post',
    data
  })
}

// 更新菜品
export function updateDish(id, data) {
  return request({
    url: `/dish/${id}`,
    method: 'put',
    data
  })
}

// 删除菜品
export function deleteDish(id) {
  return request({
    url: `/dish/${id}`,
    method: 'delete'
  })
} 