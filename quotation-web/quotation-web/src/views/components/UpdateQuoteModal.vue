<template>
  <a-modal title="更新报价" :open="isUpdateModalVisible" @cancel="handleCancel" @ok="handleOk" width="600px">
    <div>
      <div>
        <strong style="margin-top: 16px;display: inline-block;width: 20%;">商机名称：</strong> 
        {{ opportunity.name}}
      </div>
      <div v-if="opportunity.quoteDesc">
        <strong style="margin-top: 16px;display: inline-block;width: 20%;">更新报价说明：</strong> 
        {{ opportunity.quoteDesc}}
      </div>
      <div>
        <strong style="margin-top: 16px;display: inline-block;width: 20%;">全新报价：</strong>
        <a-switch v-model:checked="isNewQuote" />
      </div>
      <div v-if="!isNewQuote" style="margin-top: 16px;">
        <strong style="display: block; margin-bottom: 10px;">成本报价版本：</strong>
        <div>
          <div class="scrollable">
            <div v-if="evaluationVersions.length === 0">
              <span>无历史版本</span>
            </div>
            <a-radio-group v-model:value="selectedVersion">
              <div v-for="(version, index) in evaluationVersions" :key="version.id" @mouseover="isHovered = index"
                @mouseleave="isHovered = null" style="position: relative; margin-bottom:12px;">
                <a-radio :value="version.id">
                  <span style="padding-inline: 1px;margin-right: 20px;">{{ version.valuationVersion
                  }}</span>

                  <span style="padding-inline: 1px;margin-right: 30px;">{{ version.versionType === "INCAPABLE" ? '  -  ' : '￥' +  formatNumber(version.amount) }}</span>

                  <span style="padding-inline: 1px;margin-right: 30px;">{{ version.date }}</span>

                  <span style="padding-inline: 1px;">{{ version.preSaleName }}</span>
                </a-radio>
              </div>
            </a-radio-group>
          </div>
        </div>
      </div>
      <div v-else style="margin-top: 16px;">
        <strong style="margin-top: 16px;display: inline-block;width: 15%;">报价类型：</strong>
        <a-radio-group v-model:value="selectedQuoteType">
          <a-radio :value="'粗略报价'">粗略报价</a-radio>
          <a-radio :value="'详细报价'">详细报价</a-radio>
          <a-radio :value="'无法报价'" v-if="!props.unable">无法报价</a-radio>
        </a-radio-group>
      </div>
    </div>
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">确认</a-button>
    </template>
    <RoughQuotation v-model:open="isRoughQuotationVisible" :opportunity="opportunity" @close="handleUpdateRoughClose" :isRoughQuotationVisible="isRoughQuotationVisible"/>
    <RoughView v-model:open="isRoughViewVisible" :opportunity="opportunity" @close="handleRoughCancel"
      @ok="handleUpdateRough" :selectedVersionFromParent="selectedVersionFromParent" :isFromUpdateQuote="true"
      @handleSelect="handleSelect" />
    <UnableView v-model:open="isUnableModalVisible" :opportunity="opportunity" @close="isUnableModalVisible = false"
      :isUpdateOpen="isUpdateOpen" @quote-sent="handleQuoteClose" />
    <CostSummary :open="isCostVisible" :name="opportunity.name" :updateQuote="updateQuote"
      :updateVersionId="updateVersionId" :updateVersionData="updateVersionData" :opportunity="opportunity"
      @closeUpdate="handleCloseUpdate" />
    <Modal v-model:visible="isModalVisible" title="暂存数据存在" @cancel="handelCancel">
      <p>当前有暂存数据，是否继续在暂存数据上进行修改？</p>
      <template #footer>
        <a-button key="back" type="primary" @click="handleCustomOk">是</a-button>
        <a-button key="back" @click="handleCustomCancel">否</a-button>
      </template>
    </Modal>
  </a-modal>
</template>
  
<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import { anyType } from 'ant-design-vue/es/_util/type';
// import { useStore } from 'vuex';
import RoughQuotation from './RoughQuotation.vue';
import RoughView from '../cost/QuotedAtCost/components/RoughDetail.vue'; //废弃
import UnableView from '../components/UnableQuote.vue'
import CostSummary from '../components/costdetailquotation/components/costsummary.vue';//版本基础上修改使用
import { formatNumber } from '@/utils/format'
import { fetchCostQuoteDetail } from '@/api/costqoute';
import { Modal } from 'ant-design-vue';

const props = defineProps({
  opportunity: {
    type: Object,
    required: true,
  },
  versionData: anyType,
  unable: {
    Boolean,
    require: false
  },
  isAwaitingModel: {
    Boolean,
    require: false
  },
});

const emit = defineEmits(['close']);
const isUpdateModalVisible = ref(false)


const isNewQuote = ref(false);
const selectedVersion = ref(''); // 默认选择第一个版本

const isRoughViewVisible = ref(false)
let selectedVersionFromParent = ref('')
const selectedQuoteType = ref('');
const isCostVisible = ref(false) //查看已有版本
const updateQuote = ref(false)
// 列出所有版本
const evaluationVersions = ref([
])
watch(() => props.versionData, (newVal) => {
  if (newVal !== null) {
    evaluationVersions.value = newVal
  } else {
    evaluationVersions.value = [
    ]
  }
});
// 版本基础上更新
const updateVersionId = ref('')
const updateVersionData = ref('')
// const isUpdataView = ref(false) //更新报价版本基础上修改第一次进入汇总表时候确认按钮取消
watch(() => selectedVersion.value, (newVersion) => {
  updateVersionId.value = newVersion
  const foundQuotation = evaluationVersions.value.find(v => v.id === newVersion); //选中版本的id
  updateVersionData.value = foundQuotation
  if (foundQuotation) {
    isCostVisible.value = true
    updateQuote.value = true
    if (foundQuotation?.versionType === 'ROUGH') {
      selectedVersionFromParent.value = newVersion
    } else if (foundQuotation?.versionType === 'COST') {
      selectedVersionFromParent.value = newVersion
    }
  } else if (foundQuotation?.versionType === 'INCAPABLE') {
    // isUnableModalVisible.value = true 
  }

});

// 监听props中的opportunity变化以控制modal显示  
watch(() => props.opportunity, (newVal) => {
  if (newVal) {
    isUpdateModalVisible.value = true;
    selectedQuoteType.value = ''
  } else {
    isUpdateModalVisible.value = false;
  }
});

const handleOk = () => {
  // 处理确认逻辑
  isUpdateModalVisible.value = false
  selectedVersion.value = ''
  isNewQuote.value = false
  selectedQuoteType.value = ''
  emit('close'); // 关闭模态框  传递改变父组件模态框状态

};
const handleCancel = () => {
  isUpdateModalVisible.value = false
  selectedVersion.value = ''
  isNewQuote.value = false
  selectedQuoteType.value = ''
  emit('close'); // 关闭模态框
};

// 完全更新时model的显示
const isModalVisible = ref(false); // 控制 Modal 是否可见
const isUpdateOpen = ref(false)
const isRoughQuotationVisible = ref(false);
const isUnableModalVisible = ref(false)
watch(() => selectedQuoteType.value, async (newVal) => {
  isNewQuote.value = false
  isUpdateOpen.value = true
  if (newVal === '粗略报价') {
    emit('close');
    isRoughQuotationVisible.value = true;
  } else if (newVal === '详细报价') {
    if (props.isAwaitingModel) {
      const params = {
        status: 0,
        opportunitiesId: props.opportunity.id, // 将字符串转换为整数
      }
      try {
        // 在这里使用await  
        const response = await fetchCostQuoteDetail(params);
        console.log(response);
        if (response && response.data !== null) {
          isModalVisible.value = true;
          selectedQuoteType.value -= ''
        } else {
          openNewWindow()
        }
      } catch (error) {
        console.error('Error fetching cost quote detail:', error);
      }
    } else {
      openNewWindow()

    }
  } else if (newVal === '无法报价') {
    isUnableModalVisible.value = true
    emit('close');
  }
});
const handleCustomOk = () => {
  openNewWindow(); // 进行操作  
  isModalVisible.value = false; // 关闭 Modal  
};

const handleCustomCancel = () => {
  const isBrandNew = true; // 选择创建新的报价  
  openNewWindow(isBrandNew); // 进行操作  
  isModalVisible.value = false; // 关闭 Modal  
};
const openNewWindow = (isBrandNew) => {
  emit('close');
  let queryString = new URLSearchParams(Object.entries(props.opportunity)).toString();
  // 检查isBrandNew是否存在，如果存在，添加到查询字符串中  
  if (isBrandNew !== undefined) {
    queryString += `&isBrandNew=${encodeURIComponent(isBrandNew)}`;
  }

  /// 打开新窗口时传递queryString  
  const newWindow = window.open(`/costdetailquotation?${queryString}`, '_blank');

  if (!newWindow) {
    alert('新窗口未能打开，请检查浏览器的弹窗设置。');
    return;
  }

  isRoughQuotationVisible.value = false;
  // 定时检查新窗口是否关闭  
  const checkWindowClosed = setInterval(() => {
    if (newWindow.closed) {
      clearInterval(checkWindowClosed);
      // 当新窗口关闭时刷新当前页面  
      window.location.reload();
    }
  }, 1000);
}
const handleSelect = (v) => {
  selectedVersion.value = v
}
const handleCloseUpdate = () => {
  isCostVisible.value = false
  updateQuote.value = false
  isNewQuote.value = false
  selectedVersion.value = ''
  emit('close'); 
}
const handleQuoteClose = () => {
  isUpdateModalVisible.value = false
  selectedVersion.value = ''
  isNewQuote.value = false
  selectedQuoteType.value = ''
  emit('close'); 
}
const handleUpdateRoughClose = () => {
  isRoughQuotationVisible.value = false
  selectedVersion.value = ''
  isNewQuote.value = false
  emit('close');
}
</script>
  
<style scoped>
.scrollable {
  max-height: 260px;
  overflow-y: auto;
  /* border: 1px solid rgb(224, 224, 224); */
  background: #fafafa;
  padding: 16px;
}
</style>