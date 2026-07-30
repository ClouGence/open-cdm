<template>
  <div class="table-editor-form-panel" :class="{ 'table-editor-form-panel--standalone': panelKey === 'tableInfo' }">
    <div class="table-editor-form-grid">
      <div v-for="fieldSchema in renderableFields" :key="fieldSchema.field" :data-editor-field="fieldSchema.field">
        <CreateTableItem
          :current-schema="fieldSchema"
          :form-data="tab.formData"
          :node-type="panelKey"
          :selected-index="selectedIndex"
          :selected-node="tab.selectedNode || {}"
          :tab="tab"
        />
      </div>
    </div>
  </div>
</template>

<script>
import CreateTableItem from '@/components/modal/CreateTableItem';
import { buildRenderableFields } from './tableEditorUtils';

export default {
  name: 'TableEditorFormPanel',
  components: {
    CreateTableItem
  },
  props: {
    tab: {
      type: Object,
      required: true
    },
    panelKey: {
      type: String,
      required: true
    },
    panelSchema: {
      type: Object,
      required: true
    },
    selectedIndex: {
      type: Number,
      default: -1
    }
  },
  computed: {
    selectedData() {
      if (this.panelKey === 'tableInfo') {
        return this.tab.formData.tableInfo;
      }
      return this.tab.formData[this.panelKey]?.[this.selectedIndex] || {};
    },
    renderableFields() {
      const fields = buildRenderableFields(this.panelSchema, this.selectedData);
      if (this.panelKey === 'columns') {
        const commentField = fields.find((fieldSchema) => fieldSchema.field === 'comment');
        if (commentField) {
          commentField.titleI18N = this.$t('bei-zhu');
        }
      }
      return fields;
    }
  }
};
</script>

<style scoped lang="less">
.table-editor-form-panel {
  box-sizing: border-box;
  width: 100%;
  max-width: 1120px;
  padding: 24px;
  container-type: inline-size;
}

.table-editor-form-panel--standalone {
  width: 100%;
  max-width: 1120px;
  align-self: flex-start;
  padding: 16px 32px 32px;
}

.table-editor-form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(280px, 1fr));
  gap: 20px 48px;
  align-items: start;
}

.table-editor-form-panel--standalone .table-editor-form-grid {
  max-width: 960px;
  grid-template-columns: minmax(0, 1fr);
  gap: 24px;
}

.table-editor-form-grid > div {
  min-width: 0;
}

.table-editor-form-grid :deep(> div > div) {
  margin-top: 0 !important;
  align-items: flex-start;
}

.table-editor-form-grid :deep(> div > div > div:first-child) {
  min-width: 120px !important;
  padding-top: 7px;
  color: var(--text-primary);
  line-height: 20px !important;
}

.table-editor-form-grid :deep(.create-table-item--nested) {
  margin-left: -120px !important;
}

.table-editor-form-grid :deep(.create-table-item--nested > div:first-child) {
  min-width: 120px !important;
  padding-top: 7px;
  color: var(--text-primary);
  line-height: 20px !important;
}

.table-editor-form-panel--standalone .table-editor-form-grid :deep(> div > div > div:first-child),
.table-editor-form-panel--standalone .table-editor-form-grid :deep(.create-table-item--nested > div:first-child) {
  min-width: 136px !important;
}

.table-editor-form-panel--standalone .table-editor-form-grid :deep(.create-table-item--nested) {
  margin-top: 24px !important;
  margin-left: -136px !important;
}

.table-editor-form-grid :deep(.ant-input),
.table-editor-form-grid :deep(.ant-select-selector) {
  min-height: 36px;
  border-radius: 6px !important;
}

.table-editor-form-grid :deep(.ant-select) {
  min-width: 0;
}

.table-editor-form-grid :deep(.anticon-question-circle) {
  flex: 0 0 auto;
  margin-left: 16px !important;
}

@container (max-width: 1000px) {
  .table-editor-form-grid {
    grid-template-columns: minmax(0, 1fr);
  }
}
</style>
