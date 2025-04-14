<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="搜索反馈内容"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.isHandled"
        placeholder="处理状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="未处理" :value="0" />
        <el-option label="已处理" :value="1" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="feedbackId" align="center" width="80" />
      <el-table-column label="用户ID" prop="studentId" align="center" width="80" />
      <el-table-column label="反馈内容" prop="content" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="反馈时间" prop="feedbackTime" align="center" width="160" />
      <el-table-column label="处理状态" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isHandled === 1 ? 'success' : 'info'">
            {{ row.isHandled === 1 ? '已处理' : '未处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理结果" prop="handleResult" align="center" min-width="200" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.handleResult || '暂无' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230">
        <template slot-scope="{row}">
          <el-button
            v-if="row.isHandled === 0"
            type="primary"
            size="mini"
            @click="handleReply(row)"
          >
            处理
          </el-button>
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

    <el-dialog title="处理反馈" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="反馈内容">
          <el-input v-model="temp.content" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="temp.handleResult" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="submitReply">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFeedbackList, replyFeedback, deleteFeedback } from '@/api/feedback'
import Pagination from '@/components/Pagination'

export default {
  name: 'Feedback',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: undefined,
        isHandled: undefined
      },
      dialogVisible: false,
      temp: {
        feedbackId: undefined,
        content: '',
        handleResult: ''
      },
      rules: {
        handleResult: [{ required: true, message: '请输入处理结果', trigger: 'blur' }]
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
        const response = await getFeedbackList(this.listQuery)
        this.list = response.data
        this.total = response.data.length
      } catch (error) {
        console.error('获取反馈列表失败:', error)
      }
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReply(row) {
      this.temp = Object.assign({}, row)
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async submitReply() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await replyFeedback(this.temp.feedbackId, {
              handleResult: this.temp.handleResult,
              isHandled: 1
            })
            this.dialogVisible = false
            this.$message.success('处理成功')
            this.getList()
          } catch (error) {
            console.error('处理反馈失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该反馈吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFeedback(row.feedbackId)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除反馈失败:', error)
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
  }
  
  .el-dialog {
    .el-select {
      width: 100%;
    }
  }
}
</style> 