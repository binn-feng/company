
// import { addEmail, getEmail, editEmail, getSystemName,getProjectName,addProjectName,putProjectName,deleteProjectName,getProjectNameList,syncProjectNameList,getAllEmail,DeleteEmail,AddEmails,UpdateEmails,disArea,getAllStage,changeDesc,
//   setEditableDate, getEditableDate } from '@/api/system'
// import { setSystemName , getSystemNames} from '@/utils/auth'
// const state = {
//   systemname:getSystemNames()
// }

// const mutations = {
//   SET_SYSTEMNAME:(state,systemname)=>{
//     state.systemname = systemname
//     setSystemName(systemname)
//   }
// }

// const actions = {
//     addEmail({ commit },data) {
//       return new Promise((resolve, reject) => {
//         addEmail((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getEmail({ commit },data) {
//       return new Promise((resolve, reject) => {
//         getEmail((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     editEmail({commit},data){
//       return new Promise((resolve, reject) => {
//         editEmail((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getSystemName({commit},data){
//       return new Promise((resolve, reject) => {
//         getSystemName((data)).then((res) => {
//           resolve(res.data)
//           commit('SET_SYSTEMNAME',res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getProjectName({commit},data){
//       return new Promise((resolve, reject) => {
//         getProjectName((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     addProjectName({commit},data){
//       return new Promise((resolve, reject) => {
//         addProjectName((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     putProjectName({commit},data){
//       return new Promise((resolve, reject) => {
//         putProjectName((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     deleteProjectName({commit},data){
//       return new Promise((resolve, reject) => {
//         deleteProjectName((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getProjectNameList({commit},data){
//       return new Promise((resolve, reject) => {
//         getProjectNameList((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     syncProjectNameList({commit},data){
//       return new Promise((resolve, reject) => {
//         syncProjectNameList({}).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getAllEmail({commit},data){
//       return new Promise((resolve, reject) => {
//         getAllEmail((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     DeleteEmail({commit},data){
//       return new Promise((resolve, reject) => {
//         DeleteEmail((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     AddEmails({commit},data){
//       return new Promise((resolve, reject) => {
//         AddEmails((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     UpdateEmails({commit},data){
//       return new Promise((resolve, reject) => {
//         UpdateEmails((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     disArea({commit},data){
//       return new Promise((resolve, reject) => {
//         disArea((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     getAllStage({commit},data){
//       return new Promise((resolve, reject) => {
//         getAllStage((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     changeDesc({commit},data){
//       return new Promise((resolve, reject) => {
//         changeDesc((data)).then((res) => {
//           resolve(res.data)
//         }).catch(error => {
//           reject(error)
//         })
//       })
//     },
//     updateSystemName({commit},data){
//       return new Promise((resolve) => {
//         commit('SET_SYSTEMNAME',data)
//         resolve()
//       })
//     },
//   setEditableDate({ commit }, data) {
//     return new Promise((resolve, reject) => {
//       setEditableDate((data)).then((res) => {
//         resolve(res.data)
//       }).catch(error => {
//         reject(error)
//       })
//     })
//   },
//   getEditableDate({ commit }, data) {
//     return new Promise((resolve, reject) => {
//       getEditableDate((data)).then((res) => {
//         resolve(res.data)
//       }).catch(error => {
//         reject(error)
//       })
//     })
//   }
// }

// export default {
//   namespaced: true,
//   state,
//   mutations,
//   actions
// }

