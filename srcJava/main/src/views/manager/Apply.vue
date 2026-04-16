<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button> <!-- clearable用于模糊查询-->
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增 赛 事</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="content" label="赛事说明" />
        <el-table-column prop="userName" label="赛事人" />
        <el-table-column prop="time" label="提交时间" />
        <el-table-column prop="status" label="审核状态">
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status === '待审核'">{{ scope.row.status }}</el-tag>
            <el-tag type="success" v-if="scope.row.status === '审核通过'">{{ scope.row.status }}</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '审核拒绝'">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="审核说明" />
        <el-table-column label="操作" width="100">
          <template #default="scope" v-if="data.user.role === 'user'">
            <el-button :disabled="scope.row.status !== '待审核'" type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button :disabled="scope.row.status !== '待审核'" type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-if="data.user.role === 'admin'">
            <el-button :disabled="scope.row.status !== '待审核'" type="primary" @click="handleEdit(scope.row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5,10,15]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog title="审核信息" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="请假标题" v-if="data.user.role === 'user'">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入请假标题" />
        </el-form-item>
        <el-form-item prop="content" label="请假说明" v-if="data.user.role === 'user'">
          <el-input type="textarea" :rows="3" v-model="data.form.content" autocomplete="off" placeholder="请输入请假说明" />
        </el-form-item>
        <el-form-item prop="status" label="审核状态" v-if="data.user.role === 'admin'">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="待审核" value="待审核" />
            <el-radio-button label="审核通过" value="审核通过" />
            <el-radio-button label="审核拒绝" value="审核拒绝" />
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="reason" label="审核说明" v-if="data.user.role === 'admin' && data.form.status === '审核拒绝'">
          <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
    </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import request from "@/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {reactive, ref} from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  pageNum: 1,
  pageSize: 7,
  total: 0,
  title: null,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    title: [
      { required: true, message: '请填写标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请填写内容', trigger: 'blur' }
    ],

  }
})
const load = () => {
  // 修复：参数名 uername → username
  request.get('/apply/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg || '查询失败')
    }
  }).catch(err => { // 新增：异常捕获，避免请求报错无提示
    console.error('查询接口报错：', err)
    ElMessage.error('请稍后重试')
  })
}
load()
const add = () => {
  // 验证通过的情况下
  request.post('/apply/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('新增成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleAdd = () => {
  data.form = {}
  data.form.userId = data.user.id
  data.form.status = ''
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true

}
const reset = () => {
  data.title = null
}
const update = () => {
  // formRef 是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {   // 验证通过的情况下
      request.put('/apply/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const formRef = ref()
const save = () => {
  formRef.value.validate(valid => {
    if(valid){
      data.form.id?update():add()
    }
  })
}
const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/apply/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}
</script>