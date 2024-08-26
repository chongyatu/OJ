<template>
<div style="position: relative">
  <el-card class="box-card" >
    <div slot="header" class="clearfix">
      <div class="">
        <i class="el-icon-s-data"></i> 最近比赛
      </div>
    </div>
    <div>
      <div v-if="contests.length === 0" class="" style="text-align: center">暂无比赛</div>
      <RecentContestItem v-for="(item, index) in contests" :key="index"
                         :contest="item" @goToContestDetail="goToContestDetail"/>
    </div>
  </el-card>
</div>
</template>

<script>
import {getRecentContest} from "@/api/oj/contestApi";
import RecentContestItem from "@/views/Problem/ProblemList/components/RecentContestItem";

export default {
  name: "RecentContestCard",
  components:{
    RecentContestItem
  },
  data(){
    return{
      contests:[],
      spinShow: false,
    }
  },
  created(){
    this.spinShow = true
    getRecentContest(this.$config.recentContestDays).then(res=>{
      if(res.success){
        this.contests = res.data.contests
      }
      this.spinShow = false
    })
  },
  methods:{
    goToContestDetail(contestId){
      this.$router.push(`/contest/${contestId}`)
    }
  }
}
</script>

<style scoped>

</style>
