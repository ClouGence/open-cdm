import {
  isAmazonMsk,
  isDDLFilter,
  isDeltaLake,
  isDrDs,
  isES,
  isGrepTime,
  isHive,
  isHudi,
  isIceberg,
  isKafka,
  isKudu,
  isMongoDB,
  isMQ,
  isMySQL,
  isNoDb,
  isNoStruct,
  isOracle,
  isRagApi,
  isRedis,
  isRocketMQ,
  isStarRocks,
  isTDengine,
  isTdsqlCMySQL,
  isTdsqlMySQL
} from '@/utils';

export default {
  name: 'CommonMixin',
  methods: {
    // datasource
    isMongoDB,
    isIceberg,
    isStarRocks,
    isNoDb,
    isES,
    isHudi,
    isMQ,
    isOracle,
    isHive,
    isKudu,
    isMySQL,
    isRedis,
    isDDLFilter,
    isRocketMQ,
    isKafka,
    isNoStruct,
    isDrDs,
    isGrepTime,
    isTDengine,
    isRagApi,
    isAmazonMsk,
    isDeltaLake,
    isTdsqlMySQL,
    isTdsqlCMySQL,

    // util
    async copyText(value, msg = this.$t('fu-zhi-cheng-gong')) {
      try {
        const text = Array.isArray(value) ? value.join('') : value;

        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(text);
        } else {
          const textArea = document.createElement('textarea');
          textArea.style.position = 'fixed';
          textArea.style.opacity = '0';
          textArea.value = text;
          document.body.appendChild(textArea);
          textArea.select();
          const success = document.execCommand('copy');
          document.body.removeChild(textArea);

          if (!success) {
            throw new Error(this.$t('fu-zhi-shi-bai'));
          }
        }

        this.$Message.success({
          duration: 1.5,
          content: msg
        });
      } catch (err) {
        this.$Message.error({
          content: this.$t('fu-zhi-shi-bai')
        });
      }
    },
    async downloadLink(link, filename) {
      if (!link) {
        return;
      }

      try {
        // Use a tab if a filename is specified Load
        if (filename) {
          const a = document.createElement('a');
          a.href = link;
          a.download = filename;
          a.style.display = 'none';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        } else {
          // Open the link directly.
          window.open(link, '_blank');
        }
      } catch (err) {
        this.$Message.error({
          content: this.$t('xia-zai-shi-bai')
        });
      }
    }
  }
};
