import request from '@/utils/request'

// 获取发片类型
export function queryInvoiceTypeAll() {
  return request({
    url: "/service/invoicecategory/type/list",
    method: "get",
  })
}
// 获取发票状态
export function queryInvoiceStatusAll() {
  return request({
    url: "/service/invoicecategory/status/list",
    method: "get",
  })
}
// 获取查询数据
export function getInvoicesList(query) {
  return request({
    url: "/service/invoice/list",
    method: "get",
    params: query
  })
}

// 获取详细信息
export function getInvoiceInfo(invoiceId) {
  return request({
    url: "/service/invoice/" + invoiceId,
    method: "get",
  })
}

// 修改发票状态
export function updateInvoiceStatus(invoice) {
  return request({
    url: '/service/invoice',
    method: 'put',
    data: invoice
  })
}
