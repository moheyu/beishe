<template>
  <div class="admin-announcement-page">
    <div class="page-header">
      <h2>公告管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        发布公告
      </el-button>
    </div>
    
    <el-card>
      <el-table :data="announcementList" v-loading="loading" stripe>
        <el-table-column type="index" width="50" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
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
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑公告' : '发布公告'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAnnouncementList, addAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/announcement'

const announcementList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const total = ref(0)

const query = reactive({
  page: 1,
  size: 10
})

const form = reactive({
  id: null,
  title: '',
  content: ''
})

const loadAnnouncements = async () => {
  loading.value = true
  try {
    const res = await getAnnouncementList(query)
    // 后端返回直接数组格式
    announcementList.value = res || []
    total.value = res?.length || 0
  } catch (error) {
    console.error('加载公告失败', error)
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const handleAdd = () => {
  isEdit.value = false
  form.id = null
  form.title = ''
  form.content = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.id = row.id
  form.title = row.title
  form.content = row.content
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateAnnouncement(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await addAnnouncement(form)
      ElMessage.success('发布成功')
    }
    dialogVisible.value = false
    loadAnnouncements()
  } catch (error) {
    console.error('保存失败', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除公告"${row.title}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAnnouncement(row.id)
      ElMessage.success('删除成功')
      loadAnnouncements()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

const handlePageChange = (page) => {
  query.page = page
  loadAnnouncements()
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.admin-announcement-page {
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
