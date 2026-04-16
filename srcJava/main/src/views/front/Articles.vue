<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          所有文章
        </span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; gap: 15px; padding-left: 30px;">
        <div @click="navTo('/front/home')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          返回首页
        </div>
        <div @click="navTo('/front/forum')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          交流论坛
        </div>
        <div @click="navTo('/front/news')" style="padding: 8px 15px; cursor: pointer; border-radius: 4px; transition: all 0.3s; background: transparent; color: #333; font-size: 14px;">
          游戏资讯
        </div>
        <div style="flex: 1"></div>
        <div v-if="data.user && data.user.token">
          <el-button type="primary" @click="handleAddArticle" :icon="Plus">
            添加游戏攻略
          </el-button>
        </div>
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

    <div style="max-width: 1200px; margin: 20px auto; display: flex; gap: 20px;">
      <div style="width: 250px; flex-shrink: 0;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            游戏分类
          </div>
          <div v-for="category in data.categories" :key="category.id" 
               @click="filterByCategory(category.id)"
               style="padding: 12px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.selectedCategoryId === category.id ? '#2fbd67' : 'transparent', color: data.selectedCategoryId === category.id ? 'white' : '#333' }">
            {{ category.title }}
          </div>
          <div v-if="data.selectedCategoryId" @click="clearFilter" 
               style="padding: 12px; cursor: pointer; border-radius: 4px; background: #f0f0ee; color: #666; margin-top: 10px; text-align: center;">
            清除筛选
          </div>
        </div>

        <div style="background: white; border-radius: 8px; padding: 20px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            快捷导航
          </div>
          <div @click="navTo('/front/forum')" style="padding: 12px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s; background: transparent; color: #333;">
            交流论坛
          </div>
          <div @click="navTo('/front/news')" style="padding: 12px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s; background: transparent; color: #333;">
            游戏资讯
          </div>
        </div>
      </div>

      <div style="flex: 1; min-width: 0;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; margin-bottom: 20px;">
            <div style="font-size: 20px; font-weight: bold;">
              文章列表
            </div>
            <div style="display: flex; gap: 10px;">
              <el-select v-model="data.orderBy" placeholder="排序方式" style="width: 150px;" @change="handleSortChange">
                <el-option label="浏览量" value="views"></el-option>
                <el-option label="点赞数" value="likeCount"></el-option>
                <el-option label="收藏数" value="collectCount"></el-option>
                <el-option label="评论数" value="replyCount"></el-option>
                <el-option label="发布时间" value="time"></el-option>
              </el-select>
              <el-select v-model="data.orderDirection" placeholder="排序方向" style="width: 120px;" @change="handleSortChange">
                <el-option label="从高到低" value="desc"></el-option>
                <el-option label="从低到高" value="asc"></el-option>
              </el-select>
              <el-input
                v-model="data.searchKeyword"
                placeholder="搜索文章标题..."
                style="width: 300px;"
                @keyup.enter="handleSearch"
              />
              <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
            </div>
          </div>

          <div v-if="data.introductionData.length === 0" style="text-align: center; padding: 40px; color: #999;">
            暂无文章
          </div>
          <div v-for="item in data.introductionData" :key="item.id" style="margin-bottom: 25px; border-bottom: 1px solid #eee; padding-bottom: 20px;">
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
                <div style="font-size: 18px; font-weight: bold; cursor: pointer; margin-bottom: 10px;" 
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

          <div v-if="data.total > 0" style="display: flex; justify-content: center; align-items: center; padding: 30px 0;">
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

        <div style="width: 300px; flex-shrink: 0;">
          <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
              系统公告
            </div>
            <div v-if="data.notices.length === 0" style="text-align: center; padding: 40px; color: #999;">
              暂无公告
            </div>
            <div v-for="notice in data.notices" :key="notice.id" style="padding: 15px 0; border-bottom: 1px solid #eee; cursor: pointer;" @click="viewNotice(notice)">
              <div style="font-size: 15px; font-weight: bold; margin-bottom: 8px; color: #333;">
                {{ notice.title }}
              </div>
              <div style="font-size: 13px; color: #999;">
                {{ notice.time }}
              </div>
            </div>
            <div v-if="data.notices.length > 0" @click="navTo('/front/noticelist')" 
                 style="text-align: center; padding: 12px; cursor: pointer; color: #2c82ff; margin-top: 10px;">
              查看全部公告
            </div>
          </div>

          <div style="background: white; border-radius: 8px; padding: 20px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
              热门标签
            </div>
            <div style="display: flex; flex-wrap: wrap; gap: 8px;">
              <el-tag v-for="tag in data.hotTags" :key="tag" type="info" size="small" style="margin-bottom: 8px; cursor: pointer;">
                {{ tag }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <el-dialog :title="data.articleForm.id ? '编辑文章' : '新增文章'" v-model="data.articleFormVisible" width="70%" destroy-on-close>
    <el-form ref="articleFormRef" :model="data.articleForm" :rules="data.articleRules" label-width="100px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="title" label="文章标题">
        <el-input v-model="data.articleForm.title" autocomplete="off" placeholder="请输入标题"/>
      </el-form-item>
      <el-form-item prop="categoryId" label="文章分类">
        <el-select
            v-model="data.articleForm.categoryId"
            placeholder="请选择文章分类"
            style="width: 100%"
        >
          <el-option
              v-for="category in data.categories"
              :key="category.id"
              :label="category.title"
              :value="category.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="content" label="文章内容">
        <div style="border: 1px solid #ccc; width: 100%">
          <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="articleEditorRef"
              :mode="articleMode"
          />
          <Editor
              style="height: 400px; overflow-y: hidden;"
              v-model="data.articleForm.content"
              :mode="articleMode"
              :default-config="articleEditorConfig"
              @onCreated="articleHandleCreated"
          />
        </div>
      </el-form-item>
      <el-form-item prop="img" label="预览图">
        <el-upload
            action="http://localhost:1306/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleArticleFileSuccess"
            list-type="picture"
        >
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="video" label="视频">
        <el-upload
            action="http://localhost:1306/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleArticleVideoSuccess"
            list-type="file"
            accept="video/*"
        >
          <el-button type="primary">上传视频</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.articleFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveArticle">保 存</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import {reactive, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import {Star, Collection, ChatDotRound, View, Search, Plus} from "@element-plus/icons-vue";
import '@wangeditor/editor/dist/css/style.css';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';

const route = useRoute()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData: [],
  categories: [],
  notices: [],
  currentPage: 1,
  pageSize: 10,
  total: 0,
  searchKeyword: '',
  selectedCategoryId: null,
  orderBy: 'views',
  orderDirection: 'desc',
  hotTags: ['RPG', 'FPS', 'MOBA', '策略', '冒险', '动作', '模拟', '体育'],
  articleFormVisible: false,
  articleForm: {},
  articleRules: {
    title: [
      { required: true, message: '请填写标题', trigger: 'blur' }
    ],
    categoryId: [
      { required: true, message: '请选择分类', trigger: 'change' }
    ],
    content: [
      { required: true, message: '请填写内容', trigger: 'blur' }
    ],
    img: [
      { required: true, message: '请上传预览图', trigger: 'blur' }
    ],
    video: [
      { required: false, message: '请上传视频', trigger: 'blur' }
    ]
  }
})

const loadIntroduction = () => {
  const params = data.selectedCategoryId ? { categoryId: data.selectedCategoryId } : {}
  if (data.searchKeyword.trim()) {
    params.title = data.searchKeyword
  }
  if (data.orderBy) {
    params.orderBy = data.orderBy
  }
  if (data.orderDirection) {
    params.orderDirection = data.orderDirection
  }
  params.status = 1
  request.get('/introduction/selectPage', { 
    params: { 
      ...params,
      pageNum: data.currentPage,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.introductionData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadCategories = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categories = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadNotices = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.notices = res.data.slice(0, 5)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const filterByCategory = (categoryId) => {
  data.selectedCategoryId = categoryId
  data.currentPage = 1
  loadIntroduction()
}

const clearFilter = () => {
  data.selectedCategoryId = null
  data.currentPage = 1
  loadIntroduction()
}

const handleSizeChange = (size) => {
  data.pageSize = size
  data.currentPage = 1
  loadIntroduction()
}

const handleCurrentChange = (page) => {
  data.currentPage = page
  loadIntroduction()
}

const handleSearch = () => {
  if (!data.searchKeyword.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  data.currentPage = 1
  loadIntroduction()
}

const handleSortChange = () => {
  data.currentPage = 1
  loadIntroduction()
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const navTo = (url) => {
  router.push(url)
}

const viewNotice = (notice) => {
  ElMessage.info(notice.content || '暂无详情')
}

const articleFormRef = ref();
const articleEditorRef = ref();
const articleMode = 'default';
const articleEditorConfig = {
  // 启用视频菜单
  menus: [
    'head',
    'bold',
    'italic',
    'underline',
    'strike',
    'list',
    'justify',
    'image',
    'video',
    'insert',
    'table',
    'code',
    'splitLine',
    'undo',
    'redo'
  ],
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:1306/files/wang/upload',
      fieldName: 'file',
      headers: {
        token: data.user?.token || ''
      },
      withCredentials: false,
      timeout: 30 * 1000,
      onSuccess: (file, res) => {
        if (res.errno === 0) {
          return {
            url: res.data[0].url
          };
        } else {
          return {
            errMsg: res.msg || '上传失败'
          };
        }
      },
      onError: (file, err) => {
        console.error('上传失败:', err);
      }
    },
    uploadVideo: {
      server: 'http://localhost:1306/files/wang/upload',
      fieldName: 'file',
      headers: {
        token: data.user?.token || ''
      },
      withCredentials: false,
      timeout: 30 * 1000,
      maxFileSize: 5 * 1024 * 1024 * 1024, // 5GB
      onSuccess: (file, res) => {
        if (res.errno === 0) {
          return {
            url: res.data[0].url
          };
        } else {
          return {
            errMsg: res.msg || '上传失败'
          };
        }
      },
      onError: (file, err) => {
        console.error('上传失败:', err);
      }
    }
  }
};

const handleAddArticle = () => {
  data.articleForm = {};
  data.articleFormVisible = true;
};

const saveArticle = () => {
  articleFormRef.value.validate((valid) => {
    if (!valid) return;
    const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
    data.articleForm.userId = userFromStorage.id;
    
    const url = data.articleForm.id ? '/introduction/update' : '/introduction/add';
    const method = data.articleForm.id ? request.put : request.post;
    
    method(url, data.articleForm).then(res => {
      if (res.code === '200' || res.code === 200) {
        ElMessage.success(data.articleForm.id ? '修改成功' : '发布成功，等待审核');
        data.articleFormVisible = false;
        loadIntroduction();
      } else {
        ElMessage.error(res.msg);
      }
    })
  })
};

const handleArticleFileSuccess = (res) => {
  if (res.code === '200' || res.code === 200) {
    data.articleForm.img = res.data;
    ElMessage.success('图片上传成功');
  } else {
    ElMessage.error('图片上传失败：' + res.msg);
  }
};

const handleArticleVideoSuccess = (res) => {
  if (res.code === '200' || res.code === 200) {
    data.articleForm.video = res.data;
    ElMessage.success('视频上传成功');
  } else {
    ElMessage.error('视频上传失败：' + res.msg);
  }
};

const articleHandleCreated = (editor) => {
  articleEditorRef.value = editor;
};

onMounted(() => {
  if (route.query.orderBy) {
    data.orderBy = route.query.orderBy
  }
  loadIntroduction()
  loadCategories()
  loadNotices()
})
</script>
<style>
.el-dropdown {
  cursor: pointer;
}
</style>