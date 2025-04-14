import request from '@/utils/request'

// 获取分类列表
export function getCategoryList() {
  return request({
    url: '/DishCategory',
    method: 'get'
  })
}

// 添加分类
export function addCategory(data) {
  return request({
    url: '/DishCategory/save',
    method: 'post',
    data
  })
}

// 更新分类
export function updateCategory(data) {
  return request({
    url: `/DishCategory/update`,
    method: 'put',
    data
  })
}

// 删除分类
export function deleteCategory(id) {
  return request({
    url: `/DishCategory/del/${id}`,
    method: 'delete'
  })
} 