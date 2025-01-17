import request from '@/utils/request'

// 保存报价设置
export const saveQuoteSettings = (data) => {
  return request({
    url: '/manager/quote/batchAdd',
    method: 'post',
    data
  })
}
// 报价设置列表
export const getQuoteList = (data) => {
  return request({
    url: '/manager/quote/page',
    method: 'post',
    data
  })
}
