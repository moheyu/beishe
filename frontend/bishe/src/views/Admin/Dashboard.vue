<template>
  <div class="dashboard-page">
    <h1 class="page-title">仪表盘</h1>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon blue">
            <el-icon size="28"><Picture /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.scenicCount }}</div>
            <div class="stat-label">景点数量</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon green">
            <el-icon size="28"><MapLocation /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.routeCount }}</div>
            <div class="stat-label">路线数量</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon orange">
            <el-icon size="28"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount }}</div>
            <div class="stat-label">用户数量</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon red">
            <el-icon size="28"><View /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.viewCount }}</div>
            <div class="stat-label">总访问量</div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-grid">
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>景点分类统计</span>
          </div>
        </template>
        <div ref="scenicChartRef" class="chart-container"></div>
      </el-card>
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>用户增长趋势</span>
          </div>
        </template>
        <div ref="userChartRef" class="chart-container"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { Picture, MapLocation, User, View } from '@element-plus/icons-vue'
import { getOverallStatistics, getRecommendationStatistics } from '@/api/statistics'
import { getScenicCategoryList } from '@/api/scenicCategory'

const stats = ref({
  scenicCount: 0,
  routeCount: 0,
  userCount: 0,
  viewCount: 0
})
const scenicChartRef = ref(null)
const userChartRef = ref(null)
let scenicChart = null
let userChart = null

// 加载统计数据
const loadStatistics = async () => {
  try {
    const res = await getOverallStatistics()
    if (res) {
      stats.value = {
        scenicCount: res.totalScenics || 0,
        routeCount: res.totalRoutes || 0,
        userCount: res.totalUsers || 0,
        viewCount: res.totalViews || 0
      }
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

// 加载景点分类统计数据
const loadScenicCategoryStats = async () => {
  try {
    const categories = await getScenicCategoryList()
    if (categories && categories.length > 0) {
      // 使用真实分类数据，value 先使用随机分布，后续可从后端获取真实数量
      const data = categories.map(cat => ({
        value: Math.floor(Math.random() * 50) + 10, // 临时使用随机数，后续替换为真实数据
        name: cat.name
      }))
      initScenicChart(data)
    } else {
      // 如果没有分类数据，使用默认数据
      initScenicChart([
        { value: 40, name: '自然风光' },
        { value: 30, name: '人文古迹' },
        { value: 20, name: '主题乐园' },
        { value: 10, name: '其他' }
      ])
    }
  } catch (error) {
    console.error('加载分类统计失败', error)
    initScenicChart([
      { value: 40, name: '自然风光' },
      { value: 30, name: '人文古迹' },
      { value: 20, name: '主题乐园' },
      { value: 10, name: '其他' }
    ])
  }
}

const initScenicChart = (data) => {
  if (!scenicChartRef.value) return
  
  scenicChart = echarts.init(scenicChartRef.value)
  const option = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%' },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: { show: false },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        data: data
      }
    ]
  }
  scenicChart.setOption(option)
}

const initUserChart = () => {
  if (!userChartRef.value) return
  
  userChart = echarts.init(userChartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: { type: 'value' },
    series: [
      {
        data: [120, 200, 150, 80, 70, 110],
        type: 'line',
        smooth: true,
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
            ]
          }
        }
      }
    ]
  }
  userChart.setOption(option)
}

const handleResize = () => {
  scenicChart?.resize()
  userChart?.resize()
}

onMounted(() => {
  loadStatistics()
  loadScenicCategoryStats()
  initUserChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  scenicChart?.dispose()
  userChart?.dispose()
})
</script>

<style scoped>
.dashboard-page {
  padding: 20px;
}

.page-title {
  margin: 0 0 24px;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 12px;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 8px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 16px;
  flex-shrink: 0;
}

.stat-icon.blue { background: linear-gradient(135deg, #409eff, #79bbff); }
.stat-icon.green { background: linear-gradient(135deg, #67c23a, #95d475); }
.stat-icon.orange { background: linear-gradient(135deg, #e6a23c, #eebe77); }
.stat-icon.red { background: linear-gradient(135deg, #f56c6c, #f89898); }

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

/* 图表区域 */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 24px;
}

.chart-card {
  border-radius: 12px;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
  color: #303133;
}

.chart-container {
  height: 320px;
}

</style>
