<template>
  <div class="step-confirm">
    <a-tabs v-model:activeKey="activeTab" class="confirm-tabs">
      <a-tab-pane key="database" :tab="$t('initialization.confirmTabDatabase')">
        <div class="tab-panel">
          <div class="summary-section">
            <div v-for="item in summaryItems" :key="item.key" class="summary-item">
              <span class="summary-key">{{ item.label }}</span>
              <span v-if="isEmptyValue(item.value)" class="summary-value summary-value-empty">{{ $t('initialization.emptyValue') }}</span>
              <span v-else class="summary-value">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </a-tab-pane>

      <a-tab-pane key="scripts" :tab="$t('initialization.executionScripts')">
        <div class="tab-panel">
          <div class="summary-section">
            <div v-if="scriptNames.length" class="script-list">
              <div v-for="scriptName in scriptNames" :key="scriptName" class="script-item">{{ scriptName }}</div>
            </div>
            <div v-else class="summary-empty">{{ $t('initialization.noExecutionScripts') }}</div>
          </div>
        </div>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
export default {
  name: 'StepConfirm',
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    dbTestResult: { type: Object, default: null },
    mode: { type: String, default: 'full' },
    workflowMode: { type: String, default: 'initial' },
    executionScripts: { type: Array, default: () => [] }
  },
  data() {
    return {
      activeTab: 'database'
    };
  },
  computed: {
    scriptNames() {
      return this.executionScripts.map((item) => (typeof item === 'string' ? item : item && item.scriptName)).filter(Boolean);
    },
    summaryItems() {
      const items = this.fieldDefs.map((field) => ({
        key: field.propertyKey,
        label: this.resolveFieldLabel(field),
        value: this.formValues[field.propertyKey] || ''
      }));

      if (this.formValues['clougence.init.db.createIfMissing'] === 'true') {
        items.push({
          key: this.$t('initialization.confirmCreateDatabase'),
          label: this.$t('initialization.confirmCreateDatabase'),
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
          label: this.$t('initialization.confirmRebuildDatabase'),
          value:
            this.formValues['clougence.init.db.rebuildIfNotEmpty'] === 'true'
              ? this.$t('initialization.optionYes')
              : this.$t('initialization.optionNo')
        });
      }

      return items;
    }
  },
  methods: {
    resolveFieldLabel(field) {
      return field.label || field.description || field.propertyKey;
    },
    isEmptyValue(value) {
      return value === null || value === undefined || `${value}`.trim() === '';
    }
  }
};
</script>

<style scoped>
.step-confirm {
  height: 100%;
  min-height: 0;
}
.confirm-tabs {
  height: 100%;
}
.confirm-tabs :deep(.ant-tabs-nav) {
  margin-bottom: 0;
}
.confirm-tabs :deep(.ant-tabs-content-holder) {
  height: 100%;
  min-height: 0;
}
.confirm-tabs :deep(.ant-tabs-content) {
  height: 100%;
}
.confirm-tabs :deep(.ant-tabs-tabpane) {
  height: 100%;
}
.confirm-tabs :deep(.ant-tabs-tabpane-active) {
  height: 100%;
}
.tab-panel {
  height: 100%;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  border: 1px solid #f0f0f0;
  border-top: none;
  background: #fff;
  box-sizing: border-box;
}
.summary-section {
  height: 100%;
  margin-bottom: 0;
  background: transparent;
}
.summary-item {
  display: flex;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}
.summary-item:last-child {
  border-bottom: none;
}
.summary-key {
  font-weight: 500;
  width: 280px;
  flex-shrink: 0;
  color: #595959;
  font-size: 13px;
}
.summary-value {
  min-width: 0;
  color: #262626;
  word-break: break-all;
  font-size: 13px;
}
.summary-value-empty {
  color: #bfbfbf;
}
.summary-empty {
  color: #8c8c8c;
  font-size: 13px;
}
.script-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}
.script-item {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #262626;
  font-size: 13px;
  word-break: break-all;
}
.script-item:last-child {
  border-bottom: none;
}
.summary-empty {
  padding: 16px;
}
@media (max-width: 768px) {
  .summary-key {
    width: 180px;
  }
}
</style>
