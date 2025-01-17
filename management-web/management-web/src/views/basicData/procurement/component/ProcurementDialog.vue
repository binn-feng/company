<template>
  <el-dialog :visible.sync="visible" :title="isEditing ? readOnly ? '查看采购单' : '编辑采购单' : '新建采购单'" destroy-on-close @close="closeDialog">
    <!-- 自研 -->
    <el-form v-if="activeTab == '0'" ref="form" :model="currentProcurement" :rules="formRules" label-width="120px" :disabled="readOnly">
      <el-form-item label="类别" prop="typeCategory">
        <template>
          <el-radio-group v-model="currentProcurement.typeCategory" size="mini" :disabled="true">
            <el-radio label="1">自研</el-radio>
            <el-radio label="2">外采</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="硬件名称" prop="name">
        <el-input v-model="currentProcurement.name" />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="单位" prop="unit">
            <el-input v-model="currentProcurement.unit" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单价(元)" prop="unitPrice">
            <el-input v-model="currentProcurement.unitPrice" type="number" @input="updateExtPrice" @blur="formatPrice('unitPrice')" />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="默认采购发票税率(%)" prop="rate">
            <el-input v-model="currentProcurement.rate" type="number" max="100" min="0" @input="updateExtPrice" @blur="formatPrice('rate')" />
          </el-form-item>
        </el-col> -->
      </el-row>
      <!-- <el-form-item label="建议销售价(元)" prop="extPrice">
        <el-input v-model="currentProcurement.extPrice" type="number" :disabled="true" @blur="formatPrice('costPrice')" />
      </el-form-item> -->
      <el-form-item label="设备参数" prop="deviceDescription">
        <el-input
          v-model="currentProcurement.deviceDescription"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="currentProcurement.remark"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <template>
          <el-radio v-model="currentProcurement.status" label="1">可用</el-radio>
          <el-radio v-model="currentProcurement.status" label="2">不可用</el-radio>
        </template>
      </el-form-item>
    </el-form>
    <!-- 外采 -->
    <el-form v-else ref="form" :model="currentProcurement" :rules="formRules" label-width="130px" :disabled="readOnly">
      <el-form-item label="类别" prop="typeCategory">
        <template>
          <el-radio-group v-model="currentProcurement.typeCategory" size="mini" :disabled="true">
            <el-radio label="1">自研</el-radio>
            <el-radio label="2">外采</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="硬件名称" prop="name">
        <el-input v-model="currentProcurement.name" />
      </el-form-item>
      <el-form-item label="类型" prop="category">
        <el-input v-model="currentProcurement.category" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="currentProcurement.unit" />
      </el-form-item>
      <el-form-item label="规格型号" prop="spec">
        <el-input v-model="currentProcurement.spec" />
      </el-form-item>
      <el-form-item label="发票类型" prop="type">
        <template>
          <el-select v-model="currentProcurement.type" placeholder="请选择发票类型" style="width: 100%;" @change="updatePrice">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </template>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="单价(元)" prop="unitPrice">
            <el-input v-model="currentProcurement.unitPrice" type="number" @input="updatePrice" @blur="formatPrice('unitPrice')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购发票税率(%)" prop="rate">
            <el-input v-model="currentProcurement.rate" type="number" @input="updatePrice" @blur="formatPrice('rate')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="预估成本价(元)" prop="estimatedCost">
            <el-input v-model="currentProcurement.estimatedCost" type="number" disabled @blur="formatPrice('costPrice')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议对外报价(元)" prop="externalQuote">
            <el-input v-model="currentProcurement.externalQuote" type="number" disabled @blur="formatPrice('extPrice')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="品牌" prop="brand">
        <el-input v-model="currentProcurement.brand" />
      </el-form-item>
      <el-form-item label="设备参数" prop="deviceDescription">
        <el-input
          v-model="currentProcurement.deviceDescription"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="报价日期" prop="date">
        <el-date-picker v-model="currentProcurement.date" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" style="width: 100%;" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="currentProcurement.remark"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <template>
          <el-radio-group v-model="currentProcurement.status" size="mini" :disabled="statusDisabled">
            <el-radio label="1">可用</el-radio>
            <el-radio label="2">不可用</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
      <el-button v-if="!readOnly" type="primary" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getQuoteList } from '@/api/basicData/qoute'
export default {
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    isEditing: {
      type: Boolean,
      default: false
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    currentProcurement: {
      type: Object,
      required: true
    },
    activeTab: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      formRules: {
        typeCategory: [{ required: true, message: '请输入硬件名称', trigger: 'blur' }],
        name: [{ required: true, message: '请输入硬件名称', trigger: 'blur' }],
        unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
        unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }],
        rate: [{ required: true, message: '请输入默认税率', trigger: 'blur' }],
        extPrice: [{ required: true, message: '请输入建议销售价', trigger: 'blur' }],
        // deviceDescription: [{ required: true, message: '请输入设备参数', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        category: [{ required: true, message: '请输入类型', trigger: 'blur' }],
        spec: [{ required: true, message: '请输入规格型号', trigger: 'blur' }],
        type: [{ required: true, message: '请选择发票类型', trigger: 'blur' }],
        estimatedCost: [{ required: true, message: '请输入预估成本价', trigger: 'blur' }],
        externalQuote: [{ required: true, message: '请输入建议对外报价', trigger: 'blur' }],
        brand: [{ required: true, message: '请输入品牌', trigger: 'blur' }],
        date: [{ required: true, message: '请选择报价日期', trigger: 'blur' }]
      },
      id: 0,
      options: [
        { label: '专票', value: 'CATEGORICAL' },
        { label: '普票', value: 'ORDINARY' },
        { label: '不确定', value: 'INCONCLUSIVE' }
      ],
      xxOptions: [],
      nlOptions: [],
      statusDisabled: false,
      xxSoftDisabled: false,
      nlSoftDisabled: false,
      salesRate: 0 // 销售税率
    }
  },
  mounted() {
    this.loadQuoteSettings()
    // this.fetchProcurements()
  },
  methods: {
    formatPrice(prop) {
      const value = parseFloat(this.currentProcurement[prop])
      if (!isNaN(value)) {
        if (prop === 'rate') {
          this.currentProcurement[prop] = value.toFixed(2)
        } else {
          this.currentProcurement[prop] = value.toFixed(8)
        }
      } else {
        this.currentProcurement[prop] = '0.00'
      }
    },

    async loadQuoteSettings() {
      try {
        const params = {
          current: 1,
          model: {},
          order: 'descending',
          size: 9999,
          sort: 'id'
        }
        const response = await getQuoteList(params)
        if (response.code === 200 && response.rows.length > 3) {
          const data = response.rows
          this.salesRate = data[2].dutyRate || 0
        } else {
          this.$message.error(response.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg)
      }
    },

    updateExtPrice() {
      // 计算建议销售价 = 单价 * (1 + 税率/100)
      this.currentProcurement.extPrice = (this.currentProcurement.unitPrice * (1 + this.currentProcurement.rate / 100)).toFixed(8)
    },

    updatePrice() {
      // 专票：预估成本价 = 单价/(1+税率)   建议对外报价 = 单价
      // 普票和不确定：预估成本价 = 单价，建议对外报价 = 预估成本价 ×（1+税率）

      // 新公式 - 0108
      // 专票：预估成本价 = 单价/(1+采购发票税率)   建议对外报价 =  预估成本价 ×（1+销售税率）
      // 普票和不确定：预估成本价 = 单价，建议对外报价 = 预估成本价 ×（1+销售税率）

      // 专票
      if (this.currentProcurement.type === 'CATEGORICAL') {
        // 预估成本价
        this.currentProcurement.estimatedCost = (this.currentProcurement.unitPrice / (1 + this.currentProcurement.rate / 100)).toFixed(8)
        // 建议对外报价
        this.currentProcurement.externalQuote = (((this.currentProcurement.unitPrice / (1 + this.currentProcurement.rate / 100)).toFixed(8)) * (1 + this.salesRate / 100)).toFixed(8)
      } else {
        // 预估成本价
        this.currentProcurement.estimatedCost = this.currentProcurement.unitPrice
        // 建议对外报价
        this.currentProcurement.externalQuote = (this.currentProcurement.unitPrice * (1 + this.salesRate / 100)).toFixed(8)
      }
    },

    async save() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.$emit('save', this.currentProcurement)
      }
    },

    closeDialog() {
      this.$emit('visibleChange', false)
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
