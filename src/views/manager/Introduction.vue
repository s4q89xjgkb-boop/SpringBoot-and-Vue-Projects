<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button> <!-- clearable用于模糊查询-->
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增 攻 略</el-button>
      <el-button type="danger" @click="deleteBatch">批 量 删 除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column label="图片">
          <template #default="scope">
            <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true" style="width: 55px; height: 55px;border-radius: 5px; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="文章标题"  />
        <el-table-column prop="categoryTitle" label="攻略分类"  />
        <el-table-column prop="content" label="攻略内容" >
          <template v-slot="scope" style="width: 100%">
            <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" />
        <el-table-column prop="views" label="浏览量" />
        <el-table-column prop="status" label="审核状态">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.status === 0">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.status === 1">审核通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 2">审核拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
            <el-button type="success" icon="Check" circle @click="handleAudit(scope.row)" v-if="scope.row.status === 0"></el-button>
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

    <el-dialog title="攻略信息" v-model="data.formVisible" width="60%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="文章标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item prop="title" label="攻略分类">
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择攻略分类"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="文章详情">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        <el-form-item prop="img" label="文章预览图">
          <el-upload
              action="http://localhost:1306/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="视频上传">
          <el-upload
              action="http://localhost:1306/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleVideoSuccess"
              :accept="'video/mp4,video/avi,video/mov,video/wmv,video/flv,video/mkv'"
              list-type="text"
          >
            <el-button type="warning">上传视频</el-button>
          </el-upload>
          <el-button v-if="data.form.video" type="text" @click="previewVideo">预览视频</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog title="攻略内容" v-model="data.viewVisible" width="60%" destroy-on-close>
      <div class="strategy-content" v-html="data.content" style="padding: 0 20px; width: 100%"></div>
    </el-dialog>

    <el-dialog title="文章审核" v-model="data.auditVisible" width="30%" destroy-on-close>
      <el-form ref="auditFormRef" :model="data.auditForm" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="status" label="审核状态">
          <el-radio-group v-model="data.auditForm.status">
            <el-radio-button :label="1">审核通过</el-radio-button>
            <el-radio-button :label="2">审核拒绝</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="reason" label="审核说明" v-if="data.auditForm.status === 2">
          <el-input type="textarea" :rows="3" v-model="data.auditForm.reason" autocomplete="off" placeholder="请输入拒绝说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.auditVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveAudit">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import request from "@/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {v} from "vue-router/dist/devtools-EWN81iOl.mjs";
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  categoryData: [],
  auditForm: {},
  auditVisible: false,
  rules: {
    img: [
      { required: true, message: '请上传图片', trigger: 'blur' }
    ],
    title: [
      { required: true, message: '请填写标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请填写内容', trigger: 'blur' }
    ],

  },
  content: null,
  viewVisible: false,
})
/* wangEditor5 初始化开始 */
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const auditFormRef = ref()  // 审核表单引用
const mode = 'default'
const editorConfig = {
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
  MENU_CONF: {}
}
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user?.token,
  },
  server: 'http://localhost:1306/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file',  // 服务端图片上传接口参数
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
// 视频上传配置
editorConfig.MENU_CONF['uploadVideo'] = {
  server: 'http://localhost:1306/files/wang/upload',
  fieldName: 'file',
  headers: {
    token: data.user?.token
  },
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
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

const load = () => {
  request.get('/introduction/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res =>{
    if(res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset = () =>{
  data.title =null
  load()
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))  // 深度拷贝数据，表单与ROw中的数据不同步，点保存才可以
  data.formVisible = true
}
const handleFileSuccess = (res) => {
  // 后端返回code=200表示成功，res.data是图片访问URL
  if (res.code === 200 || res.code === '200') {
    data.form.img = res.data; // 赋值给表单的img字段
    ElMessage.success('预览图上传成功');
  } else {
    ElMessage.error('预览图上传失败：' + res.msg);
  }
}

const handleVideoSuccess = (res) => {
  // 后端返回code=200表示成功，res.data是视频访问URL
  if (res.code === 200 || res.code === '200') {
    data.form.video = res.data; // 赋值给表单的video字段
    // 将视频URL添加到文章内容中
    if (data.form.content) {
      data.form.content += `<video src="${res.data}" controls style="max-width: 100%; max-height: 500px; margin: 20px 0;"></video>`;
    } else {
      data.form.content = `<video src="${res.data}" controls style="max-width: 100%; max-height: 500px; margin: 20px 0;"></video>`;
    }
    ElMessage.success('视频上传成功');
  } else {
    ElMessage.error('视频上传失败：' + res.msg);
  }
}

const previewVideo = () => {
  if (data.form.video) {
    window.open(data.form.video, '_blank');
  }
}
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}
const formRef = ref()
const add = () => {
  // 验证通过的情况下
      data.form.userId = data.user.id
      request.post('/introduction/add', data.form).then(res => {
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
  // formRef 是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {   // 验证通过的情况下
      request.put('/introduction/update', data.form).then(res => {
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
  data.form.id ? update() : add()//有值的更新，没有的加
}
const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/introduction/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}
const handleSelectionChange = (rows) => {  // rows 就是实际选择的数组,这一个函数必须在批量删除的相邻上面
  data.rows = rows
  data.ids = data.rows.map(V => v.id)//map可以把对象数组 转化为一个纯数字数组
}
const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/introduction/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}

const handleAudit = (row) => {
  data.auditForm = {
    id: row.id,
    status: 1,
    reason: ''
  }
  data.auditVisible = true
}

const saveAudit = () => {
  auditFormRef.value.validate((valid) => {
    if (!valid) return;
    request.put('/introduction/update', data.auditForm).then(res => {
      if (res.code === '200') {
        ElMessage.success('审核成功')
        data.auditVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()
</script>

<style scoped>
/* 只修改选择器为 .strategy-content，匹配弹窗容器的class，其他不动 */
:deep(.strategy-content img) {
  max-width: 100% !important; /* 强制不超过容器宽度 */
  max-height: 80vh !important; /* 强制不超过视口80%高度，避免超出弹窗 */
  object-fit: contain !important; /* 保持图片宽高比，不拉伸变形 */
  display: block !important; /* 确保宽高限制生效 */
  margin: 0 auto; /* 图片水平居中（可选，不影响尺寸限制） */
}
</style>