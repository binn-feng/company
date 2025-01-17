<template>
  <div class="container">
    <header-title>
      <template slot="title">产品管理</template>
    </header-title>
    <div class="userSearch">
      <div class="userSearchBox">
        <el-input
          v-if="activeTab === '0'"
          v-model.trim="queryMainName"
          v-hasPermi="['manager:product:query']"
          placeholder="请输入主产品名称"
          class="search-input"
          clearable
          size="small"
          @keyup.enter.native="searchProduct"
        />
        <el-input
          v-if="activeTab === '1'"
          v-model.trim="queryParentName"
          v-hasPermi="['manager:product:query']"
          placeholder="请输入所属主产品名称"
          class="search-input"
          clearable
          size="small"
          @keyup.enter.native="searchProduct"
        />
        <el-input
          v-if="activeTab === '1'"
          v-model.trim="queryChildName"
          v-hasPermi="['manager:product:query']"
          placeholder="请输入子产品名称"
          class="search-input"
          size="small"
          clearable
          @keyup.enter.native="searchProduct"
        />
        <el-button
          v-hasPermi="['manager:product:query']"
          icon="el-icon-search"
          type="primary"
          class="search-button"
          size="small"
          @click="searchProduct"
        >搜索</el-button>
        <el-button
          v-hasPermi="['manager:product:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="small"
          @click="handleExport"
        >导出</el-button>
      </div>
      <el-button
        v-hasPermi="['manager:product:add']"
        icon="el-icon-plus"
        type="primary"
        class="add-product-button"
        size="small"
        @click="openAddDialog"
      >新建</el-button>
    </div>
    <div class="content">
      <el-tabs v-model="activeTab" @tab-click="handleCheckTab">
        <el-tab-pane v-for="(tab, index) in productTabs" :key="tab.type" :label="tab.label" :name="String(index)">
          <!-- 主产品 -->
          <el-table v-if="index === 0" v-loading="loading" :data="products" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="name" sortable label="主产品名称" />
            <el-table-column prop="shortName" sortable label="产品昵称" />
            <el-table-column prop="unit" sortable label="产品单位" />
            <el-table-column prop="nlSoftName" label="北光软著" :formatter="formatPlaceholder" />
            <el-table-column prop="xxSoftName" label="欣象软著" :formatter="formatPlaceholder" />
            <el-table-column prop="costPrice" label="内部成本价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column prop="extPrice" label="销售对外报价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column
              prop="description"
              label="产品概要描述"
              :formatter="formatPlaceholder"
              width="160"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.description">
                  {{ scope.row.description }}
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
            <el-table-column label="操作" width="160" fixed="right">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['manager:product:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
                <el-button v-if="scope.row.status === '1'" v-hasPermi="['manager:product:unPublish']" type="text" @click="changeProductStatus(scope.row)">下架</el-button>
                <el-button v-else v-hasPermi="['manager:product:publish']" type="text" @click="changeProductStatus(scope.row)">上架</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 子产品 -->
          <el-table v-if="index === 1" v-loading="loading" :data="products" class="el-table" style="width: 100%">
            <el-table-column type="index" label="序号" width="80" />
            <el-table-column prop="parentName" label="所属主产品名称" sortable />
            <el-table-column prop="name" label="子产品名称" />
            <el-table-column prop="shortName" sortable label="子产品昵称" />
            <el-table-column prop="unit" sortable label="产品单位" />
            <el-table-column prop="costPrice" label="内部成本价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column prop="extPrice" label="销售对外报价(元)" min-width="110" sortable :formatter="formatCurrency" />
            <el-table-column
              prop="description"
              label="产品概要描述"
              :formatter="formatPlaceholder"
              width="160"
            >
              <template slot-scope="scope">
                <div class="ellipsis-text" :title="scope.row.description">
                  {{ scope.row.description }}
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
            <el-table-column label="操作" width="160" fixed="right">
              <template v-slot="scope">
                <el-button title="详情" type="text" @click="openEditDialog(scope.row, 1)">详情</el-button>
                <el-button v-hasPermi="['manager:product:edit']" title="编辑" type="text" @click="openEditDialog(scope.row, 2)">编辑</el-button>
                <!-- <el-button type="text" @click="changeProductStatus(scope.row)">{{ scope.row.status === '1' ? '下架' : '上架'
                }}</el-button> -->
                <el-button v-if="scope.row.status === '1'" v-hasPermi="['manager:product:unPublish']" type="text" @click="changeProductStatus(scope.row)">下架</el-button>
                <el-button v-else v-hasPermi="['manager:product:publish']" type="text" @click="changeProductStatus(scope.row)">上架</el-button>
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
    <product-dialog
      :visible="dialogVisible"
      :is-editing="isEditing"
      :read-only="readOnly"
      :active-tab="activeTab"
      :current-product="currentProduct"
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
import ProductDialog from './component/ProductDialog.vue'
import { getMainProduct, getChildrenProduct, createProduct, updateProduct, publishProduct, unPublishProduct } from '@/api/basicData/product.js'

export default {
  components: {
    headerTitle,
    ProductDialog
  },
  data() {
    return {
      queryMainName: '',
      queryParentName: '',
      queryChildName: '',
      dialogVisible: false,
      isEditing: false,
      readOnly: false,
      currentProduct: {},
      products: [],
      currentPage: 1,
      pageSize: 10,
      activeTab: '0',
      productTabs: [
        { label: '主产品', type: 'parent' },
        { label: '子产品', type: 'child' }
      ],
      loading: false,
      total: 0
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    handleCheckTab(tab) {
      this.activeTab = tab.index
      this.fetchProducts()
    },

    formatCurrency(row, column, value) {
      if (value == null) return '-'
      return parseFloat(value).toFixed(2)
    },

    formatPlaceholder(row, column, value) {
      return value == null ? '-' : value
    },

    async fetchProducts() {
      this.loading = true
      try {
        const { currentPage, pageSize, activeTab, queryMainName, queryChildName, queryParentName } = this
        const params = {
          current: currentPage,
          size: pageSize,
          sort: 'createTime',
          order: 'descending',
          model: activeTab === '0'
            ? { category: 1, name: queryMainName }
            : { name: queryChildName, parentName: queryParentName }
        }

        const res = activeTab === '0'
          ? await getMainProduct(params)
          : await getChildrenProduct(params)

        this.products = res.rows || []
        this.total = Number(res.total)
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
      }
    },

    openAddDialog() {
      this.currentProduct = {
        status: '1',
        category: this.activeTab === '0' ? '1' : '2'
      }
      this.isEditing = false
      this.readOnly = false
      this.dialogVisible = true
    },

    openEditDialog(row, type) {
      this.isEditing = true
      this.readOnly = type === 1
      this.currentProduct = { ...row }
      this.dialogVisible = true
    },

    visibleChange(value) {
      this.dialogVisible = value
    },

    async changeProductStatus(row) {
      try {
        const action = row.status === '1' ? '下架' : '上架'
        await this.$confirm(`确认${action}该产品?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const params = {
          id: row.id,
          category: 1
        }
        const res = row.status === '1' ? await unPublishProduct(params) : await publishProduct(params)
        if (res.code === 200) {
          this.$message.success(`${action}成功`)
        } else {
          this.$message.error(res.msg)
        }
        this.fetchProducts()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败: ' + (error.data.msg || '请稍后重试'))
        }
      }
    },

    async saveSystem(system) {
      this.loading = true
      try {
        const res = this.isEditing && system.id
          ? await updateProduct(system)
          : await createProduct(system)

        if (res.code === 200) {
          const message = this.isEditing ? '更新成功' : '保存成功'
          this.$message.success(message)
        } else {
          this.$message.error(res.msg)
        }
        this.fetchProducts()
      } catch (error) {
        this.$message.error('操作失败: ' + (error.data.msg || '请稍后重试'))
      } finally {
        this.dialogVisible = false
        this.loading = false
        window.scrollTo({ top: 0 })
      }
    },

    searchProduct() {
      this.currentPage = 1
      this.fetchProducts()
    },

    handleExport() {
      this.download('manager/product/export', {}, `产品_${this.formatYMDDate(new Date())}.xlsx`)
    },

    handleCurrentChange(page) {
      this.currentPage = page
      this.fetchProducts()
    },

    handleSizeChange(size) {
      this.pageSize = size
      this.fetchProducts()
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

.add-product-button {
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
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
