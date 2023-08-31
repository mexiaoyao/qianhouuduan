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
          <el-button type="primary" icon="plus" @click="getList" v-permission="'intro:list'">查询</el-button>
          <el-button type="primary" icon="plus" @click="showCreate" v-permission="'intro:add'">添加</el-button>
        </el-form-item>
        <el-form-item>
          <el-upload action="/api/intro/import" :headers="header" :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" :on-exceed="handleExceed">
            <el-button type="primary">批量上传</el-button>
          </el-upload>
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
          <el-button type="danger" size="mini" icon="el-icon-turn-off" @click="statusAction(scope.row,2)" v-if="scope.row.status==1" v-permission="'intro:status'" title="下架" />
          <el-button type="success" size="mini" icon="el-icon-open" @click="statusAction(scope.row,1)" v-if="scope.row.status==2" v-permission="'intro:status'" title="上架" />
          <el-button type="warning" size="mini" icon="el-icon-edit" @click="showUpdate(scope.row)" v-if="scope.row.status==2" v-permission="'intro:update'" title="编辑" />
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeUser(scope.row)" v-if="scope.row.status==2" v-permission="'intro:delete'" title="删除" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="420px">
      <el-form class="small-space" :model="form" label-position="right" label-width="120px">
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
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus==='create'" type="success" @click="createForm">创建</el-button>
        <el-button v-if="dialogStatus==='update'" type="success" @click="updateForm">编辑</el-button>
        <el-button type="primary" v-else @click="updateForm">修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        header: {
          token: localStorage.getItem("token"),
        },
        parmes:{
          indexType:0
        },
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画

        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '添加'
        },
        form: {
          id: "",
          indexType:1,
          codeNumber:"",
          sharesName:"",
          sharesAlise:"",
          sharesTotalNumber:0,
          sharesAllowTotalNumber:0,
          remarks:""
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },


      getList() {
        //查询列表
        if (!this.hasPerm('intro:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/intro/list",
          method: "post",
          data: this.parmes
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },

      getIndex($index) {
        //表格序号
        return $index + 1
      },
      showCreate(row) {
        //显示新增对话框
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate(row) {
        //显示修改对话框
        this.form = row;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createForm() {
        //保存新文章
        this.api({
          url: "/intro/add",
          method: "post",
          data: this.form
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateForm() {
        //修改文章
        this.api({
          url: "/intro/update",
          method: "post",
          data: this.form
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
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
