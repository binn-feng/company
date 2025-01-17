<template>
  <div class="sales-settings">
    <header-title>
      <template slot="title">销售指标设置（元）</template>
    </header-title>
    <div class="userSearch">
      <div class="userSearchBox">
        <el-input
          v-model.trim="responsePerson"
          v-hasPermi="['manager:goal:query']"
          placeholder="请输入姓名"
          class="search-input"
          clearable
          size="small"
          @keyup.enter.native="searchProcurement"
        />
        <el-button v-hasPermi="['manager:goal:query']" type="primary" class="search-button" size="small" @click="searchProcurement">搜索</el-button>
      </div>
      <el-button type="primary" :loading="loading" size="small" @click="oneClickSyncing">一键同步</el-button>
    </div>
    <el-tabs v-model="activeTab" @tab-click="handleCheckTab">
      <el-tab-pane v-for="(tab, index) in tabs" :key="tab.type" :label="tab.label" :name="String(index)">
        <el-table v-loading="loading" :data="metrics" style="width: 100%;">
          <el-table-column type="index" label="序号" align="center" />
          <el-table-column label="姓名" prop="responsePerson" align="center" />
          <el-table-column label="一月" prop="January" align="center" />
          <el-table-column label="二月" prop="February" align="center" />
          <el-table-column label="三月" prop="March" align="center" />
          <el-table-column label="四月" prop="April" align="center" />
          <el-table-column label="五月" prop="May" align="center" />
          <el-table-column label="六月" prop="June" align="center" />
          <el-table-column label="七月" prop="July" align="center" />
          <el-table-column label="八月" prop="August" align="center" />
          <el-table-column label="九月" prop="September" align="center" />
          <el-table-column label="十月" prop="October" align="center" />
          <el-table-column label="十一月" prop="November" align="center" />
          <el-table-column label="十二月" prop="December" align="center" />
          <el-table-column label="总计" prop="totalAmount" align="center" />
        </el-table>
        <div class="footer">
          <!-- <el-pagination
            :current-page="currentPage"
            :page-sizes="[10, 20, 30]"
            :page-size="pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          /> -->
        </div>
      </el-tab-pane>
    </el-tabs>
    <!-- <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div> -->
  </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import { getMetrics, getSync } from '@/api/basicData/saleSetting.js'

export default {
  components: {
    headerTitle
  },
  data() {
    return {
      activeTab: '0',
      responsePerson: '',
      tabs: [
        { label: '2023', type: '2023' },
        { label: '2024', type: '2024' },
        { label: '2025', type: '2025' },
        { label: '2026', type: '2026' }
      ],
      currentPage: 1,
      pageSize: 9999,
      total: 0,
      metrics: [],
      loading: false
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

    async fetchProducts() {
      this.loading = true
      try {
        const { currentPage, pageSize, responsePerson } = this
        const params = {
          current: currentPage,
          size: pageSize,
          order: 'descending',
          sort: 'mbgs.createTime',
          model: {
            targetYear: this.tabs[Number(this.activeTab)].type,
            responsePerson: responsePerson
          }
        }
        const res = await getMetrics(params)
        const groupedData = {}

        res.rows.forEach(row => {
          const responsePerson = row.responsePerson
          const monthIndex = new Date(row.beginDate).getMonth()
          const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
          const monthName = monthNames[monthIndex]

          if (!groupedData[responsePerson]) {
            groupedData[responsePerson] = {
              responsePerson,
              'January': null, 'February': null, 'March': null, 'April': null, 'May': null, 'June': null,
              'July': null, 'August': null, 'September': null, 'October': null, 'November': null, 'December': null,
              'totalAmount': 0
            }
          }
          const formattedValue = row.baseValue.toLocaleString()
          groupedData[responsePerson][monthName] = formattedValue
          groupedData[responsePerson]['totalAmount'] += row.baseValue
        })

        Object.values(groupedData).forEach(person => {
          // 格式化总金额
          person.totalAmount = person.totalAmount.toLocaleString()
        })
        const result = Object.values(groupedData)

        this.metrics = result || []
        this.total = Number(res.total)
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
      }
    },

    async oneClickSyncing() {
      this.loading = true
      try {
        const res = await getSync()
        if (res && res.code === 200) {
          this.$message.success('同步成功')
          this.fetchProducts()
        }
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
      }
    },

    searchProcurement() {
      this.currentPage = 1
      this.fetchProducts()
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
.sales-settings {
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.userSearch {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
  padding: 15px 0 10px;
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

.footer {
  float: right;
  margin: 30px 0;
}
</style>
