import MarkdownIt from 'markdown-it';
import DOMPurify from 'dompurify';
import { DOMPURIFY_CONFIG } from './constants';

/**
 * Judge whether internal route links
 * @param {string} href - Link Address
 * @returns {bolean} - Internal route
 */
function isInternalRoute(href) {
  return href.startsWith('/');
}

/**
 * Safely render markdown content
 * @param {string} Content - To Render
 * @returns{string} - HTML after safe rendering
 */
function safeRenderMarkdown(content) {
  const md = new MarkdownIt({
    linkify: true,
    breaks: true,
    html: true // HTML Resolution must be enabled
  });

  // Configure a label to handle external links and internal routes
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
        // Do not process internal circuits, take href default logic
      } else {
        // External link: add target = " blank" and rel attribute
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
 * Format error messages.
 * 1. Return the first item if the message is a JSON array string.
 * 2. Otherwise render with markdown-it and sanitize with DOMPurify.
 */
export default function formatError(error) {
  if (typeof error === 'string') {
    // Check whether the message is a JSON array string.
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
      // Not a JSON array string. Continue processing.
    }
    // markdown-it render + DOMPurify secure
    return safeRenderMarkdown(error);
  }
  return error;
}
