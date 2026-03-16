<template>
  <div class="user-container">
    <!-- 页面标题区域 -->
    

    <el-row :gutter="30" style="margin-top: 40px">
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <el-card class="user-card" shadow="hover">
          <div class="user-info">
            <div class="avatar-wrapper">
              <el-avatar :src="userInfo.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" :size="120"></el-avatar>
              <div class="avatar-badge" @click="editMode = !editMode">
                <i class="el-icon-edit"></i>
              </div>
            </div>
            <h3 class="user-name">{{ userInfo.nickname || userInfo.username }}</h3>
            <p class="username">@{{ userInfo.username }}</p>
            <el-tag :type="userInfo.role === 1 ? 'danger' : 'primary'" size="medium" effect="plain">
              {{ userInfo.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <el-card class="info-card" shadow="hover">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-user"></i>
              个人信息
            </span>
          </div>

          <el-form :model="userForm" :rules="rules" ref="userForm" label-width="100px" v-if="editMode" class="edit-form">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="userForm.nickname" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleAvatarChange"
              >
                <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-check" @click="handleUpdate" :loading="updateLoading">保存</el-button>
              <el-button icon="el-icon-close" @click="editMode = false">取消</el-button>
            </el-form-item>
          </el-form>

          <el-descriptions v-else :column="2" border class="info-descriptions">
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ userInfo.nickname }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="userInfo.role === 1 ? 'danger' : 'primary'" size="small" effect="plain">
                {{ userInfo.role === 1 ? '管理员' : '普通用户' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'" size="small" effect="plain">
                {{ userInfo.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="注册时间" :span="2">{{ userInfo.createTime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="30" style="margin-top: 30px">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="action-card" shadow="hover">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-s-operation"></i>
              快捷操作
            </span>
          </div>
          <div class="action-list">
            <div class="action-item" @click="$router.push('/collection')">
              <div class="action-icon">
                <i class="el-icon-star-off"></i>
              </div>
              <div class="action-content">
                <span class="action-title">我的收藏</span>
                <span class="action-desc">查看收藏的景点</span>
              </div>
              <i class="el-icon-arrow-right action-arrow"></i>
            </div>
            <div class="action-item" @click="$router.push('/recommend')">
              <div class="action-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="action-content">
                <span class="action-title">个性化推荐</span>
                <span class="action-desc">为您推荐的景点</span>
              </div>
              <i class="el-icon-arrow-right action-arrow"></i>
            </div>
            <div class="action-item" @click="$router.push('/scenic')">
              <div class="action-icon">
                <i class="el-icon-s-grid"></i>
              </div>
              <div class="action-content">
                <span class="action-title">浏览景点</span>
                <span class="action-desc">探索更多景点</span>
              </div>
              <i class="el-icon-arrow-right action-arrow"></i>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="account-card" shadow="hover">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-lock"></i>
              账户安全
            </span>
          </div>
          <div class="account-info">
            <div class="account-item">
              <div class="account-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="account-detail">
                <span class="account-label">登录密码</span>
                <span class="account-value">已设置</span>
              </div>
              <i class="el-icon-arrow-right account-arrow"></i>
            </div>
            <div class="account-item">
              <div class="account-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="account-detail">
                <span class="account-label">用户身份</span>
                <span class="account-value">{{ userInfo.role === 1 ? '管理员' : '普通用户' }}</span>
              </div>
              <i class="el-icon-arrow-right account-arrow"></i>
            </div>
            <div class="account-item">
              <div class="account-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="account-detail">
                <span class="account-label">注册时间</span>
                <span class="account-value">{{ userInfo.createTime }}</span>
              </div>
              <i class="el-icon-arrow-right account-arrow"></i>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, updateUser } from '@/api/user'
import { uploadScenicImage } from '@/api/upload'

export default {
  name: 'User',
  data() {
    return {
      userInfo: {},
      userForm: {
        nickname: '',
        avatar: ''
      },
      editMode: false,
      updateLoading: false,
      avatarUploading: false,
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      getUserInfo().then(res => {
        this.userInfo = res.data
        this.userForm = {
          nickname: res.data.nickname,
          avatar: res.data.avatar
        }
      })
    },
    handleUpdate() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.updateLoading = true
          updateUser(this.userForm).then(() => {
            this.$message.success('更新成功')
            this.editMode = false
            
            const updatedUserInfo = {
              ...this.userInfo,
              nickname: this.userForm.nickname,
              avatar: this.userForm.avatar
            }
            this.$store.commit('SET_USER_INFO', updatedUserInfo)
            
            this.loadUserInfo()
          }).finally(() => {
            this.updateLoading = false
          })
        }
      })
    },
    handleAvatarChange(file) {
      const isJPG = file.raw.type === 'image/jpeg' || file.raw.type === 'image/png'
      const isLt5M = file.raw.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!')
        return false
      }
      if (!isLt5M) {
        this.$message.error('上传头像图片大小不能超过 5MB!')
        return false
      }

      this.avatarUploading = true
      uploadScenicImage(file.raw).then(res => {
        this.userForm.avatar = res.data.url
        
        const updatedUserInfo = {
          ...this.userInfo,
          avatar: res.data.url
        }
        this.$store.commit('SET_USER_INFO', updatedUserInfo)
        
        this.$message.success('头像上传成功')
      }).catch(() => {
        this.$message.error('头像上传失败')
      }).finally(() => {
        this.avatarUploading = false
      })
      return false
    }
  }
}
</script>

<style scoped>
/* ===== 现代化自然风设计 ===== */

/* 全局容器 */
.user-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px;
  background: linear-gradient(180deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  box-sizing: border-box;
}

/* 页面标题区域 */
.page-header {
  text-align: center;
  margin-bottom: 60px;
  padding-top: 140px;
  animation: fadeInDown 0.8s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 48px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 16px;
  letter-spacing: -1px;
}

.page-subtitle {
  font-size: 20px;
  color: #7f8c8d;
  margin: 0;
  font-weight: 300;
}

/* 用户卡片 */
.user-card {
  text-align: center;
  border-radius: 20px;
  border: none;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out 0.2s both;
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

.user-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
}

.user-info {
  padding: 30px 20px;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.avatar-wrapper .el-avatar {
  border: 4px solid white;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  background-color: transparent;
}

.avatar-wrapper .el-avatar img {
  object-fit: cover;
  width: 100%;
  height: 100%;
}

.avatar-wrapper:hover .el-avatar {
  transform: scale(1.05);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2);
}

.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  border: 3px solid white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.avatar-badge:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.user-name {
  font-size: 26px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 8px;
  transition: color 0.3s ease;
}

.user-card:hover .user-name {
  color: #667eea;
}

.username {
  font-size: 16px;
  color: #95a5a6;
  margin: 0 0 20px;
  font-weight: 300;
}

/* 信息卡片 */
.info-card {
  height: 100%;
  border-radius: 20px;
  border: none;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.info-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-title i {
  font-size: 20px;
  color: #667eea;
}

.edit-form {
  padding: 20px 0;
}

.info-descriptions {
  margin-top: 10px;
}

/* 头像上传 */
.avatar-uploader {
  text-align: center;
}

.avatar-uploader .el-upload {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  width: 178px;
  height: 178px;
  display: inline-block;
}

.avatar-uploader .el-upload:hover {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  transition: all 0.3s ease;
}

.avatar-uploader:hover .avatar-uploader-icon {
  color: #667eea;
  transform: scale(1.1);
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 12px;
  object-fit: cover;
}

/* 快捷操作卡片 */
.action-card {
  height: 100%;
  border-radius: 20px;
  border: none;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out 0.6s both;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8f4f8 100%);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.action-item:hover {
  background: linear-gradient(135deg, #e8f4f8 0%, #d4e8ed 100%);
  border-color: #667eea;
  transform: translateX(5px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.action-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  transition: all 0.3s ease;
}

.action-item:hover .action-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.action-icon i {
  font-size: 24px;
  color: white;
}

.action-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.action-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  transition: color 0.3s ease;
}

.action-item:hover .action-title {
  color: #667eea;
}

.action-desc {
  font-size: 14px;
  color: #7f8c8d;
}

.action-arrow {
  font-size: 20px;
  color: #95a5a6;
  transition: all 0.3s ease;
}

.action-item:hover .action-arrow {
  color: #667eea;
  transform: translateX(5px);
}

/* 账户安全卡片 */
.account-card {
  height: 100%;
  border-radius: 20px;
  border: none;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease-out 0.8s both;
}

.account-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
}

.account-info {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.account-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8f4f8 100%);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.account-item:hover {
  background: linear-gradient(135deg, #e8f4f8 0%, #d4e8ed 100%);
  border-color: #667eea;
  transform: translateX(5px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.account-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  transition: all 0.3s ease;
}

.account-item:hover .account-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.account-icon i {
  font-size: 24px;
  color: white;
}

.account-detail {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.account-label {
  font-size: 14px;
  color: #7f8c8d;
  font-weight: 500;
}

.account-value {
  font-size: 16px;
  color: #2c3e50;
  font-weight: 600;
  transition: color 0.3s ease;
}

.account-item:hover .account-value {
  color: #667eea;
}

.account-arrow {
  font-size: 20px;
  color: #95a5a6;
  transition: all 0.3s ease;
}

.account-item:hover .account-arrow {
  color: #667eea;
  transform: translateX(5px);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .page-title {
    font-size: 40px;
  }
}

@media (max-width: 768px) {
  .user-container {
    padding: 20px;
  }
  
  .page-header {
    margin-bottom: 40px;
  }
  
  .page-title {
    font-size: 32px;
  }
  
  .page-subtitle {
    font-size: 16px;
  }
  
  .user-info {
    padding: 20px;
  }
  
  .action-item,
  .account-item {
    padding: 15px;
  }
  
  .action-icon,
  .account-icon {
    width: 45px;
    height: 45px;
  }
  
  .action-icon i,
  .account-icon i {
    font-size: 20px;
  }
  
  .action-title {
    font-size: 15px;
  }
  
  .action-desc {
    font-size: 13px;
  }
}
</style>
