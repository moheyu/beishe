<template>
  <div class="detail-container" v-loading="loading">
    <div v-if="scenicDetail">
      <div class="detail-header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/scenic' }">景点</el-breadcrumb-item>
          <el-breadcrumb-item>{{ scenicDetail.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="detail-content">
        <div class="detail-left">
          <div class="image-gallery">
            <img :src="scenicDetail.imageUrl || 'https://via.placeholder.com/800x400'" :alt="scenicDetail.name" class="main-image">
          </div>

          <div class="info-section">
            <h2>景点介绍</h2>
            <p class="description">{{ scenicDetail.description }}</p>
          </div>

          <div class="info-section">
            <h2>景点信息</h2>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="景点名称">{{ scenicDetail.name }}</el-descriptions-item>
              <el-descriptions-item label="价格">¥{{ scenicDetail.price }}/人</el-descriptions-item>
              <el-descriptions-item label="分类">{{ scenicDetail.categoryName }}</el-descriptions-item>
              <el-descriptions-item label="浏览量">{{ scenicDetail.viewCount }}</el-descriptions-item>
              <el-descriptions-item label="地址" :span="2">{{ scenicDetail.address }}</el-descriptions-item>
              <el-descriptions-item label="开放时间" :span="2">{{ scenicDetail.openTime }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="info-section">
            <h2>景点标签</h2>
            <div class="tags">
              <el-tag v-for="tag in scenicDetail.tags" :key="tag.id" type="success" size="medium">
                {{ tag.name }}
              </el-tag>
            </div>
          </div>

          <div class="info-section">
            <h2>用户评论</h2>
            <div class="comment-input">
              <el-input
                v-model="commentContent"
                type="textarea"
                :rows="4"
                placeholder="写下您的评论..."
                maxlength="500"
                show-word-limit
              ></el-input>
              <el-button type="primary" @click="submitComment" :loading="commentLoading" class="submit-btn">
                发表评论
              </el-button>
            </div>

            <div class="comment-list" v-loading="commentLoading">
              <div v-for="comment in commentList" :key="comment.id" class="comment-item">
                <div class="comment-header">
                  <el-avatar :src="comment.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" :size="40"></el-avatar>
                  <div class="comment-user">
                    <span class="username">{{ comment.username }}</span>
                    <span class="time">{{ comment.createTime }}</span>
                  </div>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
              </div>

              <el-empty v-if="commentList.length === 0" description="暂无评论，快来发表第一条评论吧！"></el-empty>
            </div>
          </div>
        </div>

        <div class="detail-right">
          <el-card class="action-card">
            <div class="price-section">
              <span class="price">¥{{ scenicDetail.price }}</span>
              <span class="unit">/人</span>
            </div>
            <el-button type="primary" size="large" @click="toggleCollection" class="collect-btn">
              <i :class="isCollected ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              {{ isCollected ? '已收藏' : '收藏' }}
            </el-button>
          </el-card>

          <el-card class="info-card">
            <div class="info-item">
              <i class="el-icon-location-outline"></i>
              <span>{{ scenicDetail.address }}</span>
            </div>
            <div class="info-item">
              <i class="el-icon-time"></i>
              <span>{{ scenicDetail.openTime }}</span>
            </div>
            <div class="info-item">
              <i class="el-icon-view"></i>
              <span>浏览量：{{ scenicDetail.viewCount }}</span>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getScenicDetail } from '@/api/scenic'
import { getCommentsByScenicId, addComment } from '@/api/comment'
import { addCollection, deleteCollection, getCollectionList } from '@/api/collection'

export default {
  name: 'ScenicDetail',
  data() {
    return {
      scenicDetail: null,
      commentList: [],
      commentContent: '',
      loading: false,
      commentLoading: false,
      isCollected: false
    }
  },
  created() {
    this.loadScenicDetail()
    this.loadComments()
    this.checkCollection()
  },
  methods: {
    loadScenicDetail() {
      this.loading = true
      const scenicId = this.$route.params.id
      getScenicDetail(scenicId).then(res => {
        this.scenicDetail = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    loadComments() {
      this.commentLoading = true
      const scenicId = this.$route.params.id
      getCommentsByScenicId(scenicId).then(res => {
        this.commentList = res.data
      }).finally(() => {
        this.commentLoading = false
      })
    },
    submitComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }
      this.commentLoading = true
      const scenicId = this.$route.params.id
      addComment({
        scenicId: scenicId,
        content: this.commentContent
      }).then(() => {
        this.$message.success('评论成功')
        this.commentContent = ''
        this.loadComments()
      }).finally(() => {
        this.commentLoading = false
      })
    },
    checkCollection() {
      const scenicId = this.$route.params.id
      getCollectionList().then(res => {
        this.isCollected = res.data.some(item => item.id === scenicId)
      })
    },
    toggleCollection() {
      const scenicId = this.$route.params.id
      if (this.isCollected) {
        deleteCollection(scenicId).then(() => {
          this.$message.success('取消收藏成功')
          this.isCollected = false
        })
      } else {
        addCollection(scenicId).then(() => {
          this.$message.success('收藏成功')
          this.isCollected = true
        })
      }
    }
  }
}
</script>

<style scoped>
.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.detail-header {
  margin-bottom: 20px;
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 20px;
}

.detail-left {
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.image-gallery {
  margin-bottom: 30px;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.info-section {
  margin-bottom: 30px;
}

.info-section h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
}

.description {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  margin: 0;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.comment-input {
  margin-bottom: 20px;
}

.submit-btn {
  margin-top: 10px;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-user {
  margin-left: 10px;
  flex: 1;
}

.username {
  font-weight: bold;
  color: #333;
  margin-right: 10px;
}

.time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  margin-left: 50px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.detail-right {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.action-card {
  position: sticky;
  top: 20px;
}

.price-section {
  text-align: center;
  margin-bottom: 20px;
}

.price {
  font-size: 36px;
  color: #ff6b6b;
  font-weight: bold;
}

.unit {
  font-size: 16px;
  color: #999;
}

.collect-btn {
  width: 100%;
  font-size: 16px;
}

.info-card {
  position: sticky;
  top: 200px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 14px;
  color: #666;
}

.info-item i {
  font-size: 20px;
  margin-right: 10px;
  color: #409eff;
}
</style>
