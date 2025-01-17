import request from '@/utils/request'

// 获取销售指标列表
export const getMetrics = (data) => {
  return request({
    url: '/manager/goals/page',
    method: 'post',
    data
  })
}

// 一键同步
export const getSync = () => {
  return request({
    url: '/manager/goal/sync',
    method: 'post'
  })
}
