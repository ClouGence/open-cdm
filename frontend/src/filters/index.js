// src/filters/index.js
import dayjs from 'dayjs';

// Formatting Time
export function formatTime(value, fmt) {
  return dayjs(value).format(fmt);
}

// Capitalise initials
export function capitalize(value) {
  if (!value) return '';
  value = value.toString();
  return value.charAt(0).toUpperCase() + value.slice(1);
}
