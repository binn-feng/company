<template>
    <div class="container">
        <a-card>
            <template #title>
                <span class="fs16">评估说明</span>
            </template>
            <a-textarea v-model:value="valuationDesc" :placeholder="props.intoModel === 'already' ? '' : '请输入评估说明'" :rows="16"
                :disabled="props.intoModel === 'already'" :maxLength="200" />
            <div class="character-count"> <!-- Display character count -->
                <span>剩余 {{ remainingChars }} 字符</span>
            </div>
        </a-card>
    </div>
</template>

<script setup>
import { anyType } from 'ant-design-vue/es/_util/type';
import { ref, defineExpose, defineProps, watch, computed } from 'vue'
const props = defineProps({
    intoModel: {
        type: String,
        required: false
    },
    introduceData: anyType
});
watch(() => props.intoModel, (newVal) => {
    if (newVal === 'already') {
        console.log(newVal)
    }
})
const valuationDesc = ref('')
watch(() => props.introduceData, (newVal) => {
    if (newVal) {
        valuationDesc.value = newVal
        console.log(newVal, valuationDesc.value)

    }
})
const maxChars = 200;
const remainingChars = computed(() => maxChars - valuationDesc.value.length);
const getInstructionsData = () => {
    const dec = valuationDesc.value
    console.log('introduce---', dec, valuationDesc.value)
    return valuationDesc.value;
};
const reset = () => {
    valuationDesc.value = ''
}

defineExpose({ getInstructionsData, reset });
</script>

<style lang='less' scoped>
.container {
    width: 100%;
    /* max-width: 1200px; */
    padding: 20px;
    overflow: auto;
}

.character-count {
    margin-top: 10px;
    font-size: 12px;
    color: #888;
    position: absolute;
    right: 20px;
    bottom: 5px;
}
</style>