<template>
    <div>
    <a-card title="报价单" :headStyle="{ 'height': '66px' }">
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
                    <span>{{ record.amount !== null && record.amount !== undefined ? formatNumber(record.amount.toFixed(2))
                        : '-'
                    }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'projExtQuoteTotal'">
                    <span>{{ record.projExtQuoteTotal !== null && record.projExtQuoteTotal !== undefined ?
                        formatNumber(record.projExtQuoteTotal.toFixed(2)) : '-' }}</span>
                </template>
                <span v-else-if="column.dataIndex === 'type'">
                    <span v-if="record.type === 'ROUGH'">粗略报价</span>
                    <span v-else-if="record.type === 'COST'">详细报价</span>
                    <span v-else-if="record.type === 'INCAPABLE'">无法报价</span>
                </span>
                <span v-else-if="column.dataIndex === 'projProfitRateExcl'">
                    {{ record.projProfitRateExcl ? (parseFloat(record.projProfitRateExcl) * 100).toFixed(2) + '%' : '-' }}
                </span>
                <span v-else-if="column.dataIndex === 'quotation'" style="text-align: left;">
                    <a-button type="link" @click="handleDetail(record)">北光报价单</a-button>
                    <a-button type="link" @click="generateQuote(record)">欣象报价单</a-button>
                </span>
                <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
                    <a-button v-hasPermi="['quote:order:details']" type="link" @click="handleDetail(record)" class="nomp">报价详情</a-button>
                    <a-button v-hasPermi="['quote:order:download']" type="link" @click="handleDownload(record)" class="nomp">下载报价单</a-button>
                    <a-button v-hasPermi="['quote:order:reapply']" type="link" @click="handleUnqoute('reapply', record)" class="nomp">重新申请报价</a-button>
                    <a-button v-hasPermi="['quote:order:handleLost']" type="link" @click="handleUnqoute('discard', record)" class="nomp">丢单处理</a-button>
                    <a-button v-hasPermi="['quote:order:update']" type="link" @click="updateSalesOffer(record)" class="nomp">更新销售报价</a-button>
                    <a-button v-hasPermi="['quote:order:contractRequest']" type="link" @click="handleContract(record)" class="nomp">签约申请</a-button>
                </span>

            </template>
        </a-table>
    </a-card>
    <a-modal v-model:open="signingDialogVisible" :title="dialogMode === 'updateOrProceed' ? '成本报价更新确认' : '需更新成本报价'">
        <template v-if="dialogMode ===  'updateRequired'">
            <p>当前暂无成本报价，需售前团队先行更新成本评估。 </p>
        </template>
        <template v-else-if="dialogMode === 'updateOrProceed'">
            <p>请再次确认，是否需要更新成本报价。</p>
        </template>
        <template #footer>
            <div class="modal-btn">
                <a-button @click="handleCancelUpdate" v-if="dialogMode === 'updateRequired'">取消</a-button>
                <a-button v-if="dialogMode === 'updateOrProceed'" @click="handleContractConfrim()">否</a-button>
                <a-button type="primary" @click="handleUpdateCost" v-if="dialogMode === 'updateOrProceed'">是</a-button>
                <a-button type="primary" @click="handleUpdateCost" v-if="dialogMode === 'updateRequired'">更新成本报价</a-button>
            </div>
        </template>
    </a-modal>

    <GeneratorQuote v-model:open="generateQuoteVisible" :selectedData="selectedRecord" @close="generateQuoteVisible = false"
        @ok="generateQuoteVisible = false"></GeneratorQuote>
    <QuotationApproval :open="isQuotationApprovalVisible" @price-sheet-close="handleQuotationApprovalClose"
        :isPriceSheet="isPriceSheet" :componentTitle="componentTitle" :opportunity="selectedRecord"
        :isSummarySheet="isSummarySheet" />
    <SalesQuotation :open="isSaleQuoteModelVisible" :opportunity="selectedRecord" @close="handleSaleQuoteCancel"
        :isSales="isSales" @handleReapply="handleReapply" @handleLose="handleLose" :isUpdateSalesOffer="isUpdateSalesOffer">
    </SalesQuotation>
    <HandleUnable :open="isUnqouteModalVisible" :opportunity="selectedRecord" @close="handleCommonClose"
        :modalMode="modalMode" />
    <ApproveDetail :open="isModalVisible" :record="selectedRecord" @close="handleClose" @ok="handleOk"
        :isModalVisible="isModalVisible"></ApproveDetail>
    <UpdateCost :opportunity="selectedRecord" :isUpdateCostModalVisible="isUpdateCostModalVisible"
        @close="handleUpdateCostClose"></UpdateCost>
    <QuotationModel :open="isDownloadQuoteModelVisible" :isDownloadQuoteModelVisible="isDownloadQuoteModelVisible" :opportunity="selectedRecord" @close="handleCloceQuoteModel"></QuotationModel>
    </div>
</template>

<script setup> 
import { ref, computed } from 'vue';
import { onMounted } from 'vue';
import { fetchOpportunities } from '@/api/costqoute';
import { formatNumber } from '@/utils/format'
import { existCostInfo } from '@/api/saleQoute';
import ApproveDetail from '../../sales/CompanyApproval/components/ApproveDetail.vue';
import GeneratorQuote from '../../sales/CompanyApproval/components/QuoteSheet.vue'
import QuotationApproval from '../../sales/CompanyApproval/components/QuotationApproval.vue'
import SalesQuotation from '@/views/sales/SalesToQuotation/components/SalesQuotation.vue'; //销售报价
import HandleUnable from '../../sales/SalesToQuotation/components/handleUnCost.vue'
import UpdateCost from './components/UpdateCostModal.vue'
import QuotationModel from './components/QuotationModel.vue';
const isPriceSheet = ref(false)
const isQuotationApprovalVisible = ref(false)
const isSaleQuoteModelVisible = ref(false) //销售报价
const isSales = ref(false)
const isUnqouteModalVisible = ref(false)
const isModalVisible = ref(false) //报价详情
const query = ref('')
const signingDialogVisible = ref(false); // 控制签约前弹窗
const dialogMode = ref(''); // 区分弹窗内容
const isUpdateCostModalVisible = ref(false)
const isDownloadQuoteModelVisible = ref(false)
const isUpdateSalesOffer = ref(false)
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', key: 'id', visible: false },
    { title: '商机名称', dataIndex: 'name', key: 'name', sorter: (a, b) => a.name.localeCompare(b.name) },
    { title: '客户名称', dataIndex: 'customersName', key: 'customerName', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
    { title: '所属销售', dataIndex: 'saleName', key: 'saleName', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
    { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category) },
    // { title: '报价内容', dataIndex: 'quoteContent', key: 'quoteContent' },
    { title: '报价版本', dataIndex: 'currentVersion', key: 'currentVersion' },
    { title: '所属售前', dataIndex: 'preSaleId', visible: false },
    { title: '所属售前', dataIndex: 'preSaleName', width: '10%', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
    // { title: '报价类别', dataIndex: 'type', key: 'type', sorter: (a, b) => a.type.localeCompare(b.type) },
    { title: '成本报价', dataIndex: 'amount', key: 'amount', sorter: (a, b) => parseFloat(a.amount) - parseFloat(b.amount) },
    { title: '对外报价', dataIndex: 'projExtQuoteTotal', key: 'projExtQuoteTotal', sorter: (a, b) => parseFloat(a.projExtQuoteTotal) - parseFloat(b.projExtQuoteTotal) },
    { title: '整体利润率(不含外采)', dataIndex: 'projProfitRateExcl', key: 'projProfitRateExcl', width: '9%' },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime', width: '10%', sorter: (a, b) => new Date(a.updateTime) - new Date(b.updateTime)},
    { title: '操作', dataIndex: 'actions', scopedSlots: { customRender: 'actions' }, width: '8%' },
]);
const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});
const data = ref([]);
const loading = ref(false)

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
        statusList: ['4'],
        // approvalStatusList: ['2', '0']
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
        const response = await fetchOpportunities('pageWithQuote', params);
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
}
onMounted(loadOpportunities);
const componentTitle = ref('')
// const isContractModalVisible = ref(false);
const generateQuoteVisible = ref(false)
const modalMode = ref('');
const selectedRecord = ref(null);
const handleDetail = (record) => {
    selectedRecord.value = record;
    isModalVisible.value = true;
};
// 下载报价单
const handleDownload = (record) => {
    selectedRecord.value = record;
    isDownloadQuoteModelVisible.value = true
};
const handleCloceQuoteModel= () => {
    isDownloadQuoteModelVisible.value = false
};
const updateSalesOffer = (record) => {
    selectedRecord.value = record;
    isSales.value = true
    isUpdateSalesOffer.value = true
    isSaleQuoteModelVisible.value = true
};
const isSummarySheet = ref(false)
// 签约申请逻辑调整
const handleContract = async (record) => {
    selectedRecord.value = record;
    try {
        const updateRequired = await existCostInfo({ opportunitiesId: record.id });
        if (updateRequired.data) {
            // 显示“是否更新成本报价”弹窗  
            dialogMode.value = 'updateOrProceed';

        } else {
            // 显示“请更新成本报价”弹窗  
            dialogMode.value = 'updateRequired';

        }
        signingDialogVisible.value = true;
    } catch (error) {
        console.error('API调用失败:', error);
    }
};
const handleUpdateCost = () => {
    signingDialogVisible.value = false
    isUpdateCostModalVisible.value = true
}
const handleCancelUpdate = () => {
    signingDialogVisible.value = false
}
const handleUpdateCostClose = () => {
    isUpdateCostModalVisible.value = false
    loadOpportunities();
}
const handleContractConfrim = () => {
    signingDialogVisible.value = false
    signingDialogVisible.value = false
    isSummarySheet.value = true
    isPriceSheet.value = true
    isQuotationApprovalVisible.value = true
    componentTitle.value = '签约申请'
};
// 报价审批关闭
const handleQuotationApprovalClose = () => {
    isSummarySheet.value = false
    isQuotationApprovalVisible.value = false
    isPriceSheet.value = false
    componentTitle.value = ''
    loadOpportunities();

}
const handleSaleQuoteCancel = () => {
    isSaleQuoteModelVisible.value = false;
    isSales.value = false
    isUpdateSalesOffer.value = false
    loadOpportunities()
}
const handleUnqoute = (mode, record) => {
    modalMode.value = mode;
    selectedRecord.value = record;
    isUnqouteModalVisible.value = true
}
// 关闭
const handleCommonClose = () => {
    isUnqouteModalVisible.value = false
    loadOpportunities()
}
const handleClose = () => {
    isModalVisible.value = false
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

 .ant-btn {
     margin-right: 10px;
     z-index: 999;
 }
</style>