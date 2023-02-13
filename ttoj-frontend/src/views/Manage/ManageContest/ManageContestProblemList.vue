<template>
  <div style="margin-top: 10px"
  >
    <div style="position: relative;flex: 1 1 auto;overflow: auto">
      <el-table
          :data="problems"
          style=""
          v-loading="showSpin"
      >
        <el-table-column
            fixed
            prop="problemDisplayId"
            label="显示ID"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="problemName"
            label="名字"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="visible"
            label="比赛中是否可见"
            align="center"
        >
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.visible"
                @change="updateContestProblemVisibility(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            align="center"
            min-width="130px"
        >
          <template slot-scope="scope">
            <el-button @click="editContestProblemDisplayId(scope.row)" size="mini"
                       icon="el-icon-edit" type="info" plain
            >
            </el-button>
            <el-divider direction="vertical" style="height: 100%"/>
            <el-popconfirm
                title="确定删除吗？"
                @confirm="deleteProblem(scope.row)"
            >
              <el-button
                  slot="reference"
                  size="mini" icon="el-icon-delete" type="danger" plain
              >
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px;
               "
    >
      <el-button plain icon="el-icon-circle-plus-outline" size="small" @click="openArchiveProblems">添加题目</el-button>
      <div class="block" style="margin: 10px 10px 10px 20px">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :currentPage="condition.currentPage"
            :page-sizes="[10, 20]"
            :page-size="condition.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            background
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
        title="请从题库中选择题目"
        :visible.sync="dialogVisible"
        width="80%"
    >
      <el-input
          placeholder="请输入题目名字"
          v-model="searchCondition.problemName"
          clearable
          @keyup.enter.native="searchProblemsByCondition"
          style="width: 45%"
      >
      </el-input>
      <el-divider direction="vertical" style="height: 100%"/>
      <el-input
          placeholder="请输入作者昵称"
          v-model="searchCondition.authorName"
          clearable
          @keyup.enter.native="searchProblemsByCondition"
          style="width: 45%"
      >
      </el-input>
      <div style="">
        <el-table
            :data="archiveProblems"
            style=""
            v-loading="searchShowSpin"
        >
          <el-table-column
              fixed
              prop="displayId"
              label="显示ID"
              align="center"
          ></el-table-column>
          <el-table-column
              prop="name"
              label="名字"
              align="center"
          ></el-table-column>
          <el-table-column
              prop="authorName"
              label="作者"
              align="center"
          ></el-table-column>
          <el-table-column
              prop="visible"
              label="题库中是否可见"
              align="center"
          >
            <template slot-scope="scope">
              <el-switch
                  v-model="scope.row.visible"
                  @change="updateProblemVisibility(scope.row)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column
              fixed="right"
              label="操作"
              align="center"
              min-width="130px"
          >
            <template slot-scope="scope">
              <el-button @click="addContestProblem(scope.row)" size="mini"
                         icon="el-icon-folder-add" type="info" plain
              >
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="block" style="margin: 10px 10px 10px 20px">
          <el-pagination
              @size-change="handleSearchSizeChange"
              @current-change="handleSearchCurrentChange"
              :currentPage="searchCondition.currentPage"
              :page-sizes="[10, 20]"
              :page-size="searchCondition.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              background
          >
          </el-pagination>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="请输入题目在比赛中显示ID"
        :visible.sync="displayIdEditorVisible"
        width="30%"
    >
      <el-input v-model="displayIdInput" placeholder="请输入题目在比赛中显示ID"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="displayIdEditorVisible = false">取 消</el-button>
        <el-button type="primary" @click="closeDisplayIdEditor">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addContestProblemById, updateContestProblem,
  deleteContestProblemByProblemId,
  getContestProblemByCondition, getProblemsFromArchiveByCondition
} from "@/api/manage/ManageContestProblemApi";
import {updateProblemVisibility} from "@/api/manage/ManageProblemApi";

export default {
  name: "ManageContestProblemList",
  data() {
    return {
      problems: [],
      condition: {
        currentPage: 1,
        pageSize: 10,
        contestId: this.$route.params.contestId
      },
      total: 0,
      searchCondition: {
        currentPage: 1,
        authorName: '',
        problemName: '',
        pageSize: 10,
        contestId: this.$route.params.contestId
      },
      showSpin: true,
      dialogVisible: false,
      searchProblemName: '',
      archiveProblems: [],
      searchShowSpin: false,
      displayIdEditorVisible: false,
      displayIdInput: '',
      currentEditorProblemId: -1,
      doAdd: false,
      currentProblem: {}
    }
  },
  created() {
    this.getContestProblemsByCondition()
  },
  methods: {
    closeDisplayIdEditor() {
      if(this.doAdd === true){
        addContestProblemById({
          contestId: this.$route.params.contestId,
          problemId: this.currentProblem.id,
          problemName: this.currentProblem.name,
          authorName: this.currentProblem.authorName,
          problemDisplayId: this.displayIdInput
        }).then(res => {
          if (res.success) {
            this.successNotify(res.message)
            this.getContestProblemsByCondition()
          } else {
            this.errorNotify(res.message)
          }
        })
      }else{
        updateContestProblem({
          contestId: this.$route.params.contestId,
          problemId: this.currentProblem.problemId,
          problemDisplayId: this.displayIdInput
        }).then(res => {
          if (res.success) {
            this.successNotify(res.message)
            this.getContestProblemsByCondition()
          } else {
            this.errorNotify(res.message)
          }
        })
      }
      this.displayIdEditorVisible = false
      this.displayIdInput = ''
    },
    updateProblemVisibility(problem) {
      let problemId = problem.id
      let visible = problem.visible
      updateProblemVisibility(problemId, visible).then(res => {
        this.notify(res.success, res.message)
      })
    },
    searchProblemsByCondition() {
      this.searchShowSpin = true
      getProblemsFromArchiveByCondition(this.searchCondition).then(res => {
        this.searchShowSpin = false
        if (res.success) {
          this.archiveProblems = res.data.problems
          this.total = parseInt(res.data.total)
        } else {
          this.errorNotify(res.message)
        }
      })
    },
    openArchiveProblems() {
      this.dialogVisible = true
      this.searchProblemsByCondition()
    },
    addContestProblem(row) {
      this.currentProblem = row
      this.doAdd = true
      this.displayIdEditorVisible = true
    },
    updateContestProblemVisibility(row) {
      let problemId = row.problemId
      let visible = row.visible
      let contestId = this.$route.params.contestId
      updateContestProblem({contestId, problemId, visible}).then(res => {
        this.notify(res.success, res.message)
      })
    },
    getContestProblemsByCondition() {
      this.showSpin = true
      getContestProblemByCondition(this.condition).then(res => {
        this.showSpin = false
        if (res.success) {
          this.problems = res.data.contestProblems
          this.total = parseInt(res.data.total)
        } else {
          this.errorNotify(res.message)
        }
      })
    },
    handleSearchSizeChange(pageSize) {
      this.searchCondition.pageSize = pageSize
      this.searchProblemsByCondition()
    },
    handleSearchCurrentChange(currentPage) {
      this.searchCondition.currentPage = currentPage
      this.searchProblemsByCondition()
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getContestProblemsByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getContestProblemsByCondition()
    },
    editContestProblemDisplayId(row) {
      this.displayIdInput = row.problemDisplayId
      this.currentProblem = row
      this.doAdd = false
      this.displayIdEditorVisible = true
    },
    deleteProblem(row) {
      deleteContestProblemByProblemId({
        contestId: this.$route.params.contestId,
        problemId: row.problemId
      }).then(res => {
        if (res.success) {
          this.successNotify(res.message)
          this.getContestProblemsByCondition()
        } else {
          this.errorNotify(res.message)
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
