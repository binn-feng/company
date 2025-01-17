// store/module/roughquotation.js  粗略报价 
const state = {  
    quotations: [],  
    versionCounter: 0, // 添加一个计数器来生成递增的版本号  
  };  
  
  const mutations = {  
    addQuotation(state, quotation) {  
      state.quotations.push(quotation);  
    },  
    incrementVersionCounter(state) {  
      state.versionCounter += 1; // 每添加一个报价，版本计数器加1  
    }  
  };  
  
  const actions = {  
    saveQuotation({ commit, state }, quotation) {  
      const version = `v1.1.${state.versionCounter}`; // 根据计数器生成版本号  
      const quotationWithVersion = { ...quotation, version }; // 将版本号合并到报价数据中  
      console.log(quotationWithVersion,'quotationWithVersion')
      commit('addQuotation', quotationWithVersion);  
      commit('incrementVersionCounter'); // 增加版本计数器  
    }  
  };  
  
  const getters = {  
    allQuotations: (state) => state.quotations  
  };  
  
  export default {  
    namespaced: true,  
    state,  
    mutations,  
    actions,  
    getters  
  };