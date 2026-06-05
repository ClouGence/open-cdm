import { ref } from 'vue';
import { Modal } from 'view-ui-plus';
import store from '@/store';
import i18n from '@/i18n';
import errorQueue from './errorQueue';
import formatError from '@/services/formatError';
import router from '@/router';

let currentErrors = ref([]);
let currentIndex = ref(0);

// 浏览器前进/后退，关闭弹窗
router.beforeEach((to, from, next) => {
  Modal.remove();
  errorQueue.clear();
  delete window.__errorQueuePrev;
  delete window.__errorQueueNext;
  next();
});

function formatContent(content) {
  if (!content) {
    return '';
  }

  // 确保字符串
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

function showErrorQueueModal(errors) {
  currentErrors.value = errors;
  currentIndex.value = 0;

  const getCurrentError = () => currentErrors.value[currentIndex.value];

  const prevError = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--;
      updateModalContent();
    }
  };

  const nextError = () => {
    if (currentIndex.value < currentErrors.value.length - 1) {
      currentIndex.value++;
      updateModalContent();
    }
  };

  const getModalTitle = () => {
    return i18n.global.t('cuo-wu');
  };

  const getModalContent = () => {
    const error = getCurrentError();
    if (!error) return '';

    const isDark = store.state.theme === 'dark';
    const textColor = isDark ? 'rgba(255, 255, 255, 0.7)' : '#515a6e';
    const disabledColor = isDark ? 'rgba(255, 255, 255, 0.35)' : '#c5c8ce';
    const hoverColor = isDark ? '#0087c7' : '#2d8cf0';

    let html = `
      <div style="min-height: 60px; max-height: 500px; overflow-y: auto;">
        <div style="min-height: 60px; max-height: 350px; overflow-y: auto; line-height: 1.6; color: ${textColor}; word-wrap: break-word; word-break: break-all;">
          ${formatContent(error.content)}
        </div>
        
        ${
          currentErrors.value.length > 1
            ? `
          <div style="display: flex; justify-content: flex-start; align-items: center; gap: 4px;">
            <button 
              ${currentIndex.value === 0 ? 'disabled' : ''} 
              onclick="window.__errorQueuePrev()"
              style="background: none; border: none; padding: 4px 8px; padding-left: 0; cursor: ${currentIndex.value === 0 ? 'not-allowed' : 'pointer'}; color: ${currentIndex.value === 0 ? disabledColor : textColor}; font-size: 16px;"
              onmouseover="if (!this.disabled) this.style.color='${hoverColor}'"
              onmouseout="if (!this.disabled) this.style.color='${textColor}'"
            >
              <i class="ivu-icon ivu-icon-ios-arrow-back"></i>
            </button>
            <span style="font-size: 14px; color: ${textColor}; margin: 0 4px;">
              ${currentIndex.value + 1}/${currentErrors.value.length}
            </span>
            <button 
              ${currentIndex.value === currentErrors.value.length - 1 ? 'disabled' : ''} 
              onclick="window.__errorQueueNext()"
              style="background: none; border: none; padding: 4px 8px; cursor: ${currentIndex.value === currentErrors.value.length - 1 ? 'not-allowed' : 'pointer'}; color: ${currentIndex.value === currentErrors.value.length - 1 ? disabledColor : textColor}; font-size: 16px;"
              onmouseover="if (!this.disabled) this.style.color='${hoverColor}'"
              onmouseout="if (!this.disabled) this.style.color='${textColor}'"
            >
              <i class="ivu-icon ivu-icon-ios-arrow-forward"></i>
            </button>
          </div>
        `
            : ''
        }
      </div>
    `;

    return html;
  };

  // 挂到全局调用
  window.__errorQueuePrev = prevError;
  window.__errorQueueNext = nextError;

  const updateModalContent = () => {
    Modal.error({
      title: getModalTitle(),
      content: getModalContent(),
      closable: true,
      okText: i18n.global.t('que-ding'),
      width: 550,
      onOk: () => {
        errorQueue.clear();
        delete window.__errorQueuePrev;
        delete window.__errorQueueNext;
      },
      onCancel: () => {
        errorQueue.clear();
        delete window.__errorQueuePrev;
        delete window.__errorQueueNext;
      }
    });
  };

  updateModalContent();
}

function initErrorQueue() {
  errorQueue.setShowModalCallback((errors) => {
    showErrorQueueModal(errors);
  });
}

initErrorQueue();

export { showErrorQueueModal, initErrorQueue };
export default errorQueue;
