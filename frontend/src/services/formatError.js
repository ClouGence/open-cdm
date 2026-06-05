import MarkdownIt from 'markdown-it';
import DOMPurify from 'dompurify';
import { DOMPURIFY_CONFIG } from './constants';

/**
 * 判断是否为内部路由链接
 * @param {string} href - 链接地址
 * @returns {boolean} - 是否为内部路由
 */
function isInternalRoute(href) {
  return href.startsWith('/');
}

/**
 * 安全渲染markdown内容
 * @param {string} content - 要渲染的内容
 * @returns {string} - 安全渲染后的HTML
 */
function safeRenderMarkdown(content) {
  const md = new MarkdownIt({
    linkify: true,
    breaks: true,
    html: true // 必须启用HTML解析
  });

  // 配置a标签处理外部链接和内部路由
  const defaultRender =
    md.renderer.rules.link_open ||
    function (tokens, idx, options, env, self) {
      return self.renderToken(tokens, idx, options);
    };

  md.renderer.rules.link_open = function (tokens, idx, options, env, self) {
    const token = tokens[idx];
    const hrefIndex = token.attrIndex('href');

    if (hrefIndex >= 0) {
      const href = token.attrs[hrefIndex][1];

      if (isInternalRoute(href)) {
        // 内部路由不做处理，走href默认逻辑
      } else {
        // 外部链接：添加 target="_blank" 和 rel 属性
        const targetIndex = token.attrIndex('target');
        if (targetIndex < 0) {
          token.attrPush(['target', '_blank']);
        }
        const relIndex = token.attrIndex('rel');
        if (relIndex < 0) {
          token.attrPush(['rel', 'noopener noreferrer']);
        }
      }
    }

    return defaultRender(tokens, idx, options, env, self);
  };

  const html = md.render(content);

  if (typeof window !== 'undefined' && window.DOMPurify) {
    return window.DOMPurify.sanitize(html, DOMPURIFY_CONFIG);
  } else if (typeof DOMPurify !== 'undefined') {
    return DOMPurify.sanitize(html, DOMPURIFY_CONFIG);
  } else {
    return html; // fallback
  }
}

/**
 * 格式化错误信息：
 * 1. 如果是数组字符串，返回第一项
 * 2. 否则用markdown-it渲染并用DOMPurify安全过滤
 */
export default function formatError(error) {
  if (typeof error === 'string') {
    // 判断是否为数组字符串
    try {
      const arr = JSON.parse(error);
      if (Array.isArray(arr)) {
        let finalRes = '';
        arr.forEach((item) => {
          finalRes += safeRenderMarkdown(item);
        });
        return finalRes;
      }
    } catch (e) {
      // 不是JSON数组字符串，继续处理
    }
    // markdown-it 渲染 + DOMPurify 安全处理
    return safeRenderMarkdown(error);
  }
  return error;
}
