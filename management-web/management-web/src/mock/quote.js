import Mock from 'mockjs';  

const Random = Mock.Random;  

// 定义 Mock 接口  
Mock.mock('/api/saveQuoteSettings', 'post', (options) => {  
    // 解析请求体  
    const body = JSON.parse(options.body);  

    // 这里可以进行一些数据验证或处理  

    // 返回的响应体  
    return {  
        code: 200,  
        message: '保存成功',  
        data: {  
            ...body, // 返回客户端提交的表单数据  
        }  
    };  
});  
