import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import auth from '@/plugins/auth'

export default {
  namespaced: true,

  state() {
    return {
      routes: [],
      addRoutes: [],
      defaultRoutes: [],
      topbarRouters: [],
      sidebarRouters: []
    }
  },

  mutations: {
    SET_ROUTES(state, routes) {
      console.log(routes,'动态路由')
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES(state, routes) {
      console.log(routes,'默认路由')
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_TOPBAR_ROUTES(state, routes) {
      console.log(routes,'顶部菜单栏路由')
    
      state.topbarRouters = routes
    },
    SET_SIDEBAR_ROUTERS(state, routes) {
      console.log(routes,'侧边栏路由')
      state.sidebarRouters = routes
    }
  },

  actions: {
    // 生成路由
    async GenerateRoutes({ commit }) {
      try {
        // 向后端请求路由数据
        const res = await getRouters({})
        if (res && res.data) {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
          router.addRoute(asyncRoutes);
  
          commit('SET_ROUTES', rewriteRoutes)
          commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
          commit('SET_DEFAULT_ROUTES', sidebarRoutes)
          commit('SET_TOPBAR_ROUTES', sidebarRoutes)
  
          return rewriteRoutes
        }
      } catch (error) {
        console.error('Error generating routes:', error)
      }
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  let children = []
  childrenMap.forEach(el => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = `${el.path}/${c.path}`
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = `${lastRouter.path}/${el.path}`
      if (el.children && el.children.length) {
        children = children.concat(filterChildren(el.children, el))
        return
      }
    }
    children.push(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

// 动态加载视图
export const loadView = (view) => {
  return () => import(`@/views/${view}`)
}

