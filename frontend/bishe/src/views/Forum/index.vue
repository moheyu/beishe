<template>
  <div class="forum-page">
    <div class="page-header">
      <h1>旅游论坛</h1>
      <p>分享您的旅行故事，交流旅游心得</p>
    </div>
    
    <div class="forum-content">
      <el-row :gutter="20">
        <el-col :xs="24" :md="16">
          <el-card class="post-list" v-loading="loading">
            <div v-for="post in postList" :key="post.id" class="post-item" @click="viewPost(post)">
              <div class="post-header">
                <el-avatar :size="40" :src="post.avatar ? getImageUrl(post.avatar) : ''" />
                <div class="post-meta">
                  <span class="author">{{ post.username }}</span>
                  <span class="time">{{ formatTime(post.createTime) }}</span>
                </div>
                <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
                <el-tag v-if="post.isEssence" type="warning" size="small">精华</el-tag>
              </div>
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-summary">{{ post.summary || (post.content ? post.content.substring(0, 100) + '...' : '') }}</p>
              <div class="post-footer">
                <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ post.replyCount }}</span>
                <span><el-icon><Star /></el-icon> {{ post.likeCount || 0 }}</span>
              </div>
            </div>
            <div v-if="!loading && postList.length === 0" class="empty-state">
              暂无帖子
            </div>
          </el-card>
          
          <div class="pagination-wrapper" v-if="total > 0">
            <el-pagination
              v-model:current-page="query.pageNum"
              v-model:page-size="query.pageSize"
              :total="total"
              layout="prev, pager, next"
              @current-change="handlePageChange"
            />
          </div>
        </el-col>
        
        <el-col :xs="24" :md="8">
          <el-card class="forum-sidebar">
            <template #header>
              <span>热门话题</span>
            </template>
            <div v-for="(topic, index) in hotTopics" :key="index" class="hot-topic">
              <span class="rank">{{ index + 1 }}</span>
              <span class="title">{{ topic.title }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'
import { getForumPostList, getRecommendForumPost } from '@/api/forum'
import { getImageUrl } from '@/utils/image'

const router = useRouter()
const postList = ref([])
const hotTopics = ref([])
const loading = ref(false)
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10
})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString()
}

const loadPosts = async () => {
  loading.value = true
  try {
    const res = await getForumPostList(query)
    postList.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('加载帖子失败', error)
  } finally {
    loading.value = false
  }
}

const loadHotTopics = async () => {
  try {
    const res = await getRecommendForumPost({ limit: 5 })
    hotTopics.value = res || []
  } catch (error) {
    console.error('加载热门话题失败', error)
  }
}

const viewPost = (post) => {
  router.push(`/forum/${post.id}`)
}

const handlePageChange = (page) => {
  query.pageNum = page
  loadPosts()
}

onMounted(() => {
  loadPosts()
  loadHotTopics()
})
</script>

<style scoped>
.forum-page {
  max-width: 1200px;
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

.post-list {
  margin-bottom: 20px;
}

.post-item {
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background 0.3s;
}

.post-item:last-child {
  border-bottom: none;
}

.post-item:hover {
  background: #f5f7fa;
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.post-meta {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.author {
  font-weight: 500;
  color: #333;
}

.time {
  font-size: 12px;
  color: #999;
}

.post-title {
  font-size: 18px;
  color: #333;
  margin: 0 0 8px;
}

.post-summary {
  color: #666;
  font-size: 14px;
  margin: 0 0 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.post-footer {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
}

.post-footer span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.hot-topic {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.hot-topic:last-child {
  border-bottom: none;
}

.rank {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 4px;
  margin-right: 12px;
  font-size: 12px;
  color: #666;
}

.hot-topic:nth-child(1) .rank {
  background: #fef0f0;
  color: #f56c6c;
}

.hot-topic:nth-child(2) .rank {
  background: #fdf6ec;
  color: #e6a23c;
}

.hot-topic:nth-child(3) .rank {
  background: #f0f9ff;
  color: #409eff;
}

.title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #333;
}
</style>
