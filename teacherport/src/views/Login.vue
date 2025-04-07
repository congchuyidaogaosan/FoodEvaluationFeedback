<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title">
        <img src="https://img.icons8.com/fluency/48/restaurant.png" alt="logo">
        <span>食堂评价系统</span>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="el-icon-user"
            class="login-input"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="el-icon-lock"
            class="login-input"
            @keyup.enter.native="handleLogin"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="login-button"
            @click.native.prevent="handleLogin"
          >登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(response => {
            const { token, username } = response.data
            localStorage.setItem('token', token)
            localStorage.setItem('username', username)
            this.$router.push('/')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #1f4c6a 0%, #2d3a4b 100%);
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80') no-repeat center center;
    background-size: cover;
    opacity: 0.1;
    z-index: 1;
  }
  
  .login-box {
    width: 420px;
    padding: 40px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    position: relative;
    z-index: 2;
    animation: fadeIn 0.5s ease-in-out;
    
    .title {
      font-size: 24px;
      color: #333;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: bold;
      display: flex;
      align-items: center;
      justify-content: center;
      
      img {
        width: 40px;
        height: 40px;
        margin-right: 12px;
      }
    }
    
    .login-form {
      .el-form-item {
        margin-bottom: 30px;
      }
      
      .login-input {
        /deep/ .el-input__inner {
          height: 45px;
          line-height: 45px;
          border-radius: 4px;
          border: 1px solid #dcdfe6;
          transition: all 0.3s;
          
          &:focus {
            border-color: #409EFF;
            box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
          }
        }
        
        /deep/ .el-input__prefix {
          left: 15px;
          font-size: 18px;
          color: #909399;
        }
      }
      
      .login-button {
        width: 100%;
        height: 45px;
        font-size: 16px;
        border-radius: 4px;
        background: #409EFF;
        border: none;
        transition: all 0.3s;
        
        &:hover {
          background: #66b1ff;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
        }
        
        &:active {
          transform: translateY(0);
        }
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 