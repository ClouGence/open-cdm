<template>
  <div class="step-confirm">
    <div class="tab-panel">
      <div class="summary-section">
        <div v-for="item in summaryItems" :key="item.key" class="summary-item">
          <span class="summary-key">{{ item.label }}</span>
          <span class="summary-value-cell">
            <EditOutlined v-if="item.editable" class="summary-edit-icon" />
            <template v-if="item.editable && isBooleanItem(item)">
              <a-switch
                class="summary-value-switch"
                :checked="item.rawValue === 'true'"
                :checked-children="$t('initialization.optionYes')"
                :un-checked-children="$t('initialization.optionNo')"
                @change="handleBooleanValueChange(item, $event)"
              />
            </template>
            <template v-else-if="item.editable">
              <input
                class="summary-value-input"
                :type="summaryInputType(item)"
                :value="item.rawValue"
                :placeholder="$t('initialization.emptyValue')"
                @input="handleValueInput(item, $event)"
              />
            </template>
            <span v-else-if="isEmptyValue(item.value)" class="summary-value summary-value-empty">{{ $t('initialization.emptyValue') }}</span>
            <span v-else class="summary-value">{{ item.value }}</span>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { EditOutlined } from '@ant-design/icons-vue';

const INIT_DB_REBUILD_IF_NOT_EMPTY = 'clougence.init.db.rebuildIfNotEmpty';
const UPGRADE_HIDDEN_FIELD_KEYS = new Set([
  'jwt.secret',
  'clougence.init.admin.account',
  'clougence.init.admin.email',
  'clougence.init.admin.password'
]);

export default {
  name: 'StepConfirm',
  components: { EditOutlined },
  emits: ['update:formValues'],
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    mode: { type: String, default: 'full' },
    workflowMode: { type: String, default: 'initial' }
  },
  computed: {
    summaryItems() {
      const items = this.fieldDefs
        .filter((field) => this.shouldShowField(field))
        .map((field) => ({
          key: field.propertyKey,
          label: this.resolveFieldLabel(field),
          rawValue: this.formValues[field.propertyKey] ?? '',
          value: this.formValues[field.propertyKey] ?? '',
          inputType: field.inputType || 'text',
          editable: true
        }));

      return items;
    }
  },
  methods: {
    resolveFieldLabel(field) {
      return field.label || field.description || field.propertyKey;
    },
    shouldShowField(field) {
      return !(this.workflowMode === 'upgrade' && field && UPGRADE_HIDDEN_FIELD_KEYS.has(field.propertyKey));
    },
    isEmptyValue(value) {
      return value === null || value === undefined || `${value}`.trim() === '';
    },
    isBooleanItem(item) {
      return item && item.inputType === 'boolean';
    },
    summaryInputType(item) {
      if (item && item.inputType === 'number') {
        return 'number';
      }

      if (item && item.inputType === 'password') {
        return 'password';
      }

      return 'text';
    },
    handleValueInput(item, event) {
      this.emitValueChange(item, event && event.target ? event.target.value : '');
    },
    handleBooleanValueChange(item, checked) {
      this.emitValueChange(item, checked ? 'true' : 'false');
    },
    emitValueChange(item, value) {
      if (!item || !item.editable) {
        return;
      }
      this.$emit('update:formValues', { [item.key]: value });
    }
  }
};
</script>

<style scoped>
.step-confirm {
  min-height: 0;
}
.tab-panel {
  max-height: calc(100dvh - 312px);
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  border: 1px solid var(--border-secondary);
  background: var(--bg-card);
  box-sizing: border-box;
}
.summary-section {
  margin-bottom: 0;
  background: transparent;
}
.summary-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border-secondary);
}
.summary-item:last-child {
  border-bottom: none;
}
.summary-key {
  display: inline-flex;
  align-items: center;
  font-weight: 500;
  width: 160px;
  min-height: 30px;
  flex-shrink: 0;
  color: var(--text-secondary);
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.summary-value {
  display: inline-flex;
  align-items: center;
  min-height: 30px;
  min-width: 0;
  color: var(--text-primary);
  word-break: break-all;
  font-size: 13px;
}
.summary-value-cell {
  flex: 1 1 0;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}
.summary-edit-icon {
  flex: 0 0 auto;
  color: var(--primary-color);
  font-size: 13px;
}
.summary-value-input {
  width: 100%;
  min-width: 0;
  border: 1px solid transparent;
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary);
  font-size: 13px;
  line-height: 22px;
  outline: none;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease;
}
.summary-value-input {
  height: 30px;
  padding: 3px 8px;
}
.summary-value-input:hover,
.summary-value-input:focus {
  border-color: var(--primary-color);
  background: var(--bg-hover);
}
.summary-value-empty {
  color: var(--text-tertiary);
}
@media (max-width: 768px) {
  .summary-key {
    width: 104px;
  }
}
</style>
