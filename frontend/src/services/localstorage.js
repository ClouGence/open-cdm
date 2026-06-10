import { createApp } from 'vue';

export const setItem = (key, value) => {
  localStorage.setItem(key, value);
};

export const getItem = (key) => localStorage.getItem(key);

export const removeItem = (key) => localStorage.removeItem(key);

export const removeAll = () => {
  localStorage.clear(); // Note: LocalStorage does not removeAll method, should be clear
};

const app = createApp();
app.config.globalProperties.$localstorage = {
  setItem,
  getItem,
  removeItem,
  removeAll
};
