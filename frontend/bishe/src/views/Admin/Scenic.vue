<template>
  <div class="admin-scenic-page">
    <div class="page-header">
      <h2>景点管理</h2>
      <el-button type="primary" @click="$router.push('/admin/scenic/add')">
        <el-icon><Plus /></el-icon>
        新增景点
      </el-button>
    </div>
    
    <el-card class="filter-card">
      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="景点名称" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.categoryId" placeholder="全部分类" clearable>
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option label="已上架" :value="1" />
            <el-option label="已下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card>
      <el-table :data="scenicList" v-loading="loading" stripe style="width: 100%">
        <el-table-column type="index" width="50" />
        <el-table-column label="封面" width="80">
          <template #default="{ row }">
            <el-image 
              :src="row.images && row.images.length > 0 ? getImageUrl(row.images[0]) : ''" 
              fit="cover" 
              style="width: 50px; height: 50px; border-radius: 4px;" 
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="景点名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="90" />
        <el-table-column prop="location" label="位置" min-width="120" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="80">
          <template #default="{ row }">
            <span style="color: #f56c6c;">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="评分" width="90">
          <template #default="{ row }">
            <div v-if="Number(row.score) > 0">
              <el-rate :model-value="Number(row.score)" :max="5" disabled size="small" />
            </div>
            <span v-else class="no-rating">暂无评分</span>
          </template>
        </el-table-column>
        <el-table-column prop="recommendLevel" label="推荐" width="100">
          <template #default="{ row }">
            <div v-if="row.recommendLevel > 0" class="recommend-stars">
              <el-rate :model-value="row.recommendLevel" :max="5" disabled size="small" />
              <span class="recommend-level">{{ row.recommendLevel }}级</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="70">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handleSetRecommend(row)">推荐</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
    
    <!-- 推荐等级对话框 -->
    <el-dialog v-model="recommendDialogVisible" title="设置推荐等级" width="400px">
      <el-form :model="recommendForm" label-width="100px">
        <el-form-item label="推荐等级">
          <el-rate v-model="recommendForm.level" :max="5" show-score />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="recommendDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRecommend">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminScenicList, deleteScenic, updateScenic, setRecommendLevel } from '@/api/scenic'
import { getScenicCategoryList } from '@/api/scenicCategory'
import { getImageUrl as getImageUrlUtil } from '@/utils/image'

const router = useRouter()

// 处理图片 URL，拼接完整路径
const getImageUrl = (url) => {
  return getImageUrlUtil(url)
}
const scenicList = ref([])
const categories = ref([])
const total = ref(0)
const loading = ref(false)
const recommendDialogVisible = ref(false)
const recommendForm = reactive({ id: null, level: 0 })

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  categoryId: null,
  status: null
})

const loadCategories = async () => {
  try {
    const res = await getScenicCategoryList()
    categories.value = res || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadScenicList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      keyword: query.keyword,
      categoryId: query.categoryId,
      status: query.status
    }
    const res = await getAdminScenicList(params)
    scenicList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('加载景点列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  loadScenicList()
}

const handleReset = () => {
  query.keyword = ''
  query.categoryId = null
  query.status = null
  handleSearch()
}

const handleSizeChange = (size) => {
  query.pageSize = size
  loadScenicList()
}

const handlePageChange = (page) => {
  query.pageNum = page
  loadScenicList()
}

const handleEdit = (row) => {
  router.push(`/admin/scenic/${row.id}/edit`)
}

const handleSetRecommend = (row) => {
  recommendForm.id = row.id
  recommendForm.level = row.recommendLevel || 0
  recommendDialogVisible.value = true
}

const submitRecommend = async () => {
  try {
    await setRecommendLevel(recommendForm.id, recommendForm.level)
    ElMessage.success('设置成功')
    recommendDialogVisible.value = false
    loadScenicList()
  } catch (error) {
    console.error('设置推荐等级失败', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除景点"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteScenic(row.id)
      ElMessage.success('删除成功')
      loadScenicList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateScenic(row.id, { status: row.status })
    ElMessage.success(row.status === 1 ? '已上架' : '已下架')
  } catch (error) {
    console.error('更新状态失败', error)
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
  }
}

onMounted(() => {
  loadCategories()
  loadScenicList()
})
</script>

<style scoped>
.admin-scenic-page {
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

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
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

.recommend-stars {
  display: flex;
  align-items: center;
  gap: 4px;
}

.recommend-level {
  font-size: 12px;
  color: #f56c6c;
  font-weight: 500;
}

.no-rating {
  font-size: 12px;
  color: #999;
}
</style>
