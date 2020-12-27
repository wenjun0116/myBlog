import request from '@/utils/request'

// 获取地区下拉列表
export function treeselect() {
  return request({
    url: '/china/treeselect',
    method: 'get'
  })
}

// 获取省直辖市下拉列表
export function chinaAreaCitySelect() {
  return request({
    url: '/china/areaCity',
    method: 'get'
  })
}

// 根据地区ID获取子地区
export function selectChildren(id) {
  return request({
    url: '/china/' + id,
    method: 'get'
  })
}
