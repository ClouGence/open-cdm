import MarkdownIt from 'markdown-it';
import DOMPurify from 'dompurify';

const markdown = new MarkdownIt({ html: false, breaks: true }).disable('image');

export function renderAiMessage(message) {
  // Some model tool results double-escape prose line breaks. Preserve code and escaped backslashes.
  const text = message.replace(/(`+|~{3,})[\s\S]*?\1|^(?: {4}|\t).*|\\\\|\\r\\n|\\[nr]/gm, (part) => {
    if (part === '\\n' || part === '\\r' || part === '\\r\\n') return '\n';
    return part;
  });
  return DOMPurify.sanitize(markdown.render(text));
}
