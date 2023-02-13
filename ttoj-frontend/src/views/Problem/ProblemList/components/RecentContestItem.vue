<template>
<div style="width: 100%;
            text-align: center;
            padding: 10px;
            border-bottom: 1px solid var(--borderColor)
">
  <el-link type="primary" @click="goToContestDetail(contest.id)"
           style="font-size: 15px;
                  margin-bottom: 10px;
                 "
  >
    {{contest.name}}
  </el-link>
  <el-statistic ref="statistic" format="HH:mm:ss"
                :value="contest.countdown"
                time-indices
                :value-style="countdownStyle"
  >
    <span slot="title" class="normal-text">{{contest.status===-1?'未开始':'进行中'}}</span>
  </el-statistic>
</div>
</template>

<script>
export default {
  name: "RecentContestItem",
  props:{
    contest:{
      type: Object,
      default: () => {}
    }
  },
  data(){
    return{
      countdownStyle:{
        fontSize: '1rem',
        color: '#909399'
      }
    }
  },
  created() {
    this.contest.countdown = Date.now() + this.contest.remainSeconds * 1000
  },
  methods:{
    goToContestDetail(contestId){
      this.$emit('goToContestDetail', contestId)
    },
  }
}
</script>

<style scoped>

</style>
