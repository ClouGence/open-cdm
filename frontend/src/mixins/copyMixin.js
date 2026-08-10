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
          document.body.appendChild(textArea);
          textArea.select();
          const copied = document.execCommand('copy');
          document.body.removeChild(textArea);
          if (!copied) {
            throw new Error(this.$t('fu-zhi-shi-bai'));
          }
        }
        if (msg) {
          this.$Message.success(msg);
        }
      } catch (error) {
        appLogger.error(this.$t('fu-zhi-shi-bai'), error);
        this.$Message.error(this.$t('fu-zhi-shi-bai'));
      }
    }
  }
};

export default copyMixin;
