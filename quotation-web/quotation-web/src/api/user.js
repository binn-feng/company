import request from '@/utils/request';  
// 获取用户角色列表  
export const getRoleUserList = (roleId, pageNum = 1, pageSize = 99999999) => {  
  return request({  
    url: `/system/user/roleUserList/${roleId}?pageNum=${pageNum}&pageSize=${pageSize}`,
    method: 'get' 
  });  
};