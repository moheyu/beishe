<template>
  <div class="scenic-form-page">
    <h2>{{ isEdit ? '编辑景点' : '新增景点' }}</h2>
    <el-card class="form-card">
      <el-form :model="form" label-width="100px" class="scenic-form">
        <el-form-item label="景点名称" required>
          <el-input v-model="form.name" placeholder="请输入景点名称" />
        </el-form-item>
        
        <el-form-item label="景点分类" required>
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="景点位置" required>
          <el-input v-model="form.location" placeholder="请输入景点位置" />
        </el-form-item>
        
        <el-form-item label="门票价格" required>
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        
        <el-form-item label="封面图片">
          <div class="cover-uploader" @click="triggerFileInput">
            <el-image v-if="displayImage" :src="displayImage" fit="cover" class="cover-preview" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
            <input 
              ref="fileInput" 
              type="file" 
              accept="image/*" 
              style="display: none" 
              @change="handleCoverChange"
            />
          </div>
        </el-form-item>
        
        <el-form-item label="景点描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入景点描述" />
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
import { addScenic, updateScenic, getAdminScenicDetail } from '@/api/scenic'
import { getScenicCategoryList } from '@/api/scenicCategory'
import { uploadScenicImage } from '@/api/upload'

const route = useRoute()
const router = useRouter()
const fileInput = ref(null)
const categories = ref([])
const uploadLoading = ref(false)

const isEdit = computed(() => !!route.params.id)

// 计算属性：处理图片显示 URL
const displayImage = computed(() => {
  if (!form.coverImage) return ''
  // 如果已经是完整 URL 或 blob URL，直接返回
  if (form.coverImage.startsWith('http') || form.coverImage.startsWith('blob:')) {
    return form.coverImage
  }
  // 拼接完整 URL
  return `http://localhost:8080/api${form.coverImage}`
})

const form = reactive({
  name: '',
  categoryId: null,
  location: '',
  price: 0,
  coverImage: '',
  description: ''
})

const loadCategories = async () => {
  try {
    const res = await getScenicCategoryList()
    categories.value = res || []
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadDetail = async () => {
  if (!isEdit.value) return
  try {
    const res = await getAdminScenicDetail(route.params.id)
    // 将 images 数组转为 coverImage 字符串
    if (res.images && res.images.length > 0) {
      res.coverImage = res.images[0]
    }
    Object.assign(form, res)
  } catch (error) {
    console.error('加载景点详情失败', error)
  }
}

// 存储待上传的文件
const pendingUploadFile = ref(null)

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleCoverChange = (event) => {
  const file = event.target.files[0]
  if (!file) return
  // 仅本地预览，不上传
  const localUrl = URL.createObjectURL(file)
  form.coverImage = localUrl
  pendingUploadFile.value = file
}

const handleSubmit = async () => {
  try {
    let imageUrl = form.coverImage
    
    // 如果有待上传的文件，先上传图片
    if (pendingUploadFile.value) {
      uploadLoading.value = true
      try {
        const res = await uploadScenicImage(pendingUploadFile.value)
        if (res && res.url) {
          imageUrl = res.url
        }
      } catch (error) {
        console.error('图片上传失败', error)
        ElMessage.error('图片上传失败')
        return
      } finally {
        uploadLoading.value = false
      }
    }
    
    // 构造提交数据，将 coverImage 转为 images 数组格式
    const submitData = {
      ...form,
      images: imageUrl ? [imageUrl] : []
    }
    // 删除前端使用的 coverImage 字段
    delete submitData.coverImage
    
    if (isEdit.value) {
      await updateScenic(route.params.id, submitData)
      ElMessage.success('更新成功')
    } else {
      await addScenic(submitData)
      ElMessage.success('添加成功')
    }
    router.push('/admin/scenic')
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
.scenic-form-page {
  padding: 0;
}

.scenic-form-page h2 {
  margin: 0 0 24px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
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
