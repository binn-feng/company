<template>
  <div class="container">
    <header-title :need-border="false">
      <template slot="title"> 预警设置 </template>
    </header-title>
    <el-form :model="form" label-width="230px">
      <el-form-item label="对外报价最低利润率要求" class="custom-label-item">
        <div class="input-with-percentage">
          <div>
            <el-input-number
              v-model="form.externalProfit"
              class="input-with-unit"
              placeholder="请输入对外报价最低利润率"
              type="text"
              :step="1"
              :controls="false"
              :precision="2"
              :max="100"
              @input="formatInput('externalProfit')"
            />
            <span style="margin-left: 5px">%</span>
          </div>
          <div class="tips">
            <span>注：  </span>
            <div>
              <span>该利润率适用于 -- 销售对外报价</span><br>
              <span>该利润率为 项目利润率（不含硬件）</span>
            </div>
          </div>
        </div>
      </el-form-item>
      <el-divider />

      <el-form-item label="合同签约最低利润率要求" class="custom-label-item">
        <div class="input-with-percentage">
          <div>
            <el-input-number
              v-model="form.contractProfit"
              class="input-with-unit"
              placeholder="请输入合同签约最低利润率"
              type="text"
              :step="1"
              :controls="false"
              :precision="2"
              :max="100"
              @input="formatInput('contractProfit')"
            />
            <span style="margin-left: 5px">%</span>
          </div>
          <div class="tips">
            <span>注：</span>
            <div>
              <span>该利润率适用于 -- 签约申请</span><br>
              <span>该利润率为 项目利润率（不含硬件）</span>
            </div>
          </div>
        </div>
        <!-- <div> -->
        <!-- </div> -->
      </el-form-item>
      <el-divider />

      <el-form-item>
        <el-button v-hasPermi="['manager:warning:add']" type="primary" @click="save">保存</el-button>
      </el-form-item>
    </el-form>
    <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div>
  </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import { saveWarningSettings, getWarningList } from '@/api/basicData/warning'
import { formatDate } from '@/mixins/index'

export default {
  components: {
    headerTitle
  },
  data() {
    return {
      form: {
        externalProfit: '', // 对外报价最低利润率
        contractProfit: '', // 合同签约最低利润率
        createBy: '',
        updateBy: '',
        remark: '', // 备注
        createTime: '',
        updateTime: ''
      },
      loading: false
    }
  },
  mounted() {
    this.loadWarningSettings()
  },
  methods: {
    async loadWarningSettings() {
      this.loading = true
      try {
        const params = {
          current: 1,
          model: {},
          order: 'descending',
          size: 10,
          sort: 'id'
        }
        const response = await getWarningList(params)

        if (response.code === 200) {
          const data = response.rows[0]
          if (data) {
            this.form.externalProfit = this.formatDecimal(data.externalProfit) // 格式化为两位小数
            this.form.contractProfit = this.formatDecimal(data.contractProfit) // 格式化为两位小数
            this.form.createBy = data.createBy || ''
            this.form.updateBy = data.updateBy || ''
            this.form.remark = data.remark || ''
            this.form.createTime = data.createTime || ''
            this.form.updateTime = data.updateTime || ''
          }
        } else {
          this.$message.error(response.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg, error)
      } finally {
        this.loading = false
      }
    },

    formatInput(field) {
      let value = this.form[field]

      // 正则表达式限制输入为数字和小数点
      const regex = /^(\d+(\.\d{0,2})?)?$/

      // 将输入的值替换成只包含数字和小数点的格式
      if (!regex.test(value)) {
        value = value.replace(/[^0-9.]/g, '') // 去掉非数字和小数点的字符
        const parts = value.split('.')
        if (parts.length > 1) {
          // 保留小数点前的部分和两位小数
          value = parts[0] + '.' + parts[1].substring(0, 2)
        }
      }

      this.form[field] = this.formatDecimal(value)
    },

    formatDecimal(value) {
      const num = parseFloat(value)
      return isNaN(num) ? '' : num.toFixed(2) // 格式化为两位小数或空值
    },

    async save() {
      this.loading = true
      const params = {
        contractProfit: parseFloat(this.form.contractProfit) || 0,
        externalProfit: parseFloat(this.form.externalProfit) || 0,
        createBy: this.form.createBy,
        createTime: formatDate(new Date()), // 设置为当前时间
        updateBy: this.form.updateBy,
        updateTime: formatDate(new Date()), // 设置为当前时间
        remark: this.form.remark,
        id: 0,
        params: {}
      }
      try {
        const response = await saveWarningSettings(params)
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

h2 {
  margin-bottom: 20px;
}

.input-with-percentage {
  display: flex;
  // align-items: center;
  // margin: 0 auto;
  // width: 400px;
}

.tips {
  margin-left: 30px;
  display: flex;
  font-style: italic;
}

.el-input {
  width: 200px;
}

.custom-label-item .el-form-item__label {
  text-align: right !important;
}

::v-deep.el-form-item__content {
  line-height: 10px;
}

.input-with-unit {
  width: 200px;
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }

  input[type="text"] {
    -moz-appearance: textfield;
  }
}
.el-form-item:first-child {
  margin: 10px 50px 40px !important;
}

.el-form-item {
  margin: 40px 50px !important;
}

.el-button {
  float: right;
  margin-right: 20px;
}

</style>
