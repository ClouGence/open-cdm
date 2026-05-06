<template>
  <div class="step-confirm">
    <div class="summary-section">
      <div v-for="item in summaryItems" :key="item.key" class="summary-item">
        <span class="summary-key">{{ item.key }}</span>
        <span class="summary-value">{{ item.value || '(empty)' }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StepConfirm',
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    dbTestResult: { type: Object, default: null },
    mode: { type: String, default: 'full' }
  },
  computed: {
    summaryItems() {
      const items = this.fieldDefs.map((field) => ({
        key: field.propertyKey,
        value: this.formValues[field.propertyKey] || ''
      }));

      if (this.formValues['clougence.init.db.createIfMissing'] === 'true') {
        items.push({
          key: this.$t('initialization.confirmCreateDatabase'),
          value: this.$t('initialization.optionYes')
        });
      }

      if (
        this.dbTestResult &&
        this.dbTestResult.showRebuildChoice &&
        ['true', 'false'].includes(this.formValues['clougence.init.db.rebuildIfNotEmpty'])
      ) {
        items.push({
          key: this.$t('initialization.confirmRebuildDatabase'),
          value:
            this.formValues['clougence.init.db.rebuildIfNotEmpty'] === 'true'
              ? this.$t('initialization.optionYes')
              : this.$t('initialization.optionNo')
        });
      }

      return items;
    }
  }
};
</script>

<style scoped>
.summary-section {
  background: #fafafa;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
  padding: 16px;
  margin-bottom: 24px;
}
.summary-item {
  display: flex;
  padding: 6px 0;
  border-bottom: 1px solid #f0f0f0;
}
.summary-item:last-child {
  border-bottom: none;
}
.summary-key {
  font-weight: 500;
  min-width: 280px;
  color: #595959;
  font-size: 13px;
}
.summary-value {
  color: #262626;
  word-break: break-all;
  font-size: 13px;
}
</style>
