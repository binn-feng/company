
import { createStore } from 'vuex';  
import sale from './module/sale'
import role from './module/role'
import permission from './module/permission'
import loginModule from './module/login';
import roughquotation from './module/roughquotation'
export default createStore({  
    state: {  
        evaluationVersions: [],  
        currentEvaluation: null,  
        versionDisplay: '',  
        tableData: [],  
        // costData:[]
    },  
    mutations: {  
        ADD_EVALUATION(state, evaluation) {  
            console.log(' ADD_EVALUATION',evaluation)
            state.evaluationVersions.push(evaluation);  
            state.currentEvaluation = evaluation;  
            state.versionDisplay = evaluation.version;  
            state.tableData = evaluation.data
            
        },  
        SET_CURRENT_EVALUATION(state, evaluation) {  
            state.currentEvaluation = evaluation;  
            console.log(evaluation,'store')
            state.tableData = evaluation.data || []; // 确保数据更新时确保正确   
            console.log(state.tableData,'store-----')
            // state.costData = evaluation.data;

        },  
        RESET_VERSION_DISPLAY(state) {  
            state.versionDisplay = '';  
        },  
        SET_TABLE_DATA(state, data) {  
            state.tableData = data; // 确保 tableData 正确更新  
            // state.costData = data.data;

        },  
    },  
    actions: {  
        saveEvaluation({ commit, state }, evaluationData) {  
            console.log(evaluationData,'11111')
            const versionNumber = state.evaluationVersions.length + 1;  
            const newEvaluation = {  
                version: `v${versionNumber}.0.0`,  
                time: evaluationData.time,  
                person: evaluationData.person,  
                description: evaluationData.description,  
                data: evaluationData.data, // 保存当前表格数据  
            };  
            commit('ADD_EVALUATION', newEvaluation,);  
            commit('RESET_VERSION_DISPLAY');  
            // 数据已经在 ADD_EVALUATION 中更新，所以不需要再次调用 SET_TABLE_DATA  
        },  
        loadEvaluation({ commit }, version) {  
            const versionData = this.state.evaluationVersions.find(e => e.version === version);  
            if (versionData) {  
                console.log(versionData,'store---')
                commit('SET_CURRENT_EVALUATION', versionData);  
                // 无需显式设置 tableData，因为在 SET_CURRENT_EVALUATION 中已经处理  
            }  
        },  
    },  
    modules: {
       sale,
       role,
       permission,
       roughquotation,
       login: loginModule,
      }
});