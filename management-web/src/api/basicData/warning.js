import request from '@/utils/request'

// 保存成本设置
export const saveWarningSettings = (data) => {
  return request({
    url: '/manager/warning',
    method: 'post',
    data
  })
}
// 预警设置列表
export const getWarningList = (data) => {
  return request({
    url: '/manager/warning/page',
    method: 'post',
    data
  })
}
