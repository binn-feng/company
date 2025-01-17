// import defaultSettings from '@/settings'
// import store from '@/store'

// const title = store.state.system.systemname|| '北光项目工时管理系统'
const title = '北光管理系统'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
