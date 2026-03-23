<template>
  <div class="profile-page">
    <h2>个人资料</h2>
    
    <el-form :model="form" label-width="100px" class="profile-form">
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleAvatarChange"
        >
          <el-avatar v-if="form.avatar" :size="100" :src="avatarUrl" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" />
      </el-form-item>
      
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      
      <el-form-item label="手机号">
        <el-input v-model="form.phone" />
      </el-form-item>
      
      <el-form-item label="个人简介">
        <el-input v-model="form.bio" type="textarea" :rows="4" />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="handleSave">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getImageUrl } from '@/utils/image'

const userStore = useUserStore()

const form = ref({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  bio: '',
  avatar: ''
})

// 处理后的头像 URL
const avatarUrl = computed(() => {
  if (!form.value.avatar) return ''
  return getImageUrl(form.value.avatar)
})

const handleAvatarChange = (file) => {
  form.value.avatar = URL.createObjectURL(file.raw)
}

const handleSave = () => {
  userStore.updateUserInfo(form.value)
  ElMessage.success('保存成功')
}

onMounted(() => {
  if (userStore.userInfo) {
    form.value = { ...userStore.userInfo }
  }
})
</script>

<style scoped>
.profile-page h2 {
  margin: 0 0 30px;
  font-size: 24px;
  color: #333;
}

.profile-form {
  max-width: 500px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
</style>
