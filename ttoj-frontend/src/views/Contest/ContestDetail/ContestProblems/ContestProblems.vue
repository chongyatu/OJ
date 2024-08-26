<template>
  <div id="contestProblemsWrapper"
       style="position:relative;
              padding: 20px;
             "
  >
    <el-row>
      <el-col :span="16">
        <el-card class="box-card" >
          <el-table :data="problems"
                    stripe
                    style="width: 100%"
                    v-loading="spinShow"
          >
            <el-table-column
                prop="problemDisplayId"
                label="#"
                width="100px"
                align="center"/>
            <el-table-column
                prop="problemName"
                label="题目"
                class-name="problemNameColumn"
                align="center"
            >
              <template slot-scope="scope">
                <div style="display: flex;">
                  <el-button type="text" @click="openProblemTab(scope.row)">
                    {{ scope.row.problemName }}
                  </el-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column
                prop="acRate"
                label="通过率"
                width="100px"
                align="center"/>
            <el-table-column prop="alreadyPassed" label="状态" width="100px" align="center">
              <template slot-scope="scope">
                <i v-if="scope.row.alreadyPassed" style="color:#2DB55D" class="el-icon-check"></i>
                <i v-else class="el-icon-minus"></i>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8" style="padding-left: 40px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>比赛资料</span>
          </div>
          <el-link type="primary" disabled>比赛题解</el-link>
          <br>
          <el-link type="primary" disabled>比赛讨论</el-link>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import {getProblemsByContestId} from "@/api/oj/contestProblemApi";

export default {
  name: "contestProblems",
  data() {
    return {
      contestId: 0,
      spinShow: false,
      problems: [],
    }
  },
  created() {
    this.contestId = this.$route.params.contestId
    this.openProblemDrawer()
  },
  methods: {
    openProblemTab(row) {
      this.$emit('openProblemTab', row.problemDisplayId)
    },
    openProblemDrawer() {
      this.spinShow = true
      console.log(this.contestId);
      getProblemsByContestId(this.contestId).then(res => {
        this.spinShow = false
        if (res.success) {
          let problems = res.data.problems
          for (let problem of problems) {
            problem.acRate = problem.acNum + '/' + problem.submitNum
          }
          this.problems = problems
        }
        this.spinShow = false
      })
    },
    refresh() {
      this.openProblemDrawer()
    },
  }
}
</script>

<style scoped>
@media screen and (min-width: 920px) {
  #contestProblemsWrapper {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
