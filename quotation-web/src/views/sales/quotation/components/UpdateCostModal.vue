<template>
    <a-modal :open="isUpdateCostModalVisible" title="更新报价" @cancel="handleCancel" width="600px">
        <div class="modal-content">
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">商机名称：</span>
                <span style="width: 80%;">{{ opportunity.name }}</span>
            </div>
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">产品类别：</span>
                <span style="width: 80%;">{{ opportunity.category }}</span>
            </div>
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">所属销售：</span>
                <span style="width: 80%;">{{ opportunity.saleName }}</span>
            </div>
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">客户名称：</span>
                <span style="width: 80%;">{{ opportunity.customersName }}</span>
            </div>
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">此前所属售前：</span>
                <span style="width: 80%;">{{ opportunity.preSaleName }}</span>
            </div>
            <div class="modal-item" style="display: flex; align-items: flex-start;">
                <span style="width: 20%;text-align: right;margin-right: 15px;">商机介绍：</span>
                <span style="width: 80%;">{{ opportunity.introduce }}</span>
                <!-- <a-textarea style="width: 80%;" v-model:value="localOpportunity.introduce"
                    :auto-size="{ minRows: 3, maxRows: 5 }" disabled></a-textarea> -->
            </div>
            <!-- <div class="modal-item">
                <span style="width: 20%;">相关附件：</span>
                <span style="width: 80%;" v-if="modalMode === 'reapply'">
                    <a-upload v-model:file-list="fileList" name="file" :multiple="true" :headers="headers"
                        @change="handleChange" :show-upload-list="false" :before-upload="(file) => { return false; }">
                        <a-button>
                            <upload-outlined></upload-outlined>
                            选择文件
                        </a-button>
                    </a-upload>
                    <div class="uploaded-files" v-if="fileList.length > 0">
                        <h4>已选择文件:</h4>
                        <ul>
                            <li v-for="file in fileList" :key="file.uid">
                                {{ file.name }}
                                <a-button type="link" @click="removeFile(file.uid)">删除</a-button>
                            </li>
                        </ul>
                    </div>
                </span>
                <span style="width: 80%;" v-if="modalMode === 'discard'">
                    <a-button>下载</a-button>
                </span>
            </div> -->
            <div class="modal-item">
                <span style="width: 20%;text-align: right;margin-right: 15px;">售前申请：</span>
                <span style="width: 80%;">
                    <!-- <span> -->
                        <a-select ref="select" v-model:value="selectedUserId" style="width: 100%" :options="salesOptions"
                            @focus="focus" mode="multiple"></a-select>
                    <!-- </span> -->
                </span>
            </div>

            <div class="modal-item" v-if="modalMode === 'reapply'">
                <span style="width: 20%;text-align: right;margin-right: 15px;">重新报价说明</span>
                <span style="width: 80%;">
                    <a-textarea v-model:value="quoteDesc" :auto-size="{ minRows: 3, maxRows: 5 }"></a-textarea>
                </span>
            </div>
        </div>
        <template #footer>
            <a-button type="default" @click="handleCancel">取消</a-button>
            <a-button type="primary" @click="handleApplyQoute">更新报价申请</a-button>
        </template>
    </a-modal>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import { message } from 'ant-design-vue';
import { getRoleUserList } from '@/api/user';
import { updateRequired } from '@/api/saleQoute';
// import { uploadQuoteFiles } from '@/api/common';
const props = defineProps({
    opportunity: {
        type: Object,
        required: true
    },
    isUpdateCostModalVisible: {
        type: Boolean,
        required: false
    },
});
// const fileList = ref([]);
// const uploadedFiles = ref([]);
watch(() => props.isUpdateCostModalVisible, (newVal) => {
    if (newVal) {
        fetchRoleUsers();
    }
});
// 暂时注掉----------
// const handleChange = async (info) => {
//     let newFileList = [...info.fileList];

//     newFileList = newFileList.filter(file => !uploadedFiles.value.some(uploadedFile => uploadedFile.name === file.name));

//     fileList.value = newFileList;

//     if (info.file.status !== 'uploading') {
//         console.log(info.file, fileList.value);
//     }

//     if (info.file.status === 'done') {
//         message.success(`${info.file.name} file uploaded successfully`);
//         uploadedFiles.value.push(info.file);
//     } else if (info.file.status === 'error') {
//         message.error(`${info.file.name} file upload failed.`);
//     }
// };

// Upload files function
// const uploadFiles = async () => {
//     try {
//         if (fileList.value.length === 0) {
//             message.warning('Please select files to upload!');
//             return;
//         }
//         console.log('formdata---------------', fileList.value.map(file => file.originFileObj))
//         // const response = await fetchUploads(fileList.value.map(file => file.originFileObj));
//         const response = await uploadQuoteFiles({
//             opportunitiesId: props.opportunity.id,
//             files: fileList.value.map(file => file.originFileObj)
//         });

//         if (response && response.code === 200) {
//             message.success('Files uploaded successfully');
//             uploadedFiles.value = response.data;
//             fileList.value = [];
//         } else {
//             message.error('File upload failed: ' + response.message);
//         }
//     } catch (error) {
//         console.error("File upload request failed:", error);
//         message.error('File upload request failed');
//     }
// };

// const beforeUpload = (file) => {  
//     // 可以在这里进行文件类型和大小的校验  
//     const isValidFileType = ['image/jpeg', 'image/png', 'application/pdf'].includes(file.type);  
//     if (!isValidFileType) {  
//         message.error('只支持 JPG/PNG/PDF 格式文件');  
//     }  
//     return isValidFileType; // 返回true将允许上传  
// };  

// const removeFile = (uid) => {
//     fileList.value = fileList.value.filter(file => file.uid !== uid);
// };

const quoteDesc = ref('')
// 下拉列表
// const value = ref('');
const selectedUserId = ref([]);
const selectedUserLabel = ref('');
const salesOptions = ref([
]);
// 获取角色用户列表  
const fetchRoleUsers = async () => {
    try {
        const roleId = 100;
        const response = await getRoleUserList(roleId);
        console.log(response, '------')
        if (response && response.code) {
            salesOptions.value = response.rows.map(user => ({
                value: user.crmUserId,
                label: user.nickName,
            }));
            setDefaultSelectedUsers();
        }
    } catch (error) {
        console.error("获取角色用户列表失败:", error);
        message.error('获取角色用户列表失败');
    }
};
const setDefaultSelectedUsers = () => {
    if (props.opportunity.preSaleName) {
        const preSaleNames = props.opportunity.preSaleName.split(',').map(name => name.trim()); 
        const selectedIds = salesOptions.value
            .filter(option => preSaleNames.includes(option.label))
            .map(option => option.value);
        selectedUserId.value = selectedIds;
        console.log(preSaleNames,'preSaleNames',selectedIds,salesOptions.value)
    }
};
// 发射事件
const emit = defineEmits(['close']);
// const localOpportunity = ref({ ...props.opportunity });

// 关闭模态框
function handleCancel() {
    emit('close');
}
const handleApplyQoute = async () => {
    const selectedOption = salesOptions.value.find(option => option.value === selectedUserId.value);
    selectedUserLabel.value = selectedOption ? selectedOption.label : '';
    const quotationData = {
        id: props.opportunity.id, // 获取当前机会 id  
        preSaleIdList: selectedUserId.value,
        // preSaleName: selectedUserLabel.value,
    };
    try {
        // if (fileList.value.length > 0) {
        //     uploadFiles()
        // }
        await updateRequired(quotationData);
        emit('close', 'refresh');
        handleCancel();
    } catch (error) {
        console.error('申请报价失败:', error);
    }
}
</script>

<style scoped>
.modal-content {
    padding: 20px;
}

.modal-item {
    margin-bottom: 10px;
    display: flex;
    align-items: flex-start;
}

.modal-item span {
    display: inline-block;
}
</style>
