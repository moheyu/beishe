<template>
  <div class="comments-page">
    <h2>我的评论</h2>
    <el-card v-for="comment in comments" :key="comment.id" class="comment-item">
      <div class="comment-header">
        <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
        <el-button type="danger" size="small" @click="handleDeleteComment(comment.id)">删除</el-button>
      </div>
      <div class="comment-content">
        {{ comment.content }}
      </div>
      <div class="comment-target" v-if="comment.targetName">
        <span class="target-label">评论对象：</span>
        <span class="target-name">{{ comment.targetName }}</span>
      </div>
    </el-card>
    <el-empty v-if="!comments.length" description="暂无评论" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserComments, removeComment } from '@/api/comment'
import { ElMessage } from 'element-plus'

const comments = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadComments = async () => {
  try {
    const res = await getUserComments()
    comments.value = res || []
  } catch (error) {
    console.error('加载评论失败', error)
    ElMessage.error('加载评论失败')
  }
}

const handleDeleteComment = async (id) => {
  try {
    await removeComment(id)
    ElMessage.success('删除评论成功')
    loadComments()
  } catch (error) {
    console.error('删除评论失败', error)
    ElMessage.error('删除评论失败')
  }
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comments-page h2 {
  margin: 0 0 20px;
}

.comment-item {
  margin-bottom: 16px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.comment-time {
  color: #999;
  font-size: 14px;
}

.comment-content {
  margin-bottom: 12px;
  line-height: 1.6;
  color: #333;
}

.comment-target {
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
  font-size: 14px;
  color: #666;
}

.target-label {
  font-weight: 500;
}

.target-name {
  color: #409eff;
}
</style>
