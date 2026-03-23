<template>
  <div class="route-detail-page" v-if="routeData">
    <div class="detail-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/route/list' }">路线</el-breadcrumb-item>
        <el-breadcrumb-item>{{ routeData.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="detail-content">
      <div class="detail-main">
        <div class="image-gallery">
          <el-image :src="getImageUrl(routeData.coverImage)" fit="cover" class="main-image" />
        </div>
        
        <div class="info-section">
          <h1>{{ routeData.name }}</h1>
          <div class="info-meta">
            <span class="category">{{ routeData.categoryName }}</span>
            <span class="duration">
              <el-icon><Calendar /></el-icon>
              {{ routeData.duration }}天
            </span>
          </div>
          
          <div class="route-info">
            <div class="info-item">
              <span class="label">最佳季节</span>
              <span class="value">{{ routeData.season || '全年' }}</span>
            </div>
            <div class="info-item">
              <span class="label">主题类型</span>
              <span class="value">{{ routeData.theme || '综合' }}</span>
            </div>
            <div class="info-item">
              <span class="label">途经景点</span>
              <span class="value">{{ routeData.scenicCount || 0 }}个</span>
            </div>
          </div>
          
          <div class="price-section">
            <span class="price-label">预计预算</span>
            <span class="price-value">¥{{ routeData.budget }}</span>
            <span class="price-unit">/人</span>
          </div>
          
          <div class="action-buttons">
            <el-button type="primary" size="large" @click="handleCollect">
              <el-icon><Star /></el-icon>
              {{ isCollected ? '已收藏' : '收藏路线' }}
            </el-button>
          </div>
        </div>
      </div>
      
      <div class="detail-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="路线介绍" name="intro">
            <div class="intro-content">
              <p>{{ route.description }}</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="途经景点" name="scenics">
            <div class="scenic-timeline">
              <el-timeline>
                <el-timeline-item
                  v-for="(scenic, index) in routeData.scenicList"
                  :key="scenic.id"
                  :type="index === 0 ? 'primary' : ''"
                  :hollow="index !== 0"
                >
                  <div class="timeline-item" @click="$router.push(`/scenic/${scenic.id}`)">
                    <el-image :src="getImageUrl(scenic.coverImage)" fit="cover" class="timeline-image" />
                    <div class="timeline-content">
                      <h4>第{{ index + 1 }}天: {{ scenic.name }}</h4>
                      <p>{{ scenic.description }}</p>
                    </div>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
  
  <div v-else class="loading-wrapper">
    <el-skeleton :rows="10" animated />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Calendar, Star } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getRouteFullDetail } from '@/api/route'
import { getImageUrl } from '@/utils/image'

const route = useRoute()
const routeData = ref(null)
const activeTab = ref('intro')
const isCollected = ref(false)

const loadRouteDetail = async () => {
  try {
    const id = route.params.id
    const res = await getRouteFullDetail(id)
    routeData.value = res
  } catch (error) {
    console.error('加载路线详情失败', error)
    ElMessage.error('加载路线详情失败')
  }
}

const handleCollect = () => {
  isCollected.value = !isCollected.value
  ElMessage.success(isCollected.value ? '收藏成功' : '取消收藏')
}

onMounted(() => {
  loadRouteDetail()
})
</script>

<style scoped>
.route-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.detail-header {
  margin-bottom: 20px;
}

.detail-content {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.detail-main {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  padding: 30px;
}

.image-gallery {
  border-radius: 8px;
  overflow: hidden;
}

.main-image {
  width: 100%;
  height: 400px;
  border-radius: 8px;
}

.info-section h1 {
  font-size: 28px;
  color: #333;
  margin: 0 0 16px;
}

.info-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.category {
  background: #ecf5ff;
  color: #409eff;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 13px;
}

.duration {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
}

.route-info {
  margin-bottom: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #e4e7ed;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  color: #666;
}

.value {
  color: #333;
  font-weight: 500;
}

.price-section {
  margin-bottom: 24px;
  padding: 16px;
  background: #fef0f0;
  border-radius: 8px;
}

.price-label {
  color: #666;
  margin-right: 12px;
}

.price-value {
  color: #f56c6c;
  font-size: 32px;
  font-weight: bold;
}

.price-unit {
  color: #999;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.action-buttons .el-button {
  flex: 1;
}

.detail-tabs {
  padding: 0 30px 30px;
}

.intro-content {
  line-height: 1.8;
  color: #333;
}

.scenic-timeline {
  padding: 20px;
}

.timeline-item {
  display: flex;
  gap: 16px;
  cursor: pointer;
  padding: 12px;
  border-radius: 8px;
  transition: background 0.3s;
}

.timeline-item:hover {
  background: #f5f7fa;
}

.timeline-image {
  width: 120px;
  height: 80px;
  border-radius: 4px;
  flex-shrink: 0;
}

.timeline-content h4 {
  margin: 0 0 8px;
  color: #333;
}

.timeline-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.loading-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
}

@media (max-width: 768px) {
  .detail-main {
    grid-template-columns: 1fr;
  }
}
</style>
