import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import relativeTime from 'dayjs/plugin/relativeTime';
import duration from 'dayjs/plugin/duration';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter';
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore';
import 'dayjs/locale/zh-cn';
import 'dayjs/locale/en';

dayjs.extend(utc);
dayjs.extend(relativeTime);
dayjs.extend(duration);
dayjs.extend(customParseFormat);
dayjs.extend(isSameOrAfter);
dayjs.extend(isSameOrBefore);

export const setDayjsLocale = (locale) => {
  const normalizedLocale = locale === 'zh-CN' || locale === 'zh' ? 'zh-cn' : 'en';
  dayjs.locale(normalizedLocale);
  return normalizedLocale;
};

const savedLocale = typeof localStorage !== 'undefined' ? localStorage.getItem('lang') : 'zh-CN';
setDayjsLocale(savedLocale || 'zh-CN');

export default dayjs;
