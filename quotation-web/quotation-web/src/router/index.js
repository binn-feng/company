import { createRouter, createWebHistory } from 'vue-router';  
import LayoutView from '@/layout';  
// import RequestView from '../views/RequestQuotation/index.vue';  

export const constantRoutes = [  
  {  
    path: '/login',  
    name: 'login',  
    hidden: true,
    component: () => import('@/views/login/index'),  
  },
  {  
    path: '/',  
    name: 'Layout',  
    component: LayoutView, 
    redirect: '/dashboard', 
    meta: { title: '首页', icon: 'dashboard', requiresAuth: true  }, 
    children: [  
      {
        path: 'dashboard',  
        name: 'Dashboard',  
        meta: { title: '首页', icon: 'dashboard', requiresAuth: true  }, 
        component:  () => import('@/views/dashboard'),  
      },  
      {  
        path: 'profile',  
        name: 'profile',  
        hidden: true,  
        component: () => import('@/views/profile'),  
        meta: { title: '个人中心', icon: 'TrophyOutlined' },  
      },  
      {  
        path: '/costdetailquotation',  
        name: 'costdetailquotation',  
        component: () => import('@/views/components/costdetailquotation/index.vue'),  
        meta: { title: '详细成本报价', icon: 'TrophyOutlined',breadcrumb: '详细成本报价' },  
        hidden: true,  
      },  
    ],  
  },
];  

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  // {  
  //   path: '/cost/quotetocost',
  //   hidden: true,
  //   component: LayoutView,
  //   children: [
  //     {
  //       path: 'costdetailquotation',
  //       component: () => import('@/views/components/costdetailquotation/index.vue'),  
  //       meta: { title: '详细成本报价', icon: 'TrophyOutlined',breadcrumb: '详细成本报价' }, 
  //       name: 'costdetailquotation',
  //     }
  //   ]  
  // }, 
]

const router = createRouter({  
  history: createWebHistory(process.env.BASE_URL),  
  routes: constantRoutes,  
});  

export default router;