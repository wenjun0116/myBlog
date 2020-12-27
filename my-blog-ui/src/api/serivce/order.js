import request from '@/utils/request'

// 查询订单列表
export function getOrderList(query) {
  return request({
    url: '/service/order/list',
    method: 'get',
    params: query
  })
}

// 获取订单详细
export function getOrderInfo(orderId) {
  return request({
    url: '/service/order/' + orderId,
    method: 'get'
  })
}

// 编辑订单状态
export function updateOrderStatus(order) {
  return request({
    url: '/service/order',
    method: 'put',
    data: order
  })
}
