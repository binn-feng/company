<template>
    <div>
        <a-card title="废弃" :headStyle="{ 'height': '66px' }">
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
                        <span>{{ record.amount !== null && record.amount !== undefined ?
                            formatNumber(record.amount.toFixed(2))
                            : '-'
                        }}</span>
                    </template>
                    <template v-else-if="column.dataIndex === 'signQuoteTotal'">
                        <span>{{ record.signQuoteTotal !== null && record.signQuoteTotal !== undefined ?
                            formatNumber(record.signQuoteTotal.toFixed(2)) : '-' }}</span>
                    </template>
                    <span v-else-if="column.dataIndex === 'signProjProfitRateExcl'">
                        {{ record.signProjProfitRateExcl ? (parseFloat(record.signProjProfitRateExcl) * 100).toFixed(2) +
                            '%' : '-' }}
                    </span>
                    <template v-else-if="column.dataIndex === 'contractType'">
                        <span v-if="record.contractType === '1'">欣象代理</span>
                        <span v-if="record.contractType === '2'">北光直签</span>
                    </template>
                    <span v-else-if="column.dataIndex === 'type'">
                        <span v-if="record.type === 'ROUGH'">粗略报价</span>
                        <span v-else-if="record.type === 'COST'">详细报价</span>
                        <span v-else-if="record.type === 'INCAPABLE'">无法报价</span>
                    </span>
                    <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
                        <a-button type="link" @click="handleDetail(record)" class="nomp">详情</a-button>
                    </span>
                </template>
            </a-table>
        </a-card>
        <ApproveDetail :open="isModalVisible" :record="selectedRecord" @close="handleClose" @ok="handleOk"
            :isModalVisible="isModalVisible" :isWaitingSign="isWaitingSign" :isSignApprovalStatus="isSignApprovalStatus" :isAbandon="isAbandon">
        </ApproveDetail>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { fetchOpportunities, } from '@/api/costqoute';
import ApproveDetail from '../../sales/CompanyApproval/components/ApproveDetail.vue';
import { formatNumber } from '@/utils/format'

const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', width: '10%', sorter: (a, b) => a.name.localeCompare(b.name) },
    { title: '客户名称', dataIndex: 'customersName', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
    { title: '所属销售id', dataIndex: 'saleId', visible: false },
    { title: '所属销售', dataIndex: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
    { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category) },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent' },
    { title: '所属售前', dataIndex: 'preSaleId', visible: false },
    { title: '所属售前', dataIndex: 'preSaleName', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
    { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => parseFloat(a.amount) - parseFloat(b.amount) },
    { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type) },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    // { title: '预计开始时间时间', dataIndex: 'time', key: 'time' },
    { title: '操作', dataIndex: 'actions', key: 'actions', width: '8%' },
]);
const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});

const data = ref([]);
const loading = ref(true); // 加载状态
const selectedRecord = ref(null)
const isWaitingSign = ref(false) //是否显示签约申请标识
const isModalVisible = ref(false) //报价详情
const isSignApprovalStatus = ref(false)
const query = ref('');
const isAbandon = ref(false)

const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 1,
});
// 数据加载  
const loadOpportunities = async () => {
    loading.value = true;
    const filters = {
        id: '',
        name: '',
        category: '',
        statusList: ["11"],
        query: query.value.trim(),
    };
    const params = {
        current: pagination.value.current,
        model: filters,
        order: 'descending',
        size: pagination.value.pageSize,
        sort: 'id',
    }
    try {
        const response = await fetchOpportunities('pageWithSummary', params);
        data.value = response.rows || []; // 确保响应结构正确  
        // 手动默认按升序排序, 避免高亮
        data.value = [...data.value].sort(
            (a, b) => new Date(a.updateTime) - new Date(b.updateTime)
        );
        pagination.value.total = response.total;
    } catch (error) {
        console.error('Error loading opportunities:', error);
    } finally {
        loading.value = false;
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

const handleDetail = (record) => {
    selectedRecord.value = record;
    isWaitingSign.value = true
    isSignApprovalStatus.value = true
    isAbandon.value = true
    isModalVisible.value = true;
};
const handleClose = () => {
    isModalVisible.value = false
    isWaitingSign.value = false
    isSignApprovalStatus.value = false
    isAbandon.value = false
}
</script>

<style scoped>
.table-container {
    width: 100%;
    /* height: 100vh; 使用视口高度   */
    display: flex;
    /* 使用 flexbox 来排列 */
    flex-direction: column;
    /* 垂直排列 */
}

.ant-table {
    flex: 1;
    /* 使表格占据剩余的高度 */
    background-color: #f0f5ff;
    /* 表格背景色 */
    overflow: auto;
    /* 使表格可滚动 */
}

.ant-btn {
    margin-right: 10px;
    z-index: 999;
}
</style>