import errorQueue from './errorQueue';
import formatError from '@/services/formatError';
import { pushError } from '@/utils/toast';
import router from '@/router';

router.beforeEach((to, from, next) => {
  errorQueue.clear();
  next();
});

function formatContent(content) {
  if (!content) {
    return '';
  }
  let contentStr = content;
  if (typeof content !== 'string') {
    if (typeof content === 'object') {
      try {
        contentStr = JSON.stringify(content, null, 2);
      } catch (e) {
        contentStr = String(content);
      }
    } else {
      contentStr = String(content);
    }
  }
  return formatError(contentStr);
}

function showErrorQueueToast(errors) {
  errors.forEach((error) => {
    pushError(formatContent(error.content));
  });
}

function initErrorQueue() {
  errorQueue.setShowModalCallback((errors) => {
    showErrorQueueToast(errors);
  });
}

initErrorQueue();

export { showErrorQueueToast, initErrorQueue };
export default errorQueue;
