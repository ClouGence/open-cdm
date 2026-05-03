import { createI18n } from 'vue-i18n';
import iEn from 'view-ui-plus/dist/locale/en-US';
import iZhCN from 'view-ui-plus/dist/locale/zh-CN';
import { applyGoogleTranslate, clearGoogleTranslateArtifacts } from '@/utils/googleTranslate';

export const BASE_LOCALES = ['zh-CN', 'en-US'];
export const EXTRA_GOOGLE_LOCALES = ['ja', 'ko', 'vi', 'th', 'fr', 'es', 'ms'];
export const DEFAULT_LOCALE = 'en-US';

const normalizeLocale = (lang) => {
  if (!lang) return '';
  if (['zh', 'zh-TW', 'zh-HK', 'zh-CN'].includes(lang)) return 'zh-CN';
  if (lang.startsWith('en')) return 'en-US';
  return lang;
};

const getInitialLang = () => {
  const saved = localStorage.getItem('lang');
  if (saved) return normalizeLocale(saved);

  // 没有用户选择时再看环境变量
  if (process.env.VUE_APP_I18N_LOCALE === 'en') {
    return 'en-US';
  }

  const lang = navigator.language || navigator.userLanguage || 'zh-CN';
  return normalizeLocale(lang);
};

const userLang = getInitialLang();
const initialLocale = BASE_LOCALES.includes(userLang) ? userLang : DEFAULT_LOCALE;

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: initialLocale,
  fallbackLocale: DEFAULT_LOCALE,
  messages: {
    'zh-CN': Object.assign(require('./locales/zh.json'), iZhCN),
    'en-US': Object.assign(require('./locales/en.json'), iEn)
  }
});

export const setAppLanguage = async (lang) => {
  const targetLang = normalizeLocale(lang);
  localStorage.setItem('lang', targetLang);

  if (BASE_LOCALES.includes(targetLang)) {
    clearGoogleTranslateArtifacts();
    i18n.global.locale.value = targetLang;
    window.location.reload();
    return;
  }

  // 其它语言走 Google Translate，但基准文案保持英文，翻译精度更高
  i18n.global.locale.value = DEFAULT_LOCALE;
  await applyGoogleTranslate(targetLang);
};

export const bootstrapGoogleTranslate = async () => {
  const savedLang = normalizeLocale(localStorage.getItem('lang'));
  if (savedLang && !BASE_LOCALES.includes(savedLang)) {
    i18n.global.locale.value = DEFAULT_LOCALE;
    await applyGoogleTranslate(savedLang);
  }
};

export default i18n;
