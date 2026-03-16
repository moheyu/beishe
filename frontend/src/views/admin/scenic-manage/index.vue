<template>
  <div class="scenic-manage">
    <div class="page-header">
      <h2>景区管理</h2>
      <el-button type="primary" @click="showAddDialog = true">添加景区</el-button>
    </div>

    <el-card>
      <el-table :data="scenicList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="景点名称"></el-table-column>
        <el-table-column prop="categoryName" label="分类"></el-table-column>
        <el-table-column prop="price" label="价格">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}/人
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" sortable></el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="margin-top: 20px; text-align: center"
      >
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="showAddDialog" width="600px">
      <el-form :model="scenicForm" :rules="rules" ref="scenicForm" label-width="100px">
        <el-form-item label="景点名称" prop="name">
          <el-input v-model="scenicForm.name"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="scenicForm.categoryId" placeholder="请选择分类">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="scenicForm.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="scenicForm.address"></el-input>
        </el-form-item>
        <el-form-item label="开放时间" prop="openTime">
          <el-input v-model="scenicForm.openTime"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="scenicForm.description" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="images">
          <el-upload
              action="#"
              list-type="picture-card"
              :file-list="imageList"
              :on-preview="handlePicturePreview"
              :on-remove="handleRemove"
              :on-change="handleImageChange"
              :auto-upload="false"
              multiple
              :limit="9"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <div class="upload-tip">最多上传9张图片，支持jpg、jpeg、png、gif格式，单张图片不超过5MB</div>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="scenicForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { uploadScenicImage, uploadScenicImages } from '@/api/upload'
import { getAdminScenicList, addScenic, updateScenic, deleteScenic } from '@/api/admin'

export default {
  name: 'ScenicManage',
  data() {
    return {
      scenicList: [],
      loading: false,
      showAddDialog: false,
      dialogTitle: '添加景区',
      submitLoading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      scenicForm: {
        id: '',
        name: '',
        categoryId: '',
        price: 0,
        address: '',
        openTime: '',
        description: '',
        images: '',
        status: 1
      },
      imageList: [],
      imageUploading: false,
      rules: {
        name: [
          { required: true, message: '请输入景点名称', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ]
      },
      categories: [
        { id: 1, name: '自然风光' },
        { id: 2, name: '人文景观' },
        { id: 3, name: '主题乐园' },
        { id: 4, name: '历史古迹' }
      ]
    }
  },
  created() {
    this.loadScenicList()
  },
  methods: {
    async loadScenicList() {
      this.loading = true
      try {
        const res = await getAdminScenicList(this.queryParams)
        // 核心修改1：把 res.data.list 改成 res.data.records（匹配后端返回的字段名）
        this.scenicList = res.data.records || []
        // 核心修改2：total兜底（后端返回0时取records长度）
        this.total = res.data.total || (res.data.records ? res.data.records.length : 0)
        // 调试日志（可选，确认数据解析是否正确）
        console.log('景点列表数据：', res.data)
      } catch (error) {
        this.$message.error('加载景点列表失败')
        // 补充错误详情，方便排查
        console.error('加载失败详情：', error)
      } finally {
        this.loading = false
      }
    },
    handleEdit(row) {
      this.dialogTitle = '编辑景区'
      this.scenicForm = { ...row }
      if (row.images) {
        this.imageList = row.images.split(',').map((url, index) => ({
          name: `image-${index}`,
          url: url
        }))
      } else {
        this.imageList = []
      }
      this.showAddDialog = true
    },
    handleDelete(id) {
      this.$confirm('确定要删除这个景区吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteScenic(id)
          this.$message.success('删除成功')
          this.loadScenicList()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    handleSubmit() {
      this.$refs.scenicForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.imageList.length > 0) {
            const needUploadFiles = this.imageList.filter(item => item.raw)
            if (needUploadFiles.length > 0) {
              this.imageUploading = true
              uploadScenicImages(needUploadFiles.map(item => item.raw)).then(res => {
                const uploadedUrls = res.data.map(item => item.url)
                const existingUrls = this.imageList.filter(item => !item.raw).map(item => item.url)
                // 确保images字段是数组格式
                this.scenicForm.images = [...existingUrls, ...uploadedUrls]
                this.submitForm()
              }).catch(() => {
                this.$message.error('图片上传失败')
                this.submitLoading = false
                this.imageUploading = false
              })
            } else {
          // 确保images字段是数组格式
          this.scenicForm.images = this.imageList.map(item => item.url)
          this.submitForm()
        }
          } else {
        // 当没有图片时，确保images字段为空数组而不是空字符串
        this.scenicForm.images = []
        this.submitForm()
      }
        }
      })
    },
    async submitForm() {
      try {
        // 确保images字段是数组而不是空字符串
        const requestData = {
          ...this.scenicForm,
          images: this.scenicForm.images || []
        }
        if (this.scenicForm.id) {
          await updateScenic(this.scenicForm.id, requestData)
          this.$message.success('更新成功')
        } else {
          await addScenic(requestData)
          this.$message.success('添加成功')
        }
        this.showAddDialog = false
        this.submitLoading = false
        this.imageUploading = false
        this.loadScenicList()
      } catch (error) {
        this.$message.error('提交失败')
        this.submitLoading = false
        this.imageUploading = false
      }
    },
    handlePicturePreview(file) {
      window.open(file.url, '_blank')
    },
    handleRemove(file, fileList) {
      this.imageList = fileList
    },
    handleImageChange(file, fileList) {
      const isImage = file.raw.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        this.imageList = fileList.filter(item => item !== file)
        return
      }

      if (!isLt5M) {
        this.$message.error('图片大小不能超过5MB!')
        this.imageList = fileList.filter(item => item !== file)
        return
      }

      this.imageList = fileList
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.loadScenicList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.loadScenicList()
    }
  }
}
</script>

<style scoped>
.scenic-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>