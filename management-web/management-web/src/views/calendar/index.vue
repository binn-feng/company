 
<template>
  
  <div class="calendar">
    <header-title>
      <template slot="title"> 万年历 </template>
    </header-title>
    <div class="buttons">
      <el-button @click.native="addHoliday" type="success" size="small" :loading="addClenLoading">
        添加日期
      </el-button>
       <el-button type="danger" @click.native="delHoliday" size="small" :loading="delClenLoading">
        删除日期
      </el-button>
    </div>
    <div>
      <a-row class="calendarContent" style="margin: 0 auto; width: 100%">
      <a-calendar @select="onPanelChange" :locale="locale" v-loading="calendarLoading">
    <div slot="dateCellRender" slot-scope="value" class="events">
      <div v-for="item in getListData(value)" :key="item.dateStr" style="list-style:none;">
        <div v-if="item.dateStr" class="day">休息日</div>
      </div>
    </div>
  </a-calendar>
      </a-row>
    </div>
    
  </div>
</template>
<script>
import 'moment/locale/zh-cn';
import headerTitle from '@/components/Title/index.vue'
export default {
  components: {
    headerTitle,
  },
  data(){
    return{
       locale: {
          lang: {
            month: '月',
            year: '年',
          },
        },
        calendarData:[],
        datesData:[],
        deleteData:[],
        calendarLoading:false,
        addClenLoading:false,
        delClenLoading:false
    }
  },
  created(){
    this.getCalendar()
  },
  methods: {
    onPanelChange(value) {
      console.log(value.format('YYYY-MM-DD'));
      this.datesData = [
        {
          dateStr: value.format('YYYY-MM-DD'),
          dateYmd: 0,
          day: 0,
          month: 0,
          year: 0
        }
      ],
      this.deleteData = [
        {
          data:value.format('YYYY-MM-DD'),
          id:null
        }
      ]

    },
    getListData(value){
     var listData = []
      this.calendarData.forEach(e => {
        if (e.dateStr === value.format('YYYY-MM-DD')) {
          listData.push(e)
        }
      })
      return listData
    },
    getCalendar(){
      this.calendarLoading = true
      this.$store.dispatch('calendar/getCalendarList').then(res=>{
        this.calendarData = res
        this.calendarLoading = false
      }).catch(err=>{
        this.calendarLoading = false
      })
    },
    addHoliday(){
      this.addClenLoading = true
      this.$store.dispatch('calendar/addHoliday',this.datesData).then(res=>{
        this.$message.success('添加成功')
        this.addClenLoading = false
        this.getCalendar()
      }).catch(err=>{
        this.addClenLoading = false
      })
    },
    delHoliday(){
      this.delClenLoading = true
      this.$store.dispatch('calendar/delHoliday',this.deleteData).then(res=>{
        this.$message.success('删除成功')
        this.delClenLoading = false
        this.getCalendar()
      }).catch(err=>{
        this.delClenLoading = false
      })
    },
  },
};
</script>
<style >
.calendar{
  padding: 20px;
}
.calendar .day{
  background-color: rgba(121,205,205, .6);
  height:80px;
  width:100%;
  display: flex;
  justify-content: center;
  align-items: center;
  color:darkslategrey
}
.ant-radio-group {
  display: none;
}
.buttons{
  margin-top: 5px;
  margin-left: 20px;
  /* float: left;
  clear: both;
  z-index: 9999; */
  /* position: absolute;
  top: 80px; */
  
}
@media only screen
and (min-device-width : 330px)
and (max-device-width : 500px){
  .buttons .el-button{
    width: 47.6% !important;    
  }
  .buttons{
    margin-left: 0 !important;
  }
}
/* @media only screen
and (min-device-width : 320px){
  .buttons .el-button{
    width: 45.6% !important;    
  }
  .buttons{
    margin-left: 0 !important;
  }
} */

</style>