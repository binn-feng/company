<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8" style="margin-bottom: 20px;">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-back"
          size="mini"
          @click="close"
        >返回</el-button>
      </el-col>
    </el-row>
    <h4 class="form-header h4">基本信息</h4>

    <el-form ref="form" :model="form" label-width="80px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item label="用户昵称" prop="nickName">
            <el-input v-model="form.nickName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item label="登录账号" prop="userName">
            <el-input v-model="form.userName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">角色信息</h4>
    <el-table ref="table" v-loading="loading" :row-key="getRowKey" @row-click="clickRow" @selection-change="handleSelectionChange" :data="roles.slice((pageNum-1)*pageSize,pageNum*pageSize)">
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" :reserve-selection="true" width="55" />
      <el-table-column label="角色编号" align="center" prop="roleId" />
      <el-table-column label="角色名称" align="center" prop="roleName" />
      <el-table-column label="权限字符" align="center" prop="roleKey" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="footer">
      <el-pagination
        :total="total"
        :page-sizes="[10, 20, 30]"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <!-- <pagination v-show="total>0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" /> -->

    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-170px;margin-top:70px;">
        <el-button @click="close()">返回</el-button>
        <el-button type="primary" @click="submitForm()">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getAuthRole, updateAuthRole } from '@/api/system/user'

export default {
  name: 'AuthRole',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 分页信息
      total: 0,
      pageNum: 1,
      pageSize: 10,
      // 选中角色编号
      roleIds: [],
      // 角色信息
      roles: [],
      // 用户信息
      form: {}
    }
  },
  created() {
    const userId = this.$route.params && this.$route.params.userId
    if (userId) {
      this.loading = true
      getAuthRole(userId).then((response) => {
        this.form = response.user
        this.roles = response.roles
        this.total = Number(this.roles.length)
        this.$nextTick(() => {
          this.roles.forEach((row) => {
            if (row.flag) {
              this.$refs.table.toggleRowSelection(row)
            }
          })
        })
        this.loading = false
      })
    }
  },
  methods: {
    /** 单击选中行数据 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.roleIds = selection.map((item) => item.roleId)
    },
    // 保存选中的数据编号
    getRowKey(row) {
      return row.roleId
    },
    /** 提交按钮 */
    submitForm() {
      const userId = this.form.userId
      const roleIds = this.roleIds.join(',')
      updateAuthRole({ userId: userId, roleIds: roleIds }).then((response) => {
        this.$message.success('授权成功')
        this.$router.push('/system/user')
        this.close()
      })
    },
    /** 关闭按钮 */
    close() {
      this.$router.back()
    },
    handleCurrentChange(page) {
      this.pageNum = page
      this.getList()
    },

    handleSizeChange(size) {
      this.pageSize = size
      this.getList()
    }
  }
}
</script>

<style scoped>
.footer {
  float: right;
  margin: 30px 0 0;
}
</style>
