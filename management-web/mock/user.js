
// const tokens = {
//   admin: {
//     token: 'admin-token'
//   },
//   editor: {
//     token: 'editor-token'
//   }
// }

// const users = {
//   'admin-token': {
//     roles: ['admin'],
//     introduction: 'I am a super administrator',
//     avatar: require('@/assets/images_icon.png'),
//     name: 'Super Admin'
//   },
//   'editor-token': {
//     roles: ['editor'],
//     introduction: 'I am an editor',
//     avatar: require('@/assets/images_icon.png'),
//     name: 'Normal Editor'
//   }
// }

// module.exports = [
//   // user login
//   {
//     url: '/vue-admin-template/user/login',
//     type: 'post',
//     response: config => {
//       const { username } = config.body
//       const token = tokens[username]

//       // mock error
//       if (!token) {
//         return {
//           code: 60204,
//           message: 'Account and password are incorrect.'
//         }
//       }

//       return {
//         code: 20000,
//         data: token
//       }
//     }
//   },

//   // get user info
//   {
//     url: '/vue-admin-template/user/info\.*',
//     type: 'get',
//     response: config => {
//       const { token } = config.query
//       const info = users[token]

//       // mock error
//       if (!info) {
//         return {
//           code: 50008,
//           message: 'Login failed, unable to get user details.'
//         }
//       }

//       return {
//         code: 20000,
//         data: info
//       }
//     }
//   },

//   // user logout
//   {
//     url: '/vue-admin-template/user/logout',
//     type: 'post',
//     response: _ => {
//       return {
//         code: 20000,
//         data: 'success'
//       }
//     }
//   }
// ]


const tokens = {
  admin: 'admin-token'
};

const validCredentials = {  
  admin: '123456' // 假设密码为 123456  
};

const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    // avatar: require('@/assets/images_icon.png'),
    name: 'Super Admin'
  }
};

module.exports = [
  // user login
  {
    url: '/vue-admin-template/user/login',
    type: 'post',
    response: config => {
      const { username, password } = JSON.parse(config.body);  
      const token = tokens[username];

      // 模拟登录成功，返回统一的令牌
      if (token && validCredentials[username] === password) {
        return {
          code: 20000,
          data: token
        };
      } else {
        // 模拟登录失败
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        };
      }
    }
  },

  // get user info
  {
    url: '/vue-admin-template/user/info',
    type: 'get',
    response: _ => {
      return {
        code: 20000,
        data: users['admin-token']
      };
    }
  },

  // user logout
  {
    url: '/vue-admin-template/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      };
    }
  }
];
