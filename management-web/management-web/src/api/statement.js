import request from '@/utils/request'

// page接口  
export function fetchOpportunities(path, params) {
    return request({
        url: `/manager/report/${path}`,
        method: 'post',
        data: params
    });
}