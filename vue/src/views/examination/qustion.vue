<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :form="form" @submit="submitSearch" :inline="true">
        <el-form-item label="试卷种类">
            <el-cascader
                :allowClear="true"
                :fieldNames="{children:'children', label:'dictName', value: 'id' }"
                :options="questionTaskList"
                :placeholder="'请选择试卷种类'"
                @change="dictTaskChange"
                change-on-select
                v-model="form.dictTaskId"
            />
        </el-form-item>
        <el-form-item label="所属年级">
            <el-cascader
                :allowClear="true"
                :fieldNames="{children:'children', label:'dictName', value: 'id' }"
                :options="questionGradeList"
                :placeholder="'请选择所属年级'"
                @change="dictGradeChange"
                change-on-select
                v-model="form.dictGradeId"
            />
        </el-form-item>
        <el-form-item label="题目来源">
            <el-cascader
                :allowClear="true"
                :fieldNames="{children:'children', label:'dictName', value: 'id' }"
                :options="questionSourceList"
                :placeholder="'请选择题目来源'"
                @change="dictSourceChange"
                change-on-select
                v-model="form.dictSourceId"
            />
        </el-form-item>
        <el-form-item label="题目类型">
            <el-cascader
                :allowClear="true"
                :fieldNames="{children:'children', label:'dictName', value: 'id' }"
                :options="questionTypeList"
                :placeholder="'请选择题目类型'"
                @change="dictTypeChange"
                change-on-select
                v-model="form.dictTypeId"
            />
        </el-form-item>

        <el-form-item label="考题种类">
            <el-select :allowClear="true" :style="{width:'160px'}" v-model.trim="form.type">
                <el-select-option
                    :key="index"
                    :value="item.code"
                    v-for="(item,index) in typeList"
                >{{item.codeName}}</el-select-option>
            </el-select>
        </el-form-item>
        <el-form-item label="问题">
            <el-input allowClear placeholder="请输入问题" v-model.trim="form.question"></el-input>
        </el-form-item>
        <el-form-item label="创建人">
            <el-input allowClear placeholder="请输入创建人" v-model.trim="form.createName"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button :disabled="isLoading" html-type="submit" type="primary">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading="listLoading"  border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="content" label="文章" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="createTime" label="创建时间" width="170"/>
      <el-table-column align="center" prop="updateTime" label="最近修改时间" width="170"/>
      <el-table-column align="center" label="管理" width="200" >
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)"  v-permission="'article:update'">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempArticle" label-position="left" label-width="60px"
               style='width: 500px; margin-left:50px;'>
        <el-form-item label="文章">
          <el-input type="textarea" style="width:100%" show-word-limit v-model="tempArticle.content"  maxlength="100">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus==='create'" type="success" @click="createArticle">创 建</el-button>
        <el-button type="primary" v-else @click="updateArticle">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        isLoading: false,
        form: {
            dictTaskId: [],
            dictTaskName: [],

            dictGradeId: [],
            dictGradeName: [],

            dictSourceId: [],
            dictSourceName: [],

            dictTypeId: [],
            dictTypeName: [],

            type: 0,
            intro: "",
            question: "",
            answers: "",
            answerRight: "",
            createName: "",
            remarks: "",
        },
        createTime: [null, null],
        updateTime: [null, null],






        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          name: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建文章'
        },
        tempArticle: {
          id: "",
          content: ""
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        if (!this.hasPerm('article:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/article/listArticle",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      handleFilter() {
        //改变了查询条件,从第一页开始查询
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempArticle.content = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempArticle.id = this.list[$index].id;
        this.tempArticle.content = this.list[$index].content;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createArticle() {
        //保存新文章
        this.api({
          url: "/article/addArticle",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateArticle() {
        //修改文章
        this.api({
          url: "/article/updateArticle",
          method: "post",
          data: this.tempArticle
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
    }
  }
</script>
