import Vue from 'vue'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import '@/styles/index.scss' // global css
import './styles/media.scss'
import App from './App'
import store from './store'
import router from './router'
import directive from './directive'
import plugins from './plugins'
import { parseTime, resetForm, handleTree, download, formatDate, formatYMDDate } from '@/utils/public'
import PATCH_fixMouseOutsideAutoClose from '@/utils/fixMouseOutsideAutoClose.js'

import '@/icons'
import '@/permission'
import * as filters from './filters'
import { registerDirective } from '@/directive/index'

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.prototype.formatDate = formatDate
Vue.prototype.formatYMDDate = formatYMDDate

Vue.use(Antd)
Vue.use(directive)
Vue.use(plugins)
registerDirective(Vue)
Vue.use(ElementUI)
Vue.use(PATCH_fixMouseOutsideAutoClose)
Vue.prototype.$bus = new Vue()
Vue.config.productionTip = false

// 注册全局过滤器
Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key])
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
