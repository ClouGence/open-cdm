import appLogger from '@/utils/logger';
const exportMixin = {
  data() {
    return {
      exportData: '',
      exportType: ''
    };
  },
  methods: {
    exportTableData(exportType, data, columns, indexes) {
      appLogger.debug(exportType, data, columns, indexes);

      this.exportType = exportType;
    }
  }
};

export default exportMixin;
