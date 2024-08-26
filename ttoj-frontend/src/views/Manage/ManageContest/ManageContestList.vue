<template>
  <div style="
           "
  >
    <div style="margin: 10px 20px">
      <el-input v-model="condition.name"
                placeholder="输入比赛名字"
                clearable
                style="width: 30%"
                size="small"
      >
      </el-input>
      <el-button icon="el-icon-search"
                 @click="getContestsByCondition"
                 size="small"
      >搜索
      </el-button>
    </div>
    <div style="position: relative;flex: 1 1 auto;overflow: auto">
      <el-table
          :data="contests"
          style=""
          v-loading="showSpin"
      >
        <el-table-column
            prop="name"
            label="名字"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="creatorName"
            label="创建者"
            align="center"
        ></el-table-column>
        <el-table-column
            prop="status"
            label="状态"
            align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status === -1">未开始</span>
            <span v-else-if="scope.row.status === 0">进行中</span>
            <span v-else>已结束</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="visible"
            label="是否可见"
            align="center"
        >
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.visible"
                @change="changeContestVisibility(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            align="center"
            min-width="150px"
        >
          <template slot-scope="scope">
            <el-button @click="editContest(scope.row)" size="mini"
                       icon="el-icon-edit" plain
            >
            </el-button>
            <el-divider direction="vertical" style="height: 100%"/>
            <el-button @click="listContestProblems(scope.row)" size="mini" plain
                       icon="el-icon-folder-opened"
            >
            </el-button>
            <el-divider direction="vertical" style="height: 100%"/>
            <el-popconfirm
                title="确定删除吗？"
                @confirm="deleteContest(scope.row)"
            >
              <el-button
                  slot="reference"
                  size="mini" icon="el-icon-delete" type="danger" plain
              >
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block" style="margin: 10px 10px 10px 20px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :currentPage="condition.currentPage"
          :page-sizes="[10, 20]"
          :page-size="condition.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="condition.total"
          background
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>

import {
  changeContestVisibility,
  deleteContestById,
  getContestAuthorUsersBySearch,
  getContestsByCondition, saveContestAuthors, updateContestAuthors
} from "@/api/manage/ManageContestApi";

export default {
  name: "ManageContestList",
  data() {
    return {
      contests: [],
      condition: {
        currentPage: 1,
        total: 0,
        name: '',
        pageSize: 20,
      },
      showSpin: true,
    }
  },
  created() {
    this.getContestsByCondition()
  },
  mounted() {
  },
  methods: {
    listContestProblems(contest) {
      console.log(contest);
      this.$router.push(`/manage/contests/${contest.id}/problems`)
    },
    changeContestVisibility(contest) {
      let contestId = contest.id
      let visible = contest.visible
      changeContestVisibility(contestId, visible).then(res => {
      })
    },
    getContestsByCondition() {
      this.showSpin = true
      getContestsByCondition(this.condition).then(res => {
        this.showSpin = false
        if (res.success) {
          this.contests = res.data.contests
          this.condition.total = parseInt(res.data.total)
        }
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getContestsByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getContestsByCondition()
    },
    editContest(contest) {
      this.$router.push(`/manage/contests/${contest.id}`)
    },
    deleteContest(row) {
      deleteContestById(row.id).then(res => {
        if (res.success) {
          this.getContestsByCondition()
        }
      })
    },
    handleClose(contest, tag) {
      contest.contestAuthors.splice(contest.contestAuthors.indexOf(tag), 1);
    },

    showInput() {
      this.contestAuthorInputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
  }
}
</script>

<style scoped>
.el-button {
  margin-left: 3px;
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
