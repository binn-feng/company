<template>
    <a-modal :open="isModalVisible" title="选择外采硬件" @cancel="handleCancel" width="70%" :destroyOnClose="true">
        <div class="search">
            <a-input allowClear v-model:value="query" placeholder="请输入搜索内容" size="middle" style="width: 230px;"
                @pressEnter="handleSearch"></a-input>
            <a-button @click="handleSearch" type="primary" size="middle">搜索</a-button>
            <h4 class="tips">注：若无所需设备，请询价后在管理系统内先行添加</h4>
        </div>
        <a-table :data-source="data" :columns="visibleColumns" row-key="id" sticky
            :loading="loading" :pagination="pagination" @change="handleTableChange" :row-selection="rowSelection">
            <template #bodyCell="{ column, record, index }">
                <template v-if="column.dataIndex === 'index'">
                    <span>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'deviceDescription'">
                    <span class="ellipsis-text">
                        {{ record.deviceDescription || '-' }}
                    </span>
                </template>
                <span v-else>{{ record[column.dataIndex] || '-' }}</span>
            </template>
        </a-table>
        <template #footer>
            <a-button type="default" @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleSubmit">确认</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, computed, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { selectHardwareExtPage } from '@/api/mannageDebug'

const props = defineProps({
    selectedOptions: {
        type: [],
        required: true
    }
});

const data = ref([]);
const selectedRowKeysCur = reactive([])
const isModalVisible = ref(false);
const loading = ref(false);
const query = ref('')
const pagination = ref({
    current: 1,
    pageSize: 10,
    total: 1,
});

const columns = ref([
    { title: '序号', dataIndex: 'index', key: 'index', render: (_, __, index) => index + 1, width: '6%' },
    { title: 'id', dataIndex: 'id', key: 'id', visible: false },
    { title: '类型', dataIndex: 'category', key: 'category', width: '10%', sorter: (a, b) => a.category.localeCompare(b.category) },
    { title: '硬件名称', dataIndex: 'name', sorter: (a, b) => a.name.localeCompare(b.name), width: '12%' },
    { title: '品牌', dataIndex: 'brand', sorter: (a, b) => a.brand.localeCompare(b.brand) },
    { title: '规格型号', dataIndex: 'spec', key: 'spec', sorter: (a, b) => a.spec.localeCompare(b.spec), width: '11%' },
    { title: '单位', dataIndex: 'unit' },
    { title: '单价(元)', dataIndex: 'unitPrice', sorter: (a, b) => parseFloat(a.unitPrice) - parseFloat(b.unitPrice) },
    { title: '报价日期', dataIndex: 'date', key: 'date', sorter: (a, b) => new Date(a.date) - new Date(b.date), width: '11%' },
    { title: '设备参数', dataIndex: 'deviceDescription', key: 'deviceDescription', width: '10%' },
    { title: '备注', dataIndex: 'remark', key: 'remark' }
]);

const visibleColumns = computed(() => {
    return columns.value.filter(column => column.visible !== false);
});

const rowSelection = {
    selectedRowKeys: selectedRowKeysCur,
    onChange: (selectedRowKeys) => {
        selectedRowKeys.forEach(key => {
            if (!selectedRowKeysCur.includes(key)) {
                selectedRowKeysCur.push(key);
            }
        });
    },
    onSelect: (record, selected) => {
        if (!selected) {
            const index = selectedRowKeysCur.indexOf(record.id);
            if (index !== -1) {
                selectedRowKeysCur.splice(index, 1);
            }
        }
    }
};

onMounted(() => {
    fetchHardwareOptions()
});

watch(() => props.selectedOptions, (newVal) => {
    if (newVal && Array.isArray(newVal)) {
        const ids = newVal
            .filter(m => m.hardwareId)
            .map(m => {
                m.id = message.hardwareId;
                return m.hardwareId;
            });

        selectedRowKeysCur.splice(0, selectedRowKeysCur.length, ...ids);
    }
}, { immediate: true, deep: true });

// 获取硬件-自研下拉列表
const fetchHardwareOptions = async () => {
    loading.value = false
    try {
        const params = {
            current: pagination.value.current,
            model: {
                status: "1",
                query: query.value.trim()
            },
            order: 'descending',
            size: pagination.value.pageSize,
        };
        const response = await selectHardwareExtPage(params);
        if (response) {
            pagination.value.total = response.total;
            data.value = response.rows || []
        }
    } catch (error) {
        console.error('获取硬件列表失败:', error);
    } finally {
        loading.value = false
    }
};

const handleTableChange = (paginationProps) => {
    if (paginationProps.current) {
        pagination.value.current = paginationProps.current;
        pagination.value.pageSize = paginationProps.pageSize;
        pagination.value.total = paginationProps.total;
        fetchHardwareOptions();
    }
}

const handleSearch = () => {
    fetchHardwareOptions();
};

const emit = defineEmits(['close']);

function handleCancel() {
    isModalVisible.value = false;
    emit('close');
}

const handleSubmit = async () => {
    if (selectedRowKeysCur.length < 1) {
        message.error('请至少选择一条数据');
        return
    }
    emit('close', selectedRowKeysCur);
}
</script>

<style scoped>

.ellipsis-text {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}

.search {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 5px;
}

.tips {
    color: red;
    font-weight: 400;
    margin-left: 10px;
}
</style>
