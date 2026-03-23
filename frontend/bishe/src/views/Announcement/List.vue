<template>
  <div class="announcement-page">
    <div class="page-header">
      <h1>公告列表</h1>
      <p>了解平台最新动态和活动信息</p>
    </div>
    
    <div class="announcement-list">
      <el-card v-for="item in announcementList" :key="item.id" class="announcement-item" @click="viewDetail(item)">
        <div class="item-header">
          <h3>{{ item.title }}</h3>
          <span class="time">{{ formatTime(item.createTime) }}</span>
        </div>
        <p class="summary">{{ item.summary }}</p>
        <div class="item-footer">
          <el-tag v-if="item.isTop" type="danger" size="small">置顶</el-tag>
          <span class="view-more">查看详情 <el-icon><ArrowRight /></el-icon></span>
        </div>
      </el-card>
    </div>
    
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight } from '@element-plus/icons-vue'
import { getAnnouncementList } from '@/api/announcement'

const router = useRouter()
const announcementList = ref([])
const total = ref(0)

const query = reactive({
  page: 1,
  size: 5
})

const allAnnouncements = ref([])

const loadAnnouncements = async () => {
  try {
    const res = await getAnnouncementList(query)
    allAnnouncements.value = res || []
    total.value = allAnnouncements.value.length
    // 前端分页处理
    const start = (query.page - 1) * query.size
    const end = start + query.size
    announcementList.value = allAnnouncements.value.slice(start, end)
  } catch (error) {
    console.error('加载公告失败', error)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString()
}

const viewDetail = (item) => {
  router.push(`/announcements/${item.id}`)
}

const handlePageChange = (page) => {
  query.page = page
  loadAnnouncements()
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-page {
  max-width: 1000px;
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

.announcement-list {
  margin-bottom: 30px;
}

.announcement-item {
  margin-bottom: 16px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.announcement-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.item-header h3 {
  font-size: 18px;
  color: #333;
  margin: 0;
}

.time {
  color: #999;
  font-size: 14px;
}

.summary {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 12px;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.view-more {
  color: #409eff;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
}
</style>
