<template>
  <div class="dashboard">
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon user-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon scenic-icon">
              <i class="el-icon-picture-outline"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.scenicCount }}</div>
              <div class="stat-label">景点总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon view-icon">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalViews }}</div>
              <div class="stat-label">总浏览量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon comment-icon">
              <i class="el-icon-chat-dot-round"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.commentCount }}</div>
              <div class="stat-label">评论总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>热门景点排行</span>
          </div>
          <el-table :data="hotScenicList" style="width: 100%">
            <el-table-column prop="name" label="景点名称"></el-table-column>
            <el-table-column prop="viewCount" label="浏览量" sortable></el-table-column>
            <el-table-column prop="price" label="价格" sortable>
              <template slot-scope="scope">
                ¥{{ scope.row.price }}/人
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>最新用户</span>
          </div>
          <el-table :data="newUserList" style="width: 100%">
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="nickname" label="昵称"></el-table-column>
            <el-table-column prop="createTime" label="注册时间"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAdminStats } from '@/api/admin'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        userCount: 0,
        scenicCount: 0,
        totalViews: 0,
        commentCount: 0
      },
      hotScenicList: [],
      newUserList: [],
      loading: false
    }
  },
  created() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      this.loading = true
      try {
        const res = await getAdminStats()
        const data = res.data
        this.stats = {
          userCount: data.userCount,
          scenicCount: data.scenicCount,
          totalViews: data.totalViews,
          commentCount: data.commentCount
        }
        this.hotScenicList = data.hotScenicList || []
        this.newUserList = data.newUserList || []
      } catch (error) {
        this.$message.error('加载统计数据失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.scenic-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.view-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.comment-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  text-align: right;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}
</style>
