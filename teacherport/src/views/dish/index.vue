<template>
  <div class="dish-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="菜品名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.categoryId"
        placeholder="分类"
        clearable
        style="width: 200px"
        class="filter-item"
      >
        <el-option
          v-for="item in categoryOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加菜品
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
      <el-table-column label="图片" align="center" width="100">
        <template slot-scope="{row}">
          <el-image
            style="width: 50px; height: 50px"
            :src="row.image"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column label="名称" prop="name" align="center" />
      <el-table-column label="分类" prop="categoryName" align="center" />
      <el-table-column label="价格" prop="price" align="center">
        <template slot-scope="{row}">
          ¥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" />
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
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="temp.categoryId" placeholder="请选择">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="temp.price" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleImageSuccess"
            :before-upload="beforeImageUpload"
          >
            <img v-if="temp.image" :src="temp.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
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
import { getDishList, addDish, updateDish, deleteDish } from '@/api/dish'
import { getCategoryList } from '@/api/category'
import Pagination from '@/components/Pagination'

export default {
  name: 'Dish',
  components: { Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: undefined,
        categoryId: undefined
      },
      categoryOptions: [],
      dialogVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      temp: {
        id: undefined,
        name: '',
        categoryId: undefined,
        price: 0,
        image: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        image: [{ required: true, message: '请上传图片', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getCategories()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const { data } = await getDishList(this.listQuery)
        this.list = data.list
        this.total = data.total
      } catch (error) {
        console.error('获取菜品列表失败:', error)
      }
      this.listLoading = false
    },
    async getCategories() {
      try {
        const { data } = await getCategoryList()
        this.categoryOptions = data
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        categoryId: undefined,
        price: 0,
        image: '',
        status: 1
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '添加菜品'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await addDish(this.temp)
            this.dialogVisible = false
            this.$message.success('添加成功')
            this.getList()
          } catch (error) {
            console.error('添加菜品失败:', error)
          }
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑菜品'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const { id } = this.temp
            await updateDish(id, this.temp)
            this.dialogVisible = false
            this.$message.success('更新成功')
            this.getList()
          } catch (error) {
            console.error('更新菜品失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该菜品吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDish(row.id)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除菜品失败:', error)
        }
      })
    },
    handleImageSuccess(res) {
      this.temp.image = res.data
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
        return false
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
.dish-container {
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
  
  .avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100px;
    height: 100px;
    
    &:hover {
      border-color: #409EFF;
    }
    
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      line-height: 100px;
      text-align: center;
    }
    
    .avatar {
      width: 100px;
      height: 100px;
      display: block;
    }
  }
}
</style> 