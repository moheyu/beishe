<template>
  <div class="history-page">
    <div class="page-header">
      <h2>浏览历史</h2>
      <el-button type="danger" @click="handleClearHistory">清空历史</el-button>
    </div>
    <el-card v-for="record in viewRecords" :key="record.id" class="history-item">
      <div class="item-content">
        <el-image :src="record.images && record.images.length > 0 ? getImageUrl(record.images[0]) : ''" fit="cover" class="item-image" />
        <div class="item-info">
          <h3>{{ record.name }}</h3>
          <p class="item-time">{{ formatTime(record.viewTime) }}</p>
        </div>
      </div>
    </el-card>
    <el-empty v-if="!viewRecords.length" description="暂无浏览记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserViewRecords, clearUserViewRecords } from '@/api/viewRecord'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getImageUrl } from '@/utils/image'

const viewRecords = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
}

const loadViewRecords = async () => {
  try {
    const res = await getUserViewRecords()
    viewRecords.value = res || []
  } catch (error) {
    console.error('加载浏览记录失败', error)
    ElMessage.error('加载浏览记录失败')
  }
}

const handleClearHistory = () => {
  ElMessageBox.confirm('确定要清空所有浏览记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await clearUserViewRecords()
      ElMessage.success('清空浏览记录成功')
      viewRecords.value = []
    } catch (error) {
      console.error('清空浏览记录失败', error)
      ElMessage.error('清空浏览记录失败')
    }
  })
}

onMounted(() => {
  loadViewRecords()
})
</script>

<style scoped>
.history-page h2 {
  margin: 0 0 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.history-item {
  margin-bottom: 16px;
}

.item-content {
  display: flex;
  gap: 16px;
}

.item-image {
  width: 120px;
  height: 80px;
  border-radius: 4px;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
}

.item-time {
  margin: 0;
  font-size: 14px;
  color: #999;
}
</style>
