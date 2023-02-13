<template>
  <div style="height: 100%;display: flex;flex-direction: column;">
    <div class="hint-msg" v-if="!auth">请登录后提交代码</div>
    <div class="hint-msg" v-else-if="auth && judgeRes===null">
      运行之后，这里将会显示结果
    </div>
    <div v-else style="padding: 10px">
      <div style="margin-bottom: 10px">
        <span :style="{fontWeight: 700, color: !!$config.judgeResult[judgeRes.result] ? $config.judgeResult[judgeRes.result].color : ''}">
          {{ judgeRes.result }}
        </span>
        <span class="grep-text" style="margin-left: 20px">运行时间: {{ judgeRes.timeUse }}ms</span>
        <span class="grep-text">内存消耗: {{ judgeRes.memoryUse }}MB</span>
      </div>
      <div :style="{color:$config.judgeResult[judgeRes.result] ? $config.judgeResult[judgeRes.result].color : ''}">
        {{ !!$config.judgeResult[judgeRes.result] ? $config.judgeResult[judgeRes.result].explain : ''}}
      </div>
      <div v-if="test" style="">
        <div class="grep-text" style="">测试输出:</div>
        <el-card shadow="never">
          <pre>{{ judgeRes.output }}</pre>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "ConsoleResult",
  props: {
    judgeRes: null,
    test: false
  },
  computed: {
    ...mapGetters([
      'auth'
    ]),
  },
  data() {
    return {}
  }
}
</script>

<style scoped>
.grep-text {
  color: #999999;
  font-size: 14px;
  margin-right: 20px;
}
</style>
