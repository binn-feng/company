import request from '@/utils/request'

// 获取主产品列表
export const getMainProduct = (data) => {
  return request({
    url: '/manager/product/page',
    method: 'post',
    data
  })
}

// 获取子产品列表
export const getChildrenProduct = (data) => {
  return request({
    url: '/manager/product/childrenPage',
    method: 'post',
    data
  })
}

// 创建产品
export const createProduct = (data) => {
  return request({
    url: '/manager/product',
    method: 'post',
    data: data
  })
}

// 更新产品
export const updateProduct = (data) => {
  return request({
    url: '/manager/product/',
    method: 'put',
    data: data
  })
}

// 上架产品
export const publishProduct = (data) => {
  return request({
    url: '/manager/product/publish',
    method: 'post',
    data: data
  })
}

// 下架产品
export const unPublishProduct = (data) => {
  return request({
    url: '/manager/product/unPublish',
    method: 'post',
    data: data
  })
}

// 查询未对应的主产品列表
export const getProductOptions = (data) => {
  return request({
    url: '/manager/product/getOptions',
    method: 'post',
    data
  })
}
