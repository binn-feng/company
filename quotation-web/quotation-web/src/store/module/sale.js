// 销售报价
const state = {
    versions: [], // Store versions of the quote
};

const mutations = {
    ADD_QUOTE(state, quote) {
        const lastVersion = state.versions[state.versions.length - 1];
        const newVersion = lastVersion ? incrementVersion(lastVersion.version) : 'v1.0.0';
        
        // 深拷贝 quote 数据，确保历史版本不会被修改
        const newQuote = JSON.parse(JSON.stringify(quote));

        state.versions.push({ version: newVersion, data: newQuote });
    },
};

const actions = {
    saveQuote({ commit }, quote) {
        commit('ADD_QUOTE', quote);
    },
};

const getters = {
    getVersions: (state) => state.versions,
};

const incrementVersion = (version) => {
    const parts = version.split('.').map(Number);
    parts[2] += 1; // Increment the patch version
    return `v${parts[0]}.${parts[1]}.${parts[2]}`;
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters,
};
