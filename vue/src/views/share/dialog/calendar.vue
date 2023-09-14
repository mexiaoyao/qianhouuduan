<template>
  <div>
      <el-dialog title="日历设置" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="920px">
        <el-calendar v-model="value">
          <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
          <template slot="dateCell" slot-scope="{date, data}">
            <el-row :class="data.isSelected ? 'is-selected' : ''">
              {{ data.day.split('-').slice(1).join('-') }}  <i v-if="data.isSelected" class="el-icon-location-outline"></i>
            </el-row>
            <el-row class="default-date text-align-center" title="股票日" v-if="isWork(date)"><i class="el-icon-loading i-font" @click="dateShow"></i></el-row>
            <el-row class="green-date text-align-center" title="周末" v-if="isWeekend(date)"><i class="el-icon-remove-outline i-font"></i></el-row>
            <el-row class="weekend-font green-date text-align-center" title="周末" v-if="isWeekend(date)">周末</el-row>
          </template>
        </el-calendar>
      </el-dialog>
      <JieriModal :visible.sync="holidayVisible" @cancel="holidayVisible=false"></JieriModal>
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
        }
      },
      watch:{
        visible(newVal) {
            this.isShow = newVal;
            if (newVal)  this.form = this.row;
        }
      },
      methods: {
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

        dateShow(){
          this.holidayVisible = true;
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
  