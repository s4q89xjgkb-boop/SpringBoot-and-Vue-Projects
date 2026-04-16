<template>
  <div class="notice-list-container">
    <header class="header">
      <div class="header-left">
        <img class="logo" src="@/assets/images/Mlogo.png" alt="Logo">
        <span class="title">系统公告</span>
        <el-button type="primary" class="back-btn" @click="navTo('/front/home')">
          <el-icon><ArrowLeft /></el-icon>
          返回首页
        </el-button>
      </div>
      <div class="header-right">
        <div v-if="data.user && data.user.token" class="user-info">
          <el-dropdown>
            <div class="user-avatar-wrapper">
              <img v-if="data.user.avatar" class="user-avatar" :src="data.user.avatar" alt="用户头像" />
              <img v-else class="user-avatar" src="@/assets/images/Manager.png" alt="默认头像" />
              <span class="user-name">{{ data.user.name }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="navTo('/front/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/profile')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div v-else class="login-btn">
          <el-button type="primary" @click="navTo('/login')">登录</el-button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="notice-card">
        <h2 class="notice-title">系统公告</h2>
        
        <div v-if="data.notices.length === 0" class="empty-notice">
          <el-empty description="暂无公告" />
        </div>
        
        <div v-for="notice in data.notices" :key="notice.id" class="notice-item" @click="viewNotice(notice)">
          <h3 class="notice-item-title">{{ notice.title }}</h3>
          <p class="notice-item-content">{{ notice.content }}</p>
          <div class="notice-item-meta">
            <span class="notice-time">发布时间：{{ notice.time }}</span>
          </div>
        </div>

        <div v-if="data.total > 0" class="pagination">
          <el-pagination
            v-model:current-page="data.currentPage"
            v-model:page-size="data.pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="data.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/request.js";
import {ElMessage, ElEmpty, ElButton, ElIcon} from "element-plus";
import {ArrowLeft} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  notices: [],
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const loadNotices = () => {
  request.get('/notice/selectPage', { 
    params: {
      pageNum: data.currentPage,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.notices = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleSizeChange = (size) => {
  data.pageSize = size
  data.currentPage = 1
  loadNotices()
}

const handleCurrentChange = (page) => {
  data.currentPage = page
  loadNotices()
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  location.href = url
}

const viewNotice = (notice) => {
  location.href = '/front/notice?id=' + notice.id
}

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
.notice-list-container {
  background-color: #f6f9fc;
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.header {
  height: 70px;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.back-btn {
  margin-left: 20px;
  background-color: #2c82ff;
  border-color: #2c82ff;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: #409eff;
  border-color: #409eff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(44, 130, 255, 0.3);
}

.logo {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #2c82ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
}

.user-avatar-wrapper {
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.user-avatar-wrapper:hover {
  color: #2c82ff;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
  object-fit: cover;
}

.user-name {
  font-size: 14px;
  color: #303133;
}

.login-btn {
  margin-left: 16px;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.notice-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.notice-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.notice-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 30px;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f2f5;
}

.empty-notice {
  text-align: center;
  padding: 60px 0;
  color: #909399;
}

.notice-item {
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  transition: all 0.3s ease;
}

.notice-item:hover {
  transform: translateX(8px);
  border-bottom-color: #2c82ff;
}

.notice-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.notice-item-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.notice-item:hover .notice-item-title {
  color: #2c82ff;
}

.notice-item-content {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notice-item-meta {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #909399;
}

.notice-time {
  display: flex;
  align-items: center;
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #f0f2f5;
}

@media (max-width: 768px) {
  .header {
    padding: 0 10px;
  }
  
  .title {
    font-size: 20px;
  }
  
  .logo {
    width: 40px;
    height: 40px;
  }
  
  .user-name {
    display: none;
  }
  
  .main-content {
    padding: 10px;
  }
  
  .notice-card {
    padding: 20px;
  }
  
  .notice-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .notice-item {
    margin-bottom: 20px;
    padding-bottom: 20px;
  }
  
  .notice-item-title {
    font-size: 18px;
  }
}
</style>
