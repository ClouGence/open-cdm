import { createApp } from 'vue';

export const setItem = (key, value) => {
  localStorage.setItem(key, value);
};

export const getItem = (key) => localStorage.getItem(key);

export const removeItem = (key) => localStorage.removeItem(key);

export const removeAll = () => {
  localStorage.clear(); // 注意：localStorage 没有 removeAll 方法，应该是 clear
};

const app = createApp();
app.config.globalProperties.$localstorage = {
  setItem,
  getItem,
  removeItem,
  removeAll
};
