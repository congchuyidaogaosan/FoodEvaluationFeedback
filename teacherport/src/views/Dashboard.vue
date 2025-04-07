<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>总用户数</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.userCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>总菜品数</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.dishCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>总评价数</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.evaluationCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>今日评价</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.todayEvaluationCount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {
        userCount: 0,
        dishCount: 0,
        evaluationCount: 0,
        todayEvaluationCount: 0
      }
    }
  },
  created() {
    // 这里可以调用API获取统计数据
    this.getStatistics()
  },
  methods: {
    getStatistics() {
      // 模拟数据
      this.statistics = {
        userCount: 100,
        dishCount: 50,
        evaluationCount: 500,
        todayEvaluationCount: 20
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  
  .card-body {
    text-align: center;
    padding: 20px 0;
    
    .number {
      font-size: 24px;
      font-weight: bold;
      color: #409EFF;
    }
  }
}
</style> 