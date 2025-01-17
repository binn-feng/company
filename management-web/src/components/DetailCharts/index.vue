<template>
  <div id="detail-charts" >
    
    <el-dialog
      title="图标详细"
      :visible.sync="chartsDetaildialogVisible"
      width="80%"
      id="dialogcharts"
      ref="getheight"
    >
      <div slot="title" class="header-title">
        <span class="project-title">
          图表详情
        </span>
        <el-button type="danger" @click="closeChartsDetail" size="small"
          >返 回</el-button
        >
      </div>
      <div id="spancharts">
        <basic-pie :deepData="deepData" ref="charts" :height="height" :key="detailChart"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import BasicPie from '../BasicPie/index.vue'
export default {
  props: {
    isOpen: {
      type: Boolean,
      default: false,
    },
    detailChartsData: {
      type: Object,
      default: () => {},
    },
    height:{
      type:String,
      default:'750px'
    }
  },
  components:{
    BasicPie 
  },
  data() {
    return {
      chartsDetaildialogVisible: false,
      chart: null,
      deepData: {},
      detailChart:1
    };
  },
  watch: {
    isOpen(val) {
      ++this.detailChart
      this.chartsDetaildialogVisible = val;
    },
    detailChartsData(val){
      this.deepData = val
    }
  },
  mounted(){},
  methods: {
    closeChartsDetail() {
      this.$emit("update:isOpen", false);
      this.$emit('clearData')
      ++this.detailChart
    },
  },
};
</script>

<style>
#detail-charts .el-dialog {
  height: 100% !important;
  margin-top: 0 !important;
}
#detail-charts .header-title {
  display: flex;
  justify-content: space-between;
}
#detail-charts .el-dialog__headerbtn {
  display: none;
}
#detail-charts .el-dialog__header {
  border-bottom: 1px solid #e5e9eb;
}
#detail-charts .el-dialog__body {
  overflow: auto;
  max-height: calc(123vh - 281px);
}
</style>