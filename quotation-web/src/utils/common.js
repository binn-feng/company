
import { saveAs } from 'file-saver';
// 日期格式化
export function parseTime(time, pattern) {
    if (arguments.length === 0 || !time) {
        return null
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
        date = time
    } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
            time = parseInt(time)
        } else if (typeof time === 'string') {
            time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '')
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
            time = time * 1000
        }
        date = new Date(time)
    }
    const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    }
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key]
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        if (result.length > 0 && value < 10) {
            value = '0' + value
        }
        return value || 0
    })
    return time_str
}

// 表单重置
export function resetForm(refName) {
    if (this.$refs[refName]) {
        this.$refs[refName].resetFields()
    }
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str) {
    if (!str || str == 'undefined' || str == 'null') {
        return ''
    }
    return str
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
    const config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
    }

    var childrenListMap = {}
    var nodeIds = {}
    var tree = []

    for (let d of data) {
        const parentId = d[config.parentId]
        if (childrenListMap[parentId] == null) {
            childrenListMap[parentId] = []
        }
        nodeIds[d[config.id]] = d
        childrenListMap[parentId].push(d)
    }

    for (let d of data) {
        const parentId = d[config.parentId]
        if (nodeIds[parentId] == null) {
            tree.push(d)
        }
    }

    for (let t of tree) {
        adaptToChildrenList(t)
    }

    function adaptToChildrenList(o) {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]]
        }
        if (o[config.childrenList]) {
            for (let c of o[config.childrenList]) {
                adaptToChildrenList(c)
            }
        }
    }
    return tree
}

// 验证是否为blob格式  
export function blobValidate(data) {
    return data.type !== 'application/json'
    // return data instanceof Blob;
}

// 通用下载方法  
// export function download(url, params, filename, config) {  
//     return request.post(url, params, {  
//         transformRequest: [(params) => {  
//             return JSON.stringify(params); // 将参数序列化为 JSON 字符串  
//         }],  
//         headers: { 'Content-Type': 'application/json' },  
//         responseType: 'blob', // 设置响应类型为 Blob  
//         ...config  
//     }).then((response) => {  
//         // 检查响应内容是否为 Blob  
//         const isBlob = blobValidate(response);  
//         console.log("Response Type: ",response.type,response.size);
//         if (isBlob) {   
//             const blob = new Blob([response],{ type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });  
//             saveAs(blob, filename);  
//         } else {  
//             // 处理非 Blob 响应  
//             console.error('非 Blob 响应:', response);  
//             // 您可以根据需要进行进一步处理或提示用户  
//         }  
//     }).catch((error) => {  
//         console.error('下载请求失败:', error);  
//     });  
// }
// 通用下载方法
const baseURL= process.env.VUE_APP_BASE_API
export function download(url, params, filename) {
    return fetch(baseURL + url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + localStorage.getItem('token') },
        body: JSON.stringify(params)
    }).then(async (data) => {
        const blob = await data.blob()
        saveAs(blob, filename)
    }).catch((r) => {
        console.error(r)
    })
}

/**
* 参数处理
* @param {*} params  参数
*/
export function tansParams(params) {
    let result = ''
    for (const propName of Object.keys(params)) {
        const value = params[propName];
        var part = encodeURIComponent(propName) + "=";
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        let params = propName + '[' + key + ']';
                        var subPart = encodeURIComponent(params) + "=";
                        result += subPart + encodeURIComponent(value[key]) + "&";
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result
}

// status显示
export const statusMap = {
    0: '报价申请',
    1: '未更新',
    2: '待销售报价',
    3: '待报价审批',
    4: '销售已报价',
    5: '待签约',
    6: '已签约',
    7: '丢单',
    8: '报价审批未通过',
    9: '签约审核未通过',
    10: '待签约审批',
    11: '废弃',
    12: '关闭',
    13: '待更新成本报价',
};