<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px;">
          游戏攻略系统
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
        <div @click="navTo('/front/news')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          游戏资讯
        </div>
        <div style="flex: 1"></div>
        <div v-if="data.user && data.user.token">
          <el-button type="primary" @click="navTo('/front/profile?tab=articles')" :icon="Plus">
            添加游戏攻略
          </el-button>
        </div>
      </div>
      <div v-if="data.user && data.user.token">
        <el-dropdown style="padding-right: 20px">
          <div style="width: fit-content; display: flex; align-items: center;">
            <img v-if="data.user.avatar" style="width: 50px; height: 50px; border-radius: 50%;" :src="data.user.avatar" />
            <img v-else style="width: 50px; height: 50px; border-radius: 50%;" src="@/assets/images/Manager.png" />
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

    <div style="max-width: 1200px; margin: 20px auto; display: flex; gap: 20px;">
      <div style="flex: 1; min-width: 0;">
        <div style="background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); margin-bottom: 20px;">
          <div style="font-size: 28px; font-weight: bold; margin-bottom: 20px; color: #333;">
            {{ data.introductionData.title }}
          </div>
          <div style="display: flex; align-items: center; gap: 20px; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee;">
            <img
              :src="data.introductionData.userAvatar || '@/assets/images/Manager.png'"
              alt=""
              style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"
            />
            <div>
              <div style="font-size: 15px; font-weight: 500; color: #333;">
                {{ data.introductionData.userName || '未知用户' }}
              </div>
              <div style="font-size: 13px; color: #999; margin-top: 3px;">
                {{ data.introductionData.time }}
              </div>
            </div>
            <div style="margin-left: auto; display: flex; gap: 15px;">
              <span style="font-size: 14px; color: #666;">
                分类：{{ data.introductionData.categoryTitle || '未分类' }}
              </span>
            </div>
          </div>
          <!-- 显示备用方案上传的视频 -->
          <div v-if="data.introductionData.video" style="margin-bottom: 20px;">
            <video :src="data.introductionData.video" controls style="width: 100%; max-height: 500px;"></video>
          </div>
          <!-- 直接显示文章内容中的视频 -->
          <div class="content-container" v-html="data.introductionData.content" style="font-size: 16px; line-height: 1.8; color: #333; min-height: 300px;"></div>

          <div style="display: flex; align-items: center; gap: 20px; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;">
            <div @click="handleLike" style="display: flex; align-items: center; gap: 6px; cursor: pointer; padding: 8px 16px; border-radius: 20px; transition: all 0.3s;" :style="{ background: data.isLiked ? '#ff6b6b' : '#f5f5f5', color: data.isLiked ? 'white' : '#666' }">
              <el-icon><Star /></el-icon>
              <span>{{ data.introductionData.likeCount || 0 }}</span>
            </div>
            <div @click="handleCollect" style="display: flex; align-items: center; gap: 6px; cursor: pointer; padding: 8px 16px; border-radius: 20px; transition: all 0.3s;" :style="{ background: data.isCollected ? '#ff9f43' : '#f5f5f5', color: data.isCollected ? 'white' : '#666' }">
              <el-icon><Collection /></el-icon>
              <span>{{ data.introductionData.collectCount || 0 }}</span>
            </div>
            <div style="display: flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 20px; background: #f5f5f5; color: #666;">
              <el-icon><ChatDotRound /></el-icon>
              <span>{{ data.introductionData.replyCount || 0 }}</span>
            </div>
            <div style="margin-left: auto; display: flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 20px; background: #f5f5f5; color: #666;">
              <el-icon><View /></el-icon>
              <span>{{ data.introductionData.views || 0 }} 浏览</span>
            </div>
          </div>
        </div>

        <div style="background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
            <span>回复列表</span>
            <span style="font-size: 14px; font-weight: normal; color: #999;">
              共 {{ data.replies.length }} 条回复
            </span>
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
                  placeholder="写下你的回复..."
                  maxlength="500"
                  show-word-limit
                />
                <div style="text-align: right; margin-top: 10px;">
                  <el-button type="primary" @click="submitReply" style="background: #2fbd67; border: none;">
                    发布回复
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <div v-else style="text-align: center; padding: 40px; background: #f9f9f9; border-radius: 8px; margin-bottom: 30px;">
            <el-icon :size="40" color="#ddd"><User /></el-icon>
            <div style="font-size: 16px; color: #666; margin-top: 10px;">
              请先登录后发表回复
            </div>
            <el-button type="primary" @click="navTo('/login')" style="margin-top: 15px; background: #2c82ff; border: none;">
              前往登录
            </el-button>
          </div>

          <div v-if="data.replies.length === 0" style="text-align: center; padding: 60px; color: #999;">
            <el-icon :size="60" color="#ddd"><ChatDotRound /></el-icon>
            <div style="font-size: 16px; margin-top: 15px;">
              暂无回复，快来抢沙发吧~
            </div>
          </div>

          <div v-else>
            <div v-for="reply in data.replies" :key="reply.id" style="margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #f5f5f5;">
              <div style="display: flex; gap: 15px;">
                <img
                    :src="reply.userAvatar || '@/assets/images/Manager.png'"
                    alt=""
                    style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; flex-shrink: 0;"
                  />
                <div style="flex: 1; min-width: 0;">
                  <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
                    <span style="font-size: 15px; font-weight: 500; color: #333;">
                      {{ reply.userName || '未知用户' }}
                    </span>
                    <span style="font-size: 13px; color: #999;">
                      {{ reply.time }}
                    </span>
                  </div>
                  <div style="font-size: 15px; color: #333; line-height: 1.6; word-break: break-all;">
                    {{ reply.content }}
                  </div>
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
import {reactive, onMounted, onUnmounted, computed} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import {Star, Collection, ChatDotRound, User, Plus} from "@element-plus/icons-vue";

const route = useRoute()

const introductionId = computed(() => route.query.id)

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData: {},
  replies: [],
  replyContent: '',
  isLiked: false,
  isCollected: false,
  videoUrls: [] // 存储从文章内容中提取的视频 URL
})

const loadIntroduction = () => {
  const id = introductionId.value
  if (!id) {
    ElMessage.error('文章ID不存在')
    return
  }
  request.get('/introduction/selectById/' + id).then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
      
      // 从文章内容中提取视频 URL
      data.videoUrls = []
      if (data.introductionData.content) {
        // 正则表达式匹配视频 URL，包括本地服务器和第三方视频平台的 URL
        const videoUrlRegex = /https?:\/\/[^\s"']+\.(mp4|avi|mov|wmv|flv|mkv)/gi;
        const videoMatches = data.introductionData.content.match(videoUrlRegex);
        
        if (videoMatches && videoMatches.length > 0) {
          // 去重并添加到视频 URL 列表
          const uniqueUrls = [...new Set(videoMatches)];
          data.videoUrls = uniqueUrls;
          console.log('提取到视频 URL:', data.videoUrls);
        }
        
        // 同时检查是否有 video 标签，并提取其中的 src
        const parser = new DOMParser();
        const doc = parser.parseFromString(data.introductionData.content, 'text/html');
        const videoTags = doc.getElementsByTagName('video');
        
        for (let i = 0; i < videoTags.length; i++) {
          const src = videoTags[i].src;
          if (src && !data.videoUrls.includes(src)) {
            data.videoUrls.push(src);
            console.log('从 video 标签提取到视频 URL:', src);
          }
        }
      }
      
      // 添加观看历史
      addHistory()
      
      loadReplies()
      checkUserActions()
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(err => {
    console.log('加载文章详情失败:', err.message)
    ElMessage.error('加载文章详情失败，请稍后重试')
  })
}

const loadReplies = () => {
  const id = introductionId.value
  if (!id) return
  request.get('/reply/selectByArticleId/' + id).then(res => {
    if (res.code === '200') {
      data.replies = res.data || []
    }
  }).catch(err => {
    console.log('加载回复失败，接口可能未实现:', err.message)
    data.replies = []
  })
}

const checkUserActions = () => {
  if (!data.user || !data.user.token) return
  const id = introductionId.value
  if (!id) return

  request.get('/like/check', {
    params: {
      userId: data.user.id,
      introductionId: id,
      type: 'article'
    }
  }).then(res => {
    if (res.code === '200') {
      data.isLiked = res.data || false
    }
  }).catch(err => {
    console.log('检查点赞状态失败，接口可能未实现:', err.message)
    data.isLiked = false
  })

  request.get('/collect/check', {
    params: {
      userId: data.user.id,
      introductionId: id,
      type: 'article'
    }
  }).then(res => {
    if (res.code === '200') {
      data.isCollected = res.data || false
    }
  }).catch(err => {
    console.log('检查收藏状态失败，接口可能未实现:', err.message)
    data.isCollected = false
  })
}

const handleLike = () => {
  if (!data.user || !data.user.token) {
    ElMessage.warning('请先登录')
    navTo('/login')
    return
  }
  const id = introductionId.value
  if (!id) {
    ElMessage.error('文章ID不存在')
    return
  }

  if (data.isLiked) {
    request.delete('/like/delete', {
      data: {
        userId: data.user.id,
        introductionId: id,
        type: 'article'
      }
    }).then(res => {
      if (res.code === '200') {
        data.isLiked = false
        data.introductionData.likeCount = Math.max(0, (data.introductionData.likeCount || 0) - 1)
        ElMessage.success('取消点赞')
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      if (err.response && err.response.status === 404) {
        ElMessage.error('点赞功能接口尚未实现，请联系管理员')
      } else {
        ElMessage.error('操作失败，请稍后重试')
      }
      console.log('取消点赞失败:', err.message)
    })
  } else {
    request.post('/like/add', {
      userId: data.user.id,
      introductionId: id,
      type: 'article'
    }).then(res => {
      if (res.code === '200') {
        data.isLiked = true
        data.introductionData.likeCount = (data.introductionData.likeCount || 0) + 1
        ElMessage.success('点赞成功')
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      if (err.response && err.response.status === 404) {
        ElMessage.error('点赞功能接口尚未实现，请联系管理员')
      } else {
        ElMessage.error('操作失败，请稍后重试')
      }
      console.log('点赞操作失败:', err.message)
    })
  }
}

const handleCollect = () => {
  if (!data.user || !data.user.token) {
    ElMessage.warning('请先登录')
    navTo('/login')
    return
  }
  const id = introductionId.value
  if (!id) {
    ElMessage.error('文章ID不存在')
    return
  }

  if (data.isCollected) {
    request.delete('/collect/delete', {
      data: {
        userId: data.user.id,
        introductionId: id,
        type: 'article'
      }
    }).then(res => {
      if (res.code === '200') {
        data.isCollected = false
        data.introductionData.collectCount = Math.max(0, (data.introductionData.collectCount || 0) - 1)
        ElMessage.success('取消收藏')
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      if (err.response && err.response.status === 404) {
        ElMessage.error('收藏功能接口尚未实现，请联系管理员')
      } else {
        ElMessage.error('操作失败，请稍后重试')
      }
      console.log('取消收藏失败:', err.message)
    })
  } else {
    request.post('/collect/add', {
      userId: data.user.id,
      introductionId: id,
      type: 'article'
    }).then(res => {
      if (res.code === '200') {
        data.isCollected = true
        data.introductionData.collectCount = (data.introductionData.collectCount || 0) + 1
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      if (err.response && err.response.status === 404) {
        ElMessage.error('收藏功能接口尚未实现，请联系管理员')
      } else {
        ElMessage.error('操作失败，请稍后重试')
      }
      console.log('收藏操作失败:', err.message)
    })
  }
}

const submitReply = () => {
  if (!data.replyContent.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  const id = introductionId.value
  if (!id) {
    ElMessage.error('文章ID不存在')
    return
  }

  request.post('/reply/add', {
      userId: data.user.id,
      introductionId: id,
      content: data.replyContent,
      type: 'article'
    }).then(res => {
    if (res.code === '200') {
      ElMessage.success('回复成功')
      data.replyContent = ''
      data.introductionData.replyCount = (data.introductionData.replyCount || 0) + 1
      loadReplies()
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(err => {
    if (err.response && err.response.status === 404) {
      ElMessage.error('回复功能接口尚未实现，请联系管理员')
    } else {
      ElMessage.error('回复失败，请稍后重试')
    }
    console.log('回复失败:', err.message)
  })
}

const addHistory = () => {
  if (!data.user || !data.user.token) return
  const id = introductionId.value
  if (!id) return

  request.post('/history/addOrUpdate', {
    contentId: id,
    type: 'article',
    title: data.introductionData.title,
    description: data.introductionData.description,
    img: data.introductionData.img
  }).then(res => {
    if (res.code === '200') {
      console.log('添加观看历史成功')
    }
  }).catch(err => {
    console.log('添加观看历史失败，接口可能未实现:', err.message)
  })
}

const navTo = (url) => {
  router.push(url)
}

onMounted(() => {
  window.scrollTo(0, 0)
  loadIntroduction()
})

onUnmounted(() => {
  // 清理视频资源，防止浏览器在后退时仍然尝试加载视频
  data.videoUrls = [];
  // 停止所有视频播放
  const videos = document.querySelectorAll('video');
  videos.forEach(video => {
    video.pause();
    video.src = '';
  });
})
</script>

<style scoped>
:deep(.content-container img) {
  max-width: 100%;
  height: auto;
}

:deep(.content-container video) {
  max-width: 100%;
  height: auto;
  max-height: 500px;
  margin: 20px 0;
  display: block;
}
</style>