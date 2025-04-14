<template>
  <div class="app-container">
    <!-- <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="搜索评价内容"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div> -->

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="evalId" align="center" width="80" />
      <el-table-column label="用户ID" prop="studentId" align="center" width="80" />
      <el-table-column label="菜品ID" prop="dishId" align="center" width="80" />
      <el-table-column label="评分" align="center" width="400">
        <template slot-scope="{row}">
          <div class="score-item">
            <span class="score-label">口味：</span>
            <el-rate
              v-model="row.tasteScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </div>
          <div class="score-item">
            <span class="score-label">外观：</span>
            <el-rate
              v-model="row.colorScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </div>
          <div class="score-item">
            <span class="score-label">分量：</span>
            <el-rate
              v-model="row.quantityScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </div>
          <div class="score-item">
            <span class="score-label">性价比：</span>
            <el-rate
              v-model="row.pricePerformanceScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="评价内容" prop="comment" align="center" show-overflow-tooltip />
      <el-table-column label="评价时间" prop="evalTime" align="center" width="160" />
      <el-table-column label="操作" align="center" width="120">
        <template slot-scope="{row}">
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getEvaluationList, deleteEvaluation } from '@/api/evaluation'
import Pagination from '@/components/Pagination'

export default {
  name: 'Evaluation',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getEvaluationList(this.listQuery)
        this.list = response.data
        this.total = response.data.length
      } catch (error) {
        console.error('获取评价列表失败:', error)
      }
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDelete(row) {
      this.$confirm('确定要删除该评价吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteEvaluation(row.evalId)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除评价失败:', error)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  
  .filter-container {
    padding-bottom: 10px;
    
    .filter-item {
      display: inline-block;
      vertical-align: middle;
      margin-bottom: 10px;
      margin-right: 10px;
    }
  }
  
  .el-table {
    margin-top: 15px;
    
    .score-item {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .score-label {
        width: 60px;
        text-align: right;
        margin-right: 10px;
        color: #606266;
      }
      
      .el-rate {
        margin-top: -2px;
      }
    }
  }
}
</style> 