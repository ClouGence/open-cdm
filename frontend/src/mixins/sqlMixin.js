import isEqual from 'lodash.isequal';

const sqlMixin = {
  methods: {
    goTicketPage(sql) {
      const { instanceId, database, schema } = this.tab;
      localStorage.setItem('instanceToTicket', instanceId);
      localStorage.setItem('databaseToTicket', database);
      localStorage.setItem('schemaToTicket', schema);
      localStorage.setItem('sqlToTicket', sql);
      this.$router.push({
        path: 'ticket_create'
      });
    },
    dataViewIsEditing(tab) {
      const hasAddOrDeleteRow = tab.addRows.length || tab.deleteRows.length;
      if (hasAddOrDeleteRow) {
        return true;
      }

      for (let rowIndex = 0; rowIndex < tab.updateCellList.length; rowIndex++) {
        const row = tab.updateCellList[rowIndex];
        const rowKeys = Object.keys(row);
        if (rowKeys.length) {
          return true;
        }
      }

      return false;
    },
    structViewIsEditing(tab) {
      console.log(isEqual(tab.originalFormData, tab.formData), tab.originalFormData, tab.formData);
      return !isEqual(tab.originalFormData, tab.formData);
    }
  }
};

export default sqlMixin;
