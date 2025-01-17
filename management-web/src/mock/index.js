import Mock from 'mockjs';  

// 登录接口模拟  
Mock.mock('/vue-admin-template/user/login', 'post', (options) => {  
  const { username, password } = JSON.parse(options.body);  

  // 随机生成用户数据  
  const validUser = {  
    username: 'admin',  
    password: '123456'  
  };  

  // 登录验证逻辑  
  if (username === validUser.username && password === validUser.password) {  
    return {  
      code: 200,  
      message: '登录成功',  
      data: {  
        token: 'mock_token_123456' // 模拟token  
      }  
    };  
  } else {  
    return {  
      code: 401,  
      message: '用户名或密码错误'  
    };  
  }  
});
// 模拟退出登录接口
Mock.mock('/vue-admin-template/user/logout', 'post', () => {  
  return {  
    code: 200,  
    message: '退出登录成功'  
  };  
});

const Random = Mock.Random;  

// Mock user info data  
const userInfoData = {  
  name: Random.name(),  
  avatar: Random.image('100x100', Random.color(), '#FFF', 'Avatar'), // 随机头像  
  username: Random.word(),  
  areaId: Random.integer(1, 100), // 假设区域 ID  
  id: Random.integer(1, 1000), // 随机用户 ID  
  roleCode: Random.word(), // 随机角色代码  
  readonly: Random.boolean() // 随机只读状态  
};  

Mock.mock('/api/user/info', 'get', () => {
  // 随机生成用户信息数据
  const userInfoData = {
    name: Mock.Random.name(),
    avatar: Mock.Random.image('100x100', Mock.Random.color(), '#FFF', 'Avatar'),
    username: Mock.Random.word(),
    areaId: Mock.Random.integer(1, 100), // 假设区域 ID
    id: Mock.Random.integer(1, 1000), // 随机用户 ID
    roleCode: Mock.Random.word(),
    readonly: Mock.Random.boolean() // 随机只读状态
  };

  return {
    code: 200,
    data: userInfoData
  };
});

// costing
// 模拟保存接口
Mock.mock('/costing/save', 'post', (options) => {
  const { form } = JSON.parse(options.body);
  return {
    code: 200,
    message: '保存成功',
    data: {}
  };
});
