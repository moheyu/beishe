<template>
  <div class="recommend-container">

    <!-- 推荐列表区域 -->
    <div class="recommend-list" v-loading="loading">
      <div v-for="item in recommendList" :key="item.id" class="recommend-item" @click="goToDetail(item.id)">
        <div class="card-image">
          <img :src="getImageUrl(item)" :alt="item.name">
          <div class="card-overlay">
            <div class="card-tag" v-if="item.categoryName">{{ item.categoryName }}</div>
            <div class="recommend-badge">
              <i class="el-icon-star-on"></i>
              推荐
            </div>
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
    <el-empty v-if="recommendList.length === 0 && !loading" description="暂无推荐数据，请先设置您的偏好！" class="empty-state"></el-empty>

    <!-- 偏好设置区域 -->
    <el-card class="preference-card">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-setting"></i>
          偏好设置
        </span>
        <el-button type="primary" icon="el-icon-plus" size="medium" @click="showPreferenceDialog = true">添加偏好</el-button>
      </div>
      <div class="preference-list">
        <el-tag
          v-for="item in preferenceList"
          :key="item.id"
          closable
          @close="handleDeletePreference(item.id)"
          type="success"
          size="medium"
          effect="plain"
        >
          {{ item.categoryName || item.tagName }}
        </el-tag>
        <span v-if="preferenceList.length === 0" class="empty-text">暂无偏好设置，点击右上角添加</span>
      </div>
    </el-card>

    <!-- 添加偏好对话框 -->
    <el-dialog title="添加偏好" :visible.sync="showPreferenceDialog" width="500px" class="preference-dialog">
      <el-form :model="preferenceForm" label-width="100px">
        <el-form-item label="偏好类型">
          <el-radio-group v-model="preferenceType" size="medium">
            <el-radio-button label="category">分类</el-radio-button>
            <el-radio-button label="tag">标签</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择分类" v-if="preferenceType === 'category'">
          <el-select v-model="preferenceForm.categoryId" placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择标签" v-if="preferenceType === 'tag'">
          <el-select v-model="preferenceForm.tagId" placeholder="请选择标签" style="width: 100%;">
            <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showPreferenceDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAddPreference" :loading="preferenceLoading">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getRecommendScenic } from '@/api/scenic'

export default {
  name: 'Recommend',
  data() {
    return {
      recommendList: [],
      loading: false,
      showPreferenceDialog: false,
      preferenceLoading: false,
      preferenceType: 'category',
      preferenceForm: {
        categoryId: '',
        tagId: ''
      },
      preferenceList: [],
      categories: [
        { id: 1, name: '自然风光' },
        { id: 2, name: '人文景观' },
        { id: 3, name: '主题乐园' },
        { id: 4, name: '历史古迹' }
      ],
      tags: [
        { id: 1, name: '山岳' },
        { id: 2, name: '湖泊' },
        { id: 3, name: '古迹' },
        { id: 4, name: '美食' }
      ]
    }
  },
  created() {
    this.loadRecommendScenic()
  },
  methods: {
    getImageUrl(item) {
      if (item.images && item.images.length > 0) {
        if (typeof item.images === 'string') {
          try {
            const images = JSON.parse(item.images)
            return images[0]
          } catch (e) {
            return 'https://via.placeholder.com/300x200?text=No+Image'
          }
        } else if (Array.isArray(item.images)) {
          return item.images[0]
        }
      }
      if (item.imageUrl) {
        return item.imageUrl
      }
      return 'https://via.placeholder.com/300x200?text=No+Image'
    },
    loadRecommendScenic() {
      this.loading = true
      getRecommendScenic({ limit: 20 }).then(res => {
        this.recommendList = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    handleAddPreference() {
      this.$message.success('偏好添加成功')
      this.showPreferenceDialog = false
      this.loadRecommendScenic()
    },
    handleDeletePreference(id) {
      this.$confirm('确定要删除这个偏好吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadRecommendScenic()
      })
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
.recommend-container {
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

/* 推荐列表区域 */
.recommend-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
  padding-top: 80px;
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

.recommend-item {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: relative;
}

.recommend-item:hover {
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

.recommend-item:hover .card-image img {
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

.recommend-item:hover .card-overlay {
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

.recommend-item:hover .card-tag {
  transform: translateY(0);
  opacity: 1;
}

.recommend-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  backdrop-filter: blur(10px);
  transform: translateY(-10px);
  opacity: 0;
  transition: all 0.4s ease;
}

.recommend-item:hover .recommend-badge {
  transform: translateY(0);
  opacity: 1;
}

.recommend-badge i {
  margin-right: 4px;
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

.recommend-item:hover .card-title {
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

.recommend-item:hover .card-price {
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

.recommend-item:hover .card-views {
  color: #667eea;
}

.card-views i {
  font-size: 16px;
}

/* 空状态 */
.empty-state {
  margin: 60px 0;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

/* 偏好设置卡片 */
.preference-card {
  margin-top: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  border: none;
  background: white;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out 0.6s both;
}

.preference-card:hover {
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-title i {
  font-size: 20px;
  color: #667eea;
}

.preference-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  min-height: 40px;
}

.preference-list .el-tag {
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.preference-list .el-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.empty-text {
  color: #95a5a6;
  font-size: 14px;
  font-style: italic;
}

/* 对话框样式 */
.preference-dialog .el-dialog {
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.preference-dialog .el-dialog__header {
  padding: 24px 24px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.preference-dialog .el-dialog__body {
  padding: 24px;
}

.preference-dialog .el-dialog__footer {
  padding: 16px 24px 24px;
  border-top: 1px solid #f0f0f0;
}

.preference-dialog .el-form-item {
  margin-bottom: 24px;
}

.preference-dialog .el-form-item__label {
  font-weight: 600;
  color: #2c3e50;
}

.preference-dialog .el-radio-button__inner {
  border-radius: 20px;
  padding: 10px 24px;
}

.preference-dialog .el-select {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .page-title {
    font-size: 40px;
  }
  
  .recommend-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .recommend-container {
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
  
  .recommend-list {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .card-header .el-button {
    width: 100%;
  }
  
  .preference-list {
    justify-content: center;
  }
}
</style>
