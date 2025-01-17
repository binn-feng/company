import request from '@/utils/request'

// 保存成本设置
export const saveCosting = (data) => {
  return request({
    url: '/manager/cost/batchAdd',
    method: 'post',
    data
  })
}

// 获取字典数据
export const getDictionary = (type) => {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: { type }
  })
}
// 成本设置列表
export const getCostList = (data) => {
  return request({
    url: '/manager/cost/page',
    method: 'post',
    data
  })
}
