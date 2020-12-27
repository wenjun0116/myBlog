import request from '@/utils/request'
import { praseStrEmpty } from '@/utils/demo'

// 获取商品编号
export function getGoodsCode() {
  return request({
    url: '/service/goods/getGoodsCode',
    method: 'get'
  })
}

// 新增商品
export function addGoods(data) {
  return request({
    url: '/service/goods',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateGoods(data) {
  return request({
    url: '/service/goods',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delGoods(goodsId) {
  return request({
    url: '/service/goods/' + goodsId,
    method: 'delete'
  })
}

// 导出商品列表
export function exportGoods(query) {
  return request({
    url: '/service/goods/export',
    method: 'get',
    params: query
  })
}

// 获取商品列表
export function listGoods(query) {
  return request({
    url: '/service/goods/list',
    method: 'get',
    params: query
  })
}

// 查询商品详细
export function getGoods(goodsId) {
  return request({
    url: '/service/goods/' + praseStrEmpty(goodsId),
    method: 'get'
  })
}

// 上传商品图片
export function uploadGoodsImage(data) {
  return request({
    url: '/service/goods/goodsImage',
    method: 'post',
    data: data
  })
}
