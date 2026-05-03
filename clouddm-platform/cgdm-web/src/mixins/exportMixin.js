const exportMixin = {
  data() {
    return {
      exportData: '',
      exportType: ''
    };
  },
  methods: {
    exportTableData(exportType, data, columns, indexes) {
      console.log(exportType, data, columns, indexes);

      this.exportType = exportType;
    }
  }
};

export default exportMixin;
