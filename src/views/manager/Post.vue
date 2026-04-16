<template>
  <div style="padding: 20px;">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>帖子管理</span>
          <el-button type="primary" @click="handleAdd">新增帖子</el-button>
        </div>
      </template>

      <div style="margin-bottom: 20px;">
        <el-input v-model="data.searchTitle" placeholder="搜索帖子标题" style="width: 300px; margin-right: 10px;" @keyup.enter="load" />
        <el-button @click="load">搜索</el-button>
        <el-button @click="data.searchTitle = ''; load()">重置</el-button>
      </div>

      <el-table :data="data.tableData" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="帖子标题" width="200" />
        <el-table-column prop="sectionTitle" label="所属板块" width="120" />
        <el-table-column prop="userName" label="发布者" width="120" />
        <el-table-column prop="status" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 2 ? 'danger' : 'warning'">
              {{ scope.row.status === 1 ? '已通过' : scope.row.status === 2 ? '已拒绝' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="80" />
        <el-table-column prop="collectCount" label="收藏数" width="80" />
        <el-table-column prop="replyCount" label="评论数" width="80" />
        <el-table-column prop="views" label="浏览量" width="80" />
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; display: flex; justify-content: center;">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="data.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog :title="data.form.id ? '编辑帖子' : '新增帖子'" v-model="data.formVisible" width="70%" destroy-on-close>
      <el-form :model="data.form" :rules="data.rules" ref="formRef" label-width="100px">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="data.form.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="选择板块" prop="sectionId">
          <el-select v-model="data.form.sectionId" placeholder="请选择板块" style="width: 100%">
            <el-option v-for="item in data.sections" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :mode="mode" />
            <Editor style="height: 400px; overflow-y: hidden;" v-model="data.form.content" :mode="mode" :defaultConfig="editorConfig" @onCreated="handleCreated" />
          </div>
        </el-form-item>
        <el-form-item label="审核状态" prop="status">
          <el-select v-model="data.form.status" placeholder="请选择审核状态" style="width: 100%">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, shallowRef } from 'vue';
import request from '@/request.js';
import { ElMessage, ElMessageBox } from 'element-plus';
import '@wangeditor/editor/dist/css/style.css';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  tableData: [],
  sections: [],
  searchTitle: '',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  rules: {
    title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
    sectionId: [{ required: true, message: '请选择板块', trigger: 'change' }],
    content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }]
  }
});

const formRef = ref();
const editorRef = shallowRef();
const mode = 'default';
const editorConfig = {
  // 启用视频菜单
  menus: [
    'head',
    'bold',
    'italic',
    'underline',
    'strike',
    'list',
    'justify',
    'image',
    'video',
    'insert',
    'table',
    'code',
    'splitLine',
    'undo',
    'redo'
  ],
  MENU_CONF: {
    uploadImage: {
      headers: {
        token: data.user?.token
      },
      server: 'http://localhost:1306/files/wang/upload',
      fieldName: 'file'
    },
    uploadVideo: {
      headers: {
        token: data.user?.token
      },
      server: 'http://localhost:1306/files/wang/upload',
      fieldName: 'file',
      maxFileSize: 5 * 1024 * 1024 * 1024, // 5GB
      onError: (file, err) => {
        console.error('上传失败:', err);
      }
    }
  }
};

onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

const handleCreated = (editor) => {
  editorRef.value = editor;
};

const load = () => {
  request.get('/post/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.searchTitle
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list;
      data.total = res.data.total;
    }
  });
};

const loadSections = () => {
  request.get('/forumSection/selectAll', { params: { status: 1 } }).then(res => {
    if (res.code === '200') {
      data.sections = res.data;
    }
  });
};

const handleAdd = () => {
  data.form = { status: 1 };
  data.formVisible = true;
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该帖子吗？', '提示', { type: 'warning' }).then(() => {
    request.delete('/post/delete/' + id).then(res => {
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
    const url = data.form.id ? '/post/update' : '/post/add';
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

const handleSizeChange = (size) => {
  data.pageSize = size;
  data.pageNum = 1;
  load();
};

const handleCurrentChange = (page) => {
  data.pageNum = page;
  load();
};

onMounted(() => {
  loadSections();
  load();
});
</script>
