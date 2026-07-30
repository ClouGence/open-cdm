<template>
  <div class="table-editor-entity-panel">
    <div class="entity-toolbar">
      <a-button type="primary" class="entity-add-button" :disabled="!canAddItem" @click="addItem">
        <PlusOutlined />
        <span class="entity-add-button__label">{{ $t('xin-zeng') }}</span>
      </a-button>
      <a-button :disabled="selectedIndex < 0" @click="deleteItem">
        <CustomIcon type="icon-v2-Delete2" size="14" right-margin />
        {{ $t('shan-chu') }}
      </a-button>
      <a-button :disabled="selectedIndex < 0" @click="openDetails(selectedIndex)">
        {{ $t('bian-ji') }}
      </a-button>
    </div>

    <div class="entity-table-wrap">
      <table class="entity-table">
        <thead>
          <tr>
            <th class="entity-table__order">{{ $t('table-editor-order') }}</th>
            <th v-for="column in entityColumns" :key="column.key" :style="{ width: column.width }">
              {{ column.label }}
            </th>
            <th class="entity-table__actions">{{ $t('cao-zuo') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(item, index) in items"
            :key="item.key"
            class="entity-table__row"
            :class="{ 'entity-table__row--selected': index === selectedIndex }"
            tabindex="0"
            :aria-selected="index === selectedIndex"
            @click="selectItem(index)"
            @dblclick="openDetails(index)"
            @keydown.enter.prevent="openDetails(index)"
          >
            <td class="entity-table__order">{{ index + 1 }}</td>
            <td v-for="column in entityColumns" :key="column.key" :title="entityCellValue(item, column.key)">
              <span class="entity-table__value">{{ entityCellValue(item, column.key) || $t('table-editor-empty-value') }}</span>
            </td>
            <td class="entity-table__actions">
              <a-button type="text" @click.stop="openDetails(index)">{{ $t('xiang-qing') }}</a-button>
            </td>
          </tr>
          <tr v-if="!items.length">
            <td :colspan="entityColumns.length + 2" class="entity-table__empty">
              {{ $t('table-editor-empty-panel') }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <section v-if="detailsVisible && selectedIndex >= 0" class="entity-detail">
      <div class="entity-detail__header">
        <div>
          <div class="entity-detail__title">{{ $t('pei-zhi-xiang-qing') }}</div>
          <div v-if="entitySummary(selectedItem)" class="entity-detail__summary">{{ entitySummary(selectedItem) }}</div>
        </div>
        <a-button type="text" @click="detailsVisible = false">{{ $t('table-editor-hide-details') }}</a-button>
      </div>
      <TableEditorFormPanel :key="selectedItem.key" :tab="tab" :panel-key="panelKey" :panel-schema="panelSchema" :selected-index="selectedIndex" />
    </section>
  </div>
</template>

<script>
import { PlusOutlined } from '@ant-design/icons-vue';
import TableEditorFormPanel from './TableEditorFormPanel';
import { createEditorItem } from './tableEditorUtils';

export default {
  name: 'TableEditorEntityPanel',
  components: {
    PlusOutlined,
    TableEditorFormPanel
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
    }
  },
  data() {
    return {
      selectedIndex: -1,
      detailsVisible: false
    };
  },
  computed: {
    items() {
      return this.tab.formData[this.panelKey] || [];
    },
    selectedItem() {
      return this.items[this.selectedIndex] || {};
    },
    canAddItem() {
      return !['keys', 'partitions'].includes(this.panelKey) || this.items.length === 0;
    },
    entityColumns() {
      if (this.panelKey === 'indexes') {
        return [
          { key: 'name', label: this.$t('table-editor-entity-name'), width: '18%' },
          { key: 'columns', label: this.$t('table-editor-included-columns'), width: '28%' },
          { key: 'indexType', label: this.$t('table-editor-index-type'), width: '14%' },
          { key: 'indexMethod', label: this.$t('table-editor-index-method'), width: '14%' },
          { key: 'comment', label: this.$t('bei-zhu'), width: '18%' }
        ];
      }
      if (this.panelKey === 'foreignKeys') {
        return [
          { key: 'name', label: this.$t('table-editor-entity-name'), width: '18%' },
          { key: 'localColumns', label: this.$t('table-editor-local-columns'), width: '20%' },
          { key: 'referenceTarget', label: this.$t('table-editor-reference-target'), width: '22%' },
          { key: 'referenceColumns', label: this.$t('table-editor-reference-columns'), width: '18%' },
          { key: 'onDelete', label: this.$t('table-editor-on-delete'), width: '11%' },
          { key: 'onUpdate', label: this.$t('table-editor-on-update'), width: '11%' }
        ];
      }
      if (this.panelKey === 'constraints') {
        return [
          { key: 'name', label: this.$t('table-editor-entity-name'), width: '24%' },
          { key: 'constraintType', label: this.$t('table-editor-configuration-type'), width: '20%' },
          { key: 'constraintContent', label: this.$t('table-editor-configuration-content'), width: '56%' }
        ];
      }
      if (this.panelKey === 'partitions') {
        return [
          { key: 'partitionType', label: this.$t('table-editor-configuration-type'), width: '28%' },
          { key: 'partitionContent', label: this.$t('table-editor-configuration-content'), width: '72%' }
        ];
      }
      return [
        { key: 'name', label: this.$t('table-editor-entity-name'), width: '34%' },
        { key: 'summary', label: this.$t('table-editor-configuration-content'), width: '66%' }
      ];
    }
  },
  watch: {
    panelKey: {
      handler() {
        this.detailsVisible = false;
        this.selectInitialItem();
      },
      immediate: true
    },
    'items.length'() {
      if (this.selectedIndex >= this.items.length) {
        this.selectedIndex = this.items.length - 1;
      }
      if (this.items.length && this.selectedIndex < 0) {
        this.selectItem(0);
      }
      if (!this.items.length) {
        this.detailsVisible = false;
      }
    },
    'tab.validationTarget': {
      handler(target) {
        if (target?.panelKey !== this.panelKey || !Number.isInteger(target.rowIndex) || !this.items[target.rowIndex]) {
          return;
        }
        this.selectItem(target.rowIndex);
        this.detailsVisible = true;
        this.$nextTick(() => {
          const field = Array.from(this.$el.querySelectorAll('[data-editor-field]')).find((element) => element.dataset.editorField === target.field);
          field?.scrollIntoView({ block: 'center', inline: 'nearest' });
          field?.querySelector('input:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])')?.focus();
        });
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    selectInitialItem() {
      if (this.items.length) {
        this.selectItem(0);
        return;
      }
      this.selectedIndex = -1;
    },
    selectItem(index) {
      this.selectedIndex = index;
      this.tab.nodeType = this.panelKey;
      this.tab.selectedIndex = index;
      this.tab.selectedNode = {
        key: this.items[index]?.key
      };
    },
    openDetails(index) {
      if (index < 0 || !this.items[index]) {
        return;
      }
      this.selectItem(index);
      this.detailsVisible = true;
    },
    addItem() {
      if (!this.canAddItem) {
        return;
      }
      const item = createEditorItem(this.panelKey, this.panelSchema);
      if (item.name) {
        const baseName = item.name;
        const existingNames = new Set(this.items.map((entry) => entry.name));
        let suffix = 1;
        while (existingNames.has(item.name)) {
          item.name = `${baseName}_${suffix}`;
          suffix += 1;
        }
      }
      this.detailsVisible = false;
      this.items.push(item);
      this.$nextTick(() => {
        this.selectItem(this.items.length - 1);
      });
    },
    deleteItem() {
      if (this.selectedIndex < 0) {
        return;
      }
      this.items.splice(this.selectedIndex, 1);
      if (this.items.length) {
        this.selectItem(Math.min(this.selectedIndex, this.items.length - 1));
        return;
      }
      this.selectedIndex = -1;
      this.tab.selectedIndex = -1;
      this.tab.selectedNode = null;
      this.detailsVisible = false;
    },
    entityCellValue(item, key) {
      const joinColumns = (columns, field = 'name') =>
        (columns || [])
          .map((column) => column?.[field])
          .filter(Boolean)
          .join(', ');
      const firstValue = (...values) => values.find((value) => value !== undefined && value !== null && value !== '') || '';

      if (key === 'name') {
        return item.name || '';
      }
      if (key === 'columns') {
        return joinColumns(item.columns);
      }
      if (key === 'indexType') {
        return firstValue(item.indexType, item.type);
      }
      if (key === 'indexMethod') {
        return firstValue(item.indexMethod, item.method, item.algorithm);
      }
      if (key === 'comment') {
        return firstValue(item.comment, item.description);
      }
      if (key === 'localColumns') {
        return joinColumns(item.relation);
      }
      if (key === 'referenceTarget') {
        return [
          firstValue(item.referencedCatalog, item.referenceCatalog),
          firstValue(item.referencedSchema, item.referenceSchema),
          item.referencedTable
        ]
          .filter(Boolean)
          .join('.');
      }
      if (key === 'referenceColumns') {
        return joinColumns(item.relation, 'referenceColumnName');
      }
      if (key === 'onDelete') {
        return firstValue(item.onDelete, item.deleteRule);
      }
      if (key === 'onUpdate') {
        return firstValue(item.onUpdate, item.updateRule);
      }
      if (key === 'constraintType') {
        return firstValue(item.constraintType, item.type);
      }
      if (key === 'constraintContent') {
        return firstValue(item.expression, joinColumns(item.columns), this.entitySummary(item));
      }
      if (key === 'partitionType') {
        return firstValue(item.partitionType, item.type);
      }
      if (key === 'partitionContent') {
        return firstValue(item.partitionExpression, item.expression, this.entitySummary(item));
      }
      if (key === 'summary') {
        return this.entitySummary(item);
      }
      return firstValue(item[key]);
    },
    entitySummary(item) {
      if (this.panelKey === 'foreignKeys') {
        const relations = (item.relation || []).map((relation) => `${relation.name || ''} → ${relation.referenceColumnName || ''}`).join(', ');
        const target = [item.referencedSchema, item.referencedTable].filter(Boolean).join('.');
        return [relations, target].filter(Boolean).join(' · ');
      }
      if (this.panelKey === 'indexes') {
        const columns = (item.columns || [])
          .map((column) => column.name)
          .filter(Boolean)
          .join(', ');
        return [item.type, columns].filter(Boolean).join(' · ');
      }
      if (this.panelKey === 'constraints') {
        const columns = (item.columns || [])
          .map((column) => column.name)
          .filter(Boolean)
          .join(', ');
        return [item.type, item.expression, columns].filter(Boolean).join(' · ');
      }
      if (this.panelKey === 'partitions') {
        return [item.partitionType, item.partitionExpression].filter(Boolean).join(' · ');
      }
      return '';
    }
  }
};
</script>

<style scoped lang="less">
.table-editor-entity-panel {
  display: flex;
  flex: 1;
  width: 100%;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  gap: 12px;
  overflow: auto;
  background: var(--bg-primary);
}

.entity-toolbar {
  display: flex;
  flex: 0 0 auto;
  flex-wrap: wrap;
  gap: 8px;
  padding: 16px 20px 0;
}

.entity-add-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  line-height: 20px;
}

.entity-add-button :deep(.anticon) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 0 !important;
  line-height: 1;
  vertical-align: 0;
}

.entity-add-button :deep(.anticon svg) {
  display: block;
}

.entity-add-button :deep(.anticon + span) {
  margin-left: 0;
}

.entity-add-button__label {
  line-height: 20px;
}

.entity-table-wrap {
  min-width: 0;
  flex: 0 0 auto;
  overflow: auto;
  margin: 0 20px;
  border: 1px solid var(--border-light);
  border-radius: 8px;
}

.entity-table {
  width: 100%;
  min-width: 860px;
  border-collapse: separate;
  border-spacing: 0;
  table-layout: fixed;
  color: var(--text-primary);
}

.entity-table th {
  position: sticky;
  top: 0;
  z-index: 1;
  height: 44px;
  padding: 10px 14px;
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 600;
  line-height: 20px;
  text-align: left;
  vertical-align: middle;
  white-space: nowrap;
}

.entity-table td {
  height: 52px;
  padding: 8px 14px;
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-primary);
  vertical-align: middle;
}

.entity-table__row:last-child td {
  border-bottom: 0;
}

.entity-table__row {
  cursor: pointer;
  outline: 0;
}

.entity-table__row:hover td {
  background: var(--bg-secondary);
}

.entity-table__row--selected td {
  background: var(--bg-hover);
}

.entity-table__row--selected td:first-child {
  box-shadow: inset 3px 0 var(--primary-color);
}

.entity-table__row:focus-visible td {
  outline: 1px solid var(--primary-color);
  outline-offset: -1px;
}

.entity-table__order {
  width: 64px;
  color: var(--text-tertiary);
  text-align: center !important;
}

.entity-table__actions {
  width: 72px;
  text-align: center !important;
}

.entity-table__value {
  display: block;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.entity-table__empty {
  height: 180px !important;
  color: var(--text-secondary);
  text-align: center;
}

.entity-detail {
  min-width: 0;
  flex: 0 0 auto;
  margin: 0 20px 20px;
  background: var(--bg-secondary);
}

.entity-detail__header {
  display: flex;
  min-height: 54px;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 8px 16px;
}

.entity-detail__title {
  color: var(--text-primary);
  font-weight: 500;
}

.entity-detail__summary {
  max-width: 760px;
  overflow: hidden;
  color: var(--text-tertiary);
  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.entity-detail :deep(.table-editor-form-panel) {
  max-width: none;
  padding: 20px;
}

@media (max-width: 768px) {
  .entity-toolbar {
    padding: 12px 16px 0;
  }

  .entity-table-wrap {
    margin: 0 16px;
  }

  .entity-detail {
    margin: 0 16px 16px;
  }
}
</style>
