<template>
  <div class="scenic-list-page">
    <div class="page-header">
      <h1>景点列表</h1>
      <p>探索世界各地的精彩景点</p>
    </div>
    
    <div class="filter-section">
      <el-input
        v-model="query.keyword"
        placeholder="搜索景点名称"
        class="search-input"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
      
      <el-select v-model="query.categoryId" placeholder="全部分类" clearable @change="handleSearch">
        <el-option
          v-for="item in categories"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      
      <el-select v-model="query.sort" placeholder="默认排序" @change="handleSearch">
        <el-option label="默认排序" value="" />
        <el-option label="评分最高" value="rating" />
        <el-option label="价格最低" value="price_asc" />
        <el-option label="价格最高" value="price_desc" />
      </el-select>
    </div>
    
    <div class="scenic-grid">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in scenicList" :key="item.id">
          <div class="scenic-card" @click="$router.push(`/scenic/${item.id}`)">
            <div class="card-image-wrapper">
              <el-image :src="getImageUrl(item.images && item.images.length > 0 ? item.images[0] : '')" fit="cover" class="card-image" />
              <div class="card-badge" v-if="item.recommendLevel > 0">
                推荐
              </div>
            </div>
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="card-location">
                <el-icon><Location /></el-icon>
                {{ item.location }}
              </p>
              <p class="card-desc">{{ item.description }}</p>
              <div class="card-footer">
                <span class="price">¥{{ item.price }}</span>
                <div class="card-rating">
                  <el-rate :model-value="item.score" disabled />
                  <span>{{ item.score }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        layout="prev, pager, next, jumper"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search, Location } from '@element-plus/icons-vue'
import { getScenicList } from '@/api/scenic'
import { getScenicCategoryList } from '@/api/scenicCategory'
import { getImageUrl } from '@/utils/image'

const route = useRoute()
const scenicList = ref([])
const categories = ref([])
const total = ref(0)
const loading = ref(false)

const query = reactive({
  pageNum: 1,
  pageSize: 12,
  keyword: route.query.keyword || '',
  categoryId: null,
  sort: ''
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
      name: query.keyword,
      categoryId: query.categoryId
    }
    const res = await getScenicList(params)
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

const handleSizeChange = (size) => {
  query.pageSize = size
  loadScenicList()
}

const handlePageChange = (page) => {
  query.pageNum = page
  loadScenicList()
}

onMounted(() => {
  loadCategories()
  loadScenicList()
})
</script>

<style scoped>
.scenic-list-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 32px;
  color: #333;
  margin: 0 0 8px;
}

.page-header p {
  color: #666;
  margin: 0;
}

.filter-section {
  display: flex;
  gap: 16px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.search-input {
  width: 300px;
}

.scenic-grid {
  margin-bottom: 30px;
}

.scenic-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.scenic-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-image-wrapper {
  position: relative;
  height: 180px;
}

.card-image {
  width: 100%;
  height: 100%;
}

.card-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #f56c6c;
  color: #fff;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
}

.card-content {
  padding: 16px;
}

.card-content h3 {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-location {
  color: #666;
  font-size: 13px;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
  height: 42px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.card-rating {
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-rating span {
  color: #ff9900;
  font-size: 14px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .scenic-list-page {
    padding: 10px;
  }
  
  .page-header h1 {
    font-size: 24px;
  }
  
  .filter-section {
    flex-direction: column;
    gap: 10px;
  }
  
  .search-input {
    width: 100%;
  }
  
  .scenic-grid :deep(.el-col) {
    padding: 0 5px;
  }
}
</style>
