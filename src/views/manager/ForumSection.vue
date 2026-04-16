<template>
  <div style="padding: 20px;">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>论坛板块管理</span>
          <el-button type="primary" @click="handleAdd">新增板块</el-button>
        </div>
      </template>

      <el-table :data="data.tableData" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="板块名称" width="150" />
        <el-table-column prop="description" label="板块描述" width="200" />
        <el-table-column prop="icon" label="板块图标" width="100">
          <template #default="scope">
            <el-image
              v-if="scope.row.icon"
              :src="scope.row.icon"
              style="width: 50px; height: 50px; border-radius: 4px;"
              fit="cover"
            />
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="postCount" label="帖子数量" width="100" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="data.form.id ? '编辑板块' : '新增板块'" v-model="data.formVisible" width="500px">
      <el-form :model="data.form" :rules="data.rules" ref="formRef" label-width="100px">
        <el-form-item label="板块名称" prop="title">
          <el-input v-model="data.form.title" placeholder="请输入板块名称" />
        </el-form-item>
        <el-form-item label="板块描述" prop="description">
          <el-input v-model="data.form.description" type="textarea" :rows="3" placeholder="请输入板块描述" />
        </el-form-item>
        <el-form-item label="板块图标" prop="icon">
          <el-upload
            action="http://localhost:1306/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleFileSuccess"
            :show-file-list="false"
            accept="image/*"
          >
            <el-button type="primary">上传图标</el-button>
          </el-upload>
          <el-image
            v-if="data.form.icon"
            :src="data.form.icon"
            style="width: 80px; height: 80px; margin-top: 10px; border-radius: 4px;"
            fit="cover"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="data.form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="data.form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import request from '@/request.js';
import { ElMessage, ElMessageBox } from 'element-plus';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    title: [{ required: true, message: '请输入板块名称', trigger: 'blur' }]
  }
});

const formRef = ref();

const load = () => {
  request.get('/forumSection/selectAll').then(res => {
    if (res.code === '200') {
      data.tableData = res.data;
    }
  });
};

const handleAdd = () => {
  data.form = { sort: 0, status: 1 };
  data.formVisible = true;
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该板块吗？', '提示', { type: 'warning' }).then(() => {
    request.delete('/forumSection/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const save = () => {
  formRef.value.validate((valid) => {
    if (!valid) return;
    const url = data.form.id ? '/forumSection/update' : '/forumSection/add';
    const method = data.form.id ? request.put : request.post;
    method(url, data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功');
        data.formVisible = false;
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};

const handleFileSuccess = (res) => {
  if (res.code === '200' || res.code === 200) {
    data.form.icon = res.data;
    ElMessage.success('图标上传成功');
  } else {
    ElMessage.error('图标上传失败：' + res.msg);
  }
};

onMounted(() => {
  load();
});
</script>
