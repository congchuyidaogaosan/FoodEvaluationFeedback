<template>
  <div class="evaluation-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="搜索评价内容"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.dishId"
        placeholder="选择菜品"
        clearable
        style="width: 200px"
        class="filter-item"
      >
        <el-option
          v-for="item in dishOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
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
      <el-table-column label="ID" prop="id" align="center" width="80" />
      <el-table-column label="菜品" prop="dishName" align="center" />
      <el-table-column label="用户" prop="userName" align="center" />
      <el-table-column label="评分" prop="rating" align="center" width="100">
        <template slot-scope="{row}">
          <el-rate
            v-model="row.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </template>
      </el-table-column>
      <el-table-column label="评价内容" prop="content" align="center" />
      <el-table-column label="回复内容" prop="reply" align="center" />
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

    <el-dialog title="回复评价" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="评价内容" prop="content">
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
import { getEvaluationList, replyEvaluation, deleteEvaluation } from '@/api/evaluation'
import { getDishList } from '@/api/dish'
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
        keyword: undefined,
        dishId: undefined
      },
      dishOptions: [],
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
    this.getDishes()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const { data } = await getEvaluationList(this.listQuery)
        this.list = data.list
        this.total = data.total
      } catch (error) {
        console.error('获取评价列表失败:', error)
      }
      this.listLoading = false
    },
    async getDishes() {
      try {
        const { data } = await getDishList()
        this.dishOptions = data.list
      } catch (error) {
        console.error('获取菜品列表失败:', error)
      }
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
            await replyEvaluation(id, { reply })
            this.dialogVisible = false
            this.$message.success('回复成功')
            this.getList()
          } catch (error) {
            console.error('回复评价失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该评价吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteEvaluation(row.id)
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
.evaluation-container {
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