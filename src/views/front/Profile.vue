﻿﻿<template>
  <div style="background-color: #f6f9fc; min-height: 100vh; padding-bottom: 20px;">
    <div style="height: 70px; align-items: center; background: #f4f0ee; display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 50px; height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #2c82ff; margin-left: 5px">
          个人中心
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
      </div>
      <div v-if="isLoggedIn">
        <el-dropdown style="padding-right: 20px">
          <div style="width: fit-content; display: flex; align-items: center;">
            <img v-if="data.user.avatar" style="width: 50px; height: 50px; border-radius: 50%;" :src="data.user.avatar" />
            <img v-else style="width: 50px; height: 50px; border-radius: 50%;" src="@/assets/images/Manager.png" />
            <span style="padding-left: 5px">{{ data.user.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="navTo('/front/home')">返回首页</el-dropdown-item>
              <el-dropdown-item @click="logout" style="color: #f56c6c;">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div v-if="!isLoggedIn" style="max-width: 1200px; margin: 20px auto; background: white; border-radius: 8px; padding: 60px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); text-align: center;">
      <el-icon :size="80" color="#ddd"><el-icon><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path fill="currentColor" d="M12 2a10 10 0 1 0 10 10A10.011 10.011 0 0 0 12 2zm0 18a8 8 0 1 1 8-8a8.009 8.009 0 0 1-8 8zm4.875-13a1.125 1.125 0 1 1-2.25 0a1.125 1.125 0 0 1 2.25 0zm-4.633 7.54c-.408-.408-.937-.675-1.5-.675s-1.092.267-1.5.675c-.406.406-.633.935-.633 1.5s.227 1.094.633 1.5s.934.633 1.5.633s1.092-.227 1.5-.633s.633-.934.633-1.5s-.227-1.092-.633-1.5z"></path></svg></el-icon></el-icon>
      <div style="font-size: 18px; margin-top: 20px; color: #666;">
        请先登录
      </div>
      <el-button type="primary" @click="navTo('/login')" style="margin-top: 20px;">
        前往登录
      </el-button>
    </div>

    <div v-else style="max-width: 1200px; margin: 20px auto; display: flex; gap: 20px;">
      <div style="width: 250px; flex-shrink: 0;">
        <div style="background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
            个人中心
          </div>
          <div @click="data.activeTab = 'profile'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'profile' ? '#2fbd67' : 'transparent', color: data.activeTab === 'profile' ? 'white' : '#333' }">
            个人资料
          </div>
          <div @click="data.activeTab = 'favorites'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'favorites' ? '#2fbd67' : 'transparent', color: data.activeTab === 'favorites' ? 'white' : '#333' }">
            我的收藏
          </div>
          <div @click="data.activeTab = 'articles'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'articles' ? '#2fbd67' : 'transparent', color: data.activeTab === 'articles' ? 'white' : '#333' }">
            我的文章
          </div>
          <div @click="data.activeTab = 'posts'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'posts' ? '#2fbd67' : 'transparent', color: data.activeTab === 'posts' ? 'white' : '#333' }">
            我的帖子
          </div>
          <div @click="data.activeTab = 'replies'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'replies' ? '#2fbd67' : 'transparent', color: data.activeTab === 'replies' ? 'white' : '#333' }">
            我的回复
          </div>
          <div @click="data.activeTab = 'likes'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'likes' ? '#2fbd67' : 'transparent', color: data.activeTab === 'likes' ? 'white' : '#333' }">
            我的点赞
          </div>
          <div @click="data.activeTab = 'password'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'password' ? '#2fbd67' : 'transparent', color: data.activeTab === 'password' ? 'white' : '#333' }">
            修改密码
          </div>
          <div @click="data.activeTab = 'history'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'history' ? '#2fbd67' : 'transparent', color: data.activeTab === 'history' ? 'white' : '#333' }">
            我的历史
          </div>
          <div @click="data.activeTab = 'feedback'" 
               style="padding: 15px; cursor: pointer; border-radius: 4px; margin-bottom: 8px; transition: all 0.3s;"
               :style="{ background: data.activeTab === 'feedback' ? '#2fbd67' : 'transparent', color: data.activeTab === 'feedback' ? 'white' : '#333' }">
            意见反馈
          </div>
        </div>
      </div>

      <div style="flex: 1; min-width: 0;">
        <div style="background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
          <div v-if="data.activeTab === 'profile'" style="max-width: 500px; margin: 0 auto;">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
              个人资料
            </div>
            <el-form
              ref="formRef"
              :rules="data.rules"
              :model="data.user"
              label-width="100px"
              class="profile-form"
            >
              <el-form-item prop="username" label="账号">
                <el-input v-model="data.user.username" disabled autocomplete="off" placeholder="账号（注册时设置，不可修改）"/>
              </el-form-item>
              <el-form-item prop="name" label="名称">
                <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入名称"/>
              </el-form-item>
              <el-form-item prop="phone" label="电话">
                <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话"/>
              </el-form-item>
              <el-form-item prop="email" label="邮箱">
                <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱"/>
              </el-form-item>
              <el-form-item prop="gender" label="性别">
                <el-select v-model="data.user.gender" placeholder="请选择性别">
                  <el-option label="男" value="male"></el-option>
                  <el-option label="女" value="female"></el-option>
                  <el-option label="保密" value="secret"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="当前头像">
                <el-image
                  :src="data.user.avatar || 'https://cube.elemecdn.com/0/88/88c379bbbd9d7b44b42b60e6111c/element-default-avatar.png'"
                  :preview-src-list="data.user.avatar ? [data.user.avatar] : []"
                  :preview-teleported="true"
                  style="width: 80px; height: 80px; border-radius: 50%; display: block"
                />
              </el-form-item>
              <el-form-item prop="avatar" label="头像">
                <el-upload
                    action="http://localhost:1306/files/upload"
                    :headers="{ token: data.user.token }"
                    :on-success="handleFileSuccess"
                    :on-error="handleFileError"
                    list-type="picture"
                >
                  <el-button type="primary">更改头像</el-button>
                </el-upload>
              </el-form-item>
            </el-form>
            <div class="btn-wrap">
              <el-button type="primary" size="large" @click="update">保存</el-button>
            </div>
          </div>

          <div v-if="data.activeTab === 'favorites'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的收藏</span>
              <el-select v-model="data.favoriteType" @change="loadFavorites" style="width: 180px;">
                <el-option label="全部收藏" value="all"></el-option>
                <el-option label="游戏攻略" value="article"></el-option>
                <el-option label="论坛帖子" value="post"></el-option>
              </el-select>
            </div>
            
            <div v-if="data.favorites.length === 0" style="text-align: center; padding: 40px; color: #999;">
              <el-icon :size="60" color="#ddd"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path fill="currentColor" d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"></path></svg></el-icon>
              <div style="font-size: 16px; margin-top: 15px;">
                暂无收藏
              </div>
            </div>
            
            <div v-for="item in data.favorites" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex-shrink: 0;">
                  <el-image
                    :src="item.introductionImg"
                    :preview-src-list="[item.introductionImg]"
                    fit="cover"
                    style="width: 120px; height: 90px; border-radius: 6px; cursor: pointer"
                  />
                </div>
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px; cursor: pointer;" @click="viewArticle(item)">
                    {{ item.introductionTitle }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden;">
                    <div v-html="item.introductionDescription"></div>
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999;">
                    <el-tag size="small" :type="item.type === 'post' || item.postId ? 'info' : 'success'">
                      {{ item.type === 'post' || item.postId ? '论坛帖子' : '游戏攻略' }}
                    </el-tag>
                    <span>收藏时间：{{ item.time }}</span>
                  </div>
                </div>
                <div style="flex-shrink: 0;">
                  <el-button type="danger" size="small" @click="handleCancelCollect(item.id)">取消收藏</el-button>
                </div>
              </div>
            </div>
            
            <div v-if="data.favoriteTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.favoritePageNum"
                v-model:page-size="data.favoritePageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.favoriteTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleFavoriteSizeChange"
                @current-change="handleFavoriteCurrentChange"
              />
            </div>
          </div>

          <div v-if="data.activeTab === 'articles'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的文章</span>
              <el-button type="primary" @click="handleAddArticle">新增文章</el-button>
            </div>
            
            <div v-if="data.articles.length === 0" style="text-align: center; padding: 40px; color: #999;">
              暂无文章，点击上方按钮添加
            </div>
            
            <div v-for="item in data.articles" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex-shrink: 0;">
                  <el-image
                    :src="item.img"
                    :preview-src-list="[item.img]"
                    fit="cover"
                    style="width: 120px; height: 90px; border-radius: 6px; cursor: pointer"
                  />
                </div>
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px; cursor: pointer;" @click="viewArticle(item)">
                    {{ item.title }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden;">
                    {{ item.description }}
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999; margin-bottom: 8px;">
                    <span>分类：{{ item.categoryTitle || '未分类' }}</span>
                    <span>发布时间：{{ item.time }}</span>
                  </div>
                  <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
                    <el-tag size="small" :type="item.status === 1 ? 'success' : item.status === 2 ? 'danger' : 'warning'">
                      {{ item.status === 1 ? '已通过' : item.status === 2 ? '已拒绝' : '待审核' }}
                    </el-tag>
                  </div>
                  <div v-if="item.status === 2 && item.rejectReason" style="font-size: 13px; color: #f56c6c; padding: 10px; background: #fef0f0; border-radius: 4px; border-left: 3px solid #f56c6c;">
                    <div style="font-weight: bold; margin-bottom: 5px;">拒绝原因：</div>
                    <div>{{ item.rejectReason }}</div>
                  </div>
                </div>
                <div style="flex-shrink: 0; display: flex; flex-direction: column; gap: 8px;">
                  <el-button type="primary" size="small" @click="handleEditArticle(item)">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDeleteArticle(item.id)">删除</el-button>
                </div>
              </div>
            </div>
            
            <div v-if="data.articleTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.articlePageNum"
                v-model:page-size="data.articlePageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.articleTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleArticleSizeChange"
                @current-change="handleArticleCurrentChange"
              />
            </div>
          </div>

          <div v-if="data.activeTab === 'posts'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的帖子</span>
              <el-button type="primary" @click="handleAddPost">新增帖子</el-button>
            </div>
            
            <div v-if="data.myPosts.length === 0" style="text-align: center; padding: 40px; color: #999;">
              暂无帖子，点击上方按钮添加
            </div>
            
            <div v-for="item in data.myPosts" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px; cursor: pointer;" @click="viewPost(item)">
                    {{ item.title }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden;">
                    {{ item.content }}
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999;">
                    <el-tag size="small" :type="item.status === 1 ? 'success' : item.status === 2 ? 'danger' : 'warning'">
                      {{ item.status === 1 ? '已通过' : item.status === 2 ? '已拒绝' : '待审核' }}
                    </el-tag>
                    <span>板块：{{ item.sectionTitle || '未分类' }}</span>
                    <span>发布时间：{{ item.time }}</span>
                  </div>
                </div>
                <div style="flex-shrink: 0; display: flex; flex-direction: column; gap: 8px;">
                  <el-button type="primary" size="small" @click="handleEditPost(item)">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDeletePost(item.id)">删除</el-button>
                </div>
              </div>
            </div>
            
            <div v-if="data.myPostTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.myPostPageNum"
                v-model:page-size="data.myPostPageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.myPostTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleMyPostSizeChange"
                @current-change="handleMyPostCurrentChange"
              />
            </div>
          </div>

          <div v-if="data.activeTab === 'replies'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的回复</span>
              <el-select v-model="data.replyType" @change="loadReplies" style="width: 180px;">
                <el-option label="全部回复" value="all"></el-option>
                <el-option label="游戏攻略" value="article"></el-option>
                <el-option label="论坛帖子" value="post"></el-option>
              </el-select>
            </div>
            
            <div v-if="data.replies.length === 0" style="text-align: center; padding: 40px; color: #999;">
              <el-icon :size="60" color="#ddd"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path fill="currentColor" d="M21 6h-2v9H6v2c0 .55.45 1 1 1h11l4 4V7c0-.55-.45-1-1-1zm-4 6V3c0-.55-.45-1-1-1H3c-.55 0-1 .45-1 1v14l4-4h10c.55 0 1-.45 1-1z"></path></svg></el-icon>
              <div style="font-size: 16px; margin-top: 15px;">
                暂无回复
              </div>
            </div>
            
            <div v-for="item in data.replies" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 15px; color: #333; line-height: 1.6; margin-bottom: 10px; word-break: break-all;">
                    {{ item.content }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 10px; padding: 10px; background: #f9f9f9; border-radius: 6px; cursor: pointer;" @click="viewArticle(item)">
                    <span style="font-weight: 500; color: #2c82ff;">{{ item.introductionTitle }}</span>
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999;">
                    <span>回复时间：{{ item.time }}</span>
                  </div>
                </div>
                <div style="flex-shrink: 0;">
                  <el-button type="danger" size="small" @click="handleDeleteReply(item.id)">删除</el-button>
                </div>
              </div>
            </div>
            
            <div v-if="data.replyTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.replyPageNum"
                v-model:page-size="data.replyPageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.replyTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleReplySizeChange"
                @current-change="handleReplyCurrentChange"
              />
            </div>
          </div>

          <div v-if="data.activeTab === 'likes'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的点赞</span>
              <el-select v-model="data.likeType" @change="loadLikes" style="width: 180px;">
                <el-option label="全部点赞" value="all"></el-option>
                <el-option label="游戏攻略" value="article"></el-option>
                <el-option label="论坛帖子" value="post"></el-option>
              </el-select>
            </div>
            
            <div v-if="data.likes.length === 0" style="text-align: center; padding: 40px; color: #999;">
              <el-icon :size="60" color="#ddd"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path fill="currentColor" d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"></path></svg></el-icon>
              <div style="font-size: 16px; margin-top: 15px;">
                暂无点赞
              </div>
            </div>
            
            <div v-for="item in data.likes" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex-shrink: 0;">
                  <el-image
                    :src="item.introductionImg"
                    :preview-src-list="[item.introductionImg]"
                    fit="cover"
                    style="width: 120px; height: 90px; border-radius: 6px; cursor: pointer"
                  />
                </div>
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px; cursor: pointer;" @click="viewArticle(item)">
                    {{ item.introductionTitle }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden;">
                    <div v-html="item.introductionDescription"></div>
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999;">
                    <el-tag size="small" :type="item.type === 'post' || item.postId ? 'info' : 'success'">
                      {{ item.type === 'post' || item.postId ? '论坛帖子' : '游戏攻略' }}
                    </el-tag>
                    <span>点赞时间：{{ item.time }}</span>
                  </div>
                </div>
                <div style="flex-shrink: 0;">
                  <el-button type="danger" size="small" @click="handleCancelLike(item.id)">取消点赞</el-button>
                </div>
              </div>
            </div>
            
            <div v-if="data.likeTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.likePageNum"
                v-model:page-size="data.likePageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.likeTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleLikeSizeChange"
                @current-change="handleLikeCurrentChange"
              />
            </div>
          </div>

          <div v-if="data.activeTab === 'password'" style="max-width: 500px; margin: 0 auto;">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
              修改密码
            </div>
            <el-form
              ref="passwordFormRef"
              :rules="data.passwordRules"
              :model="data.user"
              label-width="100px"
              class="password-form"
            >
              <el-form-item prop="password" label="原密码">
                <el-input
                    size="large"
                    show-password
                    v-model="data.user.password"
                    autocomplete="off"
                    prefix-icon="Lock"
                    placeholder="请输入原密码"
                />
              </el-form-item>
              <el-form-item prop="newpassword" label="新密码">
                <el-input
                    size="large"
                    show-password
                    v-model="data.user.newpassword"
                    autocomplete="off"
                    prefix-icon="Lock"
                    placeholder="请输入新密码"
                />
              </el-form-item>
              <el-form-item prop="newpassword2" label="确认新密码">
                <el-input
                    size="large"
                    show-password
                    v-model="data.user.newpassword2"
                    autocomplete="off"
                    prefix-icon="Lock"
                    placeholder="请再次输入新密码"
                />
              </el-form-item>
            </el-form>
            <div class="btn-wrap">
              <el-button type="primary" size="large" @click="updatepassword">保存</el-button>
            </div>
          </div>

          <div v-if="data.activeTab === 'feedback'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px;">
              意见反馈
            </div>
            <div style="max-width: 600px; margin: 0 auto;">
              <el-form ref="feedbackFormRef" :model="data.feedbackForm" :rules="data.feedbackRules" label-width="100px" class="feedback-form">
                <el-form-item prop="content" label="反馈内容">
                  <el-input
                    v-model="data.feedbackForm.content"
                    type="textarea"
                    :rows="6"
                    placeholder="请输入您的宝贵意见和建议"
                    resize="vertical"
                  />
                </el-form-item>
              </el-form>
              <div class="btn-wrap">
                <el-button type="primary" size="large" @click="submitFeedback">提交反馈</el-button>
              </div>
            </div>
          </div>

          <div v-if="data.activeTab === 'history'">
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 20px; border-bottom: 2px solid #2fbd67; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
              <span>我的观看历史</span>
              <el-select v-model="data.historyType" @change="handleHistoryTypeChange" style="width: 180px;">
                <el-option label="全部历史" value="all"></el-option>
                <el-option label="游戏攻略" value="article"></el-option>
                <el-option label="论坛帖子" value="post"></el-option>
              </el-select>
            </div>
            
            <div v-if="data.history.length === 0" style="text-align: center; padding: 40px; color: #999;">
              <el-icon :size="60" color="#ddd"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"></path></svg></el-icon>
              <div style="font-size: 16px; margin-top: 15px;">
                暂无观看历史
              </div>
            </div>
            
            <div v-for="item in data.history" :key="item.id" style="margin-bottom: 20px; border: 1px solid #eee; border-radius: 8px; padding: 15px; transition: all 0.3s;">
              <div style="display: flex; gap: 15px;">
                <div style="flex-shrink: 0;">
                  <el-image
                    :src="item.img"
                    :preview-src-list="[item.img]"
                    fit="cover"
                    style="width: 120px; height: 90px; border-radius: 6px; cursor: pointer"
                  />
                </div>
                <div style="flex: 1; min-width: 0;">
                  <div style="font-size: 16px; font-weight: bold; margin-bottom: 8px; cursor: pointer;" @click="viewArticle(item)">
                    {{ item.title }}
                  </div>
                  <div style="font-size: 13px; color: #666; margin-bottom: 8px; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; line-clamp: 2; overflow: hidden;">
                    {{ item.description }}
                  </div>
                  <div style="display: flex; align-items: center; gap: 15px; font-size: 12px; color: #999;">
                    <el-tag size="small" :type="item.type === 'post' ? 'info' : 'success'">
                      {{ item.type === 'post' ? '论坛帖子' : '游戏攻略' }}
                    </el-tag>
                    <span>观看时间：{{ item.time }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div v-if="data.historyTotal > 0" style="display: flex; justify-content: center; align-items: center; padding: 20px 0;">
              <el-pagination
                v-model:current-page="data.historyPageNum"
                v-model:page-size="data.historyPageSize"
                :page-sizes="[5, 10, 20]"
                :total="data.historyTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleHistorySizeChange"
                @current-change="handleHistoryCurrentChange"
              />
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
                v-for="item in data.categories"
                :key="item.id"
                :label="item.title"
                :value="item.id"
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
        <el-form-item label="视频上传">
          <el-upload
              action="http://localhost:1306/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleArticleVideoSuccess"
              :accept="'video/mp4,video/avi,video/mov,video/wmv,video/flv,video/mkv'"
              list-type="text"
          >
            <el-button type="warning">上传视频</el-button>
          </el-upload>
          <el-button v-if="data.articleForm.video" type="text" @click="previewArticleVideo">预览视频</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.articleFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveArticle">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="data.postForm.id ? '编辑帖子' : '新增帖子'" v-model="data.postFormVisible" width="70%" destroy-on-close>
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
                :editor="postEditorRef"
                :mode="postMode"
            />
            <Editor
                style="height: 400px; overflow-y: hidden;"
                v-model="data.postForm.content"
                :mode="postMode"
                :default-config="postEditorConfig"
                @onCreated="postHandleCreated"
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
import { reactive, ref, computed, onMounted, onBeforeUnmount, shallowRef, watch } from "vue";
import request from "@/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import * as ElIcons from "@element-plus/icons-vue";
import iconList from "@/utils/iconList";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const data = reactive({
  user: {
    username: null,
    name: null,
    phone: null,
    email: null,
    gender: null,
    avatar: null,
    role: null,
    password: '',
    newpassword: '',
    newpassword2: ''
  },
  activeTab: 'profile',
  rules: {
    username: [],
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入电话', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' }
    ],
    gender: [],
  },
  passwordRules: {
    password: [
      { required: true, message: '请填旧密码', trigger: 'blur' }
    ],
    newpassword: [
      { required: true, message: '请填写新密码', trigger: 'blur' }
    ],
    newpassword2: [
      { required: true, message: '请再次输入新密码', trigger: 'blur' }
    ],
  },
  articles: [],
  articlePageNum: 1,
  articlePageSize: 5,
  articleTotal: 0,
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
    ]
  },
  categories: [],
  myPosts: [],
  myPostPageNum: 1,
  myPostPageSize: 5,
  myPostTotal: 0,
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
  },
  sections: [],
  favorites: [],
  favoritePageNum: 1,
  favoritePageSize: 5,
  favoriteTotal: 0,
  favoriteType: 'all',
  replies: [],
  replyPageNum: 1,
  replyPageSize: 5,
  replyTotal: 0,
  replyType: 'all',
  likes: [],
  likePageNum: 1,
  likePageSize: 5,
  likeTotal: 0,
  likeType: 'all',
  feedbackForm: {
    content: ''
  },
  feedbackRules: {
    content: [
      { required: true, message: '请输入反馈内容', trigger: 'blur' },
      { min: 10, message: '反馈内容不能少于10个字符', trigger: 'blur' }
    ]
  },
  history: [],
  historyPageNum: 1,
  historyPageSize: 5,
  historyTotal: 0,
  historyType: 'all'
});

const articleFormRef = ref();
const postFormRef = ref();
const feedbackFormRef = ref();

const isLoggedIn = computed(() => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  return !!(userFromStorage && userFromStorage.token && userFromStorage.token.length > 0);
});

const loadUserInfo = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  console.log('加载用户信息:', userFromStorage);
  data.user.id = userFromStorage.id || null;
  data.user.username = userFromStorage.username || null;
  data.user.name = userFromStorage.name || null;
  data.user.phone = userFromStorage.phone || null;
  data.user.email = userFromStorage.email || null;
  data.user.gender = userFromStorage.gender || null;
  data.user.avatar = userFromStorage.avatar || null;
  data.user.token = userFromStorage.token || null;
  data.user.role = userFromStorage.role || null;
};

const formRef = ref();
const passwordFormRef = ref();

const articleEditorRef = shallowRef();
const articleMode = 'default';
const articleEditorConfig = {
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
  MENU_CONF: {}
};
articleEditorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file',
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
};
articleEditorConfig.MENU_CONF['uploadVideo'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file',
  maxFileSize: 5 * 1024 * 1024 * 1024,
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
};

const postEditorRef = shallowRef();
const postMode = 'default';
const postEditorConfig = {
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
  MENU_CONF: {}
};
postEditorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file',
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
};
postEditorConfig.MENU_CONF['uploadVideo'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file',
  maxFileSize: 5 * 1024 * 1024 * 1024,
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
};

onBeforeUnmount(() => {
  const articleEditor = articleEditorRef.value;
  if (articleEditor == null) return;
  articleEditor.destroy();
  const postEditor = postEditorRef.value;
  if (postEditor == null) return;
  postEditor.destroy();
});

const articleHandleCreated = (editor) => {
  articleEditorRef.value = editor;
};

const postHandleCreated = (editor) => {
  postEditorRef.value = editor;
};

onMounted(() => {
  loadUserInfo();
  loadCategories();
  loadSections();
  if (data.activeTab === 'articles') {
    loadArticles();
  } else if (data.activeTab === 'posts') {
    loadMyPosts();
  }
});

watch(() => data.activeTab, (newVal) => {
  if (newVal === 'articles') {
    loadArticles();
  } else if (newVal === 'favorites') {
    loadFavorites();
  } else if (newVal === 'posts') {
    loadMyPosts();
  } else if (newVal === 'replies') {
    loadReplies();
  } else if (newVal === 'likes') {
    loadLikes();
  } else if (newVal === 'history') {
    loadHistory();
  }
});

const handleFileSuccess = (res) => {
  if (res.code === '200' || res.code === 200) {
    data.user.avatar = res.data;
    ElMessage.success('头像上传成功，请点击保存按钮保存');
  } else {
    ElMessage.error('头像上传失败：' + res.msg);
  }
};

const handleFileError = (err) => {
  ElMessage.error('头像上传失败：' + (err.message || '未知错误'));
  console.error('头像上传错误：', err);
};

const update = () => {
  formRef.value.validate((valid) => {
    if (!valid) return;
    const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
    console.log('用户存储信息:', userFromStorage);
    console.log('用户存储信息中是否有password字段:', 'password' in userFromStorage);
    console.log('用户存储信息中password字段值:', userFromStorage.password);
    const userData = {
      id: userFromStorage.id,
      name: data.user.name,
      phone: data.user.phone,
      email: data.user.email,
      gender: data.user.gender,
      avatar: data.user.avatar
    };
    console.log('发送的数据:', userData);
    console.log('请求头token:', userFromStorage.token);
    console.log('当前用户角色:', userFromStorage.role);
    console.log('localStorage中的完整用户信息:', localStorage.getItem('code_user'));
    // 手动添加token到请求头
    const config = {
      headers: {
        'token': userFromStorage.token
      }
    };
    console.log('发送请求:', '/user/update', userData, config);
    // 直接使用axios发送请求，绕过request拦截器
    import('axios').then(axios => {
      console.log('axios加载成功');
      axios.default.put('http://localhost:1306/user/update', userData, config).then(res => {
        console.log('响应数据:', res);
        if (res.data.code === '200') {
          ElMessage.success('更新成功');
          userFromStorage.name = data.user.name;
          userFromStorage.phone = data.user.phone;
          userFromStorage.email = data.user.email;
          userFromStorage.gender = data.user.gender;
          userFromStorage.avatar = data.user.avatar;
          localStorage.setItem("code_user", JSON.stringify(userFromStorage));
        } else if (res.data.code === '401') {
          console.log('权限错误:', res.data.msg);
          ElMessage.error('权限错误：' + res.data.msg);
          // 尝试重新登录
          ElMessageBox.confirm('登录状态失效，是否重新登录？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            localStorage.removeItem('code_user');
            window.location.href = '/login';
          }).catch(() => {});
        } else {
          ElMessage.error(res.data.msg || '更新失败');
        }
      }).catch(err => {
        console.log('错误详情:', err);
        console.log('错误响应:', err.response);
        if (err.response && err.response.data && err.response.data.msg) {
          console.log('错误消息:', err.response.data.msg);
          ElMessage.error('更新失败：' + err.response.data.msg);
        } else {
          ElMessage.error('更新失败：' + (err.message || '网络异常'));
        }
        console.error('更新用户信息错误：', err);
      });
    }).catch(err => {
      console.log('axios加载失败:', err);
      ElMessage.error('更新失败：axios加载失败');
    });
  });
};

const updatepassword = () => {
  passwordFormRef.value.validate((valid) => {
    if (!valid) return;
    const newPwd = data.user.newpassword;
    const confirmPwd = data.user.newpassword2;
    if (newPwd !== confirmPwd) {
      ElMessage.error("两次输入的新密码不一致！");
      return;
    }
    request.post('/updatepassword', data.user)
      .then(res => {
        if (res.code === '200' || res.code === 200) {
          ElMessage.success('修改成功，即将返回登录页');
          setTimeout(() => {
            localStorage.removeItem('code_user');
            location.href = '/login';
          }, 1600);
        } else {
          ElMessage.error(res.msg || '修改失败');
        }
      })
      .catch(err => {
        ElMessage.error('请求失败：' + (err.message || '网络异常'));
        console.error('修改密码错误：', err);
      });
  });
};

const logout = () => {
  localStorage.removeItem('code_user');
  location.href = '/login';
};

const navTo = (url) => {
  location.href = url;
};

const loadArticles = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  request.get('/introduction/selectPage', {
    params: {
      pageNum: data.articlePageNum,
      pageSize: data.articlePageSize,
      userId: userFromStorage.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.articles = res.data.list;
      data.articleTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadCategories = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categories = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadSections = () => {
  request.get('/forumSection/selectAll', { params: { status: 1 } }).then(res => {
    if (res.code === '200') {
      data.sections = res.data;
    }
  });
};

const loadMyPosts = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  request.get('/post/selectPage', {
    params: {
      pageNum: data.myPostPageNum,
      pageSize: data.myPostPageSize,
      userId: userFromStorage.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.myPosts = res.data.list;
      data.myPostTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleAddArticle = () => {
  data.articleForm = {};
  data.articleFormVisible = true;
};

const handleEditArticle = (row) => {
  data.articleForm = JSON.parse(JSON.stringify(row));
  data.articleFormVisible = true;
};

const handleDeleteArticle = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/introduction/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        loadArticles();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
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
        loadArticles();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};

const viewArticle = (item) => {
  if (item.type === 'post' || item.postId) {
    const postId = item.contentId || item.postId || item.id || item.introductionId;
    location.href = '/front/postDetail?id=' + postId;
  } else {
    const articleId = item.introductionId || item.contentId || item.id;
    location.href = '/front/introductionDetail?id=' + articleId;
  }
};

const viewPost = (item) => {
  location.href = '/front/postDetail?id=' + item.id;
};

const handleArticleSizeChange = (size) => {
  data.articlePageSize = size;
  data.articlePageNum = 1;
  loadArticles();
};

const handleArticleCurrentChange = (page) => {
  data.articlePageNum = page;
  loadArticles();
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
    if (data.articleForm.content) {
      data.articleForm.content += `<video src="${res.data}" controls style="max-width: 100%; max-height: 500px; margin: 20px 0;"></video>`;
    } else {
      data.articleForm.content = `<video src="${res.data}" controls style="max-width: 100%; max-height: 500px; margin: 20px 0;"></video>`;
    }
    ElMessage.success('视频上传成功');
  } else {
    ElMessage.error('视频上传失败：' + res.msg);
  }
};

const previewArticleVideo = () => {
  if (data.articleForm.video) {
    window.open(data.articleForm.video, '_blank');
  }
};

const handleAddPost = () => {
  data.postForm = {};
  data.postFormVisible = true;
};

const handleEditPost = (row) => {
  data.postForm = JSON.parse(JSON.stringify(row));
  data.postFormVisible = true;
};

const handleDeletePost = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/post/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        loadMyPosts();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const savePost = () => {
  postFormRef.value.validate((valid) => {
    if (!valid) return;
    const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
    data.postForm.userId = userFromStorage.id;
    
    const url = data.postForm.id ? '/post/update' : '/post/add';
    const method = data.postForm.id ? request.put : request.post;
    
    method(url, data.postForm).then(res => {
      if (res.code === '200' || res.code === 200) {
        ElMessage.success(data.postForm.id ? '修改成功' : '发布成功，等待审核');
        data.postFormVisible = false;
        loadMyPosts();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};

const handleMyPostSizeChange = (size) => {
  data.myPostPageSize = size;
  data.myPostPageNum = 1;
  loadMyPosts();
};

const handleMyPostCurrentChange = (page) => {
  data.myPostPageNum = page;
  loadMyPosts();
};

const loadFavorites = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  const params = {
    pageNum: data.favoritePageNum,
    pageSize: data.favoritePageSize,
    userId: userFromStorage.id,
    type: data.favoriteType === 'all' ? '' : data.favoriteType
  };
  
  if (data.favoriteType === 'post') {
    params.type = 'post';
  }
  
  request.get('/collect/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.favorites = res.data.list;
      data.favoriteTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleFavoriteSizeChange = (size) => {
  data.favoritePageSize = size;
  data.favoritePageNum = 1;
  loadFavorites();
};

const handleFavoriteCurrentChange = (page) => {
  data.favoritePageNum = page;
  loadFavorites();
};

const handleCancelCollect = (id) => {
  ElMessageBox.confirm('确定要取消收藏吗？', '取消收藏', { type: 'warning' }).then(() => {
    request.delete('/collect/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('取消收藏成功');
        loadFavorites();
      } else {
        ElMessage.error(res.msg);
      }
    }).catch(err => {
      console.error('取消收藏失败:', err);
      ElMessage.error('取消收藏失败，请稍后重试');
    });
  }).catch(() => {});
};

const loadReplies = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  const params = {
    pageNum: data.replyPageNum,
    pageSize: data.replyPageSize,
    userId: userFromStorage.id,
    type: data.replyType === 'all' ? '' : data.replyType
  };
  
  request.get('/reply/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.replies = res.data.list;
      data.replyTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleReplySizeChange = (size) => {
  data.replyPageSize = size;
  data.replyPageNum = 1;
  loadReplies();
};

const handleReplyCurrentChange = (page) => {
  data.replyPageNum = page;
  loadReplies();
};

const handleDeleteReply = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/reply/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        loadReplies();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const loadLikes = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  const params = {
    pageNum: data.likePageNum,
    pageSize: data.likePageSize,
    userId: userFromStorage.id,
    type: data.likeType === 'all' ? '' : data.likeType
  };
  
  request.get('/like/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.likes = res.data.list;
      data.likeTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleLikeSizeChange = (size) => {
  data.likePageSize = size;
  data.likePageNum = 1;
  loadLikes();
};

const handleLikeCurrentChange = (page) => {
  data.likePageNum = page;
  loadLikes();
};

const handleCancelLike = (id) => {
  request.delete('/like/delete/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('取消点赞成功');
      loadLikes();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadHistory = () => {
  const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
  const params = {
    pageNum: data.historyPageNum,
    pageSize: data.historyPageSize,
    userId: userFromStorage.id,
    type: data.historyType === 'all' ? '' : data.historyType
  };
  
  request.get('/history/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.history = res.data.list;
      data.historyTotal = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleHistorySizeChange = (size) => {
  data.historyPageSize = size;
  data.historyPageNum = 1;
  loadHistory();
};

const handleHistoryCurrentChange = (page) => {
  data.historyPageNum = page;
  loadHistory();
};

const handleHistoryTypeChange = () => {
  data.historyPageNum = 1;
  loadHistory();
};

const submitFeedback = () => {
  feedbackFormRef.value.validate((valid) => {
    if (!valid) return;
    const userFromStorage = JSON.parse(localStorage.getItem('code_user') || '{}');
    data.feedbackForm.userId = userFromStorage.id;

    request.post('/feedback/add', data.feedbackForm).then(res => {
      if (res.code === '200' || res.code === 200) {
        ElMessage.success('提交成功，感谢您的反馈');
        data.feedbackForm.content = '';
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};
</script>
