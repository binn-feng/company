<template>
    <div class="container">
        <a-card>
            <template #title>
                <span class="fs16">实施成本明细表</span>
            </template>
            <div class="flex-container">
                <div class="flex-item">
                    <table class="cost-table">
                        <tr>
                            <td colspan="3" class="total-style">A按人天收费</td>
                        </tr>
                        <tr class="td-style">
                            <td rowspan="3">本地</td>
                            <td>标准成本（元）</td>
                            <td>{{ localCost }}</td>
                        </tr>
                        <tr>
                            <td class="td-style">人天</td>
                            <td><a-input-number v-model:value.number="localDay" @input="calculateLocalSubtotal"
                                    style="background-color: #f0f0f0; width:100%" :controls="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                                    :disabled="props.intoModel === 'already'" :precision="2" :step="0.01" /></td>
                        </tr>
                        <tr class="td-style">
                            <td>小计（元）</td>
                            <td>{{ formatNumber(localSubtotal) }}</td>
                        </tr>
                        <tr class="td-style">
                            <td rowspan="3">外地</td>
                            <td>标准成本（元）</td>
                            <td>{{ remoteCost }}</td>
                        </tr>
                        <tr>
                            <td class="td-style">人天</td>
                            <td><a-input-number v-model:value.number="remoteDay" @input="calculateRemoteSubtotal"
                                    style="background-color: #f0f0f0; width:100%" :controls="false" placeholder="请输入"
                                    :bordered="false" :disabled="props.intoModel === 'already'" :precision="2"
                                    :step="0.01" /></td>
                        </tr>
                        <tr class="td-style">
                            <td>小计（元）</td>
                            <td>{{ formatNumber(remoteSubtotal) }}</td>
                        </tr>
                        <tr class="td-style">
                            <td colspan="2">合计（元）</td>
                            <td>{{ formatNumber(personDayAmount) }}</td>
                        </tr>

                    </table>
                </div>
                <div class="flex-item">
                    <div>
                        <table class="cost-table">
                            <tbody>
                                <tr class="total-style">
                                    <td colspan="2">B 按时间段收费（备选）</td>
                                </tr>
                                <tr>
                                    <td class="td-style">区间:</td>
                                    <td><a-input v-model:value="intervalRange"
                                            :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                            :disabled="props.intoModel === 'already'"
                                            style="background-color: #f0f0f0; width:100%" /></td>
                                </tr>
                                <tr>
                                    <td class="td-style">区间内次数:</td>
                                    <td><a-input-number v-model:value.number="occurrences" :controls="false" :precision="2"
                                            :step="0.01" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                            :bordered="false" :disabled="props.intoModel === 'already'"
                                            style="background-color: #f0f0f0; width:100%" /></td>
                                </tr>
                                <tr>
                                    <td class="td-style">打包价格（元）:</td>
                                    <td><a-input-number v-model:value.number="packagePrice" :controls="false" :precision="2"
                                            :step="0.01" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                            :bordered="false" :disabled="props.intoModel === 'already'"
                                            style="background-color: #f0f0f0; width:100%" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <table>
                <tr class="total-style" style="padding: 5px 20px;display: block;">
                    <td>实施成本合计（元）:</td>
                    <td>{{ formatNumber(totalCost) }}</td>
                </tr>
            </table>
            <div class="time-section">
                <table class="cost-table">
                    <thead>
                        <tr>
                            <td style="font-weight:600">备注</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><a-textarea name="" id="" cols="168" rows="3" v-model:value="remark"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入备注'" :disabled="props.intoModel === 'already'"></a-textarea></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </a-card>
    </div>
</template>

<script setup>
import { anyType } from 'ant-design-vue/es/_util/type';
import { ref, computed, defineExpose, defineProps, watch } from 'vue';
import { formatNumber } from '@/utils/format'

const props = defineProps({
    implementationData: anyType,
    intoModel: {
        type: String,
        required: false
    }
});
// 表格数据
const localCost = ref(1500);
const localDay = ref('');
const localSubtotal = ref(0);

const remoteCost = ref(2500);
const remoteDay = ref('');
const remoteSubtotal = ref(0);

const packagePrice = ref('');
const intervalRange = ref()
const occurrences = ref()

const remark = ref('')

// 清空
const reset = () => {
    localCost.value = 1500
    localDay.value = ''
    localSubtotal.value = 0

    remoteCost.value = 2500
    remoteDay.value = ''
    remoteSubtotal.value = 0

    packagePrice.value = ''
    intervalRange.value = ''
    occurrences.value = ''

    remark.value = ''
}

watch(() => props.implementationData, (newVal) => {
    if (props.implementationData) {
        console.log(props.implementationData, 'props.implementationData')
    }
    if (newVal) {
        // localCost.value = newVal.localCost
        localDay.value = newVal.localDay
        localSubtotal.value = newVal.localSubtotal

        // remoteCost.value = newVal.remoteCost
        remoteDay.value = newVal.remoteDay
        remoteSubtotal.value = newVal.remoteSubtotal

        packagePrice.value = newVal.packagePrice
        intervalRange.value = newVal.intervalRange
        occurrences.value = newVal.occurrences

        remark.value = newVal.remark
    } else {
        reset();
    }
}, { immediate: true });
// 计算逻辑
const calculateLocalSubtotal = () => {
    localSubtotal.value = (localCost.value * localDay.value) || 0;
    updateTotal();
};

const calculateRemoteSubtotal = () => {
    remoteSubtotal.value = (remoteCost.value * remoteDay.value) || 0;
    updateTotal();
};

const updateTotal = () => {
    personDayAmount.value = localSubtotal.value + remoteSubtotal.value;
};

const personDayAmount = computed(() => {
    return localSubtotal.value + remoteSubtotal.value;
});

const totalCost = computed(() => {
    return personDayAmount.value + (packagePrice.value || 0);
});


// 传参
const getImplementationData = () => {
    const requestData = {
        localCost: localCost.value,
        localSubtotal: localSubtotal.value,
        localDay: localDay.value,
        remoteDay: remoteDay.value,
        remoteCost: remoteCost.value,
        remoteSubtotal: remoteSubtotal.value,
        packagePrice: packagePrice.value,
        intervalRange: intervalRange.value,
        occurrences: occurrences.value,
        remark: remark.value,
        totalCost: totalCost.value
    };
    return requestData;
};
const getTotal = () => {
    return totalCost
}
const setVersionData = () => {
    // localCost.value = props.implementationData.localCost
    localDay.value = props.implementationData.localDay
    localSubtotal.value = props.implementationData.localSubtotal

    // remoteCost.value = props.implementationData.remoteCost
    remoteDay.value = props.implementationData.remoteDay
    remoteSubtotal.value = props.implementationData.remoteSubtotal

    packagePrice.value = props.implementationData.packagePrice
    intervalRange.value = props.implementationData.intervalRange
    occurrences.value = props.implementationData.occurrences

    remark.value = props.implementationData.remark
}
defineExpose({ getImplementationData, reset, getTotal, setVersionData });
</script>

<style scoped>
.container {
    height: 100%;
    padding: 20px;
    overflow: auto;
}

.flex-container {
    display: flex;
    /* 使用flex 布局 */
    justify-content: space-between;
    /* 在主轴上均匀分配空间 */
}

.flex-item {
    flex: 1;
    /* 使两个项占用相同的宽度 */
    margin: 10px;
    /* 为每个项提供一些外边距 */
    min-width: 300px;
    /* 确保每个部分有最小宽度 */
}

.container>>>.ant-card-body {
    padding: 0px !important;
    z-index: 999;
    overflow: auto;
}

input {
    border: none
}

.cost-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.cost-table th,
.cost-table td {
    border: 1px solid #f0f0f0;
    padding: 6px;
    text-align: left;
}

.cost-table input {
    width: 100%;
    box-sizing: border-box;
    outline: none;
}

.cost-table textarea {
    width: 100%;
    box-sizing: border-box;
    border: none;
    outline: none;
    background-color: #f0f0f0;
}

.total {
    font-weight: bold;
    margin-bottom: 20px;
}

.time-section {
    margin-top: 20px;
}

.total-style {
    font-weight: bold;
    font-size: 15px;
    background-color: rgba(0, 0, 0, 0.02) !important;
    /* background-color: rgb(105, 197, 240); */
}

.td-style {
    /* background-color: rgb(181, 223, 243); */

}

.container>>>.ant-input-number .ant-input-number-input {
    padding: 10px;
}

.container>>>.ant-input {
    padding: 10px;
}</style>