<template>
  <!-- 限制卡片宽度，保证不同屏幕下布局一致 -->
  <div class="card" style="max-width: 500px; width: 90%;">
    <div class="card-title">修改密码</div>
    <!-- 优化表单布局，统一标签宽度，增加内边距 -->
    <el-form
      ref="formRef"
      :rules="data.rules"
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
      <!-- 补充统一标签，保持布局对齐 -->
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
    <!-- 优化按钮样式，更协调美观 -->
    <div class="btn-wrap">
      <el-button type="primary" size="large" @click="updatepassword">保 存</el-button>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref } from "vue";
import request from "@/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: {
    ...JSON.parse(localStorage.getItem('code_user') || '{}'),
    password: '',
    newpassword: '',
    newpassword2: ''
  },
  rules: {
    password: [
      { required: true, message: '请填旧密码', trigger: 'blur' }
    ],
    newpassword: [
      { required: true, message: '请填写新密码', trigger: 'blur' }
    ],
    newpassword2: [
      { required: true, message: '请再次输入新密码', trigger: 'blur' }
    ],
  }
});

const formRef = ref();

const updatepassword = () => {
  formRef.value.validate((valid) => {
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
}
</script>
<style scoped>
/* 卡片样式优化，增强视觉层次感 */
.card {
  margin: 30px auto;
  padding: 30px 25px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px 0 rgba(0, 0, 0, 0.08);
}

/* 标题样式优化，突出主题 */
.card-title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
}

/* 表单间距优化，避免项目拥挤 */
.password-form {
  margin-bottom: 25px;
}

.password-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

/* 按钮容器样式，居中对齐，增加间距 */
.btn-wrap {
  text-align: center;
}

.btn-wrap :deep(.el-button) {
  padding: 12px 40px;
  font-size: 16px;
}
</style>
