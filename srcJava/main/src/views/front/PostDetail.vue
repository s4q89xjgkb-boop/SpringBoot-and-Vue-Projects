<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          帖子详情
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
        <el-button @click="navTo('/login')" style="border: none; background: transparent; color: #2c82ff; font-size: 16px;">
          登录
        </el-button>
      </div>
    </div>

    <div style="max-width: 1200px; margin: 20px auto; display: flex; gap: 20px;">
      <div style="flex: 1; min-width: 0;">
        <div style="background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div v-if="data.post">
            <div style="font-size: 28px; font-weight: bold; margin-bottom: 20px; color: #333;">
              {{ data.post.title }}
            </div>
            <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee;">
              <img v-if="data.post.userAvatar" :src="data.post.userAvatar" style="width: 40px; height: 40px; border-radius: 50%;" />
              <img v-else src="@/assets/images/Manager.png" style="width: 40px; height: 40px; border-radius: 50%;" />
              <div>
                <div style="font-size: 14px; color: #333;">{{ data.post.userName }}</div>
                <div style="font-size: 12px; color: #999;">{{ data.post.time }}</div>
              </div>
              <el-tag size="small" type="success">{{ data.post.sectionTitle }}</el-tag>
              <div style="margin-left: auto; display: flex; gap: 20px; font-size: 13px; color: #666;">
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><View /></el-icon>
                  {{ data.post.views }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><Star /></el-icon>
                  {{ data.post.likeCount }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px;">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ data.post.replyCount }}
                </span>
              </div>
            </div>
            <div style="font-size: 16px; line-height: 1.8; color: #333; margin-bottom: 30px;" v-if="data.post.content" v-html="sanitizeContent(data.post.content)"></div>
            <div style="display: flex; gap: 15px; padding-top: 20px; border-top: 1px solid #eee;">
              <el-button @click="handleLike" :type="data.isLiked ? 'success' : 'default'" :icon="Star">
                {{ data.isLiked ? '已点赞' : '点赞' }} ({{ data.post.likeCount }})
              </el-button>
              <el-button @click="handleCollect" :type="data.isCollected ? 'success' : 'default'" :icon="Star">
                {{ data.isCollected ? '已收藏' : '收藏' }} ({{ data.post.collectCount }})
              </el-button>
            </div>
          </div>
        </div>

        <div style="background: white; border-radius: 8px; padding: 30px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            评论列表
          </div>
          <div v-if="data.user && data.user.token" style="margin-bottom: 30px;">
            <div style="display: flex; gap: 15px;">
              <img
                :src="data.user.avatar || '@/assets/images/Manager.png'"
                alt=""
                style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"
              />
              <div style="flex: 1;">
                <el-input
                  v-model="data.replyContent"
                  type="textarea"
                  :rows="4"
                  placeholder="发表你的评论..."
                />
                <div style="text-align: right; margin-top: 10px;">
                  <el-button type="primary" @click="submitReply">发表评论</el-button>
                </div>
              </div>
            </div>
          </div>
          <div v-else style="text-align: center; padding: 20px; color: #999; margin-bottom: 20px;">
            <el-button type="primary" @click="navTo('/login')">登录后发表评论</el-button>
          </div>

          <div v-if="data.replies.length === 0" style="text-align: center; padding: 40px; color: #999;">
            <el-icon :size="60" color="#ddd"><ChatDotRound /></el-icon>
            <div style="font-size: 16px; margin-top: 15px;">
              暂无评论
            </div>
          </div>

          <div v-for="reply in data.replies" :key="reply.id" style="margin-bottom: 20px; padding: 15px; background: #f9f9f9; border-radius: 8px;">
            <div style="display: flex; gap: 12px;">
              <img
                :src="reply.userAvatar || '@/assets/images/Manager.png'"
                alt=""
                style="width: 36px; height: 36px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"
              />
              <div style="flex: 1;">
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
                  <span style="font-size: 14px; font-weight: bold; color: #333;">{{ reply.userName || '未知用户' }}</span>
                  <span style="font-size: 12px; color: #999;">{{ reply.time }}</span>
                </div>
                <div style="font-size: 14px; color: #333; line-height: 1.6;">
                  {{ reply.content }}
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
import { reactive, ref, onMounted } from "vue";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import { Star, View, ChatDotRound, ArrowLeft } from '@element-plus/icons-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  post: null,
  postId: null,
  replies: [],
  replyTotal: 0,
  replyContent: '',
  isLiked: false,
  isCollected: false,
  likeId: null,
  collectId: null
});

onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search);
  data.postId = urlParams.get('id');
  if (data.postId) {
    loadPostDetail();
    loadReplies();
    checkLikeStatus();
    checkCollectStatus();
  }
});

const loadPostDetail = () => {
  request.get('/post/selectById/' + data.postId).then(res => {
    if (res.code === '200') {
      data.post = res.data
      // 添加观看历史
      addHistory()
    }
  }).catch(err => {
    console.log('加载帖子详情失败:', err.message)
    ElMessage.error('加载帖子详情失败，请稍后重试')
  })
}

const loadReplies = () => {
  request.get('/reply/selectByPostId/' + data.postId).then(res => {
    if (res.code === '200') {
      data.replies = res.data || []
      data.replyTotal = res.data.length || 0
    }
  }).catch(err => {
    console.log('加载评论失败:', err.message)
    data.replies = []
    data.replyTotal = 0
  })
}

const checkLikeStatus = () => {
  if (!data.user.id) return;
  request.get('/like/selectAll', {
    params: {
      userId: data.user.id,
      introductionId: data.postId,
      type: 'post'
    }
  }).then(res => {
    if (res.code === '200' && res.data && res.data.length > 0) {
      data.isLiked = true
      data.likeId = res.data[0].id
    }
  }).catch(err => {
    console.log('检查点赞状态失败:', err.message)
  })
}

const checkCollectStatus = () => {
  if (!data.user.id) return;
  request.get('/collect/selectAll', {
    params: {
      userId: data.user.id,
      introductionId: data.postId,
      type: 'post'
    }
  }).then(res => {
    if (res.code === '200' && res.data && res.data.length > 0) {
      data.isCollected = true
      data.collectId = res.data[0].id
    }
  }).catch(err => {
    console.log('检查收藏状态失败:', err.message)
  })
}

const handleLike = () => {
  if (!data.user.id) {
    ElMessage.warning('请先登录');
    return;
  }
  if (data.isLiked) {
    request.delete('/like/deleteById/' + data.likeId).then(res => {
      if (res.code === '200') {
        ElMessage.success('取消点赞成功');
        data.isLiked = false;
        data.post.likeCount--;
      }
    }).catch(err => {
      console.log('取消点赞失败:', err.message)
      ElMessage.error('操作失败，请稍后重试')
    })
  } else {
    request.post('/like/add', {
      userId: data.user.id,
      introductionId: data.postId,
      type: 'post'
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('点赞成功');
        data.isLiked = true;
        data.post.likeCount++;
      }
    }).catch(err => {
      console.log('点赞失败:', err.message)
      ElMessage.error('操作失败，请稍后重试')
    })
  }
}

const handleCollect = () => {
  if (!data.user.id) {
    ElMessage.warning('请先登录');
    return;
  }
  if (data.isCollected) {
    request.delete('/collect/deleteById/' + data.collectId).then(res => {
      if (res.code === '200') {
        ElMessage.success('取消收藏成功');
        data.isCollected = false;
        data.post.collectCount--;
      }
    }).catch(err => {
      console.log('取消收藏失败:', err.message)
      ElMessage.error('操作失败，请稍后重试')
    })
  } else {
    request.post('/collect/add', {
      userId: data.user.id,
      introductionId: data.postId,
      type: 'post'
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('收藏成功');
        data.isCollected = true;
        data.post.collectCount++;
      }
    }).catch(err => {
      console.log('收藏失败:', err.message)
      ElMessage.error('操作失败，请稍后重试')
    })
  }
}

const submitReply = () => {
  if (!data.replyContent.trim()) {
    ElMessage.warning('请输入评论内容');
    return;
  }
  request.post('/reply/add', {
      content: data.replyContent,
      userId: data.user.id,
      introductionId: data.postId,
      type: 'post'
    }).then(res => {
    if (res.code === '200') {
      ElMessage.success('评论成功');
      data.replyContent = '';
      loadReplies();
      data.post.replyCount++;
    }
  }).catch(err => {
    console.log('评论失败:', err.message)
    ElMessage.error('评论失败，请稍后重试')
  })
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  location.href = url
}

const addHistory = () => {
  if (!data.user || !data.user.token) return
  if (!data.postId) return

  request.post('/history/addOrUpdate', {
    contentId: data.postId,
    type: 'post',
    title: data.post.title,
    description: data.post.content ? data.post.content.substring(0, 100) : '',
    img: ''
  }).then(res => {
    if (res.code === '200') {
      console.log('添加观看历史成功')
    }
  }).catch(err => {
    console.log('添加观看历史失败，接口可能未实现:', err.message)
  })
}

const sanitizeContent = (html) => {
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
  
  const scripts = div.getElementsByTagName('script')
  for (let i = scripts.length - 1; i >= 0; i--) {
    scripts[i].parentNode.removeChild(scripts[i])
  }
  
  const iframes = div.getElementsByTagName('iframe')
  for (let i = iframes.length - 1; i >= 0; i--) {
    iframes[i].parentNode.removeChild(iframes[i])
  }
  
  const allElements = div.getElementsByTagName('*')
  for (let i = 0; i < allElements.length; i++) {
    const element = allElements[i]
    element.removeAttribute('onclick')
    element.removeAttribute('onload')
    element.removeAttribute('onerror')
  }
  
  return div.innerHTML
}
</script>
<style scoped>
.el-dropdown {
  cursor: pointer;
}

:deep(img) {
  max-width: 100%;
  height: auto;
}

:deep(p) {
  margin: 10px 0;
  line-height: 1.8;
}

:deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
  margin: 20px 0 10px 0;
  font-weight: bold;
}

:deep(ul), :deep(ol) {
  margin: 10px 0;
  padding-left: 20px;
}

:deep(li) {
  margin: 5px 0;
}

:deep(blockquote) {
  border-left: 4px solid #ddd;
  padding-left: 15px;
  margin: 15px 0;
  color: #666;
}

:deep(code) {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

:deep(pre) {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 5px;
  overflow-x: auto;
  margin: 15px 0;
}

:deep(pre code) {
  background: transparent;
  padding: 0;
}

:deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 15px 0;
}

:deep(table th), :deep(table td) {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

:deep(table th) {
  background: #f5f5f5;
  font-weight: bold;
}

:deep(a) {
  color: #2c82ff;
  text-decoration: none;
}

:deep(a:hover) {
  text-decoration: underline;
}
</style>
