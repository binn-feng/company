<template>
    <a-modal :title="props.componentTitle" :open="isQuotationApprovalVisible" @cancel="handleCancel"
        :width="opportunity && opportunity.type !== 'INCAPABLE' ? '70%' : '800px'" @ok="handleOk" :destroyOnClose="true">
        <!-- <a-spin v-if="loading" /> -->
        <div class="ant-body">
            <div v-if="opportunity.type !== 'INCAPABLE'">
                <div style="margin:20px auto;" v-if="props.isPriceSheet || props.isReContractApplication || props.isSignDetail">
                    <h4 style="color:rgb(82, 73, 73) ">合同类型</h4>
                    <a-radio-group v-model:value="contractType" :disabled="props.isSignDetail">
                        <a-radio :value="'1'">欣象代理</a-radio>
                        <a-radio :value="'2'">北光直签</a-radio>
                    </a-radio-group>
                </div>
                <a-table :columns="columns" :data-source="data" bordered :pagination="false" size="small"
                    style="width: 100%;">
                    <template #title>
                        <div class="modal-content"
                            style="border: 1px solid #f0f0f0; background: #fafafa; padding: 20px 10px 10px;">
                            <div class="modal-item">
                                <span style="width: 20%;">项目名称：</span>
                                <span style="width: 60%;">{{ opportunity.name }}</span>
                            </div>
                            <div class="modal-item">
                                <span style="width: 20%;">销售人员：</span>
                                <span style="width: 60%; ">{{ opportunity.saleName }}</span>
                            </div>
                        </div>
                    </template>
                    <template v-slot:bodyCell="{ column, record, index }">
                        <span v-if="column.dataIndex === 'profitMargin'" :class="{
                            'highlight': isProfitRateLow && record.quotationItem !== '硬件采购'
                        }">
                            {{ record.profitMargin ? (parseFloat(record.profitMargin) * 100).toFixed(2) + '%' : '-' }}
                        </span>
                        <span v-else-if="column.dataIndex === 'totalProfitMargin'">
                            {{ record.totalProfitMargin ? (parseFloat(record.totalProfitMargin) * 100).toFixed(2) + '%' :
                                '-' }}
                        </span>
                        <!-- <span v-else-if="['totalAmount', 'quotedAmount', 'externalQuote'].includes(column.dataIndex)"> -->
                        <span v-else-if="column.dataIndex === 'externalQuote'">
                            <span v-if="props.isPriceSheet || props.isReContractApplication || props.isSignDetail">
                                <a-input-number v-model:value="record[column.dataIndex]" :placeholder="'请输入' + column.title"
                                    @input="updateExternalQuote(index, record.externalQuote)" :step="0.01" :controls="false"
                                    :disabled="props.isSignDetail" />
                            </span>
                            <span v-else>{{ record[column.dataIndex] || '-' }}</span>
                        </span>
                        <span v-else>
                            {{ record[column.dataIndex] }}
                        </span>
                    </template>
                </a-table>
            </div>

            <!-- 待报价审批显示 -->
            <div class="follow-up" v-if="props.isQuotationPendingApproval">
                <div v-if="opportunity.type === 'INCAPABLE'">
                    <div class="modal-content">
                        <div class="modal-item">
                            <span style="width: 30%;">商机名称：</span>
                            <span style="width: 60%;">{{ opportunity.name }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">客户名称：</span>
                            <span style="width: 60%; ">{{ opportunity.customersName }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">所属销售：</span>
                            <span style="width: 60%;">{{ opportunity.saleName }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">产品类别：</span>
                            <span style="width: 60%;">{{ opportunity.category }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">所属售前：</span>
                            <span style="width: 60%;">{{ opportunity.preSaleName }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">报价版本：</span>
                            <span style="width: 60%;">{{ opportunity.currentVersion }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">报价金额：</span>
                            <span style="width: 60%;">-</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">签约类型：</span>
                            <span style="width: 60%;">{{ signType == '1' ? '欣象代理' : signType == '2' ? '北光直签' : '' }}</span>
                        </div>
                        <div class="modal-item" v-if="opportunity.signType == '1'">
                            <span style="width: 40%;">欣象报价金额：</span>
                            <span style="width: 60%;">{{ xxUnableQuoteAmount }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 40%;">北光报价金额：</span>
                            <span style="width: 60%;">{{ unableQuoteAmount }}</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">整体利润率：</span>
                            <span style="width: 60%;">-</span>
                        </div>
                        <div class="modal-item">
                            <span style="width: 30%;">报价说明：</span>
                            <span style="width: 60%;">{{ valuationDesc }}</span>
                        </div>
                    </div>
                </div>
                <div style="margin: 0 10px 10px;">
                    <div>
                        <h4 style="margin-top: -5px;">项目概要介绍:</h4>
                        <div style="margin-left: 20px;">
                            {{ opportunity.introduce ? opportunity.introduce : '暂无介绍' }}
                        </div>
                    </div>
                    <div v-if="props.componentTitle == '报价审批' && isProfitRateLow">
                        <h4 style="">继续跟单理由:</h4>
                        <div style="margin-left: 20px;">
                            {{ opportunity.closeNote ? opportunity.closeNote : '-' }}
                        </div>
                    </div>
                    <!-- <div>
                        <h4>继续跟进理由:</h4>
                        <div style="margin-left: 20px;">
                            {{ opportunity.closeNote }}
                        </div>
                    </div> -->
                    <div style="margin:20px auto;">
                        <h4>是否批准：</h4>
                        <a-radio-group v-model:value="isApproval" style="margin-left: 20px;">
                            <a-radio :value="true">批准</a-radio>
                            <a-radio :value="false">拒绝</a-radio>
                        </a-radio-group>
                    </div>
                    <div>
                        <h4 style="margin-bottom:10px">报价审批说明：</h4>
                        <a-textarea v-model:value="auditDesc" placeholder="请输入报价审批说明" style="margin-left: 20px;" />
                    </div>
                </div>
            </div>
            <!-- 报价单签约申请  待签约审批-重新签约申请显示 -->
            <div class="contract-application"
                v-if="props.isPriceSheet || props.isReContractApplication || props.isSignDetail">
                
                <div>
                    <h3><span style="color:red">*</span>申请签约说明：</h3>
                    <a-textarea v-model:value="signDesc" placeholder="理由" :disabled="props.isSignDetail" />
                </div>
            </div>
            <!-- 待签约审批-签约审批显示 -->
            <div class="follow-up" v-if="props.isApprovalOfContractSigning">
                <div>
                    <div>
                        <span>合同类型：</span>
                        <span>{{ getContractTypeText(opportunity.contractType) }}</span>
                    </div>
                </div>
                <div>
                    <h3 style="display: inline-block">申请签约理由：</h3>
                    <span>{{ signDesc || '-' }}</span>
                    <!-- <a-textarea v-model:value="signDesc" :rows="4" disabled /> -->
                </div>
                <div style="margin: 0 auto 20px;">
                    <span>是否批准：</span>
                    <a-radio-group v-model:value="isFinalApproval">
                        <a-radio :value="true">批准</a-radio>
                        <a-radio :value="false">拒绝</a-radio>
                    </a-radio-group>
                </div>
                <div>
                    <a-textarea v-model:value="auditDesc" placeholder="请输入批准理由" />
                </div>
            </div>
        </div>
        <template #footer>
            <a-button @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleOk">确认</a-button>
        </template>
    </a-modal>
</template>  
  
<script setup>
import html2canvas from 'html2canvas';
import { ref, defineEmits, watch, defineProps, nextTick } from 'vue';
import { onMounted, toRefs, computed } from 'vue';
import { getApprovalAndSignInfo, getSignApprovalPageInfo, getReSignInfo, getapprovalInfo } from '@/api/saleQoute'
import { signApproval, signReject, signApplication } from '@/api/pendingContract'
import { salesApproval, salesReject } from '@/api/saleQoute';
import { getSignDetailInfo } from '@/api/pendingContract';
import { message } from 'ant-design-vue';
import { anyType } from 'ant-design-vue/es/_util/type';
import { getMap, getProfit } from '@/api/mannageDebug' //报价设置（1.06 1.13）

// const loading = ref(true)
const data = ref([
    { key: 1, quotationItem: '软件', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '人工成本', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
    { key: 2, quotationItem: '产品', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '产品成本', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
    { key: 3, quotationItem: '硬件-自研', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '硬件成本-自研', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
    { key: 4, quotationItem: '实施', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '实施成本', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
    { key: 5, quotationItem: '其他', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '其他成本', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
    { key: 6, quotationItem: '硬件采购', totalAmount: 0, quotedAmount: 0, externalQuote: 0, expectedIncome: 0, totalExpectedIncome: 0, projectCostName: '硬件-采购', projectCost: 0, expectedCost: 0, totalProfit: 0, profitMargin: 0, totalProfitMargin: 0 },
],);
const columns = ref([
    {
        title: '北光报价总金额',
        dataIndex: 'totalAmount',
        key: 'totalAmount',
        customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 6 };
            }
            return { colSpan: 0 };
        },
        render: (text) => text ? parseFloat(text).toFixed(2) : '-'
    },
    {
        title: '北光报价金额',
        dataIndex: 'quotedAmount',
        key: 'quotedAmount',
        customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 5 };
            }
            return index === 5 ? { rowSpan: 1 } : { colSpan: 0 }
        },
        render: (text) => text ? parseFloat(text).toFixed(2) : '-'
    },
    {
        title: '对外报价',
        dataIndex: 'quotationItem',
        colSpan: 2,
    },
    {
        title: '外部报价',
        colSpan: 0,
        dataIndex: 'externalQuote',
        key: 'externalQuote',
        width: '10%'
    },
    { title: '预计税后收入', dataIndex: 'expectedIncome', key: 'expectedIncome' },
    {
        title: '预计税后总收入', dataIndex: 'totalExpectedIncome', key: 'totalExpectedIncome',
        customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 5 };  // 合并三行  
            }
            return index === 5 ? { rowSpan: 1 } : { colSpan: 0 }
        },
        render: (text) => text ? parseFloat(text).toFixed(2) : '-'
    },
    {
        title: '项目成本',
        dataIndex: 'projectCostName',
        key: 'projectCostName',
        colSpan: 2,
    },
    {
        title: '项目成本',
        dataIndex: 'projectCost',
        key: 'projectCost',
        colSpan: 0,
        width: '10%',
        render: (text) => text ? parseFloat(text).toFixed(2) : '-'

    },
    {
        title: '预计成本', dataIndex: 'expectedCost', key: 'expectedCost', customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 5 };  // 合并三行  
            }
            return index === 5 ? { rowSpan: 1 } : { colSpan: 0 }
        },
    },
    {
        title: '预计利润',
        dataIndex: 'totalProfit',
        key: 'totalProfit',
        customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 5 };
            }
            return index === 5 ? { rowSpan: 1 } : { colSpan: 0 }
        },
        render: (text) => text ? parseFloat(text).toFixed(2) : '-'
    },
    {
        title: '成本利润率', dataIndex: 'profitMargin', key: 'profitMargin', customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 5 };  // 合并三行  
            }
            return index === 5 ? { rowSpan: 1 } : { colSpan: 0 }
        },
    },
    {
        title: '总成本利润率', dataIndex: 'totalProfitMargin', key: 'totalProfitMargin', customCell: (_, index) => {
            if (index === 0) {
                return { rowSpan: 6 };
            }
            return { colSpan: 0 };
        },
    },
])
const props = defineProps({
    opportunity: {
        type: Object,
        required: true,
    },
    componentTitle: {
        type: String,
        required: false,
    },
    // 报价待审批
    isQuotationPendingApproval: {
        type: Boolean,
        required: false,
    },
    // 报价单-签约申请
    isPriceSheet: {
        type: Boolean,
        required: false,
    },
    // 待签约审批-签约审批
    isApprovalOfContractSigning: {
        type: Boolean,
        required: false,
    },
    // 待签约审批-重新签约申请
    isReContractApplication: {
        type: Boolean,
        required: false,
    },
    // 通用标识
    isSummarySheet: {
        type: Boolean,
        required: false,
    },
    // 查看签约申请详情
    isSignDetail: {
        type: Boolean,
        required: false,
    },
    selectedSignId: {
        type: anyType,
        required: false,
    },
});
watch(() => props.componentTitle, (newVal) => {
    columns.value[0].title = newVal.includes('签约') ? '北光合同总金额' : '北光报价总金额';
    columns.value[1].title = newVal.includes('签约') ? '北光合同金额' : '北光报价金额';
    columns.value[2].title = newVal.includes('签约') ? '类别' : '对外报价';
});
const isSaleQuoteOkVisible = ref(false);
// 批准拒绝
const isApproval = ref(true)
const auditDesc = ref('') //审批通过/驳回理由
const signDesc = ref('')//签约申请说明
const contractType = ref('1') //合同类型
const isFinalApproval = ref(true)
const signType = ref('')
const unableQuoteAmount = ref('')
const xxUnableQuoteAmount = ref('')
const valuationDesc = ref('')
const mapData = ref(null);
const contractProfit = ref(null)
const externalProfit = ref(null)
const getContractTypeText = (contractType) => {
    const contractTypes = {
        1: '欣象代理',
        2: '北光直签'
    };
    return contractTypes[contractType] || '未知类型';
}
const emit = defineEmits(['close']);
watch(() => props.opportunity, (newVal) => {
    isSaleQuoteOkVisible.value = !!newVal;
});

const { isReContractApplication, isPriceSheet,isQuotationPendingApproval,isApprovalOfContractSigning } = toRefs(props);

// Watch for changes in either isReContractApplication or isPriceSheet  
watch([isReContractApplication, isPriceSheet,isQuotationPendingApproval,isApprovalOfContractSigning], ([newReContract, newPriceSheet], [oldReContract, oldPriceSheet]) => {
    console.log('isReContractApplication changed from', oldReContract, 'to', newReContract);
    console.log('isPriceSheet changed from', oldPriceSheet, 'to', newPriceSheet);
    getProfitFunc()
});
const getProfitFunc = async () => {
    try {
        const response = await getProfit();
        if (response && response.code === 200) {
            contractProfit.value = response.data.contractProfit
            externalProfit.value = response.data.externalProfit
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}
const imgTag = ref('')
// 捕获表格区域以供邮件显示
const captureScreenshot = async () => {
    return new Promise((resolve, reject) => {
        nextTick(() => {
            const element = document.querySelector('.ant-body');
            if (element) {
                html2canvas(element).then((canvas) => {
                    const base64Image = canvas.toDataURL('image/png');
                    resolve(base64Image);
                }).catch((error) => {
                    reject(error);
                });
            } else {
                reject(new Error('未找到要捕获的表格元素'));
            }
        });
    });
};

// 报价签约审批传参
const resData = ref(null)
const fetchData = async (id) => {
    // loading.value = true;
    const response = ref(null)
    try {
        if (props.isApprovalOfContractSigning) {
            response.value = await getSignApprovalPageInfo({ opportunitiesId: id });
        } else if (props.isReContractApplication) {
            response.value = await getReSignInfo({ opportunitiesId: id });
        } else if (props.isQuotationPendingApproval) {
            response.value = await getapprovalInfo({ opportunitiesId: id });
        } else {
            response.value = await getApprovalAndSignInfo({ opportunitiesId: id });
        }
        if (response.value.code === 200) {
            const res = response.value.data;
            resData.value = res
            contractType.value = res.contractType
            data.value = [
                {
                    key: 1,
                    quotationItem: '软件',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.softWareExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.softWareExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '人工成本',
                    projectCost: parseFloat(res.laborPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 2,
                    quotationItem: '产品',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.prodExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.prodExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '产品成本',
                    projectCost: parseFloat(res.prodPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 3,
                    quotationItem: '硬件-自研',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.selfExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.selfExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '硬件成本-自研',
                    projectCost: parseFloat(res.selfPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 4,
                    quotationItem: '实施',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.impExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.impExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '实施成本',
                    projectCost: parseFloat(res.impPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 5,
                    quotationItem: '其他',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.otherExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.otherExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '其他成本',
                    projectCost: parseFloat(res.otherPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 6,
                    quotationItem: '硬件采购',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.extExtQuote).toFixed(2),
                    externalQuote: parseFloat(res.extExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.extExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.extExtIncome).toFixed(2),
                    projectCostName: '硬件-采购',
                    projectCost: parseFloat(res.extPrice).toFixed(2),
                    expectedCost: parseFloat(res.extPrice).toFixed(2),
                    totalProfit: parseFloat(res.extProfit).toFixed(2),
                    profitMargin: res.extProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                }
            ];
            if (props.isQuotationPendingApproval) {
                signType.value = res.signType
                unableQuoteAmount.value = res.unableQuoteAmount
                xxUnableQuoteAmount.value = res.xxUnableQuoteAmount
                valuationDesc.value = res.valuationDesc
            }
            if (props.isApprovalOfContractSigning) {
                // contractType.value = res.contractType
                signDesc.value = res.signDesc
            }
        } else {
            console.log('wushuju')
        }
    } catch (error) {
        console.error("接口调用失败", error);
    } finally {
        // loading.value = false;
    }
};
// 查看申请签约版本
const fetchSignDetail = async (id) => {
    // loading.value = true;
    try {
        const response = await getSignDetailInfo({ id: id });
        if (response.code === 200) {
            const res = response.data;
            resData.value = res
            data.value = [
                {
                    key: 1,
                    quotationItem: '软件',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.softWareExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.softWareExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '人工成本',
                    projectCost: parseFloat(res.laborPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 2,
                    quotationItem: '产品',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.prodExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.prodExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '产品成本',
                    projectCost: parseFloat(res.prodPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 3,
                    quotationItem: '硬件-自研',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.selfExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.selfExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '硬件成本-自研',
                    projectCost: parseFloat(res.selfPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 4,
                    quotationItem: '实施',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.impExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.impExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '实施成本',
                    projectCost: parseFloat(res.impPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 5,
                    quotationItem: '其他',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.northQuoteAmount).toFixed(2),
                    externalQuote: parseFloat(res.otherExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.otherExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.totalExtIncome).toFixed(2),
                    projectCostName: '其他成本',
                    projectCost: parseFloat(res.otherPrice).toFixed(2),
                    expectedCost: parseFloat(res.totalCost).toFixed(2),
                    totalProfit: parseFloat(res.totalProfit).toFixed(2),
                    profitMargin: res.costProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                },
                {
                    key: 6,
                    quotationItem: '硬件采购',
                    totalAmount: parseFloat(res.northAmount).toFixed(2),
                    quotedAmount: parseFloat(res.extExtQuote).toFixed(2),
                    externalQuote: parseFloat(res.extExtQuote).toFixed(2),
                    expectedIncome: parseFloat(res.extExtIncome).toFixed(2),
                    totalExpectedIncome: parseFloat(res.extExtIncome).toFixed(2),
                    projectCostName: '硬件-采购',
                    projectCost: parseFloat(res.extPrice).toFixed(2),
                    expectedCost: parseFloat(res.extPrice).toFixed(2),
                    totalProfit: parseFloat(res.extProfit).toFixed(2),
                    profitMargin: res.extProfitRate,
                    totalProfitMargin: res.totalCostProfitRate
                }
            ];
            contractType.value = res.contractType
            signDesc.value = res.signDesc
        } else {
            console.log('失败')
        }
    } catch (error) {
        console.error("接口调用失败", error);
    } finally {
        // loading.value = false;
    }
};

// 监听属性并触发数据更新
watch(() => props.isSummarySheet, (newVal) => {
    if (newVal) {
        fetchData(props.opportunity.id);
    }
});
watch(() => props.isSignDetail, (newVal) => {
    if (newVal) {
        fetchSignDetail(props.selectedSignId);
    }
})

// 签约申请更新对外报价及对应数值
const getMapData = async () => {
    try {
        const response = await getMap();
        if (response.code === 200) {
            mapData.value = response.data;
        } else {
            console.error('Failed to fetch map data', response.errorMessage);
        }
    } catch (error) {
        console.error('Error fetching map data:', error);
    }
};
const calculateTotals = () => {
    // 合计人工成本、产品成本、硬件成本-自研、实施成本、其他成本的项目成本
    const totalProjectCost = data.value.slice(0, 5).reduce((acc, item) => {
        return acc + (Number(item.projectCost) || 0);
    }, 0);

    // 硬件采购的项目成本
    const hardwareProcCost = Number(data.value[5].projectCost) || 0;

    // 更新预计总成本
    data.value[0].expectedCost = totalProjectCost.toFixed(2);
    data.value[5].expectedCost = hardwareProcCost.toFixed(2);

    // 更新“北光报价金额”
    const sumExternalQuote = data.value.reduce(
        (acc, item, index) => index !== 5 ? acc + (Number(item.externalQuote) || 0) : acc,
        0
    );
    const hardwareProcQuote = Number(data.value[5].externalQuote) || 0;

    data.value[0].quotedAmount = sumExternalQuote.toFixed(2);
    data.value[5].quotedAmount = hardwareProcQuote.toFixed(2);

    // 更新“预计税后收入”
    data.value.forEach((item) => {
        const dutyRate = getDutyRateByItemType(item.quotationItem); // 获取对应的 duty rate
        if (dutyRate) {
            item.expectedIncome = ((Number(item.externalQuote) || 0) / (1 + dutyRate)).toFixed(2); // 按照 dutyRate 计算
        }
    });

    // 计算“预计税后总收入”
    const sumExpectedIncome = data.value
        .slice(0, 5) // 软件、产品、硬件-自研、实施、其他
        .reduce((acc, item) => acc + (Number(item.expectedIncome) || 0), 0);
    const hardwareProcExpectedIncome = Number(data.value[5].expectedIncome) || 0;

    data.value[0].totalExpectedIncome = sumExpectedIncome.toFixed(2);
    data.value[5].totalExpectedIncome = hardwareProcExpectedIncome.toFixed(2);

    // 计算“预计利润”
    const totalProfit1 = sumExpectedIncome - totalProjectCost;
    const totalProfit2 = hardwareProcExpectedIncome - hardwareProcCost;

    data.value[0].totalProfit = totalProfit1.toFixed(2);
    data.value[5].totalProfit = totalProfit2.toFixed(2);

    // 计算“成本利润率”列
    data.value.forEach((item, index) => {
        if (index === 0) {
            // 软件、产品等的成本利润率
            if (totalProjectCost !== 0) {
                item.profitMargin = Number(totalProfit1 / totalProjectCost).toFixed(4);
            } else {
                item.profitMargin = 0;
            }
        } else if (index === 5) {
            // 硬件采购的成本利润率
            if (hardwareProcCost > 0) {
                item.profitMargin = Number(totalProfit2 / hardwareProcCost).toFixed(4);
            } else {
                item.profitMargin = 0;
            }
        }
    });

    // 更新“总成本利润率”
    const totalCostSum = totalProjectCost + hardwareProcCost;
    const totalProfitSum = totalProfit1 + totalProfit2;

    data.value[0].totalProfitMargin = totalCostSum !== 0
        ? ((totalProfitSum / totalCostSum)).toFixed(4) + "%"
        : 0;

    const totalNorthQuoteAmount =
        Number(data.value[0].quotedAmount) + Number(data.value[5].quotedAmount);
    data.value[0].totalAmount = totalNorthQuoteAmount.toFixed(2);
};
// 根据 quotationItem 获取对应的 dutyRate
const getDutyRateByItemType = (itemType) => {
    const typeMap = {
        '软件': 'SOFTWARE_DEVELOPMENT',
        '产品': 'PRODUCT',
        '硬件-自研': 'SELF_DEVELOPED',
        '实施': 'IMPLEMENT',
        '其他': 'OTHER',
        '硬件采购': 'EXTERNAL_PROCUREMENT', // 可能需要添加或修改
    };

    const typeKey = typeMap[itemType];
    return mapData.value[typeKey]?.dutyRate || 0; // 返回对应的 dutyRate
};
// 深度监听外部报价的变化并更新
watch(
    () => data.value.map(item => item.externalQuote),
    (newValue) => {
        // 检查 props 的条件  
        if (props.isPriceSheet || props.isReContractApplication) {
            calculateTotals(newValue); // 只有在条件为真时才执行  
        }
    },
    { deep: true }
);

// 更新外部报价
const updateExternalQuote = (index, value) => {
    // 确保 index 在有效范围内  
    if (data.value && index >= 0 && index < data.value.length) {
        const numValue = parseFloat(value);
        if (!isNaN(numValue)) {
            data.value[index].externalQuote = numValue;
        } else {
            data.value[index].externalQuote = null; //可能后续还会更改
        }
        calculateTotals();
    } else {
        console.error(`Index ${index} is out of bounds or data.value is not initialized.`);
    }
};
const isProfitRateLow = computed(() => {
    let profitMargin = String(data.value[0].profitMargin);
    let numericMargin = parseFloat(profitMargin.replace('%', ''));
    if (props.isPriceSheet || props.isReContractApplication || props.isApprovalOfContractSigning) {
        return numericMargin < contractProfit.value; // 确保 contractProfit.value 是数字
    } else {
        return numericMargin < externalProfit.value;
    }
});

// 监听数据变化  
watch(data, () => {
    let profitMargin = String(data.value[0].profitMargin);
    let numericValue = parseFloat(profitMargin.replace('%', ''));
    let contractProfitValue = parseFloat(contractProfit.value);
    let externalProfitValue = parseFloat(externalProfit.value);
    if (props.isPriceSheet || props.isReContractApplication || props.isApprovalOfContractSigning) {
        isProfitRateLow.value = numericValue < contractProfitValue;
    } else {
        isProfitRateLow.value = numericValue < externalProfitValue;
    console.log(isProfitRateLow.value,numericValue,externalProfitValue)

    }
}, {immediate:true});
onMounted(() => {
    getMapData()
});


const handleCancel = () => {
    if (props.isQuotationPendingApproval) {
        resData.value = null
        auditDesc.value = ''
        signDesc.value = ''
        isSaleQuoteOkVisible.value = false;
        emit('close');
    } else if (props.isPriceSheet) {
        auditDesc.value = ''
        signDesc.value = ''
        isSaleQuoteOkVisible.value = false;
        emit('price-sheet-close');
    } else if (props.isApprovalOfContractSigning) {
        auditDesc.value = ''
        signDesc.value = ''
        isSaleQuoteOkVisible.value = false;
        emit('approval-contractsigning-close');
    } else if (props.isReContractApplication) {
        auditDesc.value = ''
        signDesc.value = ''
        isSaleQuoteOkVisible.value = false;
        emit('contract-apply-close');
    } else if (props.isSignDetail) {
        isSaleQuoteOkVisible.value = false;
        contractType.value = '1'
        signDesc.value = ''
        emit('sign-detail-close');
    }
    // loading.value = true
};
const handleApproval = async (params) => {
    try {
        const res = isApproval.value ? await salesApproval(params) : await salesReject(params);
        if (res.code === 200) {
            message.info(isApproval.value ? '审批通过成功！' : '审批已被驳回！');
            auditDesc.value = ''
            isSaleQuoteOkVisible.value = false;
            emit('close');
        } else {
            message.info('接口错误：' + res.errorMessage);
        }
    } catch (error) {
        console.error('处理审批错误:', error);
        message.error('审批处理失败，请稍后重试。');
    }
};
// 签约申请
const handlePriceSheet = async () => {
    // 准备请求数据
    const requestData = {
        contractType: contractType.value,
        costProfitRate: parseFloat(data.value[0].profitMargin) || 0, //成本利润率
        extExtIncome: parseFloat(data.value[5].expectedIncome || 0), // 产品预计收入
        extExtQuote: parseFloat(data.value[5].externalQuote || 0),  // 产品外部报价
        extPrice: parseFloat(data.value[5].projectCost || 0),       // 产品成本
        extProfit: parseFloat(data.value[5].totalProfit || 0),      // 产品利润
        extProfitRate: parseFloat(data.value[5].profitMargin || 0), //外采利润率
        impExtIncome: parseFloat(data.value[3].expectedIncome || 0), // 实施预计收入
        impExtQuote: parseFloat(data.value[3].externalQuote || 0),  // 实施外部报价
        impPrice: parseFloat(data.value[3].projectCost || 0),       // 实施成本
        laborPrice: parseFloat(data.value[0].projectCost || 0),     // 人工成本
        northAmount: parseFloat(data.value[0].totalAmount || 0),    // 北光报价总金额
        northQuoteAmount: parseFloat(data.value[0].quotedAmount || 0), // 北光报价金额
        opportunitiesId: props.opportunity.id,
        opportunitiesParentId: props.opportunity.opportunitiesParentId,
        otherExtIncome: parseFloat(data.value[4].expectedIncome || 0), // 其他预计收入
        otherExtQuote: parseFloat(data.value[4].externalQuote || 0),  // 其他外部报价
        otherPrice: parseFloat(data.value[4].projectCost || 0),       // 其他成本
        prodExtIncome: parseFloat(data.value[1].expectedIncome || 0), // 产品预计收入
        prodExtQuote: parseFloat(data.value[1].externalQuote || 0),  // 产品外部报价
        prodPrice: parseFloat(data.value[1].projectCost || 0),       // 产品成本
        signDesc: signDesc.value,
        selfExtIncome: parseFloat(data.value[2].expectedIncome || 0), // 自研硬件预计收入
        selfExtQuote: parseFloat(data.value[2].externalQuote || 0),  // 自研硬件外部报价
        selfPrice: parseFloat(data.value[2].projectCost || 0),       // 自研硬件成本
        softWareExtIncome: parseFloat(data.value[0].expectedIncome || 0), // 软件预计收入
        softWareExtQuote: parseFloat(data.value[0].externalQuote || 0),  // 软件外部报价
        totalCost: parseFloat(data.value[0].expectedCost || 0),      // 总成本
        totalCostProfitRate: parseFloat(data.value[0].totalProfitMargin) || 0, // 总成本利润率
        totalExtIncome: parseFloat(data.value[0].totalExpectedIncome || 0), // 总收入
        totalProfit: parseFloat(data.value[0].totalProfit || 0),     // 总利润
        signImg: imgTag.value || ''
    };
    try {
        const res = await signApplication(requestData);
        if (res.code === 200) {
            message.info('成功进行签约申请');
            isSaleQuoteOkVisible.value = false;
            signDesc.value = ''
            emit('price-sheet-close');
            emit('contract-apply-close');
        } else {
            message.info('接口错误：' + res.errorMessage);
        }
    } catch (error) {
        console.error('处理价格表错误:', error);
        message.error('价格表处理失败，请稍后重试。');
    }
};

// 签约审批
const handleSignApproval = async (params) => {
    try {
        const res = isFinalApproval.value ? await signApproval(params) : await signReject(params);
        if (res.code === 200) {
            message.info(isFinalApproval.value ? '审批通过成功！' : '审批已被驳回！');
            isSaleQuoteOkVisible.value = false;
            auditDesc.value = ''
            emit('approval-contractsigning-close');
        } else {
            message.info('接口错误：' + res.errorMessage);
        }
    } catch (error) {
        console.error('处理审批错误:', error);
        message.error('审批处理失败，请稍后重试。');
    }
};
const handleOk = async () => {
    if (props.isQuotationPendingApproval) {
        // if (!auditDesc.value.trim()) {
        //     message.warning('审批理由为必填项！');
        //     return;
        // }
        let params = null
        if (props.opportunity.type === 'INCAPABLE') {
            params = {
                opportunitiesId: props.opportunity.id,
                auditDesc: auditDesc.value,
                opportunitiesParentId: props.opportunity.opportunitiesParentId,
                northAmount: props.opportunity.projExtQuoteTotal,
                // totalCost: 0,
                // costProfitRate: 0
            };
        } else {
            params = {
                opportunitiesId: props.opportunity.id,
                auditDesc: auditDesc.value,
                opportunitiesParentId: props.opportunity.opportunitiesParentId,
                northAmount: resData.value.northAmount,
                totalCost: resData.value.totalCost,
                costProfitRate: resData.value.costProfitRate
            };
        }
        await handleApproval(params);

    } else if (props.isPriceSheet) {
        if (signDesc.value === '') {
            message.error('请填写申请签约说明');
            return;
        }
        const base64Image = await captureScreenshot();
        imgTag.value = `<img src="${base64Image}" alt="Screenshot" />`
        // const params = {
        //     contractType: contractType.value,
        //     opportunitiesId: props.opportunity.id,
        //     signDesc: signDesc.value,
        //     opportunitiesParentId: props.opportunity.opportunitiesParentId,
        //     northAmount: resData.value.northAmount,
        //     northQuoteAmount: resData.value.northQuoteAmount,
        //     totalCost: resData.value.totalCost,
        //     costProfitRate: resData.value.costProfitRate,
        //     totalCostProfitRate: resData.value.totalCostProfitRate,
        // };
        handlePriceSheet()
    } else if (props.isApprovalOfContractSigning) {
        const params = {
            opportunitiesId: props.opportunity.id,
            auditDesc: auditDesc.value,
            opportunitiesParentId: props.opportunity.opportunitiesParentId,
            northAmount: resData.value.northAmount,
            totalCost: resData.value.totalCost,
            costProfitRate: resData.value.costProfitRate
        };
        handleSignApproval(params)

    } else if (props.isReContractApplication) {
        if (signDesc.value === '') {
            message.error('请填写申请签约说明');
            return;
        }
        const base64Image = await captureScreenshot();
        imgTag.value = `<img src="${base64Image}" alt="Screenshot" />`
        // const params = {
        //     contractType: contractType.value,
        //     opportunitiesId: props.opportunity.id,
        //     signDesc: signDesc.value,
        //     opportunitiesParentId: props.opportunity.opportunitiesParentId,
        //     northAmount: resData.value.northAmount,
        //     northQuoteAmount: resData.value.northQuoteAmount,
        //     totalCost: resData.value.totalCost,
        //     costProfitRate: resData.value.costProfitRate,
        //     totalCostProfitRate: resData.value.totalCostProfitRate,
        // };
        handlePriceSheet()

    } else if (props.isSignDetail) {
        isSaleQuoteOkVisible.value = false;
        contractType.value = '1'
        signDesc.value = ''
        emit('sign-detail-close');
    }
    // loading.value = true
};
</script>  
  
<style scoped>
.modal-content {
    padding: 20px;
    display: flex;
    flex-wrap: wrap;
}

.modal-item {
    margin-bottom: 10px;
    width: 50%;
    margin-bottom: 10px;
    box-sizing: border-box;
    display: flex;
    align-items: flex-start;
}

.ant-body>>>.ant-table-content {
    position: relative;
    z-index: 999;
    overflow-x: auto;
}

.ant-modal-body {
    height: 90%;
    overflow-y: auto;
}

.ant-row {
    display: flex;
    border: 1px solid #f0f0f0;
    /* 分隔行的边框 */
}

.ant-col {
    padding: 10px;
    /* 为每个单元格添加内边距，使其更像表格 */
}

.ant-table-wrapper .ant-table.ant-table-bordered>.ant-table-title {
    border: 1px solid #f0f0f0;
    border-bottom: 0;
    padding: 0px;
}

.follow-up {
    margin-top: 10px;
}

.contract-application {
    margin-top: 25px;
}

/* 预警 */
.highlight {
    color: red;
    /* 改为红色 */
    font-weight: bold;
    /* 加粗 */
    background-color: #ffcccc;
    /* 可选：背景高亮 */
}

@media (max-width: 768px) {
    .ant-body>>>.ant-table {
        font-size: 12px;
        /* 调整字体大小 */
    }

    .ant-row {
        flex-direction: column;
        /* 列在小屏幕上垂直排列 */
    }

    .ant-col {
        padding: 5px;
        /* 调整单元格内边距 */
        width: 100%;
        /* 使每个单元格宽度为100% */
    }
}
</style>