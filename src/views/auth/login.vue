<template>
  <div class="bg">
    <div style="position: absolute; top: 20px; left: 20px;">
      <el-button @click="goBack" type="primary" plain>
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    <div style="width: 350px; background-color: #fff; opacity: 0.95;border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); padding: 40px 20px">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px; text-align: center; font-weight: bold; font-size: 24px">欢 迎 登 录</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="用户" value="user"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%" size="large" type="primary" @click="login">登 录</el-button>
        </div>
        <div style="text-align: right">
          还没有账号？请 <a style="color: #274afa" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import request from "@/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const route = useRoute()
const formRef = ref()
const data = reactive({
  form: { role: 'user' },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 3, message: '账号最少3位', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
  }
})

onMounted(() => {
  if (route.query.role === 'admin') {
    data.form.role = 'admin'
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem("code_user", JSON.stringify(res.data || {}))
          ElMessage.success('登录成功')
          if (res.data.role === 'user') {
            location.href = '/front/home'
          } else if (res.data.role === 'admin' || res.data.role === 'super_admin') {
            location.href = '/manager/home'
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const goBack = () => {
  location.href = '/front/home'
}
</script>

<style scoped>
.bg {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/images/login.png");
  background-size: cover;
}
</style>
