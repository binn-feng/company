import request from '@/utils/request'; 
// 销售报价-获取参考成本报价数据--详细
export const fetchSalesDetailInfo = (params) => {  
    return request({  
      url: '/quote/info/getSalesDetailInfo',  
      method: 'post',  
      data: params
    });  
  };
// 销售报价-获取参考成本报价数据--粗略
export const fetchSalesRoughInfo = (params) => {  
    return request({  
      url: '/quote/info/getSalesRoughInfo',  
      method: 'post',  
      data: params
    });  
  };
// 更新销售报价-获取参考成本报价数据--粗略
export const fetchReSalesInfo = (params) => {  
  return request({  
    url: '/quote/info/reSales',  
    method: 'post',  
    data: params
  });  
};
// 销售报价-商机售前报价信息详细信息
export const getQuoteInfo = (Id) => {  
  return request({  
    url: `/quote/info/${Id}`,  // 使用模板字符串插入 roleId  
    method: 'get' 
  });  
};
// 丢单处理
export const fetchOpportunitiesLose = (params) => {  
  return request({  
    url: '/quote/opportunities/lose',  
    method: 'post',  
    data: params
  });  
};
// 确认销售报价-详细
// export const confirmSalesQuote = (params) => {  
//   return request({  
//     url: '/quote/info/confirmSalesQuote',  
//     method: 'post',  
//     data: params
//   });  
// };
// 确认销售报价-详细
export const confirmSalesRoughQuote = (params) => {  
  return request({  
    url: '/quote/info',  
    method: 'post',  
    data: params
  });  
};
// 确认销售报价-无法报价
export const confirmSalesUnableQuote = (params) => {  
  return request({  
    url: '/quote/opportunities',  
    method: 'put',  
    data: params
  });  
};

// 确认销售报价
export const confirmSalesQuote = (params) => {  
  return request({  
    url: '/quote/info/addSalesQuotesVersion',  
    method: 'post',  
    data: params
  });  
};
// 获取销售报价所有版本
export const getSalesVersion = (params) => {  
  return request({  
    url: '/quote/opportunities/salesVersion',   
    method: 'post',  
    data: params
  });  
};
// 获取销售报价详细版本
export const getSalesDetailInfoById = (params) => {  
  return request({  
    url: '/quote/info/getDetailInfoById',   
    method: 'post',  
    data: params
  });  
};
// 报价审批
export const getapprovalInfo = (params) => {  
  return request({  
    url: '/quote/info/approvalInfo',   
    method: 'post',  
    data: params
  });  
};
// 签约汇总表
export const getApprovalAndSignInfo = (params) => {  
  return request({  
    url: '/quote/info/approvalAndSignInfo',   
    method: 'post',  
    data: params
  });  
};
// 重新签约申请
export const getReSignInfo = (params) => {  
  return request({  
    url: '/quote/sign/info/reSignInfo',   
    method: 'post',  
    data: params
  });  
};
// 待报价审批--签约审批
export const getSignApprovalPageInfo = (params) => {  
  return request({  
    url: '/quote/sign/info/signApprovalPage',   
    method: 'post',  
    data: params
  });  
};
// 销售报价审批通过
export const salesApproval = (params) => {  
  return request({  
    url: '/quote/opportunities/salesApproval',   
    method: 'post',  
    data: params
  });  
};
// 审批驳回
export const salesReject = (params) => {  
  return request({  
    url: '/quote/opportunities/salesReject',   
    method: 'post',  
    data: params
  });  
};
// 报价单更新报价
export const updateRequired = (params) => {  
  return request({  
    url: '/quote/opportunities/updateRequired',   
    method: 'put',  
    data: params
  });  
};
// 下载报价单
export const exportQuotation = (params,) => {
  return request({
    url: '/quote/opportunities/quotation/export',
    method: 'post',
    data: params,
    responseType: 'blob',  // 确保响应是 blob
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

// 签约申请时候标识---区分正常详细报价粗略报价和特殊粗略报价无法报价
export const existCostInfo = (params) => {  
  return request({  
    url: '/quote/info/existCostInfo',   
    method: 'post',  
    data: params
  });  
};
