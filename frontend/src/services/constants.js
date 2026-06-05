// DOMPurify 允许的HTML标签列表
export const ALLOWED_HTML_TAGS = [
  'h1',
  'h2',
  'h3',
  'h4',
  'h5',
  'h6',
  'p',
  'a',
  'ul',
  'ol',
  'li',
  'span',
  'strong',
  'em',
  'br',
  'img',
  'blockquote',
  'code',
  'pre',
  'hr',
  'del',
  'ins',
  'sub',
  'sup',
  'table',
  'tbody',
  'thead',
  'tr',
  'td',
  'th'
];

// DOMPurify 配置选项
export const DOMPURIFY_CONFIG = {
  ALLOWED_TAGS: ALLOWED_HTML_TAGS,
  FORBID_ATTR: ['onerror', 'onload', 'onclick', 'onmouseover', 'onmouseout', 'onfocus', 'onblur'], // 禁止事件处理器
  ALLOWED_ATTR: ['href', 'target', 'rel', 'title', 'class', 'id', 'style']
};
