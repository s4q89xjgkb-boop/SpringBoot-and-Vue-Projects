import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: 'http://localhost:1306',
    timeout: 30000  // 后台接口超时时间
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user= JSON.parse(localStorage.getItem('code_user') || '{}')//把存入的token传入前端,有值解析成JSON
    if (user.token) {
        config.headers['token'] = user.token
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401'){//防止直接通过路由直接进入后台，用这个判断以下,输入后台路由会返回到login'登入页面
            ElMessage.error(res.msg)
            // 只在后台页面时跳转到登录页面，前台页面不跳转
            if (location.pathname.startsWith('/manager')) {
                window.location.href = '/login'
            }
            return res; // 确保返回res，避免Promise链中断
        } else {
            return res;
        }
    },
    error => {
        if (error.response && error.response.status === 404) {
            const url = error.config?.url || ''
            console.log('请求失败，URL:', url, '状态:', 404)
            ElMessage.error('请求失败：接口不存在或参数错误')
        } else if (error.response && error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错')
        } else {
            console.error('请求错误:', error)
            ElMessage.error('网络异常，请稍后重试')
        }
        return Promise.reject(error)
    }
)

export default request
