import auth from './auth'

export default {
  install(Vue) {
    Vue.prototype.$auth = auth
  }
}
