<template>
  <div class="notice-manager-container">
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
        新增公告
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
        class="notice-table"
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
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="content" label="公告内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column label="走马灯" width="120" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.showInCarousel"
              @change="updateCarouselStatus(scope.row)"
              active-color="#52c41a"
              inactive-color="#d9d9d9"
            />
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
      title="公告信息"
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
        <el-form-item prop="title" label="公告标题">
          <el-input
            v-model="data.form.title"
            autocomplete="off"
            placeholder="请输入标题"
            class="form-input"
          />
        </el-form-item>
        <el-form-item prop="content" label="公告内容">
          <el-input
            type="textarea"
            :rows="6"
            v-model="data.form.content"
            autocomplete="off"
            placeholder="请输入内容"
            class="form-textarea"
          />
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
  form: {},
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
  request.get('/notice/selectPage', {
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
  data.form = {};
};

const formRef = ref();

const add = () => {
  request.post('/notice/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false;
      ElMessage.success('新增成功');
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/notice/update', data.form).then(res => {
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
    request.delete('/notice/delete/' + id).then(res => {
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

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的数据');
    return;
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除选中的公告吗？', '批量删除确认', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then(() => {
    request.delete('/notice/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const updateCarouselStatus = (row) => {
  request.put('/notice/update', row).then(res => {
    if (res.code === '200') {
      ElMessage.success(row.showInCarousel ? '已添加到走马灯' : '已从走马灯移除');
    } else {
      ElMessage.error(res.msg);
      row.showInCarousel = !row.showInCarousel;
    }
  }).catch(err => {
    ElMessage.error('操作失败');
    row.showInCarousel = !row.showInCarousel;
  });
};
</script>

<style scoped>
.notice-manager-container {
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
.notice-table {
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

/* 响应式设计 */
@media (max-width: 768px) {
  .notice-manager-container {
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