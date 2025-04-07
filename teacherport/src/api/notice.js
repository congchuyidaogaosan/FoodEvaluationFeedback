import request from '@/utils/request'

// 获取通知列表
export function getNoticeList(params) {
  return request({
    url: '/notice/list',
    method: 'get',
    params
  })
}

// 添加通知
export function addNotice(data) {
  return request({
    url: '/notice',
    method: 'post',
    data
  })
}

// 更新通知
export function updateNotice(id, data) {
  return request({
    url: `/notice/${id}`,
    method: 'put',
    data
  })
}

// 删除通知
export function deleteNotice(id) {
  return request({
    url: `/notice/${id}`,
    method: 'delete'
  })
} 