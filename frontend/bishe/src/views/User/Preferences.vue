<template>
  <div class="preferences-page">
    <h2>偏好设置</h2>
    <el-form label-width="120px" v-loading="loading">
      <el-form-item label="喜欢的景点类型">
        <el-checkbox-group v-model="preferences.scenicTypes">
          <el-checkbox label="自然风光" />
          <el-checkbox label="人文古迹" />
          <el-checkbox label="主题乐园" />
          <el-checkbox label="休闲度假" />
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
    if (res) {
      preferences.scenicTypes = res.scenicTypes || []
      preferences.budget = res.budget || 5000
      preferences.season = res.season || ''
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
    await updateUserPreference(preferences)
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
