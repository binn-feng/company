<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-miss" @click="gotoCreateReport()">
          <svg-icon icon-class="miss" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            <span v-if="noneReportWarningText!==''" style="color:red">{{ noneReportWarningText }}</span>
            <span v-else style="color:#45b97c">{{ noneReportText }}</span>
          </div>
          <p class="title-word">{{ name }} 您好</p>
        </div>
      </div>
    </el-col>
    <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="hi" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            欢迎登录
          </div>
          <p class="date">{{ systemname }}</p>
        </div>
      </div>
    </el-col>
    <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="fly" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            每日寄语
          </div>
          <el-tooltip class="item" effect="dark" :content="message" placement="bottom-end">
            <p class="title-word font-small">{{ message }}</p>
          </el-tooltip>
        </div>
      </div>
    </el-col>
    <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-money">
          <!-- <span class="el-icon-date card-panel-icon"></span> -->
          <svg-icon icon-class="calendar" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            日期时间
          </div>
          <p class="date font-small">{{ date }} {{ week }}<br>{{ time }}</p>
        </div>
      </div>
    </el-col>
    <!-- <el-col :xs="12" :sm="24" :lg="8" class="card-panel-col" id="aa">
      <div class="card-panels" >
        <div class="card-panel-description" style="margin-top:0 !important">
          <iframe scrolling="no" src="https://tianqiapi.com/api.php?style=tr&skin=pitaya" frameborder="0" :width="width" height="300" allowtransparency="true"></iframe>
        </div>
      </div>
    </el-col>  -->
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex'
// import elementResizeDetectorMaker from "element-resize-detector";
export default {
  computed: {
    ...mapGetters(['name', 'systemname'])
  },
  data() {
    return {
      time: '',
      date: '',
      week: '',
      noneReportWarningText: '',
      noneReportText: '',
      noneReportDate: null,
      message: '',
      endTime: new Date(),
      width: null
    }
  },
  created() {
    this.getNoneDayReport()
  },
  mounted() {
    this.updateTime()
    this.zeroPadding
    setInterval(this.updateTime, 1000)
    this.setNowTimes()
    // this.getrefs()
  },
  methods: {
    // getrefs(){
    // const _this = this;
    // const erd = elementResizeDetectorMaker();
    // erd.listenTo(document.getElementById("aa"), element => {
    //   this.width= element.offsetWidth-30
    // });
    // },
    setNowTimes() {
      const words = [
        '勿谓寸阴短，既过难再获。勿谓一丝微，既绍难再白',
        '你们的理想与热情，是你航行的灵魂的舵和帆',
        '人生如一出戏：重要的不是长度，而是表演的是否出色',
        '与卿再世相逢日，玉树临风一少年',
        '慷慨，尤其是还有谦虚，就会使人赢得好感',
        '独立思考，实事求是，锲而不舍，以勤补拙',
        '我扑在书上，就像饥饿的人扑在面包上',
        '书籍是屹立在时间的大海中的灯塔',
        '宁愿靠自己的力量打开我的前途，而不愿求有力者的垂青',
        '我们倒下去要爬起来，受到挫折要战斗得更好',
        '君子道长，小人道消。小人道长，君子道消',
        '人品学问，俱成于志气，无志气人，一事做不得',
        '善于利用时间的人，永远找不到充裕的时间',
        '我们处于什么方向不要紧，要紧的是我们正向什么方向移动',
        '谦虚不仅是一种装饰品，也是美德的护卫',
        '如果把生活比喻为创作的意境，那么阅读就像阳光',
        '左和右不一定谁更好，可是都比谁快谁占道好',
        '管理的控制工作是务使实践活动符合于计划',
        '有很多人是用青春的幸福作成功代价的',
        '令你忍受痛苦的事情，可能令你有甜蜜的回忆',
        '人就这么一辈子，开心也是一天，不开心也是一天',
        '别人怎么看我不重要，唯一重要的是;做真实的自己和现在的心情',
        '从今以后，别再过你应该过的人生，去过你想过的人生吧',
        '学习要加，骄傲要减，机会要乘，懒惰要除',
        '给自己时间，不要焦急，一步一步来，跟自己向上的心去合作，不要放弃对自己的爱护',
        '没完没了的工作，就是为了明日复明日的生活',
        '不管昨夜经历了怎样的泣不成声，早晨醒来这个城市依然车水马龙',
        '若是美好，叫做精彩。若是糟糕，叫做经历',
        '若现在就觉得失望无力，未来那么远你该怎么扛',
        '带着感恩的心启程，学会爱，爱父母，爱自己，爱朋友，爱他人',
        '比我差的人还没放弃，比我好的人仍在努力，我就更没资格说我无能为力',
        '今生今世，但愿岁月静好，现世安稳，每天都能看见他的笑颜',
        '愿你今年所有的遗憾都是来年惊喜的铺垫，加油年轻人',
        '我大好的一个人，凭什么跑到别人的生命里去当插曲',
        '坐看闲云宠辱不惊，闲看庭前花开花落，生活，最重要的就是淡定',
        '每天告诉自己要努力，即使看不到希望，也依然相信自己',
        '永远相信自己是最棒的，你的成功才会给你身边的人带来幸福',
        '再努把力，别让自己配不上自己的野心，也辜负了自己所受的苦难',
        '胜人者智，胜之者强。不是成功离我们太远，而是我们坚持的太少',
        '走的最慢的人，只要他不丧失目标，也比漫无目的徘徊的人走得快',
        '生活从不会抛弃人，是人想要抛弃生活。相信自己，从头来过，什么都不会晚',
        '你配不上自己的野心，也辜负了曾经历的苦难',
        '努力过的人总是最美的，即使最后她没赢，至少不会骗了自己',
        '本来无望的事，大胆尝试，往往能成功',
        '不管今天是什么天气，记得带上自己的阳光'
      ]
      const word = words[Math.floor(Math.random() * words.length)]
      this.message = word
    },
    updateTime() {
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
        this.zeroPadding(cd.getDate(), 2)

      const wk = cd.getDay()
      const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      this.week = weeks[wk]
    },
    zeroPadding(num, digit) {
      var zero = ''
      for (var i = 0; i < digit; i++) {
        zero += '0'
      }
      return (zero + num).slice(-digit)
    },
    formatDate(date) {
      return this.zeroPadding(date.getFullYear(), 4) +
        '-' +
        this.zeroPadding(date.getMonth() + 1, 2) +
        '-' +
        this.zeroPadding(date.getDate(), 2)
    },
    getNoneDayReport() {
      const params = {
        'currentPage': 1,
        'endDate': Date.parse(new Date(new Date().toLocaleDateString())) - 3600 * 1000 * 24 * 1,
        'pageSize': 1000,
        'projectId': 0,
        'searchVal': 'string',
        'startDate': 1630425600000,
        'userId': localStorage.getItem('id')
      }
      this.$store
        .dispatch('dailyreport/getNoneDayExchange', params)
        .then((res) => {
          const noneDailyReport = res.totalList
          if (noneDailyReport.length > 0) {
            const noneDaily = noneDailyReport[0]
            this.noneReportDate = Date.parse(new Date(noneDaily.date))
            if (this.noneReportDate >= 1630425600000) {
              this.noneReportWarningText = this.formatDate(new Date(noneDaily.date)) + ' 未写日报'
            } else {
              this.noneReportDate = null
              this.noneReportText = '恭喜您,您无遗漏任何日报'
            }
          } else {
            this.noneReportDate = null
            this.noneReportText = '恭喜您,您无遗漏任何日报'
          }
        })
        .catch(() => {
          this.noneReportDate = null
          this.noneReportText = '恭喜您,您无遗漏任何日报'
        })
    },
    gotoCreateReport() {
      if (this.noneReportDate != null) {
        this.$router.push({ name: 'DailyReportCreate', params: { code: 1000 }})
        this.$store.dispatch('dailyreport/setDate', this.noneReportDate)
        this.$bus.$emit('nonedate', this.noneReportDate)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.font-small {
  font-size: 18px;
}
.title-word{
  font-family: "Share Tech Mono", monospace;
  font-size: 20px;
  color: #009ad6;
  text-align: right;
}
.date{
   color: #009ad6;
   font-family: "Share Tech Mono", monospace;
   font-size: 20px;
   text-align: right;
}
.card-panels{
  height: 300px;
  cursor: pointer;
  font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
}
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 130px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-miss {
        background: #D81E06;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-miss {
      color: #D81E06;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 22px 0 0 6px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
        text-align: right;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  // .card-panel-description {
  //   display: none;
  // }
  .title-word{
  font-family: "Share Tech Mono", monospace;
  font-size: 12px;
}
.date{
   color: #009ad6;
   font-family: "Share Tech Mono", monospace;
   font-size: 12px;
}
  .card-panel-icon-wrapper {
    /* 0907 */
    // float: none !important;
    // width: 100%;
    // height: 100%;
    // margin: 0 !important;
    margin: 10px 0 0 10px!important;
    padding: 16px 30px 16px 30px!important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
@media (max-width:1500px) {
  .title-word{
  font-family: "Share Tech Mono", monospace;
  font-size: 12px !important;
}
.date{
   color: #009ad6;
   font-family: "Share Tech Mono", monospace;
   font-size: 12px !important;
}
}
@media (max-width:1366px) {
.card-panel-description{
  margin-top: 26px !important;
  margin-right: 8px !important;
}
}
@media (max-width:1920px) {
.card-panel-description{
  margin-top: 26px !important;
  margin-right: 12px !important;
}
 .title-word{
  font-family: "Share Tech Mono", monospace;
  font-size: 16px !important;
}
.date{
   font-family: "Share Tech Mono", monospace;
   font-size: 16px !important;
}
}
</style>
