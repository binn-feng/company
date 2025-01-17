<template>
    <a-modal :open="isQuoteSelectVisible" title="成本报价确认" @ok="handleOk" @cancel="handleCancel" width="600px">
        <div class="modal-content">
            <p class="text text-color">该成本报价需多部门协调，请线下沟通，统一报价</p>
            <p class="text">请确认是否由你统一报价？ </p>
            <a-radio-group v-model:value="selectedOption">
                <a-radio value="self">由我统一报价</a-radio>
                <a-radio value="other">对方统一报价报价</a-radio>
            </a-radio-group>
        </div>
        <template #footer>
            <a-button @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleOk">确认</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits,} from 'vue';
import { quoteByOthers,quoteBySelf } from '@/api/costqoute'
import { message } from 'ant-design-vue';
// 发射事件
const emit = defineEmits(['close']);
const props = defineProps({
    opportunity: {
      type: Object,
      required: true
    }
});
const selectedOption = ref('self');
// const isQuoteSelectVisible = ref(false);
const handleOk = async() => {
    const params = {
        "opportunitiesId": props.opportunity.id
    }
    try {
        if (selectedOption.value === 'self') {
            await quoteBySelf(params);
            message.success('将由自己报价');
            emit('self-close');
        } else if (selectedOption.value === 'other') {
            await quoteByOthers(params);
            message.success('将由对方报价');
            emit('others-close')
        }
    } catch (error) {
        message.error('选择报价人失败');
    } finally {
        // isQuoteSelectVisible.value = false;
        // emit('close');
        selectedOption.value = 'self'; 
    }
}
const handleCancel = () => {
    emit('close')
}
</script>

<style lang='less' scoped>
.modal-content{
    margin-top:15px;
    padding-inline:15px
}
.text {
    margin-bottom: 25px;
}
.text-color {
    color: rgb(250, 34, 38)
}
</style>