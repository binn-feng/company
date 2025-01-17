// src/mock/mock.js  
import Mock from 'mockjs';  

// Mock 数据  
const roleData = Mock.mock({  
    'data|100': [  // 增加可生成的数据量，便于分页  
        {  
            id: '@increment',  
            code: '@integer(1000, 9999)',  
            name: '@name',  
            updateTime: '@datetime',  
            status: '@boolean',  
            desc: '@ctitle(50)',  
        },  
    ],  
    total: 100, // 总共生成的数据条数  
});  

// 模拟 API 接口  
Mock.mock('/api/roles', 'get', (options) => {  
    const body = options.body;  
    let params = {};  
    if (body) {  
        params = JSON.parse(body);  
    }  

    // 获取分页和搜索参数  
    const { pageNum = 1, pageSize = 10, search = '' } = params;  
    const startIndex = (pageNum - 1) * pageSize;  
    const endIndex = startIndex + pageSize;  

    // 过滤数据，搜索匹配  
    const filteredData = roleData.data.filter(item => {  
        return item.name.includes(search) || item.code.toString().includes(search);  // 根据名称或编码过滤  
    });  

    // 返回切片数据  
    return {  
        status: 200,  
        data: filteredData.slice(startIndex, endIndex),  
        total: filteredData.length,  // 过滤后的总数  
    };  
});
let idCounter = roleData.data.length + 1; // 初始化 ID 计数器  

Mock.mock('/api/roles/add', 'post', (options) => {
    const newRole = JSON.parse(options.body);
    newRole.id = idCounter++; // 使用计数器生成唯一 ID  
    roleData.data.push(newRole);
    roleData.total = roleData.total + 1
    console.log(newRole, roleData.data)
    return {
        status: 200,
        data: newRole,
    };
});

Mock.mock('/api/roles/update', 'put', (options) => {
    const updateRole = JSON.parse(options.body);
    const index = roleData.data.findIndex(r => r.id === updateRole.id);

    if (index !== -1) {
        roleData.data[index] = updateRole;
        return {
            status: 200,
            data: updateRole,
        };
    } else {
        return {
            status: 404,
            message: '角色未找到',
        };
    }
});

Mock.mock('/api/roles/delete', 'delete', (options) => {
    const { id } = JSON.parse(options.body);
    const initialLength = roleData.data.length;
    roleData.data = roleData.data.filter(r => r.id !== id); // 删除角色逻辑  
    return {
        status: 200,
        message: initialLength !== roleData.data.length ? '删除成功' : '未找到该角色',
        data: { id },
    };
});  
