<template>
  <el-dialog :visible.sync="visible" :title="isEditing ? readOnly ? '查看软著' : '编辑软著' : '新建软著'" destroy-on-close @close="closeDialog">
    <!-- 北光科技 -->
    <el-form v-if="activeTab == '0'" ref="form" :model="currentSoft" :rules="formRules" label-width="120px" :disabled="readOnly">
      <el-form-item label="所属公司" prop="category">
        <template>
          <el-radio-group v-model="currentSoft.category" size="mini" :disabled="true">
            <el-radio label="2">北光科技</el-radio>
            <el-radio label="1">欣象科技</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="软著名称" prop="name">
        <el-input v-model="currentSoft.name" />
      </el-form-item>
      <el-form-item label="软著简称" prop="shortName">
        <el-input v-model="currentSoft.shortName" />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="登记号" prop="registrationMark">
            <el-input v-model="currentSoft.registrationMark" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="版本号" prop="versionNumber">
            <el-input v-model="currentSoft.versionNumber" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="登记批准日期" prop="regTime">
            <el-date-picker v-model="currentSoft.regTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次发表日期" prop="pushTime">
            <el-date-picker v-model="currentSoft.pushTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="对应欣象软著" prop="linkId">
        <el-select v-model="currentSoft.linkId" placeholder="请选择对应欣象软著" filterable clearable style="width: 100%;">
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="对应北光主产品" prop="description">
        <el-select v-model="currentSoft.productId" placeholder="请选择对应北光主产品" filterable clearable style="width: 100%;">
          <el-option
            v-for="item in productOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <!-- 欣象科技 -->
    <el-form v-else ref="form" :model="currentSoft" :rules="formRules" label-width="120px" :disabled="readOnly">
      <el-form-item label="所属公司" prop="category">
        <template>
          <el-radio-group v-model="currentSoft.category" size="mini" :disabled="true">
            <el-radio label="2">北光科技</el-radio>
            <el-radio label="1">欣象科技</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="软著名称" prop="name">
        <el-input v-model="currentSoft.name" />
      </el-form-item>
      <el-form-item label="软著简称" prop="shortName">
        <el-input v-model="currentSoft.shortName" />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="登记号" prop="registrationMark">
            <el-input v-model="currentSoft.registrationMark" type="number" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="版本号" prop="versionNumber">
            <el-input v-model="currentSoft.versionNumber" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="登记批准日期" prop="regTime">
            <el-date-picker v-model="currentSoft.regTime" value-format="yyyy-MM-dd" type="date" placeholder="请选择日期" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次发表日期" prop="pushTime">
            <el-date-picker v-model="currentSoft.pushTime" value-format="yyyy-MM-dd" type="date" placeholder="请选择日期" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="对应北光软著" prop="linkId">
        <el-select v-model="currentSoft.linkId" placeholder="请选择对应北光软著" filterable clearable style="width: 100%;" @change="handleParentChange">
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="对应北光主产品" prop="description">
        <el-input v-model="currentSoft.productName" readonly disabled />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
      <el-button v-if="!readOnly" type="primary" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getSoftOptions } from '@/api/basicData/soft.js'
import { getProductOptions } from '@/api/basicData/product.js'
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
    currentSoft: {
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
        category: [{ required: true, message: '请输入所属公司', trigger: 'blur' }],
        name: [{ required: true, message: '请输入软著名称', trigger: 'blur' }],
        registrationMark: [{ required: true, message: '请输入登记号', trigger: 'blur' }],
        versionNumber: [{ required: true, message: '请输入版本号', trigger: 'blur' }],
        regTime: [{ required: true, message: '请输入登记批准日期', trigger: 'blur' }]
      },
      id: 0,
      options: [],
      productOptions: [],
      statusDisabled: false
    }
  },
  watch: {
    visible(val) {
      if (val) {
        if (this.currentSoft.category === '2') {
          this.fetchSofts('1')
          this.fetchProducts()
        } else {
          this.fetchSofts('2')
        }
      }
    }
  },
  methods: {
    changeEndTime(val) {
      this.currentSoft.regTime = (Date.parse(val) - 1) + 24 * 60 * 60 * 1000
    },
    async fetchSofts(category) {
      this.options = []
      const params = {
        category: category,
        from: 1
      }
      const res = await getSoftOptions(params)
      if (res && res.length > 0) {
        this.options = res
      }
      if (this.currentSoft.linkId) {
        this.options.push({ id: this.currentSoft.linkId, name: this.currentSoft.linkName })
      }
    },

    async fetchProducts() {
      this.productOptions = []
      const params = {
        category: '1',
        status: 1
      }
      const res = await getProductOptions(params)
      if (res && res.length > 0) {
        this.productOptions = res
      }
      if (this.currentSoft.productId) {
        this.productOptions.push({ id: this.currentSoft.productId, name: this.currentSoft.productName })
      }
    },

    handleParentChange(e) {
      const item = this.productOptions.find(m => m.id === e)
      if (item) {
        this.currentSoft.productName = item.productName
      }
    },

    async save() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.$emit('save', this.currentSoft)
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
