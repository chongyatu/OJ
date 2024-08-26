<template>
  <div style="position:relative;
             "
  >
    <el-table :data="userRanks"
              stripe
              :cell-class-name="addClass"
              v-loading="spinShow"
    >
      <el-table-column
          type="index"
          :index="indexMethod"
          label="排名"
          width="100px"
          align="center"
      />
      <el-table-column
          prop="username"
          label="参赛者"
          align="left"
      />
      <el-table-column
          prop="acNum"
          label="通过"
          width="60px"
          align="center"
      />
      <el-table-column
          prop="totalTime"
          label="罚时"
          width="100px"
          align="center"
      />
      <el-table-column v-for="item in contestProblems"
                       :key="item.problemDisplayId"
                       :label="item.problemDisplayId"
                       width="100px"
                       align="center"
      >
        <template slot="header" slot-scope="scope">
          <div style="display: flex;flex-direction: column;align-items: center">
              <span>{{item.problemDisplayId}}</span>
              <span><span style="color: #32CA99">{{item.acNum}}</span>/{{item.submitNum}}</span>
          </div>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.firstAcTime[item.problemDisplayId] != null">{{ scope.row.firstAcTime[item.problemDisplayId] }}</span>
          <br v-if="scope.row.firstAcTime[item.problemDisplayId] != null && scope.row.waTimes[item.problemDisplayId] > 0">
          <span v-if="scope.row.waTimes[item.problemDisplayId] > 0">(-{{ scope.row.waTimes[item.problemDisplayId] }})</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="padding: 10px 10px 10px 20px;
                              overflow: auto;
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
</template>

<script>
import {getContestRankingsByCondition} from "@/api/oj/contestApi";

export default {
  name: "contestRankings",
  props: {
    propTabName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      contestId: 0,
      contestProblems: [],
      userRanks: [],
      tabName: '',
      spinShow: true,
      condition:{
        contestId: '',
        currentPage: 1, pageSize: 20
      }
    }
  },
  created() {
    this.contestId = this.$route.params.contestId
    this.tabName = this.$props.propTabName
    this.getRankListByCondition()
  },
  methods: {
    refresh() {
      this.getRankListByCondition()
    },
    addClass(row, column, rowIndex, columnIndex) {
      let rowIdx = row.rowIndex
      let columnIdx = row.columnIndex
      // console.log(row.row, row.column);
      if (columnIdx >= 4) {
        if (row.row.isFirstBlood[row.column.label]) {
          return 'firstBloodCell'
        }
        if (row.row.firstAcTime[row.column.label] != null) {
          return 'acceptedCell'
        }
        if (row.row.waTimes[row.column.label] > 0) {
          return 'notAcceptedCell'
        }
      }
    },
    indexMethod(index) {
      return index + 1
    },
    getRankListByCondition() {
      this.spinShow = true
      this.condition.contestId = this.contestId
      getContestRankingsByCondition(this.condition).then(res => {
        this.spinShow = false
        if (res.success){
          this.userRanks = res.data.contestRank.userRanks
          this.contestProblems = res.data.contestRank.contestProblems
          this.condition.total = parseInt(res.data.total)
        }
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getRankListByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getRankListByCondition()
    },
  }
}
</script>

<style>
.firstBloodCell {
  background: #bfe6de !important;
}

.acceptedCell {
  background: #eff9f7 !important;
}

.notAcceptedCell {
  background: #ffecec !important;
}

</style>
