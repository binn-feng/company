<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <!-- <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" /> -->
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    // ...mapGetters([
    //   'sidebar',
    //   // 'routers'
    // ]),
    ...mapGetters(['sidebarRouters', 'sidebar']),
    routes() {
      return this.$router.options.routes

      // 如果到时候有需要可以根据身份再来判断哪些需要显示什么  现在先全部显示了

      // const routers = this.$router.options.routes
      // const role = localStorage.getItem('role-code')
      // const username = localStorage.getItem('username')
      // if(role==='SUPER_ADMIN'){
      //   const superadmin = [routers[0],routers[1],routers[2],routers[3],routers[4],routers[5],routers[6],routers[7],routers[8],routers[10],routers[12],routers[13],routers[14]]
      //   return superadmin
      // }
      // if(username=="rpa" && role=='EMPLOYEE'){
      //   const rpc = [routers[0],routers[1],routers[11]]
      //   return rpc
      // }
      // if(role=="GROUP_MANAGER"){
      //   const group = [routers[0],routers[1],routers[2],routers[6],routers[14],routers[10]]
      //   return group
      // }
      // if(role=="EMPLOYEE"){
      //   const employee = [routers[0],routers[1],routers[2],routers[6],routers[14],routers[15]]
      //   return employee
      // }
      // if(role=="MANAGEMENT"){
      //   const area = [routers[0],routers[1],routers[2],routers[4],routers[7],routers[10],routers[12],routers[14]]
      //   return area
      // }
      // if(role=="HR"){
      //   const hr = [routers[0],routers[1],routers[2],routers[4],routers[12],routers[16],routers[14]]
      //   return hr
      // }
      // if(role=='FINANCE'){
      //   const finace = [routers[0],routers[1],routers[2],routers[4],routers[10],routers[12],routers[13],routers[17]]
      //   return finace
      // }
      // if(role=="AREA_MANAGER"){
      //   const areas = [routers[0],routers[1],routers[2],routers[6],routers[7],routers[9],routers[10],routers[13],routers[14]]
      //   return areas
      // }
      // console.log(this.$router.options.routes)
      // return this.$router.options.routes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>
