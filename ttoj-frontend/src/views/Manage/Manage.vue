<template>
  <!--  overflow: auto; 不加这个就会多个滚动条  -->
  <div style="height: 100vh;
              display: flex;
             ">
    <el-menu
        :default-active="this.$route.path"
        class="el-menu-vertical-demo"
        :default-openeds="openeds"
        router
        style="height: 100%;">
      <el-menu-item index="/manage"><i class="el-icon-s-tools"></i> 仪表盘</el-menu-item>
      <el-menu-item v-if="showUserManage" index="/manage/users"><i class="el-icon-user-solid"></i> 用户管理</el-menu-item>
      <el-submenu v-if="showProblemManage" index="1">
        <template slot="title">
          <span><i class="el-icon-s-order"></i> 题目管理</span>
        </template>
        <el-menu-item style="margin-left: 15px" index="/manage/problems">题目列表</el-menu-item>
        <el-menu-item style="margin-left: 15px" index="/manage/problems/create">创建题目</el-menu-item>
      </el-submenu>
      <el-submenu v-if="showContestManage" index="2">
        <template slot="title">
          <span><i class="el-icon-s-data"></i> 比赛管理</span>
        </template>
        <el-menu-item style="margin-left: 15px" index="/manage/contests">比赛列表</el-menu-item>
        <el-menu-item style="margin-left: 15px" index="/manage/contests/create">创建比赛</el-menu-item>
      </el-submenu>
    </el-menu>
    <div style="height: 100%;
                overflow: auto;
                flex: 1 1 auto;
               "
    >
      <router-view/>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "Manage",
  components: {},
  data() {
    return {
      openeds: ['1', '2', '3']
    }
  },
  computed: {
    ...mapGetters([
      'showProblemManage',
      'showContestManage',
      'showUserManage'
    ])
  },
  methods: {}
}
</script>

<style scoped>

</style>
