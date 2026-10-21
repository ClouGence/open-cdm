import appLogger from '@/utils/logger';

const copyMixin = {
  methods: {
    async copyText(value, msg = this.$t('fu-zhi-cheng-gong')) {
      const text = Array.isArray(value) ? value.join('') : String(value ?? '');
      try {
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(text);
        } else {
          const textArea = document.createElement('textarea');
          textArea.style.position = 'fixed';
          textArea.style.opacity = '0';
          textArea.value = text;
          const activeElement = document.activeElement;
          document.body.appendChild(textArea);
          try {
            textArea.select();
            if (!document.execCommand('copy')) {
              throw new Error(this.$t('fu-zhi-shi-bai'));
            }
          } finally {
            textArea.remove();
            activeElement?.focus({ preventScroll: true });
          }
        }
        if (msg) {
          this.$Message.success(msg);
        }
        return true;
      } catch (error) {
        appLogger.error(this.$t('fu-zhi-shi-bai'), error);
        this.$Message.error(this.$t('fu-zhi-shi-bai'));
        return false;
      }
    }
  }
};

export default copyMixin;
