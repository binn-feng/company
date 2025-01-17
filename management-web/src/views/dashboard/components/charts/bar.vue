<template>
  <div
    v-loading="chartloading"
    :class="className"
    :style="{height:height,width:width}"
    id="bar"
    element-loading-text="数据加载中..."
    element-loading-spinner="el-icon-loading">
    <h3 style="text-align:center;font-size:20px">{{Txt}}</h3>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

const animationDuration = 6000
const color = '#36a3f7'
const decalColor = 'rgba(51, 51, 51, 0.2)'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '380px'
    }
  },
  data() {
    return {
      chart: null,
      chartloading: false,
      roleCode: localStorage.getItem('role-code'),
      Txt:''
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.getDataSource()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart(data) {
      this.chart = echarts.init(this.$el, 'macarons')
      const metadata = []
      const datasource = {
        name: (this.roleCode === 'HR') ? '人员总计' : '工时总计',
        type: 'bar',
        stack: 'vistors',
        barWidth: '55%',
        data: [],
        calculable: true,
        animationDuration
      }
      const domId = document.getElementById('bar')
      if (data && data.length > 0) {
        for (const item of data) {
          metadata.push(item.name)
          datasource.data.push(item.value)
        }
      }else{
        domId.innerHTML = `<h4 style="font-size:20px;text-align:center">暂无数据</h4>`
      }

      this.chart.setOption({
        title: {
          text: (this.roleCode === 'HR') ? '区域人员详情' : '工时消耗统计',
          subtext: ''
        },
        tooltip: {
          trigger: 'axis',
          formatter(params){
            const role = localStorage.getItem('role-code')
            const item = params[0];
            if(role=='HR'){
              return `
                ${item.name}<br/>
                人员总计: ${item.data}<br/>
               `;
            }else{
               return `
                ${item.name}<br/>
                人/时: ${item.data} 小时<br/>
                人/天: ${(item.data/8).toFixed(2)} 天<br/>
                人/月: ${(item.data/8/22).toFixed(2)} 月
               `;
            }
            
          },
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          backgroundColor: 'rgba(50,50,50,0.6)',
          borderWidth: 0,
          textStyle: {
            color: '#ffffff'
          }
        },
        grid: {
          top: 35,
          left: '2%',
          right: '2%',
          bottom: 0,
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: metadata,
          show: true,
          axisTick: {
            alignWithLabel: true
          },
          axisLabel: {
            rotate: 20,
            formatter:function(value)
              {  
                return value.length > 14 ? value.substring(0, 14) + "..." : value;
              }
          }
        }],
        yAxis: [{
          type: 'value',
          show: true,
          axisTick: {
            show: false
          }
        }],
        series: datasource,
        color: color,
        aria: {
          label: {
            description: (this.roleCode === 'HR') ? '这是区域人员详情柱状图表' : '这是工时消耗统计柱状图表',
            enabled: true
          },
          enabled: true,
          decal: {
            show: true,
            decals: [{
              color: decalColor,
              dashArrayX: [1, 0],
              dashArrayY: [2, 5],
              symbolSize: 1,
              rotation: Math.PI / 6
            }]
          }
        }
      })
    },
    getDataSource() {
      this.chartloading = true
      const params = {
        pjNumber:6
      }
      this.$store
        .dispatch((this.roleCode === 'HR') ? 'person/getUserBoardAreaUser' : 'project/getProjectBoardList', params)
        .then((res) => {
          this.chartloading = false
          this.initChart(res)
        })
        .catch(() => {
          this.Txt = '服务端错误，请检查网络连接或联系管理员'
          this.chartloading = false
        })
    }
  }
}
</script>