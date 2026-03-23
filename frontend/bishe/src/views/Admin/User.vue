<template>
  <div class="admin-user-page">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>
    
    <el-card>
      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column type="index" width="50" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : ''">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getUserList } from '@/api/user'
import { ElMessage } from 'element-plus'

const userList = ref([])
const loading = ref(false)
const total = ref(0)

const query = reactive({
  page: 1,
  size: 10
})

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList(query)
    // 后端返回数组格式
    if (Array.isArray(res)) {
      userList.value = res
      total.value = res.length
    } else {
      // 分页格式
      userList.value = res?.records || []
      total.value = res?.total || 0
    }
  } catch (error) {
    console.error('加载用户列表失败', error)
    ElMessage.error('加载用户列表失败，请检查登录状态')
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const handlePageChange = (page) => {
  query.page = page
  loadUsers()
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-user-page {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-table) {
  border-radius: 8px;
}
</style>
