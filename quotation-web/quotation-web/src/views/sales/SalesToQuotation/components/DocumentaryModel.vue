<template>
    <a-modal :open="isDocumentaryModellVisible" title="是否继续跟单" @ok="handleOk" @cancel="handleCancel(false)" width="600px">
        <div class="documentary-model" >
            <span class="documentary-son1">整体利润率：</span>
            <span class="documentary-son2 son-box" :class="{
            'highlight': isProfitRateLow
        }">{{ formatPercentage(projProfitRateExcl) }}</span>
        </div>
        <div class="documentary-model">
            <span class="documentary-son1">是否继续跟单：</span>
            <span class="documentary-son2"><a-switch v-model:checked="followUp" /> </span>
        </div>
        <div class="documentary-model" v-if="!followUp">
            <span class="documentary-son1">关单说明：</span>
            <a-textarea class="documentary-son2" v-model:value="reasonDesc" placeholder="请填写关单说明" :rows="5" />
        </div>
        <div class="documentary-model" v-if="followUp">
            <span class="documentary-son1">项目概要介绍：</span>
            <div class="documentary-son2">
                {{ opportunity.introduce ? opportunity.introduce : '暂无介绍' }}
            </div>
        </div>
        <div class="documentary-model" v-if="followUp">
            <span class="documentary-son1"><span style="color: red;">*</span> 继续跟进理由：</span>
            <a-textarea class="documentary-son2" v-model:value="closeNote" placeholder="请填写继续跟进理由" :rows="5" />
        </div>
        <template #footer>
            <a-button type="default" @click="handleCancel(false)">取消</a-button>
            <a-button type="primary" @click="handleOk">确定</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, defineEmits, defineProps, computed } from 'vue';
import { fetchOpportunitiesLose } from '@/api/saleQoute';
import { message } from 'ant-design-vue';
import { anyType } from 'ant-design-vue/es/_util/type';

const followUp = ref(false); // 默认为“否”  
const closeNote = ref('');
const reasonDesc = ref('');
const emit = defineEmits(['close']);

const props = defineProps({
    projProfitRateExcl: {
        type: String,
        required: false,
    },
    opportunity: {
        type: Object,
        required: true,
    },
    externalProfit: {
        type: anyType,
        required: false,
    },
});
function formatPercentage(value) {
    const parsedValue = parseFloat(value);
    if (isNaN(parsedValue)) {
        return '-';  // 如果无法转换为有效数字，返回'-'
    }
    return (parsedValue * 100).toFixed(2) + '%';
}
const isProfitRateLow = computed(() => {
    return (
        (props.projProfitRateExcl < props.externalProfit));
});
const handleOk = async () => {
    try {
        if (followUp.value) {
            if (followUp.value && !closeNote.value.trim()) {   
                return; 
            } else {
                // 确认报价
                emit('confirmQuote', closeNote.value)
            }
        } else {
            // 调用丢单接口  
            console.log(props.opportunity)
            const quoteOpportunitiesUpdateDTO = {
                id: props.opportunity.id,
                preSaleId: props.opportunity.preSaleId,
                preSaleName: props.opportunity.preSaleName,
                reasonDesc: reasonDesc.value,
                introduce: props.opportunity.introduce,
            };
            const response = await fetchOpportunitiesLose(quoteOpportunitiesUpdateDTO);
            if (response && response.code === 200) {
                message.success('丢单处理成功');
                handleCancel(true);
            } else {
                message.error('丢单处理失败: ' + response.message);
            }
        }
    } catch (error) {
        console.error('接口调用失败:', error);
        // 处理错误，例如弹出提示框  
    } finally {
        if (followUp.value && !closeNote.value.trim()) {
            message.error('请填写继续跟进理由');
        } else {
            followUp.value = false
            emit('close'); // 在接口调用完成后关闭模态框
        }
    }
};
const handleCancel = (param) => {
    followUp.value = false
    reasonDesc.value = ''
    closeNote.value = ''
    emit('close',param);
}
</script>

<style lang='less' scoped>
.documentary-model {
    margin: 20px 0;
}

.documentary-son1 {
    display: inline-block;
    width: 20%;
    text-align: right;
    vertical-align: top;
    margin-right: 5px;
}

.documentary-son2 {
    display: inline-block;
    width: 70%;
}

.son-box {
    background-color: rgb(189, 186, 186);
    text-align: center;
}

/* 预警 */
.highlight {
    color: red;
    /* 改为红色 */
    font-weight: bold;
    /* 加粗 */
    background-color: #ffcccc;
    /* 可选：背景高亮 */
}
</style>