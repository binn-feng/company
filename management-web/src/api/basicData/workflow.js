import request from '@/utils/request'

// 保存工作流信息
export const saveWorkflow = (data) => {
  return request({
    url: '/manager/setting/batch',
    method: 'post',
    data: data
  })
}

// 获取工作流信息
export const getWorkflow = (data) => {
  return request({
    url: '/manager/setting/page',
    method: 'post',
    data: data
  })
}
