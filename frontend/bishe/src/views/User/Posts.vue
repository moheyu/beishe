<template>
  <div class="posts-page">
    <div class="page-header">
      <h2>我的帖子</h2>
      <el-button type="primary" @click="handleCreatePost">发布帖子</el-button>
    </div>
    <el-card v-for="post in posts" :key="post.id" class="post-item">
      <div class="post-header">
        <h3 class="post-title">{{ post.title }}</h3>
        <div class="post-actions">
          <el-button type="primary" size="small" @click="handleEditPost(post)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDeletePost(post.id)">删除</el-button>
        </div>
      </div>
      <p class="post-content">{{ post.content ? post.content.substring(0, 100) + '...' : '暂无内容' }}</p>
      <div class="post-footer">
        <span class="post-time">{{ formatTime(post.createTime) }}</span>
        <div class="post-stats">
          <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
          <span><el-icon><ChatDotRound /></el-icon> {{ post.replyCount }}</span>
        </div>
      </div>
    </el-card>
    <el-empty v-if="!posts.length" description="暂无帖子" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { View, ChatDotRound } from '@element-plus/icons-vue'
import { getForumPostList, deleteForumPost } from '@/api/forum'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const posts = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadPosts = async () => {
  try {
    const userId = userStore.userInfo?.id
    const res = await getForumPostList({ pageNum: 1, pageSize: 100, userId })
    posts.value = res?.records || []
  } catch (error) {
    console.error('加载帖子失败', error)
    ElMessage.error('加载帖子失败')
  }
}

const handleCreatePost = () => {
  router.push('/forum/create')
}

const handleEditPost = (post) => {
  router.push(`/forum/${post.id}/edit`)
}

const handleDeletePost = (id) => {
  ElMessageBox.confirm('确定要删除这篇帖子吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteForumPost(id)
      ElMessage.success('删除帖子成功')
      loadPosts()
    } catch (error) {
      console.error('删除帖子失败', error)
      ElMessage.error('删除帖子失败')
    }
  })
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.posts-page h2 {
  margin: 0 0 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.post-item {
  margin-bottom: 16px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.post-title {
  margin: 0;
  font-size: 18px;
  flex: 1;
}

.post-actions {
  display: flex;
  gap: 8px;
}

.post-content {
  margin: 0 0 12px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
  font-size: 14px;
  color: #999;
}

.post-stats {
  display: flex;
  gap: 16px;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
