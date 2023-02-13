<template>
  <div id="contestMyWrapper"
       style="position:relative;
             "
       v-if="auth"
  >
    <el-table
        :data="mySubmissions"
        stripe
        :default-sort="{prop: 'submitTime', order: 'descending'}"
        style=""
        v-loading="spinShow"
    >
      <el-table-column
          prop="username"
          label="用户名"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="problemDisplayId"
          label="题号"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="result"
          label="运行结果"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="timeUse"
          label="运行时间(ms)"
          width="130px"
          sortable
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="memoryUse"
          label="使用内存(MB)"
          width="140px"
          sortable
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="codeLength"
          label="代码长度(B)"
          width="140px"
          sortable
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="language"
          label="使用语言"
          width="100px"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="submitTime"
          label="提交时间"
          width="160px"
          sortable
          align="center"
      >
      </el-table-column>
    </el-table>
    <div class="block" style="padding: 10px 10px 10px 20px;
                             "
    >
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
  <div v-else>
    请登录后查看
  </div>
</template>

<script>

import {getContestSubmissionByContestId} from "@/api/oj/contestSubmissionApi";
import {mapGetters} from "vuex";

export default {
  name: "contestMy",
  data() {
    return {
      contestId: 0,
      spinShow: false,
      mySubmissions: [],
      condition: {
        currentPage: 1, pageSize: 20, total: 0,
      }
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'username',
      'auth',
      'user',
      'showManageBtn'
    ]),
  },
  created() {
    this.contestId = this.$route.params.contestId
    this.getContestMySubmissions()
  },
  methods: {
    getContestMySubmissions() {
      this.condition.contestId = this.contestId
      this.condition.userId = this.userId
      this.spinShow = true
      getContestSubmissionByContestId(this.condition).then(res => {
        this.spinShow = false
        if (res.success) {
          this.condition.total = parseInt(res.data.total)
          this.mySubmissions = res.data.contestSubmissions
        } else {
          this.errorNotify(res.message)
          this.$router.push({name: 'contestOverview'})
        }
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getContestMySubmissions()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getContestMySubmissions()
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    refresh() {
      this.getContestMySubmissions()
    },
  },

}
</script>

<style scoped>
@media screen and (min-width: 920px) {
  #contestMyWrapper {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
