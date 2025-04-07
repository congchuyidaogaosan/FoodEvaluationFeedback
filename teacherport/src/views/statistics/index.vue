<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>总评价数</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.total }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>平均评分</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.averageRating }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>好评率</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.goodRate }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>今日评价</span>
          </div>
          <div class="card-body">
            <div class="number">{{ statistics.todayCount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>评分分布</span>
          </div>
          <div class="chart-container">
            <div ref="ratingChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>菜品评价数TOP10</span>
          </div>
          <div class="chart-container">
            <div ref="dishChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span>每日评价趋势</span>
          </div>
          <div class="chart-container">
            <div ref="trendChart" style="width: 100%; height: 400px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStatistics } from '@/api/statistics'
import * as echarts from 'echarts'

export default {
  name: 'Statistics',
  data() {
    return {
      statistics: {
        total: 0,
        averageRating: 0,
        goodRate: 0,
        todayCount: 0
      },
      ratingChart: null,
      dishChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getData()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.ratingChart) {
      this.ratingChart.dispose()
    }
    if (this.dishChart) {
      this.dishChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    async getData() {
      try {
        const { data } = await getStatistics()
        this.statistics = data
        this.initCharts(data)
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    initCharts(data) {
      // 初始化评分分布图表
      this.ratingChart = echarts.init(this.$refs.ratingChart)
      this.ratingChart.setOption({
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '评分分布',
            type: 'pie',
            radius: '50%',
            data: data.ratingDistribution,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })

      // 初始化菜品评价数TOP10图表
      this.dishChart = echarts.init(this.$refs.dishChart)
      this.dishChart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.dishTop10.map(item => item.name)
        },
        series: [
          {
            name: '评价数',
            type: 'bar',
            data: data.dishTop10.map(item => item.count)
          }
        ]
      })

      // 初始化每日评价趋势图表
      this.trendChart = echarts.init(this.$refs.trendChart)
      this.trendChart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.dailyTrend.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '评价数',
            type: 'line',
            data: data.dailyTrend.map(item => item.count)
          }
        ]
      })
    },
    resizeCharts() {
      if (this.ratingChart) {
        this.ratingChart.resize()
      }
      if (this.dishChart) {
        this.dishChart.resize()
      }
      if (this.trendChart) {
        this.trendChart.resize()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-container {
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
  
  .chart-row {
    margin-top: 20px;
    
    .chart-container {
      padding: 20px;
    }
  }
}
</style> 