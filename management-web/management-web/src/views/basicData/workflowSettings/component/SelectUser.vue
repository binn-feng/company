<template>
  <div class="detail-role">
    <el-dialog
      :title="selectType === 0 ? '选择发送至用户' : '选择抄送至用户'"
      :visible.sync="detailRoleDialogVisible"
      width="50%"
      :destroy-on-close="true"
      @close="handleClose"
    >
      <div slot="header">
        <span>选择用户</span>
      </div>
      <div class="transfer-container">
        <el-transfer
          v-model="selectedItems"
          class="content-row"
          :data="filteredData"
          :titles="['可选项', '已选项']"
          filterable
          :filter="filterText"
          :render-content="renderContent"
          :props="transferProps"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="default" @click="cancel">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listUser } from '@/api/system/user'
export default {
  props: {
    selectUserVisible: {
      type: Boolean,
      default: false
    },
    userDetail: {
      type: Array,
      default: () => ([])
    },
    selectType: {
      type: Number,
      default: () => (0)
    }
  },
  data() {
    return {
      detailRoleDialogVisible: false,
      selectedItems: [], // 已选中的项
      filterText: '', // 搜索文本
      dataList: []
    }
  },
  computed: {
    // 过滤后的数据
    filteredData() {
      if (!this.filterText) {
        return this.dataList
      }
      return this.dataList.filter(item => {
        return item.nickName.includes(this.filterText)
      })
    },
    transferProps() {
      return {
        key: 'userId',
        label: 'nickName',
        disabled: 'default'
      }
    }
  },
  watch: {
    selectUserVisible(val) {
      this.detailRoleDialogVisible = val
    },
    detailRoleDialogVisible(val) {
      if (val) {
        this.initData()
      }
    }
  },
  methods: {
    async initData() {
      try {
        const res = await listUser({ pageSize: 9999, pageNum: 1 })
        if (res.code === 200) {
          if (res.rows) {
            this.dataList = res.rows
            if (this.userDetail && this.userDetail.length > 0) {
              this.updateSelectedItems(this.userDetail)
            }
          }
        } else {
          this.$message.error(res.message || '加载数据失败，请重试')
        }
      } catch (error) {
        this.$message.error('获取用户数据失败', error)
      }
    },

    updateSelectedItems(userDetail) {
      this.selectedItems = []
      if (userDetail && userDetail.length > 0) {
        userDetail.forEach(m => {
          this.selectedItems.push(m.userId)
          this.dataList.forEach(n => {
            if (n.userId === m.userId) {
              n.default = m.default
            }
          })
        })
      }
    },

    renderContent(h, option) {
      return <span>{option.nickName}</span>
    },

    save() {
      const selectedSet = new Set(this.selectedItems)
      const list = this.dataList
        .filter(n => selectedSet.has(n.userId))
        .map(n => ({
          default: n.default || false,
          nickName: n.nickName,
          userId: n.userId
        }))
      this.$emit('send-data', list, this.selectType)
      this.handleClose()
    },

    cancel() {
      this.handleClose()
    },

    handleClose() {
      this.$emit('update:selectUserVisible', false)
      this.detailRoleDialogVisible = false
      this.selectedItems = []
      this.dataList = []
    }
  }
}
</script>

<style>
.el-dialog__header {
  border-bottom: 1px solid #e5e9eb;
}

.el-dialog {
  margin-bottom: 70px;
}

.content-row {
  display: flex;
  align-items: stretch;
}

/* .bordered-column {
  border: 1px solid #e5e9eb;
  padding: 10px;
  flex: 1;
  min-height: 400px;
  overflow-y: auto;
} */

.checkbox-container {
  /* max-height: 360px; */
  /* overflow-y: auto; */
}

/* .button-column {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 0 10px;
}
.button-column div {
  padding: 5px;
} */

/* @media (max-width: 768px) {
  .content-row {
    flex-direction: column;
  }

  .button-column {
    margin: 10px 0;
  }
} */

.transfer-container {
  width: 100%;
  margin:  auto;
  display: flex;
  justify-content: center;
  height: 400px;
}

.el-transfer-panel {
  width: 290px !important;
}

.sales-settings .el-input {
  width: 90% !important;
}

.el-transfer-panel__list.is-filterable {
  height: 300px;
  overflow-y: auto;
}

.el-transfer__buttons {
  display: flex;
  align-items: flex-start;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  button:last-child{
    margin-left: 0 !important;
  }
}
</style>
