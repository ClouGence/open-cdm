<template>
  <div class="step-confirm">
    <h3>{{ $t('initialization.step.confirm') }}</h3>

    <div class="summary-section">
      <div v-for="item in summaryItems" :key="item.key" class="summary-item">
        <span class="summary-key">{{ item.key }}</span>
        <span class="summary-value">{{ item.value || '(empty)' }}</span>
      </div>
    </div>

    <div class="advanced-toggle">
      <a-switch :checked="advancedMode" @change="handleToggleAdvanced" size="small" />
      <span style="margin-left: 8px">{{ $t('initialization.advancedOptions') }}</span>
    </div>

    <div v-if="advancedMode" class="advanced-editor">
      <a-textarea
        :value="rawContent"
        @update:value="$emit('update:rawContent', $event)"
        :rows="20"
        :placeholder="$t('initialization.editConfig')"
        style="font-family: monospace; font-size: 13px"
      />
    </div>

    <div class="apply-section">
      <a-button type="primary" size="large" :loading="applying" @click="$emit('apply')">
        {{ $t('initialization.applyConfig') }}
      </a-button>
    </div>
  </div>
</template>

<script>
function isDatabaseEmpty(result) {
  return Boolean(result && (result.empty || result.isEmpty));
}

export default {
  name: 'StepConfirm',
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    dbTestResult: { type: Object, default: null },
    mode: { type: String, default: 'full' },
    advancedMode: { type: Boolean, default: false },
    rawContent: { type: String, default: '' },
    applying: { type: Boolean, default: false }
  },
  emits: ['update:advancedMode', 'update:rawContent', 'apply'],
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
        this.dbTestResult.databaseExists &&
        !isDatabaseEmpty(this.dbTestResult) &&
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
  },
  methods: {
    async handleToggleAdvanced(checked) {
      if (checked) {
        // 切换到高级模式前加载原始配置
        try {
          const res = await this.$services.dmInitRawConfig();
          if (res.success) {
            this.$emit('update:rawContent', res.data);
          }
        } catch (e) {
          console.error('Failed to load raw config', e);
        }
      }
      this.$emit('update:advancedMode', checked);
    }
  }
};
</script>

<style scoped>
.step-confirm h3 {
  margin-bottom: 24px;
}
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
.advanced-toggle {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}
.advanced-editor {
  margin-bottom: 24px;
}
.apply-section {
  text-align: center;
  margin-top: 32px;
}
</style>
