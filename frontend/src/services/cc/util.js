import xss from 'xss';

export function formatParam(param) {
  const options = {
    stripIgnoreTag: true,
    allowCommentTag: false,
    escapeHtml(html) {
      return html.replace(/</g, '<').replace(/>/g, '>');
    }
  };
  const myxss = new xss.FilterXSS(options);

  if (param) {
    if (param.toString() === '[object Object]') {
      for (const key in param) {
        if (param[key]) {
          if (param[key].toString() === '[object Object]') {
            formatParam(param[key]);
          } else if (param[key] instanceof Array) {
            param[key].forEach((item) => {
              formatParam(item);
            });
          } else if (param[key]) {
            param[key] = myxss.process(param[key].toString()).trim();
          }
        }
      }
    } else if (param instanceof Array) {
      param.forEach((item) => {
        formatParam(item);
      });
    } else {
      // param = myxss.process(param.toString()).trim();
    }
  }
}
