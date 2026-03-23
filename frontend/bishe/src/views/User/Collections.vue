<template>
  <div class="collections-page">
    <h2>我的收藏</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="景点" name="scenic">
        <el-card v-for="item in scenicCollections" :key="item.id" class="collection-item">
          <div class="item-content">
            <el-image :src="item.images && item.images.length > 0 ? item.images[0] : ''" fit="cover" class="item-image" />
            <div class="item-info">
              <h3>{{ item.name }}</h3>
              <p class="item-desc">{{ item.description }}</p>
              <div class="item-footer">
                <span class="price">¥{{ item.price }}</span>
                <el-button type="danger" size="small" @click="handleRemoveScenicCollection(item.id)">取消收藏</el-button>
              </div>
            </div>
          </div>
        </el-card>
        <el-empty v-if="!scenicCollections.length" description="暂无景点收藏" />
      </el-tab-pane>
      <el-tab-pane label="路线" name="route">
        <el-card v-for="item in routeCollections" :key="item.id" class="collection-item">
          <div class="item-content">
            <el-image :src="item.images && item.images.length > 0 ? item.images[0] : ''" fit="cover" class="item-image" />
            <div class="item-info">
              <h3>{{ item.name }}</h3>
              <p class="item-desc">{{ item.description }}</p>
              <div class="item-footer">
                <span class="duration">{{ item.durationDays }}天</span>
                <el-button type="danger" size="small" @click="handleRemoveRouteCollection(item.id)">取消收藏</el-button>
              </div>
            </div>
          </div>
        </el-card>
        <el-empty v-if="!routeCollections.length" description="暂无路线收藏" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCollectionList, removeScenicCollection, removeRouteCollection } from '@/api/collection'
import { ElMessage } from 'element-plus'

const activeTab = ref('scenic')
const scenicCollections = ref([])
const routeCollections = ref([])

const loadCollections = async () => {
  try {
    const res = await getCollectionList()
    // 后端返回格式: { scenic: [...], route: [...] }
    if (res && res.scenic) {
      scenicCollections.value = res.scenic || []
    }
    if (res && res.route) {
      routeCollections.value = res.route || []
    }
  } catch (error) {
    console.error('加载收藏失败', error)
    ElMessage.error('加载收藏失败')
  }
}

const handleRemoveScenicCollection = async (id) => {
  try {
    await removeScenicCollection(id)
    ElMessage.success('取消收藏成功')
    loadCollections()
  } catch (error) {
    console.error('取消收藏失败', error)
    ElMessage.error('取消收藏失败')
  }
}

const handleRemoveRouteCollection = async (id) => {
  try {
    await removeRouteCollection(id)
    ElMessage.success('取消收藏成功')
    loadCollections()
  } catch (error) {
    console.error('取消收藏失败', error)
    ElMessage.error('取消收藏失败')
  }
}

onMounted(() => {
  loadCollections()
})
</script>

<style scoped>
.collections-page h2 {
  margin: 0 0 20px;
}

.collection-item {
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

.item-desc {
  margin: 0 0 12px;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.duration {
  color: #666;
}
</style>
