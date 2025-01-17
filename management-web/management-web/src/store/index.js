import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import role from './modules/role'
import system from './modules/system'
// import calendar from './modules/calendar'
import permission from './modules/permission'
Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    role,
    system,
    // project,
    // calendar,
    permission
  },
  getters
})

export default store
