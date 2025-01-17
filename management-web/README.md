# vue-admin-template

# install dependency
npm install

# develop
npm run dev
```

This will automatically open http://localhost:9528

## Build

```bash
# build for test environment
npm run build:stage

# build for production environment
npm run build:prod
```

## Advanced

```bash
# preview the release environment effect
npm run preview

# preview the release environment effect + static resource analysis
npm run preview -- --report

# code format check
npm run lint

# code format check and auto fix
npm run lint -- --fix
```

## 目录结构

src/
  ├── assets/         # 静态资源（图片、样式等）
  ├── components/     # 公共组件
  ├── views/          # 页面视图
  │   ├── basicData/          # 管理中心
  │   ├── dashboard/          # 首页
  │   ├── login/              # 登录
  │   ├── profile/            # 个人中心
  │   ├── statement/          # 报表管理
  │   └── system/             # 系统管理
  ├── router/         # 路由配置
  ├── store/          # Vuex 状态管理
  ├── utils/          # 工具函数
  ├── api/            # 接口请求封装
  ├── styles/         # 全局样式文件
  └── main.js         # 项目入口
