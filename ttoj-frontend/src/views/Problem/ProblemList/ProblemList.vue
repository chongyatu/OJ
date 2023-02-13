<template>
  <div id="problemListWrapper"
       style="width: 100%;
              display: flex;
              justify-content: space-between
             "
  >
    <el-row style="width: 100%;margin-top: 20px;padding: 0 20px">
      <el-col :span="16"
              style="flex: 1 1 auto;"
      >
        <el-input v-model="condition.name"
                  placeholder="输入题目名字"
                  clearable
                  style="width: 30%"
                  size="small"
                  suffix-icon="el-icon-search"
                  @keyup.enter.native="getProblemsByCondition"
        >
        </el-input>
        <div style="position: relative;
                    margin-top: 20px;
                   "
        >
          <el-table :data="problems" stripe
                    style=""
                    @row-click="goToDetail"
                    v-loading="showSpin"
          >
            <el-table-column prop="alreadyPassed" label="状态" width="60px" align="center">
              <template slot-scope="scope">
                <i v-if="scope.row.alreadyPassed" style="color:#2DB55D" class="el-icon-check"></i>
                <i v-else class="el-icon-minus"></i>
              </template>
            </el-table-column>
            <el-table-column prop="displayId" label="#" width="80px" align="center"/>
            <el-table-column prop="name" label="题目"/>
            <el-table-column prop="level" label="难度" width="90px" align="center">
              <template slot-scope="scope">
                <span :style="{color: $config.levelColor[scope.row.level]}">{{ scope.row.level }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="acRate" label="通过率" width="90px" align="center"/>
          </el-table>
        </div>
        <div class="block" style="margin: 10px 0 0 0;overflow: auto">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :currentPage="condition.currentPage"
              :page-sizes="[30, 50]"
              :page-size="condition.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="condition.total"
              background
          >
          </el-pagination>
        </div>
      </el-col>
      <el-col :span="8" style="padding-left: 40px"
      >
        <CommonAside/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CommonAside from "@/views/Aside/CommonAside";
import {getProblemsByCondition} from "@/api/oj/problemApi";

export default {
  name: "ProblemList",
  components: {
    CommonAside,
  },
  data() {
    return {
      problems: [],
      condition: {
        currentPage: 1,
        total: 0,
        name: '',
        pageSize: 30,
      },
      showSpin: true,
    }
  },
  created() {
    this.getProblemsByCondition()
  },
  methods: {
    goToDetail(problem) {
      console.log(problem);
      this.$router.push('/problems/' + problem.id)
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getProblemsByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getProblemsByCondition()
    },
    getProblemsByCondition() {
      this.showSpin = true
      getProblemsByCondition(this.condition).then(res => {
        if (res.success) {
          this.problems = res.data.problems
          for (let i = 0; i < this.problems.length; i++) {
            if (this.problems[i].submitNum === 0) {
              this.problems[i].acRate = '0.00%'
            } else {
              this.problems[i].acRate = (this.problems[i].acNum * 100 / this.problems[i].submitNum).toFixed(1) + '%'
            }
          }
          this.condition.total = parseInt(res.data.total)
        } else {
          this.errorNotify(res.message)
        }
        this.showSpin = false
      })
    },
  }
}
</script>

<style scoped>
@media screen and (min-width: 920px) {
  #problemListWrapper {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
