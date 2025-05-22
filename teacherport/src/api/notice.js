import request from '@/utils/request'

// 获取通知列表
export function getNoticeList(params) {
  return request({
    url: '/notices',
    method: 'get',
    params
  })
}

// 添加通知
export function addNotice(data) {
  return request({
    url: '/notices/save',
    method: 'post',
    data
  })
}

// 更新通知
export function updateNotice(id, data) {
  return request({
    url: `/notices/update`,
    method: 'put',
    data: {
      ...data,
      id
    }
  })
}

// 删除通知
export function deleteNotice(id) {
  return request({
    url: `/notices/del/${id}`,
    method: 'delete'
  })
}

// 获取通知详情
export function getNoticeDetail(id) {
  return request({
    url: `/notices/${id}`,
    method: 'get'
  })
} 