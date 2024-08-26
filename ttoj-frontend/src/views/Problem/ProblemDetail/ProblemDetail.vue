<template>
  <div style="height: 100%">
      <Split v-model="split_h" style="height: 100%;width: 100%;">
        <div slot="left" style="height: 100%;
                                display: flex;
                                flex-direction: column;
                                padding-left: 20px;
                               "
        >
          <el-tabs v-model="activeName"
                   @tab-click="handleClick"
                   style="margin-bottom: 10px"
          >
            <el-tab-pane label="题目描述" name="ProblemDescription"/>
          </el-tabs>
          <router-view style="flex: 1 1 auto;overflow: auto;padding-bottom: 20px"
                       :problem="problem"
          />
        </div>

        <div slot="right" style="height: 100%;
                                                       width: 100%;
                                                       display: flex;
                                                       flex-direction: column;
                                                       flex: 1 0 auto;
                                                      ">
          <Split v-model="split_v" mode="vertical" max="40px">
            <div slot="top" style="height: 100%;
                                   display: flex;
                                   flex-direction: column;
                                   flex: 1 0 auto;
                                "
            >
              <div style="line-height: 40px;
                          height: 40px;
                          background-color: #f7f8fa;
                          padding: 0 20px;
                       "
              >
                <el-select size="mini"
                           v-model="submitLanguage"
                           placeholder="语言"
                           style="width: 100px"
                           @change="changeLanguage"
                >
                  <el-option
                      v-for="item in languageOption"
                      :key="item"
                      :label="item"
                      :value="item"
                  >
                  </el-option>
                </el-select>
              </div>
              <div style="flex-grow: 1;overflow: auto;padding-left: 8px;">
                <codemirror v-model="editor.code"
                            :options="editor.cmOptions"
                            style="height: 100%;"
                            ref="myEditor"
                />
              </div>
            </div>
            <div slot="bottom"
                 style="height: 100%;
                        display: flex;
                        flex-direction: column;
                       "
            >
              <el-tabs type="border-card"
                       style="flex: 1 1 auto;padding-left: 8px;"
                       v-model="consoleActiveTabName"
              >
                <el-tab-pane style="height: 100%;overflow: auto"
                             :label="'测试用例'"
                             :name="'testcase'"
                >
                  <codemirror v-model="testEditor.code"
                              :options="testEditor.cmOptions"
                              style="height: 100%;"
                  />
                </el-tab-pane>
                <el-tab-pane style="height: 100%;"
                             :label="'测试结果'"
                             :name="'testRes'"
                >
                  <ConsoleResult :judgeRes="testRes" :test="true"/>
                </el-tab-pane>
                <el-tab-pane style="height: 100%;"
                             :label="'提交结果'"
                             :name="'submitRes'"
                >
                  <ConsoleResult :judgeRes="judgeRes" :test="false"/>
                </el-tab-pane>
              </el-tabs>
            </div>
          </Split>
          <div style="display: flex;justify-content: space-between;
                        padding: 15px;
                        height: 50px;
                        line-height: 50px;
                        z-index: 9;
                        background: #fff;
                        border-top: 1px solid #F2F3F5;
                       ">
            <div style="display: flex;align-items: center">
              <el-button type="text" @click="switchConsole">
                <span v-if="consoleOpen">关闭控制台</span>
                <span v-else>打开控制台</span>
              </el-button>
            </div>
            <div style="display: flex;align-items: center">
              <el-button type="success" plain size="small"
                         @click="runTest"
                         :loading="testLoading"
                         icon="el-icon-question"
              >测试运行</el-button>
              <el-button type="primary" plain size="small"
                         @click="submit"
                         :loading="submitLoading"
                         icon="el-icon-upload"
              >提交代码</el-button>
            </div>
          </div>
        </div>
      </Split>
    </div>
<!--  </div>-->
</template>

<!--TODO  代码丢失-->
<script>
import ProblemDescription from "./ProblemDescription";
import {getProblemById} from "@/api/oj/problemApi";
import {archiveJudge, contestSubmit, testJudge} from "@/api/oj/judgeApi";
import {mapGetters} from "vuex";
import {getProblemByContestIdAndDisplayId} from "@/api/oj/contestProblemApi";
import MarkdownArea from "@/components/MarkdownArea";
import ConsoleResult from "@/views/Problem/components/ConsoleResult";
import Split from "@/components/split"

export default {
  name: "problemDetail",
  components: {ProblemDescription, MarkdownArea, ConsoleResult, Split},
  props: {
    propTabName: {
      type: String,
      default: ''
    }
  },
  computed: {
    ...mapGetters([
      'user',
      'userId',
      'username',
      'auth'
    ]),
    myEditor() {
      return this.$refs.myEditor.codemirror;
    }
  },
  data() {
    return {
      testRes: null,
      submitLanguage: '',
      consoleActiveTabName: 'testcase',
      consoleOpen: true,
      submitLoading: false,
      testLoading: false,
      theme: {
        'cpp': 'text/x-c++src',
        'c': 'text/x-csrc',
        'go': 'text/x-go',
        'java': 'text/x-java',
        'python2': 'text/x-python',
        'python3': 'text/x-python',
        'javascript': 'text/javascript'
      },
      split_h: 0.5,
      split_v: 0.6,
      mode: '',
      editor: {
        code: '// code here',
        label: 'C++',
        cmOptions: {
          tabSize: 4,
          mode: 'text/x-c++src',
          theme: 'idea',
          lineNumbers: true,
          line: true,
          styleActiveLine: true, // 高亮选中行
          foldGutter: true, // 块槽
          matchBrackets: true,
          indentWithTabs: true,
          smartIndent: true,
          // more CodeMirror options...
          gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
          lineWrapping: true,
          // 选中文本自动高亮，及高亮方式
          styleSelectedText: true,
          showCursorWhenSelecting: true,
          highlightSelectionMatches: {showToken: /\w/, annotateScrollbar: true},
          // extraKeys: { Ctrl: 'autocomplete' }, //自定义快捷键
          indentUnit: 4, //一个块（编辑语言中的含义）应缩进多少个空格
          autoCloseBrackets: true,
          autoCloseTags: true,
          hintOptions: {
            // 当匹配只有一项的时候是否自动补全
            completeSingle: false,
          },
        },
      },
      testEditor: {
        code: '',
        cmOptions: {
          tabSize: 4,
          mode: '',
          theme: 'idea',
          lineNumbers: true,
          line: true,
          styleActiveLine: true, // 高亮选中行
          foldGutter: true, // 块槽
          matchBrackets: false,
          indentWithTabs: true,
          smartIndent: false,
          // more CodeMirror options...
        }
      },
      languageOption: [],
      problem: {
        id: 0,
        displayId: '--',
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
      },
      judgeRes: null,
      activeName: 'ProblemDescription',
      contestId: 0,
    }
  },
  created() {
    let displayId = this.$route.params.problemDisplayId
    let contestId = this.$route.params.contestId
    this.contestId = contestId
    if ((!!displayId) && (!!contestId)) {
      this.mode = 'contest'
      this.getProblemByContestIdAndDisplayId(contestId, displayId)
    } else {
      console.log(this.$route.params);
      let problemId = this.$route.params.problemId
      if (!!problemId) {
        this.getProblemById(problemId)
      } else {
        this.errorNotify('problemId无效')
      }
    }
  },
  mounted() {
    this.myEditor.setOption('mode', this.theme['cpp'])
  },
  methods: {
    changeLanguage(lang) {
      // this.editor.cmOptions.mode = this.theme[lang]
      this.myEditor.setOption('mode', this.theme[lang])
    },
    switchConsole() {
      this.split_v = this.consoleOpen ? 1 : 0.6
      this.consoleOpen = !this.consoleOpen
    },
    runTest() {
      if (!this.auth) {
        this.warnNotify('请登录后再提交代码')
        return
      }
      this.consoleActiveTabName = 'testRes'
      this.testLoading = true
      this.testRes = null
      testJudge({
        userId: this.userId,
        problemId: this.problem.id,
        code: this.editor.code,
        testcase: this.testEditor.code,
        language: this.submitLanguage,
      }).then(res => {
        if (res.success) {
          this.testRes = res.data.judgeRes
        }
        this.testLoading = false
      }).catch(err => {
        this.testLoading = false
      })
    },
    getProblemById(problemId) {
      getProblemById(problemId).then(res => {
        let problem = res.data.problem
        this.languageOption = []
        //不用push的话页面不会更新
        this.languageOption.push(...JSON.parse(problem.languages))
        problem.sampleCase = JSON.parse(problem.sampleCase)
        this.problem = problem
        this.submitLanguage = this.languageOption[0]
      })
    },
    getProblemByContestIdAndDisplayId(contestId, displayId) {
      getProblemByContestIdAndDisplayId(contestId, displayId).then(res => {
        if (res.success) {
          let problem = res.data.problem
          this.languageOption = []
          this.languageOption.push(...JSON.parse(problem.languages))
          problem.displayId = displayId
          problem.sampleCase = JSON.parse(problem.sampleCase)
          this.problem = problem
          this.submitLanguage = this.languageOption[0]
        }
      })
    },
    refresh() {
      this.getProblemByContestIdAndDisplayId(this.contestId, this.problem.displayId)
    },
    onTestCaseChange(code) {
      this.testEditor.code = code
    },
    handleClick(tab, event) {

    },
    submit() {
      if (!this.auth) {
        this.warnNotify('请登录后再提交代码')
        return
      }
      this.consoleActiveTabName = 'submitRes'
      this.submitLoading = true
      this.judgeRes = null
      if (this.mode === 'contest') {
        contestSubmit({
          contestId: this.$route.params.contestId,
          problemId: this.problem.id,
          problemDisplayId: this.problem.displayId,
          userId: this.userId,
          username: this.username,
          code: this.editor.code,
          language: this.submitLanguage,
        }).then(res => {
          this.submitLoading = false
          if (res.success) {
            this.judgeRes = res.data.judgeRes
          }
        }).catch(err => {
          this.submitLoading = false
        })
      } else {
        archiveJudge({
          userId: this.userId,
          username: this.username,
          problemId: this.problem.id,
          problemName: this.problem.name,
          code: this.editor.code,
          language: this.submitLanguage,
        }).then(res => {
          this.submitLoading = false
          if (res.success) {
            this.judgeRes = res.data.judgeRes
          }
        }).catch(err => {
          this.submitLoading = false
        })
      }
    }
  }
}
</script>

<style>
.el-tabs--border-card > .el-tabs__content {
  padding: 0 !important;
}

.el-tabs {
  display: flex !important;
  flex-direction: column !important;
}

.el-tabs__content {
  flex: 1 1 auto !important;
}

.el-tabs--border-card {
  height: 100%;
}

.hint-msg {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  color: rgba(229, 229, 229, 1);
  height: 160px;
  font-size: 18px;
  font-weight: 500;
}
</style>
