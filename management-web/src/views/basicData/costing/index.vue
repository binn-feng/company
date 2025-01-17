<template>
  <div id="app" class="container">
    <header-title :need-border="false">
      <template slot="title"> 成本设置 </template>
    </header-title>
    <el-form :model="form" label-width="150px">
      <el-row>
        <el-col :span="2" class="ml50">
          <el-form-item>
            <div />
          </el-form-item>
        </el-col>
        <el-col :span="6" class="center mr10">
          <el-form-item label-width="0px">
            <label>公司内部</label>
          </el-form-item>
        </el-col>
        <el-col :span="6" class="center mr10">
          <el-form-item label-width="0px">
            <label>本地驻场</label>
          </el-form-item>
        </el-col>
        <el-col :span="6" class="center mr10">
          <el-form-item label-width="0px">
            <label>异地驻场</label>
          </el-form-item>
        </el-col>
      </el-row>
      <div style="">
        <el-row>
          <el-col :span="2" class="center ml50">
            <el-form-item label-width="0px">
              <div class="label-input" />
            </el-form-item>
          </el-col>
          <el-col :span="6" class="center mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label class="unit">元/人月</label>
                <label class="unit">元/人天</label>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="center mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label class="unit">元/人月</label>
                <label class="unit">元/人天</label>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="center mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label class="unit">元/人月</label>
                <label class="unit">元/人天</label>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <div v-for="(item, index) in items" :key="index" style="border-bottom: 1px solid #f0f0f0;">
        <el-row>
          <el-col :span="2" class="center mt33 ml50">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label>{{ item.label }}</label>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <el-input-number
                  v-model.number="form[item.type + '_2']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
                <el-input-number
                  v-model.number="form[item.type + '_1']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <el-input-number
                  v-model.number="form[item.type + '_4']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
                <el-input-number
                  v-model.number="form[item.type + '_3']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="mr10">
            <el-form-item label-width="0px">
              <div class="label-input">
                <el-input-number
                  v-model.number="form[item.type + '_6']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
                <el-input-number
                  v-model.number="form[item.type + '_5']"
                  :controls="false"
                  :precision="2"
                  class="input-with-unit"
                >
                  <template #prefix>￥</template>
                </el-input-number>
              </div>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="2">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label class="lh50">元/人天</label>
                <label class="lh50">元/人月</label>
              </div>
            </el-form-item>
          </el-col> -->
        </el-row>
        <!-- <el-divider /> -->
      </div>
      <el-form-item>
        <el-button v-hasPermi="['manager:cost:add']" style="margin: 20px ;" type="primary" @click="saveCosting">保存</el-button>
      </el-form-item>
    </el-form>
    <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div>
  </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import { saveCosting as apiSaveCosting } from '@/api/basicData/costing'
import { getCostList } from '@/api/basicData/costing'
import { formatDate } from '@/mixins/index'

export default {
  components: {
    headerTitle
  },
  data() {
    return {
      form: {
        createBy: '',
        createTime: formatDate(new Date()),
        manageCostList: [], // 管理成本列表
        remark: '' // 备注
      },
      loading: false,
      items: [
        { label: '需求分析', type: 'RA' },
        { label: 'UI设计', type: 'UI' },
        { label: '开发', type: 'DEV' },
        { label: '测试', type: 'TEST' },
        { label: '项目管理', type: 'PM' },
        { label: '售前支持', type: 'PRE' },
        { label: '项目实施', type: 'IMP' }
      ]
    }
  },
  mounted() {
    this.loadProductData()
  },
  methods: {
    async loadProductData() {
      this.loading = true
      try {
        const params = {
          current: 1,
          model: {},
          order: 'descending',
          size: 10,
          sort: 'id'
        }
        const response = await getCostList(params)
        if (response.code === 200) {
          const productData = response.rows || []

          if (productData.length > 0) {
            const firstData = productData[0]

            // 更新表单公共信息
            this.form.createBy = firstData.createBy || ''
            this.form.createTime =
              firstData.createTime || formatDate(new Date())
            this.form.remark = firstData.remark || ''

            // 更新管理成本列表
            this.form.manageCostList = productData.map((costItem) => ({
              type: costItem.type,
              insiderDays: costItem.insiderDays || 0.0,
              insiderMonth: costItem.insiderMonth || 0.0,
              localPresenceDays: costItem.localPresenceDays || 0.0,
              localPresenceMonth: costItem.localPresenceMonth || 0.0,
              remotePresenceDays: costItem.remotePresenceDays || 0.0,
              remotePresenceMonth: costItem.remotePresenceMonth || 0.0
            }))

            // 更新表单数据
            this.items.forEach((item) => {
              const matchingCost = this.form.manageCostList.find(
                (cost) => cost.type === item.type
              )
              if (matchingCost) {
                // 回显数据
                this.$set(
                  this.form,
                  item.type + '_1',
                  matchingCost.insiderDays
                )
                this.$set(
                  this.form,
                  item.type + '_2',
                  matchingCost.insiderMonth
                )
                this.$set(
                  this.form,
                  item.type + '_3',
                  matchingCost.localPresenceDays
                )
                this.$set(
                  this.form,
                  item.type + '_4',
                  matchingCost.localPresenceMonth
                )
                this.$set(
                  this.form,
                  item.type + '_5',
                  matchingCost.remotePresenceDays
                )
                this.$set(
                  this.form,
                  item.type + '_6',
                  matchingCost.remotePresenceMonth
                )
              }
            })
          } else {
            this.$message.warning('没有找到相关数据')
          }
        } else {
          this.$message.error(response.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
      }
    },
    async saveCosting() {
      this.loading = true
      const params = {
        createBy: this.form.createBy,
        createTime: this.form.createTime,
        manageCostList: this.items.map((item) => ({
          type: item.type,
          insiderDays: this.form[item.type + '_1'] || 0,
          insiderMonth: this.form[item.type + '_2'] || 0,
          localPresenceDays: this.form[item.type + '_3'] || 0,
          localPresenceMonth: this.form[item.type + '_4'] || 0,
          remotePresenceDays: this.form[item.type + '_5'] || 0,
          remotePresenceMonth: this.form[item.type + '_6'] || 0
        })),
        remark: this.form.remark,
        id: ''
      }

      try {
        const response = await apiSaveCosting(params)

        if (response.code === 200) {
          this.$message.success('保存成功')
        } else {
          this.$message.error(response.message || '保存失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg)
      } finally {
        this.loading = false
        window.scrollTo({ top: 0 })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  margin: 20px;
}

.label-input {
  display: flex;
  // flex-direction: column;
  align-items:center;
  margin-right: 10px;
}

.input-with-unit {
  margin: 7px;
}

.input-with-unit {
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }

  input[type="number"] {
    -moz-appearance: textfield;
  }
}

.label-input label {
  // margin-bottom: 5px;
  width: 100%;
}

.el-input {
  margin-right: 5px;
}

.el-row {
  margin-top: 5px;
  margin-bottom: 5px;
}

.el-col {
  flex-wrap: wrap;
}

.el-form {
  margin-top: -10px;
}

.el-form-item {
  // margin-bottom: none !important;
  margin: 2px 0 !important;
}

.el-form-item__content {
  line-height: 100px !important;
}

.el-button {
  float: right;
  margin-right: 20px;
}

.left {
  text-align: left;
  display: inline-block;
}

.right {
  text-align: right;
  display: inline-block;
}

.center {
  text-align: center;
  display: inline-block;
}

.mt33 {
  margin-top: 8px;
}

.ml50 {
  margin-left: 40px;
}

.lh50 {
  line-height: 55px;
  font-size: 13px;
  font-weight: 400;
  color: #000;
}

.unit {
  font-size: 13px;
}

.mr10 {
  margin-right: 15px;
}
</style>
