<template>
      <el-dialog :title="''==form.id?'添加':'编辑'" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="420px">
        <el-form class="small-space" :model="form" :rules="rules" ref="ruleForm" label-position="right" label-width="120px">
          <el-form-item label="网站名称" prop="name">
            <el-input type="text" style="width:100%" v-model="form.name"  maxlength="100"></el-input>
          </el-form-item>
          <el-form-item label="请求URL" prop="url">
            <el-input type="text" style="width:100%" v-model="form.url"  maxlength="100"></el-input>
          </el-form-item>
          <el-form-item label="允许最大请求数" prop="maxNumber">
            <el-input-number style="width:100%" v-model="form.maxNumber"></el-input-number>
          </el-form-item>
          <el-form-item label="间隔请求数" prop="spaceNumber">
            <el-input-number style="width:100%" v-model="form.spaceNumber"></el-input-number>
          </el-form-item>
          <el-form-item label="备注" prop="remarks">
            <el-input type="text" style="width:100%" v-model="form.remarks"  maxlength="100">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="cancelClick">取 消</el-button>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </div>
      </el-dialog>
  </template>
  <script>
    export default {
      name:"update-dialog",
      props: {
        visible:{
            type:Boolean,
            default: false
        },
        row: {
            type: Object,
            default: () => {},
        },
      },
      data() {
        return {
            isShow:false,
            form: {}
        }
      },
      watch:{
        visible(newVal) {
            this.isShow = newVal;
            if (newVal)  this.form = this.row;
        }
      },
      computed:{
        rules(){
            return {
              name: [{ required: true, message: "网站名称", trigger: "blur" }],
              url: [{ required: true, message: "请求URL", trigger: "blur" }],
              maxNnumber: [{ required: true, message: "股票总数不可为空", trigger: "blur" }],
              spaceNumber: [{ required: true, message: "可流动股票总数不可为空", trigger: "blur" }],
              remarks: [{ required: false, message: "备注不可为空", trigger: "blur" }],
            };
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
                        if(""==this.form.id){
                            this.createForm(this);
                        }else{
                            this.updateForm(this);
                        }
                    }
                });
            }
        },
        createForm(_this) {
          //保存新文章
          _this.api({
            url: "/intro/apiAdd",
            method: "post",
            data: _this.form
          }).then(() => {
            _this.$emit("ok");
          })
        },
        updateForm(_this) {
          //修改文章
          _this.api({
            url: "/intro/apiUpdate",
            method: "post",
            data: _this.form
          }).then(() => {
            _this.$emit("ok");
          })
        },
      }
    }
  </script>
  