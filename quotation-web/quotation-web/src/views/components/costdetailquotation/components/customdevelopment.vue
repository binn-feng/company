<template>
    <div class="container">
        <a-card>
            <template #title>
                <span class="fs16">定制开发明细表</span>
                <span style="padding-inline: 20px;font-size: 14px;"><a style="text-decoration: underline;"
                        @click="handleDownLoad" v-if="props.intoModel !== 'already'">功能清单模板下载</a></span>
                <span>
                    <a-upload v-model:file-list="fileList" name="file" :multiple="false" @change="handleChange"
                        :show-upload-list="false" :before-upload="beforeUpload">
                        <a style="text-decoration: underline;" v-if="props.intoModel !== 'already'">功能清单上传</a>
                    </a-upload>
                </span>
            </template>
            <a-table :columns="columns" :dataSource="data" :pagination="false" bordered size="small"
                :scroll="{ x: 'max-content' }">
                <template #bodyCell="{ column, record }">
                    <template
                        v-if="['demandInternalUnit', 'demandExternalUnit', 'devInternalUnit', 'devExternalUnit', 'testInternalUnit', 'testExternalUnit', 'uiInternalUnit', 'uiExternalUnit', 'pmInternalUnit', 'pmExternalUnit'].includes(column.dataIndex)">
                        <a-select v-model:value="record[column.dataIndex]" placeholder="请选择"
                            @change="updateTotal(column.dataIndex)" :bordered="false"
                            :disabled="props.intoModel === 'already'">
                            <a-select-option value="HUMAN_MONTH">人月</a-select-option>
                            <a-select-option value="HUMAN_NATURE">人天</a-select-option>
                        </a-select>
                    </template>
                    <template v-else-if="['moduleName', 'subModuleName', 'description'].includes(column.dataIndex)">
                        <a-input v-model:value="record[column.dataIndex]"
                            :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                            :disabled="props.intoModel === 'already'" />
                    </template>
                    <template v-else-if="column.dataIndex">
                        <a-input-number v-model:value="record[column.dataIndex]" @change="updateTotal(column.dataIndex)"
                            :step="0.01" :precision="2" :controls="false"
                            :placeholder="props.intoModel === 'already' ? '' : '请输入'" :bordered="false"
                            style="background-color: #f0f0f0;" :disabled="props.intoModel === 'already'" />
                    </template>
                </template>
                <template #summary>
                    <a-table-summary fixed>
                        <a-table-summary-row class="total-work">
                            <a-table-summary-cell :col-span="3" :index="0"
                                style="background-color: #fafafa;">工作量（不含外派）总计（人月）</a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" :index="4" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.internal1.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.internal2.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.internal3.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.internal4.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text> {{ parseFloat(totalWorkloads.internal5.toFixed(2))
                                }}</a-typography-text>
                            </a-table-summary-cell>
                        </a-table-summary-row>
                        <a-table-summary-row class="total-work">
                            <a-table-summary-cell :col-span="2" :row-span="2" :index="0"
                                style="background-color: #fafafa; text-align: left !important;">外派工作量（人月）</a-table-summary-cell>
                            <a-table-summary-cell :index="2" style="background-color: #fafafa;">
                                <a-typography-text>本地</a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.demandLocalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.devLocalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.testLocalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.uiLocalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.pmLocalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                        </a-table-summary-row>
                        <a-table-summary-row class="total-work">
                            <a-table-summary-cell :index="2" style="background-color: #fafafa;">
                                <a-typography-text>外地</a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.demandExternalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.devExternalWorkload.toFixed(2)) }}

                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.testExternalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.uiExternalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                            <a-table-summary-cell :col-span="5" class="total-num">
                                <a-typography-text>
                                    {{ parseFloat(totalWorkloads.pmExternalWorkload.toFixed(2)) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                        </a-table-summary-row>
                        <a-table-summary-row class="total">
                            <a-table-summary-cell :col-span="3" :index="0"
                                style="background-color: #fafafa;">软件成本总计（元）</a-table-summary-cell>
                            <a-table-summary-cell :col-span="25" style="text-align: left !important;">
                                <a-typography-text>
                                    {{ formatNumber(parseFloat(totalSoftwareCosts.toFixed(2))) }}
                                </a-typography-text>
                            </a-table-summary-cell>
                        </a-table-summary-row>
                    </a-table-summary>
                </template>
            </a-table>
            <div style="padding: 20px;display: flex;justify-content: flex-end;" v-if="props.intoModel !== 'already'">
                <a-button type="primary" @click="addRow">添加行</a-button>
            </div>
            <div class="cost-table">
                <div class="section">
                    <span class="fs16 mg12" style="padding: 0 0 5px 20px;">软件汇总表</span>
                    <table>
                        <thead>
                            <tr>
                                <th>成本类型</th>
                                <th>需求</th>
                                <th>UI设计</th>
                                <th>开发评估</th>
                                <th>测试</th>
                                <th>管理</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>开发标准成本（元/月）</td>
                                <td>{{ formatNumber(projectData.RA.insiderMonth) }}</td>
                                <td>{{ formatNumber(projectData.UI.insiderMonth) }}</td>
                                <td>{{ formatNumber(projectData.DEV.insiderMonth) }}</td>
                                <td>{{ formatNumber(projectData.TEST.insiderMonth) }}</td>
                                <td>{{ formatNumber(projectData.PM.insiderMonth) }}</td>
                            </tr>
                            <tr>
                                <td>单项人月数</td>
                                <td>{{ parseFloat(totalWorkloads.internal1.toFixed(2)) }}</td>
                                <td>{{ parseFloat(totalWorkloads.internal4.toFixed(2)) }}</td>
                                <td>{{ parseFloat(totalWorkloads.internal2.toFixed(2)) }}</td>
                                <td>{{ parseFloat(totalWorkloads.internal3.toFixed(2)) }}</td>
                                <td>{{ parseFloat(totalWorkloads.internal5.toFixed(2)) }}</td>
                            </tr>
                            <tr>
                                <td>单项小计（元）</td>
                                <td>{{ formatNumber(parseFloat(calculateSubtotal('RA',
                                    totalWorkloads.internal1).toFixed(2))) }}
                                </td>
                                <td>{{ formatNumber(parseFloat(calculateSubtotal('UI',
                                    totalWorkloads.internal4).toFixed(2))) }}
                                </td>
                                <td>{{ formatNumber(parseFloat(calculateSubtotal('DEV',
                                    totalWorkloads.internal2).toFixed(2))) }}
                                </td>
                                <td>{{ formatNumber(parseFloat(calculateSubtotal('TEST',
                                    totalWorkloads.internal3).toFixed(2))) }}
                                </td>
                                <td>{{ formatNumber(parseFloat(calculateSubtotal('PM',
                                    totalWorkloads.internal5).toFixed(2))) }}
                                </td>
                            </tr>
                            <tr>
                                <td>人月数小计</td>
                                <td colspan="5">{{ totalWorkloadsSum }}</td>
                            </tr>
                            <tr class="total-cost">
                                <td>软件定制开发成本小计（元）</td>
                                <td colspan="5">{{ formatNumber(subtotalCost) }}</td>

                            </tr>
                            <tr>
                                <td>本地驻场开发成本（元/天）</td>
                                <td>{{ formatNumber(projectData.RA.localPresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.UI.localPresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.DEV.localPresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.TEST.localPresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.PM.localPresenceDays) }}</td>
                            </tr>
                            <tr>
                                <td>外地驻场开发成本（元/天）</td>
                                <td>{{ formatNumber(projectData.RA.remotePresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.UI.remotePresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.DEV.remotePresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.TEST.remotePresenceDays) }}</td>
                                <td>{{ formatNumber(projectData.PM.remotePresenceDays) }}</td>
                            </tr>
                            <tr>
                                <td>本地人天数</td>
                                <td> {{ parseFloat((totalWorkloads.demandLocalWorkload * 21.75).toFixed(2)) }}</td>
                                <td>{{ parseFloat((totalWorkloads.uiLocalWorkload * 21.75).toFixed(2)) }}</td>
                                <td>{{ parseFloat((totalWorkloads.devLocalWorkload * 21.75).toFixed(2)) }}</td>
                                <td> {{ parseFloat((totalWorkloads.testLocalWorkload * 21.75).toFixed(2)) }}</td>
                                <td>{{ parseFloat((totalWorkloads.pmLocalWorkload * 21.75).toFixed(2)) }}</td>
                            </tr>
                            <tr>
                                <td>外地人天数</td>
                                <td>{{ parseFloat((totalWorkloads.demandExternalWorkload * 21.75).toFixed(2)) }}</td>
                                <td>{{ parseFloat((totalWorkloads.uiExternalWorkload * 21.75).toFixed(2)) }}</td>
                                <td> {{ parseFloat((totalWorkloads.devExternalWorkload * 21.75).toFixed(2)) }}</td>
                                <td>{{ parseFloat((totalWorkloads.testExternalWorkload * 21.75).toFixed(2)) }}</td>
                                <td> {{ parseFloat((totalWorkloads.pmExternalWorkload * 21.75).toFixed(2)) }}</td>
                            </tr>
                            <tr>
                                <td>单项小计（元）</td>
                                <td>{{ formatNumber(computeSubtotal(totalWorkloads.demandLocalWorkload,
                                    totalWorkloads.demandExternalWorkload, 'RA')) }}</td>
                                <td>{{ formatNumber(computeSubtotal(totalWorkloads.uiLocalWorkload,
                                    totalWorkloads.uiExternalWorkload,
                                    'UI')) }}</td>
                                <td>{{ formatNumber(computeSubtotal(totalWorkloads.devLocalWorkload,
                                    totalWorkloads.devExternalWorkload,
                                    'DEV')) }}</td>
                                <td>{{ formatNumber(computeSubtotal(totalWorkloads.testLocalWorkload,
                                    totalWorkloads.testExternalWorkload, 'TEST')) }}</td>
                                <td>{{ formatNumber(computeSubtotal(totalWorkloads.pmLocalWorkload,
                                    totalWorkloads.pmExternalWorkload,
                                    'PM')) }}</td>
                            </tr>
                            <tr>
                                <td>人天数小计</td>
                                <td colspan="5">{{ totalManDays }}</td>
                            </tr>
                            <tr class="total-cost">
                                <td>软件（驻场）成本小计（元）</td>
                                <td colspan="5">{{ formatNumber(parseFloat(totalProjectCost).toFixed(2)) }}</td>
                            </tr>
                            <tr class="total">
                                <td>软件成本总计（元）</td>
                                <td colspan="5">{{ formatNumber(parseFloat(totalSoftwareCosts.toFixed(2))) }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </a-card>
    </div>
</template>

<script setup>
import { anyType } from 'ant-design-vue/es/_util/type';
import { ref, watch, defineExpose, defineProps, computed, onMounted, reactive } from 'vue';
import { getCostMap } from '@/api/mannageDebug'
import { formatNumber } from '@/utils/format'
import { download } from '@/utils/common.js'
import { uploadCustomizable } from '@/api/common'
import { message, Modal } from 'ant-design-vue';

const props = defineProps({
    name: {
        type: String,
        required: true,
    },
    softwareData: anyType,
    intoModel: {
        type: String,
        required: false
    },
    activeKey: {
        type: String,
        required: false
    },
});
const name = ref('')
watch(() => props.name, (newVal) => {
    name.value = newVal
    columns.value[0].title = newVal;
})
// 表头配置
const columns = ref([
    {
        title: props.name,
        children: [
            { title: '模块', dataIndex: 'moduleName', key: 'moduleName', fixed: 'left' },
            { title: '子模块功能', dataIndex: 'subModuleName', key: 'subModuleName', fixed: 'left' },
            { title: '描述/备注', dataIndex: 'description', key: 'description', fixed: 'left' },
        ],
        // 固定宽度  
        fixed: 'left'
    },
    {
        title: '需求评估',
        className: 'table-blue-bg',
        children: [
            {
                title: '公司内部工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '工作量', dataIndex: 'demandInternalWorkload', key: 'demandInternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'demandInternalUnit', key: 'demandInternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            },
            {
                title: '外派工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '本地', dataIndex: 'demandLocalWorkload', key: 'demandLocalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '外地', dataIndex: 'demandExternalWorkload', key: 'demandExternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'demandExternalUnit', key: 'demandExternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            }
        ],
    },
    {
        title: '开发评估',
        className: 'table-white-bg',
        children: [
            {
                title: '公司内部工作量',
                className: 'table-white-bg',
                children: [
                    {
                        title: '工作量', dataIndex: 'devInternalWorkload', key: 'devInternalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '单位', dataIndex: 'devInternalUnit', key: 'devInternalUnit',
                        className: 'table-white-bg',
                    },
                ]
            },
            {
                title: '外派工作量',
                className: 'table-white-bg',
                children: [
                    {
                        title: '本地', dataIndex: 'devLocalWorkload', key: 'devLocalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '外地', dataIndex: 'devExternalWorkload', key: 'devExternalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '单位', dataIndex: 'devExternalUnit', key: 'devExternalUnit',
                        className: 'table-white-bg',
                    },
                ]
            }
        ]
    },
    {
        title: '测试评估',
        className: 'table-blue-bg',
        children: [
            {
                title: '公司内部工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '工作量', dataIndex: 'testInternalWorkload', key: 'testInternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'testInternalUnit', key: 'testInternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            },
            {
                title: '外派工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '本地', dataIndex: 'testLocalWorkload', key: 'testLocalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '外地', dataIndex: 'testExternalWorkload', key: 'testExternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'testExternalUnit', key: 'testExternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            }
        ]
    },
    {
        title: 'UI评估',
        className: 'table-white-bg',
        children: [
            {
                title: '公司内部工作量',
                className: 'table-white-bg',
                children: [
                    {
                        title: '工作量', dataIndex: 'uiInternalWorkload', key: 'uiInternalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '单位', dataIndex: 'uiInternalUnit', key: 'uiInternalUnit',
                        className: 'table-white-bg',
                    },
                ]
            },
            {
                title: '外派工作量',
                className: 'table-white-bg',
                children: [
                    {
                        title: '本地', dataIndex: 'uiLocalWorkload', key: 'uiLocalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '外地', dataIndex: 'uiExternalWorkload', key: 'uiExternalWorkload',
                        className: 'table-white-bg',
                    },
                    {
                        title: '单位', dataIndex: 'uiExternalUnit', key: 'uiExternalUnit',
                        className: 'table-white-bg',
                    },
                ]
            }
        ]
    },
    {
        title: '项目管理',
        className: 'table-blue-bg',
        children: [
            {
                title: '公司内部工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '工作量', dataIndex: 'pmInternalWorkload', key: 'pmInternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'pmInternalUnit', key: 'pmInternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            },
            {
                title: '外派工作量',
                className: 'table-blue-bg',
                children: [
                    {
                        title: '本地', dataIndex: 'pmLocalWorkload', key: 'pmLocalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '外地', dataIndex: 'pmExternalWorkload', key: 'pmExternalWorkload',
                        className: 'table-blue-bg',
                    },
                    {
                        title: '单位', dataIndex: 'pmExternalUnit', key: 'pmExternalUnit',
                        className: 'table-blue-bg',
                    },
                ]
            }
        ],
        width: 150
    },
]);

const data = ref([]);
for (let key = 1; key <= 6; key++) {
    data.value.push({
        key: key,
        moduleName: '',
        subModuleName: '',
        description: '',
        demandInternalWorkload: '',
        demandInternalUnit: 'HUMAN_NATURE',
        demandLocalWorkload: '',
        demandExternalWorkload: '',
        demandExternalUnit: 'HUMAN_NATURE',
        devInternalWorkload: '',
        devInternalUnit: 'HUMAN_NATURE',
        devLocalWorkload: '',
        devExternalWorkload: '',
        devExternalUnit: 'HUMAN_NATURE',
        testInternalWorkload: '',
        testInternalUnit: 'HUMAN_NATURE',
        testLocalWorkload: '',
        testExternalWorkload: '',
        testExternalUnit: 'HUMAN_NATURE',
        uiInternalWorkload: '',
        uiInternalUnit: 'HUMAN_NATURE',
        uiLocalWorkload: '',
        uiExternalWorkload: '',
        uiExternalUnit: 'HUMAN_NATURE',
        pmInternalWorkload: '',
        pmInternalUnit: 'HUMAN_NATURE',
        pmLocalWorkload: '',
        pmExternalWorkload: '',
        pmExternalUnit: 'HUMAN_NATURE',
    });
}
const hasExistingData = () => {
    return data.value.some(row =>
        row.moduleName ||
        row.subModuleName ||
        row.description ||
        row.demandInternalWorkload ||
        row.demandLocalWorkload ||
        row.demandExternalWorkload ||
        row.devInternalWorkload ||
        row.devLocalWorkload ||
        row.devExternalWorkload ||
        row.testInternalWorkload ||
        row.testLocalWorkload ||
        row.uiInternalWorkload ||
        row.pmInternalWorkload
    );
};
// 基础数据
const projectData = reactive({
    "PRE": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "PM": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "TEST": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "DEV": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "RA": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "IMP": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    },
    "UI": {
        "insiderDays": 0.00,
        "insiderMonth": 0.00,
        "localPresenceDays": 0.00,
        "localPresenceMonth": 0.00,
        "remotePresenceDays": 0.00,
        "remotePresenceMonth": 0.00
    }
});

// 添加行
const addRow = () => {
    const newRow = {
        key: data.value.length + 1,
        moduleName: '',
        subModuleName: '',
        description: '',
        demandInternalWorkload: '',
        demandInternalUnit: 'HUMAN_NATURE',
        demandLocalWorkload: '',
        demandExternalWorkload: '',
        demandExternalUnit: 'HUMAN_NATURE',
        devInternalWorkload: '',
        devInternalUnit: 'HUMAN_NATURE',
        devLocalWorkload: '',
        devExternalWorkload: '',
        devExternalUnit: 'HUMAN_NATURE',
        testInternalWorkload: '',
        testInternalUnit: 'HUMAN_NATURE',
        testLocalWorkload: '',
        testExternalWorkload: '',
        testExternalUnit: 'HUMAN_NATURE',
        uiInternalWorkload: '',
        uiInternalUnit: 'HUMAN_NATURE',
        uiLocalWorkload: '',
        uiExternalWorkload: '',
        uiExternalUnit: 'HUMAN_NATURE',
        pmInternalWorkload: '',
        pmInternalUnit: 'HUMAN_NATURE',
        pmLocalWorkload: '',
        pmExternalWorkload: '',
        pmExternalUnit: 'HUMAN_NATURE',
    };
    data.value.push(newRow);
};
// 获取基础成本设置数据
const getCostMapFunc = async () => {
    try {
        const response = await getCostMap()
        if (response && response.code === 200) {
            // projectData = response.data
            Object.assign(projectData, response.data);
            console.log(projectData, 'map数据数据')
            // updateTotal()
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }

}
onMounted(() => {
    getCostMapFunc()
});
// 下载清单模板
const handleDownLoad = () => {
    download('quote/file/customizableTemplate', null, `功能清单模板.xlsx`)
}
// 上传功能清单
const fileList = ref([]);
const uploadedFiles = ref([]);
const handleChange = async (info) => {
    let newFileList = [...info.fileList];
    // newFileList = newFileList.filter(file => !uploadedFiles.value.some(uploadedFile => uploadedFile.name === file.name));
    fileList.value = newFileList;
    const isExcel = /\.(xls|xlsx)$/.test(info.file.name);
    if (isExcel) {
        if (hasExistingData()) {
            Modal.confirm({
                title: '确认上传',
                content: '上传后全部信息将被覆盖，请确认是否继续上传？',
                onOk: async () => {
                    uploadedFiles.value.push(info.file);
                        await uploadFiles();
                   
                },
                onCancel() {

                },
            });
        } else {
            uploadedFiles.value.push(info.file); 
                await uploadFiles();
        }
    }
};
const uploadFiles = async () => {
    try {
        if (fileList.value.length === 0) {
            message.warning('Please select files to upload!');
            return;
        }
        const response = await uploadCustomizable({
            files: fileList.value.map(file => file.originFileObj)
        });

        if (response) {
            message.success('文件上传成功');
            reset()
            response.forEach((item, index) => {
                if (index >= data.value.length) {
                    addRow();
                }
                if (data.value[index]) {
                    data.value[index].moduleName = item.moduleName || '';
                    data.value[index].subModuleName = item.subModuleName || '';
                    data.value[index].description = item.description || '';
                }
            });
            fileList.value = [];
        } else {
            message.error('File upload failed: ' + response.message);
        }
    } catch (error) {
        fileList.value = [];
        if (error.response) {
            console.log(`File upload failed: ${error.response.data.message || 'Unknown error'}`);
        } else {
            console.log('File upload request failed: ' + error.message);
        }
    }
};
const beforeUpload = (file) => {
    const isExcel = /\.(xls|xlsx)$/.test(file.name);
    if (!isExcel) {
        Modal.confirm({
            title: '暂不支持该格式，请下载模板后填写重试',
            onOk: () => {
            },
        });
        // 返回 false 暂时没有上传存放地址 避免报错
        return false;
    }

    return false;  // 允许上传
}

// 计算逻辑
const totalSoftwareCosts = ref(0)
const totalWorkloads = ref({
    internal1: 0,
    demandLocalWorkload: 0,
    demandExternalWorkload: 0,
    internal2: 0,
    devLocalWorkload: 0,
    devExternalWorkload: 0,
    internal3: 0,
    testLocalWorkload: 0,
    testExternalWorkload: 0,
    internal4: 0,
    uiLocalWorkload: 0,
    uiExternalWorkload: 0,
    internal5: 0,
    pmLocalWorkload: 0,
    pmExternalWorkload: 0,
});
const updateTotal = async (selectedStage) => {
    await getCostMapFunc()
    let totals = {
        internal1: 0,  // 需求评估内部工作量
        demandLocalWorkload: 0,  // 需求评估本地外派工作量
        demandExternalWorkload: 0,  // 需求评估外地外派工作量
        internal2: 0,  // 开发评估内部工作量
        devLocalWorkload: 0,  // 开发评估本地外派工作量
        devExternalWorkload: 0,  // 开发评估外地外派工作量
        internal3: 0,  // 测试评估内部工作量
        testLocalWorkload: 0,  // 测试评估本地外派工作量
        testExternalWorkload: 0,  // 测试评估外地外派工作量
        internal4: 0,  // UI评估内部工作量
        uiLocalWorkload: 0,  // UI评估本地外派工作量
        uiExternalWorkload: 0,  // UI评估外地外派工作量
        internal5: 0,  // 项目管理内部工作量
        pmLocalWorkload: 0,  // 项目管理本地外派工作量
        pmExternalWorkload: 0,  // 项目管理外地外派工作量
    };

    const workloadSum = (workloadKey, unitKey) => {
        return data.value.reduce((sum, item) => {
            const workload = parseFloat(item[workloadKey] || 0);
            const unit = item[unitKey];
            const convertedWorkload = unit === 'HUMAN_NATURE' ? workload / 21.75 : workload; // 将人天转换为人月
            return sum + convertedWorkload;
        }, 0);
    };

    // 需求评估
    totals.internal1 = workloadSum('demandInternalWorkload', 'demandInternalUnit');
    totals.demandLocalWorkload = workloadSum('demandLocalWorkload', 'demandExternalUnit');
    totals.demandExternalWorkload = workloadSum('demandExternalWorkload', 'demandExternalUnit');

    // 开发评估
    totals.internal2 = workloadSum('devInternalWorkload', 'devInternalUnit');
    totals.devLocalWorkload = workloadSum('devLocalWorkload', 'devExternalUnit');
    totals.devExternalWorkload = workloadSum('devExternalWorkload', 'devExternalUnit');

    // 测试评估
    totals.internal3 = workloadSum('testInternalWorkload', 'testInternalUnit');
    totals.testLocalWorkload = workloadSum('testLocalWorkload', 'testExternalUnit');
    totals.testExternalWorkload = workloadSum('testExternalWorkload', 'testExternalUnit');

    // UI评估
    totals.internal4 = workloadSum('uiInternalWorkload', 'uiInternalUnit');
    totals.uiLocalWorkload = workloadSum('uiLocalWorkload', 'uiExternalUnit');
    totals.uiExternalWorkload = workloadSum('uiExternalWorkload', 'uiExternalUnit');

    // 项目管理
    totals.internal5 = workloadSum('pmInternalWorkload', 'pmInternalUnit');
    totals.pmLocalWorkload = workloadSum('pmLocalWorkload', 'pmExternalUnit');
    totals.pmExternalWorkload = workloadSum('pmExternalWorkload', 'pmExternalUnit');

    // 计算软件成本总计
    const softwareCost = totals.internal1 * projectData.RA.insiderMonth + totals.internal2 * projectData.DEV.insiderMonth + totals.internal3 * projectData.TEST.insiderMonth + totals.internal4 * projectData.UI.insiderMonth + totals.internal5 * projectData.PM.insiderMonth; // 内部工作量总计乘以每人月的费用


    // 计算外派成本
    const localExternalCost = (totals.demandLocalWorkload * projectData.RA.localPresenceDays + totals.devLocalWorkload * projectData.DEV.localPresenceDays + totals.testLocalWorkload * projectData.TEST.localPresenceDays + totals.uiLocalWorkload * projectData.UI.localPresenceDays + totals.pmLocalWorkload * projectData.PM.localPresenceDays) * 21.75;
    const remoteExternalCost = (totals.demandExternalWorkload * projectData.RA.remotePresenceDays + totals.devExternalWorkload * projectData.DEV.remotePresenceDays + totals.testExternalWorkload * projectData.TEST.remotePresenceDays + totals.uiExternalWorkload * projectData.UI.remotePresenceDays + totals.pmExternalWorkload * projectData.PM.remotePresenceDays) * 21.75;


    // 总成本
    const totalSoftwareCost = softwareCost + localExternalCost + remoteExternalCost;

    // 更新全局的 totalWorkloads 和 totalSoftwareCosts
    totalWorkloads.value = totals;
    totalSoftwareCosts.value = totalSoftwareCost;
    console.log(selectedStage, '定制开发计算')
};
// 计算人月数小计  
const totalWorkloadsSum = computed(() => {
    return (
        totalWorkloads.value.internal1 +
        totalWorkloads.value.internal2 +
        totalWorkloads.value.internal3 +
        totalWorkloads.value.internal4 +
        totalWorkloads.value.internal5
    ).toFixed(2);
});

// 计算软件定制开发成本小计  
const subtotalCost = computed(() => {
    return (
        calculateSubtotal('RA', totalWorkloads.value.internal1) +
        calculateSubtotal('UI', totalWorkloads.value.internal4) +
        calculateSubtotal('DEV', totalWorkloads.value.internal2) +
        calculateSubtotal('TEST', totalWorkloads.value.internal3) +
        calculateSubtotal('PM', totalWorkloads.value.internal5)
    ).toFixed(2);
});

// 计算单项小计（元）  
const calculateSubtotal = (role, workload) => {
    const insiderMonth = projectData[role].insiderMonth;
    return insiderMonth * workload;
};


// 计算人天数小计（总计）
const totalManDays = computed(() => {
    const localManDays = totalWorkloads.value.demandLocalWorkload +
        totalWorkloads.value.devLocalWorkload +
        totalWorkloads.value.testLocalWorkload +
        totalWorkloads.value.uiLocalWorkload +
        totalWorkloads.value.pmLocalWorkload;

    const externalManDays = totalWorkloads.value.demandExternalWorkload +
        totalWorkloads.value.devExternalWorkload +
        totalWorkloads.value.testExternalWorkload +
        totalWorkloads.value.uiExternalWorkload +
        totalWorkloads.value.pmExternalWorkload;

    return parseFloat(((localManDays + externalManDays) * 21.75).toFixed(2));
});
// 计算单项小计（元）  
const computeSubtotal = (localKey, externalKey, role) => {
    const localPresenceDays = projectData[role].localPresenceDays;  // 获取对应角色的 localPresenceDays  
    const remotePresenceDays = projectData[role].remotePresenceDays; // 获取对应角色的 remotePresenceDays  

    const localCost = localKey * localPresenceDays * 21.75;
    const externalCost = externalKey * remotePresenceDays * 21.75;
    return parseFloat((localCost + externalCost).toFixed(2)) || 0;
};

// 计算总项目成本  
const totalProjectCost = computed(() => {
    const demandSubtotal = computeSubtotal(totalWorkloads.value.demandLocalWorkload, totalWorkloads.value.demandExternalWorkload, 'RA');
    const devSubtotal = computeSubtotal(totalWorkloads.value.devLocalWorkload, totalWorkloads.value.devExternalWorkload, 'DEV');
    const testSubtotal = computeSubtotal(totalWorkloads.value.testLocalWorkload, totalWorkloads.value.testExternalWorkload, 'TEST');
    const uiSubtotal = computeSubtotal(totalWorkloads.value.uiLocalWorkload, totalWorkloads.value.uiExternalWorkload, 'UI');
    const pmSubtotal = computeSubtotal(totalWorkloads.value.pmLocalWorkload, totalWorkloads.value.pmExternalWorkload, 'PM');

    // 所有单项小计之和  
    return parseFloat((demandSubtotal + devSubtotal + testSubtotal + uiSubtotal + pmSubtotal).toFixed(2));
});

const reset = () => {
    data.value = []
    for (let key = 1; key <= 6; key++) {
        data.value.push({
            key: key,
            moduleName: '',
            subModuleName: '',
            description: '',
            demandInternalWorkload: '',
            demandInternalUnit: 'HUMAN_NATURE',
            demandLocalWorkload: '',
            demandExternalWorkload: '',
            demandExternalUnit: 'HUMAN_NATURE',
            devInternalWorkload: '',
            devInternalUnit: 'HUMAN_NATURE',
            devLocalWorkload: '',
            devExternalWorkload: '',
            devExternalUnit: 'HUMAN_NATURE',
            testInternalWorkload: '',
            testInternalUnit: 'HUMAN_NATURE',
            testLocalWorkload: '',
            testExternalWorkload: '',
            testExternalUnit: 'HUMAN_NATURE',
            uiInternalWorkload: '',
            uiInternalUnit: 'HUMAN_NATURE',
            uiLocalWorkload: '',
            uiExternalWorkload: '',
            uiExternalUnit: 'HUMAN_NATURE',
            pmInternalWorkload: '',
            pmInternalUnit: 'HUMAN_NATURE',
            pmLocalWorkload: '',
            pmExternalWorkload: '',
            pmExternalUnit: 'HUMAN_NATURE',
        });
    }
    totalWorkloads.value = {
        internal1: 0,
        demandLocalWorkload: 0,
        demandExternalWorkload: 0,
        internal2: 0,
        devLocalWorkload: 0,
        devExternalWorkload: 0,
        internal3: 0,
        testLocalWorkload: 0,
        testExternalWorkload: 0,
        internal4: 0,
        uiLocalWorkload: 0,
        uiExternalWorkload: 0,
        internal5: 0,
        pmLocalWorkload: 0,
        pmExternalWorkload: 0,
    }
    totalSoftwareCosts.value = 0
};
// 是否有暂存
watch(() => props.softwareData, (newVal) => {
    getCostMapFunc()
    if (newVal !== null) {
        data.value = newVal.quoteOpportunitiesCustomizableList
        totalWorkloads.value.internal1 = newVal.internal1
        totalWorkloads.value.demandLocalWorkload = newVal.demandLocalWorkload
        totalWorkloads.value.demandExternalWorkload = newVal.demandExternalWorkload
        totalWorkloads.value.internal2 = newVal.internal2
        totalWorkloads.value.devLocalWorkload = newVal.devLocalWorkload
        totalWorkloads.value.devExternalWorkload = newVal.devExternalWorkload
        totalWorkloads.value.internal3 = newVal.internal3
        totalWorkloads.value.testLocalWorkload = newVal.testLocalWorkload
        totalWorkloads.value.testExternalWorkload = newVal.testExternalWorkload
        totalWorkloads.value.internal4 = newVal.internal4
        totalWorkloads.value.uiLocalWorkload = newVal.uiLocalWorkload
        totalWorkloads.value.uiExternalWorkload = newVal.uiExternalWorkload
        totalWorkloads.value.internal5 = newVal.internal5
        totalWorkloads.value.pmLocalWorkload = newVal.pmLocalWorkload
        totalWorkloads.value.pmExternalWorkload = newVal.pmExternalWorkload
        totalSoftwareCosts.value = newVal.totalSoftwareCosts
        updateTotal();

    } else {
        reset()
    }
}, { immediate: true });
watch(() => props.activeKey, (newVal) => {
    if (props.softwareData) {
        console.log(newVal, props.softwareData, '定制开发')
        // if (newVal === 'software') {
        data.value = props.softwareData.quoteOpportunitiesCustomizableList
        totalWorkloads.value.internal1 = props.softwareData.internal1
        totalWorkloads.value.demandLocalWorkload = props.softwareData.demandLocalWorkload
        totalWorkloads.value.demandExternalWorkload = props.softwareData.demandExternalWorkload
        totalWorkloads.value.internal2 = props.softwareData.internal2
        totalWorkloads.value.devLocalWorkload = props.softwareData.devLocalWorkload
        totalWorkloads.value.devExternalWorkload = props.softwareData.devExternalWorkload
        totalWorkloads.value.internal3 = props.softwareData.internal3
        totalWorkloads.value.testLocalWorkload = props.softwareData.testLocalWorkload
        totalWorkloads.value.testExternalWorkload = props.softwareData.testExternalWorkload
        totalWorkloads.value.internal4 = props.softwareData.internal4
        totalWorkloads.value.uiLocalWorkload = props.softwareData.uiLocalWorkload
        totalWorkloads.value.uiExternalWorkload = props.softwareData.uiExternalWorkload
        totalWorkloads.value.internal5 = props.softwareData.internal5
        totalWorkloads.value.pmLocalWorkload = props.softwareData.pmLocalWorkload
        totalWorkloads.value.pmExternalWorkload = props.softwareData.pmExternalWorkload
        totalSoftwareCosts.value = props.softwareData.totalSoftwareCosts
        updateTotal();
        // }
    }
}, { immediate: true, deep: true })
watch(data, () => {
    updateTotal();
    totalWorkloads.value;
}, { deep: true });
// 传参
const getCustomDevData = () => {
    const requestData = data.value
        .filter(record => {
            // 筛选出有有效输入的行  
            return (
                String(record.moduleName).trim() !== '' ||
                String(record.subModuleName).trim() !== '' ||
                String(record.description).trim() !== '' ||
                (record.demandInternalWorkload && parseFloat(record.demandInternalWorkload) > 0) ||
                (record.demandLocalWorkload && parseFloat(record.demandLocalWorkload) > 0) ||
                (record.demandExternalWorkload && parseFloat(record.demandExternalWorkload) > 0) ||
                (record.devInternalWorkload && parseFloat(record.devInternalWorkload) > 0) ||
                (record.devLocalWorkload && parseFloat(record.devLocalWorkload) > 0) ||
                (record.devExternalWorkload && parseFloat(record.devExternalWorkload) > 0) ||
                (record.testInternalWorkload && parseFloat(record.testInternalWorkload) > 0) ||
                (record.testLocalWorkload && parseFloat(record.testLocalWorkload) > 0) ||
                (record.testExternalWorkload && parseFloat(record.testExternalWorkload) > 0) ||
                (record.uiInternalWorkload && parseFloat(record.uiInternalWorkload) > 0) ||
                (record.uiLocalWorkload && parseFloat(record.uiLocalWorkload) > 0) ||
                (record.pmInternalWorkload && parseFloat(record.pmInternalWorkload) > 0) ||
                (record.pmLocalWorkload && parseFloat(record.pmLocalWorkload) > 0)
            );
        })
        .map(record => ({
            moduleName: record.moduleName,
            subModuleName: record.subModuleName,
            description: record.description,
            demandInternalWorkload: record.demandInternalWorkload,
            demandInternalUnit: record.demandInternalUnit,
            demandLocalWorkload: record.demandLocalWorkload,
            demandExternalWorkload: record.demandExternalWorkload,
            demandExternalUnit: record.demandExternalUnit,
            devInternalWorkload: record.devInternalWorkload,
            devInternalUnit: record.devInternalUnit,
            devLocalWorkload: record.devLocalWorkload,
            devExternalWorkload: record.devExternalWorkload,
            devExternalUnit: record.devExternalUnit,
            testInternalWorkload: record.testInternalWorkload,
            testInternalUnit: record.testInternalUnit,
            testLocalWorkload: record.testLocalWorkload,
            testExternalWorkload: record.testExternalWorkload,
            testExternalUnit: record.testExternalUnit,
            uiInternalWorkload: record.uiInternalWorkload,
            uiInternalUnit: record.uiInternalUnit,
            uiLocalWorkload: record.uiLocalWorkload,
            uiExternalWorkload: record.uiExternalWorkload,
            uiExternalUnit: record.uiExternalUnit,
            pmInternalWorkload: record.pmInternalWorkload,
            pmInternalUnit: record.pmInternalUnit,
            pmLocalWorkload: record.pmLocalWorkload,
            pmExternalWorkload: record.pmExternalWorkload,
            pmExternalUnit: record.pmExternalUnit,
        }));
    return requestData;
};
const getTotal = () => {
    return totalSoftwareCosts
}
defineExpose({ getCustomDevData, reset, getTotal });


</script>

<style scoped>
.container {
    width: 100%;
    /* max-width: 1200px; */
    padding: 20px;
    /* overflow: auto; */
}

.container>>>.ant-card-body {
    padding: 1px !important;
    /* height: 100%; */
    width: 100%;
    z-index: 999;
    overflow-x: auto;
}

.container>>>.ant-table-container {
    width: 100%;
    overflow: auto;
}

.container>>>.ant-input {
    border: none;
    width: 150px;
}

.full-modal>>>.ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
}

.full-modal>>>.ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
}

.full-modal>>>.ant-modal-body {
    flex: 1;
}

.total {
    font-weight: bold;
    font-size: 18px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.total-cost {
    font-weight: bold;
    font-size: 14px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.total-work {
    font-weight: bold;
    font-size: 15px;
    background-color: rgba(0, 0, 0, 0.02) !important;
}

.total-work>>>.total-num {
    text-align: center;
}

/* .total-work
th,
td {
    border: 1px solid #ccc;
    padding: 8px;
}
.total-num
td[colspan="3"] {
    text-align: left;
    font-weight: bold;
} */
.section {
    margin-bottom: 20px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #ccc;
    padding: 8px;
    text-align: center;
}

th {
    background-color: #f2f2f2;
}

td[colspan="3"] {
    text-align: left;
    font-weight: bold;
}

.info p {
    margin: 5px 0;
}

.add-btn {
    margin-top: 20px;
}

.total {
    background-color: #f1f1f1;
    font-weight: 600;
}

.cost-table {
    margin-top: 20px;
    border-top: 1px solid #f0f0f0;
}

.cost-table th,
.cost-table td {
    border: 1px solid #f0f0f0;
}
</style>

<style>
.table-white-bg {
    background-color: #fff !important;
}

.table-blue-bg {
    background-color: #e6f8fe !important;
}
</style>