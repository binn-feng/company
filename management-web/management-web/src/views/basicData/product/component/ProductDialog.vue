<template>
  <el-dialog :visible.sync="visible" :title="isEditing ? readOnly ? '查看产品' : '编辑产品' : '新建产品'" destroy-on-close @close="closeDialog">
    <!-- 主产品 -->
    <el-form v-if="activeTab == '0'" ref="form" :model="currentProduct" :rules="formRules" label-width="120px" :disabled="readOnly">
      <el-form-item label="产品类别" prop="category">
        <template>
          <el-radio-group v-model="currentProduct.category" size="mini" :disabled="true">
            <el-radio label="1">主产品</el-radio>
            <el-radio label="2">子产品</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="主产品名称" prop="name">
        <el-input v-model="currentProduct.name" />
      </el-form-item>
      <el-form-item label="主产品昵称" prop="shortName">
        <el-input v-model="currentProduct.shortName" />
      </el-form-item>
      <el-form-item label="产品单位" prop="unit">
        <el-input v-model="currentProduct.unit" />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="北光内部报价" prop="costPrice">
            <el-input v-model="currentProduct.costPrice" type="number" @blur="formatPrice('costPrice')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对外销售报价" prop="extPrice">
            <el-input v-model="currentProduct.extPrice" type="number" @blur="formatPrice('extPrice')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="北光软著名称" prop="linkId">
        <el-select
          v-model="currentProduct.nlSoftId"
          placeholder="请选择对应北光软著"
          :disabled="nlSoftDisabled"
          style="width: 100%;"
          clearable
          filterable
          @change="handleBGChange"
        >
          <el-option
            v-for="item in nlOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="欣象软著名称" prop="xxrz">
        <el-select
          v-model="currentProduct.xxSoftId"
          placeholder="请选择对应北光软著"
          :disabled="xxSoftDisabled"
          style="width: 100%;"
          clearable
          filterable
          @change="handleXXChange"
        >
          <el-option
            v-for="item in xxOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="产品概要描述" prop="description">
        <el-input
          v-model="currentProduct.description"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="产品状态" prop="status">
        <template>
          <el-radio v-model="currentProduct.status" label="1">可用</el-radio>
          <el-radio v-model="currentProduct.status" label="2">不可用</el-radio>
        </template>
      </el-form-item>
    </el-form>
    <!-- 子产品 -->
    <el-form v-else ref="form" :model="currentProduct" :rules="formRules" label-width="120px" :disabled="readOnly">
      <el-form-item label="产品类别" prop="category">
        <template>
          <el-radio-group v-model="currentProduct.category" size="mini" :disabled="true">
            <el-radio label="1">主产品</el-radio>
            <el-radio label="2">子产品</el-radio>
          </el-radio-group>
        </template>
      </el-form-item>
      <el-form-item label="所属主产品" prop="parentId">
        <template>
          <el-select v-model="currentProduct.parentId" placeholder="请选择所属主产品" style="width: 100%;" filterable @change="handleParentChange">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.nameAndStatus"
              :value="item.id"
            />
          </el-select>
        </template>
      </el-form-item>
      <el-form-item label="子产品名称" prop="name">
        <el-input v-model="currentProduct.name" />
      </el-form-item>
      <el-form-item label="子产品昵称" prop="shortName">
        <el-input v-model="currentProduct.shortName" />
      </el-form-item>
      <el-form-item label="产品单位" prop="unit">
        <el-input v-model="currentProduct.unit" />
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="北光报价" prop="costPrice">
            <el-input v-model="currentProduct.costPrice" type="number" @blur="formatPrice('costPrice')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对外销售报价" prop="extPrice">
            <el-input v-model="currentProduct.extPrice" type="number" @blur="formatPrice('extPrice')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="产品概要描述" prop="description">
        <el-input
          v-model="currentProduct.description"
          type="textarea"
          :rows="4"
          maxlength="400"
        />
      </el-form-item>
      <el-form-item label="产品状态" prop="status">
        <template>
          <el-radio-group v-model="currentProduct.status" size="mini" :disabled="statusDisabled">
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
import { getMainProduct } from '@/api/basicData/product.js'
import { getSoftOptions } from '@/api/basicData/soft.js'

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
    currentProduct: {
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
        category: [{ required: true, message: '请输入产品类别', trigger: 'blur' }],
        name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        // shortName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
        costPrice: [{ required: true, message: '请输入内部成本价', trigger: 'blur' }],
        extPrice: [{ required: true, message: '请输入对外销售报价', trigger: 'blur' }],
        // description: [{ required: true, message: '请输入产品概要描述', trigger: 'blur' }],
        status: [{ required: true, message: '请输入产品状态', trigger: 'blur' }],
        parentId: [{ required: true, message: '请选择所属主产品', trigger: 'blur' }]
      },
      id: 0,
      options: [],
      xxOptions: [],
      nlOptions: [],
      statusDisabled: false,
      xxSoftDisabled: false,
      nlSoftDisabled: false
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.fetchProducts()
        this.fetchNlSofts('2')
        this.fetchXxSofts('1')
      }
    }
  },
  mounted() {
    // this.fetchProducts()
  },
  methods: {
    formatPrice(prop) {
      const value = parseFloat(this.currentProduct[prop])
      if (!isNaN(value)) {
        this.currentProduct[prop] = value.toFixed(2)
      } else {
        this.currentProduct[prop] = '0.00'
      }
    },

    async fetchNlSofts(category) {
      this.nlOptions = []
      const params = {
        category: category,
        from: 2
      }
      const res = await getSoftOptions(params)
      if (res && res.length > 0) {
        this.nlOptions = res
      }
      if (this.currentProduct.nlSoftId) {
        this.nlOptions.push({ id: this.currentProduct.nlSoftId, name: this.currentProduct.nlSoftName })
      }
    },

    async fetchXxSofts(category) {
      this.xxOptions = []
      const params = {
        category: category,
        from: 2
      }
      const res = await getSoftOptions(params)
      if (res && res.length > 0) {
        this.xxOptions = res
      }
      if (this.currentProduct.xxSoftId) {
        this.xxOptions.push({ id: this.currentProduct.xxSoftId, name: this.currentProduct.xxSoftName })
      }
    },

    async fetchProducts() {
      try {
        const params = {
          current: this.currentPage,
          size: 9999,
          model: {
            category: 1,
            sort: 'createTime',
            order: 'descending'
          }
        }
        const res = await getMainProduct(params)
        if (res.code === 200) {
          this.options = res.rows.map(m => {
            if (m.status === '2') {
              return { ...m, name: m.name + '（不可用）' }
            }
            return m
          })
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error('获取产品失败 ', error)
      }
    },

    handleParentChange(e) {
      const item = this.options.find(m => m.id === e)
      if (item) {
        this.statusDisabled = item.status === '2'
        this.currentProduct.status = item.status === '2' ? '2' : '1'
      }
    },

    handleBGChange(e) {
      const item = this.nlOptions.find(m => m.id === e)
      this.xxSoftDisabled = false
      if (item && item.linkId !== null && item.linkId !== '') {
        this.currentProduct.xxSoftId = item.linkId
        this.xxSoftDisabled = true
      }
    },

    handleXXChange(e) {
      const item = this.xxOptions.find(m => m.id === e)
      this.nlSoftDisabled = false
      if (item && item.linkId !== null && item.linkId !== '') {
        this.currentProduct.nlSoftId = item.linkId
        this.nlSoftDisabled = true
      }
    },

    async save() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.$emit('save', this.currentProduct)
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
