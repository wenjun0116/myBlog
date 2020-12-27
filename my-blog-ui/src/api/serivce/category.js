import request from '@/utils/request'

// 查询商品类目下拉树结构
export function treeselect() {
  return request({
    url: 'service/category/treeselect',
    method: 'get'
  })
}

// 查询类目列表
export function listCategory(query) {
  return request({
    url: '/service/category/list',
    method: 'get',
    params: query
  })
}

// 查询类目列表（排除节点）
export function listCategoryExcludeChild(categoryId) {
  return request({
    url: 'service/category/list/exclude/' + categoryId,
    method: 'get'
  })
}

// 添加类目
export function addCategory(data) {
  return request({
    url: '/service/category',
    method: 'post',
    data: data
  })
}

// 查询类目详情
export function getCategory(categoryId) {
  return request({
    url: '/service/category/' + categoryId,
    method: 'get'
  })
}

// 修改类目
export function updateCategory(data) {
  return request({
    url: '/service/category',
    method: 'put',
    data: data
  })
}

// 删除类目
export function delCategory(categoryId) {
  return request({
    url: '/service/category/' + categoryId,
    method: 'delete'
  })
}
