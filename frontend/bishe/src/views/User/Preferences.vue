<template>
  <div class="preferences-page">
    <h2>偏好设置</h2>
    <el-form label-width="120px" v-loading="loading">
      <el-form-item label="喜欢的景点类型">
        <el-checkbox-group v-model="preferences.scenicTypes">
          <el-checkbox label="自然风光" />
          <el-checkbox label="历史古迹" />
          <el-checkbox label="主题乐园" />
          <el-checkbox label="乡村田园" />
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="预算范围">
        <el-slider v-model="preferences.budget" :max="10000" :step="100" show-input />
      </el-form-item>
      <el-form-item label="出行季节">
        <el-select v-model="preferences.season" placeholder="请选择">
          <el-option label="春季" value="spring" />
          <el-option label="夏季" value="summer" />
          <el-option label="秋季" value="autumn" />
          <el-option label="冬季" value="winter" />
          <el-option label="全年" value="all" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saving">保存设置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserPreference, updateUserPreference } from '@/api/preference'

const categoryMap = {
  '自然风光': 11,
  '历史古迹': 10,
  '主题乐园': 12,
  '乡村田园': 5
}

const preferences = reactive({
  scenicTypes: [],
  budget: 5000,
  season: ''
})

const loading = ref(false)
const saving = ref(false)

const loadPreferences = async () => {
  loading.value = true
  try {
    const res = await getUserPreference()
    if (res && res.length > 0) {
      // 找出 budget 和 season（存放在 categoryId 为 null 的记录中）
      const commonPref = res.find(p => p.categoryId === null && (p.budget !== null || p.season !== null))
      if (commonPref) {
        preferences.budget = commonPref.budget || 5000
        preferences.season = commonPref.season || ''
      }

      // 找出分类偏好，转换为名称数组
      const categoryPrefs = res.filter(p => p.categoryId !== null)
      preferences.scenicTypes = categoryPrefs.map(p => {
        const entry = Object.entries(categoryMap).find(([_, id]) => id === p.categoryId)
        return entry ? entry[0] : null
      }).filter(Boolean)
    }
  } catch (error) {
    console.error('加载偏好设置失败', error)
    ElMessage.error('加载偏好设置失败')
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    // 将分类名称转换为 ID 数组
    const categoryIds = preferences.scenicTypes.map(name => categoryMap[name]).filter(Boolean)
    
    await updateUserPreference({
      categoryIds,
      budget: preferences.budget,
      season: preferences.season
    })
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('保存偏好设置失败', error)
    ElMessage.error('保存偏好设置失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadPreferences()
})
</script>

<style scoped>
.preferences-page h2 {
  margin: 0 0 20px;
}
</style>
