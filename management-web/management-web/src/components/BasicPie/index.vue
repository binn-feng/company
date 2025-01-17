<template>
  <div id="pie" ref="basicpie" :style="{height:deepHeight,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import resize from '@/views/dashboard/components/charts/mixins/resize'
export default {
  mixins: [resize],
  props: {
    deepData: {
      type: Object,
      default: () => {}
    },
    height: {
      type: String,
      default: '750px'
    },
    width: {
      type: String,
      default: '100%'
    }
  },
  data() {
    return {
      chart: null,
      chartsData: {},
      deepHeight: ''
    }
  },
  watch: {
    deepData: {
      handler(val) {
        if (val) {
          this.initCharts()
        }
      }
    },
    height: {
      handler(val) {
        if (val) {
          this.deepHeight = val
          this.initCharts()
        }
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
    const height = document.getElementById('app').offsetHeight
    this.deepHeight = height - 150 + 'px'
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initCharts() {
      this.chart = echarts.init(this.$refs.basicpie, 'macarons')
      this.chart.setOption({
        legend: {
          left: 'center',
          bottom: '5',
          data: this.deepData.names,
          type: 'scroll'
        },
        series: [
          {
            name: '1234',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 175],
            center: ['50%', '35%'],
            data: this.deepData.resData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    }
  }

}
</script>

<style>
</style>
