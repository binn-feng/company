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
