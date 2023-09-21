<template>
      <el-dialog title="获取状态" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="980px">
        <el-table :data="list" style="width: 100%;margin-bottom: 20px;" row-key="id" fit highlight-current-row border >
          <el-table-column fixed align="center" label="序号" width="80">
            <template slot-scope="scope">
              <span v-text="getIndex(scope.$index)"> </span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="dictName" label="状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status==1" class="color-67C23A">成功</span>
              <span v-else-if="scope.row.status==2" class="color-F56C6C">失败</span>
              <span v-else>其它</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="createTime" label="生成时间" width="170"/>
          <el-table-column align="center" prop="failNum" label="失败次数" width="170"/>
          <el-table-column align="center" prop="remarks" label="备注" width="200">
            <template slot-scope="scope">
              <p :title="scope.row.remarks" class="text-ellipsis">{{scope.row.remarks}}</p>
            </template>
          </el-table-column>
          <el-table-column fixed="right" align="center" label="操作" width="200" >
            <template slot-scope="scope">
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeStatus(scope.row)" v-permission="'intro:statusDelete'" title="删除" />
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
  </template>
  <script>
    export default {
      name:"update-list",
      props: {
        visible:{
            type:Boolean,
            default: false
        },
        sharesId: {
            type: String,
            default: "",
        },
      },
      data() {
        return {
            isShow: false,
            id: "",
            list: [],
            totalCount: 0
        }
      },
      watch:{
        visible(newVal) {
            this.isShow = newVal;
            if (newVal){
              this.id = this.sharesId;
              this.getList();
            } 
        }
      },
      methods: {
        cancelClick() {
            this.$emit("cancel");
        },
        getIndex($index) {
          //表格序号
          return $index + 1
        },
        getList() {
          //查询列表
          if (!this.hasPerm('intro:statusList')) {
            return
          }
          this.listLoading = true;
          this.api({url: "/intro/statusList", method: "post", data: {sharesId:this.id}}).then(data => {
            this.listLoading = false;
            this.list = data.list;
            this.totalCount = data.totalCount;
          })
        },
        removeStatus(row) {
          let _vue = this;
          this.$confirm('确定要删除这条更新记录么?', '提示', {
            confirmButtonText: '确定',
            showCancelButton: false,
            type: 'warning'
          }).then(() => {
            let data = {id:row.id};
            _vue.api({
              url: "/intro/statusDelete",
              method: "post",
              data: data
            }).then(() => {
              this.$message.success('删除成功')
              _vue.getList()
            }).catch(() => {
              _vue.$message.error("删除失败")
            })
          })
        },
      }
    }
  </script>
  