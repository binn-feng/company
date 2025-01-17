<template>
  <a-modal :open="isModalVisible" title="商机详情" @cancel="handleCancel" width="600px">
    <div class="modal-content">
      <div class="modal-item">
        <span style="width: 15%;">商机名称：</span>
        <span style="width: 85%;font-weight: 700;">{{ opportunity.name }}</span>
      </div>
      <div class="modal-item">
        <span style="width: 15%;">客户名称：</span>
        <span style="width: 85%;">{{ opportunity.customersName }}</span>
      </div>
      <div class="modal-item">
        <span style="width: 15%;">所属销售：</span>
        <span style="width: 85%;">{{ opportunity.saleName }}</span>
      </div>
      <div class="modal-item">
        <span style="width: 15%;">产品类别：</span>
        <span style="width: 85%;">{{ opportunity.category }}</span>
      </div>
      <div class="modal-item">
        <span style="width: 15%;">所属售前：</span>
        <span style="width: 85%;">{{ opportunity.preSaleName }}</span>
      </div>
      <div class="modal-item" style="display: flex; align-items: flex-start;">
        <span style="width: 15%;">商机介绍：</span>
        <span style="width: 85%;">{{ localOpportunity.introduce }}</span>
      </div>
      <!-- <div class="modal-item">
        <span style="width: 15%;">相关附件：</span>
        <span style="width: 85%;">
          <a-button class="nomp" type="link" @click="downloadFile">下载</a-button>
        </span>
      </div> -->
      <div v-if="hasRequiredPermissions">
        <div class="select-content" v-if="!opportunity.methodSelected && opportunity.status === '1'">
        <p class="text text-color">该成本报价需多部门协调，请线下沟通，统一报价</p>
        <p class="text">请确认是否由你统一报价？ </p>
        <a-radio-group v-model:value="selectedOption">
          <a-radio value="self">由我统一报价 </a-radio>
          <a-radio value="other">对方统一报价报价</a-radio>
        </a-radio-group>
      </div>
      <div class="select-content" v-if="opportunity.quoteByOthers && opportunity.status === '1'">
        <p class="text text-color">该成本报价需多部门协调，请线下沟通，统一报价</p>
        <a-radio-group v-model:value='other'>
          <a-radio value="other" disabled>对方统一报价报价</a-radio>
        </a-radio-group>
      </div>
      <div class="select-content"   v-if="selectedOption === 'self' && !opportunity.quoteByOthers && opportunity.status === '1' && !(!opportunity.methodSelected && opportunity.status === '1') && opportunity.isMultiDept">
        <p class="text text-color">该成本报价需多部门协调，请线下沟通，统一报价</p>
        <a-radio-group v-model:value='self'>
          <a-radio value="self" disabled>由我统一报价 </a-radio>
        </a-radio-group>
      </div>
      </div>
    </div>
    <template #footer>
      <a-button @click="handleUnable('incapable')" v-hasPermi="['quote:await:unable:quote']"
        v-if="selectedOption === 'self' && !opportunity.quoteByOthers && opportunity.status === '1'">无法报价</a-button>
      <a-button @click="handleRough('rough')" v-hasPermi="['quote:await:rough:quote']"
        v-if="selectedOption === 'self' && !opportunity.quoteByOthers && opportunity.status === '1'">粗略报价</a-button>
      <a-button type="primary" @click="handleCost('cost')" v-hasPermi="['quote:await:detail:quote']"
        v-if="selectedOption === 'self' && !opportunity.quoteByOthers && opportunity.status === '1'">详细报价</a-button>
      <a-button @click="handleCancel"
        v-if="(selectedOption === 'other' && !opportunity.quoteByOthers) || (opportunity.quoteByOthers && opportunity.status === '1') || opportunity.status !== '1'"
        type="default">取消</a-button>
      <a-button @click="handleOk"
        v-if="(selectedOption === 'other' && !opportunity.quoteByOthers) &&  opportunity.status === '1'"
        type="primary">确定</a-button>
    </template>
  </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import { computed } from 'vue'; 
import { quoteByOthers, quoteBySelf } from '@/api/costqoute'
import { anyType } from 'ant-design-vue/es/_util/type';
// import auth from '@/plugins/auth';
// import { saveAs } from 'file-saver';
// import { useStore } from 'vuex';  

// const store = useStore(); 
const props = defineProps({
  opportunity: {
    type: Object,
    required: true
  },
  crmUserId: {
    type: anyType,
    required: false
  },
});
const other = ref('other')
const self = ref('self')
const selectedOption = ref('self');
// 定义将要发射的事件  
const emit = defineEmits(['close', 'rough-click', 'detail-click']);

const isModalVisible = ref(false);
const localOpportunity = ref({ ...props.opportunity });
// 权限列表  
// 定义需要检查的权限  
const permissions = JSON.parse(localStorage.getItem('permissions'));

const requiredPermissions = [  
  'quote:await:detail:quote',  
  'quote:await:rough:quote',  
  'quote:await:unable:quote'  
];  

// 计算属性：检查用户是否拥有任意一个权限  
const hasRequiredPermissions = computed(() => {  
  if (permissions.includes('*:*:*')) {  
    return true;  
  } 
  return requiredPermissions.some(permission => permissions.includes(permission));  
}); 
// 监听props中的opportunity变化以控制modal显示  
watch(() => props.opportunity, (newVal) => {
  if (newVal) {
  console.log(permissions,'permissionsDatas')

    localOpportunity.value = { ...newVal };
    isModalVisible.value = true;
  } else {
    isModalVisible.value = false;
  }
});

async function handleCost(param) {
  isModalVisible.value = false;
  if (!props.opportunity.methodSelected) {
    await quoteBySelf({ "opportunitiesId": props.opportunity.id });
  }
  // emit('close');  
  emit('handleCost', param)
}
async function handleRough(param) {
  isModalVisible.value = false;
  if (!props.opportunity.methodSelected) {
    await quoteBySelf({ "opportunitiesId": props.opportunity.id });
  }
  // emit('close',);  
  emit('handleRough', param)
}
async function handleUnable(param) {
  isModalVisible.value = false;
  if (!props.opportunity.methodSelected) {
    await quoteBySelf({ "opportunitiesId": props.opportunity.id });
  }
  // emit('close',);   
  emit('handleUnable', param)
}
async function handleOk() {
  isModalVisible.value = false;
  if (selectedOption.value === 'other') {
    await quoteByOthers({ "opportunitiesId": props.opportunity.id });
  }
  selectedOption.value = 'self'
  emit('close');
}
const handleCancel = () => {
  selectedOption.value = 'self'
  emit('close');
}
// const downloadFile = () => {
//   const url = `/quote/file/downLoadFiles?opportunitiesId=1871093289253289985`;
//   const filename = '附件.zip';

//   download(url, filename);
// }
// function download(url, filename) {  
//   const formattedUrl = url.startsWith('/') ? url.slice(1) : url;

//   const fullUrl = 'http://192.168.122.176:8080/' + formattedUrl;

//   return fetch(fullUrl, {
//     method: 'GET',
//     headers: {
//       'Content-Type': 'application/json',
//       'Authorization': 'Bearer ' + localStorage.getItem('token'),
//     },
//   })
//     .then(async (response) => { 
//       if (!response.ok) {
//         throw new Error(`HTTP error! status: ${response.status}`);
//       } 
//       const blob = await response.blob();
//       saveAs(blob, filename);
//     })
//     .catch((error) => {
//       console.error('Download error:', error); 
//     });
// }  
</script>

<style scoped>
.modal-content {
  padding: 20px;
}

.modal-item {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

/* .modal-content {
  margin-top: 15px;
  padding-inline: 15px
} */
.select-content {
  margin-top: 10px;
}

.text {
  margin-bottom: 25px;
}

.text-color {
  color: rgb(250, 34, 38)
}
</style>