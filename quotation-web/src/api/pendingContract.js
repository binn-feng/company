import request from '@/utils/request';
// 签约申请
export const signApplication = (params) => {
  return request({
    // url: '/quote/opportunities/signApplication',
    url: '/quote/sign/info/signApplication',
    method: 'post',
    data: params
  });
};
// 获取签约申请所有版本
export const getSignVersion = (params) => {
  return request({
    url: '/quote/opportunities/signVersion',
    method: 'post',
    data: params
  });
};
// 获取签约申请版本详情
export const getSignDetailInfo = (params) => {
  return request({
    url: '/quote/sign/info/signDetailInfo',
    method: 'post',
    data: params
  });
};
// 签约审批通过
export const signApproval = (params) => {
  return request({
    url: '/quote/opportunities/signApproval',
    method: 'post',
    data: params
  });
};
// 签约审批驳回
export const signReject = (params) => {
  return request({
    url: '/quote/opportunities/signReject',
    method: 'post',
    data: params
  });
};
// 签约状态更新页
export const statusUpdatePage = (params) => {
  return request({
    url: '/quote/sign/info/statusUpdatePage',
    method: 'post',
    data: params
  });
};
// 签约状态更新
export const updateStatus = (params) => {
  return request({
    url: '/quote/sign/info/updateStatus',
    method: 'post',
    data: params
  });
};
