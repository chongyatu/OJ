<template>
  <div id="contestOverviewWrapper"
       style="position: relative;
              padding: 20px;
             "
  >
    <div style="display: flex;
                justify-content: space-between;
                align-items: center
               "
    >
      <div style="font-weight: 600;font-size: 24px">
        {{ contest.name }}
      </div>
      <div>
        比赛时间: {{ contest.startTime }} - {{ contest.endTime }} (时长:{{ contest.duration }})
      </div>
    </div>
    <div style="display: flex;
                  flex-direction: column;
                  align-items: center;
                  margin-top: 40px;
                 "
    >
      <el-statistic v-if="contest.status !== 1"
                    ref="statistic" @finish="finish" format="HH:mm:ss"
                    :value="contest.countdown"
                    time-indices
                    :value-style="countdownStyle"
      >
        <div slot="title" style="font-size: 20px; color: var(--titleColor)">{{contest.status===-1?'比赛未开始':(contest.status===0?'比赛进行中':'比赛已结束')}}</div>
      </el-statistic>
      <div v-else>比赛已结束</div>
    </div>
    <div style="margin: 20px 0;
               "
    >
      <h3>比赛信息</h3>
      <div class="callout" style="margin-bottom: 10px;
                                 "
      >
        {{ contest.description }}
      </div>
      <h3>比赛规则</h3>
      <div class="callout" style="">
        对于每次提交错误的罚时为: {{contest.penalty}} 分钟
      </div>
    </div>
  </div>

</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "contestOverview",
  props: {
    propTabName: {
      type: String,
      default: ''
    },
    contest:{
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      countdownStyle:{
        fontSize: '30px',
        color: 'var(--titleColor)'
      },
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'username',
      'auth',
      'user'
    ])
  },
  created() {
  },
  watch:{

  },
  methods: {
    finish(){
      this.$emit('reloadStatus')
    },
  },
}
</script>

<style scoped>
@media screen and (min-width: 920px) {
  #contestOverviewWrapper {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
