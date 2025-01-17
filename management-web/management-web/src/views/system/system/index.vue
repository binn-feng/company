<template>  
  <div class="container">  
    <header-title>  
      <template slot="title">系统管理</template>  
    </header-title>  

    <div class="userSearch">  
      <div class="userSearchBox">  
        <el-input  
          v-model="searchQuery"  
          placeholder="搜索系统"  
          class="search-input"  
          clearable  
          @keyup.enter="searchSystem"  
        />  
        <el-button type="primary" @click="searchSystem" class="search-button">搜索</el-button>  
      </div>  
      <el-button type="primary" @click="openAddDialog" class="add-system-button">添加系统</el-button>  
    </div>  

    <el-table :data="filteredData" class="el-table" style="width: 100%">  
      <el-table-column label="系统">  
        <template v-slot="scope">  
          <div class="system-item">  
            <img :src="scope.row.coverImage" alt="封面" class="cover-image" />  
            <span>{{ scope.row.system }}</span>  
          </div>  
        </template>  
      </el-table-column>  
      <el-table-column prop="type" label="类型"></el-table-column>  
      <el-table-column prop="clientId" label="Client ID"></el-table-column>  
      <el-table-column prop="redirectUrl" label="Redirect URL"></el-table-column>  
      <el-table-column label="操作" width="180">  
        <template v-slot="scope">  
          <el-button  
            size="mini"  
            title="编辑"  
            type="warning"  
            icon="el-icon-edit-outline"  
            @click="openEditDialog(scope.row)"  
          ></el-button>  
          <el-button  
            size="mini"  
            title="删除"  
            icon="el-icon-delete"  
            type="danger"  
            @click="deleteSystem(scope.row)"  
          ></el-button>  
        </template>  
      </el-table-column>  
    </el-table>  

    <system-dialog  
      :visible.sync="dialogVisible"  
      :is-editing="isEditing"  
      :current-system="currentSystem"  
      @save="saveSystem"  
    />  
  </div>  
</template>  

<script>  
import headerTitle from '@/components/Title/index.vue'
import SystemDialog from './component/SystemDialog.vue';  

export default {  
  components: {  
    headerTitle,
    SystemDialog,  
  },  
  data() {  
    return {  
      searchQuery: '',  
      dialogVisible: false,  
      isEditing: false,  
      currentSystem: {},  
      systems: [  
        {  
          system: 'CRM',  
          type: '管理',  
          clientId: '2',  
          redirectUrl: 'http://crm.example.com',  
          coverImage: 'https://via.placeholder.com/50',  
        },  
        {  
          system: '项目管理',  
          type: '工作管理',  
          clientId: '99',  
          redirectUrl: 'http://project.example.com',  
          coverImage: 'https://via.placeholder.com/50',  
        },  
        {  
          system: '报表管理',  
          type: '管理',  
          clientId: '104',  
          redirectUrl: 'http://report.example.com',  
          coverImage: 'https://via.placeholder.com/50',  
        },  
      ],  
    };  
  },  
  computed: {  
    filteredData() {  
      return this.systems.filter(item =>  
        item.system.toLowerCase().includes(this.searchQuery.toLowerCase())  
      );  
    },  
  },  
  methods: {  
    openAddDialog() {  
      this.isEditing = false;  
      this.currentSystem = {  
        system: '',  
        type: '',  
        clientId: '',  
        redirectUrl: '',  
        coverImage: '',  
      };  
      this.dialogVisible = true;  
    },  
    openEditDialog(row) {  
      this.isEditing = true;  
      this.currentSystem = { ...row };  
      this.dialogVisible = true;  
    },  
    deleteSystem(row) {  
      this.$confirm(`确定删除系统: ${row.system} 吗?`, '警告', {  
        confirmButtonText: '确定',  
        cancelButtonText: '取消',  
        type: 'warning',  
      })  
      .then(() => {  
        this.systems = this.systems.filter(item => item !== row);  
        this.$message.success('删除成功');  
      })  
      .catch(() => {});  
    },  
    saveSystem(system) {  
      if (this.isEditing) {  
        const index = this.systems.findIndex(item => item.clientId === system.clientId);  
        if (index !== -1) {  
          this.systems.splice(index, 1, system);  
        }  
        this.$message.success('系统信息已更新');  
      } else {  
        this.systems.push({ ...system, clientId: String(this.systems.length + 1) });  
        this.$message.success('系统已添加');  
      }  
      this.dialogVisible = false;  
    },  
    searchSystem() {  
      // 这里可以添加搜索逻辑  
    },  
  },  
};  
</script>  

<style scoped>  
.container {  
  padding: 20px;  
  /* background-color: #f5f7fa;   */
}  
.userSearch {  
  width: 100%;  
  height: 50px;  
  display: flex;  
  justify-content: space-between;  
  align-items: center;  
  margin-top: 10px;  
}  
.userSearchBox {  
  display: flex;  
  align-items: center;  
}  
.search-input {  
  flex: 1;  
  min-width: 200px;  
  margin-right: 10px;  
}  
.add-system-button {  
  margin-left: auto;  
}  
.system-item {  
  display: flex;  
  align-items: center;  
}  
.cover-image {  
  width: 50px;  
  height: 50px;  
  margin-right: 10px;  
  border-radius: 4px;  
  border: 1px solid #ccc;  
}  
.el-table {  
  margin-top: 20px;  
  width: 100%;  
  overflow-x: auto;  
}  
@media (max-width: 768px) {  
  .userSearch {  
    flex-direction: column;  
  }  
}  
</style>