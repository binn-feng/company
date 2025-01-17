import auth from './auth'

export default {
  install(app) {
    app.config.globalProperties.$auth = auth;
  }
}
