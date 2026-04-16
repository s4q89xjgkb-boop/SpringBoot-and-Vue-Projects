<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          交流论坛
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

    <div style="max-width: 1200px; margin: 20px auto; display: flex; gap: 20px;">
      <div style="width: 280px; flex-shrink: 0;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            论坛板块
          </div>
          <div @click="selectSection(null)" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s; display: flex; align-items: center; gap: 10px;"
               :style="{ background: data.selectedSectionId === null ? '#2fbd67' : 'transparent', color: data.selectedSectionId === null ? 'white' : '#333' }">
            <el-icon><ChatDotRound /></el-icon>
            <span>全部帖子</span>
          </div>
          <div v-for="section in data.sections" :key="section.id" 
               @click="selectSection(section.id)" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s; display: flex; align-items: center; gap: 10px;"
               :style="{ background: data.selectedSectionId === section.id ? '#2fbd67' : 'transparent', color: data.selectedSectionId === section.id ? 'white' : '#333' }">
            <img v-if="section.icon" :src="section.icon" style="width: 24px; height: 24px; border-radius: 4px;" />
            <el-icon v-else><Folder /></el-icon>
            <span>{{ section.title }}</span>
            <span style="margin-left: auto; font-size: 12px; opacity: 0.8;">{{ section.postCount }}</span>
          </div>
        </div>

        <div style="background: white; border-radius: 8px; padding: 20px; margin-top: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            热门论坛
          </div>
          <div v-for="section in data.hotSections" :key="section.id" 
               @click="selectSection(section.id)"
               style="padding: 12px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s; display: flex; align-items: center; gap: 10px; background: transparent; color: #333;">
            <img v-if="section.icon" :src="section.icon" style="width: 24px; height: 24px; border-radius: 4px;" />
            <el-icon v-else><Star /></el-icon>
            <span>{{ section.title }}</span>
            <span style="margin-left: auto; font-size: 12px; color: #999;">{{ section.postCount }}帖</span>
          </div>
        </div>
      </div>

      <div style="flex: 1; min-width: 0;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <div style="font-size: 20px; font-weight: bold; color: #333;">
              {{ data.sectionTitle }}
            </div>
            <el-button type="primary" @click="handleAddPost" v-if="data.user && data.user.token">
              <el-icon><Plus /></el-icon>
              发布帖子
            </el-button>
          </div>

          <div style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center;">
            <el-input
              v-model="data.searchTitle"
              placeholder="搜索帖子标题"
              style="width: 300px;"
              @keyup.enter="loadPosts"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button @click="loadPosts">搜索</el-button>
            <el-select v-model="data.orderBy" placeholder="排序方式" style="width: 150px;" @change="loadPosts">
              <el-option label="最新发布" value="time" />
              <el-option label="浏览最多" value="views" />
              <el-option label="点赞最多" value="likeCount" />
              <el-option label="评论最多" value="replyCount" />
            </el-select>
          </div>

          <div v-if="data.posts.length === 0" style="text-align: center; padding: 60px; color: #999;">
            <el-icon :size="80" color="#ddd"><ChatDotRound /></el-icon>
            <div style="font-size: 18px; margin-top: 20px;">
              暂无帖子
            </div>
            <div style="font-size: 14px; margin-top: 10px;">
              快来发布第一个帖子吧
            </div>
          </div>

          <div v-for="post in data.posts" :key="post.id" 
               style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 20px; transition: all 0.3s; cursor: pointer;"
               @click="viewPostDetail(post)">
            <div style="display: flex; gap: 15px;">
              <div style="flex-shrink: 0; display: flex; align-items: center; justify-content: center; width: 60px; height: 60px; border-radius: 8px; background: #f0f0f0; font-weight: bold; color: #666; font-size: 24px;">
                {{ post.title ? post.title.charAt(0) : '帖' }}
              </div>
              <div style="flex: 1; min-width: 0;">
                <div style="font-size: 18px; font-weight: bold; margin-bottom: 10px; color: #333;">
                  {{ post.title }}
                </div>
                <div style="font-size: 14px; color: #666; margin-bottom: 12px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden;">
                  {{ stripHtml(post.content) }}
                </div>
                <div style="display: flex; align-items: center; gap: 15px; font-size: 13px; color: #999;">
                  <span style="display: flex; align-items: center; gap: 5px;">
                    <img v-if="post.userAvatar" :src="post.userAvatar" style="width: 20px; height: 20px; border-radius: 50%;" />
                    <img v-else src="@/assets/images/Manager.png" style="width: 20px; height: 20px; border-radius: 50%;" />
                    {{ post.userName }}
                  </span>
                  <el-tag size="small" type="success">{{ post.sectionTitle }}</el-tag>
                  <span>{{ post.time }}</span>
                </div>
              </div>
              <div style="flex-shrink: 0; display: flex; flex-direction: column; gap: 8px; min-width: 80px; align-items: flex-end;">
                <span style="display: flex; align-items: center; gap: 5px; font-size: 13px; color: #666;">
                  <el-icon><View /></el-icon>
                  {{ post.views }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px; font-size: 13px; color: #666;">
                  <el-icon><Star /></el-icon>
                  {{ post.likeCount }}
                </span>
                <span style="display: flex; align-items: center; gap: 5px; font-size: 13px; color: #666;">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ post.replyCount }}
                </span>
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

    <el-dialog :title="data.postForm.id ? '编辑帖子' : '发布帖子'" v-model="data.postFormVisible" width="70%" destroy-on-close>
      <el-form ref="postFormRef" :model="data.postForm" :rules="data.postRules" label-width="100px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="帖子标题">
          <el-input v-model="data.postForm.title" autocomplete="off" placeholder="请输入帖子标题"/>
        </el-form-item>
        <el-form-item prop="sectionId" label="选择板块">
          <el-select
              v-model="data.postForm.sectionId"
              placeholder="请选择板块"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.sections"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="帖子内容">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 400px; overflow-y: hidden;"
                v-model="data.postForm.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.postFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="savePost">发 布</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, ref, computed, onMounted, onBeforeUnmount, shallowRef } from "vue";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { ArrowLeft } from '@element-plus/icons-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  sections: [],
  hotSections: [],
  posts: [],
  selectedSectionId: null,
  sectionTitle: '全部帖子',
  searchTitle: '',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  orderBy: 'time',
  postFormVisible: false,
  postForm: {},
  postRules: {
    title: [
      { required: true, message: '请填写帖子标题', trigger: 'blur' }
    ],
    sectionId: [
      { required: true, message: '请选择板块', trigger: 'change' }
    ],
    content: [
      { required: true, message: '请填写帖子内容', trigger: 'blur' }
    ]
  }
});

const postFormRef = ref();

const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file'
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

onMounted(() => {
  loadSections()
  loadPosts()
})

const loadSections = () => {
  // 调用真实的论坛板块接口
  request.get('/forumSection/selectAll', { params: { status: 1 } }).then(res => {
    if (res.code === '200') {
      data.sections = res.data
      data.hotSections = [...res.data].sort((a, b) => b.postCount - a.postCount).slice(0, 5)
    } else {
      console.log('加载论坛板块失败:', res.msg)
      data.sections = []
      data.hotSections = []
    }
  }).catch(err => {
    console.log('加载论坛板块失败，使用默认板块:', err.message)
    // 使用默认板块数据
    data.sections = []
    data.hotSections = []
  })
}

const loadPosts = () => {
  // 调用真实的帖子接口获取帖子数据
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    status: 1,
    orderBy: data.orderBy,
    orderDirection: 'desc'
  }
  if (data.selectedSectionId !== null) {
    params.sectionId = data.selectedSectionId
  }
  if (data.searchTitle) {
    params.title = data.searchTitle
  }
  request.get('/post/selectAll', { params }).then(res => {
    if (res.code === '200') {
      data.posts = res.data
      data.total = res.data.length
    } else {
      console.log('加载帖子失败:', res.msg)
      data.posts = []
      data.total = 0
    }
  }).catch(err => {
    console.log('加载帖子失败:', err.message)
    data.posts = []
    data.total = 0
  })
}

const selectSection = (sectionId) => {
  data.selectedSectionId = sectionId
  data.pageNum = 1
  if (sectionId === null) {
    data.sectionTitle = '全部帖子'
  } else {
    const section = data.sections.find(s => s.id === sectionId)
    data.sectionTitle = section ? section.title : '帖子列表'
  }
  loadPosts()
}

const handleAddPost = () => {
  data.postForm = {}
  data.postFormVisible = true
}

const savePost = () => {
  postFormRef.value.validate((valid) => {
    if (!valid) return;
    const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}')
    data.postForm.userId = userFromStorage.id
    
    const url = data.postForm.id ? '/post/update' : '/post/add'
    const method = data.postForm.id ? request.put : request.post
    
    method(url, data.postForm).then(res => {
      if (res.code === '200' || res.code === 200) {
        ElMessage.success(data.postForm.id ? '修改成功' : '发布成功，等待审核')
        data.postFormVisible = false
        loadPosts()
        loadSections()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

const viewPostDetail = (post) => {
  location.href = '/front/postDetail?id=' + post.id
}

const handleSizeChange = (size) => {
  data.pageSize = size
  data.pageNum = 1
  loadPosts()
}

const handleCurrentChange = (page) => {
  data.pageNum = page
  loadPosts()
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
