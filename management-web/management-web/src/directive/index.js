import cutName from './cutName'
import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'

export function registerDirective(Vue){
  cutName(Vue)
}

const install = function(Vue) {
  Vue.directive('hasRole', hasRole)
  Vue.directive('hasPermi', hasPermi)
}

if (window.Vue) {
  window['hasRole'] = hasRole
  window['hasPermi'] = hasPermi
  Vue.use(install); // eslint-disable-line
}

export default install
