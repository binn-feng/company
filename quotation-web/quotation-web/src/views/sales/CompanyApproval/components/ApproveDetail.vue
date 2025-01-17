<template>
  <a-modal :open="isModalVisible" title="报价详情" @cancel="handleCancel" width='600px'>
    <a-spin v-if="loading" />
    <div v-else>
      <div class="modal-content">
        <div class="modal-item">
          <span style="width: 30%;">商机名称：</span>
          <span style="width: 65%;">{{ record.name }}</span>
        </div>
        <div class="modal-item">
          <span style="width: 30%;">客户名称：</span>
          <span style="width: 65%; ">{{ record.customersName }}</span>
        </div>
        <div class="modal-item">
          <span style="width: 30%;">所属销售：</span>
          <span style="width: 65%;">{{ record.saleName }}</span>
        </div>
        <div class="modal-item">
          <span style="width: 30%;">产品类别：</span>
          <span style="width: 65%;">{{ record.category }}</span>
        </div>
        <div class="modal-item">
          <span style="width: 30%;">所属售前：</span>
          <span style="width: 65%;">{{ record.preSaleName }}</span>
        </div>
        <div class="modal-item" v-if="props.isLoss">
          <span style="width: 30%;">丢单时间 ：</span>
          <span style="width: 65%;">{{ record.loseTime }}</span>
        </div>
        <div class="modal-item" v-if="props.isLoss">
          <span style="width: 30%;">丢单原因 ：</span>
          <span style="width: 65%;">{{ record.reasonDesc }}</span>
        </div>
        <div class="modal-item" v-if="props.isAbandon">
          <span style="width: 30%;">废弃原因 ：</span>
          <span style="width: 65%;">{{ record.abandonDesc }}</span>
        </div>
        <div style="width: 100%;" class="version-list">
          <h4>成本报价版本</h4>
          <div>
            <div class="version-list scrollable">
              <div v-if="evaluationVersions.length == 0">
                <span>无</span>
              </div>
              <div class="version-item" v-for="(version, index) in evaluationVersions" :key="version.id"
                @mouseover="isHovered = index" @mouseleave="isHovered = null">
                <span style="margin-right: 15px;">{{ version.valuationVersion }}</span>
                <span style="margin-right: 15px;"> {{ version.versionType === "INCAPABLE" ? ' - ' : '￥' +
                  formatNumber(Number(version.amount).toFixed(2)) }} </span>
                <span>{{ version.date }}</span>
                <a @click="handleVersionDetail(version.id)" style="margin-left:10px">详情</a>
              </div>
            </div>
          </div>
        </div>
        <div style="width: 100%;" class="version-list">
          <h4>销售报价版本</h4>
          <div>
            <div class="version-list scrollable">
              <div v-if="saleQouteVersions.length == 0">
                <span>无</span>
              </div>
              <div class="version-item" v-for="(version) in saleQouteVersions" :key="version.id">
                <div><span style="margin-right: 15px;">{{ version.date }}</span> <span style="margin-right: 15px;">￥{{
                  formatNumber(Number(version.amountIncl).toFixed(2))
                }}</span>
                  <span>整体利润率 : {{ version.type === "INCAPABLE" ? '-' : formatPercentage(version.projProfitRateIncl) }}
                  </span><a @click="handleSalesVersionDetail(version.id)" style="margin-left:10px">详情</a>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="!isAwaiting && record.saleAuditTime !== null" style="width: 100%;">
          <div class="modal-item" style="width: 100%;margin-top: 20px;">
            <span style="width: 20%;">报价公司审批：</span>
            <span style="width: 80%;">
              <span>{{ record.saleAuditLog ? record.saleAuditLog : '无' }}&nbsp;&nbsp;</span>
              <span>{{ formatDate(record.saleAuditTime) }}</span>
            </span>
          </div>
          <div class="modal-item" style="width: 100%;">
            <span style="width: 10%;">理由：</span>
            <span style="width: 90%;">{{ record.saleAuditDesc ? record.saleAuditDesc : '无' }}</span>
          </div>
        </div>
        <div style="width: 100%;" class="version-list" v-if="props.isWaitingSign && signVersions.length !== 0">
          <h4>签约申请</h4>
          <div>
            <div class="version-list scrollable">
              <div v-if="signVersions.length === 0">
                <span>无</span>
              </div>
              <div class="version-item" v-for="(version) in signVersions" :key="version.id">
                <div>
                  <span style="margin-right: 15px;">{{ version.date }}</span><span style="margin-right: 15px;">￥{{
                    formatNumber((version.amountIncl.toFixed(2))) }}</span>
                  <span style="margin-right: 15px;">整体利润率:{{ formatPercentage(version.projProfitRateIncl) }}</span>
                  <span>{{ getContractTypeText(version.contractType) }}</span>
                  <a @click="handleSignVersionDetail(version.id)" style="margin-left:10px">详情</a>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="props.isSignApprovalStatus && record.signAuditTime !== null" style="width: 100%;">
          <div class="modal-item" style="width: 100%;margin-top: 20px;">
            <span style="width: 20%;">签约公司审批：</span>
            <span style="width: 80%;">
              <span>{{ record.signAuditLog ? record.signAuditLog : '无' }}&nbsp;&nbsp;</span>
              <span>{{ formatDate(record.signAuditTime) }}</span>
            </span>
          </div>
          <div class="modal-item" style="width: 100%;">
            <span style="width: 10%;">理由：</span>
            <span style="width: 90%;">{{ record.signAuditDesc ? record.signAuditDesc : '无' }}</span>
          </div>
        </div>
      </div>
    </div>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">确认</a-button>
    </template>
    <CostSummary :open="isCostVisible" :name="record?.name" :opportunity="record" @closeApproval="handleQuoteCancel"
      :isApprovalCostDetailId="isApprovalCostDetailId" :isApprovalCostRoughId="isApprovalCostRoughId"
      :isApprovalCostUnableId="isApprovalCostUnableId" :isAllDetail="isAllDetail" />
    <SalesQuotation :open="isSaleQuoteModelVisible" :opportunity="record" @close="handleSaleQuoteCancel"
      :isQuoteDetail="isQuoteDetail" :selectedSalesId="selectedSalesId" :isRoughSalesQuote="isRoughSalesQuote" />
    <QuotationApproval :open="isQuotationApprovalVisible" :opportunity="record" :componentTitle="componentTitle"
      :isSignDetail="isSignDetail" @sign-detail-close="handleSignDetailClose" :selectedSignId="selectedSignId" />
  </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import CostSummary from '../../../components/costdetailquotation/components/costsummary.vue'; // 成本报价详情
import SalesQuotation from '../../SalesToQuotation/components/SalesQuotation.vue'; //销售报价
import QuotationApproval from '../../CompanyApproval/components/QuotationApproval.vue'

import { getVersion } from '@/api/costqoute';
import { getSalesVersion } from '@/api/saleQoute'
import { getSignVersion } from '@/api/pendingContract'
import { formatNumber } from '@/utils/format'

const props = defineProps({
  record: {
    type: Object,
    required: true
  },
  isModalVisible: {
    type: Boolean,
    required: true
  },
  isWaitingSign: {
    type: Boolean,
    required: false
  },
  isSignApprovalStatus: {
    type: Boolean,
    required: false
  },
  // 从报价待公司审批进入
  isAwaiting: {
    type: Boolean,
    required: false
  },
  // 从丢单进入
  isLoss: {
    type: Boolean,
    required: false
  },
  // 从废弃进入
  isAbandon: {
    type: Boolean,
    required: false
  },
});
const isCostVisible = ref(false) //成本报价
const isSaleQuoteModelVisible = ref(false) //销售报价
const isQuotationApprovalVisible = ref(false) //签约申请
const isSignDetail = ref(false) //签约申请详情
const componentTitle = ref('')
const isModalVisible = ref(false);
const isQuoteDetail = ref(false) //标识
const isAllDetail = ref(false)
// const getApprovalStatusText = (status) => {
//   const statusMap = {
//     0: '报价申请',
//     1: '待成本报价',
//     2: '待销售报价',
//     3: '待报价审批',
//     4: '销售已报价',
//     5: '待签约',
//     6: '已签约',
//     7: '丢单',
//     8: '报价审批未通过',
//     9: '签约审核未通过',
// };
//   return statusMap[status] || '未知状态'; // 如果状态不在映射中，返回 '未知状态'  
// }
const getContractTypeText = (contractType) => {
  const contractTypes = {
    1: '欣象代理',
    2: '北光直签'
  };
  return contractTypes[contractType] || '未知类型';
}
const formatDate = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleDateString();
}
const loading = ref(true);
const emit = defineEmits(['close', 'rough-click', 'detail-click']);
const evaluationVersions = ref([])
// 销售报价版本
const saleQouteVersions = ref([])
// 签约申请版本
const signVersions = ref([])
const selectedVersionData = ref('')
const isApprovalCostDetailId = ref(null)
const isApprovalCostRoughId = ref(null)
const isApprovalCostUnableId = ref(null)
const handleVersionDetail = (id) => {
  isAllDetail.value = true
  // 获取版本的所有信息  给子组件传过去在调用函数
  selectedVersionData.value = evaluationVersions.value.find(v => v.id === id);
  if (selectedVersionData.value?.versionType === 'COST') {
    //  获取详细信息
    isApprovalCostDetailId.value = id
  } else if (selectedVersionData.value?.versionType === 'ROUGH') {
    //  获取粗略报价
    isApprovalCostRoughId.value = id
  } else if (selectedVersionData.value?.versionType === 'INCAPABLE') {
    //  获取无法报价报价
    isApprovalCostUnableId.value = id
  }
  isCostVisible.value = true
  console.log(id)
}
// 获取成本报价所有版本
const getVersionFunc = async (params) => {
  try {
    // 调用 getVersion 接口
    const response = await getVersion(params);
    if (response && response.code === 200) {
      evaluationVersions.value = response.data;
    } else {
      console.error('获取版本信息失败:', response.error);
    }
  } catch (error) {
    console.error('Error fetching version data:', error);
  } finally {
    loading.value = false;
  }
};
// 获取销售报价所有版本
const getSalesVersionFunc = async (params) => {
  try {
    // 调用 getVersion 接口
    const response = await getSalesVersion(params);
    if (response && response.code === 200) {
      saleQouteVersions.value = response.data;
    } else {
      console.error('获取版本信息失败:', response.error);
    }
  } catch (error) {
    console.error('Error fetching version data:', error);
  } finally {
    loading.value = false;
  }
};
// 获取签约审批所有版本
const getSignVersionFunc = async (params) => {
  try {
    // 调用 getVersion 接口
    const response = await getSignVersion(params);
    if (response && response.code === 200) {
      signVersions.value = response.data;
    } else {
      console.error('获取版本信息失败:', response.error);
    }
  } catch (error) {
    console.error('Error fetching version data:', error);
  } finally {
    loading.value = false;
  }
};
// 查看销售报价详情
const isRoughSalesQuote = ref(false)
const selectedSalesId = ref(null)
const handleSalesVersionDetail = (id) => {
  isQuoteDetail.value = true
  selectedSalesId.value = id
  if (props.record.type === 'INCAPABLE') {
    isRoughSalesQuote.value = true
  }
  isSaleQuoteModelVisible.value = true
}
// 查看签约申请详情
const selectedSignId = ref(null)
const handleSignVersionDetail = (id) => {
  selectedSignId.value = id
  componentTitle.value = '签约申请'
  isSignDetail.value = true
  isQuotationApprovalVisible.value = true
}

watch(() => props.isModalVisible, (newVal) => {
  if (newVal) {
    isModalVisible.value = true;
    // 获取成本报价所有版本
    const versionParams = {
      latest: false,
      opportunitiesId: props.record.id,
    }
    getVersionFunc(versionParams)
    // 获取销售报价所有版本
    getSalesVersionFunc(versionParams)
    if (props.isWaitingSign) {
      // 如果是待签合同模块 签约审批版本
      getSignVersionFunc(versionParams)
    }

  } else {
    isModalVisible.value = false;
  }
});

// 按钮操作
function handleSignDetailClose() {
  isSignDetail.value = false
  isQuotationApprovalVisible.value = false
}
function handleCancel() {
  loading.value = true;
  isModalVisible.value = false;
  emit('close'); // 发射关闭事件  
}
function handleOk() {
  loading.value = true;
  isModalVisible.value = false;
  emit('close'); // 发射关闭事件  
}
function handleQuoteCancel() {
  isApprovalCostDetailId.value = null
  isApprovalCostRoughId.value = null
  isApprovalCostUnableId.value = null
  isCostVisible.value = false
}
const handleSaleQuoteCancel = () => {
  isRoughSalesQuote.value = false
  isQuoteDetail.value = false;
  selectedSalesId.value = null
  isSaleQuoteModelVisible.value = false;
}
function formatPercentage(value) {
  const parsedValue = parseFloat(value);
  if (isNaN(parsedValue)) {
    return '-';  // 如果无法转换为有效数字，返回'-'
  }
  return (parsedValue * 100).toFixed(2) + '%';
}
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

.version-history {
  border: 1px solid #e0e0e0;
  padding-inline: 20px;
  padding-bottom: 20px;
  height: 150px;
  /* 固定高度 */
  overflow-y: auto;
  /* 超出内容滚动 */
}

.sale-version {
  margin: 10px 0;
}

.approval-status {
  margin: 15px 0;
}

.approval-status span {
  padding: 10px;
}

.scrollable {
  max-height: 260px;
  overflow-y: auto;
  /* border: 1px solid rgb(224, 224, 224); */
  background: #fafafa;
  padding: 16px;
}
</style>