import axios from 'axios'
import storage from "./storage";
import Vue from "vue";

// timeout设置大一些,文件上传比较慢
const request = axios.create({
    timeout: 60000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let token = storage.get('token')
    if (config.url !== 'login' && config.url !== 'register' && !!token) {
        config.headers['token'] = token;  // 设置请求头
    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        console.log(res);
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.success === false){
            Vue.prototype.errorNotify(res.message)
        }
        return res;
    },
    error => {
        Vue.prototype.errorNotify(error.message)
    }
)

export default request
