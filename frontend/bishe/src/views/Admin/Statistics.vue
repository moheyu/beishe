<template>
  <div class="statistics-page">
    <h2>数据统计</h2>
    <el-card>
      <div ref="chartRef" class="chart-container"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)
let chart = null

const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  const option = {
    title: { text: '访问统计' },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '访问量',
        type: 'line',
        data: [120, 200, 150, 80, 70, 110, 130],
        smooth: true
      }
    ]
  }
  chart.setOption(option)
}

const handleResize = () => {
  chart?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
})
</script>

<style scoped>
.statistics-page {
  padding: 0;
}

.statistics-page h2 {
  margin: 0 0 24px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

.chart-container {
  height: 400px;
}
</style>
