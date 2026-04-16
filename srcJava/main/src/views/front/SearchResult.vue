<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          搜索结果
        </span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; gap: 15px; padding-left: 30px;">
        <div @click="navTo('/front/home')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          返回首页
        </div>
        <div @click="navTo('/front/articles')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          游戏文章
        </div>
        <div @click="navTo('/front/forum')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          交流论坛
        </div>
        <div style="flex: 1"></div>
      </div>
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
              <el-dropdown-item @click="navTo('/front/profile?tab=password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div v-else>
        <el-button @click="navTo('/login')" style="border: none; background: transparent; color: #2c82ff; font-size: 16px;">
          登录
        </el-button>
      </div>
    </div>

    <div style="max-width: 1200px; margin: 20px auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
      <div style="display: flex; align-items: center; margin-bottom: 30px;">
        <el-input
          v-model="data.searchKeyword"
          placeholder="搜索文章、帖子..."
          style="width: 400px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon color="#999"><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch" :icon="Search" style="background: #2c82ff; border: none; margin-left: 10px;">
          搜索
        </el-button>
        <div style="flex: 1"></div>
        <div style="font-size: 16px; color: #666;">
          搜索关键词: <span style="color: #2c82ff; font-weight: bold;">{{ data.searchKeyword }}</span>
        </div>
      </div>

      <div style="border-bottom: 2px solid #2fbd67; padding-bottom: 10px; margin-bottom: 20px;">
        <div style="font-size: 20px; font-weight: bold;">
          搜索结果
        </div>
      </div>

      <div v-if="data.loading" style="text-align: center; padding: 60px; color: #999;">
        <el-icon class="is-loading" style="font-size: 32px; margin-right: 10px;"><Loading /></el-icon>
        搜索中...
      </div>

      <div v-else-if="data.articles.length === 0 && data.posts.length === 0" style="text-align: center; padding: 60px; color: #999;">
        暂无相关结果
      </div>

      <div v-else>
        <!-- 文章结果 -->
        <div v-if="data.articles.length > 0" style="margin-bottom: 40px;">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; color: #333;">
            游戏文章
          </div>
          <div v-for="item in data.articles" :key="'article_' + item.id" style="margin-bottom: 25px; border-bottom: 1px solid #eee; padding-bottom: 20px;">
            <div style="display: flex; gap: 20px;">
              <div style="flex-shrink: 0;">
                <el-image
                  @click="navTo('/front/introductionDetail?id=' + item.id)"
                  :src="item.img"
                  :preview-src-list="[item.img]"
                  fit="cover"
                  style="width: 200px; height: 140px; border-radius: 8px; cursor: pointer"
                />
              </div>
              <div style="flex: 1; min-width: 0;">
                <div style="font-size: 18px; font-weight: bold; cursor: pointer; margin-bottom: 10px; color: #333;"
                     @click="navTo('/front/introductionDetail?id=' + item.id)">
                  {{ item.title }}
                </div>
                <div style="font-size: 14px; color: #666; line-height: 1.6; margin-bottom: 10px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                  {{ item.description }}
                </div>
                <div style="display: flex; align-items: center; gap: 10px; font-size: 13px; color: #999;">
                  <el-image
                    :src="item.userAvatar"
                    :preview-src-list="[item.userAvatar]"
                    fit="cover"
                    style="width: 24px; height: 24px; border-radius: 50%; cursor: pointer; object-fit: cover; overflow: hidden;"
                  />
                  <span>{{ item.userName }}</span>
                  <span>{{ item.time }}</span>
                </div>
                <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999; margin-top: 8px;">
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><Star /></el-icon>
                    {{ item.likeCount || 0 }} 点赞
                  </span>
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><Collection /></el-icon>
                    {{ item.collectCount || 0 }} 收藏
                  </span>
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><ChatDotRound /></el-icon>
                    {{ item.replyCount || 0 }} 评论
                  </span>
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><View /></el-icon>
                    {{ item.views || 0 }} 浏览
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 帖子结果 -->
        <div v-if="data.posts.length > 0">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; color: #333;">
            论坛帖子
          </div>
          <div v-for="item in data.posts" :key="'post_' + item.id" style="margin-bottom: 25px; border-bottom: 1px solid #eee; padding-bottom: 20px;">
            <div style="display: flex; gap: 20px;">
              <div style="flex-shrink: 0; display: flex; align-items: center; justify-content: center; width: 60px; height: 60px; border-radius: 50%; background: #f0f0f0; font-weight: bold; color: #666; font-size: 24px;">
                {{ item.title.charAt(0) }}
              </div>
              <div style="flex: 1; min-width: 0;">
                <div style="font-size: 18px; font-weight: bold; cursor: pointer; margin-bottom: 10px; color: #333;"
                     @click="navTo('/front/postDetail?id=' + item.id)">
                  {{ item.title }}
                </div>
                <div style="font-size: 14px; color: #666; line-height: 1.6; margin-bottom: 10px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                  {{ stripHtml(item.content) }}
                </div>
                <div style="display: flex; align-items: center; gap: 10px; font-size: 13px; color: #999;">
                  <span>{{ item.userName }}</span>
                  <span>{{ item.time }}</span>
                  <span>{{ item.sectionTitle }}</span>
                </div>
                <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999; margin-top: 8px;">
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><Star /></el-icon>
                    {{ item.likeCount || 0 }} 点赞
                  </span>
                  <span style="display: flex; align-items: center; gap: 4px;">
                    <el-icon><ChatDotRound /></el-icon>
                    {{ item.replyCount || 0 }} 评论
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import {reactive, onMounted} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import {Search, Loading, Star, Collection, ChatDotRound, View} from "@element-plus/icons-vue";

const route = useRoute()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  searchKeyword: '',
  articles: [],
  posts: [],
  loading: false
})

const handleSearch = () => {
  if (!data.searchKeyword.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  performSearch()
}

const performSearch = () => {
  data.loading = true
  data.articles = []
  data.posts = []

  // 同时搜索文章和帖子
  Promise.all([
    // 搜索文章
    request.get('/introduction/selectAll', {
      params: {
        title: data.searchKeyword,
        status: 1
      }
    }),
    // 搜索帖子
    request.get('/post/selectAll', {
      params: {
        title: data.searchKeyword,
        content: data.searchKeyword,
        status: 1
      }
    })
  ]).then(([articleRes, postRes]) => {
    data.loading = false

    // 处理文章结果
    if (articleRes && articleRes.code === '200' && articleRes.data) {
      data.articles = articleRes.data
    }

    // 处理帖子结果
    if (postRes && postRes.code === '200' && postRes.data) {
      data.posts = postRes.data
    }
  }).catch(err => {
    data.loading = false
    console.warn('搜索失败:', err)
    ElMessage.error('搜索失败，请稍后重试')
  })
}

const stripHtml = (html) => {
  if (!html) return ''
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

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  router.push(url)
}

onMounted(() => {
  // 从路由参数中获取搜索关键词
  if (route.query.keyword) {
    data.searchKeyword = route.query.keyword
    performSearch()
  }
})
</script>
<style>
.el-dropdown {
  cursor: pointer;
}
</style>