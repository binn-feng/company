<template>
  <el-row :gutter="32" class="panel-group">
    <el-col :xs="24" :sm="24" :lg="8" class="card-panel-col">
        <div class="chart-wrapper">
          <Pie />
        </div>
    </el-col>
    <el-col :xs="24" :sm="24" :lg="8" class="card-panel-col">
      <div class="chart-wrapper">
          <Bar />
      </div>
    </el-col>
    <el-col :xs="24" :sm="24" :lg="8" class="card-panel-col">
     <div class="chart-wrapper">
        <weather />
    </div>
    </el-col>
  </el-row>
</template>

<script>
import Weather from './charts/weather';
import Pie from './charts/pie';
import Bar from './charts/bar.vue';

export default {
  components: {
    Weather,
    Pie,
    Bar
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin: 5px auto 5px;

.chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

  @media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
