<template>
    <a-modal title="签约状态更新" :open="isQuotationApprovalVisible" @cancel="handleCancel" @ok="handleOk" width="600px">
        <div class="modal-content">
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">商机名称：</span>
                <span class="modal-item-son modal-item-son2">{{ opportunity.name }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">客户名称：</span>
                <span class="modal-item-son modal-item-son2">{{ opportunity.customersName }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">所属销售：</span>
                <span class="modal-item-son modal-item-son2">{{ opportunity.saleName }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">产品类别：</span>
                <span class="modal-item-son modal-item-son2">{{ opportunity.category }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">所属售前：</span>
                <span class="modal-item-son modal-item-son2">{{ opportunity.preSaleName }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">合同金额：</span>
                <span class="modal-item-son modal-item-son2">{{ Number(opportunity.signQuoteTotal).toFixed(2) }}</span>
            </div>
            <div class="modal-item">
                <span class="modal-item-son modal-item-son1">合同类型：</span>
                <span class="modal-item-son modal-item-son2">{{ getContractTypeText(opportunity.contractType) }}</span>
            </div>
            <div class="checkbox-container">
                <h3>签约状态:</h3>
                <a-checkbox-group v-model:value="selectedOptions" style="display:block">
                    <div class="checkbox-item">
                        <a-checkbox value="xxVsCust" :disabled="opportunity.contractType === '2'">欣象 vs
                            客户已签</a-checkbox>
                    </div>
                    <div class="checkbox-item">
                        <a-checkbox value="xxVsNl" :disabled="opportunity.contractType === '2'">欣象 vs 北光已签</a-checkbox>
                    </div>
                    <div class="checkbox-item">
                        <a-checkbox value="nlVsCust" :disabled="opportunity.contractType === '1'">
                            北光 vs 客户已签
                        </a-checkbox>
                    </div>
                </a-checkbox-group>
            </div>
        </div>
        <template #footer>
            <a-button @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleOk">确认</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, defineProps, watch, defineEmits } from 'vue';
import { updateStatus, statusUpdatePage } from '@/api/pendingContract'
const emit = defineEmits([]);
const props = defineProps({
    opportunity: {
        type: Object,
        required: true,
    },
    isUpdateStatusVisible: {
        type: Boolean,
        required: true,
    },
});

watch(() => props.opportunity, (newVal) => {
    console.log(newVal);
});
watch(() => props.isUpdateStatusVisible, (newVal) => {
    if (newVal) {
        getStatusUpdatePage()
    }

});
// 存储选中的复选框值  
const selectedOptions = ref([]);
const selectedOptionsId = ref(null)
const getContractTypeText = (contractType) => {
    const contractTypes = {
        1: '欣象代理',
        2: '北光直签'
    };
    return contractTypes[contractType] || '未知类型';
}
const getStatusUpdatePage = async () => {
    try {
        const response = await statusUpdatePage({ 'opportunitiesId': props.opportunity.id });
        if (response && response.code === 200) {
            if (response.data) {
                selectedOptionsId.value = response.data.id
                if (response.data.xxVsCust) {
                    selectedOptions.value.push('xxVsCust');
                }
                if (response.data.xxVsNl) {
                    selectedOptions.value.push('xxVsNl');
                }
                if (response.data.nlVsCust) {
                    selectedOptions.value.push('nlVsCust');
                }
            }
        }
    } catch (error) {
        console.error('Error fetching status update:', error);
    }
};
const handleOk = async () => {
    const payload = {
        contractType: props.opportunity.contractType,
        id: selectedOptionsId.value,
        nlVsCust: selectedOptions.value.includes('nlVsCust'),
        xxVsCust: selectedOptions.value.includes('xxVsCust'),
        xxVsNl: selectedOptions.value.includes('xxVsNl'),
    };

    try {
        const response = await updateStatus(payload);
        if (response && response.code === 200) {
            selectedOptions.value = []
            emit('close')
        }
    } catch (error) {
        console.error('Error updating status:', error);

    }
};
const handleCancel = () => {
    selectedOptions.value = []
    emit('close')
}
</script>

<style lang='less' scoped>
// .modal-content {
//     padding: 20px;
// }

// .modal-item {
//     margin-bottom: 10px;
// }
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

.modal-item-son {
    display: inline-block;
}

.modal-item-son1 {
    width: 30%;
}

.modal-item-son2 {
    width: 60%;
}

.checkbox-container {
    // margin-top: 20px;
    width: 100%;
}

.checkbox-item {
    margin-bottom: 10px;
}
</style>