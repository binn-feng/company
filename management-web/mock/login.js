// src/mock/login.js  
import Mock from 'mockjs';  

Mock.mock('/api/login', 'post', (req) => {  
  const { username, password } = JSON.parse(req.body);  
  // 你可以在这里做简单的验证  
  if (username === 'admin' && password === '123456') {  
    return {  
      code: 200,  
      message: '登录成功',  
      data: { token: 'abcdef123456' } // 模拟返回的token  
    };  
  }  
  return {  
    code: 401,  
    message: '用户名或密码错误'  
  };  
});