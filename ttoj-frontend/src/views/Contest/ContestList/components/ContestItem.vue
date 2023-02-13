<template>
  <el-card shadow="never"
           style=""
  >
    <div style="display: flex;
                justify-content: space-between;
                align-items: center;
                width: 100%;
               "
    >
      <div style="margin-left:10px;">
        <el-link @click="toContestDetail()">{{ contest.name }}</el-link>
        <div class="minor-text" style="margin-top: 5px">
          比赛时间: {{ contest.startTime }} - {{ contest.endTime }}(时长:{{ contest.duration }})
        </div>
      </div>
      <div>
        <el-statistic v-if="contest.status !== 1"
                      ref="statistic" format="HH:mm:ss"
                      :value="contest.countdown"
                      time-indices
                      :value-style="countdownStyle"
        >
          <span slot="title" class="normal-text">{{ contest.status === -1 ? '未开始' : '进行中' }}</span>
        </el-statistic>
        <div v-else class="normal-text">已结束</div>
      </div>
      <el-button type="success" plain size="small"

                 @click="toContestDetail()">
        进入比赛
      </el-button>
    </div>
  </el-card>

</template>

<script>

export default {
  name: "ContestItem",
  props: {
    contest: {
      type: Object,
      default: {}
    }
  },
  created() {
    this.contest.countdown = Date.now() + this.contest.remainSeconds * 1000
  },
  data() {
    return {
      countdownStyle: {
        fontSize: '1rem',
        color: '#909399'
      }
    }
  },
  methods: {
    toContestDetail() {
      console.log(this.$props.contest);
      this.$router.push(`/contest/${this.$props.contest.id}`)
    }
  },
}
</script>

<style>
.contestName:hover {
  color: #409eff;
  cursor: pointer;
}
</style>