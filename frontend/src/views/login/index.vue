<template>
  <div class="login-container">
    <div class="video-section">
      <video class="background-video" autoplay loop muted playsinline>
        <source src="/background-video.mp4" type="video/mp4">
      </video>
      <div class="video-overlay">
        <div class="welcome-text">
                 <h1>欢迎回来！</h1>
          <p>请登录您的账户以继续使用我们的服务</p>
        </div>
      </div>
    </div>
    
    <div class="login-section">
      <div class="login-box">
        <el-tabs v-model="activeTab" class="login-tabs">
          <el-tab-pane label="登录" name="login">
            <el-form 
              ref="loginForm" 
              :model="loginForm" 
              :rules="loginRules" 
              class="login-form"
            >
              <el-form-item prop="username">
                <el-input 
                  v-model="loginForm.username" 
                  placeholder="请输入用户名"
                  prefix-icon="el-icon-user"
                ></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input 
                  v-model="loginForm.password" 
                  type="password" 
                  placeholder="请输入密码"
                  prefix-icon="el-icon-lock"
                  show-password
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button 
                  type="primary" 
                  class="login-button" 
                  :loading="loading"
                  @click="handleLogin"
                >
                  登录
                </el-button>
              </el-form-item>
              <div class="register-link">
                <span>还没有账号？</span>
                <el-button type="text" @click="activeTab = 'register'">立即注册</el-button>
              </div>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="注册" name="register">
            <el-form 
              ref="registerForm" 
              :model="registerForm" 
              :rules="registerRules" 
              class="login-form"
            >
              <el-form-item prop="username">
                <el-input 
                  v-model="registerForm.username" 
                  placeholder="请输入用户名"
                  prefix-icon="el-icon-user"
                ></el-input>
              </el-form-item>
              <el-form-item prop="nickname">
                <el-input 
                  v-model="registerForm.nickname" 
                  placeholder="请输入昵称"
                  prefix-icon="el-icon-edit"
                ></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input 
                  v-model="registerForm.password" 
                  type="password" 
                  placeholder="请输入密码"
                  prefix-icon="el-icon-lock"
                  show-password
                ></el-input>
              </el-form-item>
              <el-form-item prop="confirmPassword">
                <el-input 
                  v-model="registerForm.confirmPassword" 
                  type="password" 
                  placeholder="请确认密码"
                  prefix-icon="el-icon-lock"
                  show-password
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button 
                  type="primary" 
                  class="login-button" 
                  :loading="loading"
                  @click="handleRegister"
                >
                  注册
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import { login, register } from '@/api/auth'

export default {
  name: 'Login',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      activeTab: 'login',
      loading: false,
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ]
      },
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(res => {
            this.$store.commit('SET_TOKEN', res.data.token)
            this.$store.commit('SET_USER_INFO', res.data.userInfo)
            
            this.$message.success('登录成功')
            this.loading = false

            const userRole = res.data.userInfo?.role || ''
            const redirectPath = (userRole === 1 || userRole === 2 || String(userRole) === 'admin') ? '/admin' : '/home'

            this.$router.push(redirectPath).catch(err => {
              console.error('路由跳转失败:', err)
              window.location.href = redirectPath
            })
          }).catch(() => {
            this.loading = false
            this.$message.error('登录失败，请检查账号密码')
          })
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          const { confirmPassword, ...registerData } = this.registerForm
          register(registerData).then(() => {
            this.$message.success('注册成功，请登录')
            this.activeTab = 'login'
            this.loading = false
          }).catch(() => {
            this.loading = false
            this.$message.error('注册失败，请重试')
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.video-section {
  flex: 1;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.background-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  color: white;
}

.welcome-text h1 {
  font-size: 36px;
  margin: 0 0 15px 0;
  font-weight: 600;
}

.welcome-text p {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.login-section {
  width: 450px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.login-box {
  width: 100%;
  max-width: 400px;
}

.login-tabs {
  width: 100%;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  margin-top: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.login-button:hover {
  background: linear-gradient(135deg, #5568d3 0%, #6a3d8c 100%);
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #999;
}

.register-link span {
  margin-right: 10px;
}

:deep(.el-tabs__nav-wrap::after) {
  display: none;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-input__inner) {
  height: 45px;
  line-height: 45px;
}
</style>
