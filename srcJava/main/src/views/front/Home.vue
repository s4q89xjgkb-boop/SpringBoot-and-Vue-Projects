<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 60px; background: #fff; display: flex; align-items: center; padding: 0 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
      <div style="display: flex; align-items: center; gap: 20px; flex: 1;">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 20px; font-weight: bold; color: #333; margin-left: 10px;">
          游戏攻略系统
        </span>
      </div>

      <div style="display: flex; align-items: center; gap: 15px;">
        <el-input
          v-model="data.searchKeyword"
          placeholder="搜索文章、视频..."
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon color="#999"><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch" :icon="Search" style="background: #2c82ff; border: none;">
          搜索
        </el-button>
      </div>

      <div style="flex: 1"></div>

      <div style="display: flex; align-items: center;">
        <div v-if="data.user && data.user.token">
          <el-dropdown>
            <div style="display: flex; align-items: center; gap: 10px; cursor: pointer; padding: 8px 15px; border-radius: 20px; transition: all 0.3s;" :style="{ background: data.showDropdown ? '#f5f5f5' : 'transparent' }" @click="data.showDropdown = !data.showDropdown">
              <img v-if="data.user.avatar" style="width: 36px; height: 36px; border-radius: 50%;" :src="data.user.avatar" />
              <img v-else style="width: 36px; height: 36px; border-radius: 50%; background: #e0e0e0; color: white;" src="@/assets/images/Manager.png" />
              <span style="font-size: 14px; color: #333; font-weight: 500;">{{ data.user.name }}</span>
              <el-icon color="#999" style="margin-left: 5px;"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="navTo('/front/profile')">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item @click="navTo('/front/profile')">
                  <el-icon><Lock /></el-icon>
                  <span>修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item divided></el-dropdown-item>
                <el-dropdown-item @click="handleAdminLogin">
                  <el-icon><Management /></el-icon>
                  <span>管理员登录</span>
                </el-dropdown-item>
                <el-dropdown-item @click="logout" style="color: #f56c6c;">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
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
    </div>

    <div style="max-width: 1400px; margin: 0 auto; padding: 20px;">
      <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); margin-bottom: 20px;">
        <div style="display: flex; gap: 20px; margin-bottom: 20px;">
          <div style="flex: 2; min-width: 0;">
            <el-carousel height="400px" :interval="4000" arrow="hover" indicator-position="outside">
              <el-carousel-item v-for="banner in data.banners" :key="banner.id">
                <div style="width: 100%; height: 100%; cursor: pointer;" @click="handleBannerClick(banner)">
                  <el-image
                    :src="banner.img"
                    :preview-src-list="[banner.img]"
                    fit="cover"
                    style="width: 100%; height: 100%;"
                  />
                  <div style="position: absolute; bottom: 0; left: 0; right: 0; background: rgba(0,0,0,0.5); padding: 15px; color: white;">
                    <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px;">
                      {{ banner.title }}
                    </div>
                    <div style="font-size: 14px; color: rgba(255,255,255,0.8);">
                      {{ banner.time }}
                    </div>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>
          <div style="flex: 1; min-width: 0;">
            <el-tabs v-model="data.activeTab" type="card" stretch>
              <el-tab-pane label="热门攻略" name="articles">
                <div style="height: 400px; overflow-y: auto;">
                  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #eee;">
                    <div style="font-size: 16px; font-weight: bold; color: #333;">
                      热门文章
                    </div>
                    <el-button type="text" size="small" @click="navTo('/front/articles?orderBy=views')" style="color: #2c82ff; padding: 0;">
                      更多 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                  <div v-if="data.hotArticles.length === 0" style="text-align: center; padding: 40px; color: #999;">
                    暂无文章
                  </div>
                  <div v-for="item in data.hotArticles" :key="item.id" style="margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #f5f5f5; cursor: pointer;" @click="navTo('/front/introductionDetail?id=' + item.id)">
                    <div style="display: flex; gap: 10px;">
                      <div style="flex-shrink: 0;">
                        <el-image
                          :src="item.img"
                          :preview-src-list="[item.img]"
                          fit="cover"
                          style="width: 80px; height: 60px; border-radius: 4px;"
                        />
                      </div>
                      <div style="flex: 1; min-width: 0;">
                        <div style="font-size: 15px; font-weight: bold; margin-bottom: 5px; color: #333;">
                          {{ item.title }}
                        </div>
                        <div style="font-size: 13px; color: #999; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 1; overflow: hidden;">
                          {{ item.description }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="交流论坛" name="forum">
                <div style="height: 400px; overflow-y: auto;">
                  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #eee;">
                    <div style="font-size: 16px; font-weight: bold; color: #333;">
                      最新帖子
                    </div>
                    <el-button type="text" size="small" @click="navTo('/front/forum')" style="color: #2c82ff; padding: 0;">
                      热门论坛 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                  <div v-if="data.hotPosts?.length === 0" style="text-align: center; padding: 40px; color: #999;">
                    暂无帖子
                  </div>
                  <div v-for="post in data.hotPosts" :key="post.id" style="margin-bottom: 15px; padding-bottom: 15px; border-bottom:1px solid #f5f5f5; cursor: pointer;" @click="navTo('/front/postDetail?id=' + post.id)">
                    <div style="font-size: 15px; font-weight: bold; margin-bottom: 5px; color: #333;">
                      {{ post.title }}
                    </div>
                    <div style="font-size: 13px; color: #999; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                      <div v-html="stripHtml(post.content)"></div>
                    </div>
                    <div style="font-size: 12px; color: #999; margin-top: 5px;">
                      {{ post.time }} · {{ post.userName }}
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="游戏资讯" name="notices">
                <div style="height: 400px; overflow-y: auto;">
                  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #eee;">
                    <div style="font-size: 16px; font-weight: bold; color: #333;">
                      最新游戏资讯
                    </div>
                    <el-button type="text" size="small" @click="navTo('/front/news')" style="color: #2c82ff; padding: 0;">
                      更多 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                  <div v-if="data.news.length === 0" style="text-align: center; padding: 40px; color: #999;">
                    暂无游戏资讯
                  </div>
                  <div v-for="news in data.news" :key="news.id" style="margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #f5f5f5; cursor: pointer;" @click="navTo('/front/newsDetail?id=' + news.id)">
                    <div style="font-size: 15px; font-weight: bold; margin-bottom: 5px; color: #333;">
                      {{ news.title }}
                    </div>
                    <div style="font-size: 13px; color: #999; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                      {{ news.content }}
                    </div>
                    <div style="font-size: 12px; color: #999; margin-top: 5px;">
                      {{ news.time }}
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>

      <div style="background: white; border-radius: 8px; padding: 15px 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); margin-bottom: 20px;">
        <div style="display: flex; align-items: center;">
          <div style="display: flex; align-items: center; gap: 8px; padding-right: 15px; border-right: 2px solid #2fbd67;">
            <el-icon style="color: #2fbd67; font-size: 20px;"><Bell /></el-icon>
            <span style="font-size: 16px; font-weight: bold; color: #333;">公告</span>
          </div>
          <div style="flex: 1; padding-left: 15px; overflow: hidden;">
            <el-carousel height="30px" direction="vertical" :autoplay="true" :interval="3000" indicator-position="none" arrow="never">
              <el-carousel-item v-for="notice in data.carouselNotices" :key="notice.id">
                <div style="display: flex; align-items: center; height: 30px; cursor: pointer;" @click="viewNotice(notice)">
                  <span style="font-size: 14px; color: #666; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                    {{ notice.title }}
                  </span>
                  <span style="font-size: 12px; color: #999; margin-left: 15px;">
                    {{ notice.time }}
                  </span>
                </div>
              </el-carousel-item>
            </el-carousel>
            <div v-if="data.carouselNotices.length === 0" style="font-size: 14px; color: #999; text-align: center; padding: 0 20px;">
              暂无走马灯公告
            </div>
          </div>
          <div style="padding-left: 15px;">
            <el-button type="text" size="small" @click="navTo('/front/noticelist')" style="color: #2c82ff; padding: 0;">
              查看全部 <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
      </div>

      <div style="width: 100%; margin-top: 20px;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; margin-bottom: 20px;">
            <div style="font-size: 20px; font-weight: bold;">
              游戏文章
            </div>
            <div style="display: flex; align-items: center; gap: 15px;">
              <el-select
                v-model="data.selectedCategoryId"
                placeholder="选择游戏分类"
                filterable
                clearable
                style="width: 400px;"
                @change="filterByCategory"
                popper-class="category-select-dropdown"
              >
                <el-option
                  v-for="category in data.categories"
                  :key="category.id"
                  :label="category.title"
                  :value="category.id"
                >
                  <span style="font-size: 12px; padding: 3px 10px; border: 1px solid #dcdfe6; border-radius: 12px; background: #f5f7fa; display: inline-block;">{{ category.title }}</span>
                </el-option>
              </el-select>
              <el-button type="primary" size="small" @click="navTo('/front/articles')" style="background: #2fbd67; border: none;">
                查看更多
              </el-button>
            </div>
          </div>

            <div v-if="data.introductionData.length === 0" style="text-align: center; padding: 40px; color: #999;">
              暂无文章
            </div>
            <div style="display: flex; flex-wrap: wrap; gap: 20px;">
              <div v-for="item in data.introductionData" :key="item.id" style="width: calc(50% - 10px); margin-bottom: 20px; border: 1px solid #eee; padding: 15px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                <div style="display: flex; gap: 15px;">
                  <div style="flex-shrink: 0;">
                    <el-image
                      @click="navTo('/front/introductionDetail?id=' + item.id)"
                      :src="item.img"
                      :preview-src-list="[item.img]"
                      fit="cover"
                      style="width: 120px; height: 90px; border-radius: 6px; cursor: pointer"
                    />
                  </div>
                  <div style="flex: 1; min-width: 0;">
                    <div style="font-size: 16px; font-weight: bold; cursor: pointer; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;" 
                         @click="navTo('/front/introductionDetail?id=' + item.id)">
                      {{ item.title }}
                    </div>
                    <div style="font-size: 13px; color: #666; line-height: 1.5; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                      {{ item.description }}
                    </div>
                    <div style="display: flex; align-items: center; gap: 10px; font-size: 12px; color: #999;">
                      <el-image
                        :src="item.userAvatar"
                        :preview-src-list="[item.userAvatar]"
                        fit="cover"
                        style="width: 20px; height: 20px; border-radius: 50%; cursor: pointer; object-fit: cover; overflow: hidden;"
                      />
                      <span>{{ item.userName }}</span>
                      <span>{{ item.time }}</span>
                    </div>
                    <div style="display: flex; align-items: center; gap: 10px; font-size: 11px; color: #999; margin-top: 6px;">
                      <span style="display: flex; align-items: center; gap: 3px;">
                        <el-icon><Star /></el-icon>
                        {{ item.likeCount || 0 }}
                      </span>
                      <span style="display: flex; align-items: center; gap: 3px;">
                        <el-icon><Collection /></el-icon>
                        {{ item.collectCount || 0 }}
                      </span>
                      <span style="display: flex; align-items: center; gap: 3px;">
                        <el-icon><ChatDotRound /></el-icon>
                        {{ item.replyCount || 0 }}
                      </span>
                      <span style="display: flex; align-items: center; gap: 3px;">
                        <el-icon><View /></el-icon>
                        {{ item.views || 0 }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
        </div>
      </div>

      <div style="width: 100%;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #2c82ff; padding-bottom: 10px; margin-bottom: 20px;">
            <div style="font-size: 20px; font-weight: bold;">
              交流论坛
            </div>
            <div style="display: flex; align-items: center; gap: 15px;">
              <el-button type="primary" size="small" @click="navTo('/front/forum')" style="background: #2c82ff; border: none;">
                查看更多
              </el-button>
            </div>
          </div>

            <div v-if="data.hotPosts.length === 0" style="text-align: center; padding: 40px; color: #999;">
              暂无帖子
            </div>
            <div style="display: flex; flex-wrap: wrap; gap: 20px;">
              <div v-for="post in data.hotPosts" :key="post.id" style="width: calc(50% - 10px); margin-bottom: 20px; border: 1px solid #eee; padding: 15px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                <div style="display: flex; gap: 15px;">
                  <div style="flex-shrink: 0; display: flex; align-items: center; justify-content: center; width: 40px; height: 40px; border-radius: 50%; background: #f0f0f0; font-weight: bold; color: #666;">
                    {{ post.title.charAt(0) }}
                  </div>
                  <div style="flex: 1; min-width: 0;">
                    <div style="font-size: 16px; font-weight: bold; cursor: pointer; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;" 
                         @click="navTo('/front/postDetail?id=' + post.id)">
                      {{ post.title }}
                    </div>
                    <div style="font-size: 13px; color: #666; line-height: 1.5; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                      {{ stripHtml(post.content) }}
                    </div>
                    <div style="display: flex; align-items: center; gap: 10px; font-size: 12px; color: #999;">
                      <span>{{ post.userName }}</span>
                      <span>{{ post.time }}</span>
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
import {reactive, onMounted, ref} from "vue";
import router from "@/router/index.js";
import request from "@/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {ArrowRight, Star, Collection, ChatDotRound, Bell, Plus, Search} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData: [],
  hotArticles: [],
  categories: [],
  notices: [],
  carouselNotices: [],
  videos: [],
  banners: [],
  hotPosts: [],
  searchKeyword: '',
  selectedCategoryId: null,
  showDropdown: false,
  activeTab: 'articles',
  hotTags: ['RPG', 'FPS', 'MOBA', '策略', '冒险', '动作', '模拟', '体育'],
  adminDialogVisible: false,
  adminUsername: '',
  adminPassword: '',
  news: [] // 游戏资讯数据
})

const loadIntroduction = () => {
  const params = data.selectedCategoryId ? { categoryId: data.selectedCategoryId, orderBy: 'views', status: 1 } : { orderBy: 'views', status: 1 }
  request.get('/introduction/selectAll', { params }).then(res => {
    if (res && res.code === '200' && res.data) {
      data.introductionData = res.data.slice(0, 8)
    } else {
      console.warn('加载文章失败:', res?.msg || '接口返回异常')
      data.introductionData = []
    }
  }).catch(err => {
    console.warn('加载文章接口错误:', err.message)
    data.introductionData = []
  })
}

const loadHotArticles = () => {
  request.get('/introduction/selectAll', { params: { orderBy: 'views', status: 1 } }).then(res => {
    if (res && res.code === '200' && res.data) {
      const articles = res.data.map(item => ({
        ...item,
        totalScore: (item.likeCount || 0) + (item.collectCount || 0) + (item.replyCount || 0) + (item.views || 0)
      }))
      data.hotArticles = articles.sort((a, b) => b.totalScore - a.totalScore).slice(0, 5)
    } else {
      console.warn('加载热门文章失败:', res?.msg || '接口返回异常')
      data.hotArticles = []
    }
  }).catch(err => {
    console.warn('加载热门文章接口错误:', err.message)
    data.hotArticles = []
  })
}

const loadCategories = () => {
  request.get('/category/selectAll').then(res => {
    if (res && res.code === '200' && res.data) {
      data.categories = res.data
    } else {
      console.warn('加载分类失败:', res?.msg || '接口返回异常')
      data.categories = []
    }
  }).catch(err => {
    console.warn('加载分类接口错误:', err.message)
    data.categories = []
  })
}

const loadNotices = () => {
  request.get('/notice/selectAll').then(res => {
    if (res && res.code === '200' && res.data) {
      data.notices = res.data.slice(0, 5)
    } else {
      console.warn('加载公告失败:', res?.msg || '接口返回异常')
      data.notices = []
    }
  }).catch(err => {
    console.warn('加载公告接口错误:', err.message)
    data.notices = []
  })
}

const loadNews = () => {
  request.get('/news/selectAll', { params: { orderBy: 'time', orderDirection: 'desc', status: 1 } }).then(res => {
    if (res && res.code === '200' && res.data) {
      data.news = res.data.slice(0, 5)
    } else {
      console.warn('加载游戏资讯失败:', res?.msg || '接口返回异常')
      data.news = []
    }
  }).catch(err => {
    console.warn('加载游戏资讯接口错误:', err.message)
    data.news = []
  })
}

const loadCarouselNotices = () => {
  request.get('/notice/selectAll').then(res => {
    console.log('走马灯公告数据:', res)
    if (res && res.code === '200' && res.data) {
      data.carouselNotices = res.data.filter(notice => notice.showInCarousel === true)
      console.log('筛选后的走马灯公告:', data.carouselNotices)
    } else {
      console.warn('加载走马灯公告失败:', res?.msg || '接口返回异常')
      data.carouselNotices = []
    }
  }).catch(err => {
    console.warn('加载走马灯公告失败:', err.message)
    data.carouselNotices = []
  })
}

const loadBanners = () => {
  request.get('/banner/selectAll').then(res => {
    if (res && res.code === '200' && res.data) {
      data.banners = res.data
    } else {
      console.warn('加载轮播图失败:', res?.msg || '接口返回异常')
      data.banners = []
    }
  }).catch(err => {
    console.warn('加载轮播图接口错误:', err.message)
    data.banners = []
  })
}

const loadVideos = () => {
  request.get('/video/selectAll').then(res => {
    if (res && res.code === '200' && res.data) {
      data.videos = res.data.slice(0, 4)
    } else {
      console.warn('加载视频失败:', res?.msg || '接口返回异常')
      data.videos = []
    }
  }).catch(err => {
    console.warn('视频接口未实现:', err.message)
    data.videos = []
  })
}

const loadPosts = () => {
  // 使用真实的post接口获取帖子数据
  request.get('/post/selectAll', { 
    params: { 
      status: 1, 
      orderBy: 'time', 
      orderDirection: 'desc' 
    } 
  }).then(res => {
    if (res && res.code === '200' && res.data) {
      // 使用真实的post数据
      data.hotPosts = res.data.slice(0, 5)
    } else {
      console.warn('加载热门帖子失败:', res?.msg || '接口返回异常')
      data.hotPosts = []
    }
  }).catch(err => {
    console.warn('加载热门帖子失败:', err.message)
    data.hotPosts = []
  })
}

const filterByCategory = (categoryId) => {
  data.selectedCategoryId = categoryId
  loadIntroduction()
}

const clearFilter = () => {
  data.selectedCategoryId = null
  loadIntroduction()
}

const handleSearch = () => {
  if (!data.searchKeyword.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  navTo('/front/search?keyword=' + encodeURIComponent(data.searchKeyword))
}

const handleBannerClick = (banner) => {
  if (banner.link) {
    window.open(banner.link, '_blank')
  }
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const handleAdminLogin = () => {
  // 检查用户是否已经是管理员，如果是则直接跳转到后台
  if (data.user && (data.user.role === 'admin' || data.user.role === 'super_admin')) {
    location.href = '/manager/home'
  } else {
    // 普通用户点击时弹出确认弹窗
    ElMessageBox.confirm(
      '您是否要登录管理员账号？继续操作将退出当前账号。',
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(() => {
      localStorage.removeItem('code_user')
      location.href = '/login?role=admin'
    }).catch(() => {
    })
  }
}

const navTo = (url) => {
  router.push(url)
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

const playVideo = (url) => {
  window.open(url, '_blank')
}

const viewNotice = (notice) => {
  if (notice.content) {
    ElMessageBox.alert(notice.content, notice.title, {
      confirmButtonText: '确定',
      dangerouslyUseHTMLString: false
    })
  } else {
    ElMessage.info('暂无详情')
  }
}

const showAdminLoginDialog = () => {
  data.adminDialogVisible = true
}

const confirmAdminLogin = () => {
  if (!data.adminUsername || !data.adminPassword) {
    ElMessage.warning('请输入管理员账号和密码')
    return
  }
  
  localStorage.removeItem('code_user')
  
  request.post('/login', {
    username: data.adminUsername,
    password: data.adminPassword
  }).then(res => {
    if (res.code === '200' || res.code === 200) {
      ElMessage.success('管理员登录成功')
      data.adminDialogVisible = false
      
      localStorage.setItem('code_user', JSON.stringify({
        ...res.data,
        role: 'admin'
      }))
      
      setTimeout(() => {
        location.href = '/manager/home'
      }, 500)
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  }).catch(err => {
    ElMessage.error('登录失败，请稍后重试')
  })
}

onMounted(() => {
  loadIntroduction()
  loadHotArticles()
  loadCategories()
  loadNotices()
  loadCarouselNotices()
  loadBanners()
  loadPosts()
  loadNews()
})
</script>
<style>
.el-dropdown {
  cursor: pointer;
}

.category-select-dropdown {
  min-width: 400px !important;
  max-width: 600px !important;
}

.category-select-dropdown .el-select-dropdown__wrap {
  max-height: 400px;
}

.category-select-dropdown .el-select-dropdown__list {
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
  gap: 10px;
}

.category-select-dropdown .el-select-dropdown__item {
  width: calc(33.333% - 7px);
  padding: 8px 5px;
  height: auto;
  line-height: 1.5;
  text-align: center;
  margin: 0;
}

.category-select-dropdown .el-select-dropdown__item span {
  font-size: 12px;
  padding: 6px 8px;
  border: 1px solid #dcdfe6;
  border-radius: 12px;
  background: #f5f7fa;
  display: inline-block;
  transition: all 0.3s;
  width: 100%;
  box-sizing: border-box;
}

.category-select-dropdown .el-select-dropdown__item:hover span {
  background: #2fbd67;
  color: white;
  border-color: #2fbd67;
}

.category-select-dropdown .el-select-dropdown__item.is-selected span {
  background: #2fbd67;
  color: white;
  border-color: #2fbd67;
}
</style>
