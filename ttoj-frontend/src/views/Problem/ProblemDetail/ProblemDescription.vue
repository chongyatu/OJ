<template>
  <div style="width: 100%;
              display: flex;
              flex-direction: column;
              color: var(--mainTextColor);
             ">

    <div style="
                color: rgba(33, 40, 53, 1);
                text-decoration: none;
                text-overflow: ellipsis;
                font-size: 15px;
                ">
      {{ problem.displayId }}. {{ problem.name }}
    </div>
    <div style="display: flex;
                align-items: center;
                padding: 10px 0;
                border-bottom: 1px solid var(--placeholderTextColor);
               "
         class="minor-text"
    >
      <span>时间限制: {{problem.timeLimit}} ms</span>
      <el-divider direction="vertical"/>
      <span>空间限制: {{problem.memoryLimit}} mb</span>
      <el-divider direction="vertical"/>
      <span>难度: {{ problem.level }}</span>
      <el-divider direction="vertical"/>
      <span>作者: {{problem.authorName}}</span>
    </div>
    <div style="padding: 1em 0">
      <MarkdownArea :content="problem.description" bgc="#fff"/>
    </div>
    <div class="itemTitle">输入描述</div>
    <div style="width: 100%;">
      <MarkdownArea :content="problem.input" bgc="#fff"/>
    </div>
    <div class="itemTitle">输出描述</div>
    <div class="">
      <MarkdownArea :content="problem.output" bgc="#fff"/>
    </div>
    <el-divider><i class="el-icon-apple"></i></el-divider>
    <div v-for="(item,i) in problem.sampleCase">
      <div class="itemTitle">示例 {{ i + 1 }}:</div>
      <div style="display: flex;justify-content: space-between">
        <el-card shadow="never" style="width: 48%">
          <pre>{{item.input}}</pre>
        </el-card>
        <el-card shadow="never" style="width: 48%">
          <pre>{{item.output}}</pre>
        </el-card>
      </div>
    </div>
    <el-collapse style="margin-top: 20px"
                 v-model="activeNames" @change="handleChange"
    >
      <el-collapse-item title="显示提示" name="1">
        <div>{{problem.hint}}</div>
      </el-collapse-item>
    </el-collapse>
  </div>

</template>

<script>
import MarkdownArea from "@/components/MarkdownArea";
import {getProblemById} from "@/api/oj/problemApi";
import {getProblemByContestIdAndDisplayId} from "@/api/oj/contestProblemApi";

export default {
  name: "ProblemDescription",
  components: {
    MarkdownArea
  },
  props: {
    problem:{
      type: Object,
      default: {
        id: 0,
        displayId: 0,
        name: '--',
        description: '--',
        input: '--',
        output: '--',
        hint: '--',
        level: '--',
        timeLimit: 0,
        memoryLimit: 0,
        submitNum: 0,
        acNum: 0,
        authorName: '--',
        sampleCase: [],
      }
    }
  },
  data() {
    return {
      activeNames: [],
    }
  },
  created() {

  },
  methods: {
    handleChange(active){

    }
  }
}
</script>

<style scoped>

</style>
