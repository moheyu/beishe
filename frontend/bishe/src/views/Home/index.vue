<template>
  <div class="home-page">
    <!-- 搜索栏 -->
    <div class="search-section">
      <div class="search-container">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索景点、路线..."
          size="large"
          class="search-input"
        >
          <template #append>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 轮播图 -->
    <el-carousel height="400px" class="banner-carousel">
      <el-carousel-item v-for="(item, index) in banners" :key="index">
        <div class="banner-item" :style="{ backgroundImage: `url(${item.image})` }">
          <div class="banner-content">
            <h2>{{ item.title }}</h2>
            <p>{{ item.subtitle }}</p>
            <el-button type="primary" size="large" @click="$router.push(item.link)">
              立即探索
            </el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 推荐景点 -->
    <section class="section">
      <div class="section-header">
        <h2>推荐景点</h2>
        <router-link to="/scenic/list" class="more-link">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in recommendScenic" :key="item.id">
          <div class="scenic-card" @click="$router.push(`/scenic/${item.id}`)">
            <el-image :src="getImageUrl(item.images && item.images.length > 0 ? item.images[0] : '')" fit="cover" class="card-image" />
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="card-desc">{{ item.description }}</p>
              <div class="card-footer">
                <span class="price">¥{{ item.price }}</span>
                <div v-if="Number(item.score) > 0">
                  <el-rate :model-value="Number(item.score)" :max="5" disabled show-score />
                </div>
                <span v-else class="no-rating">暂无评分</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- 推荐路线 -->
    <section class="section">
      <div class="section-header">
        <h2>精选路线</h2>
        <router-link to="/route/list" class="more-link">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="item in recommendRoute" :key="item.id">
          <div class="route-card" @click="$router.push(`/route/${item.id}`)">
            <el-image :src="getImageUrl(item.images && item.images.length > 0 ? item.images[0] : '')" fit="cover" class="card-image" />
            <div class="card-content">
              <h3>{{ item.name }}</h3>
              <p class="card-desc">{{ item.description }}</p>
              <div class="route-info">
                <span><el-icon><Calendar /></el-icon> {{ item.durationDays }}天</span>
                <span><el-icon><Money /></el-icon> ¥{{ item.budget }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- 最新公告 -->
    <section class="section">
      <div class="section-header">
        <h2>最新公告</h2>
        <router-link to="/announcements" class="more-link">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </router-link>
      </div>
      <el-card class="announcement-card">
        <div
          v-for="item in announcements"
          :key="item.id"
          class="announcement-item"
          @click="$router.push(`/announcements/${item.id}`)"
        >
          <span class="announcement-title">{{ item.title }}</span>
          <span class="announcement-time">{{ formatTime(item.createTime) }}</span>
        </div>
      </el-card>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, ArrowRight, Calendar, Money } from '@element-plus/icons-vue'
import { getRecommendScenic } from '@/api/scenic'
import { getRecommendRoute } from '@/api/route'
import { getAnnouncementList } from '@/api/announcement'
import { getHybridRecommend } from '@/api/preference'
import { getImageUrl } from '@/utils/image'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const searchKeyword = ref('')
const recommendScenic = ref([])
const recommendRoute = ref([])
const announcements = ref([])
const userStore = useUserStore()

const banners = [
  {
    title: '探索世界之美',
    subtitle: '发现全球最美的风景，开启您的梦想之旅',
    image: 'https://picsum.photos/1920/400?random=1',
    link: '/scenic/list'
  },
  {
    title: '精选旅游路线',
    subtitle: '专业规划的路线，让您的旅行更加精彩',
    image: 'https://picsum.photos/1920/400?random=2',
    link: '/route/list'
  },
  {
    title: '加入旅游社区',
    subtitle: '与万千旅友分享您的旅行故事',
    image: 'https://picsum.photos/1920/400?random=3',
    link: '/forum'
  }
]

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/scenic/list',
      query: { keyword: searchKeyword.value }
    })
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString()
}

const loadData = async () => {
  try {
    let scenicRes, routeRes
    
    // 已登录用户使用个性化推荐，未登录使用全局推荐
    if (userStore.isLoggedIn) {
      try {
        scenicRes = await getHybridRecommend({ limit: 8 })
      } catch (e) {
        // 如果个性化推荐失败，使用全局推荐
        scenicRes = await getRecommendScenic({ limit: 8 })
      }
    } else {
      scenicRes = await getRecommendScenic({ limit: 8 })
    }
    
    const [routeResTmp, announcementRes] = await Promise.all([
      getRecommendRoute({ limit: 6 }),
      getAnnouncementList({ page: 1, size: 5 })
    ])
    
    routeRes = routeResTmp
    recommendScenic.value = scenicRes || []
    recommendRoute.value = routeRes || []
    announcements.value = announcementRes || []
  } catch (error) {
    console.error('加载首页数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

.banner-carousel {
  margin-bottom: 0;
}

.banner-item {
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}

.banner-content {
  position: relative;
  text-align: center;
  color: #fff;
  z-index: 1;
}

.banner-content h2 {
  font-size: 48px;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 24px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.search-section {
  margin: 0 0 0;
  padding: 0;
  position: relative;
  z-index: 10;
}

.search-container {
  background: #fff;
  padding: 20px;
  border-radius: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 100%;
  margin: 0;
}

.search-input :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.section {
  max-width: 1400px;
  margin: 0 auto 50px;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 28px;
  color: #333;
  margin: 0;
}

.more-link {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}

.more-link:hover {
  color: #66b1ff;
}

.scenic-card,
.route-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.scenic-card:hover,
.route-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-image {
  width: 100%;
  height: 180px;
}

.card-content {
  padding: 16px;
}

.card-content h3 {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
  height: 42px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.route-info {
  display: flex;
  gap: 16px;
  color: #666;
  font-size: 13px;
}

.route-info span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.announcement-card {
  border-radius: 8px;
}

.announcement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: color 0.3s;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-item:hover {
  color: #409eff;
}

.announcement-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 16px;
}

.announcement-time {
  color: #999;
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-content h2 {
    font-size: 28px;
  }
  
  .banner-content p {
    font-size: 14px;
  }
  
  .search-section {
    margin: 0 0 0;
    padding: 0;
  }
  
  .section {
    padding: 0 10px;
    margin-bottom: 30px;
  }
  
  .section-header h2 {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .banner-carousel {
    height: 250px;
  }
  
  .banner-content h2 {
    font-size: 22px;
  }
  
  .banner-content .el-button {
    font-size: 14px;
    padding: 8px 20px;
  }
}
</style>
