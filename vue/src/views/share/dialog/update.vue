<template>
      <el-dialog :title="''==form.id?'添加':'编辑'" :visible.sync="visible" @cancel="cancelClick" destroy-on-close width="420px">
        <el-form class="small-space" :model="form" :rules="rules" ref="ruleForm" label-position="right" label-width="120px">
          <el-form-item label="指数">
            <el-select v-model="form.indexType" style="width:100%">
              <el-option label="沪指SH" :value="1"></el-option>
              <el-option label="深指SZ" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="股票代码">
            <el-input type="text" style="width:100%" v-model="form.codeNumber"  maxlength="6"></el-input>
          </el-form-item>
          <el-form-item label="股票名称">
            <el-input type="text" style="width:100%" v-model="form.sharesName"  maxlength="100"></el-input>
          </el-form-item>
          <el-form-item label="股票别名">
            <el-input type="text" style="width:100%" v-model="form.sharesAlise"  maxlength="100"></el-input>
          </el-form-item>
          <el-form-item label="股票总股数">
            <el-input-number style="width:100%" v-model="form.sharesTotalNumber"></el-input-number>
          </el-form-item>
          <el-form-item label="可流动股票股数">
            <el-input-number style="width:100%" v-model="form.sharesAllowTotalNumber"></el-input-number>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="text" style="width:100%" v-model="form.remarks"  maxlength="100">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="cancelClick">取 消</el-button>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        </div>
      </el-dialog>
  </template>
  <script>
    import { gpCodeCheck, zhengshuCheck } from "@/utils/decorator";
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
          form: {
            id: null,
            indexType:1,
            codeNumber:null,
            sharesName:null,
            sharesAlise:null,
            sharesTotalNumber:null,
            sharesAllowTotalNumber:null,
            remarks:null
          },
          rules: {
                indexType: [{ required: true, message: "指数类型不可为空", trigger: "change" }],
                codeNumber: [
                    { required: true, message: "股票代码不可为空", trigger: "blur" },
                    {
                        validator: gpCodeCheck.bind(this),
                        message: "请输入正确的股票代码",
                        trigger: "blur",
                    },
                ],
                sharesName: [{ required: true, message: "股票名称不可为空", trigger: "blur" }],
                sharesAlise: [{ required: true, message: "股票别名不可为空", trigger: "blur" }],
                sharesTotalNumber: [
                    { required: true, message: "股票总数不可为空", trigger: "blur" },
                    {
                        validator: zhengshuCheck.bind(this),
                        message: "请输入正确的股票总数",
                        trigger: "blur",
                    },
                ],
                sharesAllowTotalNumber: [
                    { required: true, message: "可流动股票总数不可为空", trigger: "blur" },
                    {
                        validator: zhengshuCheck.bind(this),
                        message: "请输入正确的可流动股票总数",
                        trigger: "blur",
                    },
                ],
                remarks: [{ required: true, message: "备注不可为空", trigger: "blur" }],
            },
        }
      },
      watch:{
        visible(newVal, oldVal) {
            if (newVal && null!=this.row) {
                Object.assign(this.form, this.row);
            }
        },
      },
      methods: {
        cancelClick() {
            this.$emit("cancel");
        },
        submitForm(formName) {
            const _this = this;
            debugger;
            _this.$refs[formName].validate((valid) => {
            if (valid) {
                if(""==_this.form.id){
                    _this.createForm(_this);
                }else{
                    _this.updateForm(_this);
                }
            } else {
                console.log('error submit!!');
                return false;
            }
            });
        },
        createForm(_this) {
          //保存新文章
          _this.api({
            url: "/intro/add",
            method: "post",
            data: _this.form
          }).then(() => {
            _this.$emit("ok");
          })
        },
        updateForm(_this) {
          //修改文章
          _this.api({
            url: "/intro/update",
            method: "post",
            data: _this.form
          }).then(() => {
            _this.$emit("ok");
          })
        }
      }
    }
  </script>
  