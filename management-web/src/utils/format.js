export function formatProfitRate(rate) {  
    if (rate === null || rate === undefined) {  
        return 'N/A'; // 处理空值  
    }  
    return `${(rate * 100).toFixed(2)}%`; // 将利润率转换为百分比格式，保留两位小数  
}  