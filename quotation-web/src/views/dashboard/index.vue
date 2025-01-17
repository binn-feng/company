<template>
  <div class="todo-container">
    <header>
      <h1>待办事项</h1>
      <p>管理您的日常任务和目标。</p>
      <div class="sync-buttons">
        <!-- 一键同步按钮 -->
        <a-button type="primary" @click="confirmSynchronization" :loading="loading">
          一键同步
        </a-button>
      </div>
    </header>

    <!-- 待办事项列表 -->
    <div class="todo-list">
      <div class="todo-header">
        <div class="header-item">
          <span>任务</span>
        </div>
        <div class="header-item">
          <span>数量</span>
        </div>
      </div>

      <div class="todo-items">
        <a-card v-for="(item, filteredIndex) in filteredTodoItems" :key="filteredIndex" class="stat-item" @click="toggleDetails(filteredIndex)">
          <div class="stat-title">{{ item.title }}</div>
          <div class="stat-number">{{ item.count }}</div>

          <!-- 展开详细列表 -->
          <div v-if="item.isExpanded" class="item-details">
            <a-button type="primary" @click="goToDetailPage(item.title)">
              查看详情
            </a-button>
          </div>
        </a-card>
      </div>
    </div>

    <!-- 页面底部 -->
    <footer>
      <p>&copy; {{ currentYear }} 我的任务管理</p>
    </footer>

    <!-- Loading 状态 -->
    <a-spin v-if="loading" class="loading-spinner"></a-spin>
  </div>
</template>
  
<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { syncOpportunities, } from '@/api/costqoute';
import { getIndexPageInfo } from '@/api/dashboard';
import { Modal } from 'ant-design-vue';

const router = useRouter();

const todoItems = ref([
  { title: '待成本报价', count: 5, isExpanded: false, permission: 'quote:await:page' },
  { title: '待更新成本报价', count: 5, isExpanded: false, permission: 'quote:await:update:page' },
  { title: '待销售报价', count: 8, isExpanded: false, permission: 'quote:await:sale:page' },
  { title: '待报价审批', count: 8, isExpanded: false, permission: 'quote:approval:page' },
  { title: '报价审批未通过', count: 2, isExpanded: false, permission: 'quote:rejected:page' },
  { title: '待签约审批', count: 1, isExpanded: false, permission: 'quote:contract:approval:page' },
  { title: '签约审批未通过', count: 1, isExpanded: false, permission: 'quote:contract:rejected:page' },
  { title: '待签合同', count: 1, isExpanded: false, permission: 'quote:contract:pending:page' },
]);

const loading = ref(false);
const currentYear = new Date().getFullYear();
// 获取当前用户权限  
const userPermissions = JSON.parse(localStorage.getItem('permissions')) || [];
const isAdmin = userPermissions.includes('*:*:*');
// 根据权限过滤待办事项  
const filteredTodoItems = computed(() => {
  if (isAdmin) {
    return todoItems.value;
  }
  return todoItems.value.filter(item => userPermissions.includes(item.permission));
});
// 切换待办事项的展开状态
const toggleDetails = (filteredIndex) => {
  // 获取 filteredTodoItems 中的索引对应的 todoItems 的实际索引
  const originalIndex = todoItems.value.findIndex(item => item.title === filteredTodoItems.value[filteredIndex].title);
  
  // 切换 todoItems 中对应项的展开状态
  if (originalIndex !== -1) {
    todoItems.value[originalIndex].isExpanded = !todoItems.value[originalIndex].isExpanded;
  }
};


const fetchTodoItems = async () => {
  loading.value = true;
  try {
    const response = await getIndexPageInfo(); // 调用接口获取数据
    if (response) {
      // 更新待办事项列表
      todoItems.value = [
        { title: '待成本报价', count: response.listWithCostCount, isExpanded: false, permission: 'quote:await:page' },
        { title: '待更新成本报价', count: response.listAwaitUpdateCount, isExpanded: false, permission: 'quote:await:update:page' },
        { title: '待销售报价', count: response.listWithSalesCount, isExpanded: false, permission: 'quote:await:sale:page' },
        { title: '待报价审批', count: response.listWithApprovalCount, isExpanded: false, permission: 'quote:approval:page' },
        { title: '报价审批未通过', count: response.listWithApprovalFailCount, isExpanded: false, permission: 'quote:rejected:page' },
        { title: '待签约审批', count: response.listWithSignCount, isExpanded: false, permission: 'quote:contract:approval:page' },
        { title: '签约审批未通过', count: response.listWithSignFailCount, isExpanded: false, permission: 'quote:contract:rejected:page' },
        { title: '待签合同', count: response.listWithContractCount, isExpanded: false, permission: 'quote:contract:pending:page' },
      ];
    } else {
      Modal.error({ title: '加载失败', content: '获取待办事项失败，请稍后重试' });
    }
  } catch (error) {
    console.error('获取待办事项出错:', error);
    Modal.error({ title: '加载失败', content: '发生了错误，请稍后重试' });
  } finally {
    loading.value = false; // 加载结束
  }
};

// 跳转到对应的详情页面
const goToDetailPage = (title) => {
  // 根据任务标题或其他属性跳转到不同的路由
  const routeName = getRouteNameByTitle(title);
  if (routeName) {
    router.push({ name: routeName });
  }
};

// 根据标题映射路由名称
const getRouteNameByTitle = (title) => {
  const routesMap = {
    '待成本报价': 'Quotetocost',
    '待更新成本报价': 'Awaitingupdatecost',
    '已成本报价': 'Quotedatcost',
    '无法报价': 'Unablequotelist',
    '待销售报价': 'Salestoquotation',
    '待报价审批': 'Companyapproval',
    '报价审批未通过': 'Pendingquotation',
    '待签约审批': 'Pendingsigningapproval',
    '签约审批未通过': 'Unapproval',
    '待签合同': 'Pendingcontractsigning',
  };
  return routesMap[title]; // 返回对应的路由名称
};

// 页面加载时获取待办事项
onMounted(() => {
  fetchTodoItems();
});


// 确认同步
const confirmSynchronization = () => {
  Modal.confirm({
    title: '确认同步',
    content: '您确定要进行一键同步吗?',
    okText: '确认',
    cancelText: '取消',
    onOk: handleSynchronization,
  });
};

// 同步处理
const handleSynchronization = async () => {
  loading.value = true;
  try {
    const response = await syncOpportunities();
    if (response && response.code === 200) {
      console.log('同步成功');
      Modal.success({ title: '同步成功', content: '一键同步完成' });
      fetchTodoItems();
    } else {
      console.error('同步失败:', response.error);
      Modal.error({ title: '同步失败', content: '请稍后重试' });
    }
  } catch (error) {
    console.error('同步错误:', error);
    Modal.error({ title: '同步失败', content: '发生了错误，请稍后重试' });
  } finally {
    loading.value = false; // 加载结束
  }
};

</script>
  
<style scoped>
/* 页面容器 */
.todo-container {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  padding: 20px;
  border-radius: 12px;
}

/* 页面头部样式 */
header {
  text-align: center;
  margin-bottom: 30px;
}

header h1 {
  font-size: 2.2em;
  color: #333;
}

header p {
  color: #666;
  font-size: 1em;
}

.sync-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 15px;
}

.sync-buttons a-button {
  width: 160px;
}

.todo-list {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.todo-header {
  display: flex;
  justify-content: space-between;
  background: #f9f9f9;
  padding: 10px 15px;
  border-radius: 8px;
}

.header-item {
  font-weight: bold;
  color: #5c5c5c;
}

.todo-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-item {
  background: #ffffff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.stat-item:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.stat-title {
  font-weight: bold;
  color: #333;
  font-size: 1.2em;
}

.stat-number {
  font-size: 1.4em;
  color: #007bff;
  margin-top: 10px;
}

.item-details {
  margin-top: 10px;
  text-align: left;
}

.item-details ul {
  list-style-type: disc;
  padding-left: 20px;
}

.item-details a-button {
  margin-top: 10px;
}

footer {
  text-align: center;
  margin-top: 40px;
  font-size: 1em;
  color: #888;
}

.loading-spinner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
  