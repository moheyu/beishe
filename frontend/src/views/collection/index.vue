<template>
  <div class="collection-container">
    <h2 class="page-title">我的收藏</h2>

    <div class="collection-list" v-loading="loading">
      <div v-for="item in collectionList" :key="item.id" class="collection-item" @click="goToDetail(item.id)">
        <el-card :body-style="{ padding: '0px' }">
          <div class="item-image">
            <img :src="item.imageUrl || 'https://via.placeholder.com/300x200'" :alt="item.name">
            <el-button
              type="danger"
              icon="el-icon-delete"
              circle
              class="delete-btn"
              @click.stop="handleDelete(item.id)"
            ></el-button>
          </div>
          <div class="item-content">
            <h3>{{ item.name }}</h3>
            <p class="description">{{ item.description }}</p>
            <div class="item-footer">
              <span class="price">¥{{ item.price }}/人</span>
              <span class="collect-time">{{ item.collectTime }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <el-empty v-if="collectionList.length === 0 && !loading" description="暂无收藏，快去收藏喜欢的景点吧！"></el-empty>
    </div>
  </div>
</template>

<script>
import { getCollectionList, deleteCollection } from '@/api/collection'

export default {
  name: 'Collection',
  data() {
    return {
      collectionList: [],
      loading: false
    }
  },
  created() {
    this.loadCollectionList()
  },
  methods: {
    loadCollectionList() {
      this.loading = true
      getCollectionList().then(res => {
        this.collectionList = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    handleDelete(id) {
      this.$confirm('确定要取消收藏吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCollection(id).then(() => {
          this.$message.success('取消收藏成功')
          this.loadCollectionList()
        })
      })
    },
    goToDetail(id) {
      this.$router.push(`/scenic/${id}`)
    }
  }
}
</script>

<style scoped>
.collection-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-title {
  font-size: 32px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.collection-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.collection-item {
  cursor: pointer;
  transition: transform 0.3s;
}

.collection-item:hover {
  transform: translateY(-5px);
}

.item-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.collection-item:hover .item-image img {
  transform: scale(1.1);
}

.delete-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  opacity: 0;
  transition: opacity 0.3s;
}

.collection-item:hover .delete-btn {
  opacity: 1;
}

.item-content {
  padding: 15px;
}

.item-content h3 {
  font-size: 18px;
  color: #333;
  margin: 0 0 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.description {
  font-size: 14px;
  color: #666;
  margin: 0 0 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 40px;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 20px;
  color: #ff6b6b;
  font-weight: bold;
}

.collect-time {
  font-size: 12px;
  color: #999;
}
</style>
