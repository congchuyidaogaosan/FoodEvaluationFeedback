<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-input
        v-model="listQuery.name"
        placeholder="分类名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button> -->
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加分类
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
      <el-table-column label="ID" prop="categoryId" align="center" width="80" />
      <el-table-column label="分类图片" align="center" width="120">
        <template slot-scope="{row}">
          <el-image
            :src="row.url"
            :preview-src-list="[row.url]"
            style="width: 80px; height: 80px"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column label="分类名称" prop="categoryName" align="center" />
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

    <el-dialog :title="dialogStatus === 'create' ? '新增分类' : '编辑分类'" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="temp.categoryName" />
        </el-form-item>
        <el-form-item label="图片链接" prop="url">
          <el-input v-model="temp.url" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/category'
import Pagination from '@/components/Pagination'

export default {
  name: 'Category',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: undefined
      },
      dialogVisible: false,
      dialogStatus: '',
      temp: {
        categoryId: undefined,
        categoryName: '',
        url: ''
      },
      rules: {
        categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
        url: [{ required: true, message: '请输入图片链接', trigger: 'blur' }]
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
        const response = await getCategoryList()
        this.list = response.data
        this.total = response.total
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        categoryId: undefined,
        categoryName: '',
        url: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await addCategory(this.temp)
            this.dialogVisible = false
            this.$message.success('创建成功')
            this.getList()
          } catch (error) {
            console.error('创建分类失败:', error)
          }
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await updateCategory(this.temp)
            this.dialogVisible = false
            this.$message.success('更新成功')
            this.getList()
          } catch (error) {
            console.error('更新分类失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该分类吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteCategory(row.categoryId)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除分类失败:', error)
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
    
    .el-image {
      border-radius: 4px;
      border: 1px solid #ebeef5;
      transition: all 0.3s;
      
      &:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
      }
    }
  }
}
</style> 