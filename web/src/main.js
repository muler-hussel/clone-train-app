import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd, { notification }from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')

const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('Request param: ', config);
    /*const _token = store.state.member.token;
    if (_token) {
        config.headers.token = _token;
        console.log("请求headers增加token:", _token);
    }*/
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('Result: ', response);
    return response;
}, error => {
    console.log('Result: ', error);
    /*const response = error.response;
    const status = response.status;
    if (status === 401) {
        // 判断状态码是401 跳转到登录页
        console.log("未登录或登录超时，跳到登录页");
        store.commit("setMember", {});
        notification.error({ description: "未登录或登录超时" });
        router.push('/login');
    }*/
    return Promise.reject(error);
});
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('Environment: ', process.env.NODE_ENV);
console.log('Server: ', process.env.VUE_APP_SERVER);