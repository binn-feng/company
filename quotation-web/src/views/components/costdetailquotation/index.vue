<template>
    <div class="container">
        <div class="tab-box">
            <a-tabs v-model:activeKey="activeKey" type="card">
                <a-tab-pane key="instructions" tab="评估说明">
                    <Instructions ref="instructionsRef" :intoModel="intoModel" :introduceData="introduceData" />
                </a-tab-pane>
                <a-tab-pane key="persale" tab="售前支持小计（元）">
                    <PerSales ref="presalesRef" :presaleSupportData="presaleSupportData" :intoModel="intoModel"
                        :activeKey="activeKey" :supportRemark="supportRemark" />
                </a-tab-pane>
                <a-tab-pane key="software" tab="定制开发小计（元）">
                    <CustomDev ref="customDevRef" :softwareData="softwareData" :name="name" :intoModel="intoModel"
                        :activeKey="activeKey" />
                </a-tab-pane>
                <a-tab-pane key="product" tab="产品平台小计（元）">
                    <UniveralSubtotal :tableType="activeKey" ref="productRef" :productCostsData="productCostsData"
                        :intoModel="intoModel" />
                </a-tab-pane>
                <a-tab-pane key="self" tab="硬件小计（元）-自研">
                    <UniveralSubtotal :tableType="activeKey" ref="selfRef" :selfInnovateData="selfInnovateData"
                        :intoModel="intoModel" />
                </a-tab-pane>
                <a-tab-pane key="purchasing" tab="硬件小计（元）-外采">
                    <ExternalHardware ref="purchasingRef" :purchasingData="purchasingData" :intoModel="intoModel" />
                </a-tab-pane>
                <a-tab-pane key="implementation" tab="实施小计（元）">
                    <Implementation ref="implementationRef" :implementationData="implementationData"
                        :intoModel="intoModel" />
                </a-tab-pane>
                <a-tab-pane key="other" tab="其他小计（元）">
                    <UniveralSubtotal :tableType="activeKey" ref="otherRef" :otherData="otherData" :intoModel="intoModel" />
                </a-tab-pane>
                <template #leftExtra>
                    <a class="tabs-extra-demo-button" @click="handleBack">
                        <DoubleLeftOutlined />
                        <DoubleLeftOutlined />
                    </a>
                </template>
            </a-tabs>
            <div class="fixed-button" v-if="intoModel !== 'already'">
                <a-button type="default" @click="showCancelModal">取消</a-button>
                <a-button type="primary" @click="handleStorage">暂存报价</a-button>
                <a-button type="primary" @click="handleOpen">提交报价</a-button>
            </div>
        </div>

        <a-modal v-model:open="isCancelModalVisible" title="取消成本报价" :footer="null" :width="600" centered>
            <div style="font-size: 16px; padding: 10px 0;">
                报价信息有更新，是否保存后退出？
            </div>
            <div class="custom-modal-footer">
                <a-button type="primary" size="middle" @click="handleStay">保存并退出</a-button>
                <a-button danger type="primary" size="middle" @click="handleExitWithoutSave">不保存退出</a-button>
                <a-button type="default" size="middle" @click="handleClose">取消</a-button>
            </div>
        </a-modal>
        <CostSummary :open="isCostVisible" @close="close" :costTotalData="costTotalData" :costData="costData"
            :opportunityDatas="opportunityDatas" ref="costSummaryRef" :name="name" :qouteToCost="qouteToCost"
            :evaluation="evaluation" :opportunitiesIdUpdateBasic="opportunitiesIdUpdateBasic" @ok="ok" :isAboutMultiDept="isAboutMultiDept" :intoModel="intoModel" :versionIds="versionIds"/>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import PerSales from './components/presales.vue';
import Implementation from './components/implementation.vue';
import UniveralSubtotal from './components/universalsubtotal.vue';
import ExternalHardware from './components/externalhardware.vue';
import CustomDev from './components/customdevelopment.vue';
import CostSummary from './components/costsummary.vue';
import Instructions from './components/instructions.vue';
import { fetchCostQuoteDetail, fetchCostQuote } from '@/api/costqoute';
import { message } from 'ant-design-vue';
import { DoubleLeftOutlined } from '@ant-design/icons-vue';

const activeKey = ref('instructions');
const isCostVisible = ref(false);
const isCancelModalVisible = ref(false);
const costData = ref({}); // 用于保存所有子组件的数据 
const costTotalData = ref({}); // 用于保存所有子组件总计的数据 
const instructionsRef = ref(null);
const presalesRef = ref(null);
const customDevRef = ref(null);
const productRef = ref(null);
const selfRef = ref(null);
const purchasingRef = ref(null);
const implementationRef = ref(null);
const otherRef = ref(null);
const costSummaryRef = ref(null)

import { useRoute } from 'vue-router'; // 如果使用 Vue Router

const route = useRoute(); // 获取路由信息
const opportunityDatas = ref({});
// 初始化数据
const initialData = ref({})

// 暂存数据
const introduceData = ref(null)
const presaleSupportData = ref(null)
const softwareData = ref(null)
const productCostsData = ref(null)
const selfInnovateData = ref(null)
const purchasingData = ref(null)
const implementationData = ref(null)
const otherData = ref(null)
const evaluation = ref(null)
const supportRemark = ref(null)
// 调取接口查看是否有暂存数据
const storageData = ref({})
const getCostQuoteDetail = async (params) => {
    try {
        const response = await fetchCostQuoteDetail(params);
        if (response && response.code === 200) {
            const quoteDetails = response.data;
            // 根据获取的数据填充你的表格或者其他内容
            storageData.value = quoteDetails
            if (storageData.value === null) {
                introduceData.value = null
                presaleSupportData.value = null;
                supportRemark.value = null
                softwareData.value = null;
                productCostsData.value = null;
                selfInnovateData.value = null;
                purchasingData.value = null;
                implementationData.value = null;
                otherData.value = null;
                // 保存初始数据
                initialData.value = {
                    "quoteOpportunitiesCustomizableList": softwareData.value?.quoteOpportunitiesCustomizableList || [],
                    "quoteOpportunitiesExternalList": purchasingData.value?.quoteOpportunitiesExternalList || [],
                    "quoteOpportunitiesImplementList": implementationData.value?.quoteOpportunitiesImplementList || {},
                    "quoteOpportunitiesOtherList": otherData.value?.quoteOpportunitiesOtherList || [],
                    "quoteOpportunitiesProductList": productCostsData.value?.quoteOpportunitiesProductList || [],
                    "quoteOpportunitiesSelfList": selfInnovateData.value?.quoteOpportunitiesSelfList || [],
                    "quoteOpportunitiesSupportList": presaleSupportData.value?.quoteOpportunitiesSupportList || [],
                    "valuationDesc": '',
                    "supportRemark": '', //售前表备注
                };
            } else {
                evaluation.value = {
                    time: storageData.value.time,
                    person: storageData.value.person,
                    version: storageData.value.version
                }
                if (route.query.isBrandNew) {
                    introduceData.value = null
                    presaleSupportData.value = null;
                    supportRemark.value = null
                    softwareData.value = null;
                    productCostsData.value = null;
                    selfInnovateData.value = null;
                    purchasingData.value = null;
                    implementationData.value = null;
                    otherData.value = null;
                } else {
                    // 否则赋值为 props.storageData 的相应字段  
                    introduceData.value = storageData.value.valuationDesc
                    presaleSupportData.value = storageData.value.quoteOpportunitiesSupportVO;
                    supportRemark.value = storageData.value.supportRemark;
                    softwareData.value = storageData.value.quoteOpportunitiesCustomizableVo;
                    productCostsData.value = storageData.value.quoteOpportunitiesProductVO;
                    selfInnovateData.value = storageData.value.quoteOpportunitiesSelfVO;
                    purchasingData.value = storageData.value.quoteOpportunitiesExternalVO;
                    implementationData.value = storageData.value.quoteOpportunitiesImplement;
                    otherData.value = storageData.value.quoteOpportunitiesOtherVO;
                }

                // 保存初始数据
                initialData.value = {
                    "quoteOpportunitiesCustomizableList": softwareData.value?.quoteOpportunitiesCustomizableList || [],
                    "quoteOpportunitiesExternalList": purchasingData.value?.quoteOpportunitiesExternalList || [],
                    "quoteOpportunitiesImplementList": implementationData.value?.quoteOpportunitiesImplementList || {},
                    "quoteOpportunitiesOtherList": otherData.value?.quoteOpportunitiesOtherList || [],
                    "quoteOpportunitiesProductList": productCostsData.value?.quoteOpportunitiesProductList || [],
                    "quoteOpportunitiesSelfList": selfInnovateData.value?.quoteOpportunitiesSelfList || [],
                    "quoteOpportunitiesSupportList": presaleSupportData.value?.quoteOpportunitiesSupportList || [],
                    "valuationDesc": introduceData.value ? introduceData.value : '',
                    "supportRemark": storageData.value?.supportRemark || '', //售前表备注
                };
            }
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
};



// 查看具体版本详细信息
const detailData = ref({});
const setDetailData = () => {
    presaleSupportData.value = detailData.value.quoteOpportunitiesSupportVO;
    supportRemark.value = detailData.value.supportRemark;
    softwareData.value = detailData.value.quoteOpportunitiesCustomizableVo;
    productCostsData.value = detailData.value.quoteOpportunitiesProductVO;
    selfInnovateData.value = detailData.value.quoteOpportunitiesSelfVO;
    purchasingData.value = detailData.value.quoteOpportunitiesExternalVO;
    implementationData.value = detailData.value.quoteOpportunitiesImplement;
    otherData.value = detailData.value.quoteOpportunitiesOtherVO;
    introduceData.value = detailData.value.valuationDesc
    // 保存初始数据
    initialData.value = {
        "quoteOpportunitiesCustomizableList": softwareData.value?.quoteOpportunitiesCustomizableList || [],
        "quoteOpportunitiesExternalList": purchasingData.value?.quoteOpportunitiesExternalList || [],
        "quoteOpportunitiesImplementList": implementationData.value?.quoteOpportunitiesImplementList || {},
        "quoteOpportunitiesOtherList": otherData.value?.quoteOpportunitiesOtherList || [],
        "quoteOpportunitiesProductList": productCostsData.value?.quoteOpportunitiesProductList || [],
        "quoteOpportunitiesSelfList": selfInnovateData.value?.quoteOpportunitiesSelfList || [],
        "quoteOpportunitiesSupportList": presaleSupportData.value?.quoteOpportunitiesSupportList || [],
        "valuationDesc": introduceData.value ? introduceData.value : '',
        "supportRemark": storageData.value?.supportRemark || '', //售前表备注
    };
}

// 判断从哪里进入
const intoModel = ref('')
const sourceIdentifier = ref('');
const name = ref('')
const isQuoteToCost = ref('')
const versionIds = ref('')
onMounted(async () => {
    // 从路由的查询参数中提取数据-----不直接进入 而是先进入汇总表
    if (route.query.tab) {
        sourceIdentifier.value = route.query.source || '';
        if (sourceIdentifier.value === 'updateQuote') {
            intoModel.value = 'updateQuote'
        } else {
            intoModel.value = 'already'
        }

        const tabValue = route.query.tab.replace(/"/g, '');
        name.value = route.query.name
        activeKey.value = tabValue
        versionIds.value = route.query.versionId
        const detailDataParam = sessionStorage.getItem('detailData');
        if (detailDataParam) {
            // 解析为对象
            detailData.value = JSON.parse(detailDataParam);
            nextTick(() => {
                setDetailData()
            })
        }
    } else {
        sourceIdentifier.value = ''
        intoModel.value = ''
        const opportunityData = route.query;
        if (opportunityData) {
            opportunityDatas.value = route.query
            name.value = opportunityDatas.value.name
            const params = {
                status: 0,
                opportunitiesId: opportunityDatas.value.id,
            }
            getCostQuoteDetail(params)
        }
    }
    if (route.query.isQuoteToCost) {
        const isQuoteToCostString = route.query.isQuoteToCost.replace(/"/g, '');
        isQuoteToCost.value = (isQuoteToCostString.toLowerCase() === 'true');
    }

});
// 重置所有子组件内容
const resetAll = () => {
    instructionsRef.value?.reset && instructionsRef.value.reset();
    presalesRef.value?.reset && presalesRef.value.reset();
    customDevRef.value?.reset && customDevRef.value.reset();
    productRef.value?.reset && productRef.value.reset();
    selfRef.value?.reset && selfRef.value.reset();
    purchasingRef.value?.reset && purchasingRef.value.reset();
    implementationRef.value?.reset && implementationRef.value.reset();
    otherRef.value?.reset && otherRef.value.reset();
};

const commonData = () => {
    costData.value = {
        instructions: getInstructions(),
        quoteOpportunitiesSupportList: getSupportList(),
        supportRemark: getSupportRemark(),
        quoteOpportunitiesCustomizableList: getCustomizableList(),
        quoteOpportunitiesExternalList: getExternalList(),
        quoteOpportunitiesImplementList: getImplementList(),
        quoteOpportunitiesOtherList: getOtherList(),
        quoteOpportunitiesProductList: getProductList(),
        quoteOpportunitiesSelfList: getSelfList(),
        valuationDesc: instructionsRef.value ? instructionsRef.value.getInstructionsData() : '',
    };

    costTotalData.value = {
        presalesTotal: getPresalesTotal(),
        customDevTotal: getCustomDevTotal(),
        purchasingTotal: getPurchasingTotal(),
        implementationTotal: getImplementationTotal(),
        otherTotal: getOtherTotal(),
        productTotal: getProductTotal(),
        selfTotal: getSelfTotal(),
    };
};

// 解决tab页面根据activeKey 动态渲染如果不切换tab页面不渲染数据的问题
const getInstructions = () => {
    const instructions = instructionsRef.value ? instructionsRef.value.getInstructionsData() : '';
    return instructions ? instructions :
        (introduceData.value ?
            introduceData.value :
            '');
};

const getSupportList = () => {
    if (!presalesRef.value) { // 如果 presalesRef 为 null 或 undefined
        return presaleSupportData.value && presaleSupportData.value.quoteOpportunitiesSupportList.length
            ? presaleSupportData.value.quoteOpportunitiesSupportList
            : [];
    }

    const supportData = presalesRef.value.getSalesData();
    return supportData.length > 0 ? supportData : []; // 如果有数据则返回真实数据，否则返回空数组
};
// 单独获取售前表里面的售前说明字段
const getSupportRemark = () => {
    const supportRemark = presalesRef.value ? presalesRef.value.getSupportRemark() : '';
    return supportRemark ? supportRemark :
        (supportRemark.value ?
            supportRemark.value :
            '');
};
const getCustomizableList = () => {
    if (!customDevRef.value) { // 如果 customDevRef 为 null 或 undefined
        return softwareData.value && softwareData.value.quoteOpportunitiesCustomizableList.length
            ? softwareData.value.quoteOpportunitiesCustomizableList
            : [];
    }

    const customizableData = customDevRef.value.getCustomDevData();
    return customizableData.length > 0 ? customizableData : []; // 如果有数据则返回真实数据，否则返回空数组
};


const getExternalList = () => {
    if (!purchasingRef.value) { // 如果 purchasingRef 为 null 或 undefined
        return purchasingData.value && purchasingData.value.quoteOpportunitiesExternalList.length
            ? purchasingData.value.quoteOpportunitiesExternalList
            : [];
    }

    const externalData = purchasingRef.value.getPurchasingData();
    return externalData.length > 0 ? externalData : []; // 如果有数据则返回真实数据，否则返回空数组
};

const getImplementList = () => {
    if (!implementationRef.value) { // 如果 implementationRef 为 null 或 undefined
        return implementationData.value && Object.keys(implementationData.value).length > 0
            ? implementationData.value
            : {};
    }

    const implementData = implementationRef.value.getImplementationData();
    return Object.keys(implementData).length > 0 ? implementData : {};
};

const getOtherList = () => {
    if (!otherRef.value) { // 如果 otherRef 为 null 或 undefined
        return otherData.value && otherData.value.quoteOpportunitiesOtherList.length
            ? otherData.value.quoteOpportunitiesOtherList
            : [];
    }

    const otherSubtotalsData = otherRef.value.getOtherData();
    return otherSubtotalsData.length > 0 ? otherSubtotalsData : [];
};

const getProductList = () => {
    if (!productRef.value) { // 如果 productRef 为 null 或 undefined
        return productCostsData.value && productCostsData.value.quoteOpportunitiesProductList.length
            ? productCostsData.value.quoteOpportunitiesProductList
            : [];
    }

    const productData = productRef.value.getProductData();
    return productData.length > 0 ? productData : [];
};

const getSelfList = () => {
    if (!selfRef.value) { // 如果 selfRef 为 null 或 undefined
        return selfInnovateData.value && selfInnovateData.value.quoteOpportunitiesSelfList.length
            ? selfInnovateData.value.quoteOpportunitiesSelfList
            : [];
    }

    const selfData = selfRef.value.getSelfData();
    return selfData.length > 0 ? selfData : [];
};

const getPresalesTotal = () => {
    if (!presalesRef.value) { // 如果 presalesRef 为 null 或 undefined
        return presaleSupportData.value && presaleSupportData.value.totalCost
            ? presaleSupportData.value.totalCost
            : 0;
    }
    return presalesRef.value.getTotal().value
};

const getCustomDevTotal = () => {
    if (!customDevRef.value) { // 如果 customDevRef 为 null 或 undefined
        return softwareData.value && softwareData.value.totalSoftwareCosts
            ? softwareData.value.totalSoftwareCosts
            : 0;
    }
    return customDevRef.value.getTotal().value;
};

const getPurchasingTotal = () => {
    if (!purchasingRef.value) { // 如果 purchasingRef 为 null 或 undefined
        return purchasingData.value && purchasingData.value.totalEstimatedlCost
            ? purchasingData.value.totalEstimatedlCost
            : 0;
    }
    return purchasingRef.value.getTotal().value;
};

const getImplementationTotal = () => {
    if (!implementationRef.value) { // 如果 implementationRef 为 null 或 undefined
        return implementationData.value && implementationData.value.totalCost
            ? implementationData.value.totalCost
            : 0;
    }
    return implementationRef.value.getTotal().value;
};

const getOtherTotal = () => {
    if (!otherRef.value) { // 如果 otherRef 为 null 或 undefined
        return otherData.value && otherData.value.totalCost
            ? otherData.value.totalCost
            : 0;
    }
    return otherRef.value.getOtherTotal().value;
};

const getProductTotal = () => {
    if (!productRef.value) { // 如果 productRef 为 null 或 undefined
        return productCostsData.value && productCostsData.value.totalCost
            ? productCostsData.value.totalCost
            : 0;
    }
    return productRef.value.getProductTotal().value;
};

const getSelfTotal = () => {
    if (!selfRef.value) { // 如果 selfRef 为 null 或 undefined
        return selfInnovateData.value && selfInnovateData.value.totalCost
            ? selfInnovateData.value.totalCost
            : 0;
    }
    return selfRef.value.getSelfTotal().value;
};


const qouteToCost = ref(false)
// 从更新成本的现有版本基础上进入的时候传id  
const opportunitiesIdUpdateBasic = ref('')
const isAboutMultiDept = ref(false)
const handleOpen = () => {
    commonData()
    if (sourceIdentifier.value !== '') {
        opportunitiesIdUpdateBasic.value = detailData.value.opportunitiesId
    } else {
        opportunitiesIdUpdateBasic.value = opportunityDatas.value.id
    }
    qouteToCost.value = true
    isCostVisible.value = true;
    isAboutMultiDept.value = true
};

const handleStorage = async () => {
    try {
        commonData();
        opportunitiesIdUpdateBasic.value = sourceIdentifier.value !== '' ?
            detailData.value.opportunitiesId :
            opportunityDatas.value.id;

        const payload = {
            amount: costSummaryRef.value ? costSummaryRef.value.getTotal().value : 0,
            opportunitiesId: opportunitiesIdUpdateBasic.value,
            quoteOpportunitiesCustomizableList: costData.value.quoteOpportunitiesCustomizableList,
            quoteOpportunitiesExternalList: costData.value.quoteOpportunitiesExternalList,
            quoteOpportunitiesImplementList: costData.value.quoteOpportunitiesImplementList,
            quoteOpportunitiesOtherList: costData.value.quoteOpportunitiesOtherList,
            quoteOpportunitiesProductList: costData.value.quoteOpportunitiesProductList,
            quoteOpportunitiesSelfList: costData.value.quoteOpportunitiesSelfList,
            quoteOpportunitiesSupportList: costData.value.quoteOpportunitiesSupportList,
            status: '0',
            valuationDesc: costData.value.valuationDesc || '',
            supportRemark: costData.value.supportRemark || '', //售前表备注
        };

        const response = await fetchCostQuote(payload);
        if (response && response.code === 200) {
            message.info('暂存成功');
        } else {
            console.error('Failed to save quote:', response.error);
        }
    } catch (error) {
        console.error('Error while saving quote:', error);
    }
};
// 判断数据是否发生变化
const isDataChanged = () => {
    commonData()
    return JSON.stringify(initialData.value) !== JSON.stringify({
        quoteOpportunitiesCustomizableList: costData.value.quoteOpportunitiesCustomizableList,
        quoteOpportunitiesExternalList: costData.value.quoteOpportunitiesExternalList,
        quoteOpportunitiesImplementList: costData.value.quoteOpportunitiesImplementList,
        quoteOpportunitiesOtherList: costData.value.quoteOpportunitiesOtherList,
        quoteOpportunitiesProductList: costData.value.quoteOpportunitiesProductList,
        quoteOpportunitiesSelfList: costData.value.quoteOpportunitiesSelfList,
        quoteOpportunitiesSupportList: costData.value.quoteOpportunitiesSupportList,
        valuationDesc: costData.value.valuationDesc || '',
        supportRemark: costData.value.supportRemark || '', //售前表备注
    });
};
const showCancelModal = () => {
    if (isDataChanged()) {
        isCancelModalVisible.value = true;
    } else {
        window.close();
    }
};

const handleStay = () => {
    handleStorage()
    isCancelModalVisible.value = false;
    window.close();
};

const handleExitWithoutSave = () => {
    isCancelModalVisible.value = false;
    resetAll()
    window.close();
};

const handleClose = () => {
    isCancelModalVisible.value = false;
};

const close = () => {
    isCostVisible.value = false;
    qouteToCost.value = false
    isAboutMultiDept.value = false
    // window.close();
};
const ok = () => {
    isCostVisible.value = false;
    qouteToCost.value = false
    window.close();
};
const handleBack = () => {
    // router.push({ name: 'Dashboard' });
    window.close()
}
</script>

<style lang="less" scoped>
.custom-modal-footer {
    text-align: right;
    padding: 10px 0px;
}

.container {
    height: 100%;
    padding: 20px;
    overflow: auto;
    position: relative;
}

.fixed-button {
    margin-top: 20px;
    // position: fixed;
    text-align: right;
    margin-right: 20px;
    margin-bottom: 20px;
    z-index: 1000;
}

.ant-btn {
    margin: 3px;
}

.tabs-extra-demo-button {
    margin-right: 16px;
}
</style>
