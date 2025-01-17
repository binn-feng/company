<template>
    <div class="container">
        <a-card>
            <template #title>
                <span class="fs16">{{ title }}</span>
            </template>
            <a-table :columns="columns" :dataSource="data" bordered :pagination="false" size="small">
                <template #bodyCell="{ column, record, index }">
                    <template v-if="column.dataIndex === 'index'">
                        <span>{{ index + 1 }}</span>
                    </template>
                    <!-- 产品名称下拉框 -->
                    <template v-else-if="column.dataIndex === 'name' && props.tableType === 'product'">
                        <a-select v-model:value="record.name" :options="productOptions" :bordered="false"
                            :disabled="props.intoModel === 'already'" placeholder="请选择产品"
                            show-search
                            listHeight="300"
                            :filter-option="filterProductOption"
                            @change="handleProductChange(record, index)" allowClear
                            @clear="handleProductClear(record, index)">
                        </a-select>
                    </template>
                    <!-- 硬件自研名称下拉框 -->
                    <template v-else-if="column.dataIndex === 'name' && props.tableType === 'self'">
                        <a-select v-model:value="record.name" :options="hardwareOptions" :bordered="false"
                            :disabled="props.intoModel === 'already'" placeholder="请选择硬件"
                            show-search
                            listHeight="300"
                            :filter-option="filterHardwareOption"
                            @change="handleHardwareChange(record, index)" :allowClear="!!record.name"
                            @clear="handleHardwareClear(record, index)">
                        </a-select>
                    </template>
                    <template
                        v-else-if="(column.dataIndex === 'unit' || column.dataIndex === 'unitPrice') && (props.tableType === 'product' || props.tableType === 'self')">
                        <a-tooltip v-if="!record.name" content="选择产品后将自动填写">
                            <template #title>选择后将自动填写</template>
                            <span style="color: rgba(0, 0, 0, 0.25);">无</span>
                        </a-tooltip>
                        <template v-else>
                            <span v-if="column.dataIndex === 'unit'">{{ record.unit || '无单位' }}</span>
                            <span v-if="column.dataIndex === 'unitPrice'">{{ formatNumber(record.unitPrice) || '无单价'
                            }}</span>
                        </template>
                    </template>
                    <template v-else-if="['unitPrice', 'number'].includes(column.dataIndex)">
                        <a-input-number v-model:value="record[column.dataIndex]" :bordered="false" :controls="false"
                            @change="() => updateTotalPrice(index)"
                            :placeholder="props.intoModel === 'already' ? '' : '请输入'" :step='0.01' :precision="2"
                            :disabled="props.intoModel === 'already'" style="background-color: #f0f0f0;" />
                    </template>
                    <template v-else-if="column.dataIndex === 'amount'">
                        <span>{{ formatNumber(record.amount) }}</span>
                    </template>
                    <template v-else>
                        <a-input v-model:value="record[column.dataIndex]" :bordered="false"
                            :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                            :disabled="props.intoModel === 'already'" />
                    </template>
                </template>
                <template #summary>
                    <a-table-summary fixed>
                        <a-table-summary-row class="total">
                            <a-table-summary-cell :col-span="5">{{ title }}小计（元）:</a-table-summary-cell>
                            <!-- <a-table-summary-cell>{{ totalCost }}</a-table-summary-cell>
                            <a-table-summary-cell><a-input :bordered="false"></a-input></a-table-summary-cell> -->
                            <a-table-summary-cell :col-span="props.tableType === 'self' ? 3 : 2">
                                {{ formatNumber(totalCost) }}
                            </a-table-summary-cell>
                        </a-table-summary-row>
                    </a-table-summary>
                </template>
            </a-table>
            <div style="padding: 20px;" v-if="props.intoModel !== 'already'">
                <a-button type="primary" @click="addRow">添加行</a-button>
            </div>
        </a-card>
    </div>
</template>
  
<script setup>
import { ref, computed, defineProps, watch, onMounted, defineExpose, nextTick } from 'vue';
import { formatNumber } from '@/utils/format'
import { getProductChildrenPage, selectHardwareSelf } from '@/api/mannageDebug'

const props = defineProps({
    tableType: String,  // 用于区分是“硬件-自研”还是“其他”
    dataSource: Array,
    productCostsData: Object,
    selfInnovateData: Object,
    otherData: Object,
    intoModel: {
        type: String,
        required: false
    }
});

const data = ref([]);
const columns = ref([]);
const title = ref('');
const hardwareOptions = ref([]);
const productOptions = ref([]);  // 下拉框选项
const selectedProducts = ref([]); // 跟踪已选择的产品
const selectedHardwares = ref([]);
const setColumns = () => {
    columns.value = [
        { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '4%' },
        { title: '名称', dataIndex: 'name', key: 'name', width: '20%', align: props.tableType === 'product' ? 'center' : '' },
        { title: '产品id', dataIndex: 'productId', key: 'productId', width: '5%', show: false },
        { title: '硬件id', dataIndex: 'hardwareId', key: 'hardwareId', width: '5%', show: false },
        { title: '单位', dataIndex: 'unit', key: 'unit', width: '5%', },
        { title: '单价', dataIndex: 'unitPrice', key: 'unitPrice', width: '6%', },
        { title: '数量', dataIndex: 'number', key: 'number', width: '3%', },
        { title: '总价', dataIndex: 'amount', key: 'amount', align: 'right', width: '10%', },
        { title: '设备参数', dataIndex: 'settingParam', key: 'settingParam', show: props.tableType === 'self', width: '15%' },  // 仅在硬件-自研表显示
        { title: '备注', dataIndex: 'remark', key: 'remark', width: '30%' },
    ].filter(col => col.show !== false);
};

const hardwareData = ref([...Array(6)].map(() => ({
    name: '',
    unit: '',
    unitPrice: '',
    number: '',
    amount: 0,
    settingParam: '',
    remark: '',
    hardwareId: ''
})));
const otherData = ref([...Array(6)].map(() => ({
    name: '',
    unit: '',
    unitPrice: '',
    number: '',
    amount: 0,
    remark: ''
})));
const productData = ref([...Array(6)].map(() => ({
    name: null,
    unit: '',
    unitPrice: '',
    number: '',
    amount: 0,
    remark: '',
    productId: ''
})));

// 监听
watch(() => props.productCostsData, (newVal) => {

    if (newVal !== null) {
        productData.value = newVal.quoteOpportunitiesProductList
        setInitialData()
    } else {
        setInitialData()
    }
});
watch(() => props.selfInnovateData, (newVal) => {

    if (newVal !== null) {
        hardwareData.value = newVal.quoteOpportunitiesSelfList
        setInitialData()
    } else {
        setInitialData()
    }
});
watch(() => props.otherData, (newVal) => {

    if (newVal && newVal.quoteOpportunitiesOtherList && Array.isArray(newVal.quoteOpportunitiesOtherList)) {
        // 先将 quoteOpportunitiesOtherList 的数据赋值给 data  
        const list = newVal.quoteOpportunitiesOtherList;
        // 如果数量不足 6 项，填充空数据  
        const filledData = list.length < 6 ?
            [...list, ...Array.from({ length: 6 - list.length }, () => ({
                serialNumber: '',
                name: '',
                unit: '',
                unitPrice: '',
                number: '',
                amount: '',
                remark: '',
                productId: '',
                hardwareId: ''
            }))] : list;
        otherData.value = filledData;
        setInitialData()
    } else {
        setInitialData()
    }
});
const filterProductOption = (input, option) => {
  return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
const filterHardwareOption = (input, option) => {
  return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
// 获取硬件-自研下拉列表
const fetchHardwareOptions = async () => {
    try {
        const params = {
            status: "1", // 只取有效的硬件
        };
        const response = await selectHardwareSelf(params);
        if (response) {
            hardwareOptions.value = response.map((hardware) => ({
                label: hardware.name,
                value: hardware.name,
                unit: hardware.unit || '默认单位',
                price: hardware.unitPrice,
                description: hardware.remark,
                deviceDescription: hardware.deviceDescription,
                hardwareId: hardware.id,
            }));
        }
    } catch (error) {
        console.error('获取硬件列表失败:', error);
    }
};
// 获取产品列表
const fetchProductOptions = async () => {
    try {
        const params = {
            status: "1"
        };
        const response = await getProductChildrenPage(params);
        if (response.code === 200) {
            productOptions.value = response.rows.map(product => ({
                label: product.nameAndStatus,
                value: product.nameAndStatus,
                unit: product.unit || '默认单位',
                price: product.costPrice,
                description: product.description,
                productId: product.id
            }));
        }
    } catch (error) {
        console.error('获取产品列表失败:', error);
    }
};
const handleProductClear = (record, index) => {
    // 清空相关字段
    record.name = null;
    record.unit = '';
    record.unitPrice = '';
    record.remark = '';
    record.amount = 0;
    record.productId = ''

    // 更新已选产品列表
    updateSelectedProducts();

    // 更新总价
    updateTotalPrice(index);
};
// 清空硬件选择时的处理
const handleHardwareClear = (record, index) => {
    record.name = null;
    record.unit = '';
    record.unitPrice = '';
    record.remark = '';
    record.amount = 0;
    record.hardwareId = ''
    record.settingParam = ''
    updateSelectedHardwares
    updateTotalPrice(index);
};

// 当选择产品时更新单价和单位
const handleProductChange = (record, index) => {
    const selectedProduct = productOptions.value.find(option => option.value === record.name);
    if (selectedProduct) {
        record.unit = selectedProduct.unit;
        record.unitPrice = selectedProduct.price;
        record.remark = selectedProduct.description

        record.productId = selectedProduct.productId
        // 更新已选产品列表
        updateSelectedProducts();
        updateTotalPrice(index);  // 计算总价
    }
};
// 处理硬件名称变化
const handleHardwareChange = (record, index) => {
    const selectedHardware = hardwareOptions.value.find(option => option.value === record.name);
    if (selectedHardware) {
        record.unit = selectedHardware.unit;
        record.unitPrice = selectedHardware.price;
        record.remark = selectedHardware.remark;
        record.hardwareId = selectedHardware.hardwareId;
        record.settingParam = selectedHardware.deviceDescription

        // 更新已选硬件列表
        updateSelectedHardwares();
        updateTotalPrice(index);
    }
};
const updateSelectedProducts = () => {
    // 获取当前表中所有已选择的产品的 productId
    selectedProducts.value = productData.value
        .map((row) => row.productId)  // 改为根据 productId 筛选
        .filter((productId) => productId);  // 只保留有有效 productId 的项

    // 动态更新 productOptions 的 disabled 属性
    productOptions.value.forEach((option) => {
        option.disabled = selectedProducts.value.includes(option.productId);  // 通过 productId 禁用
    });
};

const updateSelectedHardwares = () => {
    // 获取当前表中所有已选择的硬件的 hardwareId
    selectedHardwares.value = hardwareData.value
        .map(row => row.hardwareId)  // 改为根据 hardwareId 筛选
        .filter(hardwareId => hardwareId);  // 只保留有有效 hardwareId 的项

    // 动态更新 hardwareOptions 的 disabled 属性
    hardwareOptions.value.forEach(option => {
        option.disabled = selectedHardwares.value.includes(option.hardwareId);  // 通过 hardwareId 禁用
    });
};

// 监听数据变化，更新已选产品和初始数据
watch(
    () => productData.value,
    () => {
        updateSelectedProducts();
    },
    { deep: true }
);
// 监听硬件数据变化
watch(() => hardwareData.value, () => {
    updateSelectedHardwares();
}, { deep: true });

const productTotal = computed(() => {
    return productData.value.reduce((sum, row) => sum + parseFloat(row.amount || 0), 0);
});

const selfTotal = computed(() => {
    return hardwareData.value.reduce((sum, row) => sum + parseFloat(row.amount || 0), 0);
});

const otherTotal = computed(() => {
    return otherData.value.reduce((sum, row) => sum + parseFloat(row.amount || 0), 0);
});

const setInitialData = () => {
    hardwareData.value = (props.selfInnovateData && props.selfInnovateData.quoteOpportunitiesSelfList)
        ? [...props.selfInnovateData.quoteOpportunitiesSelfList]
        : hardwareData.value;
    otherData.value = (props.otherData && props.otherData.quoteOpportunitiesOtherList)
        ? [...props.otherData.quoteOpportunitiesOtherList]
        : otherData.value;
    productData.value = (props.productCostsData && props.productCostsData.quoteOpportunitiesProductList)
        ? [...props.productCostsData.quoteOpportunitiesProductList]
        : productData.value;
    // 检查每个数据的长度，如果少于 6 行，填充空数据
    const fillDataToSix = (dataList) => {
        const missingRows = 6 - dataList.length;
        if (missingRows > 0) {
            const emptyRows = Array.from({ length: missingRows }, () => ({
                name: '',
                unit: '',
                unitPrice: '',
                number: '',
                amount: 0,
                settingParam: '',
                remark: '',
                product: '',
                productId: '',
                hardwareId: ''
            }));
            return [...dataList, ...emptyRows];
        }
        return dataList;
    };
    // 更新硬件数据、其他数据和产品数据
    hardwareData.value = fillDataToSix(hardwareData.value);
    otherData.value = fillDataToSix(otherData.value);
    productData.value = fillDataToSix(productData.value);
    if (props.tableType === 'self') {
        title.value = '硬件小计-自研';
        data.value = hardwareData.value;
        // SelfTotal.value= totalCost
    } else if (props.tableType === 'other') {
        title.value = '其他';
        data.value = otherData.value;
        // OtherTotal.value = totalCost
    } else if (props.tableType === 'product') {
        title.value = '产品平台';
        data.value = productData.value;
        // ProductTotal.value = totalCost
    }
    setColumns();
};
watch(() => data, (newVal) => {
    if (props.tableType === 'self') {
        if (newVal) {
            hardwareData.value = newVal.value
        } else {
            hardwareData.value = (props.selfInnovateData && props.selfInnovateData.quoteOpportunitiesSelfList)
                ? [...props.selfInnovateData.quoteOpportunitiesSelfList]
                : hardwareData.value;
        }
    } else if (props.tableType === 'other') {
        if (newVal) {
            otherData.value = newVal.value
        } else {
            otherData.value = (props.otherData && props.otherData.quoteOpportunitiesOtherList)
                ? [...props.otherData.quoteOpportunitiesOtherList]
                : otherData.value;
        }

    } else if (props.tableType === 'product') {
        if (newVal) {
            productData.value = newVal.value
        } else {
            productData.value = (props.productCostsData && props.productCostsData.quoteOpportunitiesProductList)
                ? [...props.productCostsData.quoteOpportunitiesProductList]
                : productData.value;
        }
    }
}, { deep: true });


const updateTotalPrice = (index) => {
    const row = data.value[index];
    const unitPrice = parseFloat(row.unitPrice);
    const number = parseFloat(row.number);
    row.amount = !isNaN(unitPrice) && !isNaN(number) ? (unitPrice * number).toFixed(2) : '0.00';
};

const totalCost = computed(() => {
    return data.value.reduce((sum, row) => sum + parseFloat(row.amount || 0), 0).toFixed(2);
});

const addRow = () => {
    if (props.tableType === 'self') {
        // 为硬件-自研类型添加一行，包括设备参数  
        data.value.push({
            name: '',
            unit: '',
            unitPrice: '',
            number: '',
            amount: 0,
            settingParam: '',
            remark: '',
            hardwareId: ''
        });
    } else {
        // 为其他类型添加一行，不包括设备参数  
        data.value.push({
            name: null,
            unit: '',
            unitPrice: '',
            number: '',
            amount: 0,
            remark: '',
            product: '',
            productId: ''
        });
    }
};

watch(() => props.dataSource, (newVal) => {
    data.value = newVal || [];
});
onMounted(() => {
    nextTick(() => {
        fetchHardwareOptions()
        fetchProductOptions()
        setInitialData();
        updateSelectedProducts();
    })
});

watch(() => props.tableType, setInitialData);

// 重置表单
const reset = () => {
    otherData.value = Array.from({ length: 6 }, () => ({
        serialNumber: '',
        name: '',
        unit: '',
        unitPrice: '',
        number: '',
        amount: '',
        remark: ''
    }));
    productData.value = Array.from({ length: 6 }, () => ({
        serialNumber: '',
        name: '',
        unit: '',
        unitPrice: '',
        number: '',
        amount: '',
        remark: '',
        product: '',
        productId: ''
    }));
    hardwareData.value = Array.from({ length: 6 }, () => ({
        name: '',
        unit: '',
        unitPrice: '',
        number: '',
        amount: 0,
        settingParam: '',
        remark: '',
        hardwareId: ''
    }));
    setInitialData();
}

// 传参
const isDefinedAndNotEmpty = (value) => {
    return value !== undefined && value !== null &&
        typeof value !== 'function' && // 确保不是一个函数  
        value.toString().trim() !== '';
};

const getSelfData = () => {
    const requestSelfData = hardwareData.value.filter(record =>
        isDefinedAndNotEmpty(record.name) ||
        isDefinedAndNotEmpty(record.unit) ||
        isDefinedAndNotEmpty(record.unitPrice) ||  // 确保字符串检查  
        isDefinedAndNotEmpty(record.number) ||      // 确保字符串检查  
        isDefinedAndNotEmpty(record.remark) ||
        isDefinedAndNotEmpty(record.settingParam)
    );
    return requestSelfData;
};

const getProductData = () => {
    const requestProductData = productData.value.filter(record =>
        isDefinedAndNotEmpty(record.name) ||
        isDefinedAndNotEmpty(record.unit) ||
        isDefinedAndNotEmpty(record.unitPrice) ||  // 确保字符串检查  
        isDefinedAndNotEmpty(record.number) ||      // 确保字符串检查  
        isDefinedAndNotEmpty(record.remark) ||
        isDefinedAndNotEmpty(record.settingParam)
    );
    return requestProductData;
};

const getOtherData = () => {
    const requestOtherData = otherData.value.filter(record =>
        isDefinedAndNotEmpty(record.name) ||
        isDefinedAndNotEmpty(record.unit) ||
        isDefinedAndNotEmpty(record.unitPrice) ||  // 确保字符串检查  
        isDefinedAndNotEmpty(record.number) ||      // 确保字符串检查  
        isDefinedAndNotEmpty(record.remark)
    );
    return requestOtherData;
};
const getProductTotal = () => {
    return productTotal
}
const getSelfTotal = () => {
    return selfTotal
}
const getOtherTotal = () => {
    return otherTotal
}
defineExpose({ getSelfData, getProductData, getOtherData, reset, getProductTotal, getSelfTotal, getOtherTotal });

</script>
  
<style scoped>
.container {
    padding: 20px;
    overflow: auto;
}

.container>>>.ant-card-body {
    padding: 0px !important;
    z-index: 999;
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

.container>>>.ant-select {
    width: 100%;
}
</style>
  