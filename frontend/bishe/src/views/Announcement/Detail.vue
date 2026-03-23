<template>
  <div class="announcement-detail-page">
    <el-card>
      <h1 class="title">{{ announcement.title }}</h1>
      <div class="meta">
        <span>{{ formatTime(announcement.createTime) }}</span>
      </div>
      <div class="content">
        {{ announcement.content }}
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAnnouncementDetail } from '@/api/announcement'

const route = useRoute()
const announcement = ref({})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadDetail = async () => {
  try {
    const id = route.params.id
    const res = await getAnnouncementDetail(id)
    announcement.value = res || {}
  } catch (error) {
    console.error('加载公告详情失败', error)
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.announcement-detail-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  font-size: 24px;
  margin: 0 0 16px;
}

.meta {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.content {
  line-height: 1.8;
  color: #333;
}
</style>
