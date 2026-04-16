<template>
  <div style="padding: 20px;">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>游戏攻略审核</span>
          <el-button @click="load">刷新</el-button>
        </div>
      </template>

      <div style="margin-bottom: 20px;">
        <el-input v-model="data.searchTitle" placeholder="搜索攻略标题" style="width: 300px; margin-right: 10px;" @keyup.enter="load" />
        <el-button @click="load">搜索</el-button>
        <el-button @click="data.searchTitle = ''; load()">重置</el-button>
      </div>

      <el-table :data="data.tableData" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="攻略标题" width="200" />
        <el-table-column prop="categoryTitle" label="攻略分类" width="120" />
        <el-table-column prop="userName" label="发布者" width="120" />
        <el-table-column prop="status" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 2 ? 'danger' : 'warning'">
              {{ scope.row.status === 1 ? '已通过' : scope.row.status === 2 ? '已拒绝' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="攻略内容" min-width="300">
          <template #default="scope">
            <el-button type="primary" @click="handleView(scope.row)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="success" size="small" @click="handleAudit(scope.row, 1)" :disabled="scope.row.status === 1">通过</el-button>
            <el-button type="danger" size="small" @click="handleReject(scope.row)" :disabled="scope.row.status === 2">拒绝</el-button>
            <el-button type="info" size="small" @click="handleViewRejectReason(scope.row)" v-if="scope.row.status === 2 && scope.row.rejectReason">查看拒绝理由</el-button>
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

    <el-dialog title="拒绝理由" v-model="data.rejectVisible" width="30%">
      <el-form label-width="80px">
        <el-form-item label="拒绝理由">
          <el-input type="textarea" :rows="4" v-model="data.rejectReason" placeholder="请输入拒绝理由（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>

    <el-dialog title="攻略内容" v-model="data.viewVisible" width="70%" destroy-on-close>
      <div class="strategy-content" v-html="data.viewContent" style="padding: 0 20px; width: 100%"></div>
    </el-dialog>

    <el-dialog title="拒绝理由" v-model="data.rejectReasonVisible" width="50%">
      <div style="padding: 20px;">
        <div style="font-size: 14px; color: #666; line-height: 1.8;">
          {{ data.currentRejectReason || '暂无拒绝理由' }}
        </div>
      </div>
      <template #footer>
        <el-button @click="data.rejectReasonVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
:deep(.strategy-content img) {
  max-width: 100% !important;
  max-height: 80vh !important;
  object-fit: contain !important;
  display: block !important;
  margin: 0 auto;
}
</style>

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
  rejectVisible: false,
  rejectReason: '',
  currentRow: null,
  viewVisible: false,
  viewContent: null,
  rejectReasonVisible: false,
  currentRejectReason: null
});

const load = () => {
  request.get('/introduction/selectPage', {
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
  ElMessageBox.confirm(`确定要通过该攻略吗？`, '提示', { type: 'warning' }).then(() => {
    request.put('/introduction/update', {
      id: row.id,
      status: status
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('通过成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const handleReject = (row) => {
  data.currentRow = row;
  data.rejectReason = '';
  data.rejectVisible = true;
};

const handleView = (row) => {
  data.viewContent = row.content;
  data.viewVisible = true;
};

const handleViewRejectReason = (row) => {
  data.currentRejectReason = row.rejectReason;
  data.rejectReasonVisible = true;
};

const confirmReject = () => {
  request.put('/introduction/update', {
    id: data.currentRow.id,
    status: 2,
    rejectReason: data.rejectReason
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('拒绝成功');
      data.rejectVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
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
  load();
});
</script>
