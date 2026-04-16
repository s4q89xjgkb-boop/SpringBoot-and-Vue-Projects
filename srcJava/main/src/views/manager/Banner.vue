<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增 轮 播 图</el-button>
      <el-button type="danger" @click="deleteBatch">批 量 删 除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column label="图片">
          <template #default="scope">
            <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true" style="width: 55px; height: 55px;border-radius: 5px; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="link" label="跳转链接" />
        <el-table-column prop="sort" label="排序" />
        <el-table-column prop="time" label="发布时间" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
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

    <el-dialog title="轮播图信息" v-model="data.formVisible" width="60%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="img" label="轮播图">
          <el-upload
              action="http://localhost:1306/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item prop="link" label="跳转链接">
          <el-input v-model="data.form.link" autocomplete="off" placeholder="请输入跳转链接"/>
        </el-form-item>
        <el-form-item prop="sort" label="排序">
          <el-input-number v-model="data.form.sort" :min="0" :max="999" placeholder="请输入排序"/>
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
import {onBeforeUnmount, onMounted, reactive, ref, shallowRef} from "vue";
import request from "@/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  rules: {
    img: [
      { required: true, message: '请上传图片', trigger: 'blur' }
    ],
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' }
    ],
    link: [
      { required: true, message: '请输入跳转链接', trigger: 'blur' }
    ],
    sort: [
      { required: true, message: '请输入排序', trigger: 'blur' }
    ],
  }
})

const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize
  }
  if (data.title) {
    params.title = data.title
  }
  request.get('/banner/selectPage', {
    params: params
  }).then(res =>{
    if(res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () =>{
  data.title =null
  load()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleFileSuccess = (res) => {
  if (res.code === 200 || res.code === '200') {
    data.form.img = res.data;
    ElMessage.success('图片上传成功');
  } else {
    ElMessage.error('图片上传失败：' + res.msg);
  }
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const formRef = ref()
const add = () => {
  request.post('/banner/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('新增成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/banner/update', data.form).then(res => {
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

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/banner/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/banner/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

onMounted(() => {
  load()
})

onBeforeUnmount(() => {
})

</script>

<style scoped>
.card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
</style>
