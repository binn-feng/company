import request from '@/utils/request';  
export const getIndexPageInfo = (data) => {  
    return request({  
      url: '/quote/opportunities/indexPageInfo', // 登录的API接口  
      method: 'post',  
      data,  
    });  
  };