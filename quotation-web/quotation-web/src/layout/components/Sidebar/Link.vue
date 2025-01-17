<template>  
    <router-link :to="to" class="link">  
      <slot></slot>  
    </router-link>  
  </template>  
  
  <script>  
  export default {  
    props: {  
      to: {  
        type: String,  
        required: true,  
      },  
    },  
  };  
  </script>  
  
  <style scoped>  
  .link {  
    text-decoration: none;  
    color: #000;  
  }  
  .link:hover {  
    color: #1890ff;  
  }  
  </style>