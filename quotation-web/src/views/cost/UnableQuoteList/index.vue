<template>
    <div>
    <a-card title="无法报价" :headStyle="{ 'height': '66px' }">
        <template #extra>
            <div style="display: flex; justify-content: flex-start; align-items: center; height: 100%;">
                <div style="display: flex; align-items: center;gap: 10px;">
                    <a-input v-model:value="query" size="middle" placeholder="请输入搜索内容" allowClear
                        @pressEnter="handleSearch">
                    </a-input>
                    <a-button @click="handleSearch" type="primary" size="middle">搜索
                    </a-button>
                </div>
            </div>
        </template>
        <!-- <a-spin v-if="loading" /> -->
        <a-table :pagination="pagination" @change="handleTableChange" :data-source="data" :columns="visibleColumns" :loading="loading"
            row-key="key" style="width: 100%; height: 100%;">
            <template #bodyCell="{ column, record, index }">
                <template v-if="column.dataIndex === 'index'">
                    <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'status'">
                    <span>{{ getStatusText(record.status) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'quoteDesc'">
                    <div class="quote-desc">
                        {{ record.quoteDesc || '' }}
                    </div>
                </template>
                <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
                    <a-button v-hasPermi="['quote:cannot:detail']" class="nomp" type="link"
                        @click="viewDetails(record)">商机详情</a-button>
                    <a-button v-hasPermi="['quote:cannot:quote:detail']" class="nomp" type="link"
                        @click="quoteDetails(record)">报价详情</a-button>
                    <a-button v-hasPermi="['quote:cannot:update']" type="link" class="nomp"
                        @click="updateDetails(record)"
                        v-if="(record.status == 2 || record.status == 3) && record.parentStatus == '00001301'">更新报价</a-button>
                </span>
                <span v-else>{{ record[column.dataIndex] }}</span>
            </template>
        </a-table>
    </a-card>

    <OpportunityModal v-model:open="isModalVisible" :opportunity="selectedRecord" @close="isModalVisible = false"
        @ok="isModalVisible = false" />
    <UpdateQuoteModal v-model:open="isUpdateModalVisible" :opportunity="selectedRecord" @close="handleUpdateClose"
        :name="selectedRecord?.name" :versionData="versionData" :unable="unable" />
    <CostSummary :open="isCostVisible" :name="selectedRecord?.name" @closedetail="handleClose" :costDetail="costDetail"
        :versionData="versionData" :opportunity="selectedRecord" :isRough="isRough" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import OpportunityModal from '../../components/OpportunityDetail.vue';
import UpdateQuoteModal from '../../components/UpdateQuoteModal.vue';
import CostSummary from '../../components/costdetailquotation/components/costsummary.vue';
import { fetchOpportunities, getVersion } from '@/api/costqoute';
import { statusMap } from '@/utils/common'

const loading = ref(true); // 加载状态

// 数据和列定义  
const data = ref([]);
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', sorter: (a, b) => a.name.localeCompare(b.name), width: '15%', },
    { title: '客户名称', dataIndex: 'customersName', sorter: (a, b) => a.customersName.localeCompare(b.customersName), width: '10%', },
    { title: '所属销售', dataIndex: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName), width: '10%', },
    { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category), width: '10%', },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent', width: '12%', },
    // { title: '附件', dataIndex: 'appendix', width: '15%' },
    { title: '无法报价原因', dataIndex: 'quoteDesc'},
    { title: '报价版本', dataIndex: 'currentVersion', width: '8%', key: 'currentVersion', },
    { title: '状态', dataIndex: 'status', key: 'status', width: '8%', },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    { title: '操作', dataIndex: 'actions', width: '8%' },
]);

const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});

// 模态框控制  
const isModalVisible = ref(false); // 控制商机详情是否可见  
const isUpdateModalVisible = ref(false); // 控制更新报价模态框是否可见  
const selectedRecord = ref(null); // 存储选中的记录  
const isCostVisible = ref(false)
const query = ref('')


// 根据状态数字返回对应的中文描述
const getStatusText = (status) => {
    return statusMap[status] || '未知状态'; // 如果状态未定义，返回'未知状态'
};
// 分页信息  
const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 0,
});

// 加载数据  
const loadOpportunities = async () => {
    loading.value = true; // 开始加载 
    const filters = {
        name: '',
        category: '',
        typeList: ['INCAPABLE'], // 查询类型为无法报价  
        statusList: ['2', '3', '4', '5', '6', '7', '8', '9', '10', '12'],
        // parentStatusList: ['00001301'],
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
        const response = await fetchOpportunities('pageWithIncapable', params);

        // 更新数据  
        data.value = response.rows || []; // 确保响应结构正确  
        // 手动默认按升序排序, 避免高亮
        data.value = [...data.value].sort(
            (a, b) => new Date(a.updateTime) - new Date(b.updateTime)
        );
        pagination.value.total = response.total;

    } catch (error) {
        console.error('Error loading opportunities:', error);
    } finally {
        loading.value = false; // 无论成功与否都结束加载  
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
// 各种视图处理函数  
function viewDetails(record) {
    selectedRecord.value = record; // 设置选中的记录  
    isModalVisible.value = true; // 显示模态框  
}
const unable = ref(false)
function updateDetails(record) {
    selectedRecord.value = record;

    // 获取所有版本
    const versionParams = {
        latest: false,
        opportunitiesId: record.id,
    }
    getVersionFunc(versionParams)
    unable.value = true
    isUpdateModalVisible.value = true;
}
// 
const costDetail = ref(false)
const quoteDetails = (record) => {
    // 获取所有版本
    const versionParams = {
        latest: false,
        opportunitiesId: record.id,
    }
    getVersionFunc(versionParams)
    selectedRecord.value = record;
    costDetail.value = true
    isCostVisible.value = true
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

// 调用加载函数  
onMounted(loadOpportunities);

const handleUpdateClose = () => {
    isUpdateModalVisible.value = false;
    unable.value = false
    loadOpportunities()
}
const handleClose = () => {
    isCostVisible.value = false
    costDetail.value = false
    loadOpportunities()
}
</script>
<style scoped>
 .table-container {
     width: 100%;
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

 .quote-desc {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
 }
</style>