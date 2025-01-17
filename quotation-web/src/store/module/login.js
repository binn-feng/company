import { login as loginApi } from '@/api/login'; // 导入登录 API  
import { getInfo as getInfoApi } from '@/api/common'; // 导入获取用户信息接口  

const loginModule = {  
    namespaced: true, // 开启命名空间  
    state: () => ({  
        isAuthenticated: false,  
        user: null, // 用于存储用户信息 
        permissions: [], // 用于存储用户权限 
        token: null,  
    }),  
    mutations: {  
        login(state, { token, user }) {  
            console.log('token', token);  
            state.isAuthenticated = true;  
            state.token = token;  
            state.user = user; // 存储用户信息  
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify(state.user)); // 存储 token 到 localStorage  
        },  
        logout(state) {  
            state.isAuthenticated = false;  
            state.user = null;  
            state.token = null;  
            localStorage.removeItem('token');
            localStorage.removeItem('user'); // 清除本地存储的 token  
        },  
        setUser(state, user) {  
            console.log('user',user)
            state.user = user; // 更新用户信息 
            localStorage.setItem('user', JSON.stringify(state.user)); 
        }, 
        setPermissions(state, permissions) {  
            console.log('permissions', permissions);  
            state.permissions = permissions; // 更新用户权限  
            localStorage.setItem('permissions', JSON.stringify(state.permissions)); // 存储到 localStorage  
        },   
    },  
    actions: {  
        async login({ commit }, credentials) {  
            try {  
                const response = await loginApi(credentials);  
                const { token } = response; // 假设 API 返回的 token  
                commit('login', { token, user: null }); // 登录时不存储用户信息  
            } catch (error) {  
                console.error('Login failed:', error);  
                throw error;  
            }  
        },  
        async fetchUserInfo({ commit }) {  
            try {  
                const response = await getInfoApi(); // 调用 getInfo 接口  
                commit('setUser', response.user); 
                commit('setPermissions', response.permissions);  
            } catch (error) {  
                console.error('Failed to fetch user info:', error);  
                throw error;  
            }  
        },  
        logout({ commit }) {  
            commit('logout');  
        },  
    },  
};  

export default loginModule;