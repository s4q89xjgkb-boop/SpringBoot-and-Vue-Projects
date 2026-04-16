<template>
  <div class="card" style="width: 50%">
    <div>个人中心</div>
    <el-form ref="formRef" :model="data.user" label-width="80px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="username" label="账号">
        <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号"/>
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
            :src="data.user.avatar || '默认头像占位图地址'"
            :preview-src-list="[data.user.avatar]"
            :preview-teleported="true"
            style="width: 40px; height: 40px; border-radius: 50%; display: block"
        />
      </el-form-item>
      <el-form-item prop="avatar" label="头像">
        <el-upload
            action="http://localhost:1306/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleFileSuccess"
            list-type="picture"
        >
          <el-button type="primary">更改头像</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <div style="text-align: center;">
      <el-button type="primary" style="padding: 20px 38px" @click="update">保 存</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from '@/request.js'
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),

})
const handleFileSuccess = (res) => {
  data.user.avatar = res.data
  ElMessage.success('头像上传成功')
}
const emit = defineEmits(['updateUser'])
const update = () =>{
  let url
  if(data.user.role === 'admin' || data.user.role === 'super_admin'){
    url ='/admin/update'
  } else if(data.user.role === 'user'){
    url='/user/update'
  } else {
    ElMessage.error('用户角色错误')
    return
  }
  request.put(url, data.user).then(res =>{
    if(res.code === '200'){
      ElMessage.success('更新成功')
      localStorage.setItem("code_user", JSON.stringify(data.user))
      emit('updateUser')
    }
  })
}
</script>

<style scoped>
/* 可选：添加少量样式优化 */
.card {
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>