import request from '@/utils/request'

// 获取菜品列表
export function getDishList(params) {
  return request({
    url: '/dishes',
    method: 'get',
    params
  })
}

// 添加菜品
export function addDish(data) {
  return request({
    url: '/dishes/save',
    method: 'post',
    data
  })
}

// 更新菜品
export function updateDish(data) {
  return request({
    url: `/dishes/update`,
    method: 'put',
    data
  })
}

// 删除菜品
export function deleteDish(id) {
  return request({
    url: `/dishes/del/${id}`,
    method: 'delete'
  })
} 