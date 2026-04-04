<template>
  <div class="scenic-detail-page" v-if="scenic">
    <div class="detail-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/scenic/list' }">景点</el-breadcrumb-item>
        <el-breadcrumb-item>{{ scenic.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <div class="detail-content">
      <div class="detail-main">
        <div class="image-gallery">
          <el-image :src="getImageUrl(scenic.images && scenic.images.length > 0 ? scenic.images[0] : '')" fit="cover" class="main-image" />
        </div>
        
        <div class="info-section">
          <h1>{{ scenic.name }}</h1>
          <div class="info-meta">
            <span class="location">
              <el-icon><Location /></el-icon>
              {{ scenic.location }}
            </span>
            <span class="category">{{ scenic.categoryName }}</span>
          </div>
          
          <div class="rating-section">
            <div v-if="scenic.averageScore > 0">
              <el-rate :model-value="scenic.averageScore" :max="5" disabled show-score />
              <span class="rating-text">{{ scenic.averageScore.toFixed(1) }}分</span>
              <span class="rating-count">({{ scenic.ratingCount }}人评分)</span>
            </div>
            <span v-else class="no-rating">暂无评分</span>
            
            <div v-if="userStore.isLoggedIn" class="user-rating">
              <span>我要评分：</span>
              <el-rate 
                v-model="userScore" 
                :max="5" 
                show-score 
                :disabled="userScore > 0"
                @change="handleRate"
              />
              <span v-if="userScore > 0" class="rated-text">已评分</span>
            </div>
          </div>
          
          <div class="price-section">
            <span class="price-label">门票价格</span>
            <span class="price-value">¥{{ scenic.price }}</span>
          </div>
          
          <div class="action-buttons">
            <el-button type="primary" size="large" @click="handleCollect">
              <el-icon><Star /></el-icon>
              {{ isCollected ? '已收藏' : '收藏' }}
            </el-button>
            <el-button size="large" @click="handleShare">
              <el-icon><Share /></el-icon>
              分享
            </el-button>
          </div>
        </div>
      </div>
      
      <div class="detail-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="景点介绍" name="intro">
            <div class="intro-content">
              <p>{{ scenic.description }}</p>
              <div v-if="scenic.detail" class="detail-text" v-html="scenic.detail"></div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="交通指南" name="traffic">
            <div class="traffic-content">
              <p>{{ scenic.trafficInfo || '暂无交通信息' }}</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="开放时间" name="openTime">
            <div class="open-time-content">
              <p>{{ scenic.openTime || '暂无开放时间信息' }}</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="用户评论" name="comments">
            <div class="comments-section">
              <div class="comment-input">
                <el-input
                  v-model="commentContent"
                  type="textarea"
                  :rows="3"
                  placeholder="发表您的评论..."
                />
                <el-button type="primary" @click="handleAddComment">发表评论</el-button>
              </div>
              <div v-if="comments.length > 0" class="comment-list">
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                  <div class="comment-header">
                    <el-avatar :size="32" :src="comment.avatar || ''" />
                    <div class="comment-user">
                      <span class="username">{{ comment.username }}</span>
                      <span class="time">{{ formatTime(comment.createTime) }}</span>
                    </div>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                </div>
              </div>
              <el-empty v-else description="暂无评论" />
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
import { Location, Star, Share } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getScenicDetail } from '@/api/scenic'
import { getScenicComments, addComment } from '@/api/comment'
import { getImageUrl } from '@/utils/image'
import { addScenicCollection, removeScenicCollection } from '@/api/collection'
import { addViewRecord } from '@/api/viewRecord'
import { rateScenic } from '@/api/rating'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const scenic = ref(null)
const activeTab = ref('intro')
const isCollected = ref(false)
const comments = ref([])
const commentContent = ref('')
const userScore = ref(0)
const userStore = useUserStore()

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

const loadScenicDetail = async () => {
  try {
    const id = route.params.id
    const res = await getScenicDetail(id)
    scenic.value = res
    loadComments()
    // 加载用户评分
    if (userStore.isLoggedIn) {
      try {
        const ratingRes = await (await import('@/api/rating')).getScenicRating(id)
        if (ratingRes && ratingRes.userScore) {
          userScore.value = ratingRes.userScore
        }
      } catch (e) {
        // 忽略评分获取错误
      }
    }
    // 记录浏览历史
    try {
      await addViewRecord(id)
    } catch (e) {
      // 忽略浏览记录错误，不影响主流程
    }
  } catch (error) {
    console.error('加载景点详情失败', error)
    ElMessage.error('加载景点详情失败')
  }
}

const loadComments = async () => {
  try {
    const id = route.params.id
    const res = await getScenicComments(id)
    comments.value = res || []
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

const handleCollect = async () => {
  const id = route.params.id
  try {
    if (isCollected.value) {
      await removeScenicCollection(id)
      ElMessage.success('取消收藏成功')
    } else {
      await addScenicCollection(id)
      ElMessage.success('收藏成功')
    }
    isCollected.value = !isCollected.value
  } catch (error) {
    console.error('操作收藏失败', error)
    ElMessage.error('操作收藏失败，请先登录')
  }
}

const handleRate = async (score) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    await rateScenic(route.params.id, score)
    ElMessage.success('评分成功')
    loadScenicDetail()
  } catch (error) {
    console.error('评分失败', error)
    ElMessage.error('评分失败')
  }
}

const handleShare = () => {
  ElMessage.success('链接已复制到剪贴板')
}

const handleAddComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  try {
    const id = route.params.id
    await addComment({
      scenicId: id,
      content: commentContent.value
    })
    ElMessage.success('评论成功')
    commentContent.value = ''
    loadComments()
  } catch (error) {
    console.error('添加评论失败', error)
    ElMessage.error('添加评论失败')
  }
}

onMounted(() => {
  loadScenicDetail()
})
</script>

<style scoped>
.scenic-detail-page {
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
  margin-bottom: 16px;
  color: #666;
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
}

.category {
  background: #ecf5ff;
  color: #409eff;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 13px;
}

.rating-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.rating-text {
  color: #ff9900;
  font-size: 18px;
  font-weight: bold;
}

.price-section {
  margin-bottom: 24px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.price-label {
  color: #666;
  margin-right: 12px;
}

.price-value {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
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

.intro-content,
.traffic-content,
.open-time-content {
  line-height: 1.8;
  color: #333;
}

.intro-content p {
  margin: 0 0 16px;
}

.detail-text {
  margin-top: 20px;
}

.comments-section {
  padding: 20px 0;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.comment-user {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 500;
  color: #333;
}

.time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  line-height: 1.6;
  color: #333;
}

.loading-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
}

.no-rating {
  color: #999;
  font-size: 16px;
}

.rating-count {
  margin-left: 8px;
  color: #999;
  font-size: 14px;
}

.user-rating {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.rated-text {
  color: #67c23a;
  font-size: 14px;
}

@media (max-width: 768px) {
  .detail-main {
    grid-template-columns: 1fr;
  }
}
</style>
