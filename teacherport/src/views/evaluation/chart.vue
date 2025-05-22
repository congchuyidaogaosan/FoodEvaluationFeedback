<template>
  <div class="app-container">
    <el-card class="chart-card">
      <div slot="header" class="clearfix">
        <span>菜品评价统计</span>
        <el-select v-model="selectedDish" placeholder="选择菜品" @change="loadChartData" style="float: right; width: 200px;">
          <el-option
            v-for="item in dishList"
            :key="item.dishId"
            :label="item.dishName"
            :value="item.dishId"
          />
        </el-select>
      </div>
      <div id="chart-container" style="width: 100%; height: 400px;"></div>
    </el-card>

    <el-row :gutter="20" class="summary-row">
      <el-col :span="6">
        <el-card class="summary-card">
          <div class="summary-title">评价总数</div>
          <div class="summary-value">{{ statistics.totalEvaluations }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card">
          <div class="summary-title">平均口味评分</div>
          <div class="summary-value">{{ statistics.avgTasteScore.toFixed(1) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card">
          <div class="summary-title">平均外观评分</div>
          <div class="summary-value">{{ statistics.avgColorScore.toFixed(1) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card">
          <div class="summary-title">平均性价比评分</div>
          <div class="summary-value">{{ statistics.avgPricePerformanceScore.toFixed(1) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="chart-card">
      <div slot="header" class="clearfix">
        <span>每月评价趋势</span>
      </div>
      <div id="trend-chart-container" style="width: 100%; height: 400px;"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getEvaluationStatistics, getEvaluationTrend } from '@/api/statistics'
import { getDishList } from '@/api/dish'

export default {
  name: 'EvaluationChart',
  data() {
    return {
      dishList: [],
      selectedDish: null,
      radarChart: null,
      trendChart: null,
      statistics: {
        totalEvaluations: 0,
        avgTasteScore: 0,
        avgColorScore: 0,
        avgQuantityScore: 0,
        avgPricePerformanceScore: 0
      }
    }
  },
  async mounted() {
    await this.loadDishes()
    this.initCharts()
    if (this.dishList.length > 0) {
      this.selectedDish = this.dishList[0].dishId
      this.loadChartData()
    }
    this.loadTrendData()
  },
  beforeDestroy() {
    if (this.radarChart) {
      this.radarChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    async loadDishes() {
      try {
        const res = await getDishList()
        this.dishList = res.data
      } catch (error) {
        console.error('获取菜品列表失败:', error)
        this.$message.error('获取菜品列表失败')
      }
    },
    initCharts() {
      // 雷达图
      const radarContainer = document.getElementById('chart-container')
      this.radarChart = echarts.init(radarContainer)
      
      // 趋势图
      const trendContainer = document.getElementById('trend-chart-container')
      this.trendChart = echarts.init(trendContainer)
      
      // 监听窗口大小变化
      window.addEventListener('resize', () => {
        this.radarChart.resize()
        this.trendChart.resize()
      })
    },
    async loadChartData() {
      if (!this.selectedDish) return
      
      try {
        const res = await getEvaluationStatistics(this.selectedDish)
        this.statistics = res.data
        
        // 更新雷达图
        this.radarChart.setOption({
          title: {
            text: '菜品评价维度分析'
          },
          tooltip: {},
          radar: {
            indicator: [
              { name: '口味', max: 5 },
              { name: '色泽', max: 5 },
              { name: '分量', max: 5 },
              { name: '性价比', max: 5 }
            ]
          },
          series: [{
            name: '评分详情',
            type: 'radar',
            data: [
              {
                value: [
                  this.statistics.avgTasteScore, 
                  this.statistics.avgColorScore, 
                  this.statistics.avgQuantityScore, 
                  this.statistics.avgPricePerformanceScore
                ],
                name: '平均分'
              }
            ]
          }]
        })
      } catch (error) {
        console.error('获取评价统计数据失败:', error)
        this.$message.error('获取评价统计数据失败')
      }
    },
    async loadTrendData() {
      try {
        const res = await getEvaluationTrend()
        const data = res.data
        
        const months = data.map(item => item.month)
        const counts = data.map(item => item.count)
        
        this.trendChart.setOption({
          title: {
            text: '评价数量趋势'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: months
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: counts,
            type: 'line',
            smooth: true
          }]
        })
      } catch (error) {
        console.error('获取评价趋势数据失败:', error)
        this.$message.error('获取评价趋势数据失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  
  .chart-card {
    margin-bottom: 20px;
  }
  
  .summary-row {
    margin-bottom: 20px;
  }
  
  .summary-card {
    height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    
    .summary-title {
      font-size: 14px;
      color: #606266;
      margin-bottom: 10px;
    }
    
    .summary-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
    }
  }
}
</style> 