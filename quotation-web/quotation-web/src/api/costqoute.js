import request from '@/utils/request'; 

// 待成本报价一键同步接口
export const syncOpportunities = () => {  
  return request({  
    url: `/quote/opportunities/syncOpportunities`,  // 使用模板字符串插入 roleId  
    method: 'get' 
  });  
};
// 待成本报价同步状态接口
export const syncOpportunitiesStatus = () => {  
  return request({  
    url: `/quote/opportunities/syncOpportunitiesStatus`,  // 使用模板字符串插入 roleId  
    method: 'get' 
  });  
};
// page接口  
export const fetchOpportunities = (path,params) => {  
  return request({  
    url: `/quote/opportunities/${path}`,  
    method: 'post',  
    data: params
  });  
};
// 多售前---由自己报价
export const quoteBySelf = (params) => {  
  return request({  
    url: `/quote/opportunities/quoteBySelf`,  
    method: 'post',  
    data: params
  });  
};
// 多售前---由他人报价
export const quoteByOthers = (params) => {  
  return request({  
    url: `/quote/opportunities/quoteByOthers`,  
    method: 'post',  
    data: params
  });  
};
// 粗略报价和无法报价
export const fetchRoughQuote = (params) => {  
  return request({  
    url: '/quote/rough',   
    method: 'post',  
    data: params
  });  
};
export const fetchUnableQuote = (params) => {  
  return request({  
    url: '/quote/opportunities/incapable',   
    method: 'put',  
    data: params
  });  
};
// 成本详细报价新增保存接口
export const fetchCostQuote = (params) => {  
  return request({  
    url: '/quote/opportunities/detail',  
    method: 'post',  
    data: params
  });  
};
// 成本报价-详细  暂存-保存
export const fetchCostQuoteDetail = (params) => {  
  return request({  
    url: '/quote/opportunities/detail/getDetailInfo',  
    method: 'post',  
    data: params
  });  
};
// 成本报价-粗略  保存
export const getRoughInfo = (params) => {  
  return request({  
    url: '/quote/rough/getRoughInfo',  
    method: 'post',  
    data: params
  });  
};
// 粗略版本  --- 暂时废弃
export const getRoughVersion = (params) => {  
  return request({  
    url: '/quote/rough/version',   
    method: 'post',  
    data: params
  });  
};
// 获取商机所有版本
export const getVersion = (params) => {  
  return request({  
    url: '/quote/opportunities/version',   
    method: 'post',  
    data: params
  });  
};

// 粗略报价列表
export const getRoughPage = (params) => {  
  return request({  
    url: '/quote/rough/getRoughInfo',   
    method: 'post',  
    data: params
  });  
};

// 报价申请
export const fetchApplyQuote = (params) => {  
  return request({  
    url: '/quote/opportunities/apply',   
    method: 'put',  
    data: params
  });  
}
// 无法报价详情
export const getUnableInfo = (Id) => {  
  return request({  
    url: `/quote/unable/${Id}`,  // 使用模板字符串插入 roleId  
    method: 'get' 
  });  
};

// 查询报价部门---占比相关
export const getDept = (params) => {  
  return request({  
    url: '/quote/radio/dept',   
    method: 'post',  
    data: params
  });  
};
