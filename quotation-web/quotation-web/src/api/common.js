import request from '@/utils/request';

// 上传多文件  
export const fetchUploads = async (fileList) => {
    console.log('fileList', fileList)
    const formData = new FormData();
    fileList.forEach(file => {
        formData.append('files', file);
    });
    try {
        const response = await request({
            url: '/common/uploads',
            method: 'post',
            data: formData,
            // headers: {  
            //     'Content-Type': 'multipart/form-data', // 确保内容类型为多部分数据  
            // },  
        });
        return response;
    } catch (error) {
        console.error("上传文件失败:", error);
        throw error;
    }
};
// 上传多文件（新）
export const uploadQuoteFiles = async (data) => {
    console.log('data', data)
    const formData = new FormData();
    formData.append("opportunitiesId", data.opportunitiesId);
    data.files.forEach(file => {
        formData.append('fileList', file);
    });
    try {
        const response = await request({
            url: '/quote/file/upLoadFiles',
            method: 'post',
            data: formData,
        });
        return response;
    } catch (error) {
        console.error("上传文件失败:", error);
        throw error;
    }
};
// 下载文件
export const getLoadFiles = () => {
    return request({
        url: `/quote/file/downLoadFiles?opportunitiesId=1858431269639020546`,
        method: 'get',
    });
};
// 获取用户信息
export const getInfo = () => {
    return request({
        url: `/getInfo`,
        method: 'get',
    });
};
// 上传功能清单
export const uploadCustomizable = async (data) => {
    const formData = new FormData();
    data.files.forEach(file => {
        formData.append('file', file);
    });
    try {
        const response = await request({
            url: '/quote/file/uploadCustomizable',
            method: 'post',
            data: formData,
        });
        return response;
    } catch (error) {
        console.error("上传文件失败:", error);
        throw error;
    }
};