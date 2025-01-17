import axios from 'axios'
import route from '../router/index'
import { removeToken } from '@/utils/auth'
import _ from 'lodash'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      // 解析 Token，检查是否过期
      const tokenPayload = JSON.parse(atob(token.split('.')[1]))
      const isTokenExpired = tokenPayload.exp * 1000 < Date.now()

      if (isTokenExpired) {
        localStorage.removeItem('token')
        route.push('/login')
        return Promise.reject(new Error('凭证已过期，请重新登录'))
      } else {
        config.headers['Authorization'] = `Bearer ${token}`
      }
    }
    return config
  },
  (error) => {
    console.error('请求错误: ', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    window.location.hasPrompt401 = false
    if (response.status === 200 || response.status === 204) {
      const resCode = response.data.code

      if (resCode === undefined || _.isNaN(resCode)) {
        return response.data
      } else if (resCode === 200) {
        return response.data
      } else if (resCode === 500) {
        Message.error({ message: response.data.msg, type: 'error' })
        return Promise.reject({ code: response.data.code, errorMessage: response.data.msg })
      } else if (resCode === 401) {
        Message.error('凭证已过期，请重新登录')
        removeToken()
        localStorage.removeItem('token')
        route.push({ path: '/login' })
        return response.data
      }
    }
    const errorMsg = { code: response.data.code, data: response.data }
    return Promise.reject(errorMsg)
  },
  (error) => {
    if (error.response && error.response.status) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          if (data.errorCode === '401-001') {
            Message.error(data.msg)
          } else if (!window.location.hasPrompt401 || error.response.config.url === '/api/atlas/v2/login') {
            window.location.hasPrompt401 = true
            data.errorType = 0
            data.errorCode = 401
            // router.push('/', () => {})
            removeToken()
            localStorage.removeItem('token')
            Message.error(data.msg || '凭证已过期，请重新登录')
            route.push({ path: '/login' })
          }
          Promise.reject(data)
          break
        case 500:
          Message.error(data.msg || '服务器错误，请稍后重试')
          Promise.reject(data)
          break
        default:
          Message.error(data.msg)
          Promise.reject(data)
          break
      }
    }
    return Promise.reject(error)
  }
)

export default service
