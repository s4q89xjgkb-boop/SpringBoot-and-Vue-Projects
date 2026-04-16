<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
      <div style="font-size: 20px; font-weight: bold; color: #333;">
        <el-icon><Message /></el-icon>
        意见反馈管理
      </div>
    </div>

    <div style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
      <!-- 统计信息 -->
      <div style="margin-bottom: 20px; display: flex; flex-wrap: wrap; gap: 20px;">
        <div style="display: flex; align-items: center; gap: 10px; padding: 10px 20px; background: #f0f9ff; border-radius: 6px; border-left: 4px solid #2c82ff;">
          <el-icon style="font-size: 24px; color: #2c82ff;"><Document /></el-icon>
          <div>
            <div style="font-size: 14px; color: #666;">总反馈数</div>
            <div style="font-size: 24px; font-weight: bold; color: #2c82ff;">{{ data.totalFeedback }}</div>
          </div>
        </div>
        <div style="display: flex; align-items: center; gap: 10px; padding: 10px 20px; background: #e6f7ff; border-radius: 6px; border-left: 4px solid #1890ff;">
          <el-icon style="font-size: 24px; color: #1890ff;"><Calendar /></el-icon>
          <div>
            <div style="font-size: 14px; color: #666;">今日反馈数</div>
            <div style="font-size: 24px; font-weight: bold; color: #1890ff;">{{ data.todayFeedback }}</div>
          </div>
        </div>
      </div>

      <!-- 搜索区域 -->
      <div style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center;">
        <el-input
          v-model="data.searchQuery"
          placeholder="搜索反馈内容或用户名"
          clearable
          style="width: 300px;"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 反馈列表 -->
      <el-table
        v-loading="data.loading"
        :data="currentPageData"
        style="width: 100%"
        border
        stripe
        :header-cell-style="{ background: '#fafafa', fontWeight: 'bold' }"
      >
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="avatar" label="用户头像" width="100">
          <template #default="scope">
            <el-avatar :size="40" :src="scope.row.avatar">
              {{ scope.row.username?.charAt(0) || '用' }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="content" label="反馈内容" min-width="300">
          <template #default="scope">
            <div style="word-break: break-all; line-height: 1.5;">{{ scope.row.content }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="提交时间" width="200" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDetail(scope.row)">
              <el-icon><View /></el-icon> 查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px; display: flex; justify-content: flex-end; align-items: center;">
        <el-pagination
          v-model:current-page="data.currentPage"
          v-model:page-size="data.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.filteredFeedbacks.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 反馈详情对话框 -->
    <el-dialog
      v-model="data.dialogVisible"
      :title="'反馈详情 - ID: ' + data.currentFeedback.id"
      width="50%"
      destroy-on-close
    >
      <div v-if="data.currentFeedback" style="padding: 20px 0;">
        <div style="margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px; font-size: 16px;">基本信息</div>
          <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 15px;">
            <el-avatar :size="50" :src="data.currentFeedback.avatar">
              {{ data.currentFeedback.username?.charAt(0) || '用' }}
            </el-avatar>
            <div>
              <div style="font-size: 16px; font-weight: bold;">{{ data.currentFeedback.username }}</div>
              <div style="font-size: 14px; color: #666;">用户ID: {{ data.currentFeedback.userId }}</div>
            </div>
          </div>
        </div>

        <div style="margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px; font-size: 16px;">反馈内容</div>
          <div style="padding: 15px; background: #f9f9f9; border-radius: 6px; line-height: 1.8; word-break: break-all;">
            {{ data.currentFeedback.content }}
          </div>
        </div>

        <div style="margin-bottom: 20px;">
          <div style="font-weight: bold; margin-bottom: 10px; font-size: 16px;">提交信息</div>
          <div style="display: flex; gap: 30px; font-size: 14px;">
            <div>
              <span style="color: #666;">提交时间：</span>
              <span style="font-weight: bold;">{{ data.currentFeedback.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.dialogVisible = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue';
import request from '@/request.js';
import { ElMessage } from 'element-plus';
import { Message, Document, Search, View, Calendar } from '@element-plus/icons-vue';

const data = reactive({
  feedbacks: [],
  filteredFeedbacks: [],
  searchQuery: '',
  loading: false,
  currentPage: 1,
  pageSize: 10,
  totalFeedback: 0,
  todayFeedback: 0,
  dialogVisible: false,
  currentFeedback: {}
});

// 计算当前页显示的数据
const currentPageData = computed(() => {
  const start = (data.currentPage - 1) * data.pageSize;
  const end = start + data.pageSize;
  return data.filteredFeedbacks.slice(start, end);
});

// 加载反馈列表
const loadFeedbacks = async () => {
  data.loading = true;
  try {
    const res = await request.get('/feedback/selectAll');
    if (res.code === '200' || res.code === 200) {
      data.feedbacks = res.data || [];
      data.totalFeedback = data.feedbacks.length || 0;
      
      // 计算今日反馈数
      const today = new Date().toISOString().split('T')[0];
      data.todayFeedback = data.feedbacks.filter(feedback => {
        return feedback.time && feedback.time.startsWith(today);
      }).length;
      
      data.filteredFeedbacks = [...data.feedbacks];
      ElMessage.success('反馈列表加载成功');
    } else {
      ElMessage.error(res.msg || '反馈列表加载失败');
      data.feedbacks = [];
      data.totalFeedback = 0;
      data.todayFeedback = 0;
      data.filteredFeedbacks = [];
    }
  } catch (error) {
    console.error('加载反馈列表失败:', error);
    ElMessage.error('网络异常，加载失败');
    data.feedbacks = [];
    data.totalFeedback = 0;
    data.todayFeedback = 0;
    data.filteredFeedbacks = [];
  } finally {
    data.loading = false;
  }
};



// 搜索功能
const handleSearch = () => {
  if (!data.searchQuery) {
    data.filteredFeedbacks = [...data.feedbacks];
    return;
  }
  
  const query = data.searchQuery.toLowerCase();
  data.filteredFeedbacks = data.feedbacks.filter(item => {
    return (
      item.content?.toLowerCase().includes(query) ||
      item.username?.toLowerCase().includes(query)
    );
  });
  data.currentPage = 1;
};

// 重置搜索
const resetSearch = () => {
  data.searchQuery = '';
  data.filteredFeedbacks = [...data.feedbacks];
  data.currentPage = 1;
};

// 查看详情
const viewDetail = (row) => {
  data.currentFeedback = { ...row };
  data.dialogVisible = true;
};

// 分页大小变化
const handleSizeChange = (size) => {
  data.pageSize = size;
  data.currentPage = 1;
};

// 当前页变化
const handleCurrentChange = (page) => {
  data.currentPage = page;
};

// 页面加载时初始化数据
onMounted(() => {
  loadFeedbacks();
});
</script>

<style scoped>
/* 自定义样式 */
.feedback-table :deep(.el-table__header-wrapper th) {
  background-color: #fafafa;
  font-weight: bold;
}

.feedback-table :deep(.el-table__body-wrapper tr:hover) {
  background-color: #f0f9ff;
}

.dialog-footer {
  text-align: right;
}
</style>