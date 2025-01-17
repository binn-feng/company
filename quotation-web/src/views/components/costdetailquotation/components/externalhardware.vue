<template>
    <div class="container">
        <a-card>
            <template #title>
                <span class="fs16">硬件-外采 成本明细表</span>
                <a-button type="primary" v-if="props.intoModel !== 'already'" @click="viewDetails(record)" style="margin:20px;">添加</a-button>
            </template>
            <a-table :columns="visibleColumns" :dataSource="data" bordered :pagination="false" size="small">
                <template #bodyCell="{ column, record, index }">
                    <template v-if="column.dataIndex === 'index'">
                        <span>{{ index + 1 }}</span>
                    </template>
                    <template v-else-if="column.dataIndex === 'name'">
                        <div class="hardware-select">
                            <a-select
                                v-model:value="record.name"
                                :options="hardwareOptions" 
                                :bordered="false"
                                show-search
                                listHeight="300"
                                :filter-option="filterOption"
                                :disabled="props.intoModel === 'already'" 
                                :placeholder="props.intoModel === 'already' ? '' : '请选择硬件'"
                                @change="handleHardwareChange(record, index)" :allowClear="!!record.name"
                                @clear="handleHardwareClear(record, index)">
                            </a-select>
                        </div>
                    </template>
                    <template v-else-if="column.dataIndex === 'rate'">
                        <div class="tax-input">
                            <!-- <a-input-number v-model:value="record.rate" @change="() => updateTotalPrice(index)"
                                :precision="2" :disabled="props.intoModel === 'already'" placeholder="输入税率"
                                :controls="false" :step="0.01" style="width: 60px;text-align:center !important" /> -->
                            <a-input-number v-model:value="record.rate" @change="() => updateTotalPrice(index)"
                                :precision="2" :disabled="true" placeholder="输入税率"
                                :controls="false" :step="0.01" style="width: 60px;text-align:center !important" />
                        </div>
                    </template>
                    <template v-else-if="column.dataIndex === 'estimatedCost'">
                        {{ formatNumber(record.estimatedCost) }}
                    </template>
                    <template v-else-if="column.dataIndex === 'externalQuote'">
                        {{ formatNumber(record.externalQuote) }}
                    </template>
                    <a-input-number
                        v-else-if="column.dataIndex !== 'purchaseInquiry' && (column.dataIndex === 'unitPrice' || column.dataIndex === 'number')"
                        v-model:value.number="record[column.dataIndex]" :bordered="false" :controls="false" :precision="2"
                        :step="0.01" @change="() => updateTotalPrice(index)"
                        :placeholder="props.intoModel === 'already' ? '' : '请输入'" :disabled="props.intoModel === 'already'"
                        style="background-color: #f0f0f0;padding:0 11px" />
                    <a-input
                        v-else-if="column.dataIndex !== 'purchaseInquiry' && column.dataIndex !== 'unitPrice' && column.dataIndex !== 'number' && column.dataIndex !== 'type' && column.dataIndex !== 'index'"
                        v-model:value="record[column.dataIndex]" :bordered="false" :controls="false"
                        @change="() => updateTotalPrice(index)" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                        :disabled="props.intoModel === 'already'" />
                    <template v-else-if="column.dataIndex === 'type'">
                        <!-- <a-select v-model:value="record.type" @change="() => updateTotalPrice(index)"
                            :disabled="props.intoModel === 'already'"> -->
                            <a-select v-model:value="record.type" @change="() => updateTotalPrice(index)"
                                :disabled="true">
                            <a-select-option value="ORDINARY">普通发票</a-select-option>
                            <a-select-option value="CATEGORICAL">专用发票</a-select-option>
                            <a-select-option value="INCONCLUSIVE">不确定</a-select-option>
                        </a-select>
                    </template>
                    <template v-else-if="column.dataIndex === 'purchaseInquiry'">
                        {{ formatNumber(record.purchaseInquiry) }}
                    </template>

                </template>
                <template #summary>
                    <a-table-summary fixed>
                        <a-table-summary-row class="total">
                            <a-table-summary-cell :col-span="5">硬件 - 外采 成本小计（元）</a-table-summary-cell>
                            <a-table-summary-cell>{{ formatNumber(totalCost) }}</a-table-summary-cell>
                            <a-table-summary-cell><a-input :bordered="false"></a-input></a-table-summary-cell>
                            <a-table-summary-cell><a-input :bordered="false"></a-input></a-table-summary-cell>
                            <a-table-summary-cell>{{ formatNumber(totalEstimatedlCost)
                            }}</a-table-summary-cell>
                            <a-table-summary-cell>{{ formatNumber(totalExternalquotation)
                            }}</a-table-summary-cell>
                            <a-table-summary-cell><a-input :bordered="false"></a-input></a-table-summary-cell>
                            <a-table-summary-cell><a-input :bordered="false"></a-input></a-table-summary-cell>
                        </a-table-summary-row>
                    </a-table-summary>
                </template>
            </a-table>
            <div style="display: flex; justify-content: end;">
                <a-button type="primary" @click="addRow" style="margin:20px;"
                    v-if="props.intoModel !== 'already'">添加行</a-button>
            </div>
        </a-card>
        <HandleSelectHardware :open="modalVisible" @close="handleClose" :selectedOptions="data"/>
    </div>
</template>

<script setup>
import { ref, computed, defineExpose, defineProps, onMounted, watch, nextTick } from 'vue';
import { anyType } from 'ant-design-vue/es/_util/type';
import { formatNumber } from '@/utils/format'
import { selectHardwareExt } from '@/api/mannageDebug'
import HandleSelectHardware from '../components/HandleSelectHardware.vue';

const props = defineProps({
    purchasingData: anyType,
    intoModel: {
        type: String,
        required: false
    }
});
const hardwareOptions = ref([]);
const selectedHardwares = ref([]);
const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '4%' },
    { title: '名称', dataIndex: 'name', key: 'name', width: '20%' },
    { title: '硬件id', dataIndex: 'hardwareId', key: 'hardwareId', width: '5%', show: false },
    { title: '单位', dataIndex: 'unit', key: 'unit', width: '7%' },
    { title: '单价', dataIndex: 'unitPrice', key: 'unitPrice', width: '7%' },
    { title: '数量', dataIndex: 'number', key: 'number', width: '4%' },
    { title: '采购询价', dataIndex: 'purchaseInquiry', key: 'purchaseInquiry', align: 'right', width: '7%' },
    { title: '发票类型', dataIndex: 'type', key: 'type', width: '10%' },
    { title: '采购发票税率(%)', dataIndex: 'rate', key: 'rate', render: (text) => `${text}%`, width: '3%' },
    { title: '预估成本价', dataIndex: 'estimatedCost', key: 'estimatedCost', width: '8%' },
    { title: '建议对外报价', dataIndex: 'externalQuote', key: 'externalQuote', width: '8%' },
    { title: '设备参数', dataIndex: 'settingParam', key: 'settingParam', width: '13%' },
    { title: '备注', dataIndex: 'remark', key: 'remark', width: '25%' },
]);
const visibleColumns = computed(() => {
    return columns.value.filter(col => col.show !== false);
});
const data = ref(Array.from({ length: 6 }, () => ({
    name: '', hardwareId: '', unit: '', unitPrice: '', number: '',
    purchaseInquiry: 0, type: 'ORDINARY', rate: 0,
    estimatedCost: '0.00', externalQuote: '0.00', settingParam: '', remark: ''
})));

const modalVisible = ref(false)
function viewDetails() {
    modalVisible.value = true
}
function handleClose(items) {
    if (!items) {
        modalVisible.value = false
        return
    } 

    data.value = items
        .map(m => hardwareOptions.value.find(item => item.hardwareId === m))
        .filter(item => item !== undefined);

    data.value.map((m, index) => {
        const selectedHardware = hardwareOptions.value.find(option => option.value === m.value);
        if (selectedHardware) {
            data.value[index].unit = selectedHardware.unit;
            data.value[index].unitPrice = selectedHardware.price;
            data.value[index].rate = selectedHardware.rate;
            data.value[index].remark = selectedHardware.description;
            data.value[index].settingParam = selectedHardware.settingParam;
            data.value[index].hardwareId = selectedHardware.hardwareId;
            data.value[index].name = selectedHardware.label,
            data.value[index].type = selectedHardware.type || 'ORDINARY';
            // 更新已选硬件列表
            updateSelectedHardwares();
            updateTotalPrice(index);
        }
    })
    modalVisible.value = false
}


// 重置表单
const reset = () => {
    data.value = Array.from({ length: 6 }, () => ({
        name: '', hardwareId: '', unit: '', unitPrice: '', number: '',
        purchaseInquiry: 0, type: 'ORDINARY', rate: 0,
        estimatedCost: '0.00', externalQuote: '0.00', settingParam: '', remark: ''
    }))
}
watch(() => props.purchasingData, (newVal) => {
    if (newVal) {
        data.value = newVal.quoteOpportunitiesExternalList
    } else {
        reset();
    }
}, { immediate: true });
// 获取硬件-自研下拉列表
const fetchHardwareOptions = async () => {
    try {
        const params = {
            status: "1", // 只取有效的硬件
        };
        const response = await selectHardwareExt(params);
        if (response) {
            hardwareOptions.value = response.map((hardware) => ({
                label: hardware.name + hardware.spec,
                value: hardware.id,
                name: hardware.name,
                unit: hardware.unit || '默认单位',
                price: hardware.unitPrice,
                description: hardware.remark,
                settingParam: hardware.deviceDescription,
                hardwareId: hardware.id,
                rate: hardware.rate,
                type: hardware.type || 'ORDINARY'
            }));
        }
    } catch (error) {
        console.error('获取硬件列表失败:', error);
    }
};
const filterOption = (input, option) => {
  return option.name.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
// 处理硬件名称变化
const handleHardwareChange = (record, index) => {
    const selectedHardware = hardwareOptions.value.find(option => option.value === record.name);
    if (selectedHardware) {
        record.unit = selectedHardware.unit;
        record.unitPrice = selectedHardware.price;
        record.remark = selectedHardware.description;
        record.settingParam = selectedHardware.settingParam;
        record.hardwareId = selectedHardware.hardwareId;
        record.rate = selectedHardware.rate;
        record.type = selectedHardware.type || 'ORDINARY';
        // 更新已选硬件列表
        updateSelectedHardwares();
        updateTotalPrice(index);
    }
};
const handleHardwareClear = (record, index) => {
    record.name = null;
    record.unit = '';
    record.unitPrice = '';
    record.remark = '';
    record.amount = 0;
    record.settingParam=''
    record.hardwareId = ''
    record.rate = 0
    record.type = 'ORDINARY'
    record.number= ''
    updateSelectedHardwares
    updateTotalPrice(index);
};

const updateSelectedHardwares = () => {
    // 获取当前表中所有已选择的硬件
    selectedHardwares.value = data.value
        .map(row => row.hardwareId)
        .filter(hardwareId => hardwareId);
    hardwareOptions.value.forEach(option => {
        option.disabled = selectedHardwares.value.includes(option.hardwareId);
    });
};
watch(() => data.value, () => {
    updateSelectedHardwares();
}, { deep: true });
const updateTotalPrice = (index) => {
    const row = data.value[index];
    const unitPrice = parseFloat(row.unitPrice);
    const number = parseFloat(row.number);
    const type = row.type;
    const rateRate = parseFloat(row.rate) / 100;

    if (!isNaN(unitPrice) && !isNaN(number) && !isNaN(rateRate)) {
        row.purchaseInquiry = unitPrice * number;

        let estimatedCosts = 0; // 默认值为0  
        let externalQuote = 0; // 默认值为0  

        if (type === 'CATEGORICAL') {
            estimatedCosts = (unitPrice / (1 + rateRate)) * number;
            externalQuote = estimatedCosts * 1.13; // 预估成本价*1.13  
        } else if (type === 'ORDINARY' || type === 'INCONCLUSIVE') {
            estimatedCosts = parseFloat(row.purchaseInquiry); // 采购询价  
            externalQuote = estimatedCosts * 1.13; // 预估成本价*1.13  
        } else { // 不确定则保持原值  
            estimatedCosts = 0; // 可以设置为0  
            externalQuote = 0; // 可以设置为0  
        }

        // 确保有正确的数字格式化  
        row.estimatedCost = Number(estimatedCosts);
        row.externalQuote = Number(externalQuote);
    } else {
        row.purchaseInquiry = '0.00';
        row.estimatedCost = '0.00';
        row.externalQuote = '0.00';
    }
};

const totalCost = computed(() => {
    return data.value.reduce((sum, row) => sum + parseFloat(row.purchaseInquiry || 0), 0);
});

const totalEstimatedlCost = computed(() => {
    const cost = data.value.reduce((sum, row) => sum + parseFloat(row.estimatedCost || 0), 0);
    return parseFloat(cost);
});
const totalExternalquotation = computed(() => {
    return data.value.reduce((sum, row) => sum + parseFloat(row.externalQuote || 0), 0);
});
const addRow = () => {
    data.value.push({ name: '', unit: '', unitPrice: '', number: '', purchaseInquiry: 0, type: 'ORDINARY', rate: 0 , estimatedCost: '0.00', externalQuote: '0.00', settingParam: '', remark: '' });
};

// 传参
const getPurchasingData = () => {
    const requestData = data.value.filter(record => {
        return (
            (record.name && record.name.trim() !== '') ||
            (record.unit && record.unit.trim() !== '') ||
            (record.unitPrice !== undefined && record.unitPrice !== null && record.unitPrice.toString().trim() !== '') || // Ensure string check  
            (record.number !== undefined && record.number !== null && record.number.toString().trim() !== '') || // Ensure string check  
            (record.settingParam && record.settingParam.trim() !== '') ||
            (record.remark && record.remark.trim() !== '')
        );
    });
    return requestData;
};
const getTotal = () => {
    return totalEstimatedlCost
}
defineExpose({ getPurchasingData, reset, getTotal });
onMounted(() => {
    nextTick(() => {
        fetchHardwareOptions()
        updateSelectedHardwares();
    })
});
</script>

<style scoped>
.container {
    height: 100%;
    padding: 20px;
    overflow: auto;
}

.container>>>.ant-card-body {
    padding: 0px !important;
    z-index: 999;
    overflow: auto;
}

.total-row {
    margin-top: 20px;
    font-weight: bold;
}

.total {
    font-weight: bold;
    font-size: 16px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.container>>>.ant-input {
    padding: 0;
    z-index: 999;
}

.container>>>.ant-input-number-input {
    padding: 0;
    z-index: 999;
}

.tax-input>>>.ant-input-number-input {
    text-align: center;
    z-index: 999;
}

.hardware-select>>>.ant-select {
    width: 100%;
}
</style>