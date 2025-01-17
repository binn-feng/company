<template>
  <div class="container">
    <header-title :need-border="false">
      <template slot="title"> 报价设置 </template>
    </header-title>
    <el-form :model="form" label-width="150px">
      <el-row style="margin: 10px 0;">
        <el-col :span="3" class="ml50 left">
          <el-form-item label-width="0px">
            <div class="label-input" />
          </el-form-item>
        </el-col>
        <el-col :span="5" class="center">
          <div class="label-input">
            <label>默认税率</label>
          </div>
        </el-col>
        <el-col :span="6" class="center">
          <div class="label-input">
            <label>默认利润率</label>
          </div>
        </el-col>
      </el-row>
      <div v-for="(item, index) in items" :key="index" style="border-bottom: 1px solid #f0f0f0;">
        <el-row>
          <el-col :span="3" class="ml50 center">
            <el-form-item label-width="0px">
              <div class="label-input">
                <label>{{ item.label }}</label>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="5" class="center">
            <el-form-item label-width="0px">
              <div class="label-input">
                <div class="input-row">
                  <el-input-number
                    v-model="form[item.label + '_taxRate']"
                    class="input-with-unit"
                    placeholder="请输入默认税率"
                    :controls="false"
                    :precision="2"
                    :step="1"
                    :max="100"
                    @input="validateNumber(item.label + '_taxRate')"
                  />
                  <span class="percentage">%</span>
                  <span v-if="index === 4" style="display: block;margin-left: -10px;margin-bottom: -10px;">（销售税率）</span>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="center">
            <el-form-item label-width="0px">
              <div class="label-input">
                <div class="input-row">
                  <el-input-number
                    v-model="form[item.label + '_profitRate']"
                    class="input-with-unit"
                    placeholder="请输入默认利润率"
                    :controls="false"
                    :precision="2"
                    :step="1"
                    :max="100"
                    @input="validateNumber(item.label + '_profitRate')"
                  />
                  <span class="percentage">%</span>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <el-form-item>
        <el-button v-hasPermi="['manager:quote:add']" type="primary" @click="save">保存</el-button>
      </el-form-item>
    </el-form>
    <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div>
  </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import { saveQuoteSettings, getQuoteList } from '@/api/basicData/qoute'
import { formatDate } from '@/mixins/index'

export default {
  components: {
    headerTitle
  },
  data() {
    return {
      form: {
        remark: '',
        createBy: '',
        updateBy: ''
      },
      loading: false,
      items: [
        { label: '售前支持', type: 'SALES_SUPPORT' },
        { label: '产品', type: 'PRODUCT' },
        { label: '软件开发', type: 'SOFTWARE_DEVELOPMENT' },
        { label: '采购-自研硬件', type: 'SELF_DEVELOPED' },
        { label: '采购-外采硬件', type: 'EXTERNAL_PROCUREMENT' },
        { label: '实施', type: 'IMPLEMENT' },
        { label: '其他', type: 'OTHER' }
      ]
    }
  },
  created() {
    this.loading = true
  },
  mounted() {
    this.loadQuoteSettings()
  },
  methods: {
    async loadQuoteSettings() {
      try {
        const params = {
          current: 1,
          model: {},
          order: 'descending',
          size: 10,
          sort: 'id'
        }
        const response = await getQuoteList(params)

        if (response.code === 200) {
          const data = response.rows
          // 初始化表单数据
          this.items.forEach((item) => {
            this.$set(this.form, item.label + '_taxRate', 0)
            this.$set(this.form, item.label + '_profitRate', 0)
          })
          data.forEach((row) => {
            // const key = row.type.replace(/_/g, '') // 去掉下划线以匹配items中的label

            const matchingItem = this.items.find(
              (item) => item.type === row.type
            )
            if (matchingItem) {
              this.$set(
                this.form,
                matchingItem.label + '_taxRate',
                row.dutyRate || 0
              )
              this.$set(
                this.form,
                matchingItem.label + '_profitRate',
                row.profitability || 0
              )
            }
          })
          this.loading = false
        } else {
          this.$message.error(response.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg)
      }
    },
    async save() {
      this.loading = true
      const params = {
        managedQuoteList: this.items.map((item) => ({
          dutyRate: parseFloat(this.form[item.label + '_taxRate']) || 0,
          profitability: parseFloat(this.form[item.label + '_profitRate']) || 0,
          type: item.type,
          createBy: this.form.createBy,
          createTime: formatDate(new Date()),
          updateBy: this.form.updateBy,
          updateTime: formatDate(new Date())
        })),
        remark: this.form.remark,
        createBy: this.form.createBy,
        updateBy: this.form.updateBy,
        createTime: formatDate(new Date()),
        updateTime: formatDate(new Date())
      }

      try {
        const response = await saveQuoteSettings(params)
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
    },

    validateNumber(field) {
      const value = this.form[field]
      if (value === undefined || value === null) return
      const regex = /^\d*\.?\d{0,2}$/
      if (!regex.test(value)) {
        this.form[field] = value.slice(0, -1)
      }
      if (value) {
        // 将字符串转为数字，避免 NaN
        const numericValue = parseFloat(this.form[field])
        if (!isNaN(numericValue)) {
          this.form[field] = numericValue.toFixed(2)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  margin: 20px;
}

.input-row {
  width: 100%;
}

.el-form {
  margin-top: -10px;
}

.el-form-item {
  margin: 15px 0 !important;
}

.label-input {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-right: 10px;
}

.label-input label {
  // margin-bottom: 5px;
  width: 100%;

}

.el-input {
  margin-right: 5px;
}

.percentage {
  margin-left: 5px;
}

.el-button {
  float: right;
  margin-right: 20px;
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

.right {
  text-align: right;
  display: inline-block;
}

.center {
  text-align: center;
  display: inline-block;
}

.mt33 {
  margin-top: 40px;
}

.ml50 {
  margin-left: 50px;
  margin-right: 20px;
}
</style>
