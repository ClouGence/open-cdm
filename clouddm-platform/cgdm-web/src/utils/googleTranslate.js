let loadingPromise = null;

const SCRIPT_URL = 'https://translate.google.com/translate_a/element.js?cb=__initGoogleTranslate';

const injectScript = () => {
  if (typeof window === 'undefined') return Promise.resolve();
  if (window.google && window.google.translate) return Promise.resolve();
  if (loadingPromise) return loadingPromise;

  loadingPromise = new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = SCRIPT_URL;
    script.async = true;
    script.onerror = reject;
    window.__initGoogleTranslate = () => resolve();
    document.body.appendChild(script);
  });

  return loadingPromise;
};

export const applyGoogleTranslate = async (lang) => {
  if (typeof window === 'undefined') return;
  await injectScript();

  if (!window.google || !window.google.translate) return;
  if (!window.__gtElement) {
    window.__gtElement = new window.google.translate.TranslateElement(
      {
        pageLanguage: 'en',
        autoDisplay: false
      },
      'google_translate_element'
    );
  }

  const select = document.querySelector('.goog-te-combo');
  if (select) {
    select.value = lang;
    select.dispatchEvent(new Event('change'));
  }
};

export const clearGoogleTranslateArtifacts = () => {
  // Best-effort清理，无法彻底卸载，只隐藏痕迹。
  const bannerFrame = document.querySelector('iframe.goog-te-banner-frame');
  if (bannerFrame && bannerFrame.parentNode) {
    bannerFrame.parentNode.removeChild(bannerFrame);
  }
  const styleTag = document.querySelector('style#google-translate-custom');
  if (styleTag && styleTag.parentNode) {
    styleTag.parentNode.removeChild(styleTag);
  }
  const tooltip = document.getElementById('goog-gt-tt');
  if (tooltip && tooltip.parentNode) {
    tooltip.parentNode.removeChild(tooltip);
  }
};
