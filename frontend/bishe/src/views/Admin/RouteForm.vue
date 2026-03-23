<template>
  <div class="route-form-page">
    <h2>{{ isEdit ? '编辑路线' : '新增路线' }}</h2>
    <el-card class="form-card">
      <el-form :model="form" label-width="100px" class="route-form">
        <el-form-item label="路线名称" required>
          <el-input v-model="form.name" placeholder="请输入路线名称" />
        </el-form-item>
        
        <el-form-item label="路线分类" required>
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="行程天数" required>
          <el-input-number v-model="form.duration" :min="1" :max="30" />
        </el-form-item>
        
        <el-form-item label="预计预算" required>
          <el-input-number v-model="form.budget" :min="0" :precision="2" />
        </el-form-item>
        
        <el-form-item label="最佳季节">
          <el-input v-model="form.season" placeholder="如：春季、秋季" />
        </el-form-item>
        
        <el-form-item label="封面图片">
          <el-upload
            class="cover-uploader"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleCoverChange"
            v-loading="uploadLoading"
          >
            <el-image v-if="form.coverImage" :src="displayImage" fit="cover" class="cover-preview" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="路线描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入路线描述" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { addRoute, updateRoute, getAdminRouteDetail, getAdminRouteFullDetail, getRouteCategoryList } from '@/api/route'
import { uploadScenicImage } from '@/api/upload'
import { getImageUrl } from '@/utils/image'

const route = useRoute()
const router = useRouter()
const categories = ref([])
const uploadLoading = ref(false)

const isEdit = computed(() => !!route.params.id)

// 存储待上传的文件
const pendingUploadFile = ref(null)

// 计算属性：处理图片显示 URL
const displayImage = computed(() => {
  if (!form.coverImage) return ''
  return getImageUrl(form.coverImage)
})

const form = reactive({
  name: '',
  categoryId: null,
  duration: 3,
  budget: 0,
  season: '',
  coverImage: '',
  description: ''
})

const loadCategories = async () => {
  try {
    const res = await getRouteCategoryList()
    categories.value = res || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadDetail = async () => {
  if (!isEdit.value) return
  try {
    const res = await getAdminRouteDetail(route.params.id)
    Object.assign(form, res)
  } catch (error) {
    console.error('加载路线详情失败', error)
  }
}

const handleCoverChange = (file) => {
  // 仅本地预览，不上传
  form.coverImage = URL.createObjectURL(file.raw)
  pendingUploadFile.value = file.raw
}

const handleSubmit = async () => {
  try {
    let submitData = { ...form }
    
    // 如果有待上传的文件，先上传图片
    if (pendingUploadFile.value) {
      uploadLoading.value = true
      try {
        const res = await uploadScenicImage(pendingUploadFile.value)
        if (res && res.url) {
          submitData.coverImage = res.url
        }
      } catch (error) {
        console.error('图片上传失败', error)
        ElMessage.error('图片上传失败')
        return
      } finally {
        uploadLoading.value = false
      }
    }
    
    if (isEdit.value) {
      await updateRoute(route.params.id, submitData)
      ElMessage.success('更新成功')
    } else {
      await addRoute(submitData)
      ElMessage.success('添加成功')
    }
    router.push('/admin/route')
  } catch (error) {
    console.error('保存失败', error)
  }
}

onMounted(() => {
  loadCategories()
  loadDetail()
})
</script>

<style scoped>
.route-form-page {
  padding: 20px;
}

.route-form-page h2 {
  margin: 0 0 20px;
}

.form-card {
  max-width: 800px;
}

.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 300px;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-uploader:hover {
  border-color: #409eff;
}

.cover-preview {
  width: 100%;
  height: 100%;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
</style>
