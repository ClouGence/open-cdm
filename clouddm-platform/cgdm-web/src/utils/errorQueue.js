/**
 * 错误队列管理器
 * 用于收集多个错误信息，统一显示在一个弹窗中
 */
import i18n from '@/i18n';

class ErrorQueue {
  constructor() {
    this.errors = [];
    this.showModalCallback = null;
    this.throttleTimer = null;
    this.throttleDelay = 300;
    this.lastExecuteTime = 0;
  }

  /**
   * 添加错误到队列
   * @param {Object} error - 错误对象
   * @param {string} error.title - 错误标题
   * @param {string} error.content - 错误内容
   * @param {string} error.type - 错误类型 (error/warning/info)
   * @param {Function} error.onOk - 确定按钮回调
   * @param {string} error.url - 请求 URL
   */
  addError(error) {
    const errorItem = {
      id: Date.now() + Math.random(),
      title: error.title || i18n.global.t('cuo-wu'),
      content: error.content || i18n.global.t('xi-tong-yi-chang'),
      type: error.type || 'error',
      onOk: error.onOk || null,
      url: error.url || ''
    };

    this.errors.push(errorItem);
    this.throttleShowModal();
  }

  throttleShowModal() {
    const now = Date.now();
    const timeSinceLastExecute = now - this.lastExecuteTime;

    // 如果距离上次执行时间已经超过节流间隔，立即执行
    if (timeSinceLastExecute >= this.throttleDelay) {
      this.lastExecuteTime = now;
      this.showModal();
      // 清除可能存在的定时器
      if (this.throttleTimer) {
        clearTimeout(this.throttleTimer);
        this.throttleTimer = null;
      }
    } else {
      // 如果还在节流窗口内，且还没有设置定时器，设置定时器在窗口结束时执行
      if (!this.throttleTimer) {
        const remainingTime = this.throttleDelay - timeSinceLastExecute;
        this.throttleTimer = setTimeout(() => {
          this.lastExecuteTime = Date.now();
          this.throttleTimer = null;
          this.showModal();
        }, remainingTime);
      }
    }
  }

  showModal() {
    if (this.errors.length === 0) {
      return;
    }

    if (this.showModalCallback) {
      const errorsCopy = [...this.errors];
      this.showModalCallback(errorsCopy);
    }
  }

  setShowModalCallback(callback) {
    this.showModalCallback = callback;
  }

  clear() {
    this.errors = [];
    if (this.throttleTimer) {
      clearTimeout(this.throttleTimer);
      this.throttleTimer = null;
    }
    this.lastExecuteTime = 0;
  }

  getErrorCount() {
    return this.errors.length;
  }
}

const errorQueue = new ErrorQueue();

export default errorQueue;
