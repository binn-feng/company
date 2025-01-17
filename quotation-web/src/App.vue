<template>
  <div id="app" class="container">
    <a-config-provider :locale="zhCN">
      <router-view />
    </a-config-provider>
  </div>
</template>

<script setup>
import zhCN from "ant-design-vue/es/locale/zh_CN";
</script>

<style lang="less">
body {
  margin: 0;
  padding: 0;
  // height: 100vh;
  // overflow: auto;
  overflow: hidden;
  height: 100vh;
  /* 确保占满全高 */
}

.container {
  height: 100%;
  // padding: 20px;
  /* 使容器高度填满父元素 */
  // overflow-y: auto;
  /* 只允许垂直滚动 */
}

#app {
  height: 100%;
  // padding: 20px;
  // overflow: auto;

}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.ant-card-head-title {
  text-align: left;
}

.ant-card {
  height: 100%;
}

.ant-layout-content {
  background-color: white;
}

.nomp {
  margin: 0;
  padding: 0 15px 0 0;
}

.fs16 {
  font-size: 17px !important;
  font-weight: 700;
}

.mg12 {
  margin: 12px 0 !important;
  display: inline-block;
}
</style>
