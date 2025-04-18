import request from '@/utils/request'

// 登录
export function login(data) {
  return request({
    url: '/LoginAdmin/login',
    method: 'post',
    data
  })
}

// 获取用户列表
export function getUserList() {
  return request({
    url: '/Student',
    method: 'get'
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/Student/save',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: `/Student/update`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/Student/del/${id}`,
    method: 'delete'
  })
} 