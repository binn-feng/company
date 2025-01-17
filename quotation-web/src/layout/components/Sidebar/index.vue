<template>  
    <a-layout class="layout">  
      <a-sider v-model:collapsed="collapsed" :trigger="null" collapsible :collapsed-width="64" class="sider">  
        <Logo />  
        <a-menu theme="dark" mode="inline" :defaultSelectedKeys="[defaultSelectedKey]" :selectedKeys="selectedKeys"  
          @select="handleMenuSelect">  
          <a-menu-item v-for="route in routes" :key="route.name" :to="route.path">  
            <span>{{ route.meta.title }}</span>  
          </a-menu-item>  
        </a-menu>  
      </a-sider>  
      <a-layout>  
        <a-header style="padding: 0; background: #fff;">  
          <a-button type="text" @click="toggleCollapsed" style="font-size: 16px; width: 64px; height: 64px;">  
            <menu-unfold-outlined v-if="collapsed" />  
            <menu-fold-outlined v-else />  
          </a-button>  
        </a-header>  
        <a-content style="margin: 24px; padding: 24px; min-height: 280px;">  
          <router-view />  
        </a-content>  
      </a-layout>  
    </a-layout>  
  </template>  
  
  <script>  
  import { ref, computed } from 'vue';  
  import { useRoute, useRouter } from 'vue-router';  
  import { Layout, Menu, Button } from 'ant-design-vue';  
  import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue';  
  import Logo from './Logo.vue'; // 确保路径正确  
  
  export default {  
    name: 'LayoutView',  
    components: {  
      ALayout: Layout,  
      AHeader: Layout.Header,  
      ASider: Layout.Sider,  
      AContent: Layout.Content,  
      AButton: Button,  
      AMenu: Menu,  
      AMenuItem: Menu.Item,  
      MenuFoldOutlined,  
      MenuUnfoldOutlined,  
      Logo,  
    },  
    setup() {  
      const collapsed = ref(false);  
      const route = useRoute();  
      const router = useRouter();  
      const selectedKeys = ref([route.name]);  
  
      const routes = computed(() => {  
        return route.matched[0].children;  
      });  
  
      const defaultSelectedKey = computed(() => {  
        return route.name;  
      });  
  
      const handleMenuSelect = (key) => {  
        router.push({ name: key });  
      };  
  
      const toggleCollapsed = () => {  
        collapsed.value = !collapsed.value;  
      };  
  
      return {  
        collapsed,  
        routes,  
        defaultSelectedKey,  
        selectedKeys,  
        toggleCollapsed,  
        handleMenuSelect,  
      };  
    },  
  };  
  </script>  
  
  <style scoped>  
  .logo {  
    height: 32px;  
    background: rgba(255, 255, 255, 0.3);  
    margin: 16px;  
  }  
  
  /* 侧边栏和内容区域的响应式设计 */  
  .layout {  
    height: 100vh;  
  }  
  
  .a-sider {  
    width: 200px;  
  }  
  
  .a-sider-collapsed {  
    width: 64px;  
  }  
  
  /* 媒体查询，针对小屏幕 */  
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
  }  
  </style>