const Mock = require('mockjs');

// 模拟登录接口
Mock.mock('/api/login', 'post', (options) => {
  const { username, password } = JSON.parse(options.body);
  if (username === 'admin' && password === '123456') {
    return {
      code: 200,
      message: '登录成功',
      token: Mock.Random.guid(), // 生成一个随机GUID作为Token
      cookie: 'token=' + Mock.Random.guid() // 设置一个Cookie
    };
  } else {
    return {
      code: 401,
      message: '用户名或密码错误'
    };
  }
});

// 模拟登出接口
Mock.mock('/api/logout', 'post', () => {
  return {
    code: 200,
    message: '登出成功'
  };
});
