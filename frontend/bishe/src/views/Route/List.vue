<template>
  <div class="route-list-page">
    <div class="page-header">
      <h1>路线列表</h1>
      <p>精选旅游路线，开启您的精彩旅程</p>
    </div>
    
    <div class="filter-section">
      <el-input
        v-model="query.keyword"
        placeholder="搜索路线名称"
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
        <el-option label="天数最短" value="duration" />
        <el-option label="预算最低" value="budget" />
      </el-select>
    </div>
    
    <div class="route-grid">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="item in routeList" :key="item.id">
          <div class="route-card" @click="$router.push(`/route/${item.id}`)">
            <div class="card-image-wrapper">
              <el-image :src="getImageUrl(item.coverImage)" fit="cover" class="card-image" />
              <div class="card-duration">
                <el-icon><Calendar /></el-icon>
                {{ item.duration }}天
              </div>
            </div>
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="card-desc">{{ item.description }}</p>
              <div class="route-tags">
                <el-tag v-if="item.season" size="small" type="success">{{ item.season }}</el-tag>
                <el-tag v-if="item.theme" size="small" type="warning">{{ item.theme }}</el-tag>
              </div>
              <div class="card-footer">
                <span class="budget">预算: ¥{{ item.budget }}</span>
                <span class="scenic-count">{{ item.scenicCount }}个景点</span>
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
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Calendar } from '@element-plus/icons-vue'
import { getRouteList } from '@/api/route'
import { getRouteCategoryList } from '@/api/routeCategory'
import { getImageUrl } from '@/utils/image'

const routeList = ref([])
const categories = ref([])
const total = ref(0)
const loading = ref(false)

const query = reactive({
  pageNum: 1,
  pageSize: 9,
  keyword: '',
  categoryId: null,
  sort: ''
})

const loadCategories = async () => {
  try {
    const res = await getRouteCategoryList()
    categories.value = res || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadRouteList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      name: query.keyword,
      categoryId: query.categoryId
    }
    const res = await getRouteList(params)
    routeList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('加载路线列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  loadRouteList()
}

const handleSizeChange = (size) => {
  query.pageSize = size
  loadRouteList()
}

const handlePageChange = (page) => {
  query.pageNum = page
  loadRouteList()
}

onMounted(() => {
  loadCategories()
  loadRouteList()
})
</script>

<style scoped>
.route-list-page {
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

.route-grid {
  margin-bottom: 30px;
}

.route-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.route-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-image-wrapper {
  position: relative;
  height: 200px;
}

.card-image {
  width: 100%;
  height: 100%;
}

.card-duration {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
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

.route-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.budget {
  color: #f56c6c;
  font-size: 16px;
  font-weight: bold;
}

.scenic-count {
  color: #666;
  font-size: 13px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
}
</style>
