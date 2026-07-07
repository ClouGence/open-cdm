import { toast } from 'vue-sonner';

const DEFAULT_DURATION = 3000;

function stripHtml(html) {
  if (!html || typeof html !== 'string') return html;
  const tmp = document.createElement('div');
  tmp.innerHTML = html;
  return tmp.textContent || tmp.innerText || '';
}

function resolve(msg, duration, onClose) {
  if (typeof msg === 'object' && msg !== null) {
    return {
      content: stripHtml(msg.content || ''),
      duration: msg.duration ?? DEFAULT_DURATION,
      onClose: msg.onClose
    };
  }
  return {
    content: stripHtml(msg || ''),
    duration: duration ?? DEFAULT_DURATION,
    onClose
  };
}

const Toast = {
  success(msg, duration, onClose) {
    const { content, duration: d, onClose: cb } = resolve(msg, duration, onClose);
    return toast.success(content, { duration: d, onDismiss: cb });
  },

  error(msg, duration, onClose) {
    const { content, duration: d, onClose: cb } = resolve(msg, duration, onClose);
    return toast.error(content, { duration: d, onDismiss: cb });
  },

  warning(msg, duration, onClose) {
    const { content, duration: d, onClose: cb } = resolve(msg, duration, onClose);
    return toast.warning(content, { duration: d, onDismiss: cb });
  },

  info(msg, duration, onClose) {
    const { content, duration: d, onClose: cb } = resolve(msg, duration, onClose);
    return toast.info(content, { duration: d, onDismiss: cb });
  },

  loading(msg, duration, onClose) {
    const { content, duration: d, onClose: cb } = resolve(msg, duration, onClose);
    return toast.loading(content, { duration: d, onDismiss: cb });
  },

  config() {
    // no-op for backward compatibility
  },

  destroy() {
    toast.dismiss();
  }
};

// Manual push entry point
export function pushError(msg, duration) {
  return Toast.error(msg, duration ?? 8000);
}

export function pushSuccess(msg, duration) {
  return Toast.success(msg, duration);
}

export function pushWarning(msg, duration) {
  return Toast.warning(msg, duration);
}

export function pushInfo(msg, duration) {
  return Toast.info(msg, duration);
}

export default Toast;
