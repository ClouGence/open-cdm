const copyMixin = {
  methods: {
    copyText(value, msg = this.$t('fu-zhi-cheng-gong')) {
      console.log(value);
      if (Array.isArray(value)) {
        value = value.join('');
      }
      const textArea = document.createElement('textarea');
      textArea.value = value;
      document.body.appendChild(textArea);
      textArea.select();
      if (document.execCommand('copy')) {
        document.execCommand('copy');
        document.body.removeChild(textArea);
        if (msg) {
          this.$Message.success({
            duration: 1.5,
            content: msg
          });
        }
      }
    }
  }
};

export default copyMixin;
