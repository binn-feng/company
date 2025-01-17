<template>  
    <div>  
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
    padding: 10px;  
  }  
  </style>