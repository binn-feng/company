<template>
    <div class="app-main">
        <slot :key="$route.fullPath"></slot> <!-- 添加 key 以确保更新 -->
    </div>
</template>
<script>
export default {
    name: 'AppMain',
};
</script>  
  
<style scoped>  .app-main {
      margin-left: 220px;
      /* 确保内容区与侧边栏对齐 */
      padding: 20px;
      flex: 1;
      /* 确保该部分自适应 */
      background-color: #ffffff;
      /* 为内容区域添加背景色 */
      border-radius: 8px;
      /* 添加圆角 */
  }</style>