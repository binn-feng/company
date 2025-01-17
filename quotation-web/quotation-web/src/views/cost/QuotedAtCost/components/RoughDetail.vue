<template>
    <a-modal :open="isRoughQuotationVisible" title="粗略报价详情" @cancel="handleCancel" @ok="handleOk">
        <div class="modal-content">
            <div>
                <select v-model="selectedVersion" style="width:50%; height:30px; margin-bottom:20px;margin-right:10px">
                    <option value=''>请选择版本号</option>
                    <option v-for="version in evaluationVersions" :key="version.valuationVersion" :value="version.id">
                        {{ version.valuationVersion }}
                    </option>
                </select>
                <a-button @click="viewEvaluationVersion(selectedVersion)" style="margin-right:10px">查看</a-button>
            </div>
            <div>
                <div class="modal-item">
                    <span class="modal-item-son modal-item-son1">商机名称：</span>
                    <span class="modal-item-son modal-item-son2">{{ opportunity?.name }}</span>
                </div>
                <div class="modal-item">
                    <span class="modal-item-son modal-item-son1">报价说明：</span>
                    <a-textarea v-model:value="localOpportunity.quoteDesc" :auto-size="{ minRows: 3, maxRows: 5 }"
                        class="modal-item-son modal-item-son2" disabled="true"></a-textarea>
                </div>
                <div class="modal-item">
                    <span class="modal-item-son modal-item-son1">粗略报价总额：</span>
                    <a-input v-model:value="localOpportunity.amount" class="modal-item-son modal-item-son2"
                        disabled="true"></a-input>
                </div>
            </div>
        </div>
    </a-modal>
</template>  
  
<script setup>
import { anyType } from 'ant-design-vue/es/_util/type';
import { ref, defineProps, defineEmits, watch } from 'vue';
import { getRoughPage } from '@/api/costqoute'
// import { useStore } from 'vuex';  

const props = defineProps({
    opportunity: {
        type: Object,
        required: true
    },
    selectedVersionFromParent: { // 新增 prop 用于接收从父组件传入的版本  
        type: String,
        default: '',
    },
    isFromUpdateQuote: { // 新增 prop 用于判断来源  
        type: Boolean,
        default: false,
    },
    laseteRoughVersionData: anyType

});
const laseteRoughVersionData = ref([])
watch(props.laseteRoughVersionData, (newVal) => {
    laseteRoughVersionData.value = newVal
})
const emit = defineEmits(['close', 'handleSelect']);
// const store = useStore();  

const isRoughQuotationVisible = ref(false);
let selectedVersion = ref('');
const localOpportunity = ref({ ...props.opportunity });
watch(() => props.isFromUpdateQuote, (newVal) => {
    // 在这里确保你的逻辑是按照需求进行的，比如可以在 true 时显示子组件  
    if (newVal) {
        isRoughQuotationVisible.value = true; // 控制组件可见性  
    }
});
// 监听props中的opportunity变化以控制modal显示  
watch(() => props.opportunity, (newVal) => {
    localOpportunity.value = { ...newVal };
    isRoughQuotationVisible.value = !!newVal;
    if (!newVal) {
        resetData(); // 当机遇为空时重置数据  
    }
});

// 重置数据  
const resetData = () => {
    selectedVersion.value = ''; 
};

// 处理确认按钮  
function handleOk() {
    handleCancel();   
}

// 处理取消按钮  
function handleCancel() {
    isRoughQuotationVisible.value = false;
    emit('close'); 
    emit('handleSelect', '')
    resetData(); // 确保在关闭时重置数据  
}
const getRoughPageDetail = async (id) => {
    try {
        const response = await getRoughPage(id);
        if (response && response.code === 200) {
            const roughquoteDetails = response.data;
            localOpportunity.value = roughquoteDetails
        } else {
            console.error('获取报价详情失败:', response.error);
        }
    } catch (error) {
        console.error('Error fetching cost quote detail:', error);
    }
}
const viewEvaluationVersion = (param) => {
    if(selectedVersion.value === '') {
        alert('请先选择版本号')
    } 
    getRoughPageDetail({ id: param })
}; 
</script>  
  
<style scoped>  .modal-content {
      padding: 20px;
  }

  .modal-item {
      margin-bottom: 10px;
  }

  .modal-item-son {
      display: inline-block;
  }

  .modal-item-son1 {
      width: 30%;
  }

  .modal-item-son2 {
      width: 60%;
      margin-left: 5%;
  }
</style>