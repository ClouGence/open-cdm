export default function checkES5Support() {
  // Whether or not to support ES5
  var es5Supported = !!(
    Array.prototype.forEach &&
    Function.prototype.bind &&
    Object.keys &&
    typeof JSON !== 'undefined' &&
    typeof JSON.parse === 'function'
  );

  if (!es5Supported) {
    // Get browser preferred language
    var lang = (navigator.languages && navigator.languages[0]) || navigator.language || navigator.userLanguage || 'zh-CN';
    lang = lang.toLowerCase();

    // Adjudication of English
    var isEnglish = lang.startsWith('en');

    // Chinese Hint
    var zhMessage =
      '您的浏览器版本过低，无法正常运行本系统。\n\n' +
      '请升级或更换为以下现代浏览器（最低版本建议）：\n' +
      ' - Google Chrome 60 及以上\n' +
      ' - Microsoft Edge 79 及以上\n' +
      ' - Mozilla Firefox 54 及以上\n' +
      ' - Safari 10 及以上（Mac）\n\n' +
      '升级浏览器可以获得更好的安全性、性能和使用体验。';

    // English Hint
    var enMessage =
      'Your browser version is too low to run this system properly.\n\n' +
      'Please upgrade or switch to one of the following modern browsers (minimum recommended versions):\n' +
      ' - Google Chrome 60+\n' +
      ' - Microsoft Edge 79+\n' +
      ' - Mozilla Firefox 54+\n' +
      ' - Safari 10+ (Mac)\n\n' +
      'Upgrading your browser ensures better security, performance, and user experience.';

    // Blast window tips
    alert(isEnglish ? enMessage : zhMessage);

    // Abort follow-up script execution
    throw new Error('浏览器版本过低，请升级浏览器版本。');
  }
}
