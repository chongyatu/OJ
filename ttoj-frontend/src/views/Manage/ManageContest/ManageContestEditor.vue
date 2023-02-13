<template>
  <div style="height: 100%;
              padding: 0 10px 20px 20px;
              overflow: auto;
              position: relative;
             "
  >
    <el-form ref="contest"
             :model="contest"
             :rules="contestRules"
             label-width="120px"
             label-position="top"
             inline
             v-loading="spinShow"
    >
      <el-row :gutter="10">
        <el-col :span="24">
          <el-form-item label="比赛名字" prop="name" style="width: 80%">
            <el-input v-model="contest.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="24">
          <el-form-item label="比赛描述" prop="description" style="width: 100%">
            <mavon-editor v-model="contest.description"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="6">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
                v-model="contest.startTime"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                placeholder="选择开始时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
                v-model="contest.endTime"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
                placeholder="选择结束时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="是否可见" prop="visible">
            <el-switch v-model="contest.visible"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="罚时(minutes)" prop="penalty">
            <el-input v-model="contest.penalty"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider/>
      <el-row :gutter="10">
        <el-col>
          <el-button type="primary" @click="handleSave">保存比赛</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>

import {getContestByContestId, saveContest, updateContest} from "@/api/manage/ManageContestApi";
import {mapGetters} from "vuex";

export default {
  name: "ManageContestEditor",
  components: {},

  data() {
    let validatePenalty = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请填写提交错误的罚时'));
      } else {
        let penalty = parseInt(value)
        if (penalty >= 0 && penalty <= 100) {
          callback();
        } else {
          callback(new Error('罚时的范围是0-100'));
        }
      }
    };
    let validateEndTime = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请填写比赛结束时间'));
      } else {
        if (new Date(value).valueOf() <= new Date(this.contest.startTime).valueOf()) {
          callback(new Error('开始时间晚于结束时间'));
        } else {
          callback();
        }
      }
    };
    return {
      spinShow: false,
      contest: {
        name: '',
        description: '',
        visible: false,
        creatorName: this.username,
        creatorId: this.userId,
        penalty: 20,
      },
      contestRules: {
        name: [
          {required: true, message: '请填写比赛名称', trigger: 'blur'},
          {min: 1, max: 100, message: '长度为1-100'}
        ],
        description: [
          {required: true, message: '请填写题面描述', trigger: 'blur'}
        ],
        startTime: [
          {required: true, message: '请填写开始时间', trigger: 'blur'}
        ],
        endTime: [{validator: validateEndTime, trigger: 'blur'}],
        visible: [
          {required: true, message: '请填写题目是否可见', trigger: 'blur'}
        ],
        penalty: [
          {validator: validatePenalty, trigger: 'blur'}
        ],
      },
      contestId: -1
    }
  },
  computed: {
    ...mapGetters([
      'user',
      'userId',
      'username',
      'auth'
    ])
  },
  created() {
    let contestId = this.$route.params.contestId
    if (!!contestId) {
      console.log(contestId);
      this.contestId = contestId
      this.spinShow = true
      getContestByContestId(contestId).then(res => {
        this.spinShow = false
        this.contest = res.data.contest
      })
    }
  },
  methods: {
    handleSave() {
      this.$refs['contest'].validate(valid => {
        if (valid) {
          console.log(this.contest.startTime);
          if (this.contestId !== -1) {
            updateContest(this.contest).then(res => {
              this.notify(res.success, res.message)
            })
          } else {
            this.contest.creatorName = this.username
            this.contest.creatorId = this.userId
            saveContest(this.contest).then(res => {
              this.notify(res.success, res.message)
            })
          }
        } else {
          this.warnNotify('参数不合法,请重试')
        }
      })
    },
  }
}
</script>

<style scoped>
</style>
