<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item label="股票指数">
          <el-select v-model="parmes.indexType" placeholder="请选择股票指数" @change="getList">
            <el-option label="全部" :value="0"></el-option>
            <el-option label="沪指SH" :value="1"></el-option>
            <el-option label="深指SZ" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="股票代码">
          <el-input allowClear placeholder="请输入股票代码" v-model.trim="parmes.codeNumber"></el-input>
        </el-form-item>
        <el-form-item label="股票名称">
          <el-input allowClear placeholder="请输入股票名称" v-model.trim="parmes.sharesName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList" v-permission="'intro:list'">查询</el-button>
          <el-button type="primary" @click="showCreate" v-permission="'intro:add'">添加</el-button>
          <el-button type="primary" @click="calendarShow" v-permission="'intro:add'">日历设置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" style="width: 100%;margin-bottom: 20px;" row-key="id" fit highlight-current-row border >
      <el-table-column fixed align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column fixed align="center" prop="dictName" label="指数">
        <template slot-scope="scope">
          <span v-if="scope.row.indexType==1">沪指(SH)</span>
          <span v-else-if="scope.row.indexType==2">深指(SZ)</span>
          <span v-else>其它</span>
        </template>
      </el-table-column>
      <el-table-column fixed align="center" prop="codeNumber" label="股票代码" width="170"/>
      <el-table-column fixed align="center" prop="sharesName" label="股票名称" width="170"/>
      <el-table-column align="center" prop="sharesAlise" label="股票别名" width="220">
        <template slot-scope="scope">
          <p :title="scope.row.sharesAlise" class="text-ellipsis">{{scope.row.sharesAlise}}</p>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="sharesTotalNumber" label="股票总股数" width="170"/>
      <el-table-column align="center" prop="sharesAllowTotalNumber" label="可流动股票股数" width="170"/>
      <el-table-column align="center" prop="dictName" label="状态">
        <template slot-scope="scope">
          <p v-if="scope.row.status==1" class="text-color-white text-p-t-5 text-p-b-5 bg-67C23A">上线</p>
          <p v-else-if="scope.row.status==2" class="text-color-white text-p-t-5 text-p-b-5 bg-F56C6C">下线</p>
          <p v-else>其它</p>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="loadTime" label="最后更新" width="170"/>
      <el-table-column align="center" prop="createTime" label="创建时间" width="170"/>
      <el-table-column align="center" prop="updateTime" label="最近修改时间" width="170"/>
      <el-table-column align="center" prop="remarks" label="备注" width="200">
        <template slot-scope="scope">
          <p :title="scope.row.remarks" class="text-ellipsis">{{scope.row.remarks}}</p>
        </template>
      </el-table-column>
      <el-table-column fixed="right" align="center" label="操作" width="200" >
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-view" @click="detailAction(scope.row)" v-if="scope.row.status==1" v-permission="'intro:status'" title="详情" />
          <el-button type="danger" size="mini" icon="el-icon-turn-off" @click="statusAction(scope.row,2)" v-if="scope.row.status==1" v-permission="'intro:status'" title="下架" />
          <el-button type="success" size="mini" icon="el-icon-open" @click="statusAction(scope.row,1)" v-if="scope.row.status==2" v-permission="'intro:status'" title="上架" />
          <el-button type="warning" size="mini" icon="el-icon-edit" @click="showUpdate(scope.row)" v-if="scope.row.status==2" v-permission="'intro:update'" title="编辑" />
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeUser(scope.row)" v-if="scope.row.status==2" v-permission="'intro:delete'" title="删除" />
        </template>
      </el-table-column>
    </el-table>

    <AddModal :visible.sync="updateVisible" @cancel="cancelAction" @ok="okDialog" :row="itemObj" ></AddModal>
    <CalendarModal :visible.sync="calendarVisible" @cancel="calendarVisible=false"></CalendarModal>
    <StatusModal :visible.sync="statusVisible" @cancel="statusCancel" @ok="okStatus" :row="itemObj" ></StatusModal>
  </div>
</template>
<script>
  import AddModal from "./dialog/update.vue";
  import CalendarModal from "./dialog/calendar.vue";
  import StatusModal from "./dialog/updateList.vue";
  export default {
    components: { AddModal, CalendarModal, StatusModal },
    data() {
      return {
        parmes:{
          indexType:0
        },
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画

        updateVisible: false,
        itemObj: {},

        dialogDetaiVisible: false,
        calendarVisible: false, //日历弹框
        statusVisible: false, //更新详情
      }
    },
    created() {
      this.getList();
    },
    methods: {
      //日历弹框
      calendarShow(){
        this.calendarVisible = true;
      },
      getList() {
        //查询列表
        if (!this.hasPerm('intro:list')) {
          return
        }
        this.listLoading = true;
        this.api({url: "/intro/list", method: "post", data: this.parmes}).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },

      getIndex($index) {
        //表格序号
        return $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.itemObj = {
          id: "",
          indexType:1,
          codeNumber:"",
          sharesName:"",
          sharesAlise:"",
          sharesTotalNumber:0,
          sharesAllowTotalNumber:0,
          remarks:""
        };
        this.updateVisible = true;
      },
      showUpdate(row) {
        //显示修改对话框
        Object.assign(this.itemObj, row)
        this.updateVisible = true;
      },
      cancelAction() {
        //显示修改对话框
        this.updateVisible = false;
      },
      detailAction(row){
        Object.assign(this.itemObj, row);
        this.statusVisible = true;
      },
      statusCancel(){
        this.statusVisible = false;
      },
      okStatus(){
        this.updateVisible = false;
        this.getList();
      },
      okDialog(){
        this.statusVisible = false;
        this.getList();
      },
      statusAction(row, status){
          let _vue = this;
          let msg = "上架";
          if(status==2){
            msg = "下架";
          }
          this.$confirm('确定要'+msg+'《'+row.sharesName+'》?', '提示', {
            confirmButtonText: '确定',
            showCancelButton: false,
            type: 'warning'
          }).then(() => {
            let data = {id:row.id,status:status};
            _vue.api({
              url: "/intro/status",
              method: "post",
              data: data
            }).then(() => {
              this.$message.success('操作成功')
              _vue.getList()
            }).catch(() => {
              _vue.$message.error("操作失败")
            })
          })
      },
      removeUser(row) {
        let _vue = this;
        this.$confirm('确定要删除《'+row.sharesName+'》?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let data = {id:row.id};
          _vue.api({
            url: "/intro/delete",
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
