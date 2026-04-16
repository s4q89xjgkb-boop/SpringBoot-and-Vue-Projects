<template>
  <div class="news-manager-container">
    <!-- 搜索区域 -->
    <div class="search-card card">
      <div class="search-form">
        <el-input
          clearable
          @clear="load"
          class="search-input"
          v-model="data.title"
          placeholder="请输入标题查询"
          :prefix-icon="Search"
        ></el-input>
        <el-button type="primary" @click="load" class="search-btn">
          <el-icon><Search /></el-icon>
          查询
        </el-button>
        <el-button @click="reset" class="reset-btn">
          重置
        </el-button>
      </div>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-card card">
      <el-button type="primary" @click="handleAdd" class="add-btn">
        <el-icon><Plus /></el-icon>
        新增新闻
      </el-button>
      <el-button type="danger" @click="deleteBatch" class="batch-delete-btn" :disabled="data.rows.length === 0">
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>

    <!-- 数据表格区域 -->
    <div class="table-card card">
      <el-table
        :data="data.tableData"
        class="news-table"
        @selection-change="handleSelectionChange"
        :header-cell-style="{ 
          color: '#303133', 
          backgroundColor: '#f0f9ff',
          fontWeight: '600',
          fontSize: '14px'
        }"
        :row-class-name="(row, index) => index % 2 === 0 ? 'table-row-even' : 'table-row-odd'"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="新闻标题" min-width="200" />
        <el-table-column prop="content" label="新闻内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              icon="Edit" 
              circle 
              @click="handleEdit(scope.row)"
              class="edit-btn"
            ></el-button>
            <el-button 
              type="danger" 
              icon="Delete" 
              circle 
              @click="del(scope.row.id)"
              class="delete-btn"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-card card">
      <el-pagination
        v-model:current-page="data.pageNum"
        v-model:page-size="data.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[5, 10, 15, 20]"
        :total="data.total"
        @current-change="load"
        @size-change="load"
        background
      />
    </div>

    <!-- 编辑/新增弹窗 -->
    <el-dialog
      title="新闻信息"
      v-model="data.formVisible"
      width="500px"
      destroy-on-close
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form
        ref="formRef"
        :model="data.form"
        :rules="data.rules"
        label-width="80px"
        class="form-container"
      >
        <el-form-item prop="title" label="新闻标题">
          <el-input
            v-model="data.form.title"
            autocomplete="off"
            placeholder="请输入标题"
            class="form-input"
          />
        </el-form-item>
        <el-form-item prop="content" label="新闻内容">
          <el-input
            type="textarea"
            :rows="6"
            v-model="data.form.content"
            autocomplete="off"
            placeholder="请输入内容"
            class="form-textarea"
          />
        </el-form-item>
        <el-form-item prop="img" label="新闻图片">
          <el-upload
            action=""
            :http-request="handleUpload"
            :show-file-list="true"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            accept="image/*"
            class="mb-2"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <el-input
            v-model="data.form.img"
            autocomplete="off"
            placeholder="图片URL"
            class="form-input"
            readonly
          />
        </el-form-item>
        <el-form-item prop="status" label="状态">
          <el-radio-group v-model="data.form.status">
            <el-radio label="1">已发布</el-radio>
            <el-radio label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { Search, Plus, Delete, Edit } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import request from "@/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  form: {
    status: 1 // 默认已发布
  },
  formVisible: false,
  rules: {
    title: [
      { required: true, message: '请填写标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请填写内容', trigger: 'blur' }
    ]
  },
  rows: [],
  ids: []
});

const load = () => {
  request.get('/news/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total || 0;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

load();

const reset = () => {
  data.title = null;
  load();
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const handleAdd = () => {
  data.formVisible = true;
  data.form = {
    status: 1 // 默认已发布
  };
};

const formRef = ref();

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/news/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('新增成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/news/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('修改成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const save = () => {
  data.form.id ? update() : add();
};

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then(() => {
    request.delete('/news/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const handleSelectionChange = (rows) => {
  data.rows = rows;
  data.ids = rows.map(v => v.id);
};

const handleUpload = (params) => {
  const formData = new FormData();
  formData.append('file', params.file);
  request.post('/files/upload', formData).then(res => {
    if (res.code === '200' || res.code === 200) {
      data.form.img = res.data;
      ElMessage.success('图片上传成功');
    } else {
      ElMessage.error(res.msg || '图片上传失败');
    }
  }).catch(error => {
    console.error('上传错误:', error);
    if (error.response && error.response.status === 500) {
      ElMessage.error('服务器内部错误，请检查后端日志');
    } else if (error.response && error.response.status === 404) {
      ElMessage.error('上传接口不存在');
    } else {
      ElMessage.error('网络异常，请稍后重试');
    }
  });
};

const handleUploadSuccess = () => {
  // 已在handleUpload中处理
};

const handleUploadError = () => {
  ElMessage.error('图片上传失败');
};

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error('请选择图片文件');
    return false;
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB');
    return false;
  }
  return true;
};

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的数据');
    return;
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除选中的新闻吗？', '批量删除确认', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then(() => {
    request.delete('/news/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};
</script>

<style scoped>
.news-manager-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.card {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
  padding: 20px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

/* 搜索区域样式 */
.search-form {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-input {
  width: 320px;
}

.search-btn, .reset-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 操作按钮区域样式 */
.action-card {
  display: flex;
  gap: 12px;
}

.add-btn, .batch-delete-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 表格区域样式 */
.news-table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.el-table__inner-wrapper {
  border-radius: 8px;
  overflow: hidden;
}

.table-row-even {
  background-color: #fafafa;
}

.table-row-odd {
  background-color: #ffffff;
}

.el-table__body tr:hover > td {
  background-color: #f0f9ff !important;
}

/* 分页区域样式 */
.pagination-card {
  display: flex;
  justify-content: center;
  padding-top: 10px;
}

/* 弹窗样式 */
.form-container {
  padding: 0;
}

.form-input, .form-textarea {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
}

/* 图片上传样式 */
.avatar-uploader {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-uploader-icon:hover {
  color: #409eff;
  border-color: #409eff;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid #d9d9d9;
}

.mt-2 {
  margin-top: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .news-manager-container {
    padding: 10px;
  }
  
  .search-form {
    flex-wrap: wrap;
  }
  
  .search-input {
    width: 100%;
    margin-bottom: 12px;
  }
  
  .action-card {
    flex-direction: column;
  }
  
  .add-btn, .batch-delete-btn {
    width: 100%;
    justify-content: center;
  }
  
  .el-table {
    font-size: 13px;
  }
  
  .el-dialog {
    width: 95% !important;
  }
}
</style>