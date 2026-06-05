import {
  CHECK_MODE,
  getColumnMapping,
  isAmazonMsk,
  isDDLFilter,
  isDeltaLake,
  isDrDs,
  isES,
  isGrepTime,
  isHasSchema,
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
  isTdsqlMySQL,
  JOB_MODE,
  JOB_TYPE
} from '@/utils';
import DataSourceGroup from '@/views/dataSourceGroup.json';

export default {
  name: 'CommonMixin',
  methods: {
    getColumnMapping,
    // mode type
    isEditMode: (mode) => [JOB_MODE.EDIT, JOB_MODE.FULL_EDIT].includes(mode),
    isCreateMode: (mode) => [JOB_MODE.CREATE, JOB_MODE.SIMILAR].includes(mode),

    // job type
    isSyncJob: (type) => type === JOB_TYPE.SYNC,
    isMigrationJob: (type) => type === JOB_TYPE.MIGRATION,
    isReviseJob: (type) => type === JOB_TYPE.REVISE,
    isCheckJob: (type) => type === JOB_TYPE.CHECK,
    isStructMigrationJob: (type) => type === JOB_TYPE.STRUCT_MIGRATION,

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
    isHasSchema,
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

    // check mode
    isCheckOneMode: (mode) => mode === CHECK_MODE.CHECK_ONCE,
    isCheckPeriodMode: (mode) => mode === CHECK_MODE.CHECK_PERIOD,
    isNoCheckMode: (mode) => mode === CHECK_MODE.NO_CHECK,

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
        console.error(this.$t('fu-zhi-shi-bai'), err);
        this.$Message.error({
          content: this.$t('fu-zhi-shi-bai')
        });
      }
    },
    async downloadLink(link, filename) {
      if (!link) {
        console.error('下载链接不能为空');
        return;
      }

      try {
        // 如果指定了文件名，使用 a 标签下载
        if (filename) {
          const a = document.createElement('a');
          a.href = link;
          a.download = filename;
          a.style.display = 'none';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        } else {
          // 否则直接打开链接
          window.open(link, '_blank');
        }
      } catch (err) {
        console.error('下载失败:', err);
        this.$Message.error({
          content: this.$t('xia-zai-shi-bai')
        });
      }
    }
  }
};
