<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <div class="logo">
        <img src="https://img.icons8.com/fluency/48/restaurant.png" alt="logo">
        <span>食堂评价系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/user">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/dish">
          <i class="el-icon-food"></i>
          <span>菜品管理</span>
        </el-menu-item>
        <el-menu-item index="/category">
          <i class="el-icon-menu"></i>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/evaluation">
          <i class="el-icon-chat-dot-round"></i>
          <span>评价管理</span>
        </el-menu-item>
        <el-menu-item index="/feedback">
          <i class="el-icon-message"></i>
          <span>反馈回复</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <i class="el-icon-data-analysis"></i>
          <span>评价统计</span>
        </el-menu-item>
        <el-menu-item index="/notice">
          <i class="el-icon-bell"></i>
          <span>通知发布</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="main-container">
      <div class="navbar">
        <div class="right-menu">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              {{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div class="app-main">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      username: localStorage.getItem('username') || '管理员'
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    logout() {
      this.$confirm('确定退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        this.$router.push('/login')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  background: #f0f2f5;
  
  .sidebar-container {
    width: 210px;
    height: 100%;
    background: #304156;
    transition: width 0.28s;
    box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
    z-index: 1001;
    
    .logo {
      height: 60px;
      line-height: 60px;
      text-align: center;
      color: #fff;
      font-size: 18px;
      font-weight: 600;
      padding: 10px 0;
      background: #2b2f3a;
      box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      
      img {
        width: 32px;
        height: 32px;
        vertical-align: middle;
        margin-right: 12px;
      }
    }
    
    .el-menu {
      border-right: none;
      
      .el-menu-item {
        height: 50px;
        line-height: 50px;
        font-size: 14px;
        
        &:hover {
          background: #263445 !important;
        }
        
        &.is-active {
          background: #263445 !important;
          border-right: 3px solid #409EFF;
        }
        
        i {
          margin-right: 16px;
          font-size: 18px;
        }
      }
    }
  }
  
  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    
    .navbar {
      height: 60px;
      line-height: 60px;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      padding: 0 20px;
      
      .right-menu {
        float: right;
        
        .el-dropdown-link {
          cursor: pointer;
          color: #409EFF;
          display: flex;
          align-items: center;
          
          i {
            margin-left: 8px;
            transition: transform 0.3s;
          }
          
          &:hover i {
            transform: rotate(180deg);
          }
        }
      }
    }
    
    .app-main {
      flex: 1;
      padding: 20px;
      overflow-y: auto;
      background: #f0f2f5;
    }
  }
}

// 路由切换动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style> 
  transform: translateX(30px);
</style> 