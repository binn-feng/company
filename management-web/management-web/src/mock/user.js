import Mock from 'mockjs';  

// 模拟用户数据  
const userData = Mock.mock({  
  'data|100': [  
    {  
      id: '@increment',  
      username: '@name',  
      roleName: '@word',  
      email: '@EMAIL',  
      areaName: '@city',  
      areaId: '@increment',  
      emailNotice: '@boolean',  
    },  
  ],  
  total: 100,
});  

Mock.mock('/api/users', 'get', (options) => {
    const body = options.body;
    // 确保 body 是一个有效的 JSON 字符串
    let params = {};
    if (body) {
        params = JSON.parse(body);
    }

    // 获取分页参数
    const { pageNum = 1, pageSize = 10, search = '' } = params;
    const startIndex = (pageNum - 1) * pageSize;
    const endIndex = startIndex + pageSize;

    // 过滤数据，搜索匹配
    const filteredData = userData.data.filter(item => {
        return item.username.includes(search) || item.roleName.includes(search); // 根据用户名或角色名称过滤
    });

    // 返回切片数据
    return {
        status: 200,
        data: filteredData.slice(startIndex, endIndex),
        total: filteredData.length,
    };
});

// 添加用户  
Mock.mock('/api/users/add', 'post', (options) => {  
  const newUser = JSON.parse(options.body);  
  newUser.id = userData.data.length + 1; // 给新用户生成一个 ID  
  userData.data.push(newUser);  
  
  return {  
    status: 200,  
    data: newUser,  
  };  
});  

// 更新用户  
Mock.mock('/api/users/update', 'put', (options) => {  
  const updatedUser = JSON.parse(options.body);  
  const index = userData.data.findIndex(user => user.id === updatedUser.id);  

  if (index !== -1) {  
    userData.data[index] = updatedUser;  
    return {  
      status: 200,  
      data: updatedUser,  
    };  
  } else {  
    return {  
      status: 404,  
      message: '用户未找到',  
    };  
  }  
});  

// 删除用户  
Mock.mock(/\/api\/users\/delete/, 'delete', (options) => {  
  const { id } = JSON.parse(options.body);  
  const initialLength = userData.data.length;  
  userData.data = userData.data.filter(user => user.id !== id);  
  
  return {  
    status: 200,  
    message: initialLength !== userData.data.length ? '删除成功' : '未找到该用户',  
    data: { id },  
  };  
});  


// 初始化密码  
Mock.mock(/\/api\/users\/(.*?)\/password/, 'post', (options) => {  
    // 从 URL 动态提取 userId  
    const userId = options.url.match(/\/api\/users\/(.*?)\/password/)[1];  
  
    // 从请求体中解析 id（如果需要）  
    const { id } = JSON.parse(options.body) || {};  
  
    // 验证用户 ID 是否存在  
    if (!userId || !id) {  
      return {  
        status: 400,  
        message: '无效的用户ID',  
      };  
    }  
  
    return {  
      status: 200,  
      message: '密码初始化成功',  
      userId, // 可以选择返回 userId 以验证  
    };  
  });