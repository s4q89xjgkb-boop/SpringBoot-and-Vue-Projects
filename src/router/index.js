import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/front/home' },
    
    // 前台页面
    {path: '/front/home',component: ()=>import('../views/front/Home.vue')},
    {path: '/front/introductiondetail',component: ()=>import('../views/front/IntroductionDetail.vue')},
    {path: '/front/forum',component: ()=>import('../views/front/Forum.vue')},
    {path: '/front/postDetail',component: ()=>import('../views/front/PostDetail.vue')},
    {path: '/front/news',component: ()=>import('../views/front/News.vue')},
    {path: '/front/newsDetail',component: ()=>import('../views/front/NewsDetail.vue')},
    {path: '/front/articles',component: ()=>import('../views/front/Articles.vue')},
    {path: '/front/noticelist',component: ()=>import('../views/front/NoticeList.vue')},
    {path: '/front/notice',component: ()=>import('../views/front/NoticeDetail.vue')},
    {path: '/front/profile',component: ()=>import('../views/front/Profile.vue')},
    {path: '/front/search',component: ()=>import('../views/front/SearchResult.vue')},
    
    // 后台管理页面
    {path: '/manager',component: ()=>import('../views/manager/Manager.vue'),
      children: [
        {path: 'Admin', meta: { name :'管理员信息', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Admin.vue'),},
        {path: 'home', meta: { name :'主页', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Home.vue'),},
        {path: 'User', meta: { name :'用户信息', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/User.vue'),},
        {path: 'person', meta: { name :'个人信息', requiresAuth: true },component:()=> import('../views/manager/person.vue'),},
        {path: 'updatepassword', meta: { name :'修改密码', requiresAuth: true },component:()=> import('../views/manager/updatePassword.vue'),},
        {path: 'notice', meta: { name :'公告', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Notice.vue')},
        {path: 'introduction', meta: { name :'游戏攻略', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Introduction.vue')},
        {path: 'introductionAudit', meta: { name :'游戏攻略审核', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/IntroductionAudit.vue')},
        {path: 'category', meta: { name :'游戏分类', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Category.vue')},
        {path: 'Banner', meta: { name :'轮播图管理', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Banner.vue')},
        {path: 'news', meta: { name :'游戏咨询管理', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/NewsManager.vue')},
        {path: 'forumSection', meta: { name :'论坛板块', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/ForumSection.vue')},
        {path: 'post', meta: { name :'帖子管理', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Post.vue')},
        {path: 'postAudit', meta: { name :'帖子审核', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/PostAudit.vue')},
        {path: 'feedback', meta: { name :'意见反馈管理', requiresAuth: true, requiresAdmin: true },component:()=> import('../views/manager/Feedback.vue')},
      ]},
    
    // 认证页面
    {path: '/login',component: ()=>import('../views/auth/login.vue')},
    {path: '/register',component: ()=>import('../views/auth/Register.vue')},
    {path: '/notFound',component: ()=>import('../views/auth/404.vue')},
    {path: '/:pathMatch(.*)',redirect:'/notFound'}
  ],
})

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('code_user') || '{}')
  
  if (to.meta.requiresAuth) {
    if (!user || !user.token) {
      next('/login')
      return
    }
    
    if (to.meta.requiresAdmin && (user.role !== 'admin' && user.role !== 'super_admin')) {
      next('/front/home')
      return
    }
  }
  
  next()
})

export default router
