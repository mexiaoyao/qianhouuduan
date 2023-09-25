<template>
      <el-dialog title="API列表" :visible.sync="isShow" :before-close="cancelClick" @cancel="cancelClick" width="1024px">
        <el-row>
          <el-button type="primary" @click="addApi" v-permission="'intro:add'">添加</el-button>
        </el-row>
        <el-table :data="list" style="width: 100%;margin-bottom: 20px;" row-key="id" fit highlight-current-row border >
          <el-table-column fixed align="center" label="序号" width="80">
            <template slot-scope="scope">
              <span v-text="getIndex(scope.$index)"> </span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="name" label="API名称">
            <template slot-scope="scope">
              <p :title="scope.row.name" class="text-ellipsis">{{scope.row.name}}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="url" label="URL">
            <template slot-scope="scope">
              <p :title="scope.row.url" class="text-ellipsis">{{scope.row.url}}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="maxNumber" label="允许请求数"/>
          <el-table-column align="center" prop="spaceNumber" label="安全数"/>
          <el-table-column align="center" prop="remarks" label="备注">
            <template slot-scope="scope">
              <p :title="scope.row.remarks" class="text-ellipsis">{{scope.row.remarks}}</p>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="createTime" label="生成时间" width="170"/>
          <el-table-column fixed="right" align="center" label="操作" width="150">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="addApi(scope.row)" v-permission="'intro:statusDelete'" title="编辑" />
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeStatus(scope.row)" v-permission="'intro:statusDelete'" title="删除" />
            </template>
          </el-table-column>
        </el-table>
        <ApiUpdate :visible.sync="apiVisible" @cancel="apiVisible" @ok="okStatus" :row="item" ></ApiUpdate>
      </el-dialog>
  </template>
  <script>
    import ApiUpdate from "./apiUpdate.vue";
    export default {
      components: { ApiUpdate },
      name:"api-list",
      props: {
        visible:{
            type:Boolean,
            default: false
        }
      },
      data() {
        return {
            isShow: false,
            list: [],
            totalCount: 0,
            apiVisible: false,
            item:{}
        }
      },
      watch:{
        visible(newVal) {
            this.isShow = newVal;
            if (newVal){
              this.getList();
            } 
        }
      },
      methods: {
        addApi(row){
          this.apiVisible = true;
          this.item = row;
        },
        okStatus(){
          this.apiVisible = false;
        },
        cancelClick() {
            this.$emit("cancel");
        },
        getIndex($index) {
          //表格序号
          return $index + 1
        },
        getList() {
          //查询列表
          if (!this.hasPerm('intro:apiList')) {
            return
          }
          this.listLoading = true;
          this.api({url: "/intro/apiList", method: "post", data: {}}).then(data => {
            this.listLoading = false;
            this.list = data.list;
            this.totalCount = data.totalCount;
          })
        },
        removeStatus(row) {
          let _vue = this;
          this.$confirm('确定要删除这条API记录么?', '提示', {
            confirmButtonText: '确定',
            showCancelButton: false,
            type: 'warning'
          }).then(() => {
            let data = {id:row.id};
            _vue.api({
              url: "/intro/apiDelete",
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
  