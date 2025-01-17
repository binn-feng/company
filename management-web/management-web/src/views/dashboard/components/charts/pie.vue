<template>
  <div :class="className"
    :style="{height:height,width:width}"
    v-loading="chartloading"
    id="pie"
    element-loading-text="数据加载中..."
    element-loading-spinner="el-icon-loading">
    <h3 style="text-align:center;font-size:20px">{{Txt}}</h3>
  </div>
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
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
      roleCode: localStorage.getItem('role-code'),
      chartloading: false,
      Txt:''
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.getDataSource();
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
      const datasource = []
      const domId = document.getElementById('pie')
      if (data && data.length > 0) {
        for (const item of data) {
          metadata.push(item.name);
          datasource.push({value: item.value, name: item.name});
        }
      }else{
        domId.innerHTML = `<h4 style="font-size:20px;text-align:center">暂无数据</h4>`
      }
      this.chart.setOption({
        title: {
            text: (this.roleCode === 'HR') ? '区域人员分布图' : ((this.roleCode === 'EMPLOYEE') ? '项目小时统计图' : '项目人员分布图'),
             subtext: ''
        },
         noDataLoadingOption: {
                    text: '暂无数据',//没有数据时显示的文字
                    effect: 'whirling',//loading效果，此为气泡，还有'spin' | 'bar' | 'ring' | 'whirling' | 'dynamicLine' | 'bubble'
                    effectOption: {
                        backgroundColor: "rgba(50,50,50,0)",//loading的背景
                    },
                    textStyle: {//没有数据时显示的文字的样式
                        fontSize: 32,
                        fontWeight: 'bold'
                    }
                },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)',
          backgroundColor: 'rgba(50,50,50,0.6)',
          borderWidth: 0,
          textStyle: {
            color: '#ffffff'
          },
          position:'top'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: metadata,
          type:'scroll',
          formatter:function(value)
              {  
                return value.length > 14 ? value.substring(0, 14) + "..." : value;
              }
        },
        series: [
          {
            name: (this.roleCode === 'HR') ? '区域人员分布图' : ((this.roleCode === 'EMPLOYEE') ? '项目小时统计图' : '项目人员分布图'),
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: datasource,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    getDataSource() {
      this.chartloading = true
      const params = {
        pjNumber:6
      }
      this.$store
        .dispatch((this.roleCode === 'HR') ? 'person/getUserBoardAreaUser': ((this.roleCode === 'EMPLOYEE') ? 'project/getProjectBoardList' : 'project/getProjectBoardUserList'),params)
        .then((res) => {
          this.chartloading = false
          this.initChart(res)
        })
        .catch((err) => {
          this.Txt = '服务端错误，请检查网络连接或联系管理员'
          this.chartloading = false
        });
    },
  }
}
</script>