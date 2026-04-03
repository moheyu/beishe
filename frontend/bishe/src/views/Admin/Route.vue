<template>
  <div class="admin-route-page">
    <div class="page-header">
      <h2>路线管理</h2>
      <el-button type="primary" @click="$router.push('/admin/route/add')">
        <el-icon><Plus /></el-icon>
        新增路线
      </el-button>
    </div>
    
    <el-card>
      <el-table :data="routeList" v-loading="loading" stripe>
        <el-table-column type="index" width="50" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image :src="getImageUrl(row.coverImage)" fit="cover" style="width: 60px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="路线名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="duration" label="天数" width="80">
          <template #default="{ row }">
            {{ row.duration }}天
          </template>
        </el-table-column>
        <el-table-column prop="budget" label="预算" width="100">
          <template #default="{ row }">
            <span style="color: #f56c6c;">¥{{ row.budget }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminRouteList, deleteRoute } from '@/api/route'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const routeList = ref([])
const total = ref(0)
const loading = ref(false)

const query = reactive({
  pageNum: 1,
  pageSize: 10
})

const loadRouteList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: query.pageNum,
      pageSize: query.pageSize
    }
    const res = await getAdminRouteList(params)
    routeList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('加载路线列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size) => {
  query.pageSize = size
  loadRouteList()
}

const handlePageChange = (page) => {
  query.pageNum = page
  loadRouteList()
}

const handleEdit = (row) => {
  router.push(`/admin/route/${row.id}/edit`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除路线"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRoute(row.id)
      ElMessage.success('删除成功')
      loadRouteList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  loadRouteList()
})
</script>

<style scoped>
.admin-route-page {
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
