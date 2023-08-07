<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item label="试卷种类">
          <el-select v-model="parmes.type" placeholder="请选择试卷种类" @change="getList">
            <el-option label="试卷种类" :value="1"></el-option>
            <el-option label="所属年级" :value="2"></el-option>
            <el-option label="考题来源" :value="3"></el-option>
            <el-option label="考题类型" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-permission="'dict:add'">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="list"
      :row-class-name ="rowClaName" 
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column align="left" prop="dictName" label="字典名称"></el-table-column>
      <el-table-column align="center" prop="createTime" label="创建时间" width="170"/>
      <el-table-column align="center" prop="updateTime" label="最近修改时间" width="170"/>
      <el-table-column align="center" prop="createName" label="创建者" width="100"/>
      <el-table-column align="center" prop="updateName" label="修改者" width="100"/>
      <el-table-column align="center" label="操作" width="200" >
        <template slot-scope="scope">
          <el-button type="success" size="mini" icon="el-icon-plus" @click="showCreate(scope.row)"  v-permission="'dict:add'" title="添加" />
          <el-button type="warning" size="mini" icon="el-icon-edit" @click="showUpdate(scope.row)"  v-permission="'dict:update'" title="编辑" />
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeUser(scope.row)"  v-permission="'dict:delete'" title="删除" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="form" label-position="left" label-width="80px" style='width: 500px; margin-left:50px;'>
        <el-form-item label="字典名称">
          <el-input type="text" style="width:100%" v-model="form.dictName"  maxlength="100">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus==='create'" type="success" @click="createForm">创建</el-button>
        <el-button type="primary" v-else @click="updateForm">修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getTreeParents } from '../../utils/index';
  export default {
    data() {
      return {
        parmes:{
          type:1
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
          dictName: ""
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      rowClaName({row, rowIndex}){ // eslint-disable-line no-unused-vars
        if(row.parentId){
            //组内元素
            const parents_id = row.parentId;
            const p_index = this.reArrIndex(this.list, 'id', parents_id); //找到父元素在组里的index
            if(p_index !== -1){
                  const [...p_arr] = this.list[p_index]['children'];
                  const s_index = this.reArrIndex(p_arr, 'id', row.id);
                  row.index = s_index;
              }
        }else{
              const t_index = this.reArrIndex(this.list, 'id', row.id); //找到父元素在组里的index
              row.index = t_index;
        }
      },
      //封装二维数组根据某个索引对象中的某个属性，返回当前索引
      reArrIndex(arr, attr, attrval){
          let index = arr.findIndex((item) => {
              return item[attr] == attrval;
          });
          return index;
      },
      getList() {
        //查询列表
        if (!this.hasPerm('dict:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/dict/list",
          method: "get",
          params: this.parmes
        }).then(data => {
          this.listLoading = false;
          this.list = getTreeParents(data.list);
        })
      },

      getIndex($index) {
        //表格序号
        return $index + 1
      },
      getType(type) {
        switch(type){
          case 1:
            return "试卷种类";  //例如，语文，数学，英语等等
          case 2:
            return "所属年级";  //例如，一年级，大一等
          case 3:
            return "考题来源";  //例如，苏教版，某年考题等
          case 4:
            return "考题类型";  //例如，应用题，字词积累等
          default:
            return "";
        }
      },
      showCreate(row) {
        //显示新增对话框
        this.form.dictName = "";
        this.form.parentId = row.id || 0;
        this.form.type = row.type || this.parmes.type;
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate(row) {
        //显示修改对话框
        this.form.id = row.id;
        this.form.dictName = row.dictName || "";
        this.form.parentId = row.parentId || 0;
        this.form.type = row.type;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createForm() {
        //保存新文章
        this.api({
          url: "/dict/add",
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
          url: "/dict/update",
          method: "post",
          data: this.form
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      removeUser(row) {
        let _vue = this;
        this.$confirm('确定删除"'+row.dictName+'"?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let data = {id:row.id};
          _vue.api({
            url: "/dict/delete",
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
