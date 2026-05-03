// src/filters/index.js
import dayjs from 'dayjs';

// 格式化时间
export function formatTime(value, fmt) {
  return dayjs(value).format(fmt);
}

// 首字母大写
export function capitalize(value) {
  if (!value) return '';
  value = value.toString();
  return value.charAt(0).toUpperCase() + value.slice(1);
}
