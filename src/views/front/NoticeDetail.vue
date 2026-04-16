<template>
  <div class="notice-detail-container">
    <header class="header">
      <div class="header-left">
        <img class="logo" src="@/assets/images/Mlogo.png" alt="Logo">
        <span class="title">公告详情</span>
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
                <el-dropdown-item @click="navTo('/front/noticelist')">公告列表</el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/home')">返回首页</el-dropdown-item>
                <el-dropdown-item divided></el-dropdown-item>
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
        <div v-if="data.notice.id" class="notice-content">
          <h1 class="notice-title">{{ data.notice.title }}</h1>
          <div class="notice-meta">
            <span class="notice-time">发布时间：{{ data.notice.time }}</span>
          </div>
          <div class="notice-body">
            {{ data.notice.content }}
          </div>
          <div class="back-btn-container">
            <el-button type="primary" @click="navTo('/front/noticelist')" :icon="ArrowLeft">
              返回公告列表
            </el-button>
          </div>
        </div>
        <div v-else class="notice-not-found">
          <el-empty
            description="公告不存在"
            :image-size="120"
          >
            <el-button type="primary" @click="navTo('/front/noticelist')">
              返回公告列表
            </el-button>
          </el-empty>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import {reactive, onMounted, computed} from "vue";
import {useRoute} from "vue-router";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import {ArrowLeft} from "@element-plus/icons-vue";

const route = useRoute()

const noticeId = computed(() => route.query.id)

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  notice: {}
})

const loadNotice = () => {
  const id = noticeId.value
  if (!id) {
    ElMessage.error('公告ID不存在')
    return
  }
  request.get('/notice/selectById/' + id).then(res => {
    if (res.code === '200') {
      data.notice = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  location.href = url
}

onMounted(() => {
  window.scrollTo(0, 0)
  loadNotice()
})
</script>

<style scoped>
.notice-detail-container {
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
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.notice-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.notice-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.notice-content {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.notice-title {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  line-height: 1.3;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f2f5;
}

.notice-meta {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #909399;
  margin-bottom: 40px;
  padding: 12px 0;
}

.notice-time {
  display: flex;
  align-items: center;
}

.notice-body {
  font-size: 16px;
  color: #606266;
  line-height: 2.0;
  padding: 30px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
  white-space: pre-wrap;
  word-break: break-word;
}

.back-btn-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #f0f2f5;
}

.notice-not-found {
  text-align: center;
  padding: 80px 20px;
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
    margin-bottom: 16px;
    padding-bottom: 12px;
  }
  
  .notice-meta {
    margin-bottom: 20px;
    font-size: 13px;
  }
  
  .notice-body {
    font-size: 15px;
    line-height: 1.8;
    padding: 20px;
  }
  
  .back-btn-container {
    margin-top: 20px;
  }
}
</style>
