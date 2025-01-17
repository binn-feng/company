<template>
    <div>
    <a-card title="已成本报价" :headStyle="{ 'height': '66px' }">
        <!-- <a-spin v-if="loading" /> -->
        <template #extra>
            <div>
                <div style="display: flex; align-items: center;gap: 10px;">
                    <a-input v-model:value="query" placeholder="请输入搜索内容" size="middle" @pressEnter="handleSearch"
                        allowClear></a-input>
                    <a-button @click="handleSearch" type="primary" size="middle">搜索
                    </a-button>
                </div>
            </div>
        </template>
        <a-table :columns="visibleColumns" :data-source="data" row-key="id" :pagination="pagination" :loading="loading"
            @change="handleTableChange">
            <template #bodyCell="{ column, record, index }">
                <template v-if="column.dataIndex === 'index'">
                    <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'amount'">
                    <span>{{ record.amount !== null && record.amount !== undefined ?
                        formatNumber(Number(record.amount).toFixed(2)) : '-'
                        }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'status'">
                    <span>{{ getStatusText(record.status) }}</span>
                </template>
                <span v-else-if="column.dataIndex === 'type'">
                    <span v-if="record.type === 'ROUGH'">粗略报价</span>
                    <span v-else-if="record.type === 'COST'">详细报价</span>
                    <span v-else-if="record.type === 'INCAPABLE'">无法报价</span>
                </span>
                <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
                    <a-button v-hasPermi="['quote:finish:detail']" class="nomp" type="link"
                        @click="viewDetails(record)">商机详情</a-button>
                    <a-button v-hasPermi="['quote:finish:quote:detail']" class="nomp" type="link"
                        @click="quoteDetails(record)">报价详情</a-button>
                    <a-button v-hasPermi="['quote:finish:update']" type="link" class="nomp"
                        @click="updateDetails(record)"
                        v-if="(record.status == 2 || record.status == 3) && record.parentStatus == '00001301'">更新报价</a-button>
                </span>
                <span v-else>{{ record[column.dataIndex] }}</span>
            </template>
        </a-table>
    </a-card>
    <OpportunityModal v-model:open="isModalVisible" :opportunity="selectedRecord" @close="isModalVisible = false"
        @ok="isModalVisible = false" />
    <RoughQuotation v-model:open="isRoughQuotationVisible" :opportunity="selectedRecord" @close="handleRoughCancel"
        @ok="isRoughQuotationVisible = false" :isRoughDetail="isRoughDetail" :costDetail="costDetail"
        :versionData="versionData" :isRoughQuotationVisible="isRoughQuotationVisible"/>
    <UpdateQuoteModal v-model:open="isUpdateModalVisible" :opportunity="selectedRecord" @close="handleUpdateCancel"
        :versionData="versionData" />
    <CostSummary :open="isCostVisible" :name="selectedRecord?.name" @closedetail="handleClose" :costDetail="costDetail"
        :versionData="versionData" :opportunity="selectedRecord" :isRough="isRough"
        @close-update="(flag) => updateDetails(selectedRecord, flag)" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, } from 'vue';
import OpportunityModal from '../../components/OpportunityDetail.vue';
import UpdateQuoteModal from '../../components/UpdateQuoteModal.vue';
import RoughQuotation from '../../components/RoughQuotation.vue';
import CostSummary from '../../components/costdetailquotation/components/costsummary.vue';
import { formatNumber } from '@/utils/format'
import { fetchOpportunities, fetchCostQuoteDetail, getVersion } from '@/api/costqoute';
import { statusMap } from '@/utils/common'
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', key: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', width: '10%', sorter: (a, b) => a.name.localeCompare(b.name) },
    { title: '客户名称', dataIndex: 'customersName', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
    { title: '所属销售', dataIndex: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
    { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category) },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent', width: '8%', },
    { title: '所属售前', dataIndex: 'preSaleId', visible: false },
    { title: '所属售前', dataIndex: 'preSaleName', width: '9%', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
    { title: '附件', dataIndex: 'appendix', key: 'appendix', visible: false },
    { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => a.amount - b.amount},
    // { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type) },
    { title: '报价版本', dataIndex: 'currentVersion', width: '8%', key: 'currentVersion', },
    { title: '状态', dataIndex: 'status', key: 'status', sorter: (a, b) => a.status.localeCompare(b.status)},
    { title: '报价说明', dataIndex: 'quoteDesc', key: 'quoteDesc', visible: false, },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    { title: '操作', dataIndex: 'actions', width: '8%', scopedSlots: { customRender: 'actions' }, },
])
const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});
const data = ref([]);
const isModalVisible = ref(false); // 控制商机详情是否可见  
const isRoughQuotationVisible = ref(false); // 控制粗略详情是否可见
let isUpdateModalVisible = ref(false); // 控制更新报价模态框是否可见
const selectedRecord = ref(null); // 存储选中的记录  
const isCostVisible = ref(false)
const loading = ref(true); // 加载状态
const query = ref('');

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
        typeList: ['COST', 'ROUGH'],
        statusList: ['2', '3', '4', '5', '6', '7', '8', '9', '10', '12'],
        // parentStatusList: ['00001301',],
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
        const response = await fetchOpportunities('pageWithCosted', params);
        data.value = response.rows || []; // 确保响应结构正确  
        // 手动默认按升序排序, 避免高亮
        data.value = [...data.value].sort(
            (a, b) => new Date(a.updateTime) - new Date(b.updateTime)
        );
        // 获取最终版本号码
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

// 根据状态数字返回对应的中文描述
const getStatusText = (status) => {
    return statusMap[status] || '未知状态'; // 如果状态未定义，返回'未知状态'
};

const handleSearch = () => {
    loadOpportunities(); // 重新加载数据并应用搜索
};
function viewDetails(record) {
    selectedRecord.value = record; // 设置选中的记录  
    isModalVisible.value = true; // 显示模态框  
}

const updateDetails = (record, fromChild = false) => {
    selectedRecord.value = record;
    if (fromChild) {
        isCostVisible.value = false
    }
    // 获取所有版本
    const versionParams = {
        latest: false,
        opportunitiesId: record.id,
    }
    getVersionFunc(versionParams)
    isUpdateModalVisible.value = true;
};

// 定义 getVersionFunc 函数  获取所有版本
let versionData = ref() //所有版本号
const getVersionFunc = async (params) => {
    try {
        // 调用 getVersion 接口
        const response = await getVersion(params);
        if (response && response.code === 200) {
            versionData.value = response.data;
        } else {
            console.error('获取版本信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    }
};

// 调用 fetchCostQuoteDetail 获取报价详情数据  查看版本时候调用  或者暂存
const storageData = ref(null)
const getCostQuoteDetail = async (id) => {
    try {
        const response = await fetchCostQuoteDetail(id);
        if (response && response.code === 200) {
            const quoteDetails = response.data;
            // 根据获取的数据填充你的表格或者其他内容
            storageData.value = quoteDetails
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};

// 获取最新版本id=--------------------------
const lastestversionId = ref(null)
const getVersionFuncs = async (params) => {
    try {
        // 调用 getVersion 接口
        const response = await getVersion(params);
        if (response && response.code === 200) {
            lastestversionId.value = response.data.id;
            console.log('版本数据:', response.data.id);
        } else {
            console.error('获取版本信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    }
};

const isRoughDetail = ref(false)
const costDetail = ref(false)
const quoteDetails = (record) => {
    // 获取所有版本
    const versionParams = {
        latest: false,
        opportunitiesId: record.id,
    }
    getVersionFunc(versionParams)
    selectedRecord.value = record;
    console.log('查看报价详情', record, selectedRecord.value);
    costDetail.value = true
    // 粗略报价
    const isRough = ref('')
    if (record.type === 'ROUGH') {
        isRough.value = 'yes'
        isCostVisible.value = true
    } else {
        // 获取最新版本
        const lastestversionParams = {
            latest: true,
            opportunitiesId: record.id,
        }
        getVersionFuncs(lastestversionParams)
        // 成本详细报价
        const params = {
            status: 0,
            opportunitiesId: record.id, // 将字符串转换为整数
        }
        getCostQuoteDetail(params)
        isCostVisible.value = true
        isRough.value = 'no'
    }
};
const handleClose = () => {
    isCostVisible.value = false
    costDetail.value = false
    loadOpportunities()
}
const handleRoughCancel = () => {
    isRoughQuotationVisible.value = false; // 控制粗略详情是否可见
}
const handleUpdateCancel = () => {
    isUpdateModalVisible.value = false
    loadOpportunities()
}

onMounted(loadOpportunities);


</script>

<style scoped lang="less">
 .ant-layout-content {
     overflow: auto;
 }

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
