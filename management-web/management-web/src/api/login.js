import request from '@/utils/request'

// 登录接口
export const login = (data) => {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}
