<template>
  <a-modal :open="isRoughQuotationVisible" title="粗略报价" @cancel="handleCancel" @ok="handleOk" width='60%'>
    <div class="modal-content">
      <div class="modal-item" style="display: flex; justify-content: space-between; ">
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1">商机名称：</span>
          <span class="modal-item-son modal-item-son2">{{ opportunity.name }}</span>
        </div>
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1">客户名称：</span>
          <span class="modal-item-son modal-item-son2">{{ opportunity.customersName }}</span>
        </div>
      </div>
      <div class="modal-item" style="display: flex; justify-content: space-between; ">
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1">所属销售：</span>
          <span class="modal-item-son modal-item-son2">{{ opportunity.saleName }}</span>
        </div>
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1">产品类别：</span>
          <span class="modal-item-son modal-item-son2">{{ opportunity.category }}</span>
        </div>
      </div>
      <div class="modal-item" style="display: flex; justify-content: space-between;">
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1">所属售前：</span>
          <span class="modal-item-son modal-item-son2">{{ opportunity.preSaleName }}</span>
        </div>
        <div style="flex: 1;">
          <span class="modal-item-son modal-item-son1" style="width: 30%;vertical-align: middle">粗略报价总额：</span>
          <span class="modal-item-son modal-item-son2">
            <a-input-number v-if="hasInputAmount" v-model:value="quotationAmount" disabled :controls="false" :step="0.01"
              style="width: 100%;"></a-input-number>
            <a-input-number v-else v-model:value="quotationHandleAmount" :controls="false" :step="0.01"
              style="width: 100%;" disabled></a-input-number>
          </span>
        </div>
      </div>

      <div class="rough-quote">
        <a-table size="small" :dataSource="data" :pagination="false" :columns="columns" row-key="key" bordered>
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'amount'">
              <a-input-number v-model:value="record[column.dataIndex]" :step="0.01" :controls="false" placeholder="请输入"
                :bordered="false" @change="updateHasInputAmount"></a-input-number>
            </template>
            <template v-if="column.dataIndex === 'valuationDesc'">
              <a-input v-model:value="record[column.dataIndex]" placeholder="请输入" :bordered="false"></a-input>
            </template>
          </template>
        </a-table>
      </div>

      <div class="modal-item" style="display: flex; align-items: flex-start; padding-top:20px;">
        <span style="width:15%"><span style="color: red;margin-right: 5px;"> *</span>粗略报价说明：</span>
        <span style="width:80%">
          <a-textarea v-model:value="valuationDesc" :auto-size="{ minRows: 3, maxRows: 5 }"
            class="modal-item-son modal-item-son2" style="width:100%"></a-textarea>
        </span>
      </div>  
    </div>
    <div v-if="opportunity.isMultiDept == true">
        <div style="width:100%"><span style="color: red;margin-right: 5px;"> *</span>产品项目组占比：</div>
        <div class="modal-item" style="display: flex; align-items: flex-start; padding-inline:20px; padding-block: 10px;">
          <div style="width:100%">
            <template v-for="(dept, index) in deptData" :key="index">
              <div style="display: flex; align-items: center; margin-bottom: 10px;">
                <span style="flex: 1;">{{ dept.deptName }}</span>
                <span style="flex: 1;">{{ dept.userNames }}</span>
                <span style="flex: 1;">
                  <a-input-number v-model:value="percentages[index]" :controls="false" :step='0.01' :precision="2" /> %
                </span>
              </div>
            </template>
          </div> 
        </div>
      </div>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">确定粗略报价</a-button>
    </template>
  </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, computed } from 'vue';
import { anyType } from 'ant-design-vue/es/_util/type';
// import { useStore } from 'vuex';  
import { fetchRoughQuote, getDept } from '@/api/costqoute';  // 引入接口  
import { message } from 'ant-design-vue'
const props = defineProps({
  opportunity: {
    type: Object,
    required: true
  },
  isRoughDetail: Boolean,
  // 从已成本进入
  costDetail: {
    type: Boolean,
    required: false,
  },
  versionData: anyType,
  isRoughQuotationVisible: {
    type: Boolean,
    required: false,
  },
});

const columns = [
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
let data = ref([
  { key: 1, item: '售前支持小计（元）', amount: '', valuationDesc: '' },
  { key: 2, item: '定制开发小计（元）', amount: '', valuationDesc: '' },
  { key: 3, item: '产品平台小计（元）', amount: '', highlight: true, valuationDesc: '' },
  { key: 4, item: '硬件小计（元） - 自研', amount: '', valuationDesc: '' },
  { key: 5, item: '硬件小计（元） - 外采', amount: '', valuationDesc: '' },
  { key: 6, item: '实施小计（元）', amount: '', valuationDesc: '' },
  { key: 7, item: '其他小计（元）', amount: '', valuationDesc: '' },
]);
const emit = defineEmits(['close']);
const isRoughQuotationVisible = ref(false);
const valuationDesc = ref('');
const quotationAmount = ref(null);
const hasInputAmount = ref(false);
const quotationHandleAmount = ref('')
const deptData = ref([]);  // 部门数据
const percentages = ref([]);
const updateHasInputAmount = () => {
  hasInputAmount.value = data.value.some(item => item.amount && item.amount > 0);
};  
watch(() => props.opportunity, async (newVal) => {
  isRoughQuotationVisible.value = !!newVal;
  hasInputAmount.value = false;
  if (newVal.isMultiDept) {
    console.log(newVal.isMultiDept)
    try {
      const res = await getDept({ 'opportunitiesId': newVal.id });
      deptData.value = res; 
      percentages.value = new Array(res.length).fill(0);
    } catch (error) {
      message.error("获取部门数据失败");
    }
  }
});

watch(() => props.isRoughQuotationVisible, async (newVal) => {
   if(newVal) {
    if (props.opportunity.isMultiDept) {
   
    try {
      const res = await getDept({ 'opportunitiesId': props.opportunity.id });
      deptData.value = res; 
      percentages.value = new Array(res.length).fill(0);
    } catch (error) {
      message.error("获取部门数据失败");
    }
  }
   }

});
quotationAmount.value = computed(() => {
  return data.value.reduce((sum, item) => sum + parseFloat(item.amount || 0), 0).toFixed(2);
});
const reset = () => {
  valuationDesc.value = ''
  quotationAmount.value = null;
  hasInputAmount.value = false;
  quotationHandleAmount.value = ''
  data.value = [
    { key: 1, item: '售前支持小计（元）', amount: '', valuationDesc: '' },
    { key: 2, item: '定制开发小计（元）', amount: '', valuationDesc: '' },
    { key: 3, item: '产品平台小计（元）', amount: '', highlight: true, valuationDesc: '' },
    { key: 4, item: '硬件小计（元） - 自研', amount: '', valuationDesc: '' },
    { key: 5, item: '硬件小计（元） - 外采', amount: '', valuationDesc: '' },
    { key: 6, item: '实施小计（元）', amount: '', valuationDesc: '' },
    { key: 7, item: '其他小计（元）', amount: '', valuationDesc: '' },
  ]
  quotationAmount.value = computed(() => {
    return data.value.reduce((sum, item) => sum + (parseFloat(item.amount) || 0), 0).toFixed(2);
  });
}
async function handleOk() {
  const radioList= ref([])
  if (props.opportunity.isMultiDept) {
            console.log(percentages.value)
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
 
  // const missingDescriptions = data.value.filter(item => !item.valuationDesc || item.valuationDesc.trim() === '');
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

  data.value.forEach(item => {
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
  const totalAmount = parseFloat(quotationAmount.value.value || 0);
  const handleAmount = parseFloat(quotationHandleAmount.value || 0);
  if (totalAmount <= 0 && handleAmount <= 0) {
    message.error('粗略报价不能为空且必须大于零');
    return;
  }
  if (valuationDesc.value === '') {
    message.error('请填写报价说明');
    return;
  }
  const totalQuotationAmount = parseFloat(quotationAmount.value.value);
  const quotationData = {
    radioList:radioList.value,
    amount: totalQuotationAmount ? totalQuotationAmount : quotationHandleAmount.value, // 确保是数字类型
    opportunitiesId: props.opportunity.id,
    valuationDesc: valuationDesc.value,
    quoteOpportunitiesRoughDetails: [
      formData
    ] // 直接赋值 formData
  };

  try {
    const res = await fetchRoughQuote(quotationData); // 调用粗略报价接口
    console.log('粗略报价成功:', formData);
    if (res.code === 200) {
      message.success('粗略报价成功')
    }
    reset()
    emit('quote-sent'); // 通知父组件重载数据
    handleCancel(); // 关闭 modal
  } catch (error) {
    console.error('粗略报价失败:', error);
  }
}


function handleCancel() {
  reset()
  emit('close'); 
}  
</script>
<style scoped>
.modal-content {
  padding: 20px;
}

.modal-flex {
  display: flex;
  justify-content: space-between;
}

.modal-left,
.modal-right {
  width: 45%;
  /* 控制左右两列的宽度 */
}

.modal-item {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.modal-item-son {
  display: inline-block;
}

.modal-item-son1 {
  width: 20%;
}

.modal-item-son2 {
  /* width: 80%; */
  /*右侧内容宽度 */
}

.modal-content>>>.ant-table-cell {
  padding: 0;
}

.rough-quote {
  margin-top: 20px;
}

.rough-quote>>>.ant-input-number {
  width: 100%
}
</style>