import { createI18n } from 'vue-i18n';
import iEn from 'view-ui-plus/dist/locale/en-US';
import iZhCN from 'view-ui-plus/dist/locale/zh-CN';

export const BASE_LOCALES = ['zh-CN', 'en-US'];
export const DEFAULT_LOCALE = 'en-US';

const normalizeLocale = (lang) => {
  if (!lang) return DEFAULT_LOCALE;
  if (['zh', 'zh-TW', 'zh-HK', 'zh-CN'].includes(lang)) return 'zh-CN';
  if (lang.startsWith('en')) return 'en-US';
  return DEFAULT_LOCALE;
};

const getInitialLang = () => {
  const saved = localStorage.getItem('lang');
  if (saved) return normalizeLocale(saved);

  if (process.env.VUE_APP_I18N_LOCALE === 'en') {
    return 'en-US';
  }

  const lang = navigator.language || navigator.userLanguage || 'zh-CN';
  return normalizeLocale(lang);
};

const initialLocale = getInitialLang();

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

export const setAppLanguage = (lang) => {
  const targetLang = normalizeLocale(lang);
  localStorage.setItem('lang', targetLang);
  i18n.global.locale.value = targetLang;
  window.location.reload();
};

export default i18n;
