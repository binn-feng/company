<template>
  <div>
  <a-card title="报价待公司审批" :headStyle="{ 'height': '66px' }">
    <template #extra>
        <div style="display: flex; justify-content: flex-start; align-items: center; text-align: center;">
            <div style="display: flex; align-items: center;gap: 8px;">
                <a-input allowClear v-model:value="query" placeholder="请输入搜索内容" size="middle"
                    @pressEnter="handleSearch"></a-input>
                <a-button @click="handleSearch" type="primary" size="middle">搜索</a-button>
            </div>
        </div>
    </template>
    <a-table :columns="visibleColumns" :data-source="data" :pagination="pagination" row-key="key" :loading="loading"
      @change="handleTableChange">
      <template #bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'index'">
          <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'amount'">
          <span>
            {{ record.amount !== null && record.amount !== undefined ? formatNumber(record.amount.toFixed(2)) : '-' }}
          </span>
        </template>
        <template v-else-if="column.dataIndex === 'projExtQuoteTotal'">
          <span>
            {{ record.projExtQuoteTotal !== null && record.projExtQuoteTotal !== undefined ?
              formatNumber(record.projExtQuoteTotal.toFixed(2)) : '-' }}
          </span>
        </template>
        <span v-else-if="column.dataIndex === 'type'">
          <span v-if="record.type === 'ROUGH'">粗略报价</span>
          <span v-else-if="record.type === 'COST'">详细报价</span>
          <span v-else-if="record.type === 'INCAPABLE'">无法报价</span>
        </span>
        <span v-else-if="column.dataIndex === 'projProfitRateExcl'">
          {{ record.projProfitRateExcl ? (parseFloat(record.projProfitRateExcl) * 100).toFixed(2) + '%' : '-' }}
        </span>
        <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
          <a-button v-hasPermi="['quote:approval:details']" type="link" @click="handleDetail(record)" class="nomp">详情</a-button>
          <a-button v-hasPermi="['quote:approval:approve']" type="link" @click="generateQuote(record)" class="nomp">报价审批</a-button>
        </span>

      </template>
    </a-table>
  </a-card>
  <ApproveDetail :open="isModalVisible" :record="selectedRecord" @close="handleClose" @ok="handleOk"
    :isModalVisible="isModalVisible" :isAwaiting="isAwaiting"></ApproveDetail>
  <QuotationApproval :open="isQuotationApprovalVisible" @close="handleQuotationApprovalClose"
    :isQuotationPendingApproval="isQuotationPendingApproval" :componentTitle="componentTitle"
    :opportunity="selectedRecord" :isSummarySheet="isSummarySheet" />
  </div>
</template>  
    
<script setup>
import { ref, computed, } from 'vue';
import { onMounted } from 'vue'
import ApproveDetail from './components/ApproveDetail.vue';
import QuotationApproval from './components/QuotationApproval.vue'
import { fetchOpportunities } from '@/api/costqoute';
import { formatNumber } from '@/utils/format'

const isModalVisible = ref(false)
const selectedRecord = ref(null)
const isAwaiting = ref(false)
const loading = ref(false)
const columns = ref([
  { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
  { title: 'id', dataIndex: 'id', key: 'id', visible: false },
  { title: '商机名称', dataIndex: 'name', width: '10%', sorter: (a, b) => a.name.localeCompare(b.name) },
  { title: '客户名称', dataIndex: 'customersName', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
  { title: '所属销售', dataIndex: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
  { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category) },
  // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent' },
  { title: '报价版本', dataIndex: 'currentVersion', key: 'currentVersion' },
  { title: '所属售前', dataIndex: 'preSaleId', visible: false },
  { title: '所属售前', dataIndex: 'preSaleName', width: '10%', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
  // { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type) },
  { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => parseFloat(a.amount) - parseFloat(b.amount) },
  { title: '对外报价', dataIndex: 'projExtQuoteTotal', key: 'projExtQuoteTotal', sorter: (a, b) => parseFloat(a.projExtQuoteTotal) - parseFloat(b.projExtQuoteTotal) },
  { title: '整体利润率', dataIndex: 'projProfitRateExcl', key: 'projProfitRateExcl', },
  { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
  { title: '操作', dataIndex: 'actions', scopedSlots: { customRender: 'actions' }, width: '8%'},
]);
const visibleColumns = computed(() => {
  return columns.value.filter(column => column.visible !== false);
});
const data = ref([]);
const query = ref('')
// 数据加载  
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 1,
});
// 数据加载  
const loadOpportunities = async () => {
  loading.value = true
  const filters = {
    id: '',
    name: '',
    category: '',
    // typeList: ['COST', 'ROUGH', 'INCAPABLE'],
    parentStatusList: ['00001301'],
    statusList: ['3'],
    // approvalStatusList: ['1',]
    query: query.value.trim()
  };
  const params = {
    current: pagination.value.current,
    model: filters,
    order: 'descending',
    size: pagination.value.pageSize,
    sort: 'id',
  }
  try {
    const response = await fetchOpportunities('pageWithApproval',params);
    data.value = response.rows || []; // 确保响应结构正确  
    // 手动默认按升序排序, 避免高亮
    data.value = [...data.value].sort(
      (a, b) => new Date(a.updateTime) - new Date(b.updateTime)
    );
    pagination.value.total = response.total;
  } catch (error) {
    console.error('Error loading opportunities:', error);
  } finally {
    loading.value = false
  }
};
const handleTableChange = (paginationProps) => {
  if (paginationProps.current) {
    pagination.value.current = paginationProps.current;  // Update current page
    loadOpportunities(); // Reload data for the updated page
  }
}

const handleSearch = () => {
  loadOpportunities(); // 重新加载数据并应用搜索
};
onMounted(loadOpportunities);
// 报价审批
const isQuotationApprovalVisible = ref(false)

const handleClose = () => {
  isModalVisible.value = false
  isAwaiting.value = false
}
const handleDetail = (record) => {
  selectedRecord.value = record;
  isAwaiting.value = true
  isModalVisible.value = true;
};
// 判断从哪个组件进入通用组件的标识
const componentTitle = ref('')
const isQuotationPendingApproval = ref(false)
const isSummarySheet = ref(false)
const generateQuote = (record) => {
  console.log('报价审批:', record);
  selectedRecord.value = record;
  isSummarySheet.value = true
  isQuotationApprovalVisible.value = true
  isQuotationPendingApproval.value = true
  componentTitle.value = '报价审批'
};
// 报价审批关闭
const handleQuotationApprovalClose = () => {
  isSummarySheet.value = false
  isQuotationApprovalVisible.value = false
  isQuotationPendingApproval.value = false
  componentTitle.value = ''
  loadOpportunities();
}
</script>  
    
<style scoped> .table-container {
   width: 100%;
   /* height: 100vh;   */
   display: flex;
   flex-direction: column;
 }

 .ant-table {
   flex: 1;
   background-color: #f0f5ff;
   overflow: auto;
 }

 .ant-btn {
    margin-right: 10px;
    z-index: 999;
  }
</style>