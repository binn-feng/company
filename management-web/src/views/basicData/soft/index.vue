<template>
  <div class="container">
    <header-title>
      <template slot="title">软著管理</template>
    </header-title>
    <div class="userSearch">
      <div class="userSearchBox">
        <el-input
          v-model.trim="queryMainName"
          v-hasPermi="['manager:soft:query']"
          placeholder="请输入软著名称"
          class="search-input"
          clearable
          size="small"
          @keyup.enter.native="searchSoft"
        />
        <el-button
          v-hasPermi="['manager:soft:query']"
          icon="el-icon-search"
          type="primary"
          class="search-button"
          size="small"
          @click="searchSoft"
        >搜索</el-button>
        <el-button
          v-hasPermi="['manager:soft:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="small"
          @click="handleExport"
        >导出</el-button>
      </div>
      <el-button
        v-hasPermi="['manager:soft:add']"
        icon="el-icon-plus"
        type="primary"
        class="add-soft-button"
        size="small"
        @click="openAddDialog"
      >新建</el-button>
    </div>
    <div class="content">
      <el-tabs v-model="activeTab" @tab-click="handleCheckTab">
        <el-tab-pane v-for="(tab, index) in softTabs" :key="tab.type" :label="tab.label" :name="String(index)">
          <!-- 北光 -->
          <el-table v-if="index === 0" v-loading="loading" :data="softs" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="name" sortable label="北光软著名称" min-width="110" />
            <el-table-column prop="shortName" label="软著简称" :formatter="formatPlaceholder" />
            <el-table-column prop="registrationMark" label="登记号" :formatter="formatPlaceholder" />
            <el-table-column prop="versionNumber" label="版本号" sortable />
            <el-table-column prop="regTime" label="登记批准日期" min-width="110" sortable />
            <el-table-column prop="pushTime" label="首次发表日期" :formatter="formatPlaceholder" />
            <el-table-column prop="productName" label="关联主产品" min-width="110" sortable />
            <el-table-column prop="linkName" label="对应欣象软著名称" :formatter="formatPlaceholder" min-width="120" />
            <el-table-column label="操作" width="130">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['manager:soft:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 欣象 -->
          <el-table v-if="index === 1" v-loading="loading" :data="softs" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="name" label="欣象软著名称" sortable min-width="110" />
            <el-table-column prop="shortName" label="软著简称" />
            <el-table-column prop="registrationMark" label="登记号" sortable />
            <el-table-column prop="versionNumber" label="版本号" sortable />
            <el-table-column prop="regTime" label="登记批准日期" min-width="110" />
            <el-table-column prop="pushTime" label="首次发表日期" :formatter="formatPlaceholder" width="160" />
            <el-table-column prop="productName" label="关联主产品" min-width="110" sortable />
            <el-table-column prop="linkName" label="对应北光软著名称" :formatter="formatPlaceholder" width="160" />
            <el-table-column label="操作" width="130">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['manager:soft:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="footer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="[10, 20, 30]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <soft-dialog
      :visible="dialogVisible"
      :is-editing="isEditing"
      :read-only="readOnly"
      :active-tab="activeTab"
      :current-soft="currentSoft"
      @save="saveSystem"
      @visibleChange="visibleChange"
    />
    <!-- <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div> -->
  </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import SoftDialog from './component/SoftDialog.vue'
import { getMainSoft, createSoft, updateSoft } from '@/api/basicData/soft.js'

export default {
  components: {
    headerTitle,
    SoftDialog
  },
  data() {
    return {
      queryMainName: '',
      queryParentName: '',
      queryChildName: '',
      dialogVisible: false,
      isEditing: false,
      readOnly: false,
      currentSoft: {},
      softs: [],
      currentPage: 1,
      pageSize: 10,
      activeTab: '0',
      softTabs: [
        { label: '北光软著', type: 'parent' },
        { label: '欣象软著', type: 'child' }
      ],
      loading: false,
      total: 0
    }
  },
  created() {
    this.fetchSofts()
  },
  methods: {
    handleCheckTab(tab) {
      this.activeTab = tab.index
      this.fetchSofts()
    },

    formatCurrency(row, column, value) {
      if (value == null) return '-'
      return parseFloat(value).toFixed(2)
    },

    formatPlaceholder(row, column, value) {
      return value == null ? '-' : value
    },

    async fetchSofts() {
      this.loading = true
      try {
        const { currentPage, pageSize, activeTab, queryMainName } = this
        const params = {
          current: currentPage,
          size: pageSize,
          sort: 'createTime',
          order: 'descending',
          model: activeTab === '0'
            ? { category: 2, name: queryMainName }
            : { category: 1, name: queryMainName }
        }

        const res = await getMainSoft(params)

        this.softs = res.rows || []
        this.total = Number(res.total)
      } catch (error) {
        this.$message.error(error.data.msg || '请稍后重试')
      } finally {
        this.loading = false
      }
    },

    openAddDialog() {
      this.currentSoft = {
        status: '1',
        category: this.activeTab === '0' ? '2' : '1'
      }
      this.isEditing = false
      this.readOnly = false
      this.dialogVisible = true
    },

    openEditDialog(row, type) {
      this.isEditing = true
      this.readOnly = type === 1
      this.currentSoft = { ...row }
      this.dialogVisible = true
    },

    visibleChange(value) {
      this.dialogVisible = value
    },

    async saveSystem(system) {
      this.loading = true
      try {
        const res = this.isEditing && system.id
          ? await updateSoft(system)
          : await createSoft(system)

        if (res.code === 200) {
          const message = this.isEditing ? '更新成功' : '保存成功'
          this.$message.success(message)
        } else {
          this.$message.error(res.msg)
        }
        this.fetchSofts()
      } catch (error) {
        this.$message.error('操作失败: ' + (error.data.msg || '请稍后重试'))
      } finally {
        this.dialogVisible = false
        this.loading = false
        window.scrollTo({ top: 0 })
      }
    },

    searchSoft() {
      this.currentPage = 1
      this.fetchSofts()
    },

    handleExport() {
      this.download('manager/soft/export', {
        ...this.queryParams
      }, `软著_${this.formatYMDDate(new Date())}.xlsx`)
    },

    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchSofts()
    },

    handleSizeChange(size) {
      this.pageSize = size
      this.fetchSofts()
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}

.userSearch {
  width: 100%;
  /* height: 50px; */
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
  padding: 15px 0 10px;
  /* border-bottom: 1px solid #eee; */
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

.add-soft-button {
  margin-left: 10px;
}

.el-table {
  width: 100%;
  overflow-x: auto;
  /* padding: 0 20px; */
}

.footer {
  float: right;
  margin: 30px 0;
}
</style>
