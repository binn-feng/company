export default {
  data() {
    return {
      pickerOptions: {
        disabledDate(date) {
          if (date.getTime() < 1630425600000) {
            return true
          }
          return false
        }
      }
    }
  }
}

export function formatDate(date) {
  // 格式化日期为 yyyy-MM-dd HH:mm:ss
  const padTo2Digits = (num) => String(num).padStart(2, '0')
  return (
    date.getFullYear() +
    '-' +
    padTo2Digits(date.getMonth() + 1) +
    '-' +
    padTo2Digits(date.getDate()) +
    ' ' +
    padTo2Digits(date.getHours()) +
    ':' +
    padTo2Digits(date.getMinutes()) +
    ':' +
    padTo2Digits(date.getSeconds())
  )
}
