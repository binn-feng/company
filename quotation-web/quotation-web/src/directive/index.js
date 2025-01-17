import hasRole from './permission/hasRole';
import hasPermi from './permission/hasPermi';

// 挂载到 window 对象
if (typeof window !== 'undefined') {
  window.hasRole = hasRole;
  window.hasPermi = hasPermi;
  // window.hasPermiOr = hasPermiOr
}

const install = (app) => {
  // 注册自定义指令
  app.directive('hasRole', hasRole);
  app.directive('hasPermi', hasPermi);
  // app.directive('hasPermiOr', hasPermiOr);

};

export default install;
