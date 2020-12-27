import request from '@/utils/request'

// 查询物流下拉列表
export function getLogisticsSelect() {
  return request({
    url: '/service/logistics/select',
    method: 'get'
  })
}

// 获取物流列表
export function listLogistics(query) {
  return request({
    url: '/service/logistics/list',
    method: 'get',
    params: query
  })
}

// 新增物流
export function addLogistics(data) {
  return request({
    url: '/service/logistics',
    method: 'post',
    data: data
  })
}

// 修改物流
export function updateLogistics(data) {
  return request({
    url: '/service/logistics',
    method: 'put',
    data: data
  })
}

// 查询物流详细
export function getLogistics(logisticsId) {
  return request({
    url: '/service/logistics/' + logisticsId,
    method: 'get'
  })
}

// 删除物流信息
export function delLogistics(logisticsId) {
  return request({
    url: '/service/logistics/' + logisticsId,
    method: 'delete'
  })
}
