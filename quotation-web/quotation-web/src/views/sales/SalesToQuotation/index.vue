<template>
    <div>
    <a-card title="待销售报价" :headStyle="{ 'height': '66px' }">
        <template #extra>
            <div style="display: flex; justify-content: flex-start; align-items: center; text-align: center;">
                <div style="display: flex; align-items: center;gap: 8px;">
                    <a-input allowClear v-model:value="query" placeholder="请输入搜索内容" size="middle"
                        @pressEnter="handleSearch"></a-input>
                    <a-button @click="handleSearch" type="primary" size="middle">搜索</a-button>
                </div>
            </div>
        </template>
        <!-- <a-spin v-if="loading" /> -->
        <a-table :data-source="data" :columns="visibleColumns" row-key="key" style="width: 100%; height: 100%;" :loading="loading"
            :pagination="pagination" @change="handleTableChange">
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
                <template v-else-if="column.dataIndex === 'status'">
                    <span>{{ getStatusText(record.status) }}</span>
                </template>
                <span v-else-if="column.dataIndex === 'type'">
                    <span v-if="record.type === 'ROUGH'">粗略报价</span>
                    <span v-else-if="record.type === 'COST'">详细报价</span>
                    <span v-else-if="record.type === 'INCAPABLE'">无法报价</span>
                </span>
                <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
                    <a-button v-hasPermi="['quote:sale:details']" type="link" @click="viewDetails(record)" class="nomp">报价详情</a-button>
                    <a-button v-hasPermi="['quote:sale:create']" type="link" class="nomp"
                        @click="saleQuoteDetails(record)">销售报价</a-button>
                    <a-button v-hasPermi="['quote:sale:reapply']" type="link" class="nomp"
                        @click="handleUnqoute('reapply', record)">重新申请报价</a-button>
                    <a-button v-hasPermi="['quote:sale:handleLost']" type="link" class="nomp"
                        @click="handleUnqoute('discard', record)">丢单处理</a-button>
                </span>
                <span v-else>{{ record[column.dataIndex] }}</span>
            </template>
        </a-table>
    </a-card>
    <CostSummary :open="isCostVisible" :name="selectedRecord?.name" @closeFinal="handleClose" :finalVersion="finalVersion"
        :versionData="versionData" :opportunity="selectedRecord" :finalDetailData="finalDetailData"
        :isFinalVersionType="isFinalVersionType" :lastestRoughversionId="lastestRoughversionId"
        :lastestUnableversionId="lastestUnableversionId" @handle-sale="handleSale" @handle-reapply="handleSaleReapply"
        @handle-lose="handleSaleLose" />
    <RoughQuotation v-model:open="isRoughQuotationVisible" :opportunity="selectedRecord" @close="handleRoughCancel"
        @ok="isRoughQuotationVisible = false" :isRoughDetail="isRoughDetail" />
    <HandleUnable :open="isUnqouteModalVisible" :opportunity="selectedRecord" @close="handleCommonClose"
        :modalMode="modalMode" />
    <SalesQuotation :open="isSaleQuoteModelVisible" :opportunity="selectedRecord" @close="handleSaleQuoteCancel"
        :isSales="isSales" @handleReapply="handleReapply" @handleLose="handleLose">
    </SalesQuotation>
    <UnableQuote v-model:open="isUnableQuotationVisible" :opportunity="selectedRecord" @sales-close="handleUnableClose"
        :isSaleUnableQoute="isSaleUnableQoute" />
    </div>
</template>  

<script setup>
import { ref, onMounted, computed } from 'vue';
import { fetchOpportunities, getVersion, fetchCostQuoteDetail } from '@/api/costqoute';
import { formatNumber } from '@/utils/format'
import CostSummary from '../../components/costdetailquotation/components/costsummary.vue'; // 成本报价详情
import RoughQuotation from '../../components/RoughQuotation.vue'; //粗略报价
import SalesQuotation from './components/SalesQuotation.vue'; //销售报价
import UnableQuote from '../../components/UnableQuote.vue'; // 无法报价
import HandleUnable from './components/handleUnCost.vue' //丢单 重新申请报价
import { statusMap } from '@/utils/common'

const query = ref('')
const isSales = ref(false)
const isUnableQuotationVisible = ref(false) //粗略报价
const isSaleUnableQoute = ref(false)
const isCostVisible = ref(false)
const isUnqouteModalVisible = ref(false)
const selectedRecord = ref(null)
const isRoughQuotationVisible = ref(false); // 控制粗略详情是否可见
const isSaleQuoteModelVisible = ref(false) //销售报价
const data = ref([
]);
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', key: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', width: '10%', sorter: (a, b) => a.name.localeCompare(b.name) },
    { title: '客户名称', dataIndex: 'customersName',  sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
    { title: '所属销售', dataIndex: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
    { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category) },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent', width: '7%' },
    { title: '所属售前', dataIndex: 'preSaleId', visible: false },
    { title: '所属售前', dataIndex: 'preSaleName', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
    { title: '报价版本', dataIndex: 'currentVersion', key: 'currentVersion' },
    { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => parseFloat(a.amount) - parseFloat(b.amount) },
    // { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type), width: '7%' },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    { title: '附件', dataIndex: 'appendix', key: 'appendix', visible: false },
    // { title: '报价类别', dataIndex: 'quoteType', key: 'quoteType' },
    // { title: '报价人', dataIndex: 'offeror', key: 'offeror', sorter: (a, b) => a.offeror.localeCompare(b.offeror) },
    { title: '操作', dataIndex: 'actions', scopedSlots: { customRender: 'actions' }, width: '8%' },
]);
const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});


const getStatusText = (status) => {
    return statusMap[status] || '未知状态';
};
// 数据加载  
const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 1,
});
const loading = ref(true);
// 数据加载  
const loadOpportunities = async () => {
    loading.value = true;
    const filters = {
        id: '',
        name: '',
        category: '',
        // typeList: ['COST', 'ROUGH', 'INCAPABLE'],
        statusList: ['2'],
        parentStatusList: ['00001301'],
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
        const response = await fetchOpportunities('pageWithSales', params);
        // const filteredOpportunities = response.rows.filter(opportunity =>
        //     ['COST', 'ROUGH',].includes(opportunity.type)
        // );
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



// 获取最新版本id----粗略时候调用
const lastestRoughversionId = ref('')
const lastestUnableversionId = ref('')
const getLastestVersionFuncs = async (record) => {
    try {
        const lastestversionParams = {
            latest: true,
            opportunitiesId: record.id
        }
        // 调用 getVersion 接口
        const response = await getVersion(lastestversionParams);
        if (response && response.code === 200) {
            if (record.type === 'ROUGH') {
                lastestRoughversionId.value = response.data[response.data.length - 1].id;
            } else if (record.type === 'INCAPABLE') {
                lastestUnableversionId.value = response.data[response.data.length - 1].id;
            }
            console.log('版本数据:', response.data[response.data.length - 1].id, lastestRoughversionId.value);
        } else {
            console.error('获取版本信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    }
};
// 获取详细报价最新版本id和最新版本对应数据
const lastestversionId = ref(null)
const getVersionFuncs = async (params) => {
    try {
        // 调用 getVersion 接口
        const response = await getVersion(params);
        if (response && response.code === 200) {
            lastestversionId.value = response.data[0].id;
            console.log('版本数据:', response.data.id);
            // 成本详细报价--获取最新版本数据
            getCostQuoteDetail({ id: lastestversionId.value })
        } else {
            console.error('获取版本信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    }
};
const finalDetailData = ref() //存储指定版本的详细数据
const getCostQuoteDetail = async (id) => {
    try {
        const response = await fetchCostQuoteDetail(id);
        if (response && response.code === 200) {
            const quoteDetails = response.data;
            // 根据获取的数据填充你的表格或者其他内容
            finalDetailData.value = quoteDetails
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};

const isFinalVersionType = ref(false)
const finalVersion = ref(false)
function viewDetails(record) {
    selectedRecord.value = record;
    finalVersion.value = true
    // 粗略报价
    if (record.type === 'ROUGH') {
        isFinalVersionType.value = true
        // 获取最新版本(最终版本)id

        getLastestVersionFuncs(record)
        isCostVisible.value = true

    } else if (record.type === 'COST') {
        isFinalVersionType.value = false
        // 获取最新版本(最终版本)
        const lastestversionParams = {
            latest: true,
            opportunitiesId: record.id,
        }
        getVersionFuncs(lastestversionParams)
        isCostVisible.value = true
    } else if (record.type === 'INCAPABLE') {
        // isFinalVersionType.value = false

        // isUnableQuotationVisible.value = true
        // 获取最新版本(最终版本)id
        getLastestVersionFuncs(record)
        isCostVisible.value = true
        isSaleUnableQoute.value = true
    }
}
function saleQuoteDetails(record) {
    selectedRecord.value = record;
    isSales.value = true
    isSaleQuoteModelVisible.value = true
}
const modalMode = ref('');
const handleUnqoute = (mode, record) => {
    modalMode.value = mode;
    selectedRecord.value = record;
    isUnqouteModalVisible.value = true
}
function handleClose() {
    isCostVisible.value = false;
    finalVersion.value = false
    lastestRoughversionId.value = ''
    lastestUnableversionId.value = ''
    isFinalVersionType.value = false
    loadOpportunities()
}
const handleSaleQuoteCancel = () => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    loadOpportunities()
}
const handleReapply = (param) => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    handleUnqoute('reapply', param)
}
const handleLose = (param) => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    handleUnqoute('discard', param)
}
const handleUnableClose = () => {
    isUnableQuotationVisible.value = false
    isSaleUnableQoute.value = false
    loadOpportunities()
}
const handleCommonClose = () => {
    modalMode.value = ''
    isUnqouteModalVisible.value = false
    loadOpportunities()
}
const handleSale = (param) => {
    isCostVisible.value = false
    saleQuoteDetails(param)
}
const handleSaleReapply = (param) => {
    isCostVisible.value = false
    handleUnqoute('reapply', param)
}
const handleSaleLose = (param) => {
    isCostVisible.value = false
    handleUnqoute('discard', param)
}
</script>
<style scoped>  .table-container {
      width: 100%;

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
  }</style>