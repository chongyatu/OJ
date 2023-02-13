<template>
  <div id="contestListWrapper"
       style=""
  >
    <el-row style="margin-top: 20px;padding: 0 20px">
      <el-col>
        <el-input v-model="condition.name"
                  placeholder="输入比赛名字"
                  clearable
                  style="width: 40%"
                  size="small"
                  suffix-icon="el-icon-search"
                  @keyup.enter.native="getContestList"
        >
        </el-input>
        <div style="
                  overflow: auto;
                  position: relative;
                 "
             v-loading="spinShow"
        >
          <ContestItem v-for="(item,index) in contests"
                       :key="item.id"
                       :contest="item"
                       style="margin-top: 10px"
          >
          </ContestItem>
        </div>
        <div class="block" style="padding: 10px 0px 10px 0px;
                                overflow: auto
                               "
        >
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :currentPage="condition.currentPage"
              :page-sizes="[15, 30]"
              :page-size="condition.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="condition.total"
              background
          >
          </el-pagination>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ContestItem from "./components/ContestItem";
import {getContestsByCondition} from "@/api/oj/contestApi";
import {getDurationString} from "@/utils/time";

export default {
  name: "ContestList",
  components: {
    ContestItem
  },
  created() {
    this.getContestList()
  },
  data() {
    return {
      contests: [
        {
          id: 0,
          name: '--',
          description: '--',
          startTime: 0,
          endTime: 0,
        }
      ],
      spinShow: true,
      condition: {
        currentPage: 1, pageSize: 15, visible: true, total: 0, name: ''
      }
    }
  },
  methods: {
    getContestList() {
      this.spinShow = true
      getContestsByCondition(this.condition).then(res => {
        let contests = res.data.contests
        for (let i in contests) {
          console.log(contests[i].startTime, contests[i].endTime);
          contests[i].duration = getDurationString(contests[i].startTime, contests[i].endTime)
        }
        this.condition.total = parseInt(res.data.total)
        this.contests = contests
        this.spinShow = false
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getContestList()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getContestList()
    },
  }
}
</script>

<style scoped>
@media screen and (min-width: 920px) {
  #contestListWrapper {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
