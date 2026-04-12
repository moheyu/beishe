<template>
  <div class="forum-detail-page">
    <el-card class="post-card" v-loading="loading">
      <h1 class="post-title">{{ post.title }}</h1>
      <div class="post-meta">
        <el-avatar :size="40" :src="post.avatar ? getImageUrl(post.avatar) : ''" />
        <div class="meta-info">
          <span class="author">{{ post.username }}</span>
          <span class="time">{{ formatTime(post.createTime) }}</span>
        </div>
      </div>
      <div class="post-content">
        {{ post.content }}
      </div>
    </el-card>
    
    <el-card class="comments-section">
      <template #header>
        <span>帖子回复</span>
      </template>
      <div class="comment-input">
        <el-input
          v-model="replyContent"
          type="textarea"
          :rows="3"
          placeholder="发表您的回复..."
        />
        <el-button type="primary" @click="handleAddReply">发表回复</el-button>
      </div>
      <div v-if="replies.length > 0" class="reply-list">
        <div v-for="reply in replies" :key="reply.id" class="reply-item">
          <div class="reply-header">
            <el-avatar :size="32" :src="reply.avatar ? getImageUrl(reply.avatar) : ''" />
            <div class="reply-user">
              <span class="username">{{ reply.username }}</span>
              <span class="time">{{ formatTime(reply.createTime) }}</span>
            </div>
          </div>
          <div class="reply-content">{{ reply.content }}</div>
        </div>
      </div>
      <el-empty v-else description="暂无回复" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getForumPostDetail, getForumReplyList, addForumReply } from '@/api/forum'
import { getImageUrl } from '@/utils/image'
import { ElMessage } from 'element-plus'

const route = useRoute()
const post = ref({})
const replies = ref([])
const loading = ref(false)
const replyContent = ref('')

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadPostDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const res = await getForumPostDetail(id)
    post.value = res || {}
    loadReplies()
  } catch (error) {
    console.error('加载帖子详情失败', error)
  } finally {
    loading.value = false
  }
}

const loadReplies = async () => {
  try {
    const id = route.params.id
    const res = await getForumReplyList(id, { pageNum: 1, pageSize: 100 })
    replies.value = res?.records || []
  } catch (error) {
    console.error('加载回复失败', error)
  }
}

const handleAddReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  
  try {
    const id = route.params.id
    await addForumReply(id, {
      content: replyContent.value
    })
    ElMessage.success('回复成功')
    replyContent.value = ''
    loadReplies()
  } catch (error) {
    console.error('添加回复失败', error)
    ElMessage.error('添加回复失败')
  }
}

onMounted(() => {
  loadPostDetail()
})
</script>

<style scoped>
.forum-detail-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.post-card {
  margin-bottom: 20px;
}

.post-title {
  font-size: 24px;
  margin: 0 0 20px;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.meta-info {
  display: flex;
  flex-direction: column;
}

.author {
  font-weight: 500;
}

.time {
  font-size: 12px;
  color: #999;
}

.post-content {
  line-height: 1.8;
  color: #333;
}

.comments-section {
  margin-top: 20px;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.reply-list {
  margin-top: 20px;
}

.reply-item {
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.reply-user {
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

.reply-content {
  line-height: 1.6;
  color: #333;
}
</style>
