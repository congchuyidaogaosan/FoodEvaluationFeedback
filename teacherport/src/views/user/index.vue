<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-input
        v-model="listQuery.keyword"
        placeholder="搜索用户名"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button> -->
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加用户
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
      <el-table-column label="ID" prop="studentId" align="center" width="80" />
      <el-table-column label="学号" prop="studentNumber" align="center">
        <template slot-scope="{row}">
          <span>{{ row.studentNumber || '未设置' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名" prop="username" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username || '未设置' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" prop="contactInfo" align="center">
        <template slot-scope="{row}">
          <span>{{ row.contactInfo || '未设置' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="OpenID" prop="openId" align="center" width="280">
        <template slot-scope="{row}">
          <el-tooltip class="item" effect="dark" :content="row.openId" placement="top">
            <span>{{ row.openId }}</span>
          </el-tooltip>
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

    <el-dialog :title="dialogStatus === 'create' ? '新增用户' : '编辑用户'" :visible.sync="dialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
      >
        <el-form-item label="学号" prop="studentNumber">
          <el-input v-model="temp.studentNumber" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="temp.contactInfo" />
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
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'
import Pagination from '@/components/Pagination'

export default {
  name: 'User',
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
      dialogStatus: '',
      temp: {
        studentId: '',
        studentNumber: '',
        username: '',
        contactInfo: ''
      },
      rules: {
        studentNumber: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }]
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
        const response = await getUserList(this.listQuery)
        // 直接使用返回的数组作为列表数据
        this.list = response.data
        // 设置总数为数组长度
        this.total = response.data.length
      } catch (error) {
        console.error('获取用户列表失败:', error)
      }
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        studentId: undefined,
        studentNumber: '',
        username: '',
        contactInfo: ''
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
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            await addUser(this.temp)
            this.dialogVisible = false
            this.$message.success('创建成功')
            this.getList()
          } catch (error) {
            console.error('创建用户失败:', error)
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {

            await updateUser(this.temp)
            this.dialogVisible = false
            this.$message.success('更新成功')
            this.getList()
          } catch (error) {
            console.error('更新用户失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteUser(row.studentId)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          console.error('删除用户失败:', error)
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
    
    /deep/ .el-tooltip__popper {
      max-width: 300px;
    }
  }
}
</style> 