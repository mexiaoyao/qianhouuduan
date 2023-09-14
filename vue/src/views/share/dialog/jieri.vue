<template>
    <div>
        <el-dialog title="选择节假日" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="400px">
          <el-form :model="form" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="假日名称" prop="holiday">
              <el-select v-model="form.holiday" placeholder="请选择节日" clearable>
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="cancelClick">取消</el-button>
              <el-button type="primary" @click="submitForm">提交</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </div>
    </template>
    <script>
    import { holidayCheck } from "@/utils/decorator";
      export default {
        name:"jieri-dialog",
        props: {
          visible:{
              type:Boolean,
              default: false
          }
        },
        data() {
          return {
              isShow:false,
              options: [
                { value: 0, label: '工作日'},
                { value: 1, label: '元旦节'},
                { value: 2, label: '除夕'}, 
                { value: 3, label: '春节'}, 
                { value: 4, label: '清明节'},
                { value: 5, label: '劳动节'},
                { value: 6, label: '端午节'},
                { value: 7, label: '中秋节'},
                { value: 8, label: '国庆节'},
                { value: 9, label: '其它节日'}
              ],
              form:{
                holiday:0,
              },
              rules: {
                holiday: [
                  { required: true, message: '请选择节假日', trigger: 'change' },
                  { validator: holidayCheck.bind(this), trigger: 'change' }
                ]
              }
          }
        },
        watch:{
          visible(newVal) {
              this.isShow = newVal;
          }
        },
        methods: {
          cancelClick() {
              this.$emit("cancel");
          },
          submitForm() {
            if(this.$refs.ruleForm){
              this.$refs.ruleForm.validate((valid) => {
                if (valid) {
                  alert('submit!');
                } else {
                  console.log('error submit!!');
                  return false;
                }
              });
            }
          },
          dateShow(){
            this.isShow = true;
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
    