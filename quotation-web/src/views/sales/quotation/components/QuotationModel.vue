<template>
    <a-modal :open="isDownloadQuoteModelVisible" title="下载报价单" @cancel="handleCancel" @ok="handleOk" width="60%">
        <div class="radio-buttons">
            <h2>报价单</h2>
            <a-radio-group v-model:value="quoteType" :disabled="isQuoteTypeDisabled" @change="handleQuoteTypeChange">
                <a-radio :value="'nl'">北光报价单</a-radio>
                <a-radio :value="'xx'">欣象报价单</a-radio>
            </a-radio-group>
        </div>
        <div class="pricing-report">
            <div class="table">
                <table>
                    <thead>
                        <tr class="header-title">
                            <th colspan="5" class="title-name">{{ quoteTitle }}</th>
                        </tr>
                        <tr class="title-content">
                            <th colspan="2">客户名称：</th>
                            <th colspan="3" class="text-content">{{ opportunity.customersName }}</th>
                        </tr>
                        <tr class="title-content">
                            <th colspan="2">项目名称：</th>
                            <th class="text-content">{{ opportunity.name }}</th>
                            <th colspan="">报价人：</th>
                            <th colspan="" class="text-content">{{ opportunity.saleName }}</th>
                        </tr>
                        <tr class="title-content">
                            <th colspan="2">报价日期：</th>
                            <th class="text-content">{{ date }}</th>
                            <th colspan="">联系电话：</th>
                            <th colspan="" class="text-content"></th>
                        </tr>
                        <tr class="table-title">
                            <th colspan="5" class="title-name">一、软件费用</th>
                        </tr>
                        <tr>
                            <th>序号</th>
                            <th>项目名称</th>
                            <th>描述</th>
                            <th>单项报价</th>
                            <th>备注</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td class="text-content">软件开发</td>
                            <td></td>
                            <td class="text-content">{{ formatNumber(softwareDevelopmentQuote) }}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3">本项合计：</td>
                            <td>{{ formatNumber(softwareDevelopmentQuote) }}</td>
                            <td></td>
                        </tr>

                        <tr class="table-title">
                            <th colspan="5" class="title-name">二、硬件费用</th>
                        </tr>
                        <tr>
                            <th>序号</th>
                            <th>项目名称</th>
                            <th>描述</th>
                            <th>单项报价</th>
                            <th>备注</th>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td class="text-content">硬件费用</td>
                            <td></td>
                            <td class="text-content">{{ formatNumber(hardwareCostQuote) }}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3">本项合计：</td>
                            <td>{{ formatNumber(hardwareCostQuote) }}</td>
                            <td></td>
                        </tr>

                        <tr class="table-title">
                            <th colspan="5" class="title-name">三、实施费用</th>
                        </tr>
                        <tr>
                            <th>序号</th>
                            <th>项目名称</th>
                            <th>描述</th>
                            <th>单项报价</th>
                            <th>备注</th>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td class="text-content">实施费用</td>
                            <td></td>
                            <td class="text-content">{{ formatNumber(implementationCostQuote) }}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3">本项合计：</td>
                            <td>{{ formatNumber(implementationCostQuote) }}</td>
                            <td></td>
                        </tr>

                            <tr class="table-title" v-if="quoteType === 'xx'">
                                <th colspan="5" class="title-name">四、其他费用</th>
                            </tr>
                            <tr v-if="quoteType === 'xx'">
                                <th>序号</th>
                                <th>项目名称</th>
                                <th>描述</th>
                                <th>单项报价</th>
                                <th>备注</th>
                            </tr>
                            <tr v-if="quoteType === 'xx'">
                                <td>1</td>
                                <td class="text-content">其他费用</td>
                                <td></td>
                                <td class="text-content">{{ formatNumber(xxAdditionalQuote) }}</td>
                                <td></td>
                            </tr>
                            <tr v-if="quoteType === 'xx'">
                                <td colspan="3">本项合计：</td>
                                <td>{{ formatNumber(xxAdditionalQuote) }}</td>
                                <td></td>
                            </tr> 
                   

                        <tr class="total">
                            <td colspan="3">总报价（人民币：元）：</td>
                            <td colspan="2">{{ formatNumber(calculateTotal) }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <template #footer>
            <a-button @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleOk">下载</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { defineEmits, defineProps, watch, ref, computed } from 'vue';
import { formatNumber } from '@/utils/format'
import { getSalesVersion, getSalesDetailInfoById } from '@/api/saleQoute'
import { download } from '@/utils/common.js'
// import { saveAs } from 'file-saver';

const emit = defineEmits([]);
const props = defineProps({
    opportunity: {
        type: Object,
        required: true
    },
    isDownloadQuoteModelVisible: {
        type: Boolean,
        required: true
    }
});
const saleQouteVersion = ref(null)
const softwareDevelopmentQuote = ref(0)
const xxSoftwareDevelopmentQuote = ref(0)
const nlSoftwareDevelopmentQuote = ref(0)
const hardwareCostQuote = ref(0)
const xxHardwareCostQuote = ref(0)
const nlHardwareCostQuote = ref(0)
const implementationCostQuote = ref(0)
const xxImplementationCostQuote = ref(0)
const nlImplementationCostQuote = ref(0)
const xxAdditionalQuote = ref(0)

const quoteData = ref(null)
const quoteType = ref(null);
const signType = ref(null)
const isQuoteTypeDisabled = ref(false);
const date = ref('')
const quoteTitle = computed(() => {
    return quoteType.value === 'nl' ? '北光科技报价单' : '欣象科技报价单';
});
const calculateTotal = computed(() => {
    if (quoteType.value === 'nl') {  
        return softwareDevelopmentQuote.value + hardwareCostQuote.value + implementationCostQuote.value;  
    } else if (quoteType.value === 'xx') {  
        return softwareDevelopmentQuote.value + hardwareCostQuote.value + implementationCostQuote.value + xxAdditionalQuote.value;  
    } else {  
        return 0; 
    }
});

watch(() => props.isDownloadQuoteModelVisible, (newVal) => {
    console.log(newVal, 'newVal')
    if (newVal) {
        const versionParams = {
            latest: true,
            opportunitiesId: props.opportunity.id,
        }
        getSalesVersionFunc(versionParams)
    }
});

// 获取最新报价版本
const getSalesVersionFunc = async (params) => {
    try {
        // 调用 getVersion 接口
        const response = await getSalesVersion(params);
        if (response && response.code === 200) {
            saleQouteVersion.value = response.data;
            date.value = response.data[0].date
            getSalesDetailInfoFunc(response.data[0].id)
        } else {
            console.error('获取版本信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    }
};
const getSalesDetailInfoFunc = async (id) => {
    try {
        const response = await getSalesDetailInfoById({ id: id });
        if (response && response.code === 200) {
            quoteData.value = response.data
            nlSoftwareDevelopmentQuote.value = response.data.preExtQuote + response.data.devExtQuote + response.data.prodExtQuote + response.data.otherExtQuote
            nlHardwareCostQuote.value = response.data.selfExtQuote + response.data.extExtQuote
            nlImplementationCostQuote.value = response.data.impExtQuote
            xxSoftwareDevelopmentQuote.value = response.data.xxSoftWareQuoteTotal
            xxHardwareCostQuote.value = response.data.xxHardWareQuoteTotal
            xxImplementationCostQuote.value = response.data.xxImpQuote
            xxAdditionalQuote.value = response.data.xxAdditionalQuote
            signType.value = response.data.signType
            softwareDevelopmentQuote.value = nlSoftwareDevelopmentQuote.value;
            hardwareCostQuote.value = nlHardwareCostQuote.value
            implementationCostQuote.value = nlImplementationCostQuote.value
            if (signType.value === '1') {
                quoteType.value = 'xx'
                isQuoteTypeDisabled.value = true;
            } else if (signType.value === '2') {
                quoteType.value = 'nl'
                isQuoteTypeDisabled.value = true; // 禁用
            }
        } else {
            console.error('获取信息失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching version data:', error);
    } finally {
        // loading.value = false;
    }
};
const handleQuoteTypeChange = () => {
    if (quoteType.value === 'nl') {
        softwareDevelopmentQuote.value = nlSoftwareDevelopmentQuote.value;
        hardwareCostQuote.value = nlHardwareCostQuote.value
        implementationCostQuote.value = nlImplementationCostQuote.value
    } else {
        softwareDevelopmentQuote.value = xxSoftwareDevelopmentQuote.value;
        hardwareCostQuote.value = xxHardwareCostQuote.value
        implementationCostQuote.value = xxImplementationCostQuote.value
    }
};
function handleOk() {
    const quoteDataToExport = {
        opportunityId: props.opportunity.id,
        subject: props.opportunity.name,
        customerName: props.opportunity.customersName,
        date: date.value,
        remark: '',
        salesName: props.opportunity.saleName,
        salesPhone: '',
        softwareDevelopmentQuote: softwareDevelopmentQuote.value,
        hardwareCostQuote: hardwareCostQuote.value,
        implementationCostQuote: implementationCostQuote.value,
        totalQuote: calculateTotal.value,
        isBeiGuangQuote: quoteType.value === 'nl'
    };
    if (quoteType.value !== 'nl') {  
        quoteDataToExport.xxAdditionalQuote = xxAdditionalQuote.value;  
    }  
    download('quote/opportunities/quotation/export', quoteDataToExport, `${quoteDataToExport.subject}-报价单.xlsx`)
    // const url = `${process.env.VUE_APP_BASE_API}quote/opportunities/quotation/export`;
}


function handleCancel() {
    emit('close');
}
</script>

<style lang='less' scoped>
.header-title th {
    background-color: #f5d180;
    text-align: center;
    font-weight: 900;
    font-size: 18px;
}

.radio-buttons {
    margin-bottom: 15px;
}

.customer-info {
    margin-bottom: 20px;
}

.table {
    margin-top: 20px;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

th,
td {
    border: 1px solid #000;
    padding: 8px;
    text-align: left;
}

.title-content th {
    font-size: 14px;
    font-weight: 400;
}

.table-title th {
    background-color: #f5d180;
}

.title-name {
    color: blue
}

.text-content {
    color: rgb(26, 128, 187)
}

.total {
    background-color: yellow;
    font-weight: bold;
    padding: 10px;
    text-align: right;
}

/* 调整表格宽度 */
th:nth-child(1),
td:nth-child(1) {
    width: 60px;
}

th:nth-child(5),
td:nth-child(5) {
    width: 200px;
}

th:nth-child(n+2):nth-child(-n+4),
td:nth-child(n+2):nth-child(-n+4) {
    width: 150px;
}
</style>
