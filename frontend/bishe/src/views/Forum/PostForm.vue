<template>
  <div class="post-form-page">
    <div class="form-header">
      <el-button :icon="ArrowLeft" @click="goBack" text>返回</el-button>
      <h2>{{ isEdit ? '编辑帖子' : '发布帖子' }}</h2>
    </div>

    <el-card class="form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" maxlength="100" show-word-limit />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="12"
            placeholder="请输入帖子内容"
            maxlength="5000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="关联景点">
          <el-input v-model.number="form.scenicId" placeholder="可选，输入景点ID" type="number" clearable />
        </el-form-item>

        <el-form-item label="关联路线">
          <el-input v-model.number="form.routeId" placeholder="可选，输入路线ID" type="number" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEdit ? '保存修改' : '发布帖子' }}
          </el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { addForumPost, updateForumPost, getForumPostDetail } from '@/api/forum'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)
const postId = computed(() => route.params.id ? Number(route.params.id) : null)

const form = reactive({
  title: '',
  content: '',
  scenicId: null,
  routeId: null
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { min: 10, message: '内容至少 10 个字符', trigger: 'blur' }
  ]
}

const goBack = () => {
  router.back()
}

const loadPost = async () => {
  if (!isEdit.value) return
  try {
    const res = await getForumPostDetail(postId.value)
    if (res) {
      form.title = res.title || ''
      form.content = res.content || ''
      form.scenicId = res.scenicId || null
      form.routeId = res.routeId || null
    }
  } catch (error) {
    console.error('加载帖子失败', error)
    ElMessage.error('加载帖子信息失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const data = {
      title: form.title,
      content: form.content,
      scenicId: form.scenicId || null,
      routeId: form.routeId || null
    }

    if (isEdit.value) {
      await updateForumPost(postId.value, data)
      ElMessage.success('帖子更新成功')
    } else {
      await addForumPost(data)
      ElMessage.success('帖子发布成功')
    }
    router.push('/user/posts')
  } catch (error) {
    console.error('提交帖子失败', error)
    ElMessage.error(isEdit.value ? '更新帖子失败' : '发布帖子失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadPost()
})
</script>

<style scoped>
.post-form-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.form-header h2 {
  margin: 0;
  font-size: 22px;
  color: #333;
}

.form-card {
  padding: 10px;
}
</style>
