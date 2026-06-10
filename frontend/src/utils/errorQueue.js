/**
 * Error Queue Manager
 * To collect multiple errors and display them in one bullet window Medium
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
   * Add Error to Queue
   * @param {Object} Error - Wrong Object
   * @param {string} Error.title - Wrong Title
   * @param {string} Error
   * @param {string} Error. type - Error type (error/warning/info)
   * @param {Function} Error.onOK - confirm button echo
   * @param {string} Error.url - Request URL
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

    // Implement immediately if the cut-off has exceeded the last execution time
    if (timeSinceLastExecute >= this.throttleDelay) {
      this.lastExecuteTime = now;
      this.showModal();
      // Clear a possible timer
      if (this.throttleTimer) {
        clearTimeout(this.throttleTimer);
        this.throttleTimer = null;
      }
    } else {
      // Set the timer to be executed at the end of the window if it is still in the throttle window and the timer has not been set
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
