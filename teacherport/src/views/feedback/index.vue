<template>
  <div class="feedback-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="搜索反馈内容"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
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
      <el-table-column label="ID" prop="id" align="center" width="80" />
      <el-table-column label="用户" prop="userName" align="center" />
      <el-table-column label="反馈内容" prop="content" align="center" />
      <el-table-column label="回复内容" prop="reply" align="center" />
      <el-table-column label="状态" prop="status" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已回复' : '未回复' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" />
      <el-table-column label="操作" align="center" width="230">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleReply(row)">
            回复
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

    <el-dialog title="回复反馈" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="反馈内容" prop="content">
          <el-input v-model="temp.content" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="回复内容" prop="reply">
          <el-input v-model="temp.reply" type="textarea" :rows="3" />
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
        keyword: undefined
      },
      dialogVisible: false,
      temp: {
        id: undefined,
        content: '',
        reply: ''
      },
      rules: {
        reply: [{ required: true, message: '请输入回复内容', trigger: 'blur' }]
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
        const { data } = await getFeedbackList(this.listQuery)
        this.list = data.list
        this.total = data.total
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
            const { id, reply } = this.temp
            await replyFeedback(id, { reply })
            this.dialogVisible = false
            this.$message.success('回复成功')
            this.getList()
          } catch (error) {
            console.error('回复反馈失败:', error)
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
          await deleteFeedback(row.id)
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
.feedback-container {
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
}
</style> 