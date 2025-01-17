<template>
    <a-modal :title="(props.qouteToCost || props.updateQuote) ? '成本报价' : getQuoteDetailTitle(props.opportunity?.type)"
        :open="isCostVisible" @cancel="handleCancel" width="60%" @ok="handleOk" :mask-closable="true">
        <div class="ant-layout-content">
            <a-card>
                <template #title>
                    <span class="fs16">{{ props.name }} - 成本预算表</span>
                </template>
                <div class="version-history" v-if="props.costDetail?.toString() === 'true'">
                    <span style="font-weight: 700;font-size: 16px;">成本报价版本：</span>
                    <!-- <div style="margin-bottom:16px">
                        <a-button type="primary">设为最终版本</a-button>
                    </div> -->
                    <div>
                        <div v-if="evaluationVersions.length === 0">
                            <span>无历史版本</span>
                        </div>
                        <a-radio-group v-model:value="selectedVersion" class="version-list">
                            <div class="version-item" v-for="(version, index) in evaluationVersions" :key="version.id"
                                @mouseover="isHovered = index" @mouseleave="isHovered = null">
                                <a-radio :value="version.id">
                                    <span style="padding-inline: 1px;margin-right: 20px;">{{ version.valuationVersion
                                    }}</span>

                                    <span style="padding-inline: 1px;margin-right: 30px;">{{ version.versionType ===
                                        "INCAPABLE" ? ' - ' : '￥' + formatNumber(version.amount) }}</span>

                                    <span style="padding-inline: 1px;margin-right: 30px;">{{ version.date }}</span>

                                    <span style="padding-inline: 1px;">{{ version.preSaleName }}</span>
                                </a-radio>
                            </div>
                        </a-radio-group>
                    </div>
                </div>
                <div class="detail"
                    v-if="shouldShowDetail || props.qouteToCost?.toString() === 'true' || (props.finalVersion && !props.isFinalVersionType && !props.lastestUnableversionId) || props.isApprovalCostDetailId">
                    <div v-if="!props.qouteToCost">
                        <h3>当前版本</h3>
                        <div class="evaluation">
                            <div class="evaluation-model"><span>评估版本：</span><span>{{ evaluation?.version }}</span></div>
                            <div class="evaluation-model"><span>评估时间：</span><span>{{ evaluation?.time }}</span></div>
                            <div class="evaluation-model"><span>评估人：</span><span>{{ evaluation?.person }}</span></div>
                        </div>
                    </div>
                    <a-table :dataSource="data" :pagination="false" :columns="columns" row-key="key" bordered size="middle">
                        <template #bodyCell="{ column, record }">
                            <template
                                v-if="((props.costDetail && isSkip === true) || props.finalVersion || props.updateQuote || props.isAllDetail) && column.dataIndex === 'item'">
                                <a @click="handleOpen(record.tab)" class="item-link">
                                    {{ record.item }}
                                </a>
                            </template>
                            <template v-if="column.dataIndex === 'amount'">
                                {{ formatNumber(record.amount) }}
                            </template>
                        </template>
                        <template #summary>
                            <a-table-summary-cell>项目成本合计（元）：</a-table-summary-cell>
                            <a-table-summary-cell>{{ formatNumber(totalAmount) }}</a-table-summary-cell>
                        </template>
                    </a-table>
                </div>
                <div class="rough" v-if="shouldShowRough || props.isApprovalCostRoughId">
                    <h3>版本详情</h3>
                    <div class="modal-content">
                        <div class="modal-flex">
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">商机名称：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.name }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">客户名称：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.customersName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">所属销售：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.saleName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">产品类别：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.category }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">所属售前：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.preSaleName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">报价版本：</span>
                                <span class="modal-item-son modal-item-son2">{{ qouteversion }}</span>
                            </div>
                            <div class="modal-item">
                                <span>报价总金额：</span>
                                <span>{{ quotationAmount }}</span>
                            </div>
                        </div>

                        <div style="margin: 20px 0;">
                            <a-table size="small" :dataSource="roughdata" :pagination="false" :columns="roughcolumns"
                                row-key="key" bordered>
                                <template #bodyCell="{ column, record }">
                                    <template v-if="column.dataIndex === 'amount'">
                                        <a-input-number v-model:value="record[column.dataIndex]" :step="0.01" :precision="2"
                                            style="width: 160px" :controls="false"
                                            :placeholder="!props.updateQuote ? '' : '请输入'" :bordered="false"
                                            :disabled="!props.updateQuote"></a-input-number>
                                    </template>
                                    <template v-if="column.dataIndex === 'valuationDesc'">
                                        <a-input v-model:value="record[column.dataIndex]"
                                            :placeholder="!props.updateQuote ? '' : '请输入'" :bordered="false"
                                            :disabled="!props.updateQuote"></a-input>
                                    </template>
                                </template>
                            </a-table>
                        </div>

                        <div class="modal-item">
                            <span style="margin-right: 15px;">报价说明：</span>
                            <span v-if="props.updateQuote" style="width:70%">
                                <a-textarea v-model:value="valuationDesc" :auto-size="{ minRows: 3, maxRows: 5 }"
                                    :disabled="!props.updateQuote"></a-textarea>
                            </span>
                            <span v-else style="width:70%">
                                {{ valuationDesc }}
                            </span>
                        </div>
                    </div>
                </div>
                <div class="incapable"
                    v-if="shouldShowIncapable || props.isApprovalCostUnableId || props.lastestUnableversionId">
                    <h3>版本详情</h3>
                    <div class="modal-content">
                        <div class="modal-flex">
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">商机名称：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.name }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">客户名称：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.customersName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">所属销售：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.saleName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">产品类别：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.category }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">所属售前：</span>
                                <span class="modal-item-son modal-item-son2">{{ opportunity.preSaleName }}</span>
                            </div>
                            <div class="modal-item">
                                <span class="modal-item-son modal-item-son1">报价版本：</span>
                                <span class="modal-item-son modal-item-son2">{{ qouteversion }}</span>
                            </div>
                        </div>

                        <div class="modal-item" style="display: flex; align-items: flex-start;"
                            :style="props.updateQuote ? 'margin-top: 8px;' : ''">
                            <span class="modal-item-son" style="width:13%">无法报价原因：</span>
                            <!-- <span class="modal-item-son modal-item-son2">{{ valuationUnableDesc }}</span> -->
                            <span style="width:60%">
                                <a-textarea v-if="props.updateQuote" v-model:value="valuationUnableDesc"
                                    :auto-size="{ minRows: 3, maxRows: 5 }" class="modal-item-son"
                                    :disabled="!props.updateQuote"></a-textarea>
                                <span v-else>{{ valuationUnableDesc }}</span>
                            </span>
                        </div>
                    </div>
                </div>
                <div v-if="deptData.length > 0" style="margin-top:10px">
                    <div style="width:100%"><span style="color: red;margin-right: 5px;"> *</span>产品项目组占比：</div>
                    <div class="modal-item"
                        style="display: flex; align-items: flex-start; padding-inline:20px; padding-block: 10px;">
                        <div style="width:100%">
                            <template v-for="(dept, index) in deptData" :key="index">
                                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                    <span style="flex: 1;">{{ dept.deptName }}</span>
                                    <span style="flex: 1;">{{ dept.userNames }}</span>
                                    <span style="flex: 1;">
                                        <a-input-number v-model:value="percentages[index]" :controls="false" :step='0.01'
                                            :precision="2"
                                            :disabled="(!props.updateQuote && shouldShowRough) || (!qouteToCost && !shouldShowRough)" />
                                        %
                                    </span>
                                </div>
                            </template>
                        </div>
                    </div>
                </div>
            </a-card>
        </div>
        <template #footer>
            <a-button @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleOk" v-if="props.qouteToCost || props.updateQuote">确定</a-button>
            <a-button type="primary" v-if="props.finalVersion" @click="handleSaleQuote">销售报价</a-button>
            <a-button type="primary" v-if="props.finalVersion" @click="handleReapply">重新申请报价</a-button>
            <a-button type="primary" v-if="props.finalVersion" @click="handleLose">丢单处理</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, computed, defineEmits, defineProps, watch, defineExpose } from 'vue';
import { fetchCostQuote, fetchCostQuoteDetail, getRoughInfo, fetchRoughQuote, getUnableInfo } from '@/api/costqoute'
import { anyType } from 'ant-design-vue/es/_util/type';
import { message } from 'ant-design-vue';
import { formatNumber } from '@/utils/format'
import { fetchUnableQuote, getDept } from '@/api/costqoute';

const props = defineProps({
    // 从待成本进入
    costTotalData: {
        type: Object,
        required: true,
    },
    costData: {
        type: Object,
        required: true,
    },
    opportunityDatas: {
        type: Object,
        required: true,
    },
    name: {
        type: String,
        required: true,
    },
    // 从待成本报价详细报价或者更新报价提交报价进入
    qouteToCost: {
        type: Boolean,
        required: false,
    },
    evaluation: anyType,
    opportunitiesIdUpdateBasic: {
        type: String,
        required: false,
    },
    // 从已成本进入
    opportunity: {
        type: Object,
        required: false
    },
    costDetail: {
        // 从已成本或者无法报价进入查看历史版本
        type: Boolean,
        required: false,
    },
    versionData: anyType,
    // 从待销售报价进入
    finalVersion: {
        type: Boolean,
        required: false,
    },
    finalDetailData: {
        type: Object,
        required: false,
    },
    isFinalVersionType: {
        type: Boolean,
        required: false,
    },
    lastestUnableversionId: anyType,
    lastestRoughversionId: anyType,
    // 判断详细报价还是粗略报价
    isRough: {
        type: String,
        required: false,
    },
    // 更新报价进入
    updateQuote: {
        type: Boolean,
        required: false,
    },
    updateVersionId: anyType,
    updateVersionData: anyType,
    // 从待报价审批进入查看报价详情
    isApprovalCostDetailId: {
        type: anyType,
        required: false,
    },
    isApprovalCostRoughId: {
        type: anyType,
        required: false,
    },
    isApprovalCostUnableId: {
        type: anyType,
        required: false,
    },
    // 从所有的详情页进入
    isAllDetail: {
        type: Boolean,
        required: false,
    },
    // 占比相关
    isAboutMultiDept: {
        type: Boolean,
        required: false,
    },
    intoModel: {
        type: anyType,
        required: false,
    },
    // 占比
    versionIds: {
        type: anyType,
        required: false,
    },
});


// 粗略报价详情
const roughcolumns = [
    {
        title: '类别',
        dataIndex: 'item',
        key: 'item',
    },
    {
        title: '粗略报价',
        dataIndex: 'amount',
        key: 'amount',
    },
    {
        title: '报价说明',
        dataIndex: 'valuationDesc',
        key: 'valuationDesc',
    }
]
const roughdata = ref([
    { key: 1, item: '售前支持小计（元）', amount: '', valuationDesc: '' },
    { key: 2, item: '定制开发小计（元）', amount: '', valuationDesc: '' },
    { key: 3, item: '产品平台小计（元）', amount: '', highlight: true, valuationDesc: '' },
    { key: 4, item: '硬件小计（元） - 自研', amount: '', valuationDesc: '' },
    { key: 5, item: '硬件小计（元） - 外采', amount: '', valuationDesc: '' },
    { key: 6, item: '实施小计（元）', amount: '', valuationDesc: '' },
    { key: 7, item: '其他小计（元）', amount: '', valuationDesc: '' },
]);
const emit = defineEmits(['close']);
const isCostVisible = ref(false);
const columns = [
    {
        title: '项目',
        dataIndex: 'item',
        key: 'item',
        width: '30%'
    },
    {
        title: '金额（元）',
        dataIndex: 'amount',
        key: 'amount',
        width: '30%'

    },
]
let data = ref([
    { key: 1, item: '售前支持小计（元）', amount: 0.00, tab: 'persale' },
    { key: 2, item: '定制开发小计（元）', amount: 0.00, tab: 'software' },
    { key: 3, item: '产品平台小计（元）', amount: 0.00, highlight: true, tab: 'product' },
    { key: 4, item: '硬件小计（元） - 自研', amount: 0.00, tab: 'self' },
    { key: 5, item: '硬件小计（元） - 外采', amount: 0.00, tab: 'purchasing' },
    { key: 6, item: '实施小计（元）', amount: 0.00, tab: 'implementation' },
    { key: 7, item: '其他小计（元）', amount: 0.00, tab: 'other' },
]);
const totalAmount = computed(() => {
    return data.value.reduce((sum, item) => sum + Number(item.amount), 0).toFixed(2);
});
const deptData = ref([]);  // 部门数据
const percentages = ref([]);
watch(() => props.qouteToCost, async (newVal) => {
    if (newVal) {
        console.log(props.opportunityDatas)
        if ((props.opportunityDatas || props.opportunitiesIdUpdateBasic)) {
            try {
                const res = await getDept({ 'opportunitiesId': props.opportunityDatas.id || props.opportunitiesIdUpdateBasic, 'costId': props.versionIds });
                deptData.value = res;
                percentages.value = deptData.value.map(dept => dept.radio);
            } catch (error) {
                message.error("获取部门数据失败");
                console.log(error)
            }
        }

    }
}
);
// 版本相关
const evaluation = ref({
    time: '',
    person: '',
    version: '',
});

// 从已成本进入
// 版本相关
const isHovered = ref(null);
const selectedVersion = ref(null);
const detailData = ref({}) //存储指定版本的详细数据
const evaluationVersions = ref([
])

// 标题动态显示
const getQuoteDetailTitle = (type) => {
    console.log(type, 'type')
    if (props.finalVersion) {
        switch (type) {
            case 'COST':
                return '报价详情 - 详细报价';
            case 'ROUGH':
                return '报价详情 - 粗略报价';
            case 'INCAPABLE':
                return '报价详情 - 无法报价';
            default:
                return '报价详情'; // 默认情况  
        }
    } else {
        return '报价详情'
    }

}

// 待销售报价查看成本报价详情
watch(() => props.finalDetailData, (newVal) => {
    if (newVal !== null) {
        deptData.value = newVal.radioList;
        percentages.value = deptData.value.map(dept => dept.radio);
        evaluation.value.time = props.finalDetailData?.time
        evaluation.value.person = props.finalDetailData?.person
        evaluation.value.version = props.finalDetailData?.valuationVersion
        data.value.forEach(item => {
            switch (item.key) {
                case 1:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesSupportVO?.totalCost || 0.00).toFixed(2);
                    break;
                case 2:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesCustomizableVo?.totalSoftwareCosts || 0.00).toFixed(2);
                    break;
                case 3:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesProductVO?.totalCost || 0.00).toFixed(2);
                    break;
                case 4:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesSelfVO?.totalCost || 0.00).toFixed(2);
                    break;
                case 5:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesExternalVO?.totalEstimatedlCost || 0.00).toFixed(2);
                    break;
                case 6:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesImplement?.totalCost || 0.00).toFixed(2);
                    break;
                case 7:
                    item.amount = parseFloat(props.finalDetailData?.quoteOpportunitiesOtherVO?.totalCost || 0.00).toFixed(2);
                    break;
                default:
                    item.amount = 0.00; // 默认值  
            }
        });
    } else {
        console.log(newVal)
    }
});

// 打开弹窗时更新数据  
const setDataFromProps = () => {
    data.value[0].amount = parseFloat(props.costTotalData.presalesTotal || 0).toFixed(2);
    data.value[1].amount = parseFloat(props.costTotalData.customDevTotal || 0).toFixed(2);
    data.value[2].amount = parseFloat(props.costTotalData.productTotal || 0).toFixed(2);
    data.value[3].amount = parseFloat(props.costTotalData.selfTotal || 0).toFixed(2);
    data.value[4].amount = parseFloat(props.costTotalData.purchasingTotal || 0).toFixed(2);
    data.value[5].amount = parseFloat(props.costTotalData.implementationTotal || 0).toFixed(2);
    data.value[6].amount = parseFloat(props.costTotalData.otherTotal || 0).toFixed(2);
    evaluation.value = props?.evaluation
};
// 当弹窗打开时调用该方法设置数据  
watch(() => props.costTotalData, (newValue) => {
    if (newValue) {
        setDataFromProps();
    }
});
watch(() => props.evaluation, (newVal) => {
    if (newVal !== null) {
        evaluation.value = newVal.value
    } else {
        console.log(newVal)
    }
});

// 待公司审批查看报价详情
// 粗略
watch(() => props.isApprovalCostRoughId, (newVal) => {
    console.log(newVal, '从待审批进入')
    if (newVal !== null) {
        selectRoughVersion(newVal)
    } else {
        console.log(newVal)
    }
});
// 详细
watch(() => props.isApprovalCostDetailId, (newVal) => {
    if (newVal !== null) {
        getCostQuoteDetail({ id: newVal })
    } else {
        console.log(newVal)
    }
});
// 无法
watch(() => props.isApprovalCostUnableId, (newVal) => {
    if (newVal !== null) {
        selectUnableVersion(newVal)
    } else {
        console.log(newVal)
    }
});


watch(() => props.versionData, (newVal) => {
    if (newVal !== null) {
        evaluationVersions.value = newVal
    } else {
        evaluationVersions.value = [
        ]
    }
});
// 选中要查看的版本
// 监听 selectedVersion 变化，实时查看所选版本
const shouldShowDetail = ref(false)
const shouldShowRough = ref(false)
const shouldShowIncapable = ref(false)
const qouteversion = ref('')// 粗略版本显示
const versionId = ref('') // 获取版本id---占比相关
watch(selectedVersion, (newVersion) => {
    const selectedVersionData = ref('')
    if (props.updateQuote) {
        selectedVersionData.value = props.updateVersionData
        versionId.value = props.updateVersionData.id
    } else {
        selectedVersionData.value = evaluationVersions.value.find(v => v.id === newVersion);
    }

    if (newVersion) {
        if (selectedVersionData.value?.versionType === 'COST') {
            shouldShowDetail.value = true
            shouldShowRough.value = false
            shouldShowIncapable.value = false
            selectVersion(newVersion);
        } else if (selectedVersionData.value?.versionType === 'ROUGH') {
            qouteversion.value = selectedVersionData.value?.valuationVersion || '';
            shouldShowRough.value = true
            shouldShowDetail.value = false
            shouldShowIncapable.value = false
            selectRoughVersion(newVersion);
        } else if (selectedVersionData.value?.versionType === 'INCAPABLE') {
            qouteversion.value = selectedVersionData.value?.valuationVersion || '';
            shouldShowRough.value = false
            shouldShowDetail.value = false
            shouldShowIncapable.value = true
            selectUnableVersion(newVersion)
        }
    }
});
// 从更新报价进入查看选中版本
watch(() => props.updateQuote, (newVal) => {
    if (newVal) {
        selectedVersion.value = props.updateVersionId
    }
})
// 从待销售进入 并且报价类型是无法报价
const lastestUnableversionId = ref('')
watch(() => props.lastestUnableversionId, (newVal) => {
    if (newVal) {
        lastestUnableversionId.value = newVal
        selectUnableVersion(lastestUnableversionId.value);
    }
})
// 从待销售进入 并且报价类型是粗略报价
const lastestRoughversionId = ref('')
watch(() => props.lastestRoughversionId, (newVal) => {
    if (newVal) {
        lastestRoughversionId.value = newVal
        selectRoughVersion(lastestRoughversionId.value);
    }
})

watch(() => props.isFinalVersionType, (newVal) => {
    if (newVal) {
        shouldShowRough.value = true
        shouldShowDetail.value = false
    }
})
// 无法报价详情
const valuationUnableDesc = ref(null)
const selectUnableVersion = async (id) => {
    try {
        const response = await getUnableInfo(id);
        if (response && response.code === 200) {
            valuationUnableDesc.value = response.data.valuationDesc
            qouteversion.value = response.data.valuationVersion
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};
// 粗略报价详情
const valuationDesc = ref('');
const quotationAmount = computed(() => {
    // 检查是否所有金额都为零
    const allZero = roughdata.value.every(item => parseFloat(item.amount) === 0);

    if (allZero) {
        // 如果表格数据都为零，使用接口返回的amount值
        return (parseFloat(quotationAmountFromAPI.value) || 0).toFixed(2);
    } else {
        // 否则，按表格中的数据计算总和
        return roughdata.value.reduce((sum, item) => sum + (parseFloat(item.amount) || 0), 0).toFixed(2);
    }
});

const selectRoughVersion = (version) => {
    getRoughCostQuoteDetail({ id: version })
};
const quotationAmountFromAPI = ref(null);
const getRoughCostQuoteDetail = async (id) => {
    try {
        const response = await getRoughInfo(id);
        if (response && response.code === 200) {
            const quoteDetails = response.data;
            if (props.updateQuote?.toString() === 'true') {
                const res = await getDept({ 'opportunitiesId': props.opportunity.id || props.opportunitiesIdUpdateBasic, 'costId': id.id });
                deptData.value = res;
                percentages.value = deptData.value.map(dept => dept.radio);
            } else {
                deptData.value = quoteDetails.radioList
            }
            percentages.value = deptData.value.map(dept => dept.radio);
            const details = response.data.quoteOpportunitiesRoughDetails[0];
            roughdata.value = [
                { key: 1, item: '售前支持小计（元）', amount: details.supportAmount, valuationDesc: details.supportDesc },
                { key: 2, item: '定制开发小计（元）', amount: details.customAmount, valuationDesc: details.customDesc },
                { key: 3, item: '产品平台小计（元）', amount: details.productAmount, valuationDesc: details.productDesc },
                { key: 4, item: '硬件小计（元） - 自研', amount: details.selfAmount, valuationDesc: details.selfDesc },
                { key: 5, item: '硬件小计（元） - 外采', amount: details.externalAmount, valuationDesc: details.externalDesc },
                { key: 6, item: '实施小计（元）', amount: details.implementAmount, valuationDesc: details.implementDesc },
                { key: 7, item: '其他小计（元）', amount: details.otherAmount, valuationDesc: details.otherDesc }
            ];
            // 存储接口返回的金额
            quotationAmountFromAPI.value = quoteDetails.amount;
            valuationDesc.value = quoteDetails.valuationDesc
            qouteversion.value = quoteDetails.valuationVersion
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};
// 详细报价 查看选中详情
const isSkip = ref(false)
const selectVersion = (version) => {
    isSkip.value = true
    getCostQuoteDetail({ id: version })
};
// 调取详细成本报价接口
const getCostQuoteDetail = async (id) => {
    try {
        const response = await fetchCostQuoteDetail(id);
        if (response && response.code === 200) {
            const quoteDetails = response.data;
            // 根据获取的数据填充表格 
            detailData.value = quoteDetails
            evaluation.value.time = detailData.value.time
            evaluation.value.person = detailData.value.person
            evaluation.value.version = detailData.value.valuationVersion
            deptData.value = quoteDetails.radioList
            // 获取对应的值并赋值到 data 的 amount 属性  
            data.value.forEach(item => {
                switch (item.key) {
                    case 1:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesSupportVO?.totalCost || 0.00).toFixed(2);
                        break;
                    case 2:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesCustomizableVo?.totalSoftwareCosts || 0.00).toFixed(2);
                        break;
                    case 3:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesProductVO?.totalCost || 0.00).toFixed(2);
                        break;
                    case 4:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesSelfVO?.totalCost || 0.00).toFixed(2);
                        break;
                    case 5:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesExternalVO?.totalEstimatedlCost || 0.00).toFixed(2);
                        break;
                    case 6:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesImplement?.totalCost || 0.00).toFixed(2);
                        break;
                    case 7:
                        item.amount = parseFloat(detailData.value.quoteOpportunitiesOtherVO?.totalCost || 0.00).toFixed(2);
                        break;
                    default:
                        item.amount = 0.00; // 默认值  
                }
            });
            percentages.value = deptData.value.map(dept => dept.radio);
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};

// 清空详细报价所有
function resetAmount(data) {
    for (let i = 0; i < data.length; i++) {
        data[i].amount = 0;
    }
}
// 清空粗略报价所有
const resetRoughAll = () => {
    for (let i = 0; i < roughdata.value.length; i++) {
        roughdata.value[i].amount = 0;
        roughdata.value[i].valuationDesc = '';
    }
    valuationDesc.value = ''
}
const resetAll = () => {
    isSkip.value = false
    selectedVersion.value = ''
    resetAmount(data.value);
    resetRoughAll()
    detailData.value = {}
}

const handleOpen = (param) => {
    const tab = encodeURIComponent(JSON.stringify(param))
    const name = encodeURIComponent(props.name);
    const versionIds = encodeURIComponent(versionId.value);
    if (props.finalVersion?.toString() === 'true') {
        // 从待销售报价进入
        const detailVersionData = JSON.stringify(props.finalDetailData);
        sessionStorage.setItem('detailData', detailVersionData);
        window.open(`/costdetailquotation?tab=${tab}&name=${name}`, '_blank');
    } else if (props.costDetail?.toString() === 'true' || props.isAllDetail) {
        // 从已成本进入

        const detailVersionData = JSON.stringify(detailData.value); // 传参
        sessionStorage.setItem('detailData', detailVersionData);
        window.open(`/costdetailquotation?tab=${tab}&name=${name}`, '_blank');
    } else if (props.updateQuote?.toString() === 'true') {
        // 从更新报价进入
        const detailVersionData = JSON.stringify(detailData.value); // 传参
        sessionStorage.setItem('detailData', detailVersionData);
        const sourceIdentifier = 'updateQuote';
        const newWindow = window.open(`/costdetailquotation?tab=${tab}&source=${sourceIdentifier}&name=${name}&versionId=${versionIds}`, '_blank');
        if (!newWindow) {
            alert('新窗口未能打开，请检查浏览器的弹窗设置。');
            return;
        }
        // 定时检查新窗口是否关闭  
        const checkWindowClosed = setInterval(() => {
            if (newWindow.closed) {
                clearInterval(checkWindowClosed);
                // 当新窗口关闭时刷新当前页面  
                window.location.reload();
            }
        }, 800);
    }
}
const handleCancel = () => {
    percentages.value = []
    deptData.value = []
    selectedVersion.value = false
    shouldShowDetail.value = false
    shouldShowRough.value = false
    shouldShowIncapable.value = false
    if (props.costDetail?.toString() === 'true') {
        // resetRoughAll()
        resetAll()
        emit('closedetail');
    } else if (props.updateQuote?.toString() === 'true') {
        resetAll()
        emit('closeUpdate');

    } else if (props.finalVersion?.toString() === 'true') {
        // resetRoughAll()
        resetAll()
        emit('closeFinal');
    } else if (props.isApprovalCostDetailId || props.isApprovalCostRoughId || props.isApprovalCostUnableId) {
        emit('closeApproval')
    } else {
        isCostVisible.value = false;
        emit('close');
    }

};
// const handleUpdate = () => {
//     emit('close-update',true)
// }
const handleSaleQuote = () => {
    emit('handle-sale', props.opportunity)
}
const handleReapply = () => {
    emit('handle-reapply', props.opportunity)
}
const handleLose = () => {
    emit('handle-lose', props.opportunity)
}
const handleOk = async () => {
    // 判断从哪里进入的此页面
    if (props.costDetail === true) {
        resetAll()
        emit('closedetail');
    } else if (props.updateQuote?.toString() === 'true') {
        const radioList = ref([])
        if (shouldShowRough.value && !shouldShowDetail.value) {
            if (props.opportunity.isMultiDept == true) {
                const totalPercentage = percentages.value.reduce((sum, value) => sum + Number(value), 0);
                // 验证总和是否为 100  
                if (totalPercentage !== 100) {
                    message.error("输入的占比总和必须为 100%");
                    return;
                }
                radioList.value = deptData.value.map((dept, index) => ({
                    deptId: dept.deptId,
                    radio: percentages.value[index], // 获取对应的输入框值  
                    supportPerson: dept.supportPerson,
                }));
            }
            const formData = {
                supportAmount: 0,
                supportDesc: '',
                customAmount: 0,
                customDesc: '',
                externalAmount: 0,
                externalDesc: '',
                implementAmount: 0,
                implementDesc: '',
                otherAmount: 0,
                otherDesc: '',
                productAmount: 0,
                productDesc: '',
                selfAmount: 0,
                selfDesc: ''
            };

            roughdata.value.forEach(item => {
                switch (item.key) {
                    case 1:
                        formData.supportAmount = parseFloat(item.amount) || 0;
                        formData.supportDesc = item.valuationDesc || '';
                        break;
                    case 2:
                        formData.customAmount = parseFloat(item.amount) || 0;
                        formData.customDesc = item.valuationDesc || '';
                        break;
                    case 3:
                        formData.productAmount = parseFloat(item.amount) || 0;
                        formData.productDesc = item.valuationDesc || '';
                        break;
                    case 4:
                        formData.selfAmount = parseFloat(item.amount) || 0;
                        formData.selfDesc = item.valuationDesc || '';
                        break;
                    case 5:
                        formData.externalAmount = parseFloat(item.amount) || 0;
                        formData.externalDesc = item.valuationDesc || '';
                        break;
                    case 6:
                        formData.implementAmount = parseFloat(item.amount) || 0;
                        formData.implementDesc = item.valuationDesc || '';
                        break;
                    case 7:
                        formData.otherAmount = parseFloat(item.amount) || 0;
                        formData.otherDesc = item.valuationDesc || '';
                        break;
                    default:
                        break;
                }
            });
            const totalQuotationAmount = parseFloat(quotationAmount.value);
            const quotationData = {
                radioList: radioList.value,
                amount: totalQuotationAmount, // 确保是数字类型
                opportunitiesId: props.opportunity.id,
                valuationDesc: valuationDesc.value,
                quoteOpportunitiesRoughDetails: [
                    formData
                ] // 直接赋值 formData
            };

            try {
                await fetchRoughQuote(quotationData); // 调用粗略报价接口
                emit('quote-sent');
                handleCancel();
            } catch (error) {
                console.error('粗略报价失败:', error);
            }
        } else if (shouldShowIncapable.value && !shouldShowDetail.value) {
            // 验证原因说明是否为空
            if (!valuationUnableDesc.value.trim()) {
                message.info('请填写无法报价原因')
                return;
            }
            const quotationData = {
                id: props.opportunity.id,
                quoteDesc: valuationUnableDesc.value,
                // type: 'INCAPABLE' 
            };

            try {
                await fetchUnableQuote(quotationData);
                emit('quote-sent');
                handleCancel();
            } catch (error) {
                console.error('无法报价失败:', error);
            }
        } else if (shouldShowDetail.value) {
            handleOpen('persale')
        }
        resetAll()
        emit('closeUpdate');

    } else if (props.finalVersion?.toString() === 'true') {
        resetAll()
        emit('closeFinal');
    } else {
        // 占比传参定义
        const radioList = ref([])
        if (deptData.value.length > 0) {
            const totalPercentage = percentages.value.reduce((sum, value) => sum + Number(value), 0);
            // 验证总和是否为 100  
            if (totalPercentage !== 100) {
                message.error("输入的占比总和必须为 100%");
                return; // 阻止继续执行  
            }
            radioList.value = deptData.value.map((dept, index) => ({
                deptId: dept.deptId,
                radio: percentages.value[index], // 获取对应的输入框值  
                supportPerson: dept.supportPerson,
            }));
        }
        let preTotal = 0;
        let devTotal = 0;
        let productTotal = 0;
        let selfTotal = 0;
        let externalTotal = 0;
        let implementTotal = 0;
        let otherTotal = 0;

        data.value.forEach(item => {
            switch (item.key) {
                case 1:
                    preTotal = parseFloat(item.amount) || 0; // 售前支持小计  
                    break;
                case 2:
                    devTotal = parseFloat(item.amount) || 0;  // 定制开发小计  
                    break;
                case 3:
                    productTotal = parseFloat(item.amount) || 0; // 产品平台小计  
                    break;
                case 4:
                    selfTotal = parseFloat(item.amount) || 0; // 硬件小计 - 自研  
                    break;
                case 5:
                    externalTotal = parseFloat(item.amount) || 0; // 硬件小计 - 外采  
                    break;
                case 6:
                    implementTotal = parseFloat(item.amount) || 0; // 实施小计  
                    break;
                case 7:
                    otherTotal = parseFloat(item.amount) || 0; // 其他小计  
                    break;
                default:
                    break;
            }
        });
        try {
            const opportunitiesId = ref('')
            if (props.opportunitiesIdUpdateBasic.value !== '') {
                opportunitiesId.value = props.opportunitiesIdUpdateBasic
            } else {
                opportunitiesId.value = props.opportunityDatas.id
            }
            const payload = {
                amount: parseFloat(totalAmount.value),
                opportunitiesId: opportunitiesId.value,
                quoteOpportunitiesCustomizableList: props.costData.quoteOpportunitiesCustomizableList,
                quoteOpportunitiesExternalList: props.costData.quoteOpportunitiesExternalList,
                quoteOpportunitiesImplementList: props.costData.quoteOpportunitiesImplementList,
                quoteOpportunitiesOtherList: props.costData.quoteOpportunitiesOtherList,
                quoteOpportunitiesProductList: props.costData.quoteOpportunitiesProductList,
                quoteOpportunitiesSelfList: props.costData.quoteOpportunitiesSelfList,
                quoteOpportunitiesSupportList: props.costData.quoteOpportunitiesSupportList,
                status: '1',
                valuationDesc: props.costData.valuationDesc || '',
                supportRemark: props.costData.supportRemark || '',
                // 总值
                preTotal: preTotal,
                devTotal: devTotal,
                proTotal: productTotal,
                selfTotal: selfTotal,
                extTotal: externalTotal,
                impTotal: implementTotal,
                otherTotal: otherTotal,
                radioList: radioList.value
            };

            const response = await fetchCostQuote(payload);

            if (response && response.code === 200) {
                console.log('Quote saved successfully:', response.data);
                message.info('提交成功');
                setTimeout(() => {
                    emit('ok');
                    emit('close');
                }, 1000)

            } else {
                message.info('提交失败');
                console.error('Failed to save quote:', response.error);
            }
        } catch (error) {
            alert('提交失败')
            console.error('Error while saving quote:', error);
        }
    }
    // 置空数据
    shouldShowDetail.value = false
    shouldShowRough.value = false
    shouldShowIncapable.value = false
    deptData.value = []
    selectedVersion.value = null
    percentages.value = []
};

const getTotal = () => {
    return totalAmount
}
defineExpose({ getTotal });
</script>

<style scoped>
.ant-layout-content {
    height: 100%;
    overflow: auto !important;
    margin-top: 20px;
}

.evaluation {
    display: flex;
    justify-content: space-between;
    padding: 10px;
    border: 1px solid #f0f0f0;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.evaluation h3 {
    display: block;
}

.evaluation-model {
    flex: 1;
    display: flex;
    align-items: center;
    padding: 0 10px;
}

.evaluation-model span {
    margin-right: 5px;
}

.layout {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
}

.left {
    flex: 1;
    margin-right: 20px;
    width: 55%;
}

.right {
    width: 35%;
}

.evaluation {
    margin-bottom: 30px;
}

.total {
    margin-top: 30px;
    font-weight: bold;
}

.item-link {
    cursor: pointer;
}

label {
    /* font-weight: bold; */
}

table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #000;
}

td,
th {
    border: 1px solid #000;
    text-align: left;
    height: 45px;
}

.label {
    width: 30%;
    text-align: center;
    line-height: 45px;
}

.input {
    width: 60%;
}

.input input,
.input textarea {
    width: 98%;
    border: none;
    outline: none;
}

.ant-layout-content>>>.ant-table-summary>.ant-table-cell {
    background: #f1f1f1;
    border: 1px solid #faf5f5;
    padding: 16px;
    font-weight: 600;
}

.modal-content {
    padding: 0 20px;
}

.modal-flex {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.modal-item {
    flex: 1 1 45%;
    box-sizing: border-box;
    margin-bottom: 10px;
    display: flex;
    align-items: flex-start;
}

.modal-item-son {
    display: inline-block;
}

.modal-item-son1 {
    width: 22%;
}

.modal-item-son2 {
    width: 60%;
}

.version-history {
    /* padding: 0 16px; */
    max-height: 330px;
    /* min-height: 130px; */
    overflow-y: auto;
    display: flex;
    gap: 20px;
}

/* .version-list {
    display: flex;
    flex-wrap: wrap;   
} */

.version-item {
    flex: 45%;
    box-sizing: border-box;
    margin-bottom: 12px;
    position: relative;
}

@media screen and (max-width: 768px) {
    .modal-item {
        flex: 1 1 100%;
    }
}
</style>