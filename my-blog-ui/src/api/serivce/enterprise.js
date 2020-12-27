import request from '@/utils/request'

// 新增企业信息
export function addEnterprise(data) {
  return request({
    url: '/service/enterprise',
    method: 'post',
    data: data
  })
}