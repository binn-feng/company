<template>
    <a-modal title="生成销售报价单" :open="generateQuoteVisible" ok-text="确认" cancel-text="取消" @ok="handleOk"
      @cancel="handleCancel">
      <div class="modal-content">
        <div class="input-group">
          <span class="label">商机名称</span>
          <span class="value">{{ selectedData.opportunityName }}</span>
        </div>
        <div class="input-group">
          <span class="label">生成报价单</span>
          <a-radio-group v-model:value="selectedReports" class="value">
            <a-radio value="北光报单">北光报单</a-radio>
            <a-radio value="欣象报单">欣象报单</a-radio>
          </a-radio-group>
        </div>
        <div class="input-group">
          <span class="label">发送邮箱</span>
          <a-switch v-model:checked="sendEmail"/>
        </div>
        <div v-if="sendEmail" class="input-group">
          <span class="label">发送格式</span>
          <a-radio-group v-model:value="selectedFormats" class="value">
            <a-radio value="PDF" key="PDF">PDF</a-radio>
            <a-radio value="Excel" key="Excel">Excel</a-radio>
          </a-radio-group> 
        </div>
      </div>
    </a-modal>
  </template>  
    
  <script setup>
  import { ref, defineProps, defineEmits, watch } from 'vue';
  const generateQuoteVisible = ref(false);
  const selectedReports = ref('北光报单');
  const sendEmail = ref(false);
  const selectedFormats = ref('PDF');
  const props = defineProps({
    selectedData: {
      type: Object,
      required: true,
    },
  });
  const emit = defineEmits(['close']);
  watch(() => props.selectedData, (newVal) => {
    generateQuoteVisible.value = !!newVal;
  });
  const handleOk = () => {
    console.log({
      selectedReports: selectedReports.value,
      sendEmail: sendEmail.value,
      selectedFormats: selectedFormats.value,
    });
    generateQuoteVisible.value = false;
    emit('ok');
  };
  
  const handleCancel = () => {
    generateQuoteVisible.value = false;
    emit('close');
  };
  </script>
  
  <style scoped>  
  .modal-content {  
    padding: 20px;  
  }  
  
  .input-group {  
    display: flex;  
    align-items: center;  
    margin-bottom: 15px;  
  }  
  
  .label {  
    flex: 0 0 120px; /* 固定标签宽度 */  
    font-weight: bold;  
  }  
  
  .value {  
    flex: 1; /* 让值部分占据剩余空间 */  
    padding-left: 10px; /* 添加左侧内边距 */  
  }  
  
  a-radio-group {  
    margin-top: 5px;  
  }  
  </style>  