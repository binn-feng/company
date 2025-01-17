<template>
    <div class="app-container">
        <header-title :need-border="true" style="margin-bottom: 20px;">
            <template slot="title">待成本报价</template>
        </header-title>
        <el-form size="small" :inline="true">
            <el-form-item>
                <el-input v-model.trim="queryParams.model.query" placeholder="请输入" clearable size="small" style="width: 240px" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" size="small" @click="handleQuery">搜索</el-button>
                <el-button size="small" @click="resetQuery">刷新</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8" style="margin-bottom: 10px;">
            <el-col :span="1.5">
                <el-button v-hasPermi="['system:role:export']" type="warning" plain icon="el-icon-download"
                    size="mini">导出</el-button>
            </el-col>
        </el-row>

        <el-table :data="data" style="width: 100%" :loading="loading">
            <el-table-column type="selection" min-width="55" align="center" />
            <el-table-column label="序号" type="index" min-width="50" />
            <el-table-column label="商机名称" prop="name" min-width="250" />
            <el-table-column label="客户名称" prop="customersName" min-width="150" />
            <el-table-column label="所属销售" prop="saleName" min-width="150" />
            <el-table-column label="产品类别" prop="category" min-width="150" />
            <el-table-column label="报价内容" prop="quoteContent" min-width="300" />
            <el-table-column label="所属售前" prop="preSaleName" min-width="150" />
        </el-table>

        <div class="footer">
            <el-pagination :total="total" :page-sizes="[10, 20, 30]" :current-page="queryParams.current"
                :page-size="queryParams.size" layout="total, sizes, prev, pager, next, jumper"
                @current-change="handleCurrentChange" @size-change="handleSizeChange" />
        </div>
    </div>
</template>

<script>
import headerTitle from '@/components/Title/index.vue'
import { fetchOpportunities } from '@/api/statement'
export default {
    components: { headerTitle },
    data() {
        return {
            data: [],  // 列表数据
            queryParams: {
                current: 1, // 当前页码
                size: 10, // 每页条数
                model: {
                    query: '', // 查询参数
                    statusList: ['1'],
                }
            },
            total: 0, // 总条数
            loading: false, // 加载状态
        };
    },
    created() {
        this.getList()
    },
    methods: {
        getList() {
            this.loading = true
            fetchOpportunities('pageWithSales', this.queryParams).then(response => {
                this.data = response.rows
                this.total = Number(response.total)
                this.loading = false
            }).catch(error => {
                console.error('Error fetching opportunities:', error);
                this.loading = false;
            });
        },

        handleCurrentChange(page) {
            this.queryParams.current = page
            this.getList()
        },

        handleSizeChange(size) {
            this.queryParams.size = size
            this.getList()
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.current = 1
            this.getList()
        },
        resetQuery() {
            // 重置查询条件
            this.queryParams.model.query = '';
            // 重置分页参数
            this.queryParams.current = 1;
            this.queryParams.size = 10;
            // 获取数据
            this.getList();
        },
    }
}
</script>
<style scoped>
.footer {
    float: right;
    margin: 30px 0;
}
</style>
