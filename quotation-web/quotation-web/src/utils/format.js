export function formatNumber(num) {
    // 使用Intl.NumberFormat配置千位分隔符和两位小数
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(num);
  }

  export function formatIntNumber(num) {
    return Math.ceil(num)
  }