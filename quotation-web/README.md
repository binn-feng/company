# nl-quotation-system

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run dev
```

### Compiles and minifies for production
```
npm run build:prod
```

## 目录结构

src/
  ├── assets/         # 静态资源（图片、样式等）
  ├── components/     # 公共组件
  ├── views/          # 页面视图
  │   ├── businessTrip/       # 报销申请、出差申请
  │   ├── collect/            # 汇总
  │   ├── components/         # 公共组件
  │   ├── contract/           # 待签合同
  │   ├── cost/               # 成本报价
  │   ├── dashboard/          # 首页
  │   ├── login/              # 登录
  │   ├── profile/            # 个人中心
  │   ├── purchasing/         # 采购申请
  │   ├── sales/              # 销售报价
  │   └── salesForecast/      # 销售预测
  ├── router/         # 路由配置
  ├── store/          # Vuex 状态管理
  ├── utils/          # 工具函数
  ├── api/            # 接口请求封装
  ├── styles/         # 全局样式文件
  └── main.js         # 项目入口
