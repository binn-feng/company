<template>
  <div>
    <a-card title="待成本报价" class="table-container" :headStyle="{ 'height': '66px' }">
      <template #extra>
        <div style="display: flex; justify-content: flex-start; align-items: center; text-align: center;">
          <div style="display: flex; align-items: center;gap: 8px;">
            <a-input allowClear v-model:value="query" placeholder="请输入搜索内容" size="middle"
              @pressEnter="handleSearch"></a-input>
            <a-button @click="handleSearch" type="primary" size="middle">搜索</a-button>
            <a-button v-hasPermi="['quote:await:sync']" type="primary" @click="confirmSynchronization" size="middle"
              :loading="loading">一键同步</a-button>
          </div>
        </div>
      </template>
      <div class="table-wrapper">
        <a-table :data-source="data" :columns="visibleColumns" row-key="key" :pagination="pagination"
          :loading="loadingTable" @change="handleTableChange">
          <template #bodyCell="{ column, record, index }">
            <template v-if="column.dataIndex === 'index'">
              <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
            </template>
            <span v-else-if="column.dataIndex === 'attachments'">
              <a-button class="nomp" type="link" @click="viewAttachments(record)">查看</a-button>
            </span>
            <span v-else-if="column.dataIndex === 'processCategory'">
              <span>
                {{ record.processCategory == '1' ? '正常流程' : (record.processCategory == '2' ? '需更新成本报价' : '') }}
              </span>
            </span>
            <span v-else-if="column.dataIndex === 'actions'" style="text-align: left;">
              <a-button v-hasPermi="['quote:await:detail']" type="link" @click="viewDetails(record)"
                class="nomp">商机详情</a-button>
              <a-button v-hasPermi="['quote:await:detail:quote']" type="link" @click="viewCost(record, false)"
                class="nomp" v-if="!record.quoteByOthers">详细报价</a-button>
              <a-button v-hasPermi="['quote:await:rough:quote']" type="link" @click="viewStrategy(record)" class="nomp"
                v-if="!record.quoteByOthers">粗略报价</a-button>
              <a-button v-hasPermi="['quote:await:unable:quote']" type="link" @click="viewUnable(record)" class="nomp"
                v-if="!record.quoteByOthers && record.processCategory !== '2'">无法报价</a-button>
            </span>
            <span v-else>{{ record[column.dataIndex] }}</span>
          </template>
        </a-table>
      </div>
    </a-card>
    <OpportunityModal v-model:open="isModalVisible" :opportunity="selectedRecord" :crmUserId='crmUserId'
      @close="handleDetailClose" @detail-click="handleDetailClick"
      @handleCost="(quoteType) => handleSelfSelectClose(quoteType)"
      @handleRough="(quoteType) => handleSelfSelectClose(quoteType)"
      @handleUnable="(quoteType) => handleSelfSelectClose(quoteType)" />
    <RoughQuotation v-model:open="isRoughQuotationVisible" :opportunity="selectedRecord" @close="handleRoughClose"
      @quote-sent="loadOpportunities" :isRoughQuotationVisible="isRoughQuotationVisible"/>
    <UnableQuote v-model:open="isUnableQuotationVisible" :opportunity="selectedRecord" @close="handleUnableClose"
      @quote-sent="loadOpportunities" />
    <QuoteSelection v-model:open="isQuoteSelectVisible" :opportunity="selectedRecord"
      @others-close="handleOtherSelectClose" @self-close="handleSelfSelectClose" @close="handleSelectClose">
    </QuoteSelection>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import OpportunityModal from '../../components/OpportunityDetail.vue';
import RoughQuotation from '../../components/RoughQuotation.vue';
import UnableQuote from '../../components/UnableQuote.vue';
import QuoteSelection from './components/QuoteSelectionModal.vue'
import { fetchOpportunities, fetchCostQuoteDetail, getVersion, syncOpportunities } from '@/api/costqoute';
import { getLoadFiles } from '@/api/common'
import { Modal } from 'ant-design-vue';
import { message } from 'ant-design-vue'


// 从 Vuex 中提取用户数据并计算属性  
const user = computed(() => {
  const storedUser = localStorage.getItem('user');
  return storedUser ? JSON.parse(storedUser) : null; // 将字符串转换为对象，如果没有数据则返回 null
});

const data = ref([]);
const loading = ref(false); // 加载状态 
const quoteType = ref('') //报价类别 去判断点击哪个按钮
const loadingTable = ref(false);


const columns = ref([
  { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
  { title: 'id', dataIndex: 'id', visible: false },
  { title: '商机名称', dataIndex: 'name', width: '10%', sorter: (a, b) => a.name.localeCompare(b.name) },
  { title: '客户名称', dataIndex: 'customersName', width: '10%', sorter: (a, b) => a.customersName.localeCompare(b.customersName) },
  { title: '所属销售id', dataIndex: 'saleId', visible: false },
  { title: '所属销售', dataIndex: 'saleName', width: '10%', sorter: (a, b) => a.saleName.localeCompare(b.saleName) },
  { title: '产品类别', dataIndex: 'category', key: 'category', sorter: (a, b) => a.category.localeCompare(b.category), width: '10%' },
  { title: '所属售前', dataIndex: 'preSaleId', width: '10%', visible: false },
  { title: '部门', dataIndex: 'isMultiDept', width: '10%', visible: false },
  { title: '所属售前', dataIndex: 'preSaleName', width: '10%', sorter: (a, b) => a.preSaleName.localeCompare(b.preSaleName) },
  // { title: '附件', dataIndex: 'attachments', width: '5%' },
  { title: '操作', dataIndex: 'actions', width: '8%', fixed: 'right', },
  { title: '商机介绍', dataIndex: 'introduce', visible: false },
]);
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
const visibleColumns = computed(() => {
  return columns.value.filter(column => column.visible !== false);
});

let isModalVisible = ref(false);
const isRoughQuotationVisible = ref(false);
const isUnableQuotationVisible = ref(false);
const selectedRecord = ref(null);
const query = ref('');
const isQuoteSelectVisible = ref(false)
const crmUserId = ref(null)
// 获取数据的函数  
const loadOpportunities = async () => {
  loadingTable.value = true
  const filters = {
    id: '',
    name: '',
    category: '',
    statusList: [1],
    parentStatusList: ['00001301'],
    query: query.value.trim()
  };
  const params = {
    current: pagination.value.current,
    model: filters,
    order: 'descending',
    size: pagination.value.pageSize,
    sort: 'id',
  };
  try {
    const response = await fetchOpportunities('pageWithCost', params);
    data.value = response.rows || [];
    // 手动默认按升序排序, 避免高亮
    data.value = [...data.value].sort(
      (a, b) => new Date(a.updateTime) - new Date(b.updateTime)
    );
    pagination.value.total = response.total;
    crmUserId.value = user.value?.crmUserId;
    console.log('crmid-----', crmUserId.value, user.value)
    if (crmUserId.value) {
      data.value.forEach(record => {
        // 如果 `nonQuoters` 包含当前用户的 crmid，则禁用报价按钮
        record.isQuotable = !record.nonQuoters.includes(crmUserId);
      });
    }
  } catch (error) {
    console.error('Error loading opportunities:', error);
  } finally {
    loadingTable.value = false
  }
};

const handleTableChange = (paginationProps) => {
  if (paginationProps.current) {
    pagination.value.current = paginationProps.current;
    loadOpportunities();
  }
}

const handleSearch = () => {
  loadOpportunities();
};
onMounted(loadOpportunities);

const confirmSynchronization = () => {
  Modal.confirm({
    title: '确认同步',
    content: '您确定要进行一键同步吗?',
    okText: '确认',
    cancelText: '取消',
    onOk: handleSynchronization,
  });
};
function viewDetails(record) {
  selectedRecord.value = record;
  isModalVisible.value = true;
}
// const viewAttachments = async (record) => {  
//       try {  
//         console.log('查看附件:', record);  
//         // 调用 API 获取附件  
//         const response = await getLoadFiles({ opportunitiesId: record.id });  

//         // 假设 response.data 包含附件的文件列表  
//         if (response && response.data) {  
//           // handleAttachments(response.data);  
//           console.log(response)
//         } else {  
//           message.warning('未找到附件文件。');  
//         }  
//       } catch (error) {  
//         console.error('加载附件失败:', error);  
//         message.error('加载附件失败，请稍后再试。');  
//       }  
//     };  
const viewAttachments = async (record) => {

  try {
    // 使用 API 获取下载链接  
    const response = await getLoadFiles(record.id);
    const fileUrl = response.data.url;
    fetch(fileUrl)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob();
      })
      .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'internal.zip'; // 指定下载的文件名  
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url); // 释放URL对象  
      })
      .catch(error => console.error('Error downloading the file:', error));
  } catch (error) {
    console.error('Error fetching file:', error);
  }
}
// 定义 getVersionFunc 函数
const versionData = ref([])
const getVersionFunc = async (params) => {
  try {
    const response = await getVersion(params);
    if (response && response.code === 200) {
      versionData.value = response.data;
    } else {
      console.error('获取版本信息失败:', response.error);
    }
  } catch (error) {
    console.error('Error fetching version data:', error);
  }
};

// 调用 fetchCostQuoteDetail 获取报价详情数据  看是否有暂存
const storageData = ref(null)
const getCostQuoteDetail = async (id) => {
  try {
    const response = await fetchCostQuoteDetail(id);
    if (response && response.code === 200) {
      const quoteDetails = response.data;
      // 根据获取的数据填充表格
      storageData.value = quoteDetails
      console.log(quoteDetails, '成本报价暂存')
    } else {
      console.error('获取报价详情失败:', response.error);
    }
  } catch (error) {
    console.error('Error fetching cost quote detail:', error);
  }
};
// 同步处理
const handleSynchronization = async () => {
  loading.value = true;
  try {
    const response = await syncOpportunities();
    if (response && response.code === 200) {
      console.log('同步成功');
      Modal.success({
        title: '同步成功', content: '一键同步完成',
        onOk: () => {
          loadOpportunities()
        }
      });
    } else {
      console.error('同步失败:', response.error);
      Modal.error({ title: '同步失败', content: '请稍后重试' });
    }
  } catch (error) {
    console.error('同步错误:', error);
    Modal.error({ title: '同步失败', content: '发生了错误，请稍后重试' });
  } finally {
    loading.value = false; // 加载结束
  }
};

function viewCost(record, boolean,isDetail) {
  quoteType.value = 'cost'
  selectedRecord.value = record;
  if (!record.methodSelected && !boolean) {
    isQuoteSelectVisible.value = true;
  } else {
    if (record.isMultiDept && !isDetail) {
      Modal.confirm({
        title: '该成本报价需多部门协调，请线下沟通，统一报价',
        content: '由我统一报价',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          handleCostWindow(record)
        },
        onCancel: () => {
          // 用户取消操作  
          console.log('用户取消了报价选择.');
        }
      });
    } else {
      handleCostWindow(record)
    }

  }
}
const handleCostWindow = (record) => {
  // 传参  
  const isQuoteToCostParam = `isQuoteToCost=${encodeURIComponent(true)}`;
  const queryString = new URLSearchParams(Object.entries(record)).toString();

  // 打开新窗口时传递queryString  
  const newWindow = window.open(`/costdetailquotation?${queryString}&${isQuoteToCostParam}`, '_blank');
  if (!newWindow) {
    message.error('新窗口未能打开，请检查浏览器的弹窗设置。');
    return;
  }

  // 定时检查新窗口是否关闭  
  const checkWindowClosed = setInterval(() => {
    if (newWindow.closed) {
      clearInterval(checkWindowClosed);
      // 当新窗口关闭时刷新当前页面  
      window.location.reload();
    }
  }, 1000);

  // 获取已有版本  
  const versionParams = {
    latest: false,
    opportunitiesId: record.id,
  };
  getVersionFunc(versionParams);

  // 成本详细报价 是否有暂存  
  const params = {
    status: 0,
    opportunitiesId: record.id, // 将字符串转换为整数  
  };
  getCostQuoteDetail(params);
  console.log('成本报价:', record);
}
function viewStrategy(record) {
  quoteType.value = 'rough'
  selectedRecord.value = record;
  if (!record.methodSelected) {
    isQuoteSelectVisible.value = true;
  } else {
    if (record.isMultiDept) {
      Modal.confirm({
        title: '该成本报价需多部门协调，请线下沟通，统一报价',
        content: '由我统一报价',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          isRoughQuotationVisible.value = true;
        },
        onCancel: () => { 
          console.log('用户取消了报价选择.');
        }
      });
    } else {
      isRoughQuotationVisible.value = true;
    }
  }
}
function handleOtherSelectClose() {
  isQuoteSelectVisible.value = false;
  loadOpportunities();
}
function handleSelectClose() {
  isQuoteSelectVisible.value = false
}
function handleDetailClose() {
  isModalVisible.value = false
  loadOpportunities()
}
function handleSelfSelectClose(param) {
  if (param) {
    isModalVisible.value = false
    quoteType.value = param
  }
  isQuoteSelectVisible.value = false;
  if (quoteType.value === 'incapable') {
    isUnableQuotationVisible.value = true;
  } else if (quoteType.value === 'rough') {
    isRoughQuotationVisible.value = true;
  } else if (quoteType.value === 'cost') {
    viewCost(selectedRecord.value, true,true)
  }
}
function viewUnable(record) {
  quoteType.value = 'incapable'
  console.log('无法报价:', record);
  selectedRecord.value = record;
  if (!record.methodSelected) {
    isQuoteSelectVisible.value = true;
  } else {
    if (record.isMultiDept) {
      Modal.confirm({
        title: '该成本报价需多部门协调，请线下沟通，统一报价',
        content: '由我统一报价',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          isUnableQuotationVisible.value = true;
        },
        onCancel: () => {
          console.log('用户取消了报价选择.');
        }
      });
    } else {
      isUnableQuotationVisible.value = true;
    }
  }
}

function handleDetailClick() {
  isModalVisible.value = false;
}
function handleUnableClose() {
  isUnableQuotationVisible.value = false
  loadOpportunities()
}
function handleRoughClose() {
  isRoughQuotationVisible.value = false
  loadOpportunities()
}
</script>

<style scoped>
.table-container {
  width: 100%;
}

.table-wrapper {
  overflow-x: auto;
}

.table-container>>>.ant-table {
  min-width: 800px;
  table-layout: auto;
}

.table-container>>>.ant-btn {
  /* padding: 0; */
  /* padding-right: 10px; */
  /* margin-inline: 6px; */
  z-index: 999;
}

@media (max-width: 768px) {
  .table-container>>>.ant-table {
    font-size: 12px;
  }

  .table-container>>>.ant-table th,
  .table-container>>>.ant-table td {
    padding: 8px 4px;
  }
}

:where(.css-dev-only-do-not-override-1p3hq3p).ant-card .ant-card-head {
  min-height: 66px !important;
}
</style>