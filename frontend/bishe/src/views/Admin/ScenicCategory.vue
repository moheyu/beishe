<template>
  <div class="scenic-category-page">
    <div class="page-header">
      <h2>景点分类管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>
    
    <el-card>
      <el-table :data="categoryList" v-loading="loading" stripe>
        <el-table-column type="index" width="50" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 分类表单对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
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
import { getScenicCategoryList, addScenicCategory, updateScenicCategory, deleteScenicCategory } from '@/api/scenic'

const categoryList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: '',
  sort: 0
})

const loadCategories = async () => {
  loading.value = true
  try {
    const res = await getScenicCategoryList()
    // 后端返回分页格式 {records: [...], total: ...}
    categoryList.value = res?.records || res || []
  } catch (error) {
    console.error('加载分类失败', error)
    ElMessage.error('加载分类失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.id = null
  form.name = ''
  form.sort = 0
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.sort = row.sort
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateScenicCategory(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await addScenicCategory(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadCategories()
  } catch (error) {
    console.error('保存失败', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除分类"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteScenicCategory(row.id)
      ElMessage.success('删除成功')
      loadCategories()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.scenic-category-page {
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
