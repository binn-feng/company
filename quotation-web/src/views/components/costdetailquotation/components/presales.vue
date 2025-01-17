<template>
    <div class="container">
        <a-row gutter="16">
            <a-col :span="17">
                <a-card>
                    <template #title>
                        <span class="fs16" style="margin-left: 15px;">售前支持成本明细表</span>
                    </template>
                    <table class="data-table" bordered>
                        <tr class="day-total">
                            <td colspan="3">售前支持工时累计 (人月)</td>
                            <td colspan="3">
                                <!-- <input v-model="totalWorkHours" readonly /> -->
                                {{ parseFloat(totalWorkHours.toFixed(2)) }}
                            </td>
                        </tr>
                        <tr class="day-total">
                            <td colspan="3">公司内部（人月）</td>
                            <td colspan="3">
                                <!-- <input v-model="totalInternal" readonly /> -->
                                {{ parseFloat(totalInternal.toFixed(2)) }}
                            </td>
                        </tr>
                        <tr class="day-total">
                            <td colspan="3">本地（人月）</td>
                            <td colspan="3">
                                <!-- <input v-model="totalLocal" readonly /> -->
                                {{ parseFloat(totalLocal.toFixed(2)) }}

                            </td>
                        </tr>
                        <tr class="day-total">
                            <td colspan="3">外地（人月）</td>
                            <td colspan="3">
                                <!-- <input v-model="totalExternal" readonly /> -->
                                {{ parseFloat(totalExternal.toFixed(2)) }}
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 11%;"></th>
                            <th style="width: 45%;">内容</th>
                            <th style="width: 11%;">公司内部</th>
                            <th style="width: 11%;">本地外派工作量</th>
                            <th style="width: 11%;">外地外派工作量</th>
                            <th style="width: 11%;">单位</th>
                        </tr>
                        <tr>
                            <td :rowspan="inputData.records.length">已投入</td>
                            <td><a-input v-model:value="inputData.records[0].content" :bordered="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" style="width: 100%;"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <!-- <td><input v-model="inputData.records[0].content" /></td> -->
                            <td><a-input-number v-model:value="inputData.records[0].within" @input="updateEstimatedTotals()"
                                    :precision="2" style="width: 100%;background-color: #f0f0f0;" :step="0.01"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td><a-input-number v-model:value="inputData.records[0].local" @input="updateEstimatedTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td><a-input-number v-model:value="inputData.records[0].remote" @input="updateEstimatedTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td>
                                <a-select :bordered="false" v-model:value="inputData.records[0].unit"
                                    @change="updateTotals()" style="width: 100%" :disabled="props.intoModel === 'already'">
                                    <a-select-option value="HUMAN_MONTH">人月</a-select-option>
                                    <a-select-option value="HUMAN_DAY">人天</a-select-option>
                                </a-select>
                            </td>
                        </tr>
                        <tr v-for="(record, index) in inputData.records.slice(1, inputData.records.length)"
                            :key="index + 1">
                            <td><a-input v-model:value="record.content" :bordered="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" style="width: 100%;"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="record.within" @input="updateTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="record.local" @input="updateTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="record.remote" @input="updateTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" /></td>
                            <td>
                                <a-select :bordered="false" v-model:value="record.unit" @change="updateTotals()"
                                    style="width: 100%" :disabled="props.intoModel === 'already'">
                                    <a-select-option value="HUMAN_MONTH">人月</a-select-option>
                                    <a-select-option value="HUMAN_DAY">人天</a-select-option>
                                </a-select>
                            </td>
                        </tr>
                        <tr v-if="props.intoModel !== 'already'" style="width: 100%;">
                            <div style="padding: 20px;display: flex;justify-content: flex-end;">
                                <a-button type="primary" @click="addRecord('投入')">添加已投入行</a-button>
                            </div>
                        </tr>
                        <tr>
                            <td :rowspan="inputData.estimatedRecords.length">预计后期投入</td>
                            <td><a-input v-model:value="inputData.estimatedRecords[0].content" :bordered="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" style="width: 100%;"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="inputData.estimatedRecords[0].within"
                                    @input="updateEstimatedTotals()" style="width: 100%;background-color: #f0f0f0;"
                                    :step="0.01" :precision="2" :controls="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="inputData.estimatedRecords[0].local"
                                    @input="updateEstimatedTotals()" style="width: 100%;background-color: #f0f0f0;"
                                    :step="0.01" :precision="2" :controls="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="inputData.estimatedRecords[0].remote"
                                    @input="updateEstimatedTotals()" style="width: 100%;background-color: #f0f0f0;"
                                    :step="0.01" :precision="2" :controls="false"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td>
                                <a-select :bordered="false" v-model:value="inputData.estimatedRecords[0].unit"
                                    @change="updateTotals()" style="width: 100%" :disabled="props.intoModel === 'already'">
                                    <a-select-option value="HUMAN_MONTH">人月</a-select-option>
                                    <a-select-option value="HUMAN_DAY">人天</a-select-option>
                                </a-select>
                            </td>
                        </tr>
                        <tr v-for="(estimatedRecord, index) in inputData.estimatedRecords.slice(1, inputData.estimatedRecords.length)"
                            :key="index + 1">
                            <td><a-input v-model:value="estimatedRecord.content" style="width: 100%"
                                    :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                                    :disabled="props.intoModel === 'already'" /></td>
                            <td><a-input-number v-model:value="estimatedRecord.within" @input="updateEstimatedTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td><a-input-number v-model:value="estimatedRecord.local" @input="updateEstimatedTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td><a-input-number v-model:value="estimatedRecord.remote" @input="updateEstimatedTotals()"
                                    style="width: 100%;background-color: #f0f0f0;" :step="0.01" :precision="2"
                                    :controls="false" :placeholder="props.intoModel === 'already' ? '' : '请输入'"
                                    :bordered="false" :disabled="props.intoModel === 'already'" />
                            </td>
                            <td>
                                <a-select :bordered="false" v-model:value="estimatedRecord.unit" @change="updateTotals()"
                                    style="width: 100%" :disabled="props.intoModel === 'already'">
                                    <a-select-option value="HUMAN_MONTH">人月</a-select-option>
                                    <a-select-option value="HUMAN_DAY">人天</a-select-option>
                                </a-select>
                            </td>
                        </tr>
                        <tr v-if="props.intoModel !== 'already'">
                            <div style="padding: 20px;">
                                <a-button type="primary" @click="addRecord('预计')">添加预计后期投入行</a-button>
                            </div>
                        </tr>
                    </table>
                    <div class="cost-summary">
                        <span class="fs16 mg12">前期支持成本合计（元）</span>
                        <table class="cost-table">
                            <tbody>
                                <tr class="day-total">
                                    <td>公司内部（按照 25000 元/人月计算）</td>
                                    <td>{{ formatNumber(calculateCost(totalInternal, 25000)) }}</td>
                                </tr>
                                <tr class="day-total">
                                    <td>本地外派成本（按照 1500 元/人天计算）</td>
                                    <td>{{ formatNumber(calculateCost(totalLocal, 1500 * 21.75)) }}</td>
                                </tr>
                                <tr class="day-total">
                                    <td>外地外派成本（按照 2500 元/人天计算）</td>
                                    <td>{{ formatNumber(calculateCost(totalExternal, 2500 * 21.75)) }}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="total-cost">
                            <strong>总计(元)：</strong> {{ formatNumber(totalCost) }}
                        </div>
                    </div>
                </a-card>
            </a-col>
            <a-col :span="7">
                <a-card class="remark-card">
                    <h3>售前成本说明</h3>
                    <div style="border-bottom: 1px solid #f0f0f0;padding-bottom: 15px;">
                        售前投入相关成本，需纳入整体项目成本预估中~
                        <p>
                            <span>当前整体分为两部分：</span>
                            <span>1. 已投入：即在进行项目成本预估时，以投入的售前支持时间；</span>
                            <span> 2. 预计后期投入：该部分需要售前人员与销售团队确认，基于整体情况，后续预计还会投入的相关工作与预计时间投入；</span>
                        </p>
                        <p>
                            <span>售前成本包括但不限于：</span>
                            <span>会议，前期调研，需求分析，竞品分析，解决方案，POC预演，陪标，投标文件等</span>
                        </p>

                    </div>
                    <h3 style="padding-top: 10px;">售前成本备注</h3>
                    <a-textarea v-model:value="supportRemark" placeholder="请输入售前备注内容" rows="8" :disabled="props.intoModel === 'already'"/>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script setup>
import { anyType } from 'ant-design-vue/es/_util/type';
import { ref, computed, defineExpose, defineProps, watch } from 'vue';
import { formatNumber } from '@/utils/format'

// 监听是否有暂存 
const props = defineProps({
    presaleSupportData: anyType,
    activeKey: {
        type: String,
        required: false
    },
    intoModel: {
        type: String,
        required: false
    },
    supportRemark: anyType,
});
const inputData = ref({
    records: Array.from({ length: 2 }, () => ({ content: '', within: '', local: '', remote: '', type: 'INVESTED', unit: 'HUMAN_MONTH' })),
    estimatedRecords: Array.from({ length: 2 }, () => ({ content: '', within: '', local: '', remote: '', type: 'LATE_INPUTS', unit: 'HUMAN_MONTH' })),
});
const supportRemark = ref('')
// 重置表单
const reset = () => {
    inputData.value = {
        records: Array.from({ length: 2 }, () => ({ content: '', within: '', local: '', remote: '', type: 'INVESTED', unit: 'HUMAN_MONTH' })),
        estimatedRecords: Array.from({ length: 2 }, () => ({ content: '', within: '', local: '', remote: '', type: 'LATE_INPUTS', unit: 'HUMAN_MONTH' })),
    }
}

// watch(() => props.activeKey, (newVal) => {
//     supportRemark.value = props.supportRemark
//     if (props.presaleSupportData) {
//         if (newVal === 'persale') {
//             supportRemark.value = props.supportRemark
//             // 对 quoteOpportunitiesSupportList 进行分类并赋值给 inputData
//             const investedRecords = props.presaleSupportData?.quoteOpportunitiesSupportList.filter(record => record.type === 'INVESTED');
//             const lateInputRecords = props.presaleSupportData?.quoteOpportunitiesSupportList.filter(record => record.type === 'LATE_INPUTS');

//             // 对于已投入的数据
//             inputData.value.records = investedRecords.map(record => ({
//                 content: record.content || '',
//                 within: record.within || '',
//                 local: record.local || '',
//                 remote: record.remote || '',
//                 type: record.type,
//                 unit: record.unit
//             }));

//             // 对于预计后期投入的数据
//             inputData.value.estimatedRecords = lateInputRecords.map(record => ({
//                 content: record.content || '',
//                 within: record.within || '',
//                 local: record.local || '',
//                 remote: record.remote || '',
//                 type: record.type,
//                 unit: record.unit
//             }));

//             // 如果数量不足6项，自动填充空数据
//             while (inputData.value.records.length < 2) {
//                 inputData.value.records.push({ content: '', within: '', local: '', remote: '', type: 'INVESTED', unit: 'HUMAN_MONTH' });
//             }
//             while (inputData.value.estimatedRecords.length < 2) {
//                 inputData.value.estimatedRecords.push({ content: '', within: '', local: '', remote: '', type: 'LATE_INPUTS', unit: 'HUMAN_MONTH' });
//             }
//         }
//     }
// }, { immediate: true })
watch(() => props.presaleSupportData, (newVal) => {
    console.log(props.presaleSupportData, 'props.presaleSupportData')
    if (newVal) {
        const investedRecords = newVal.quoteOpportunitiesSupportList.filter(record => record.type === 'INVESTED');
        const lateInputRecords = newVal.quoteOpportunitiesSupportList.filter(record => record.type === 'LATE_INPUTS');

        // 对于已投入的数据
        inputData.value.records = investedRecords.map(record => ({
            content: record.content || '',
            within: record.within || '',
            local: record.local || '',
            remote: record.remote || '',
            type: record.type,
            unit: record.unit
        }));

        // 对于预计后期投入的数据
        inputData.value.estimatedRecords = lateInputRecords.map(record => ({
            content: record.content || '',
            within: record.within || '',
            local: record.local || '',
            remote: record.remote || '',
            type: record.type,
            unit: record.unit
        }));

        // 如果数量不足6项，自动填充空数据
        while (inputData.value.records.length < 2) {
            inputData.value.records.push({ content: '', within: '', local: '', remote: '', type: 'INVESTED', unit: 'HUMAN_MONTH' });
        }
        while (inputData.value.estimatedRecords.length < 2) {
            inputData.value.estimatedRecords.push({ content: '', within: '', local: '', remote: '', type: 'LATE_INPUTS', unit: 'HUMAN_MONTH' });
        }
        supportRemark.value = props.supportRemark
    } else {
        reset();
    }
}, { immediate: true });
watch(() => props.supportRemark, (newVal) => {
    console.log(newVal,'supportRemark')
    if (newVal) {
        supportRemark.value = props.supportRemark
    }
})
const totalInternal = computed(() => {
    const totalFromRecords = inputData.value.records.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const internalValue = parseFloat(record.within || 0);
            return total + (isNaN(internalValue) ? 0 : internalValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const internalValue = parseFloat(record.within || 0) / 21.75;
            return total + (isNaN(internalValue) ? 0 : internalValue);
        }

    }, 0);
    const totalFromEstimatedRecords = inputData.value.estimatedRecords.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const internalValue = parseFloat(record.within || 0);
            return total + (isNaN(internalValue) ? 0 : internalValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const internalValue = parseFloat(record.within || 0) / 21.75;
            return total + (isNaN(internalValue) ? 0 : internalValue);
        }

    }, 0);
    return totalFromRecords + totalFromEstimatedRecords;
});
const totalLocal = computed(() => {
    const totalFromRecords = inputData.value.records.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const localValue = parseFloat(record.local || 0);
            return total + (isNaN(localValue) ? 0 : localValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const localValue = parseFloat(record.local || 0) / 21.75;
            return total + (isNaN(localValue) ? 0 : localValue);
        }

    }, 0);
    const totalFromEstimatedRecords = inputData.value.estimatedRecords.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const localValue = parseFloat(record.local || 0);
            return total + (isNaN(localValue) ? 0 : localValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const localValue = parseFloat(record.local || 0) / 21.75;
            return total + (isNaN(localValue) ? 0 : localValue);
        }

    }, 0);
    return totalFromRecords + totalFromEstimatedRecords;
});
const totalExternal = computed(() => {
    const totalFromRecords = inputData.value.records.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const externalValue = parseFloat(record.remote || 0);
            return total + (isNaN(externalValue) ? 0 : externalValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const externalValue = parseFloat(record.remote || 0) / 21.75;
            return total + (isNaN(externalValue) ? 0 : externalValue);
        }

    }, 0);
    const totalFromEstimatedRecords = inputData.value.estimatedRecords.reduce((total, record) => {
        if (record.unit === 'HUMAN_MONTH') {
            const externalValue = parseFloat(record.remote || 0);
            return total + (isNaN(externalValue) ? 0 : externalValue);
        } else if (record.unit === 'HUMAN_DAY') {
            const externalValue = parseFloat(record.remote || 0) / 21.75;
            return total + (isNaN(externalValue) ? 0 : externalValue);
        }

    }, 0);
    return totalFromRecords + totalFromEstimatedRecords;
});

const totalWorkHours = computed(() => {
    return totalInternal.value + totalLocal.value + totalExternal.value;
});

const totalCost = computed(() => {
    const cost = parseFloat(calculateCost(totalInternal.value, 25000)) + parseFloat(calculateCost(totalLocal.value, 1500 * 21.75)) + parseFloat(calculateCost(totalExternal.value, 2500 * 21.75))
    return parseFloat(cost.toFixed(2));
});

const calculateCost = (amount, rate) => {
    return (amount * rate).toFixed(2);
};
// 添加行
const addRecord = (type) => {
    if (type === '投入') {
        inputData.value.records.push({
            content: '',
            within: null,
            local: null,
            remote: null,
            type: 'INVESTED',
            unit: 'HUMAN_MONTH'
        });
    } else if (type === '预计') {
        inputData.value.estimatedRecords.push({
            content: '',
            within: null,
            local: null,
            remote: null,
            type: 'LATE_INPUTS',
            unit: 'HUMAN_MONTH'
        });
    }
    inputData.value = { ...inputData.value };
}

// 传参
const getSalesData = () => {
    const requestData = [
        // 遍历已投入记录  
        ...inputData.value.records
            .filter(record => {
                return (
                    String(record.content).trim() !== '' ||
                    String(record.within).trim() !== '' ||
                    String(record.local).trim() !== '' ||
                    String(record.remote).trim() !== ''
                );
            })
            .map(record => ({
                content: record.content,
                within: record.within === '' ? '' : Number(record.within), // 根据其为字符串还是数字来设置  
                local: record.local === '' ? '' : Number(record.local),
                remote: record.remote === '' ? '' : Number(record.remote),
                type: record.type,
                unit: record.unit,
            })),
        // 遍历预计后期投入记录  
        ...inputData.value.estimatedRecords
            .filter(record => {
                return (
                    String(record.content).trim() !== '' ||
                    String(record.within).trim() !== '' ||
                    String(record.local).trim() !== '' ||
                    String(record.remote).trim() !== ''
                );
            })
            .map(record => ({
                content: record.content,
                within: record.within === '' ? '' : Number(record.within),
                local: record.local === '' ? '' : Number(record.local),
                remote: record.remote === '' ? '' : Number(record.remote),
                type: record.type,
                unit: record.unit,

            }))
    ];
    return requestData;
};
const getTotal = () => {
    return totalCost
}
const getSupportRemark = () => {
    return supportRemark
}
defineExpose({ getSalesData, reset, getTotal, getSupportRemark });

function updateTotals() {
    // 
}

function updateEstimatedTotals() {
    // 
}

</script>

<style scoped >
.container {
    height: 100%;
    padding: 20px;
    overflow: auto;
}

.container>>>.ant-card-body {
    padding: 0px !important;
    z-index: 999;
}

.container>>>.ant-card-head {
    text-align: center;
    /* background-color: #d4edda; */
    padding: 10px;
    border-radius: 5px;
}

.title {
    text-align: center;
    background-color: #d4edda;
    padding: 10px;
    border-radius: 5px;
}

.total {
    text-align: center;
    font-size: 24px;
    margin: 20px 0;
}

.data-table-total {
    margin-bottom: 0px !important;
    z-index: 999;
}

.data-table,
.cost-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.data-table th,
.data-table td,
.cost-table td {
    border: 1px solid #f0f0f0;
    padding: 6px;
    text-align: center;
    height: 22px;
}

.data-table th {
    /* background-color: #f8f9fa; */
}

.data-table input {
    width: 100%;
    height: 100%;
    /* padding: 5px; */
    border: none;
    /* border-radius: 4px; */
    text-align: center;
    outline: none;
}

.cost-summary {
    /* background-color: #e9ecef; */
    padding: 10px;
    border-radius: 5px;
}

.total-cost {
    border: 1px solid #f0f0f0;
    font-weight: bold;
    font-size: 16px;
    padding: 10px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.day-total {
    border: 1px solid #f0f0f0;
    font-weight: bold;
    font-size: 15px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.container .remark-card {
    padding: 0px 20px;
}

.remark-card span {
    display: block;
}

.fs16 {
    font-size: 17px;
    font-weight: 700;
}

.mg12 {
    margin: 12px 0;
    display: inline-block;
}

</style>