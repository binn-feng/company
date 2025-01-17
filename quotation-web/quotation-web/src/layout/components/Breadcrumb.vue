<template>
    <a-breadcrumb :routes="breadcrumbRoutes" />
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const breadcrumbRoutes = computed(() => {
    const matchedRoutes = route.matched;
    return matchedRoutes.filter(route => route.name !== 'Layout').map(route => {
        return {
            path: route.path,
            breadcrumbName: route.meta.title || route.name,
        };
    });
});
</script>

<style scoped>
.a-breadcrumb {
    margin: 16px 0; 
    font-size: 16px; 
}

.a-breadcrumb a {
    color: #1890ff; 
}

.a-breadcrumb .ant-breadcrumb li:last-child {
    color: #ccc; 
}
</style>
