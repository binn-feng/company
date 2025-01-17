<template>
  <div class="dashboard-container">
    <!-- <div class="title-text">
      <panel />
    </div>
    <div class="title-text">
      <charts />
    </div> -->
    <div>
      <el-calendar v-loading="dashboardLoading">
        <template slot="dateCell" slot-scope="{ data }">
          <div class="dates">
            {{ data.day.split("-").slice(2).join("-") }}
          </div>
          <div>
            <div v-if="holidayMap.has(data.day)" class="li-02 restDay">休息</div>
            <!-- <div
              v-for="(item, index) in calendarData"
              :key="index"
            >
              <div
                v-if="
                  data.day.split('-').slice(1)[0] ==
                  item.dateStr
                    .split('-')
                    .slice(1)
                    .join('-')
                    .split('', 2)
                    .join('')
                "
              >
                <div
                  v-if="
                    data.day.split('-').slice(2)[0] ==
                    item.dateStr
                      .split('-')
                      .slice(2)
                      .join('-')
                      .split('', 2)
                      .join('')
                  "
                  class="li-02"
                >
                  <div class="restDay">休息</div>
                </div>
              </div>
            </div> -->
          </div>
        </template>
      </el-calendar>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import panel from './components/panel.vue'
import charts from './components/charts.vue'
export default {
  name: 'Dashboard',
  components: {
    panel,
    charts
  },
  data() {
    return {
      time: '',
      date: '',
      // calendarData: [],
      holidayMap: new Map(),
      dashboardLoading: false
    }
  },
  computed: {
    ...mapGetters(['name', 'systemname'])
  },
  created() {
    this.getCalendar()
  },
  mounted() {
    this.updateTime()
    this.zeroPadding
    setInterval(this.updateTime, 1000)
  },
  methods: {
    getCalendar() {
      this.dashboardLoading = true
      // this.$store.dispatch('calendar/getCalendarList').then((res) => {
      //   // this.calendarData = res
      //   const holidayMap = new Map()
      //   res.forEach(item => {
      //     holidayMap.set(item.dateStr, item.dateStr)
      //   })
      //   this.holidayMap = holidayMap
      //   this.dashboardLoading = false
      // }).catch(() => {
      //   this.dashboardLoading = false
      // })
    },

    updateTime() {
      const week = [
        '星期天',
        '星期一',
        '星期二',
        '星期三',
        '星期四',
        '星期五',
        '星期六'
      ]
      const cd = new Date()
      this.time =
        this.zeroPadding(cd.getHours(), 2) +
        ':' +
        this.zeroPadding(cd.getMinutes(), 2) +
        ':' +
        this.zeroPadding(cd.getSeconds(), 2)
      this.date =
        this.zeroPadding(cd.getFullYear(), 4) +
        '-' +
        this.zeroPadding(cd.getMonth() + 1, 2) +
        '-' +
        this.zeroPadding(cd.getDate(), 2) +
        ' ' +
        week[cd.getDay()]
    },
    zeroPadding(num, digit) {
      let zero = ''
      for (let i = 0; i < digit; i++) {
        zero += '0'
      }
      return (zero + num).slice(-digit)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}
.dashboard-text {
  margin-top: 0;
}
.dashboard {
  /* &-container {
  } */
  &-text {
    font-size: 30px;
    line-height: 46px;
    text-align: center;
  }
}
#clock {
  font-family: "Share Tech Mono", monospace;
  color: #ffffff;
  text-align: center;
  color: #0000cc;
}
#clock .time {
  letter-spacing: 0.05em;
  font-size: 40px;
  padding: 5px 0;
}
#clock .date {
  letter-spacing: 0.1em;
  font-size: 24px;
}
#clock .name {
  letter-spacing: 0.1em;
  font-size: 12px;
  padding: 20px 0 0;
}
.li-02 {
  width: 100%;
  height: 86px;
  line-height: 86px;
  background-color: rgba(121,205,205, .6)
}
.restDay {
  display: flex;
  justify-content: center;
  font-family: "Share Tech Mono", monospace;
}
.dates {
  position: absolute;
}

</style>
<style >
.dashboard-container .el-calendar-table .el-calendar-day {
  padding: 0 !important;
}
.el-backtop, .el-calendar-table td.is-today {
    color: #409EFF;
    background:#eff7fe;
}
</style>
