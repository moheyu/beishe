<template>
  <div class="scenic-container">
    <!-- 页面标题区域 -->
 

    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-card class="filter-card">
        <el-form :inline="true" :model="queryParams" class="filter-form">
          <el-form-item label="景点名称">
            <el-input v-model="queryParams.name" placeholder="请输入景点名称" clearable prefix-icon="el-icon-search"></el-input>
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
              <el-option label="全部" value=""></el-option>
              <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 景点列表区域 -->
    <div class="scenic-list" v-loading="loading">
      <div v-for="item in scenicList" :key="item.id" class="scenic-item" @click="goToDetail(item.id)">
        <div class="card-image">
          <img 
            :src="item.images && item.images.length > 0 ? item.images[0] : defaultImage" 
            :alt="item.name"
          >
          <div class="card-overlay">
            <div class="card-tag" v-if="item.categoryName">{{ item.categoryName }}</div>
          </div>
        </div>
        <div class="card-content">
          <h3 class="card-title">{{ item.name }}</h3>
          <p class="card-description">{{ item.description }}</p>
          <div class="card-footer">
            <span class="card-price">¥{{ item.price }}/人</span>
            <span class="card-views">
              <i class="el-icon-view"></i>
              {{ item.viewCount }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-if="scenicList.length === 0 && !loading" description="暂无景点数据" class="empty-state"></el-empty>

    <!-- 分页区域 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        background
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getScenicList } from '@/api/scenic'

export default {
  name: 'Scenic',
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        categoryId: ''
      },
      scenicList: [],
      total: 0,
      loading: false,
      categories: [
        { id: 1, name: '自然风光' },
        { id: 2, name: '人文景观' },
        { id: 3, name: '主题乐园' },
        { id: 4, name: '历史古迹' }
      ],
      defaultImage: 'https://via.placeholder.com/300x200?text=No+Image'
    }
  },
  created() {
    this.loadScenicList()
  },
  methods: {
    loadScenicList() {
      console.log('loadScenicList 被执行了')
      this.loading = true
      getScenicList(this.queryParams).then(res => {
        console.log('原始返回数据:', res.data.records) // 看原始数据
        
        this.scenicList = res.data.records.map(item => {
          console.log('处理前的item:', item) // 看每个item原始值
          
          // 解析 images 字符串为数组
          if (item.images) {
            if (typeof item.images === 'string') {
              try {
                item.images = JSON.parse(item.images)
              } catch (e) {
                console.error('解析失败', e)
                item.images = []
              }
            }
            if (!Array.isArray(item.images)) {
              item.images = []
            }
          } else {
            item.images = []
          }
          
          console.log('处理后的item.images:', item.images) // 看处理结果
          return item
        })
        
        console.log('最终scenicList:', this.scenicList) // 看最终赋值结果
        this.total = res.data.total
      }).finally(() => {
        this.loading = false
      })
    },
    handleSearch() {
      this.queryParams.pageNum = 1
      this.loadScenicList()
    },
    handleReset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        name: '',
        categoryId: ''
      }
      this.loadScenicList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.loadScenicList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.loadScenicList()
    },
    goToDetail(id) {
      this.$router.push(`/scenic/${id}`)
    }
  }
}
</script>

<style scoped>
/* ===== 现代化自然风设计 ===== */

/* 全局容器 */
.scenic-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px;
  background: linear-gradient(180deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  box-sizing: border-box;
}

/* 页面标题区域 */
.page-header {
  text-align: center;
  margin-bottom: 60px;
  animation: fadeInDown 0.8s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 48px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 16px;
  letter-spacing: -1px;
}

.page-subtitle {
  font-size: 20px;
  color: #7f8c8d;
  margin: 0;
  font-weight: 300;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 40px;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.filter-card {
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  border: none;
  background: white;
  transition: all 0.3s ease;
}

.filter-card:hover {
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
  justify-content: center;
  padding: 10px 0;
}

.filter-form .el-form-item {
  margin-bottom: 0;
}

/* 景点列表区域 */
.scenic-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.scenic-item {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: relative;
}

.scenic-item:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.card-image {
  position: relative;
  height: 240px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.scenic-item:hover .card-image img {
  transform: scale(1.15);
}

.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.3) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
}

.scenic-item:hover .card-overlay {
  opacity: 1;
}

.card-tag {
  position: absolute;
  top: 16px;
  right: 16px;
  background: rgba(255, 255, 255, 0.95);
  color: #667eea;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transform: translateY(-10px);
  opacity: 0;
  transition: all 0.4s ease;
}

.scenic-item:hover .card-tag {
  transform: translateY(0);
  opacity: 1;
}

.card-content {
  padding: 24px;
}

.card-title {
  font-size: 22px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 12px;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.scenic-item:hover .card-title {
  color: #667eea;
}

.card-description {
  font-size: 15px;
  color: #7f8c8d;
  margin: 0 0 20px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 48px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-price {
  font-size: 24px;
  color: #667eea;
  font-weight: 700;
  transition: transform 0.3s ease;
}

.scenic-item:hover .card-price {
  transform: scale(1.1);
}

.card-views {
  font-size: 14px;
  color: #95a5a6;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: color 0.3s ease;
}

.scenic-item:hover .card-views {
  color: #667eea;
}

.card-views i {
  font-size: 16px;
}

/* 空状态 */
.empty-state {
  margin: 60px 0;
  animation: fadeInUp 0.8s ease-out 0.6s both;
}

/* 分页区域 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  animation: fadeInUp 0.8s ease-out 0.8s both;
}

.pagination .el-pagination {
  padding: 20px 40px;
  background: white;
  border-radius: 50px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .page-title {
    font-size: 40px;
  }
  
  .scenic-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .scenic-container {
    padding: 20px;
  }
  
  .page-header {
    margin-bottom: 40px;
  }
  
  .page-title {
    font-size: 32px;
  }
  
  .page-subtitle {
    font-size: 16px;
  }
  
  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-form .el-form-item {
    width: 100%;
  }
  
  .scenic-list {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .pagination .el-pagination {
    padding: 15px 20px;
  }
}
</style>
