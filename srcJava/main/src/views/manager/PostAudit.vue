<template>
  <div style="padding: 20px;">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>帖子审核</span>
          <el-button @click="load">刷新</el-button>
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
        <el-table-column prop="content" label="帖子内容" min-width="300">
          <template #default="scope">
            <div style="max-height: 100px; overflow: hidden; text-overflow: ellipsis;">
              {{ scope.row.content }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="success" size="small" @click="handleAudit(scope.row, 1)" :disabled="scope.row.status === 1">通过</el-button>
            <el-button type="danger" size="small" @click="handleAudit(scope.row, 2)" :disabled="scope.row.status === 2">拒绝</el-button>
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看详情</el-button>
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

    <el-dialog title="帖子详情" v-model="data.detailVisible" width="70%">
      <div v-if="data.currentPost">
        <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px;">{{ data.currentPost.title }}</div>
        <div style="margin-bottom: 20px;">
          <el-tag type="success">{{ data.currentPost.sectionTitle }}</el-tag>
          <span style="margin-left: 10px;">发布者：{{ data.currentPost.userName }}</span>
          <span style="margin-left: 10px;">时间：{{ data.currentPost.time }}</span>
        </div>
        <div style="line-height: 1.8;" v-html="data.currentPost.content"></div>
      </div>
      <template #footer>
        <el-button @click="data.detailVisible = false">关闭</el-button>
        <el-button type="success" @click="handleAudit(data.currentPost, 1)" :disabled="data.currentPost.status === 1">通过</el-button>
        <el-button type="danger" @click="handleAudit(data.currentPost, 2)" :disabled="data.currentPost.status === 2">拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import request from '@/request.js';
import { ElMessage, ElMessageBox } from 'element-plus';

const data = reactive({
  tableData: [],
  searchTitle: '',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  detailVisible: false,
  currentPost: null
});

const load = () => {
  request.get('/post/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.searchTitle,
      status: 0
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list;
      data.total = res.data.total;
    }
  });
};

const handleAudit = (row, status) => {
  const action = status === 1 ? '通过' : '拒绝';
  ElMessageBox.confirm(`确定要${action}该帖子吗？`, '提示', { type: 'warning' }).then(() => {
    request.put('/post/update', {
      id: row.id,
      status: status
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success(`${action}成功`);
        load();
        if (data.detailVisible) {
          data.detailVisible = false;
        }
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const handleView = (row) => {
  data.currentPost = JSON.parse(JSON.stringify(row));
  data.detailVisible = true;
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
  load();
});
</script>
