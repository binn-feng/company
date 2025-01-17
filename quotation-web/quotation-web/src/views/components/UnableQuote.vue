<template>
  <a-modal :open="isUnableQuotationVisible" title="无法报价" @cancel="handleCancel" @ok="handleOk" width="600px">
    <div class="modal-content">
      <div class="modal-item">
        <span class="modal-item-son modal-item-son1">商机名称：</span>
        <span class="modal-item-son modal-item-son2">{{ opportunity.name }}</span>
      </div>
      <div class="modal-item" style="display: flex; align-items: flex-start; margin-top: 30px;">
        <span class="modal-item-son modal-item-son1"><span style="color:red;margin-right: 5px;">*</span>原因说明：</span>
        <span class="modal-item-son modal-item-son2">
          <a-textarea :auto-size="{ minRows: 7, maxRows: 10 }" v-model:value="quoteDesc"
            :disabled="props.isSaleUnableQoute"></a-textarea>
        </span>
      </div>
    </div>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">确认</a-button>
    </template>
  </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import { fetchUnableQuote } from '@/api/costqoute';
import { message } from 'ant-design-vue';

const props = defineProps({
  opportunity: {
    type: Object,
    required: true
  },
  isUpdateOpen: {
    type: Boolean,
    required: false
  },
  isSaleUnableQoute: {
    type: Boolean,
    required: false
  },
});

const emit = defineEmits(['close']);

const quoteDesc = ref('');

const isUnableQuotationVisible = ref(false);

watch(() => props.isSaleUnableQoute, (newVal) => {
  if (newVal) {
    quoteDesc.value = props.opportunity.quoteDesc
  } else {
    isUnableQuotationVisible.value = false;
  }
});
watch(() => props.opportunity, (newVal) => {
  if (newVal) {
    isUnableQuotationVisible.value = true;
  } else {
    isUnableQuotationVisible.value = false;
  }
});

async function handleOk() {
  if (props.isSaleUnableQoute) {
    emit('sales-close')
  } else {
    // 验证原因说明是否为空
    if (!quoteDesc.value.trim()) {
      message.info('请填写无法报价原因')
      return; // 不继续执行，提示用户填写原因说明
    }
    const quotationData = {
      id: props.opportunity.id,
      quoteDesc: quoteDesc.value,
      // type: 'INCAPABLE' 
    };

    try {
      await fetchUnableQuote(quotationData);
      console.log('无法报价报价成功:', quotationData);
      quoteDesc.value = ''
      emit('quote-sent');
      handleCancel();
    } catch (error) {
      console.error('无法报价失败:', error);
    }
  }

}

function handleCancel() {
  if (props.isSaleUnableQoute) {
    emit('sales-close') //待销售
  } else {
    emit('close'); // 更新组件和无法报价发射关闭事件  
  }
  isUnableQuotationVisible.value = false;

}

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

 .modal-item-son {
   display: inline-block;
 }

 .modal-item-son1 {
   width: 18%;
 }

 .modal-item-son2 {
   width: 80%;
 }
</style>