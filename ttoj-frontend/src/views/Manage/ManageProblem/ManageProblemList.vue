<template>
  <div style="height: 100%;
              display: flex;
              flex-direction: column;
           "
  >
    <div style="margin: 10px 20px">
      <el-input v-model="condition.authorName"
                placeholder="输入作者名字"
                clearable
                style="width: 30%"
                size="small"
      >
      </el-input>
      <el-divider direction="vertical" style="height: 100%"/>
      <el-input v-model="condition.name"
                placeholder="输入题目名字"
                clearable
                style="width: 30%"
                size="small"
      >
      </el-input>
      <el-button icon="el-icon-search"
                 size="small"
                 @click="getProblemsByCondition"
      >搜索
      </el-button>
    </div>
    <div style="position: relative;flex: 1 1 auto;overflow: auto"
         v-loading="showSpin"
    >
      <el-table
          :data="problems"
          style=""
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
            label="是否可见"
            align="center"
            width="80"
        >
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.visible"
                @change="changeProblemVisibility(scope.row)"
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
            <el-button @click="editProblem(scope.row)" size="mini"
                       icon="el-icon-edit" type="info" plain
            >
            </el-button>
            <el-tooltip class="item" effect="dark" content="下载题目测试文件" placement="top-start">
              <el-button @click="downloadTestCase(scope.row)" size="mini"
                         icon="el-icon-download"
                         type="primary" plain
              >
              </el-button>
            </el-tooltip>
            <!--
            1、在封装好的组件上使用，所以要加上.native才能click。
            2、prevent是用来阻止默认的事件。就相当于…event.preventDefault()，父组件想在子组件上监听自己的click的话，需要加上native修饰符。
            -->
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
    <div class="block" style="margin: 10px 10px 10px 20px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :currentPage="condition.currentPage"
          :page-sizes="[20, 30]"
          :page-size="condition.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="condition.total"
          background
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>

import {
  updateProblemVisibility,
  deleteProblem,
  getProblemsByCondition,
} from "@/api/manage/ManageProblemApi";
import {downloadTestCase} from "@/api/manage/ManageFileApi";

export default {
  name: "ManageProblemList",
  data() {
    return {
      problems: [],
      condition: {
        currentPage: 1,
        size: 3,
        total: 0,
        authorName: '',
        name: '',
        pageSize: 20,
      },
      showSpin: false,
    }
  },
  created() {
    this.getProblemsByCondition()
  },
  methods: {
    changeProblemVisibility(problem) {
      let problemId = problem.id
      let visible = problem.visible

      updateProblemVisibility(problemId, visible).then(res => {
      })
    },
    getProblemsByCondition() {
      this.showSpin = true
      getProblemsByCondition(this.condition).then(res => {
        this.showSpin = false
        if (res.success) {
          this.problems = res.data.problems
          this.condition.total = parseInt(res.data.total)
        }
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getProblemsByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getProblemsByCondition()
    },
    downloadTestCase({id}) {
      downloadTestCase(id)
    },
    editProblem(problem) {
      this.$router.push(`/manage/problems/${problem.id}`)
    },
    deleteProblem(row) {
      console.log(row);
      deleteProblem(row.id).then(res => {
        if (res.success) {
          this.successNotify(res.message)
          this.getProblemsByCondition()
        }
      })
    },
  }
}
</script>

<style scoped>
.el-button {
  margin-left: 3px;
}
</style>
