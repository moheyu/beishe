<template>
  <div class="home-container">
    <!-- Hero区域 - 自然风轮播 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">探索自然之美</h1>
        <p class="hero-subtitle">发现世界各地的绝美风景</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" round @click="$router.push('/scenic')">开始探索</el-button>
          <el-button size="large" round @click="$router.push('/recommend')">推荐景点</el-button>
        </div>
      </div>
      <div class="hero-bg"></div>
    </div>

    <!-- 新的三画布容器 -->
    <div class="canvas-section">
      <div class="section-header">
        <h2 class="section-title">精选景点</h2>
        <p class="section-subtitle">为您精心挑选的顶级旅游目的地</p>
      </div>
      <div class="new-canvas-container">
        <!-- 拉环装饰 -->
        <div class="topVis_straps">
          <div class="topVis_straps_item" v-for="n in 4" :key="'new-strap-'+n">
            <svg class="topVis_straps_item_svg" viewBox="0 0 50 90">
              <rect class="strap-rect" x="18" y="5" width="14" height="55"></rect>
              <circle class="strap-ring" cx="25" cy="70" r="15" fill="none" stroke="#2196F3" stroke-width="8"></circle>
            </svg>
          </div>
        </div>

        <div class="canvas-wrapper">
          <div class="corner corner-tl"></div>
          <div class="corner corner-tr"></div>
          <div class="corner corner-bl"></div>
          <div class="corner corner-br"></div>
          <canvas id="new-canvas1" class="canvas-item" width="740" height="600"></canvas>
        </div>

        <div class="canvas-wrapper">
          <div class="corner corner-tl"></div>
          <div class="corner corner-tr"></div>
          <div class="corner corner-bl"></div>
          <div class="corner corner-br"></div>
          <canvas id="new-canvas2" class="canvas-item" width="740" height="600"></canvas>
        </div>

        <div class="canvas-wrapper">
          <div class="corner corner-tl"></div>
          <div class="corner corner-tr"></div>
          <div class="corner corner-bl"></div>
          <div class="corner corner-br"></div>
          <canvas id="new-canvas3" class="canvas-item" width="740" height="600"></canvas>
        </div>
      </div>
    </div>

    <!-- 特色介绍区域 -->
    <div class="features-section">
      <div class="section-header">
        <h2 class="section-title">为什么选择我们</h2>
        <p class="section-subtitle">为您提供最优质的旅游体验</p>
      </div>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-star-on"></i>
          </div>
          <h3 class="feature-title">精选景点</h3>
          <p class="feature-desc">严选全球顶级旅游目的地</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-price-tag"></i>
          </div>
          <h3 class="feature-title">优惠价格</h3>
          <p class="feature-desc">最具竞争力的旅游价格</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-service"></i>
          </div>
          <h3 class="feature-title">专业服务</h3>
          <p class="feature-desc">24小时专业客服支持</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-medal"></i>
          </div>
          <h3 class="feature-title">品质保证</h3>
          <p class="feature-desc">100%品质保证承诺</p>
        </div>
      </div>
    </div>

    <!-- 推荐景点区域 -->
    <div class="recommend-section">
      <div class="section-header">
        <h2 class="section-title">推荐景点</h2>
        <p class="section-subtitle">为您推荐的热门旅游目的地</p>
      </div>
      <div class="scenic-grid" v-loading="recommendLoading">
        <div v-for="item in recommendList" :key="item.id" class="scenic-card" @click="goToDetail(item.id)">
          <div class="card-image">
            <img :src="item.imageUrl || 'https://via.placeholder.com/400x300'" :alt="item.name">
            <div class="card-overlay">
              <div class="card-tag" v-if="item.categoryName">{{ item.categoryName }}</div>
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
      <el-empty v-if="recommendList.length === 0 && !recommendLoading" description="暂无推荐景点数据"></el-empty>
    </div>

    <!-- 用户评价区域 -->
    <div class="testimonials-section">
      <div class="section-header">
        <h2 class="section-title">用户评价</h2>
        <p class="section-subtitle">听听用户怎么说</p>
      </div>
      <div class="testimonials-grid">
        <div class="testimonial-card">
          <div class="testimonial-content">
            <p>"这次旅行体验太棒了！景点美不胜收，服务也非常周到，强烈推荐！"</p>
          </div>
          <div class="testimonial-author">
            <div class="author-avatar">
              <img src="https://via.placeholder.com/50" alt="用户头像">
            </div>
            <div class="author-info">
              <h4>张先生</h4>
              <span>北京</span>
            </div>
          </div>
        </div>
        <div class="testimonial-card">
          <div class="testimonial-content">
            <p>"价格实惠，景点选择丰富，客服响应及时，下次还会选择这里！"</p>
          </div>
          <div class="testimonial-author">
            <div class="author-avatar">
              <img src="https://via.placeholder.com/50" alt="用户头像">
            </div>
            <div class="author-info">
              <h4>李女士</h4>
              <span>上海</span>
            </div>
          </div>
        </div>
        <div class="testimonial-card">
          <div class="testimonial-content">
            <p>"非常专业的旅游平台，行程安排合理，导游讲解生动，收获满满！"</p>
          </div>
          <div class="testimonial-author">
            <div class="author-avatar">
              <img src="https://via.placeholder.com/50" alt="用户头像">
            </div>
            <div class="author-info">
              <h4>王先生</h4>
              <span>广州</span>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { getRecommendScenic, getScenicList } from '@/api/scenic'   // 根据实际路径调整

// 三画布轮播配置
const CONFIG = {
  canvasWidth: 740,
  canvasHeight: 600,
  slideSpeed: 6,
  pauseDuration: 3000
}

export default {
  name: 'Home',
  components: {
    // 移除了 Swiper 相关组件
  },
  data() {
    return {
      hotList: [],
      recommendList: [],
      recommendLoading: false,
      hotLoading: false,
      carouselList: [],          // 轮播数据源（来自热门景点前N个）
      strapSwing: false,         // 控制拉环摆动动画（备用，实际直接操作DOM）
      // 新画布状态（完整轮播功能）
      newCanvasState: {
        offsetX: 0,
        isPaused: false,
        pauseTimer: null,
        currentIndex: 0,
        images: [],
        longCanvas: null,
        imageCount: 0
      }

    }
  },
  created() {
    this.loadRecommendScenic()
    this.loadHotScenic()
  },
  mounted() {
    // 初始化新画布轮播（等待数据）
    this.$nextTick(() => {
      if (this.carouselList.length) {
        this.initNewCanvasCarousel()
      }
    })
  },
  beforeDestroy() {
    // 清理新画布轮播的定时器
    if (this.newCanvasState.pauseTimer) {
      clearTimeout(this.newCanvasState.pauseTimer)
    }
  },
  watch: {
    carouselList: {
      handler(newList) {
        if (newList && newList.length) {
          this.initNewCanvasCarousel()
        }
      },
      deep: true
    }
  },
  methods: {
    // 获取图片完整URL（使用相对路径通过Vite代理）
    getImageUrl(images) {
      if (images && images.length > 0) {
        let url = images[0]
        if (url.startsWith('http')) {
          return url
        }
        // 使用相对路径，通过Vite代理访问后端图片
        if (url.includes('20260312') && !url.includes('20260312/')) {
          url = url.replace('20260312', '20260312/')
        }
        // 使用相对路径，让Vite代理处理跨域
        return '/api/uploads/' + url
      }
      return 'https://via.placeholder.com/300x200?text=No+Image'
    },

    // 加载推荐景点
    loadRecommendScenic() {
      this.recommendLoading = true
      getRecommendScenic({ limit: 4 })
        .then(res => {
          this.recommendList = (res.data || []).map(item => {
            if (item.images) {
              if (typeof item.images === 'string') {
                try {
                  item.images = JSON.parse(item.images)
                } catch (e) {
                  console.error('解析推荐景点图片失败', e)
                  item.images = []
                }
              }
              if (!Array.isArray(item.images)) {
                item.images = []
              }
            } else {
              item.images = []
            }
            item.imageUrl = this.getImageUrl(item.images)
            return item
          })
        })
        .catch(err => {
          console.error('加载推荐景点失败:', err)
        })
        .finally(() => {
          this.recommendLoading = false
        })
    },

    // 加载热门景点（用于轮播和热门列表）
    loadHotScenic() {
      this.hotLoading = true
      getScenicList({ pageNum: 1, pageSize: 4 })
        .then(res => {
          console.log('热门景点API响应:', res)
          const records = res.data?.records || []
          console.log('热门景点记录:', records)
          this.hotList = records.map(item => {
            console.log('处理景点数据:', item)
            if (item.images) {
              if (typeof item.images === 'string') {
                try {
                  item.images = JSON.parse(item.images)
                } catch (e) {
                  console.error('解析热门景点图片失败', e)
                  item.images = []
                }
              }
              if (!Array.isArray(item.images)) {
                item.images = []
              }
            } else {
              item.images = []
            }
            item.imageUrl = this.getImageUrl(item.images)
            console.log('景点图片URL:', item.imageUrl)
            return item
          })
          this.updateCarouselData(this.hotList)
          this.updateSecondCarouselData(this.hotList)
        })
        .catch(err => {
          console.error('加载热门景点失败:', err)
        })
        .finally(() => {
          this.hotLoading = false
        })
    },

    // 更新轮播数据（保持与原来一致的格式）
    updateCarouselData(hotList) {
      console.log('更新轮播数据，热门景点数据:', hotList)
      if (hotList && hotList.length > 0) {
        // 取前4个（与原来一致），但为了轮播效果更好，可重复或补默认图，这里保持原样4个
        const newCarouselList = hotList.slice(0, 4).map((item, index) => ({
          id: index + 1,
          scenicId: item.id,
          title: item.name,
          description: item.description,
          image: item.imageUrl || 'https://via.placeholder.com/1200x400?text=' + item.name
        }))
        console.log('生成的轮播数据:', newCarouselList)
        this.carouselList = newCarouselList
      }
    },

    // 更新第二个轮播数据
    updateSecondCarouselData(hotList) {
      console.log('更新第二个轮播数据，热门景点数据:', hotList)
      if (hotList && hotList.length > 0) {
        // 如果有足够的数据，取后4个，否则重复使用前4个
        let sourceList = hotList
        if (hotList.length >= 4) {
          sourceList = hotList.slice(-4) // 取最后4个
        }
        const newSecondCarouselList = sourceList.map((item, index) => ({
          id: index + 1,
          scenicId: item.id,
          title: item.name,
          description: item.description,
          image: item.imageUrl || 'https://via.placeholder.com/1200x400?text=' + item.name
        }))
        console.log('生成的第二个轮播数据:', newSecondCarouselList)
        this.secondCarouselList = newSecondCarouselList
      }
    },

    // 加载图片列表
    loadImages(urls) {
      console.log('开始加载轮播图片:', urls)
      const promises = urls.map((url, i) => {
        return new Promise((resolve, reject) => {
          const img = new Image()
          img.crossOrigin = 'anonymous'
          img.onload = () => {
            console.log(`图片 ${i} 加载成功:`, url)
            resolve(img)
          }
          img.onerror = (e) => {
            console.error(`图片 ${i} 加载失败:`, url, e)
            reject(new Error(`图片加载失败: ${url}`))
          }
          img.src = url
        })
      })
      return Promise.all(promises)
    },

    // 创建长图画布（原始图片拼接，不复制）
    createLongCanvas(images) {
      const canvas = document.createElement('canvas')
      const totalWidth = CONFIG.canvasWidth * images.length
      canvas.width = totalWidth
      canvas.height = CONFIG.canvasHeight
      const ctx = canvas.getContext('2d')
      images.forEach((img, i) => {
        ctx.drawImage(img, i * CONFIG.canvasWidth, 0, CONFIG.canvasWidth, CONFIG.canvasHeight)
      })
      return canvas
    },

    // 初始化新画布轮播（完全按照原始代码逻辑）
    initNewCanvasCarousel() {
      console.log('初始化新画布轮播')
      // 清理之前的定时器
      if (this.newCanvasState.pauseTimer) {
        clearTimeout(this.newCanvasState.pauseTimer)
      }

      // 使用轮播数据源的图片
      const imageUrls = this.carouselList.map(item => item.image)
      console.log('新画布轮播图片URL:', imageUrls)

      if (imageUrls.length === 0) {
        console.log('没有图片数据')
        return
      }

      this.newCanvasState.imageCount = imageUrls.length

      // 加载图片
      this.loadImages(imageUrls).then(images => {
        console.log('新画布轮播图片加载成功，数量:', images.length)
        this.newCanvasState.images = images
        this.newCanvasState.longCanvas = this.createLongCanvas(images)
        this.newCanvasState.offsetX = 0
        this.newCanvasState.isPaused = false
        this.animateNew()
        this.startNewSwing()
      }).catch(err => {
        console.error('新画布轮播图片加载失败:', err)
        this.showNewCanvasError()
      })
    },

    // 新画布轮播动画循环
    animateNew() {
      this.updateNewCanvasState()
      this.renderNewCanvas()
      requestAnimationFrame(this.animateNew)
    },

    // 更新新画布轮播状态
    updateNewCanvasState() {
      if (this.newCanvasState.isPaused) return

      this.newCanvasState.offsetX += CONFIG.slideSpeed

      // 循环重置
      const totalWidth = CONFIG.canvasWidth * this.newCanvasState.imageCount
      if (this.newCanvasState.offsetX >= totalWidth) {
        this.newCanvasState.offsetX = 0
      }

      // 检查是否需要暂停
      const expectedOffset = Math.round(this.newCanvasState.offsetX / CONFIG.canvasWidth) * CONFIG.canvasWidth
      if (Math.abs(this.newCanvasState.offsetX - expectedOffset) < CONFIG.slideSpeed) {
        this.newCanvasState.offsetX = expectedOffset
        this.pauseNewCanvas()
      }
    },

    // 暂停新画布轮播
    pauseNewCanvas() {
      this.newCanvasState.isPaused = true
      this.newCanvasState.currentIndex = Math.floor(this.newCanvasState.offsetX / CONFIG.canvasWidth) % this.newCanvasState.imageCount
      console.log(`图片 ${this.newCanvasState.currentIndex + 1} 暂停`)

      this.stopNewSwing()

      this.newCanvasState.pauseTimer = setTimeout(() => {
        this.newCanvasState.isPaused = false
        console.log('继续滑动')
        this.startNewSwing()
      }, CONFIG.pauseDuration)
    },

    // 渲染新画布轮播的三个画布
    renderNewCanvas() {
      if (!this.newCanvasState.longCanvas) return
      const ctx1 = document.getElementById('new-canvas1')?.getContext('2d')
      const ctx2 = document.getElementById('new-canvas2')?.getContext('2d')
      const ctx3 = document.getElementById('new-canvas3')?.getContext('2d')
      if (!ctx1 || !ctx2 || !ctx3) return

      const totalWidth = CONFIG.canvasWidth * this.newCanvasState.imageCount

      const offset1 = this.newCanvasState.offsetX % totalWidth
      const offset2 = (this.newCanvasState.offsetX + CONFIG.canvasWidth) % totalWidth
      const offset3 = (this.newCanvasState.offsetX + CONFIG.canvasWidth * 2) % totalWidth

      this.drawNewToCanvas(ctx1, offset1)
      this.drawNewToCanvas(ctx2, offset2)
      this.drawNewToCanvas(ctx3, offset3)
    },

    // 绘制新画布轮播的单个画布
    drawNewToCanvas(ctx, startX) {
      ctx.clearRect(0, 0, CONFIG.canvasWidth, CONFIG.canvasHeight)

      const totalWidth = CONFIG.canvasWidth * this.newCanvasState.imageCount
      let drawX = startX % totalWidth
      let widthRemaining = CONFIG.canvasWidth
      let destX = 0

      while (widthRemaining > 0) {
        const srcX = drawX
        const srcWidth = Math.min(widthRemaining, totalWidth - srcX)

        ctx.drawImage(
          this.newCanvasState.longCanvas,
          srcX, 0, srcWidth, CONFIG.canvasHeight,
          destX, 0, srcWidth, CONFIG.canvasHeight
        )

        widthRemaining -= srcWidth
        destX += srcWidth
        drawX = (drawX + srcWidth) % totalWidth
      }
    },

    // 启动新画布轮播的拉环摆动
    startNewSwing() {
      const items = document.querySelectorAll('.new-canvas-container .topVis_straps_item')
      items.forEach(item => {
        item.classList.remove('isSwing')
        void item.offsetWidth
        item.classList.add('isSwing')
        item.addEventListener('animationend', () => {
          item.classList.remove('isSwing')
        }, { once: true })
      })
    },

    // 停止新画布轮播的拉环摆动
    stopNewSwing() {
      document.querySelectorAll('.new-canvas-container .topVis_straps_item').forEach(item => {
        item.classList.remove('isSwing')
      })
    },

    // 新画布轮播错误提示
    showNewCanvasError() {
      ['new-canvas1', 'new-canvas2', 'new-canvas3'].forEach(id => {
        const canvas = document.getElementById(id)
        if (canvas) {
          const ctx = canvas.getContext('2d')
          ctx.fillStyle = '#fee'
          ctx.fillRect(0, 0, CONFIG.canvasWidth, CONFIG.canvasHeight)
          ctx.fillStyle = '#c00'
          ctx.font = '20px Arial'
          ctx.textAlign = 'center'
          ctx.fillText('图片加载失败', CONFIG.canvasWidth / 2, CONFIG.canvasHeight / 2)
        }
      })
    },

    // 跳转详情
    goToDetail(id) {
      this.$router.push(`/scenic/${id}`)
    }
  }
}
</script>

<style scoped>
/* ===== 自然风现代化设计 ===== */

/* 全局样式 */
.home-container {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  width: 100%;
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
}

/* Hero区域 */
.hero-section {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
}

.hero-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=1920') center/cover no-repeat;
  opacity: 0.3;
  animation: heroBgMove 30s linear infinite;
}

@keyframes heroBgMove {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
  padding: 20px;
  animation: fadeInUp 1s ease-out;
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

.hero-title {
  font-size: 72px;
  font-weight: 700;
  margin: 0 0 20px;
  letter-spacing: -2px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.hero-subtitle {
  font-size: 28px;
  margin: 0 0 40px;
  opacity: 0.9;
  font-weight: 300;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

/* 公共区块样式 */
.section {
  max-width: 1400px;
  margin: 0 auto 80px;
  padding: 0 40px;
  width: 100%;
  box-sizing: border-box;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-title {
  font-size: 48px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 16px;
  letter-spacing: -1px;
}

.section-subtitle {
  font-size: 20px;
  color: #7f8c8d;
  margin: 0;
  font-weight: 300;
}

/* 画布区域 */
.canvas-section {
  background: linear-gradient(180deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 80px 0;
}

/* 特色介绍区域 */
.features-section {
  background: white;
  padding: 100px 0;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  margin-top: 40px;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
}

.feature-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 12px;
}

.feature-desc {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0;
  line-height: 1.6;
}

/* 推荐景点区域 */
.recommend-section {
  background: linear-gradient(180deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 100px 0;
}

.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 30px;
  width: 100%;
  box-sizing: border-box;
}

.scenic-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.scenic-card:hover {
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

.scenic-card:hover .card-image img {
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

.scenic-card:hover .card-overlay {
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
}

.card-views {
  font-size: 14px;
  color: #95a5a6;
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-views i {
  font-size: 16px;
}

/* 用户评价区域 */
.testimonials-section {
  background: white;
  padding: 100px 0;
}

.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.testimonial-card {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.testimonial-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.testimonial-content {
  font-size: 18px;
  color: #2c3e50;
  line-height: 1.8;
  margin-bottom: 30px;
  font-style: italic;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-info h4 {
  font-size: 18px;
  color: #2c3e50;
  margin: 0 0 4px;
  font-weight: 600;
}

.author-info span {
  font-size: 14px;
  color: #7f8c8d;
}

/* CTA区域 */
/* ===== 三画布轮播样式 ===== */
:root {
  --color-primary: #667eea;
  --color-strap: #764ba2;
  --color-bg: #f5f7fa;
  --color-canvas: #ffffff;
  --color-padding: #e8f4f8;
  --canvas-width: 740px;
  --canvas-height: 600px;
  --canvas-padding: 20px;
  --spacing-lg: 40px;
}

.new-canvas-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0;
  margin: 0 auto;
  padding: 40px 0;
  background: linear-gradient(180deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 30px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.new-canvas-container .topVis_straps {
  position: absolute;
  top: 20px;
  left: 0;
  z-index: 4;
  width: 100%;
  display: flex;
  justify-content: center;
  column-gap: 100px;
  filter: drop-shadow(5px 10px 10px rgba(43, 45, 66, 0.25));
}

.new-canvas-container .topVis_straps_item {
  width: 60px;
  height: 130px;
  transform-origin: top center;
  opacity: 0.95;
}

.new-canvas-container .topVis_straps_item_svg {
  width: 100%;
  height: 100%;
}

.new-canvas-container .strap-rect {
  fill: #667eea;
}

.new-canvas-container .strap-ring {
  fill: none;
  stroke: #667eea;
  stroke-width: 10;
}

.new-canvas-container .canvas-wrapper {
  position: relative;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  flex-shrink: 0;
  display: inline-block;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.new-canvas-container .canvas-wrapper:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.4);
}

.new-canvas-container .canvas-item {
  background-color: #ffffff;
  border-radius: 12px;
}

.new-canvas-container .corner {
  position: absolute;
  width: 500px;
  height: 500px;
  border-color: rgba(255, 255, 255, 0.5);
  border-style: solid;
  border-width: 0;
  z-index: 10;
  pointer-events: none;
  background: transparent;
}

.new-canvas-container .corner-tl {
  top: 15px;
  left: 15px;
  border-top-width: 6px;
  border-left-width: 6px;
  border-top-left-radius: 18px;
}

.new-canvas-container .corner-tr {
  top: 15px;
  right: 15px;
  border-top-width: 6px;
  border-right-width: 6px;
  border-top-right-radius: 18px;
}

.new-canvas-container .corner-bl {
  bottom: 15px;
  left: 15px;
  border-bottom-width: 6px;
  border-left-width: 6px;
  border-bottom-left-radius: 18px;
}

.new-canvas-container .corner-br {
  bottom: 15px;
  right: 15px;
  border-bottom-width: 6px;
  border-right-width: 6px;
  border-bottom-right-radius: 18px;
}

.new-canvas-container .topVis_straps_item.isSwing {
  animation: swing 1.2s linear forwards;
}

@keyframes swing {
  0% { transform: rotate(0deg); }
  50% { transform: rotate(20deg); }
  100% { transform: rotate(0deg); }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .hero-title {
    font-size: 56px;
  }
  
  .section-title {
    font-size: 40px;
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 40px;
  }
  
  .hero-subtitle {
    font-size: 20px;
  }
  
  .section {
    padding: 0 20px;
    margin-bottom: 60px;
  }
  
  .section-title {
    font-size: 32px;
  }
  
  .section-subtitle {
    font-size: 16px;
  }
  
  .features-grid,
  .testimonials-grid {
    grid-template-columns: 1fr;
  }
  
  .scenic-grid {
    grid-template-columns: 1fr;
  }
  
  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .cta-title {
    font-size: 32px;
  }
  
  .new-canvas-container {
    overflow-x: auto;
    justify-content: flex-start;
  }
  
  .new-canvas-container .canvas-wrapper {
    flex-shrink: 0;
  }
}
</style>