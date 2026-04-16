<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          游戏资讯
        </span>
        <el-button type="primary" @click="navTo('/front/home')" style="margin-left: 20px; background: #2c82ff; border: none;">
          <el-icon><ArrowLeft /></el-icon>
          返回主页
        </el-button>
      </div>
      <div style="flex: 1"></div>
      <div v-if="data.user && data.user.token">
        <el-dropdown style="padding-right: 20px">
          <div style="width: fit-content; display: flex; align-items: center;">
            <img v-if="data.user.avatar" style="width: 50px; height: 50px; border-radius: 50%" :src="data.user.avatar" />
            <img v-else style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Manager.png" />
            <span style="padding-left: 5px">{{ data.user.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="navTo('/front/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div v-else>
        <el-button type="primary" @click="navTo('/login')" style="background: #2c82ff; border: none;">
          登录
        </el-button>
      </div>
    </div>

    <div style="max-width: 1200px; margin: 20px auto; display: flex; flex-direction: column; gap: 20px;">
      <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
          <div style="font-size: 20px; font-weight: bold; color: #333;">
            游戏资讯
          </div>
        </div>

        <div style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
          <el-input
            v-model="data.searchTitle"
            placeholder="搜索资讯标题"
            style="width: 300px;"
            @keyup.enter="loadNews"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button @click="loadNews">搜索</el-button>
          <el-select v-model="data.orderBy" placeholder="排序方式" style="width: 150px;" @change="loadNews">
            <el-option label="最新发布" value="time" />
            <el-option label="浏览最多" value="views" />
            <el-option label="点赞最多" value="likeCount" />
            <el-option label="评论最多" value="replyCount" />
          </el-select>
        </div>

        <div v-if="data.news.length === 0" style="text-align: center; padding: 60px; color: #999;">
          <el-icon :size="80" color="#ddd"><Document /></el-icon>
          <div style="font-size: 18px; margin-top: 20px;">
            暂无资讯
          </div>
          <div style="font-size: 14px; margin-top: 10px;">
            敬请期待更多游戏资讯
          </div>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); gap: 20px;">
          <div v-for="item in data.news" :key="item.id" 
               style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; overflow: hidden; transition: all 0.3s; cursor: pointer; box-shadow: 0 2px 8px rgba(0,0,0,0.05);"
               @click="navTo('/front/newsDetail?id=' + item.id)"
               @mouseenter="style={transform: 'translateY(-5px)', boxShadow: '0 4px 12px rgba(0,0,0,0.1)'}"
               @mouseleave="style={transform: 'translateY(0)', boxShadow: '0 2px 8px rgba(0,0,0,0.05)'}">
            <div style="position: relative; width: 100%; height: 200px;">
              <template v-if="item.img">
                <el-image
                  :src="item.img"
                  :preview-src-list="[item.img]"
                  fit="cover"
                  style="width: 100%; height: 100%;"
                />
              </template>
              <template v-else>
                <div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: #f0f0f0;">
                  <div style="font-size: 80px; font-weight: bold; color: #666;">
                    {{ item.title ? item.title.charAt(0) : '资' }}
                  </div>
                </div>
              </template>
              <div style="position: absolute; bottom: 0; left: 0; right: 0; background: linear-gradient(transparent, rgba(0,0,0,0.6)); padding: 15px; color: white;">
                <div style="font-size: 14px;">
                  {{ item.time }}
                </div>
              </div>
            </div>
            <div style="padding: 15px;">
              <div style="font-size: 16px; font-weight: bold; margin-bottom: 10px; color: #333; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                {{ item.title }}
              </div>
              <div style="font-size: 14px; color: #666; margin-bottom: 12px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 3; overflow: hidden;">
                {{ stripHtml(item.content) }}
              </div>
              <div style="display: flex; align-items: center; gap: 15px; font-size: 13px; color: #999;">
                <span style="display: flex; align-items: center; gap: 5px;">
                  <img v-if="item.userAvatar" :src="item.userAvatar" style="width: 20px; height: 20px; border-radius: 50%;" />
                  <img v-else src="@/assets/images/Manager.png" style="width: 20px; height: 20px; border-radius: 50%;" />
                  {{ item.userName || '管理员' }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><View /></el-icon>
                  {{ item.views || 0 }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><Star /></el-icon>
                  {{ item.likeCount || 0 }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ item.replyCount || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="data.total > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
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
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import { ArrowLeft, Search, Star, View, ChatDotRound, Document } from '@element-plus/icons-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  news: [],
  searchTitle: '',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  orderBy: 'time',
  orderDirection: 'desc'
});

onMounted(() => {
  loadNews()
})

const loadNews = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    status: 1,
    orderBy: data.orderBy,
    orderDirection: data.orderDirection
  }
  if (data.searchTitle) {
    params.title = data.searchTitle
  }
  request.get('/news/selectPage', { params }).then(res => {
    if (res.code === '200' || res.code === 200) {
      data.news = res.data.list || res.data
      data.total = res.data.total || res.data.length
    } else {
      console.log('加载资讯失败:', res.msg)
      data.news = []
      data.total = 0
    }
  }).catch(err => {
    console.log('加载资讯失败:', err.message)
    data.news = []
    data.total = 0
  })
}

const handleSizeChange = (size) => {
  data.pageSize = size
  data.pageNum = 1
  loadNews()
}

const handleCurrentChange = (page) => {
  data.pageNum = page
  loadNews()
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  location.href = url
}

const stripHtml = (html) => {
  if (!html) return ''
  // 首先处理可能的HTML转义字符
  let unescapedHtml = html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
  
  const div = document.createElement('div')
  div.innerHTML = unescapedHtml
  return div.textContent || div.innerText || ''
}
</script>
<style>
.el-dropdown {
  cursor: pointer;
}
</style>