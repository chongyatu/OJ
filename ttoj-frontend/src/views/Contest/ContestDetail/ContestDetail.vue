<template>
  <div style="height: 100%; display: flex; flex-direction: column;">
    <el-tabs v-model="tabsValue" type="card"
             @tab-remove="removeTab"
             @tab-click="handleTabClick"
             style=""
             :before-leave="beforeLeave"
    >
      <el-tab-pane
          v-for="(item, index) in tabs"
          :key="item.name"
          :label="item.title"
          :disabled="item.disabled"
          :name="item.name"
          lazy
          :closable="item.closable"
      >
      </el-tab-pane>
      <el-tab-pane name="refreshBtn">
        <div slot="label" style="color: #409EFF">
          <i class="el-icon-refresh-right"></i> <span>刷新</span>
        </div>
      </el-tab-pane>
    </el-tabs>
    <keep-alive>
      <router-view :key="$route.fullPath"
                   :contest="contest"
                   ref="child"
                   @openProblemTab="openProblemTab"
                   @reloadStatus="reloadStatus"
                   style="width: 100%; flex: auto"
      ></router-view>
    </keep-alive>
  </div>
</template>

<script>
import {getContestByContestId} from "@/api/oj/contestApi";
import {getDurationString} from "@/utils/time";

export default {
  name: "contestDetail",
  components: {},
  data() {
    return {
      contest: {},
      tabsValue: 'contestOverview',
      tabs: [
        {title: '比赛说明', name: 'contestOverview', closable: false, disabled: false},
        {title: '题目', name: 'contestProblems', closable: false, disabled: true},
        {title: '排名', name: 'contestRankings', closable: false, disabled: true},
        {title: '我的', name: 'contestMy', closable: false, disabled: true},
      ],
    }
  },
  created() {
    console.log('contestDetail created');
    this.contestId = this.$route.params.contestId
    this.getContestById(this.contestId)
  },
  mounted() {
  },
  methods: {
    reloadStatus(){
      this.getContestById(this.contestId)
    },
    getSuffixName(){
      let path = this.$route.fullPath
      path = path.substr(-1) === '/' ? path.substring(0, path.length - 1) : path;
      return path.substring(path.lastIndexOf('/') + 1);
    },
    getContestById(contestId) {
      getContestByContestId(contestId).then(res => {
        console.log('contest: ', res);
        if (res.success) {
          let contest = res.data.contest
          contest.duration = getDurationString(contest.startTime, contest.endTime)
          contest.countdown = Date.now() + contest.remainSeconds * 1000
          this.contest = contest
          if (contest.status !== -1) {
            this.enableTabs()
            let cur = this.getSuffixName()
            if (this.tabs.filter(({name}) => name === cur).length === 0) {
              this.openProblemTab(cur)
            } else {
              this.tabsValue = cur
            }
          }else{
            // 比赛未开始,跳回详情页
            this.jumpTo('contestOverview')
          }
        }
      })
    },
    enableTabs() {
      for (let i = 1; i < 4; i++) {
        this.tabs[i].disabled = false
      }
    },
    beforeLeave(visitName, currentName) {
      if (visitName === 'refreshBtn') {
        if(currentName === 'contestOverview'){
          this.getContestById(this.contestId)
        }else{
          this.$refs.child.refresh()
        }
        return false;
      }
    },
    openProblemTab(id) {
      if (this.tabs.filter(({name}) => name === id).length === 0) {
        this.tabs.push({
          title: id, name: id, closable: true, disabled: false
        });
      }
      this.jumpTo(id)
    },
    handleTabClick(tab) {
      if (tab.name !== 'refreshBtn') {
        this.$router.push(`/contest/${this.contestId}/${tab.name}`)
      }
    },
    removeTab(targetName) {
      let tabs = this.tabs;
      let activeName = this.tabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.tabs = tabs.filter(tab => tab.name !== targetName);
      this.jumpTo(activeName)
    },
    jumpTo(name){
      this.tabsValue = name;
      this.$router.push(`/contest/${this.contestId}/${name}`)
    }
  }
}
</script>

<style scoped>
</style>
