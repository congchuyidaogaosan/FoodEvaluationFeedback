<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-input
        v-model="listQuery.name"
        placeholder="菜品名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      /> -->
      <!-- <el-select
        v-model="listQuery.categoryId"
        placeholder="分类"
        clearable
        style="width: 200px"
        class="filter-item"
      >
        <el-option
          v-for="item in categoryOptions"
          :key="item.categoryId"
          :label="item.categoryName"
          :value="item.categoryId"
        />
      </el-select> -->
      <!-- <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button> -->
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
      <el-table-column label="ID" prop="dishId" align="center" width="80" />
      <el-table-column label="菜品图片" align="center" width="120">
        <template slot-scope="{row}">
          <el-image
            :src="row.url"
            :preview-src-list="[row.url]"
            style="width: 80px; height: 80px"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column label="菜品名称" prop="dishName" align="center" />
      <el-table-column label="分类" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ getCategoryName(row.categoryId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" align="center" width="120">
        <template slot-scope="{row}">
          <div>原价：¥{{ row.price }}</div>
          <div v-if="row.isSpecial === 1" style="color: #f56c6c">
            特价：¥{{ Number(row.specialprice).toFixed(1) }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="配料" prop="ingredients" align="center" show-overflow-tooltip />
      <el-table-column label="营养信息" prop="nutritionInfo" align="center" show-overflow-tooltip />
      <el-table-column label="特价状态" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isSpecial === 1 ? 'danger' : 'info'">
            {{ row.isSpecial === 1 ? '特价中' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评分" align="center" width="100">
        <template slot-scope="{row}">
          <el-rate
            v-model="row.rating"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}"
          />
        </template>
      </el-table-column>
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

    <el-dialog :title="dialogStatus === 'create' ? '新增菜品' : '编辑菜品'" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="菜品名称" prop="dishName">
          <el-input v-model="temp.dishName" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="temp.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryOptions"
              :key="item.categoryId"
              :label="item.categoryName"
              :value="item.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="图片链接" prop="url">
          <el-input v-model="temp.url" />
        </el-form-item>
        <el-form-item label="原价" prop="price">
          <el-input-number v-model="temp.price" :min="0" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="是否特价" prop="isSpecial">
          <el-switch
            v-model="temp.isSpecial"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <el-form-item v-if="temp.isSpecial === 1" label="特价" prop="specialprice">
          <el-input-number v-model="temp.specialprice" :min="0" :max="temp.price" :precision="1" :step="0.1" />
        </el-form-item>
        <el-form-item label="配料" prop="ingredients">
          <el-input v-model="temp.ingredients" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="营养信息" prop="nutritionInfo">
          <el-input v-model="temp.nutritionInfo" type="textarea" :rows="2" />
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
import { getDishList, addDish, updateDish, deleteDish } from '@/api/dish'
import { getCategoryList } from '@/api/category'
import Pagination from '@/components/Pagination'

export default {
  name: 'Dish',
  components: { Pagination },
  data() {
    const validateSpecialPrice = (rule, value, callback) => {
      if (this.temp.isSpecial === 1 && (!value || value >= this.temp.price)) {
        callback(new Error('特价必须小于原价'))
      } else {
        callback()
      }
    }
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
      temp: {
        dishId: undefined,
        dishName: '',
        categoryId: undefined,
        price: 0,
        ingredients: '',
        nutritionInfo: '',
        isSpecial: 0,
        url: '',
        specialprice: 0
      },
      rules: {
        dishName: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        url: [{ required: true, message: '请输入图片链接', trigger: 'blur' }],
        specialprice: [{ validator: validateSpecialPrice, trigger: 'blur' }]
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
        const response = await getDishList()
        this.list = response.data
        this.total = response.total
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
        dishId: undefined,
        dishName: '',
        categoryId: undefined,
        price: 0,
        ingredients: '',
        nutritionInfo: '',
        isSpecial: 0,
        url: '',
        specialprice: 0
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
            await addDish(this.temp)
            this.dialogVisible = false
            this.$message.success('创建成功')
            this.getList()
          } catch (error) {
            console.error('创建菜品失败:', error)
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
            await updateDish(this.temp)
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
          await deleteDish(row.dishId)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除菜品失败:', error)
        }
      })
    },
    getCategoryName(categoryId) {
      const category = this.categoryOptions.find(item => item.categoryId === categoryId)
      return category ? category.categoryName : '未知分类'
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
  
  .el-dialog {
    .el-select {
      width: 100%;
    }
  }
}
</style> 