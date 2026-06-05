import { createApp } from 'vue';
import request from './request';
import { api } from './api';

const services = {};

Object.entries(api).forEach((item) => {
  const url = item[1];
  // if (process.env.VUE_APP_BASE_URL) {
  //   url = process.env.VUE_APP_BASE_URL + item[1];
  // }
  services[item[0]] = (options = {}) => request({ url, ...options });
});

// 使用 app.config.globalProperties 设置全局属性
const app = createApp({});
app.config.globalProperties.$services = services;

export default services;
export { services };
