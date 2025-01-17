import request from '@/utils/request'

// 获取自研列表
export const getSelfProcurement = (data) => {
  return request({
    url: '/manager/self/page',
    method: 'post',
    data
  })
}

// 获取外采列表
export const getExtProcurement = (data) => {
  return request({
    url: '/manager/ext/page',
    method: 'post',
    data
  })
}

// 创建自研
export const createSelfProcurement = (data) => {
  return request({
    url: '/manager/self',
    method: 'post',
    data: data
  })
}

// 创建外采
export const createExtProcurement = (data) => {
  return request({
    url: '/manager/ext',
    method: 'post',
    data: data
  })
}

// 更新自研
export const updateSelfProcurement = (data) => {
  return request({
    url: '/manager/self',
    method: 'put',
    data: data
  })
}

// 更新外采
export const updateExtProcurement = (data) => {
  return request({
    url: '/manager/ext',
    method: 'put',
    data: data
  })
}

// 上架自研
export const publishSelfProcurement = (data) => {
  return request({
    url: '/manager/self/publish',
    method: 'post',
    data: data
  })
}

// 下架自研
export const unPublishSelfProcurement = (data) => {
  return request({
    url: '/manager/self/unPublish',
    method: 'post',
    data: data
  })
}

// 上架外采
export const publishExtProcurement = (data) => {
  return request({
    url: '/manager/ext/publish',
    method: 'post',
    data: data
  })
}

// 下架外采
export const unPublishExtProcurement = (data) => {
  return request({
    url: '/manager/ext/unPublish',
    method: 'post',
    data: data
  })
}
