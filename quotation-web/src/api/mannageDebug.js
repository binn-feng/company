import request from '@/utils/request';   
// 预警设置
export const getProfit = () => {  
  return request({  
    url: `/manager/warning/getProfit`,
    method: 'get' 
  });  
};
// 成本设置
export const getCostMap = () => {  
  return request({  
    url: `/manager/cost/getCostMap`,
    method: 'post' 
  });  
};
// 产品下拉列表
export const getProductChildrenPage = (params) => {  
  return request({  
    url: `/quote/opportunities/detail/selectProduct`,
    method: 'post' ,
    data: params
  });  
};
// 硬件-自研下拉列表
export const selectHardwareSelf = (params) => {  
  return request({  
    url: `/quote/opportunities/detail/selectHardwareSelf`,
    method: 'post' ,
    data: params
  });  
};
// 硬件-外采下拉列表
export const selectHardwareExt = (params) => {  
  return request({  
    url: `/quote/opportunities/detail/selectHardwareExt`,
    method: 'post' ,
    data: params
  });  
};
// 硬件-外采分页列表
export const selectHardwareExtPage = (params) => {  
  return request({  
    url: `/quote/opportunities/detail/hardware/ext/page`,
    method: 'post' ,
    data: params
  });  
};
// 报价设置
export const getMap = () => {  
  return request({  
    url: `/manager/quote/getMap`,
    method: 'get' 
  });  
};
