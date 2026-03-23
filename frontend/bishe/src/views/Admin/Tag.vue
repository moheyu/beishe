<template>
  <div class="tag-page">
    <div class="page-header">
      <h2>标签管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增标签
      </el-button>
    </div>
    
    <el-card>
      <el-table :data="tagList" v-loading="loading" stripe>
        <el-table-column type="index" width="50" />
        <el-table-column prop="name" label="标签名称" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑标签' : '新增标签'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标签名称">
          <el-input v-model="form.name" />
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
import { getTagList, addTag, updateTag, deleteTag } from '@/api/tag'

const tagList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: ''
})

const loadTags = async () => {
  loading.value = true
  try {
    const res = await getTagList()
    // 后端返回分页格式 {records: [...], total: ...}
    tagList.value = res?.records || res || []
  } catch (error) {
    console.error('加载标签失败', error)
    ElMessage.error('加载标签失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateTag(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await addTag(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadTags()
  } catch (error) {
    console.error('保存失败', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除标签"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTag(row.id)
      ElMessage.success('删除成功')
      loadTags()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tag-page {
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

:deep(.el-table) {
  border-radius: 8px;
}
</style>
