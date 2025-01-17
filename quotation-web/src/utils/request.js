import axios from 'axios';
import router from '@/router'; // 引入 Vue Router 实例
import { message } from 'ant-design-vue'

// 创建一个 axios 实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 基础URL，通常在.env文件中配置
  timeout: 5000 // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在请求头中添加token
    const token = localStorage.getItem('token');
    if (token) {
      // 解析 Token，检查是否过期
      const tokenPayload = JSON.parse(atob(token.split('.')[1]));
      const isTokenExpired = tokenPayload.exp * 1000 < Date.now();

      if (isTokenExpired) {
        // 如果 Token 过期，清除 Token 并跳转到登录页面
        localStorage.removeItem('token');
        router.push('/login'); // 跳转到登录页面
        return Promise.reject(new Error('Token 已过期，请重新登录'));
      } else {
        config.headers['Authorization'] = `Bearer ${token}`; // 设置请求头
      }
    }
    return config;
  },
  (error) => {
    console.error('请求错误: ', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 检查业务响应中的 code 是否为 401
    if (response.data.code === 401) {
      console.error('业务错误: 401 Unauthorized - Token 失效');
      // 清除本地 Token
      localStorage.removeItem('token');
      // 跳转到登录页面
      router.push('/login');
      return Promise.reject(new Error('Token 失效，请重新登录'));
    } else if (response.data.code === 500) {
      message.error(response.data.msg);
      return Promise.reject({ code: response.data.code, errorMessage: response.data.msg })
    } else {
      return response.data; // 正常返回业务数据
    }
  },
  (error) => {
    console.error('响应错误: ', error);
    return Promise.reject(error);
  }
);

export default service;
