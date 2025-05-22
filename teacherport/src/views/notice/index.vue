<template>
  <div class="notice-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        发布通知
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
      <el-table-column label="内容" prop="content" align="center" />
      <el-table-column label="状态" prop="isHandled" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.isHandled === 1 ? 'success' : 'info'">
            {{ row.isHandled === 1 ? '已处理' : '未处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理结果" prop="handleResult" align="center">
        <template slot-scope="{row}">
          {{ row.handleResult || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="反馈时间" prop="feedbackTime" align="center" />
      <el-table-column label="操作" align="center" width="230">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="temp.content" type="textarea" :rows="6" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getNoticeList, addNotice, updateNotice, deleteNotice } from '@/api/notice'
import Pagination from '@/components/Pagination'

export default {
  name: 'Notice',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10
      },
      dialogVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      temp: {
        feedbackId: undefined,
        content: '',
        isHandled: 0,
        handleResult: '',
        feedbackTime: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
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
        const { data } = await getNoticeList(this.listQuery)
        this.list = data
        this.total = data.length
      } catch (error) {
        console.error('获取通知列表失败:', error)
      }
      this.listLoading = false
    },
    resetTemp() {
      this.temp = {
        feedbackId: undefined,
        content: '',
        isHandled: 0,
        handleResult: '',
        feedbackTime: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '发布通知'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await addNotice({
              content: this.temp.content,
              isHandled: this.temp.isHandled,
              handleResult: this.temp.handleResult
            })
            this.dialogVisible = false
            this.$message.success('发布成功')
            this.getList()
          } catch (error) {
            console.error('发布通知失败:', error)
          }
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑通知'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const { feedbackId } = this.temp
            await updateNotice(feedbackId, {
              content: this.temp.content,
              isHandled: this.temp.isHandled,
              handleResult: this.temp.handleResult
            })
            this.dialogVisible = false
            this.$message.success('更新成功')
            this.getList()
          } catch (error) {
            console.error('更新通知失败:', error)
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
          await deleteNotice(row.feedbackId)
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
.notice-container {
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