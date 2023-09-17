<template>
  <div>
      <el-dialog title="日历设置" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="920px">
        <el-calendar v-model="value">
          <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
          <template slot="dateCell" slot-scope="{date, data}">
            <el-row :class="data.isSelected ? 'is-selected' : ''">
              {{ data.day.split('-').slice(1).join('-') }}  <i v-if="data.isSelected" class="el-icon-location-outline"></i>
            </el-row>
            <el-row class="default-date text-align-center" title="股票日" v-if="isWork(date) && !holidayDetail(data.day).holiday "><i class="el-icon-loading i-font" @click="dateShow(data.day)"></i></el-row>
            <el-row class="green-date text-align-center" v-if="isWeekend(date) || holidayDetail(data.day).holiday"><i class="el-icon-remove-outline i-font"></i></el-row>
            <el-row class="weekend-font green-date text-align-center padding-top8" title="周末" v-if="isWeekend(date)">周末</el-row>
            <el-row class="weekend-font green-date text-align-center padding-top8" :title="holidayName(holidayDetail(data.day).holiday)" v-if="holidayDetail(data.day).holiday">
              <span @click="removeSet(holidayDetail(data.day))">{{holidayName(holidayDetail(data.day).holiday)}}</span>
            </el-row>
          </template>
        </el-calendar>
      </el-dialog>
      <JieriModal :visible.sync="holidayVisible" :day.sync="day" @cancel="holidayCance"></JieriModal>
    </div>
  </template>
  <script>
    import JieriModal from "./jieri.vue";
    export default {
      name:"calendar-dialog",
      components: { JieriModal },
      props: {
        visible:{
            type:Boolean,
            default: false
        }
      },
      data() {
        return {
            isShow:false,
            value: new Date(),

            holidayVisible:false,
            day: "",
            workList:[],
        }
      },
      watch:{
        visible(newVal) {
          this.isShow = newVal;
          if (newVal)  this.form = this.row;
        }
      },
      created(){
        this.getData();
      },
      methods: {
        holidayCance(){
          this.holidayVisible = false;
          this.getData();
        },
        getData(){
          this.api({url: "/intro/workList", method: "post", data: {}}).then(res => {
            this.workList = res.list || [];
          })
        },
        holidayDetail(date){
          const hl = {};
          for(let i =0; i<this.workList.length; i++){
              if(this.workList[i].workDate === date){
                Object.assign(hl,this.workList[i]);
                break;
              }
          }
          return hl;
        },

        holidayName(holiday){
          switch(holiday){
            case "1" :
              return "元旦节";
            case "2" :
              return "除夕";
            case "3" :
              return "春节";
            case "4" :
              return "清明节";
            case "5" :
              return "劳动节";
            case "6" :
              return "端午节";
            case "7" :
              return "中秋节";
            case "8" :
              return "国庆节";
            case "9" :
              return "其它节日";
            default:
              return "工作日";
          }
        },

        removeSet(row){
          let _vue = this;
          this.$confirm('确定要重置'+row.workDate+'?', '提示', {
            confirmButtonText: '确定',
            showCancelButton: false,
            type: 'warning'
          }).then(() => {
            let data = {id:row.id};
            _vue.api({
              url: "/intro/remove",
              method: "post",
              data: data
            }).then(() => {
              this.$message.success('重置成功')
              _vue.getData()
            })
          })
        },


        cancelClick() {
            this.$emit("cancel");
        },
        isWork(nextDate){
            if(!this.isWeekend(nextDate)){
              return true;
            }
            return false
        },
        isWeekend(nextDate){
          const day = new Date(nextDate).getDay();
          if([0,6].includes(day)){
            return true;
          }
          return false;
        },

        dateShow(day){
          this.holidayVisible = true;
          this.day = day;
        }
      }
    }
  </script>
  <style lang="scss" scoped>
  .is-selected {
    color: #1989FA;
  }
  .default-date{
    color: red;
  }
  .green-date{
    color: green;
  }
  .text-align-center{
    text-align: center;
  }
  .i-font{
    font-size: 2em;
  }
  .weekend-font{
    font-size: .6em;
  }
</style>
  