<template xmlns="http://www.w3.org/1999/html">
  <div>
    <div style="height: 70px; align-items: center ;background: #f4f0ee; display: flex"><!--头部区域-->
      <div style="display: flex;align-items: center;padding-left: 20px">
        <img style="width: 50px;height: 50px; border-radius: 50%" src="@/assets/images/Mlogo.png" alt="">
        <span style="font-size: 24px; font-weight: bold;color: #2c82ff;margin-left: 5px">
          后台管理平台
        </span>
      </div>
      <!-- 新增：面包屑容器 - 放在头部中间空白区域，垂直居中 -->
      <div style="flex: 1; display: flex; align-items: center; padding-left: 20px;">
        <el-breadcrumb separator="/" style="font-size: 14px;">
          <el-breadcrumb-item>
            <router-link to="/front/home" style="color: #666; text-decoration: none;">返回前台</router-link>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <router-link to="/manager/Home" style="color: #666; text-decoration: none;">首页</router-link>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentRoutePath === '/manager/Admin'" style="color: #333;">
            管理员
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div>
        <el-dropdown style="padding-right: 5px">
          <div style="width: fit-content; display: flex;align-items: center; padding-right: 20px">
            <img v-if="data.user?.avatar" style="width: 50px;height: 50px; border-radius: 50%" :src="data.user?.avatar" />
            <span style="padding-left: 5px">{{data.user?.name}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div><!-- 头部结束-->
    <!--下方开始-->
    <div style="display: flex">
      <!--meun degins-->
      <div style="width: 230px">
        <!-- 优化：使用 useRoute 和 watch 来确保高亮实时更新 -->
        <el-menu router :default-openeds="['1','2','3']" :default-active="currentRoutePath" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/Home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><DataBoard /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/banner">轮播图管理</el-menu-item>
        <el-menu-item index="/manager/notice">系统公告</el-menu-item>
        <el-menu-item index="/manager/category">游戏分类</el-menu-item>
        <el-menu-item index="/manager/introduction">游戏攻略文章</el-menu-item>
        <el-menu-item index="/manager/introductionAudit">游戏攻略审核</el-menu-item>
        <el-menu-item index="/manager/news">游戏咨询管理</el-menu-item>
        <el-menu-item index="/manager/feedback">意见反馈管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon><ChatDotRound /></el-icon>
              <span>论坛管理</span>
            </template>
            <el-menu-item index="/manager/forumSection">论坛板块</el-menu-item>
            <el-menu-item index="/manager/post">帖子管理</el-menu-item>
            <el-menu-item index="/manager/postAudit">帖子审核</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <!-- 关键：路径保持为 /manager/data，与你的路由配置完全一致 -->
            <el-menu-item index="/manager/Admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/User">用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!--meun over-->
      <!--data area-->
      <div style="flex: 1; margin: 0 0 0 0; padding: 10px; overflow: auto; background: #dcf4f8">
        <router-view @updateuser="updateUser"/>
      </div>
      <!--data area-->
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { reactive } from "vue";
import { useRoute } from 'vue-router';
import {DataBoard, House, User, ChatDotRound} from "@element-plus/icons-vue";
// 新增：引入面包屑组件和路由跳转组件
import { ElBreadcrumb, ElBreadcrumbItem } from "element-plus";
import { RouterLink } from "vue-router";
import router from "@/router/index.js";

// 1. 获取当前路由实例
const route = useRoute();

// 2. 创建一个 ref 来存储当前路由路径
const currentRoutePath = ref(route.path);
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}")
})
const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}
const updateUser = () => {
    data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

// 3. 监听路由变化，实时更新 currentRoutePath，确保菜单高亮和面包屑正确
watch(
    () => route.path,
    (newPath) => {
      currentRoutePath.value = newPath;
    },
    { immediate: true }
);
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  height: 100%;
}
#app {
  height: 100%;
}
.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}
/* 确保菜单高度正确 */
.el-menu {
  border-right: none;
}
/* 优化面包屑样式，避免hover样式冲突 */
.el-breadcrumb__item a:hover {
  color: #2c82ff !important;
}
</style>
