<template>
  <div class="user-manage">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>

    <el-card>
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column prop="role" label="角色">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === 1 ? 'danger' : 'primary'" size="small">
              {{ scope.row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" :type="scope.row.status === 1 ? 'warning' : 'success'" @click="handleToggleStatus(scope.row)">
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="showEditDialog" width="500px">
      <el-form :model="userForm" :rules="rules" ref="userForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="userForm.nickname"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="userForm.role">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAdminUserList, updateUserStatus, updateUser } from '@/api/admin'

export default {
  name: 'UserManage',
  data() {
    return {
      userList: [],
      loading: false,
      showEditDialog: false,
      submitLoading: false,
      dialogTitle: '编辑用户',
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      userForm: {
        id: '',
        username: '',
        nickname: '',
        role: 0,
        status: 1
      },
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadUserList()
  },
  methods: {
    async loadUserList() {
      this.loading = true
      try {
        const res = await getAdminUserList(this.queryParams)
        const pageData = res.data
        this.userList = pageData.records || []
        this.total = pageData.total || 0
      } catch (error) {
        this.$message.error('加载用户列表失败')
      } finally {
        this.loading = false
      }
    },
    handleEdit(row) {
      this.userForm = { ...row }
      this.showEditDialog = true
    },
    async handleToggleStatus(row) {
      const action = row.status === 1 ? '禁用' : '启用'
      try {
        await this.$confirm(`确定要${action}这个用户吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const newStatus = row.status === 1 ? 0 : 1
        await updateUserStatus(row.id, newStatus)
        this.$message.success(`${action}成功`)
        this.loadUserList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(`${action}失败`)
        }
      }
    },
    async handleSubmit() {
      this.$refs.userForm.validate(async valid => {
        if (valid) {
          this.submitLoading = true
          try {
            await updateUser(this.userForm.id, {
              nickname: this.userForm.nickname,
              role: this.userForm.role,
              status: this.userForm.status
            })
            this.$message.success('更新成功')
            this.showEditDialog = false
            this.loadUserList()
          } catch (error) {
            this.$message.error('更新失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.loadUserList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.loadUserList()
    }
  }
}
</script>

<style scoped>
.user-manage {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}
</style>