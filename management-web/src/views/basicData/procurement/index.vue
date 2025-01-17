<template>
  <div class="container">
    <header-title>
      <template slot="title">采购管理</template>
    </header-title>
    <div class="userSearch">
      <div class="userSearchBox">
        <el-input
          v-if="activeTab === '0'"
          v-model.trim="queryMainName"
          v-hasPermi="['quote:ext:query', 'quote:self:query']"
          placeholder="请输入自研硬件名称"
          class="search-input"
          clearable
          size="small"
          @keyup.enter.native="searchProcurement"
        />
        <el-input
          v-if="activeTab === '1'"
          v-model.trim="queryChildName"
          v-hasPermi="['quote:ext:query', 'quote:self:query']"
          placeholder="请输入外采硬件名称"
          class="search-input"
          size="small"
          clearable
          @keyup.enter.native="searchProcurement"
        />
        <el-button
          v-hasPermi="['quote:ext:query', 'quote:self:query']"
          icon="el-icon-search"
          type="primary"
          class="search-button"
          size="small"
          @click="searchProcurement"
        >搜索</el-button>
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="small"
          @click="handleSelfExport"
        >自研导出</el-button>
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="small"
          @click="handleExtExport"
        >外采导出</el-button>
      </div>
      <el-button
        v-hasPermi="['quote:ext:add', 'quote:self:add']"
        icon="el-icon-plus"
        type="primary"
        class="add-procurement-button"
        size="small"
        @click="openAddDialog"
      >新建</el-button>
    </div>
    <div class="content">
      <el-tabs v-model="activeTab" @tab-click="handleCheckTab">
        <el-tab-pane v-for="(tab, index) in procurementTabs" :key="tab.type" :label="tab.label" :name="String(index)">
          <!-- 自研 -->
          <el-table v-if="index === 0" v-loading="loading" :data="procurements" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="name" sortable label="硬件名称" />
            <el-table-column prop="unit" label="单位" />
            <el-table-column prop="unitPrice" label="单价(元)" sortable :formatter="formatCurrency" />
            <!-- <el-table-column prop="rate" label="默认税率(%)" min-width="100" sortable :formatter="formatPlaceholder" /> -->
            <!-- <el-table-column prop="extPrice" label="建议销售价(元)" min-width="110" sortable :formatter="formatCurrency" /> -->
            <el-table-column
              prop="deviceDescription"
              label="设备参数"
              :formatter="formatPlaceholder"
              width="180"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.deviceDescription">
                  {{ scope.row.deviceDescription }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注"
              :formatter="formatPlaceholder"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.remark">
                  {{ scope.row.remark || '-' }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="当前状态">
              <template #default="scope">
                <el-tag v-if="scope.row.status === '1'" type="success" size="medium">
                  可用
                </el-tag>
                <el-tag v-else type="danger" size="medium">
                  不可用
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right" align="center">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['quote:ext:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
                <el-button v-if="scope.row.status === '1'" v-hasPermi="['manager:ext:unPublish']" type="text" @click="changeProcurementStatus(scope.row)">下架</el-button>
                <el-button v-else v-hasPermi="['manager:ext:publish']" type="text" @click="changeProcurementStatus(scope.row)">上架</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 外采 -->
          <el-table v-if="index === 1" v-loading="loading" :data="procurements" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="55" />
            <el-table-column prop="category" label="类型" />
            <el-table-column prop="name" sortable label="硬件名称" />
            <el-table-column prop="brand" label="品牌" />
            <el-table-column prop="spec" sortable label="规格型号" />
            <el-table-column prop="unit" label="单位" />
            <el-table-column prop="unitPrice" label="单价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column prop="type" label="发票类型">
              <template slot-scope="scope">
                <div>
                  {{ getType(scope.row.type) }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="rate" sortable label="采购发票税率(%)" min-width="100" />
            <el-table-column prop="estimatedCost" label="预估成本价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column prop="externalQuote" label="建议对外报价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column prop="date" sortable label="报价日期" min-width="100" />
            <el-table-column
              prop="deviceDescription"
              label="设备参数"
              :formatter="formatPlaceholder"
              width="200"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.deviceDescription">
                  {{ scope.row.deviceDescription }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注"
              width="100"
              :formatter="formatPlaceholder"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.remark">
                  {{ scope.row.remark || '-' }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="当前状态" width="110">
              <template #default="scope">
                <el-tag v-if="scope.row.status === '1'" type="success" size="medium">
                  可用
                </el-tag>
                <el-tag v-else type="danger" size="medium">
                  不可用
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130" fixed="right" align="center">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['quote:self:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
                <el-button v-if="scope.row.status === '1'" v-hasPermi="['manager:self:unPublish']" type="text" @click="changeProcurementStatus(scope.row)">下架</el-button>
                <el-button v-else v-hasPermi="['manager:self:publish']" type="text" @click="changeProcurementStatus(scope.row)">上架</el-button>
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
    <procurement-dialog
      :visible="dialogVisible"
      :is-editing="isEditing"
      :read-only="readOnly"
      :active-tab="activeTab"
      :current-procurement="currentProcurement"
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
import ProcurementDialog from './component/ProcurementDialog.vue'
import {
  getSelfProcurement,
  getExtProcurement,
  createSelfProcurement,
  createExtProcurement,
  updateSelfProcurement,
  updateExtProcurement,
  publishSelfProcurement,
  unPublishSelfProcurement,
  publishExtProcurement,
  unPublishExtProcurement
} from '@/api/basicData/procurement.js'

export default {
  components: {
    headerTitle,
    ProcurementDialog
  },
  data() {
    return {
      queryMainName: '',
      queryParentName: '',
      queryChildName: '',
      dialogVisible: false,
      isEditing: false,
      readOnly: false,
      currentProcurement: {},
      procurements: [],
      currentPage: 1,
      pageSize: 10,
      activeTab: '0',
      procurementTabs: [
        { label: '自研', type: 'parent' },
        { label: '外采', type: 'child' }
      ],
      loading: false,
      total: 0
    }
  },
  created() {
    this.fetchProcurements()
  },
  methods: {
    handleCheckTab(tab) {
      this.activeTab = tab.index
      this.fetchProcurements()
    },

    formatCurrency(row, column, value) {
      if (value == null) return '-'
      return parseFloat(value).toFixed(2)
    },

    formatPlaceholder(row, column, value) {
      return value == null ? '-' : value
    },

    getType(type) {
      switch (type) {
        case 'CATEGORICAL':
          return '专票'
        case 'ORDINARY':
          return '普票'
        case 'INCONCLUSIVE':
          return '不确定'
        default:
          return '未知'
      }
    },

    async fetchProcurements() {
      this.loading = true
      try {
        const { currentPage, pageSize, activeTab, queryMainName, queryChildName } = this
        const params = {
          current: currentPage,
          size: pageSize,
          sort: 'createTime',
          order: 'descending',
          model: activeTab === '0'
            ? { name: queryMainName }
            : { name: queryChildName }
        }

        const res = activeTab === '0'
          ? await getSelfProcurement(params)
          : await getExtProcurement(params)

        this.procurements = res.rows || []
        this.total = Number(res.total)
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
      }
    },

    openAddDialog() {
      this.currentProcurement = {
        status: '1',
        typeCategory: this.activeTab === '0' ? '1' : '2'
      }
      this.isEditing = false
      this.readOnly = false
      this.dialogVisible = true
    },

    openEditDialog(row, type) {
      this.isEditing = true
      this.readOnly = type === 1
      this.currentProcurement = {
        ...row,
        typeCategory: this.activeTab === '0' ? '1' : '2'
      }
      this.dialogVisible = true
    },

    visibleChange(value) {
      this.dialogVisible = value
    },

    async changeProcurementStatus(row) {
      try {
        const action = row.status === '1' ? '下架' : '上架'
        await this.$confirm(`确认${action}该产品?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const params = {
          id: row.id
        }
        const actionType = row.status === '1' ? 'unPublish' : 'publish' // 去下架或去上架
        const procurementType = this.activeTab === '0' ? 'Self' : 'Ext' // 自研或外采

        const actions = {
          unPublish: {
            Self: unPublishSelfProcurement,
            Ext: unPublishExtProcurement
          },
          publish: {
            Self: publishSelfProcurement,
            Ext: publishExtProcurement
          }
        }
        const selectedAction = actions[actionType][procurementType]
        const res = await selectedAction(params)
        if (res.code === 200) {
          this.$message.success(`${action}成功`)
        } else {
          this.$message.error(res.msg)
        }
        this.fetchProcurements()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败: ' + (error.data.msg || '请稍后重试'))
        }
      }
    },

    async saveSystem(system) {
      this.loading = true
      try {
        const operationType = this.isEditing && system.id ? 'update' : 'create' // 编辑还是新建
        const procurementType = this.currentProcurement.typeCategory === '1' ? 'Self' : 'Ext' // 自研或外采

        const actions = {
          update: {
            Self: updateSelfProcurement,
            Ext: updateExtProcurement
          },
          create: {
            Self: createSelfProcurement,
            Ext: createExtProcurement
          }
        }

        const selectedAction = actions[operationType][procurementType]
        const res = await selectedAction(system)

        if (res.code === 200) {
          const message = this.isEditing ? '更新成功' : '保存成功'
          this.$message.success(message)
        } else {
          this.$message.error(res.msg)
        }
        this.fetchProcurements()
      } catch (error) {
        this.$message.error('操作失败: ' + (error.data.msg || '请稍后重试'))
      } finally {
        this.dialogVisible = false
        this.loading = false
        window.scrollTo({ top: 0 })
      }
    },

    searchProcurement() {
      this.currentPage = 1
      this.fetchProcurements()
    },

    handleExtExport() {
      this.download('manager/ext/export', {}, `外采硬件_${this.formatYMDDate(new Date())}.xlsx`)
    },

    handleSelfExport() {
      this.download('manager/self/export', {}, `自研硬件_${this.formatYMDDate(new Date())}.xlsx`)
    },

    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchProcurements()
    },

    handleSizeChange(size) {
      this.pageSize = size
      this.fetchProcurements()
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

.add-procurement-button {
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

.ellipsis-text {
  /* width: 180px; */
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
