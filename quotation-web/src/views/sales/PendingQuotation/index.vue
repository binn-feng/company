<template>
    <div>
    <a-card title="报价审批未通过" class="table-container" :headStyle="{ 'height': '66px' }">
        <template #extra>
            <div style="display: flex; justify-content: flex-start; align-items: center; text-align: center;">
                <div style="display: flex; align-items: center;gap: 8px;">
                    <a-input allowClear v-model:value="query" placeholder="请输入搜索内容" size="middle"
                        @pressEnter="handleSearch"></a-input>
                    <a-button @click="handleSearch" type="primary" size="middle">搜索</a-button>
                </div>
            </div>
        </template>
        <a-table :columns="visibleColumns" :data-source="data" :pagination="pagination" rowKey="key" :loading="loading"
            @change="handleTableChange">
            <template #bodyCell="{ column, record, index }">
                <template v-if="column.dataIndex === 'index'">
                    <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'amount'">
                    <span>
                        {{ record.amount !== null && record.amount !== undefined ? formatNumber(record.amount.toFixed(2)) :
                            '-' }}
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
                    <a-button v-hasPermi="['quote:rejected:details']" type="link" @click="handleDetail(record)" class="nomp">详情</a-button>
                    <a-button v-hasPermi="['quote:rejected:reapply']" type="link" @click="handleUnqoute('reapply', record)" class="nomp">重新申请报价</a-button>
                    <a-button v-hasPermi="['quote:rejected:handleLost']" type="link" @click="handleUnqoute('discard', record)" class="nomp">丢单处理</a-button>
                    <a-button v-hasPermi="['quote:rejected:update']" type="link" @click="updateSalesOffer(record)" class="nomp">更新销售报价</a-button>
                </span>
            </template>
        </a-table>
    </a-card>
    <GeneratorQuote v-model:open="generateQuoteVisible" :selectedData="selectedRecord" @close="generateQuoteVisible = false"
        @ok="generateQuoteVisible = false"></GeneratorQuote>
    <HandleUnable :open="isUnqouteModalVisible" :opportunity="selectedRecord" @close="handleCommonClose"
        :modalMode="modalMode" />
    <ApproveDetail :open="isModalVisible" :record="selectedRecord" @close="handleClose" @ok="handleOk"
        :isModalVisible="isModalVisible"></ApproveDetail>
    <SalesQuotation :open="isSaleQuoteModelVisible" :opportunity="selectedRecord" @close="handleSaleQuoteCancel"
        :isSales="isSales" @handleReapply="handleReapply" @handleLose="handleLose" :isUpdateSalesOffer="isUpdateSalesOffer">
    </SalesQuotation>
</div>
</template>  
  
<script setup>
import { ref, computed } from 'vue';
import { onMounted } from 'vue';
import { formatNumber } from '@/utils/format'
import GeneratorQuote from '../../sales/CompanyApproval/components/QuoteSheet.vue' //废弃
// import PendingDetail from './components/PendingDetail.vue'
import { fetchOpportunities } from '@/api/costqoute';
import ApproveDetail from '../../sales/CompanyApproval/components/ApproveDetail.vue'; //报价详情
import HandleUnable from '../../sales/SalesToQuotation/components/handleUnCost.vue' //丢单 重新申请报价
import SalesQuotation from '@/views/sales/SalesToQuotation/components/SalesQuotation.vue'; //销售报价
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', key: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', key: 'name', sorter: (a, b) => a.name.localeCompare(b.name) },
    { title: '客户名称', dataIndex: 'customersName', key: 'customerName', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
    { title: '所属销售', dataIndex: 'saleName', key: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
    { title: '产品类别', dataIndex: 'category', key: 'productType', sorter: (a, b) => a.category.localeCompare(b.category) },
    { title: '所属售前', dataIndex: 'preSaleId', visible: false },
    { title: '所属售前', dataIndex: 'preSaleName', width: '10%', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
    // { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type) },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent' },
    { title: '报价版本', dataIndex: 'currentVersion', key: 'currentVersion' },
    { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => parseFloat(a.amount) - parseFloat(b.amount) },
    { title: '对外报价', dataIndex: 'projExtQuoteTotal', key: 'projExtQuoteTotal', sorter: (a, b) => parseFloat(a.projExtQuoteTotal) - parseFloat(b.projExtQuoteTotal) },
    { title: '整体利润率', dataIndex: 'projProfitRateExcl', key: 'projProfitRateExcl', },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    { title: '操作', dataIndex: 'actions', scopedSlots: { customRender: 'actions' }, width: '8%' },
]);
const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});
const data = ref([]);
const isUnqouteModalVisible = ref(false)
const isModalVisible = ref(false) //报价详情
const generateQuoteVisible = ref(false)
const selectedRecord = ref(null); // 存储选中的记录 
const isSaleQuoteModelVisible = ref(false) //销售报价
const isSales = ref(false)
const query = ref('')
const loading = ref(false); // 加载状态 
const isUpdateSalesOffer = ref(false)
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
        parentStatusList: ['00001301'],
        statusList: ['8'],
        // approvalStatusList: ['3',]
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
        const response = await fetchOpportunities('pageWithApprovalFail',params);
        // const filteredOpportunities = response.rows.filter(opportunity =>
        //     ['COST', 'ROUGH',].includes(opportunity.type)
        // );
        data.value = response.rows || [];
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
        pagination.value.current = paginationProps.current;
        loadOpportunities();
    }
}

const handleSearch = () => {
    loadOpportunities(); // 重新加载数据并应用搜索
};
onMounted(loadOpportunities);

const handleClose = () => {
    isModalVisible.value = false
}
const handleOk = () => {
}
const handleDetail = (record) => {
    selectedRecord.value = record;
    isModalVisible.value = true;
};
const modalMode = ref('');
const handleUnqoute = (mode, record) => {
    modalMode.value = mode;
    selectedRecord.value = record;
    isUnqouteModalVisible.value = true
}
const handleCommonClose = () => {
    isUnqouteModalVisible.value = false
    loadOpportunities()
}
const updateSalesOffer = (record) => {
    selectedRecord.value = record;
    isSales.value = true
    isUpdateSalesOffer.value = true
    isSaleQuoteModelVisible.value = true
};
const handleSaleQuoteCancel = () => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    loadOpportunities()
}
const handleReapply = (param) => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    isUpdateSalesOffer.value = false
    handleUnqoute('reapply', param)
}
const handleLose = (param) => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    isUpdateSalesOffer.value = false
    handleUnqoute('discard', param)
}
</script>  
  
<style scoped> .table-container {
     width: 100%;
     display: flex;
     flex-direction: column;
 }

 .ant-table {
     flex: 1;
     background-color: #f0f5ff;
     overflow: auto;
 }

 /* .ant-btn {
     padding: 0;
     padding-right: 10px;
     z-index: 999;
 } */
 .table-container>>>.ant-btn {
  /* padding: 0; */
  /* padding-right: 10px; */
  /* margin-inline: 6px; */
  z-index: 999;
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