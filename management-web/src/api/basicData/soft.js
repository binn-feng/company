import request from '@/utils/request'

// 获取软著列表
export const getMainSoft = (data) => {
  return request({
    url: '/manager/soft/page',
    method: 'post',
    data
  })
}

// 创建软著
export const createSoft = (data) => {
  return request({
    url: '/manager/soft',
    method: 'post',
    data: data
  })
}

// 更新软著
export const updateSoft = (data) => {
  return request({
    url: '/manager/soft',
    method: 'put',
    data: data
  })
}

// 查询未对应的软著列表
export const getSoftOptions = (data) => {
  return request({
    url: '/manager/soft/getOptions',
    method: 'post',
    data
  })
}
