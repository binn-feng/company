<template>  
    <div class="sidebar-item">  
      <Link :to="item.path">{{ item.meta.title }}</Link>  
    </div>  
  </template>  
  
  <script>  
  import Link from './Link.vue';  
  
  export default {  
    components: {  
      Link,  
    },  
    props: {  
      item: {  
        type: Object,  
        required: true,  
      },  
    },  
  };  
  </script>  
  
  <style scoped>  
  .sidebar-item {  
    padding: 15px; /* 增加内边距 */  
    transition: background 0.3s; /* 增加背景色变化的过渡效果 */  
  }  
  
  .sidebar-item:hover {  
    background-color: #f0f0f0; /* 悬停时的背景色 */  
  }  
  </style>