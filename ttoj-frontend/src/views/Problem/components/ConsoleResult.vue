<template>
  <div style="height: 100%;">
    <div class="hint-msg" v-if="!auth">请登录后提交代码</div>
    <div class="hint-msg" v-else-if="auth && judgeRes===null">
      运行之后，这里将会显示结果
    </div>
    <div v-else style="height: 100%;padding: 10px;display: flex;flex-direction: column;">
      <div style="margin-bottom: 10px">
        <span :style="{fontWeight: 700,
                       marginRight: '20px',
                       color: $config.judgeResult[judgeRes.result].color}">
          {{ $config.judgeResult[judgeRes.result].title}}
        </span>
        <span class="grep-text" style="margin-right: 20px">运行时间: {{ judgeRes.timeUse }}ms</span>
        <span class="grep-text">内存消耗: {{ judgeRes.memoryUse }}MB</span>
      </div>
      <div :style="{color:$config.judgeResult[judgeRes.result].color}">
        <div>{{ judgeRes.compileErr }}</div>
        {{$config.judgeResult[judgeRes.result].explain}}
      </div>
      <div v-if="test" class="grep-text">测试输出:</div>
      <el-card v-if="test" shadow="never" style="flex: auto; overflow: auto">
        <pre>{{ judgeRes.output }}</pre>
      </el-card>
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
