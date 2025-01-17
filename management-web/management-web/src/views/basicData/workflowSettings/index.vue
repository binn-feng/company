<template>
  <div class="sales-settings">
    <header-title :need-border="false">
      <template slot="title"> 工作流设置 </template>
    </header-title>
    <div class="content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane
          v-for="(tab, index) in WorkflowTabs"
          :key="tab.type"
          :label="tab.label"
          :name="String(index)"
        >
          <!-- <div style="width: 100%;height: 230px;color: #000;margin: 5px 5px 30px;">
            流程图 {{ index + 1 }} {{ tab.label }}
          </div> -->
          <el-form v-if="formData[index]" ref="addUserRuleForm" :model="formData[index]" label-width="70px" class="ruleForm" :border="true">
            <el-form-item label="发送至" prop="senderInfo">
              <el-tag v-if="index === 0" effect="light">{所属售前}</el-tag>
              <el-tag v-if="index === 1 || index === 3 || index === 5" effect="light">{所属销售}</el-tag>
              <el-tag
                v-for="(item, itemIndex) in formData[index].senderInfo"
                :key="item.nickName"
                :closable="!item.default"
                :disable-transitions="false"
                effect="light"
                @close="handleClose('senderInfo', index, itemIndex)"
              >
                {{ item.nickName }}
              </el-tag>
              <el-button class="button-new-tag" size="small" type="primary" @click="handleSelectUser(formData[index].senderInfo, 0)">+ 添加</el-button>
            </el-form-item>
            <el-form-item label="抄送至" prop="carbonCopyInfo">
              <el-tag v-if="index === 0 || index === 2 || index === 4" effect="light">{所属销售}</el-tag>
              <el-tag
                v-for="(item, itemIndex) in formData[index].carbonCopyInfo"
                :key="item.nickName"
                :closable="!item.default"
                :disable-transitions="false"
                effect="light"
                @close="handleClose('carbonCopyInfo', index, itemIndex)"
              >
                {{ item.nickName }}
              </el-tag>
              <el-button class="button-new-tag" size="small" type="primary" @click="handleSelectUser(formData[index].carbonCopyInfo, 1)">+ 添加</el-button>
            </el-form-item>
            <el-form-item label="邮件主题" prop="emailSubject">
              <el-input v-model="formData[index].emailSubject" placeholder="请输入邮件主题" maxlength="50" @focus="setCurrentInput('emailSubject')" />
            </el-form-item>
            <el-form-item label="邮件模板">
              <!-- <el-input
                v-model="formData[index].emailTemplate"
                type="textarea"
                :rows="10"
                placeholder="请输入邮件模板"
                maxlength="400"
                @focus="setCurrentInput('emailTemplate')"
              /> -->
              <vue-editor
                v-model="formData[index].emailTemplate"
                placeholder="请输入邮件模板"
                maxlength="400"
                @focus="setCurrentInput('emailTemplate')"
              />
              <span>主题/模板支持关键字：</span>
              <el-button
                v-for="item in canSelectList[index].labels"
                :key="item"
                size="mini"
                plain
                @click="insertText(item)"
              >
                {{ item }}
              </el-button>
            </el-form-item>
          </el-form>
          <div style="margin-top: 30px" />
        </el-tab-pane>
      </el-tabs>
      <div class="actions">
        <el-popover
          v-model="toDefaultVisible"
          placement="top"
          width="200"
        >
          <p style="text-align: center;padding-top: 10px;">确认恢复默认设置？</p>
          <div style="text-align: center; padding-bottom: 5px;">
            <el-button size="mini" @click="toDefaultVisible = false">取消</el-button>
            <el-button type="primary" size="mini" @click="comfirmDefault">确定</el-button>
          </div>
          <el-button slot="reference" v-hasPermi="['manager:setting:add', 'manager:setting:edit']" type="default" style="margin-right: 10px;">恢复默认设置</el-button>
        </el-popover>
        <el-button v-hasPermi="['manager:setting:add', 'manager:setting:edit']" type="primary" @click="save">保存</el-button>
      </div>
    </div>
    <select-user
      :select-user-visible.sync="selectUserVisible"
      :user-detail="userDetail"
      :select-type="selectType"
      @send-data="handleChildData"
    />
    <div v-if="loading" class="spinner-overlay">
      <a-spin />
    </div>
  </div>
</template>

<script>
import { saveWorkflow, getWorkflow } from '@/api/basicData/workflow'
import headerTitle from '@/components/Title/index.vue'
import SelectUser from './component/SelectUser.vue'
import currentFormData from './formData'
import { VueEditor } from 'vue2-editor'
export default {
  components: {
    headerTitle,
    SelectUser,
    VueEditor
  },
  data() {
    return {
      activeTab: 0,
      selectUserVisible: false,
      selectType: 0,
      WorkflowTabs: [
        { label: '需成本报价', type: 'NEED_COST_QUOTATION' },
        { label: '已完成成本报价', type: 'COMPLETE_COST_QUOTATION' },
        { label: '报价审批', type: 'QUOTATION_APPROVAL' },
        { label: '报价审批结果通知', type: 'QUOTATION_APPROVAL_RESULT_NOTIFICATION' },
        { label: '签约申请', type: 'SIGNING_APPLICATION' },
        { label: '签约审批结果通知', type: 'SIGNING_APPROVAL_RESULT_NOTIFICATION' },
        { label: '丢单通知', type: 'LOSS_NOTIFICATION' }
      ],
      userDetail: [],
      defaultFormData: currentFormData,
      formData: {},
      loading: false,
      toDefaultVisible: false,
      canSelectList: [
        { labels: ['{商机主题}', '{日期}', '{报价系统链接}'] },
        { labels: ['{售前部门}', '{商机主题}', '{报价类型}', '{版本号}', '{日期}', '{成本总额}', '{报价系统链接}'] },
        { labels: ['{商机主题}', '{日期}', '{所属销售}', '{所属售前}', '{成本总额}', '{销售对外报价}', '{项目报价利润率}', '{继续跟进原因}', '{报价系统链接}'] },
        { labels: ['{商机主题}', '{销售报价审批结果}', '{日期}', '{成本总额}', '{销售对外报价}', '{项目报价利润率}', '{报价系统链接}'] },
        { labels: ['{商机主题}', '{日期}', '{所属销售}', '{所属售前}', '{成本总额}', '{北光合同总金额}', '{北光合同金额不含硬件部分}', '{总成本利润率}', '{成本利润率}', '{继续签约原因}', '{报价系统链接}', '{签约申请详情图片}'] },
        { labels: ['{商机主题}', '{日期}', '{签约申请审批结果}', '{成本总额}', '{销售对外报价}', '{项目利润率}', '{报价系统链接}'] },
        { labels: ['{商机主题}', '{日期}', '{所属销售}', '{所属售前}', '{成本总额}', '{销售对外报价}', '{项目报价利润率}', '{项目签约利润率}', '{报价申请结果}', '{申请签约金额}', '{签约申请审批结果}', '{丢单原因}', '{报价系统链接}'] }
      ],
      currentInput: ''
    }
  },
  created() {
    this.initFormData()
  },
  methods: {
    async initFormData() {
      try {
        const res = await getWorkflow({ pageSize: 9999, pageNum: 1 })
        if (res.code === 200) {
          if (res.rows) {
            this.formData = res.rows
            this.formData.forEach(item => {
              if (item.emailTemplate) {
                item.emailTemplate = item.emailTemplate
                  .replace(/<br\s*\/?>/g, '\n')
                  .trim()
              }
            })
          }
        } else {
          this.$message.error(res.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg, error)
      }
    },

    handleChildData(selectedItems, selectType) {
      if (selectType === 0) {
        this.formData[this.activeTab].senderInfo = selectedItems
      } else {
        this.formData[this.activeTab].carbonCopyInfo = selectedItems
      }
    },

    handleSelectUser(users, type) {
      this.userDetail = users
      this.selectUserVisible = true
      this.selectType = type
    },

    handleClose(type, index, itemIndex) {
      if (Array.isArray(this.formData[index][type])) {
        this.formData[index][type].splice(itemIndex, 1)
      }
    },

    getUserIds(infoArray) {
      return infoArray.filter(m => m.userId).map(m => m.userId).join(',')
    },

    async save() {
      let isEmpty = false
      this.formData.forEach((item, index) => {
        item.type = this.WorkflowTabs[index].type
        item.senderInfo.forEach
        // 处理发送的人
        item.sender = this.getUserIds(item.senderInfo)
        // 处理抄送的人
        item.carbonCopy = this.getUserIds(item.carbonCopyInfo)
        if (item.emailSubject === '' || item.emailSubject === null || item.emailTemplate === '' || item.emailTemplate === null) {
          isEmpty = true
        }
      })
      if (isEmpty) {
        return this.$message.error('请完善信息之后再次提交')
      }
      this.loading = true
      const updatedList = this.formData.map(item => {
        return {
          ...item,
          emailSubject: item.emailSubject.trim(),
          emailTemplate: item.emailTemplate
            .replace(/\n/g, '<br>') // 替换换行符为 <br>
            .trim()
        }
      })
      try {
        const res = await saveWorkflow(updatedList)
        if (res.code === 200) {
          this.$message.success('保存成功')
        } else {
          this.$message.error(res.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error(error.data.msg, error)
      } finally {
        this.loading = false
        window.scrollTo({ top: 0 })
      }
    },

    toDefaultSet() {
      this.toDefaultVisible = true
    },

    comfirmDefault() {
      this.formData = JSON.parse(JSON.stringify(this.defaultFormData))
      this.toDefaultVisible = false
      this.save()
    },

    setCurrentInput(inputType) {
      // 设置当前输入框类型
      this.currentInput = inputType
    },
    insertText(item) {
      if (this.currentInput === 'emailSubject') {
        this.formData[this.activeTab].emailSubject += item
      } else if (this.currentInput === 'emailTemplate') {
        this.formData[this.activeTab].emailTemplate += item
      }
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

.sales-settings .el-tabs {
  flex: 1;
}

.sales-settings .el-table .el-table__header {
  width: 100% !important;
}

.sales-settings .el-table .el-table__body {
  width: 100% !important;
}

.sales-settings .actions {
  margin-top: 20px;
  text-align: end;
}

.sales-settings .el-table {
  flex: 1;
}

.sales-settings .el-input {
  width: 100%;
  resize: none;
}

.sales-settings .el-input .el-input__inner {
  /* border: none; */
}

@media (max-width: 768px) {
  .sales-settings .el-table-column {
    width: 100% !important;
  }
}

.content {
  padding: 0 20px;
}

.el-form-item__label {
  text-align: left;
}

.ruleForm {
  padding-left: 30px;
  margin-top: 30px;
}

.el-tag + .el-tag {
    margin-left: 10px;
  }
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
