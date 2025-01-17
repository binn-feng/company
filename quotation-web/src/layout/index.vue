<template>
  <a-layout class="layout">
    <a-sider v-model:collapsed="collapsed" :trigger="null" collapsible :collapsed-width="64" class="sider"
      :collapsible-style="{ transition: 'width 0.3s' }">
      <a-menu theme="dark" mode="inline" :defaultSelectedKeys="[defaultSelectedKey]" :selectedKeys="selectedKeys"
        @select="handleMenuSelect" :openKeys="openKeys" @openChange="handleOpenChange" style="margin-top: 16px;">
        <template v-for="route in visibleRoutes" :key="route.name">
          <!-- 一级菜单项 -->
          <a-menu-item v-if="(!route.children || route.children.length === 0) && !route.hidden"
            :to="{ name: route.name }" class="custom-menu-item">
            <div>
              <!-- <component :is="route.meta.icon" class="menu-icon" /> -->
              <svg-icon :icon-class="route.meta.icon" class="menu-icon" />
              <span v-if="!collapsed" class="menu-text">{{ route.meta.title }}</span>
            </div>
          </a-menu-item>

          <!-- 二级菜单项 -->
          <a-sub-menu v-if="route.children && route.children.length > 0" :key="route.name">
            <template #title v-if="route.meta">
              <span>
                <svg-icon :icon-class="route.meta.icon" class="menu-icon" />
                <span v-if="!collapsed" class="menu-text">{{ route.meta.title }}</span>
              </span>
            </template>
            <template v-for="child in route.children" :key="child.name">
              <a-menu-item :to="{ name: child.name }" class="custom-menu-item">
                <div class="menu-item-content">
                  <!-- <component :is="child.meta.icon" class="menu-icon" /> -->
                  <svg-icon :icon-class="child.meta.icon" class="menu-icon" />
                  <span class="menu-text">{{ child.meta.title }}</span>
                </div>
              </a-menu-item>
            </template>
          </a-sub-menu>
        </template>
      </a-menu>

    </a-sider>
    <a-layout>
      <a-header
        style="padding: 0;z-index: 1000; background: #fff; display: flex; align-items: center;position: relative; justify-content: space-between;height: 50px;-webkit-box-shadow: 0 1px 4px rgba(0, 21, 41, .08);box-shadow: 0 1px 4px rgba(0, 21, 41, .08);">
        <div class="header-content" style="display: flex; align-items: center; width: 100%;">
          <a-button type="text" @click="toggleCollapsed" style="font-size: 18px; width: 50px; height: 50px;">
            <MenuUnfoldOutlined v-if="collapsed" />
            <MenuFoldOutlined v-else />
          </a-button>
          <BreadcrumbView style="flex: 1;margin-left:5px" />
          <div class="navbar" style="display: flex; align-items: center;">
            <a-dropdown>
              <a class="ant-dropdown-link" @click.prevent>
                <DownOutlined style="margin-right: 10px;" />
                <a-avatar size="large" style="cursor: pointer;" :style="{ backgroundColor: '#fff',border: '1px solid #e2dddd' }">
                  <template #icon>
                    <img src="../assets/image.png" >
                  </template>
                </a-avatar>
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="goToProfile">个人信息</a-menu-item>
                  <router-link to="/"><a-menu-item>主页</a-menu-item></router-link>
                  <a-menu-item @click="handleLogout">退出登录</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
            <span style="color:rgb(82, 78, 78);margin-left:6px">{{ user.userName }}</span>
          </div>
        </div>
      </a-header>
      <a-content class="content-main" style="min-height: 280px; overflow:auto;">
        <router-view v-slot="{ Component }">
          <Transition name="fade" mode="out-in" appear>
              <!-- <keep-alive> -->
              <component :is="Component" />
              <!-- </keep-alive> -->
          </Transition>
        </router-view>
      </a-content>
    </a-layout>
  </a-layout>
</template>

<script>
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
// import { h } from 'vue';
import BreadcrumbView from './components/Breadcrumb.vue';
import SvgIcon from '@/components/SvgIcon';
import { Layout, Menu, Button } from 'ant-design-vue';
import { MenuFoldOutlined, MenuUnfoldOutlined, DownOutlined, UserOutlined } from '@ant-design/icons-vue';
import { HomeOutlined, FolderOutlined, PayCircleOutlined, DollarCircleOutlined, InfoCircleOutlined, ContactsOutlined, CheckOutlined, ExclamationOutlined, TrophyOutlined, ContainerOutlined, PartitionOutlined, ApartmentOutlined, DeliveredProcedureOutlined, FileDoneOutlined, PauseCircleOutlined, MinusCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';

export default {
  name: 'LayoutView',
  components: {
    ALayout: Layout,
    AHeader: Layout.Header,
    ASider: Layout.Sider,
    AContent: Layout.Content,
    AButton: Button,
    AMenu: Menu,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    DownOutlined,
    BreadcrumbView,
    SvgIcon,
    UserOutlined
  },
  setup() {
    const collapsed = ref(false);
    const route = useRoute();
    const router = useRouter();
    const selectedKeys = ref([route.name]);
    const openKeys = ref([]); // 用于控制哪些子菜单是打开的
    const user = computed(() => {
      const storedUser = localStorage.getItem('user');
      return storedUser ? JSON.parse(storedUser) : null; // 将字符串转换为对象，如果没有数据则返回 null
    });
    const routes = computed(() => {
      return route.matched[0].children || route.matched[0].name;  // 确保一级菜单是独立的
    });

    // 监听路由变化，动态控制 content-main 样式
    watch(route, () => {
      const contentMain = document.querySelector('.content-main')
      if (contentMain) {
        contentMain.style.overflowX = 'hidden'
      }
    })

    const store = useStore();
    // const visibleRoutes = computed(() => {
    //   return routes.value.filter(route => !route.hidden);
    // });
    const visibleRoutes = computed(() => {
      console.log(store.state.permission.sidebarRouters, '..............................')
      return store.state.permission.sidebarRouters.map(route => {
        if (!route.hidden) {
          if (route.children[0].name === 'Dashboard') {
            return {
              ...route,
              children: [] // 确保首页没有子菜单
            };
          }

          return {
            ...route,
            children: route.children ? route.children.filter(childRoute => !childRoute.hidden) : []
          };
        }
        return null;
      }).filter(route => route !== null);
    });

    const defaultSelectedKey = computed(() => {
      return route.name;
    });

    const handleMenuSelect = (key) => {
      console.log(key)
      if (key.key === 0) {
        selectedKeys.value = [key.key];
        router.push({ name: 'Dashboard' });
      } else {
        selectedKeys.value = [key.key];
        router.push({ name: key.key });
      }

    };

    const toggleCollapsed = () => {
      collapsed.value = !collapsed.value;
    };

    const goToProfile = () => {
      router.push({ name: 'profile' });
    };

    const handleLogout = () => {
      console.log('退出登录');
      store.dispatch('login/logout');
      store.dispatch('role/LogOut');
      router.push({ name: 'login' });
    };

    const getIcon = (iconName) => {
      return {
        HomeOutlined,
        InfoCircleOutlined,
        ContactsOutlined,
        CheckOutlined,
        ExclamationOutlined,
        TrophyOutlined,
        ContainerOutlined,
        PartitionOutlined,
        ApartmentOutlined,
        DeliveredProcedureOutlined,
        FileDoneOutlined,
        PauseCircleOutlined,
        MinusCircleOutlined,
        CloseCircleOutlined,
        DollarCircleOutlined,
        PayCircleOutlined,
        FolderOutlined
      }[iconName];
    };

    const handleOpenChange = (keys) => {
      // 使每次点击一个子菜单时，其他子菜单自动关闭
      if (keys.length === 0) {
        openKeys.value = [];
      } else {
        openKeys.value = [keys[keys.length - 1]]; // 只保留当前打开的子菜单
      }
    };

    // const renderSubMenuTitle = (route) => {
    //   if (route.meta) {
    //     return h('span', [
    //       h(<svg-icon :icon-class="route.meta.icon" class="menu-icon" />),
    //       h('span', { class: "menu-text" }, route.meta.title),
    //     ]);
    //   }
    // };

    watch(() => route.name, (newName) => {
      selectedKeys.value = [newName];
    });

    return {
      collapsed,
      routes,
      visibleRoutes,
      defaultSelectedKey,
      selectedKeys,
      toggleCollapsed,
      handleMenuSelect,
      getIcon,
      goToProfile,
      handleLogout,
      // renderSubMenuTitle,
      openKeys,
      handleOpenChange,
      user
    };
  },
};
</script>

<style scoped>
.user-avatar {
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 10px;
}

.sub-el-icon {
  color: currentColor;
  width: 1em;
  height: 1em;
}

.logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
  margin: 16px;
}

.layout {
  height: 100vh;
}

.a-sider {
  width: 200px;
}

.a-sider-collapsed {
  width: 64px;
}

.ant-header {
  position: relative;
}

.ant-header::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background-color: rgba(0, 0, 0, 0.1);
  transform: scaleY(0.5);
}

.ant-header {
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}

.ant-layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.custom-menu-item {
  padding-left: 0 !important;
}

.menu-item-content {
  display: flex;
  align-items: center;
}

.menu-icon {
  margin-right: 10px;
}

.menu-text {
  margin-left: 10px;
}

.navbar {
  margin: 16px;
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .a-sider {
    width: 64px;
  }

  .a-sider-collapsed {
    width: 64px;
  }

  .a-content {
    padding: 16px;
  }

  .a-header {
    justify-content: flex-start;
  }

  .custom-menu-item {
    padding-left: 0 !important;
    z-index: 999;
  }
}

/* 路由切换动画 */
/* fade-transform */
.fade-leave-active,
.fade-enter-active {
  transition: transform 0.5s ease, opacity 0.5s ease;
  overflow-x: hidden;
}
 
/* 可能为enter失效，拆分为 enter-from和enter-to */
.fade-enter-from {  
  opacity: 0;
  transform: translateX(-30px);
  overflow-x: hidden;
}
.fade-enter-to { 
  opacity: 1;
  transform: translateX(0px);
  overflow-x: hidden;
}
 
.fade-leave-to {
  opacity: 0;
  transform: translateX(30px);
  overflow-x: hidden;
}

</style>
