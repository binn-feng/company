import { createApp } from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import Antd from "ant-design-vue";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import * as AntIconsVue from "@ant-design/icons-vue";
import store from "./store";
import plugins from "./plugins";
import directive from './directive'
import '@/icons' // icon

router.beforeEach((to, from, next) => {
    const isAuthenticated = !!localStorage.getItem("token"); // 检查 token 是否存在

    if (
        to.matched.some((record) => record.meta.requiresAuth) &&
        !isAuthenticated
    ) {
        // 需要认证但用户未登录，重定向到登录页
        next({ name: "login" });
    } else {
        if (!isAuthenticated && to.name === "login") {
            next();                
        } else {
            // 如果用户已认证且正在访问登录页，直接跳转到首页
            if (isAuthenticated && to.name === "login") {
                next({ name: "Dashboard" });
            } else {
                if (store.state.role.roles.length === 0) {
                    // 判断当前用户是否已拉取完user_info信息
                    store
                        .dispatch("role/GetInfo")
                        .then(() => {
                            store
                                .dispatch("permission/GenerateRoutes")
                                .then((accessRoutes) => {
                                    if (accessRoutes) {
                                        accessRoutes.forEach((route) => {
                                            router.addRoute(route);
                                        });
                                    }
                                    if (to.redirectedFrom) {
                                        router.replace(to.redirectedFrom);
                                    } else {
                                        next({ ...to, replace: true });
                                    }
                                });
                        })
                        .catch((error) => {
                            if (error.response && error.response.status === 401) {
                                // 如果是401错误，清除无效token
                                localStorage.removeItem("token");
                                next({ name: "login", replace: true });
                            } else {
                                next({ name: "login", replace: true });
                            }
                        });
                } else {
                    next();
                }
            }
        }
    }
});

// createApp(App).use(router).use(Antd).use(ElementPlus).use(ElementPlusIconsVue).use(AntIconsVue).use(store).mount('#app')
const app = createApp(App);
app
    .use(router)
    .use(store)
    .use(Antd)
    .use(ElementPlus)
    .use(ElementPlusIconsVue)
    .use(AntIconsVue)
    .use(plugins)
    .use(directive)
    .mount("#app");
