<template>
  <div style="height: 100%;
              display: flex;
              flex-direction: column;
             "
  >
    <div style="margin: 10px 20px">
      <el-input v-model="condition.username"
                placeholder="输入用户名称"
                clearable
                style="width: 30%"
                size="small"
      >
      </el-input>
      <el-divider direction="vertical" style="height: 100%"/>
      <el-button icon="el-icon-search"
                 @click="getUserByCondition"
                 size="small"
      >搜索
      </el-button>
    </div>
    <div style="position: relative;flex: 1 1 auto;overflow: auto">
      <el-table
          :data="users"
          style=""
          v-loading="showSpin"
      >
        <el-table-column
            fixed
            prop="username"
            label="用户名"
            align="center"
        ></el-table-column>
        <el-table-column
            label="特殊身份"
            align="center"
            min-width="80px"
        >
          <template slot-scope="scope">
            {{scope.row.roles}}
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            align="center"
            min-width="50px"
        >
          <template slot-scope="scope">
            <el-button @click="editUser(scope.row)" size="mini"
                       icon="el-icon-edit" type="info" plain
            >
            </el-button>
            <el-divider direction="vertical" style="height: 100%"/>
            <el-popconfirm
                title="确定删除吗？"
                @confirm="deleteUser(scope.row)"
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
          :page-sizes="[20, 30]"
          :page-size="condition.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="condition.total"
          background
      >
      </el-pagination>
    </div>

    <el-dialog
        title="修改用户信息"
        :visible.sync="userDialogVisible"
        label-position="right"
        width="40%"
    >
      <el-form ref="userForm"
               :model="userForm"
               label-width="80px"
      >
        <el-form-item label="角色" prop="roles">
          <el-checkbox-group v-model="userForm.roles">
            <el-checkbox v-for="role in $config.roles" :label="role" :key="role">{{ role }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="userDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveUser">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {getUserListByCondition, updateUserRoles} from "@/api/manage/ManageUserApi";

export default {
  name: "ManageUserList",
  data() {
    return {
      users: [],
      selectedUserId: 0,
      condition: {
        currentPage: 1, size: 3, total: 0,
        username: '',
        pageSize: 20,
      },
      showSpin: false,
      userDialogVisible: false,
      userForm: {
        roles: []
      },
    }
  },
  created() {
    this.getUserByCondition()
  },
  methods: {
    saveUser() {
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          console.log(this.userForm.roles);
          updateUserRoles(this.selectedUserId, this.userForm.roles).then(res => {
            if (res.success) {
              this.getUserByCondition()
              this.successNotify(res.message)
            }
            this.userDialogVisible = false
          })
        }
      })
    },
    editUser(row) {
      this.selectedUserId = row.id
      this.userForm.roles = row.roles
      this.userDialogVisible = true
    },
    deleteUser(row) {

    },
    getUserByCondition() {
      this.showSpin = true
      getUserListByCondition(this.condition).then(res => {
        if (res.success) {
          this.condition.total = parseInt(res.data.total)
          this.users = res.data.users
        }
        this.showSpin = false
      })
    },
    handleSizeChange(pageSize) {
      this.condition.pageSize = pageSize
      this.getUserByCondition()
    },
    handleCurrentChange(currentPage) {
      this.condition.currentPage = currentPage
      this.getUserByCondition()
    },
  }
}
</script>

<style scoped>

</style>
