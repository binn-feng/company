// // src/store/modules/role.js
// import axios from 'axios';

// export default {
//   state: {
//     roles: [],
//   },
//   mutations: {
//     SET_ROLES(state, roles) {
//       state.roles = roles;
//     },
//   },
//   actions: {
//     async getRoleList({ commit }) {
//       try {
//         const response = await axios.get('/api/roles');
//         commit('SET_ROLES', response.data);
//       } catch (error) {
//         console.error('Error fetching role list:', error);
//       }
//     },
//     async addRole({ commit }, data) {
//       try {
//         const response = await axios.post('/api/roles/add', data);
//         return response.data;
//       } catch (error) {
//         console.error('Error adding role:', error);
//       }
//     },
//     async updateRole({ commit }, { id, data }) {
//       try {
//         const response = await axios.put(`/api/roles/update?${id}`, data);
//         return response.data;
//       } catch (error) {
//         console.error('Error updating role:', error);
//       }
//     },
//     async deleteRole({ commit }, id) {
//       try {
//         await axios.delete(`/api/roles/delete?${id}`);
//       } catch (error) {
//         console.error('Error deleting role:', error);
//       }
//     },
//   },
//   getters: {
//     getRoles: (state) => state.roles,
//   },
// };
