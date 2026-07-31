<template>
  <div class="table-editor-columns-panel">
    <div class="columns-toolbar">
      <div class="columns-toolbar__actions">
        <a-button type="primary" class="column-add-button" @click="addColumn">
          <PlusOutlined />
          <span class="column-add-button__label">{{ $t('table-editor-add-column') }}</span>
        </a-button>
        <a-button :disabled="selectedRowIndex < 0" @click="requestDeleteSelected">
          <CustomIcon type="icon-v2-Delete2" size="14" right-margin />
          {{ $t('shan-chu') }}
        </a-button>
        <a-button :disabled="!canUndoSelected" @click="undoSelected">{{ $t('table-editor-undo-column-changes') }}</a-button>
        <a-button :disabled="!canMoveUp" @click="moveSelected(-1)">{{ $t('table-editor-move-up') }}</a-button>
        <a-button :disabled="!canMoveDown" @click="moveSelected(1)">{{ $t('table-editor-move-down') }}</a-button>
      </div>
      <div class="columns-toolbar__tools">
        <span class="columns-toolbar__count">{{ $t('table-editor-column-count', [rows.length]) }}</span>
        <a-input v-model:value="searchText" class="columns-toolbar__search" allow-clear :placeholder="$t('table-editor-search-column')" />
      </div>
    </div>

    <div class="columns-table-wrap">
      <table class="columns-table">
        <thead>
          <tr>
            <th class="column-order">{{ $t('table-editor-order') }}</th>
            <th class="column-name">{{ $t('table-editor-column-name') }}</th>
            <th class="column-type">{{ $t('table-editor-data-type') }}</th>
            <th class="column-length">{{ $t('table-editor-length') }}</th>
            <th class="column-precision">{{ $t('table-editor-precision-date-precision') }}</th>
            <th v-if="keysSchema" class="column-check">{{ $t('table-editor-primary-key') }}</th>
            <th v-if="fieldSchemas.notNull" class="column-check">{{ $t('table-editor-not-null') }}</th>
            <th v-if="fieldSchemas.autoIncrement" class="column-check">{{ $t('table-editor-auto-increment') }}</th>
            <th v-if="fieldSchemas.defaultOpt || fieldSchemas.default" class="column-default">
              {{ $t('table-editor-default-value') }}
            </th>
            <th v-if="fieldSchemas.comment" class="column-comment">{{ $t('table-editor-comment') }}</th>
            <th class="column-actions">{{ $t('cao-zuo') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="entry in filteredRows" :key="entry.row.key">
            <tr
              class="columns-table__row"
              :class="{
                'columns-table__row--selected': entry.index === selectedRowIndex,
                'columns-table__row--error': rowError(entry.row)
              }"
              :data-row-index="entry.index"
              @click="selectRow(entry.index)"
            >
              <td class="column-order">
                <span v-if="rowError(entry.row)" class="column-error-indicator" :title="rowError(entry.row)" :aria-label="rowError(entry.row)">
                  {{ $t('table-editor-error-indicator') }}
                </span>
                <span v-else>{{ entry.index + 1 }}</span>
              </td>
              <td :class="{ 'column-cell--error': columnError(entry.row, 'name') }">
                <a-input
                  v-if="entry.index === selectedRowIndex"
                  v-model:value="entry.row.name"
                  data-editor-field="name"
                  :disabled="isReadOnly(fieldSchemas.name, entry.row)"
                  :title="columnError(entry.row, 'name')"
                  @focus="beginCellEdit(entry.row, entry.index, 'name')"
                  @change="finishRename(entry.row)"
                  @keydown="handleCellKeydown($event, entry.row, entry.index, 'name')"
                />
                <button
                  v-else
                  type="button"
                  class="cell-edit-trigger cell-edit-trigger--mono"
                  :title="entry.row.name"
                  @click.stop="activateCell(entry.index, 'name')"
                >
                  {{ entry.row.name || $t('table-editor-empty-value') }}
                </button>
              </td>
              <td :class="{ 'column-cell--error': columnError(entry.row, 'columnType') }">
                <a-select
                  v-if="entry.index === selectedRowIndex"
                  v-model:value="entry.row.columnType"
                  data-editor-field="columnType"
                  show-search
                  :disabled="isReadOnly(fieldSchemas.columnType, entry.row)"
                  :title="columnError(entry.row, 'columnType')"
                  @focus="beginCellEdit(entry.row, entry.index, 'columnType')"
                  @change="handleTypeChange(entry.row, $event)"
                  @keydown.esc.stop="cancelCellEdit(entry.row, 'columnType')"
                >
                  <a-select-option v-for="option in typeOptions" :key="option.value" :value="option.value">
                    {{ option.label }}
                  </a-select-option>
                </a-select>
                <button
                  v-else
                  type="button"
                  class="cell-edit-trigger cell-edit-trigger--mono"
                  :title="entry.row.columnType"
                  @click.stop="activateCell(entry.index, 'columnType')"
                >
                  {{ entry.row.columnType || $t('table-editor-empty-value') }}
                </button>
              </td>
              <td class="column-length" :class="{ 'column-cell--error': columnError(entry.row, 'length') }">
                <div v-if="entry.index === selectedRowIndex" class="single-type-parameter-editor">
                  <a-input
                    v-if="activeTypeField(entry.row, 'length')"
                    v-model:value="entry.row.length"
                    data-editor-field="length"
                    :disabled="isReadOnly(typeFieldSchema(entry.row, 'length'), entry.row)"
                    :title="columnError(entry.row, 'length')"
                    @focus="beginCellEdit(entry.row, entry.index, 'length')"
                    @keydown="handleCellKeydown($event, entry.row, entry.index, 'length')"
                  />
                  <span v-else class="cell-empty">{{ $t('table-editor-empty-value') }}</span>
                </div>
                <button
                  v-else
                  type="button"
                  class="cell-edit-trigger"
                  :disabled="!activeTypeField(entry.row, 'length')"
                  :title="typeParameterValue(entry.row, 'length')"
                  @click.stop="activateCell(entry.index, 'length')"
                >
                  {{ typeParameterValue(entry.row, 'length') }}
                </button>
              </td>
              <td
                class="column-precision"
                :class="{
                  'column-cell--error':
                    columnError(entry.row, 'numericPrecision') || columnError(entry.row, 'numericScale') || columnError(entry.row, 'datePrecision')
                }"
              >
                <div v-if="entry.index === selectedRowIndex" class="type-parameter-editor">
                  <label v-if="activeTypeField(entry.row, 'numericPrecision')" class="type-parameter-editor__item">
                    <span>{{ $t('table-editor-precision') }}</span>
                    <a-input
                      v-model:value="entry.row.numericPrecision"
                      data-editor-field="numericPrecision"
                      :disabled="isReadOnly(typeFieldSchema(entry.row, 'numericPrecision'), entry.row)"
                      :title="columnError(entry.row, 'numericPrecision')"
                      @focus="beginCellEdit(entry.row, entry.index, 'numericPrecision')"
                      @keydown="handleCellKeydown($event, entry.row, entry.index, 'numericPrecision')"
                    />
                  </label>
                  <label v-if="activeTypeField(entry.row, 'numericScale')" class="type-parameter-editor__item">
                    <span>{{ $t('table-editor-scale') }}</span>
                    <a-input
                      v-model:value="entry.row.numericScale"
                      data-editor-field="numericScale"
                      :disabled="isReadOnly(typeFieldSchema(entry.row, 'numericScale'), entry.row)"
                      :title="columnError(entry.row, 'numericScale')"
                      @focus="beginCellEdit(entry.row, entry.index, 'numericScale')"
                      @keydown="handleCellKeydown($event, entry.row, entry.index, 'numericScale')"
                    />
                  </label>
                  <label v-if="activeTypeField(entry.row, 'datePrecision')" class="type-parameter-editor__item">
                    <span>{{ $t('table-editor-date-precision') }}</span>
                    <a-input
                      v-model:value="entry.row.datePrecision"
                      data-editor-field="datePrecision"
                      :disabled="isReadOnly(typeFieldSchema(entry.row, 'datePrecision'), entry.row)"
                      :title="columnError(entry.row, 'datePrecision')"
                      @focus="beginCellEdit(entry.row, entry.index, 'datePrecision')"
                      @keydown="handleCellKeydown($event, entry.row, entry.index, 'datePrecision')"
                    />
                  </label>
                  <span v-if="!hasPrecisionParameters(entry.row)" class="cell-empty">{{ $t('table-editor-empty-value') }}</span>
                </div>
                <button
                  v-else
                  type="button"
                  class="cell-edit-trigger"
                  :disabled="!hasPrecisionParameters(entry.row)"
                  :title="precisionParameterSummary(entry.row)"
                  @click.stop="activateCell(entry.index, firstPrecisionParameterField(entry.row))"
                >
                  {{ precisionParameterSummary(entry.row) }}
                </button>
              </td>
              <td v-if="keysSchema" class="column-check">
                <a-checkbox
                  :checked="isPrimaryColumn(entry.row)"
                  @focus="selectRow(entry.index)"
                  @change="togglePrimaryColumn(entry.row, $event.target.checked)"
                />
              </td>
              <td v-if="fieldSchemas.notNull" class="column-check">
                <a-checkbox
                  v-model:checked="entry.row.notNull"
                  :disabled="isReadOnly(fieldSchemas.notNull, entry.row) || isPrimaryColumn(entry.row)"
                  @focus="selectRow(entry.index)"
                />
              </td>
              <td v-if="fieldSchemas.autoIncrement" class="column-check">
                <a-checkbox
                  v-model:checked="entry.row.autoIncrement"
                  :disabled="!activeTypeField(entry.row, 'autoIncrement') || isReadOnly(typeFieldSchema(entry.row, 'autoIncrement'), entry.row)"
                  @focus="selectRow(entry.index)"
                />
              </td>
              <td v-if="fieldSchemas.defaultOpt || fieldSchemas.default">
                <div v-if="entry.index === selectedRowIndex" class="column-default-editor">
                  <a-select
                    v-if="fieldSchemas.defaultOpt"
                    v-model:value="entry.row.defaultOpt"
                    data-editor-field="defaultOpt"
                    :disabled="isReadOnly(fieldSchemas.defaultOpt, entry.row)"
                    @focus="beginCellEdit(entry.row, entry.index, 'defaultOpt')"
                    @keydown.esc.stop="cancelCellEdit(entry.row, 'defaultOpt')"
                  >
                    <a-select-option v-for="option in defaultOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </a-select-option>
                  </a-select>
                  <a-input
                    v-if="!fieldSchemas.defaultOpt || entry.row.defaultOpt === 'CUSTOM'"
                    v-model:value="entry.row.default"
                    data-editor-field="default"
                    :disabled="isReadOnly(fieldSchemas.default, entry.row)"
                    :placeholder="$t('table-editor-default-value')"
                    @focus="beginCellEdit(entry.row, entry.index, 'default')"
                    @keydown="handleCellKeydown($event, entry.row, entry.index, 'default')"
                  />
                </div>
                <button
                  v-else
                  type="button"
                  class="cell-edit-trigger"
                  :title="defaultDisplayValue(entry.row)"
                  @click.stop="activateCell(entry.index, fieldSchemas.defaultOpt ? 'defaultOpt' : 'default')"
                >
                  {{ defaultDisplayValue(entry.row) }}
                </button>
              </td>
              <td v-if="fieldSchemas.comment" class="column-comment-cell">
                <a-input
                  v-if="entry.index === selectedRowIndex"
                  v-model:value="entry.row.comment"
                  data-editor-field="comment"
                  :disabled="isReadOnly(fieldSchemas.comment, entry.row)"
                  @focus="beginCellEdit(entry.row, entry.index, 'comment')"
                  @keydown="handleCellKeydown($event, entry.row, entry.index, 'comment')"
                />
                <button v-else type="button" class="cell-edit-trigger" :title="entry.row.comment" @click.stop="activateCell(entry.index, 'comment')">
                  {{ entry.row.comment || $t('table-editor-empty-value') }}
                </button>
              </td>
              <td class="column-actions">
                <a-button type="text" class="column-details-button" @click.stop="openDetails(entry.index)">
                  {{ $t('table-editor-show-details') }}
                </a-button>
              </td>
            </tr>
          </template>
          <tr v-if="!filteredRows.length">
            <td :colspan="tableColumnCount" class="columns-table__empty">
              {{ searchText ? $t('table-editor-no-matching-columns') : $t('table-editor-no-columns') }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <CCModal
      v-model="showDetailsModal"
      :title="$t('table-editor-show-details')"
      :width="800"
      wrap-class-name="table-editor-column-details-modal"
      @on-cancel="cancelDetails"
    >
      <div class="column-details-modal-body">
        <TableEditorFormPanel
          v-if="detailsTab && detailsPanelSchema"
          :tab="detailsTab"
          panel-key="columns"
          :panel-schema="detailsPanelSchema"
          :selected-index="0"
        />
      </div>
      <template #footer>
        <a-button @click="cancelDetails">{{ $t('qu-xiao') }}</a-button>
        <a-button type="primary" @click="confirmDetails">{{ $t('que-ding') }}</a-button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { PlusOutlined } from '@ant-design/icons-vue';
import { Modal } from 'ant-design-vue';
import CCModal from '@/components/ui/CCModal';
import { cloneDeep as deepClone, isEqual } from '@/utils/lodash';
import TableEditorFormPanel from './TableEditorFormPanel';
import { createEditorItem, findFieldSchema, initializeFields, validatePanelItem } from './tableEditorUtils';

export default {
  name: 'TableEditorColumnsPanel',
  components: {
    CCModal,
    PlusOutlined,
    TableEditorFormPanel
  },
  props: {
    tab: {
      type: Object,
      required: true
    },
    panelSchema: {
      type: Object,
      required: true
    },
    keysSchema: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      searchText: '',
      selectedRowIndex: -1,
      renameSources: {},
      cellEdit: null,
      showDetailsModal: false,
      detailsTab: null,
      detailsPanelSchema: null,
      detailsRowIndex: -1
    };
  },
  computed: {
    rows() {
      return this.tab.formData.columns || [];
    },
    filteredRows() {
      const search = this.searchText.trim().toLowerCase();
      return this.rows
        .map((row, index) => ({ row, index }))
        .filter(
          ({ row }) =>
            !search ||
            String(row.name || '')
              .toLowerCase()
              .includes(search)
        );
    },
    fieldSchemas() {
      return {
        name: findFieldSchema(this.panelSchema, 'name'),
        columnType: findFieldSchema(this.panelSchema, 'columnType'),
        length: findFieldSchema(this.panelSchema, 'length'),
        numericPrecision: findFieldSchema(this.panelSchema, 'numericPrecision'),
        numericScale: findFieldSchema(this.panelSchema, 'numericScale'),
        datePrecision: findFieldSchema(this.panelSchema, 'datePrecision'),
        notNull: findFieldSchema(this.panelSchema, 'notNull'),
        autoIncrement: findFieldSchema(this.panelSchema, 'autoIncrement'),
        defaultOpt: findFieldSchema(this.panelSchema, 'defaultOpt'),
        default: findFieldSchema(this.panelSchema, 'default'),
        comment: findFieldSchema(this.panelSchema, 'comment')
      };
    },
    typeOptions() {
      return this.fieldSchemas.columnType?.options || [];
    },
    defaultOptions() {
      return this.fieldSchemas.defaultOpt?.options || [];
    },
    primaryKey() {
      return this.tab.formData.keys?.[0] || null;
    },
    canMoveUp() {
      return this.selectedRowIndex > 0;
    },
    canMoveDown() {
      return this.selectedRowIndex >= 0 && this.selectedRowIndex < this.rows.length - 1;
    },
    canUndoSelected() {
      if (this.selectedRowIndex < 0) {
        return false;
      }
      const row = this.rows[this.selectedRowIndex];
      const originalRow = this.originalRow(row);
      if (!originalRow) {
        return true;
      }
      return !isEqual(row, originalRow) || this.isPrimaryColumn(row) !== this.wasOriginallyPrimary(originalRow);
    },
    tableColumnCount() {
      let count = 6;
      if (this.keysSchema) count += 1;
      if (this.fieldSchemas.notNull) count += 1;
      if (this.fieldSchemas.autoIncrement) count += 1;
      if (this.fieldSchemas.defaultOpt || this.fieldSchemas.default) count += 1;
      if (this.fieldSchemas.comment) count += 1;
      return count;
    },
    duplicateNames() {
      const counts = new Map();
      this.rows.forEach((row) => {
        const name = String(row.name || '').trim();
        if (name) {
          counts.set(name, (counts.get(name) || 0) + 1);
        }
      });
      return new Set(
        Array.from(counts.entries())
          .filter(([, count]) => count > 1)
          .map(([name]) => name)
      );
    }
  },
  watch: {
    'rows.length': {
      handler() {
        if (this.selectedRowIndex >= this.rows.length) {
          this.selectedRowIndex = this.rows.length - 1;
        }
        if (this.detailsRowIndex >= this.rows.length) {
          this.cancelDetails();
        }
      }
    },
    'tab.validationTarget': {
      handler(target) {
        if (target?.panelKey === 'columns') {
          this.focusValidationTarget(target);
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    originalRow(row) {
      return (this.tab.originalFormData?.columns || []).find((item) => item.key === row?.key) || null;
    },
    wasOriginallyPrimary(originalRow) {
      return Boolean((this.tab.originalFormData?.keys || []).some((key) => (key.columns || []).some((column) => column.name === originalRow?.name)));
    },
    undoSelected() {
      if (!this.canUndoSelected) {
        return;
      }
      const row = this.rows[this.selectedRowIndex];
      const originalRow = this.originalRow(row);
      if (!originalRow) {
        this.deleteSelected();
        return;
      }

      const primaryChanged = this.isPrimaryColumn(row) !== this.wasOriginallyPrimary(originalRow);
      if (row.name !== originalRow.name) {
        this.renameColumnReferencesForRow(row, row.name, originalRow.name);
      }
      this.rows.splice(this.selectedRowIndex, 1, deepClone(originalRow));
      if (primaryChanged) {
        this.tab.formData.keys = deepClone(this.tab.originalFormData?.keys || []);
      }
      this.cellEdit = null;
      this.renameSources[originalRow.key] = originalRow.name;
      this.$nextTick(() => this.focusCell(this.selectedRowIndex, 'name'));
    },
    isReadOnly(fieldSchema, row) {
      if (!fieldSchema || row.isAdd) {
        return false;
      }
      return fieldSchema.readOnly;
    },
    activeTypeOption(row) {
      return this.typeOptions.find((option) => option.value === row.columnType) || null;
    },
    activeTypeField(row, fieldName) {
      return Boolean(this.typeFieldSchema(row, fieldName));
    },
    typeFieldSchema(row, fieldName) {
      const optionField = findFieldSchema({ children: this.activeTypeOption(row)?.children || [] }, fieldName);
      const directField = (this.panelSchema.children || []).find((field) => field.field === fieldName);
      return optionField || directField || null;
    },
    hasPrecisionParameters(row) {
      return ['numericPrecision', 'numericScale', 'datePrecision'].some((fieldName) => this.activeTypeField(row, fieldName));
    },
    firstPrecisionParameterField(row) {
      const fieldName = ['numericPrecision', 'numericScale', 'datePrecision'].find((field) => this.activeTypeField(row, field));
      return fieldName || 'columnType';
    },
    typeParameterValue(row, fieldName) {
      if (!this.activeTypeField(row, fieldName)) {
        return this.$t('table-editor-empty-value');
      }
      const value = row[fieldName];
      return value === undefined || value === null || value === '' ? this.$t('table-editor-empty-value') : value;
    },
    precisionParameterSummary(row) {
      if (this.activeTypeField(row, 'datePrecision')) {
        return this.typeParameterValue(row, 'datePrecision');
      }
      const values = ['numericPrecision', 'numericScale']
        .filter((fieldName) => this.activeTypeField(row, fieldName))
        .map((fieldName) => this.typeParameterValue(row, fieldName))
        .filter((value) => value !== this.$t('table-editor-empty-value'));
      return values.join(', ') || this.$t('table-editor-empty-value');
    },
    defaultDisplayValue(row) {
      if (row.defaultOpt === 'CUSTOM') {
        return row.default || this.$t('table-editor-empty-value');
      }
      const option = this.defaultOptions.find((item) => item.value === row.defaultOpt);
      return option?.label || row.default || this.$t('table-editor-empty-value');
    },
    handleTypeChange(row, value) {
      const option = this.typeOptions.find((item) => item.value === value);
      if (option?.children) {
        initializeFields(row, option.children);
      }
    },
    selectRow(index) {
      this.selectedRowIndex = index;
      this.tab.nodeType = 'columns';
      this.tab.selectedIndex = index;
      this.tab.selectedNode = {
        key: this.rows[index]?.key
      };
    },
    activateCell(index, fieldName) {
      this.selectRow(index);
      this.$nextTick(() => this.focusCell(index, fieldName));
    },
    addColumn() {
      const row = createEditorItem('columns', this.panelSchema);
      const baseName = row.name || 'column_name';
      const names = new Set(this.rows.map((column) => column.name));
      let suffix = 1;
      while (names.has(row.name)) {
        row.name = `${baseName}_${suffix}`;
        suffix += 1;
      }

      this.rows.push(row);
      this.searchText = '';
      const rowIndex = this.rows.length - 1;
      this.selectRow(rowIndex);
      this.$nextTick(() => {
        this.$el.querySelector(`[data-row-index="${rowIndex}"]`)?.scrollIntoView({
          block: 'center',
          inline: 'nearest'
        });
        this.focusCell(rowIndex, 'name');
      });
    },
    openDetails(index) {
      const row = this.rows[index];
      this.selectRow(index);
      this.detailsRowIndex = index;
      this.detailsPanelSchema = deepClone(this.panelSchema);
      const nameFieldSchema = findFieldSchema(this.detailsPanelSchema, 'name');
      if (nameFieldSchema) {
        nameFieldSchema.titleI18N = this.$t('table-editor-column-name');
      }
      this.detailsTab = {
        ...this.tab,
        formData: {
          ...deepClone(this.tab.formData),
          columns: [deepClone(row)]
        },
        selectedIndex: 0,
        nodeType: 'columns',
        selectedNode: {
          key: row.key
        }
      };
      this.showDetailsModal = true;
    },
    cancelDetails() {
      this.showDetailsModal = false;
      this.detailsTab = null;
      this.detailsPanelSchema = null;
      this.detailsRowIndex = -1;
    },
    confirmDetails() {
      const row = this.detailsTab?.formData?.columns?.[0];
      if (!row) {
        return;
      }

      row.name = String(row.name || '').trim();
      if (!row.name) {
        this.$Message.error(this.$t('table-editor-column-name-required'));
        this.focusDetailsField('name');
        return;
      }
      if (this.rows.some((column, index) => index !== this.detailsRowIndex && String(column.name || '').trim() === row.name)) {
        this.$Message.error(this.$t('table-editor-column-name-duplicate'));
        this.focusDetailsField('name');
        return;
      }
      if (!row.columnType) {
        this.$Message.error(this.$t('table-editor-column-type-required'));
        this.focusDetailsField('columnType');
        return;
      }

      const requiredError = validatePanelItem(this.panelSchema, row)[0];
      if (requiredError) {
        this.$Message.error(this.$t('sso-field-required', [requiredError.label || requiredError.field]));
        this.focusDetailsField(requiredError.field);
        return;
      }

      const originalRow = this.rows[this.detailsRowIndex];
      if (originalRow.name !== row.name) {
        this.renameColumnReferencesForRow(originalRow, originalRow.name, row.name);
      }
      this.rows.splice(this.detailsRowIndex, 1, deepClone(row));
      this.renameSources[row.key] = row.name;
      this.cellEdit = null;
      this.cancelDetails();
    },
    focusDetailsField(fieldName) {
      this.$nextTick(() => {
        const field = document.querySelector(`.table-editor-column-details-modal [data-editor-field="${fieldName}"]`);
        const focusable =
          field?.matches?.('input, textarea, [tabindex]') && !field.disabled
            ? field
            : field?.querySelector?.('input:not([disabled]), textarea:not([disabled]), .ant-select-selector, [tabindex]:not([tabindex="-1"])');
        focusable?.focus();
        focusable?.select?.();
      });
    },
    beginCellEdit(row, index, fieldName) {
      this.selectRow(index);
      this.cellEdit = {
        rowKey: row.key,
        fieldName,
        value: deepClone(row[fieldName])
      };
      if (fieldName === 'name') {
        this.renameSources[row.key] = row.name;
      }
    },
    commitCellAndMove(event, row, index, fieldName) {
      if (fieldName === 'name') {
        this.finishRename(row);
      }
      this.cellEdit = null;
      const visibleIndex = this.filteredRows.findIndex((entry) => entry.index === index);
      const nextEntry = this.filteredRows[visibleIndex + 1];
      if (!nextEntry) {
        return;
      }
      const currentCell = event.target?.closest?.('td');
      const cellIndex = currentCell?.cellIndex ?? 1;
      this.$nextTick(() => {
        this.selectRow(nextEntry.index);
        const nextRow = this.$el.querySelector(`[data-row-index="${nextEntry.index}"]`);
        const nextCell = nextRow?.children?.[cellIndex];
        const focusable = nextCell?.querySelector('input:not([disabled]), .ant-select:not(.ant-select-disabled) .ant-select-selector');
        if (focusable) {
          focusable.focus();
          focusable.select?.();
        }
      });
    },
    handleCellKeydown(event, row, index, fieldName) {
      if (event.key === 'Enter') {
        event.preventDefault();
        this.commitCellAndMove(event, row, index, fieldName);
        return;
      }
      if (event.key === 'Escape') {
        event.preventDefault();
        this.cancelCellEdit(row, fieldName);
      }
    },
    cancelCellEdit(row, fieldName) {
      if (!this.cellEdit || this.cellEdit.rowKey !== row.key || this.cellEdit.fieldName !== fieldName) {
        return;
      }
      const previousValue = deepClone(this.cellEdit.value);
      if (fieldName === 'name' && row.name !== previousValue) {
        this.renameColumnReferencesForRow(row, row.name, previousValue);
        this.renameSources[row.key] = previousValue;
      }
      row[fieldName] = previousValue;
      if (fieldName === 'columnType') {
        this.handleTypeChange(row, previousValue);
      }
      this.cellEdit = null;
    },
    focusCell(rowIndex, fieldName) {
      const row = this.$el.querySelector(`[data-row-index="${rowIndex}"]`);
      const field = row?.querySelector(`[data-editor-field="${fieldName}"]`);
      const focusable =
        field?.matches?.('input, textarea, [tabindex]') && !field.disabled
          ? field
          : field?.querySelector?.('input:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])');
      if (focusable) {
        focusable.focus();
        focusable.select?.();
      }
    },
    focusValidationTarget(target) {
      this.searchText = '';
      if (target.sourcePanelKey === 'keys') {
        this.$nextTick(() => {
          const primaryColumnName = this.primaryKey?.columns?.[0]?.name;
          const rowIndex = this.rows.findIndex((row) => row.name === primaryColumnName);
          if (rowIndex >= 0) {
            this.selectRow(rowIndex);
            this.$el.querySelector(`[data-row-index="${rowIndex}"] .ant-checkbox-input`)?.focus();
          }
        });
        return;
      }
      this.$nextTick(() => {
        if (Number.isInteger(target.rowIndex) && this.rows[target.rowIndex]) {
          this.selectRow(target.rowIndex);
          const row = this.$el.querySelector(`[data-row-index="${target.rowIndex}"]`);
          row?.scrollIntoView({ block: 'center', inline: 'nearest' });
          this.focusCell(target.rowIndex, target.field || 'name');
          return;
        }
        this.$el.querySelector('.columns-toolbar .column-add-button')?.focus();
      });
    },
    finishRename(row) {
      const oldName = this.renameSources[row.key];
      const newName = row.name;
      this.renameSources[row.key] = newName;
      if (oldName === newName) {
        return;
      }
      this.renameColumnReferencesForRow(row, oldName, newName);
    },
    renameColumnReferencesForRow(row, oldName, newName) {
      const anotherRowOwnsOldName = this.rows.some((item) => item.key !== row.key && item.name === oldName);
      if (anotherRowOwnsOldName) {
        return;
      }
      this.renameColumnReferences(oldName, newName);
    },
    renameColumnReferences(oldName, newName) {
      ['keys', 'indexes', 'constraints'].forEach((panelKey) => {
        (this.tab.formData[panelKey] || []).forEach((item) => {
          (item.columns || []).forEach((column) => {
            if (column.name === oldName) {
              column.name = newName;
            }
          });
        });
      });
      (this.tab.formData.foreignKeys || []).forEach((foreignKey) => {
        (foreignKey.relation || []).forEach((relation) => {
          if (relation.name === oldName) {
            relation.name = newName;
          }
        });
      });
    },
    isPrimaryColumn(row) {
      return Boolean(this.primaryKey?.columns?.some((column) => column.name === row.name));
    },
    togglePrimaryColumn(row, checked) {
      if (checked) {
        this.addPrimaryColumn(row);
        row.notNull = true;
        return;
      }
      this.removePrimaryColumn(row.name);
    },
    addPrimaryColumn(row) {
      if (!this.tab.formData.keys) {
        this.tab.formData.keys = [];
      }
      if (!this.tab.formData.keys.length) {
        this.tab.formData.keys.push(createEditorItem('keys', this.keysSchema));
      }
      if (!Array.isArray(this.primaryKey.columns)) {
        this.primaryKey.columns = [];
      }
      if (this.primaryKey.columns.some((column) => column.name === row.name)) {
        return;
      }
      const columnsSchema = findFieldSchema(this.keysSchema, 'columns');
      const primaryColumn = {
        key: `${row.key}-primary`,
        name: row.name
      };
      initializeFields(primaryColumn, columnsSchema?.children);
      this.primaryKey.columns.push(primaryColumn);
    },
    removePrimaryColumn(name) {
      if (!this.primaryKey?.columns) {
        return;
      }
      this.primaryKey.columns = this.primaryKey.columns.filter((column) => column.name !== name);
      if (!this.primaryKey.columns.length) {
        this.tab.formData.keys = [];
      }
    },
    requestDeleteSelected() {
      if (this.selectedRowIndex < 0) {
        return;
      }
      const row = this.rows[this.selectedRowIndex];
      const anotherRowOwnsName = this.rows.some((item) => item.key !== row.key && item.name === row.name);
      const referenceCount = anotherRowOwnsName ? 0 : this.countColumnReferences(row.name);
      if (!referenceCount) {
        this.deleteSelected();
        return;
      }
      Modal.confirm({
        title: this.$t('table-editor-delete-column-title'),
        content: this.$t('table-editor-delete-column-reference-warning', [row.name, referenceCount]),
        onOk: () => this.deleteSelected()
      });
    },
    countColumnReferences(name) {
      let count = 0;
      ['keys', 'indexes', 'constraints'].forEach((panelKey) => {
        (this.tab.formData[panelKey] || []).forEach((item) => {
          count += (item.columns || []).filter((column) => column.name === name).length;
        });
      });
      (this.tab.formData.foreignKeys || []).forEach((foreignKey) => {
        count += (foreignKey.relation || []).filter((relation) => relation.name === name).length;
      });
      return count;
    },
    removeColumnReferences(name) {
      ['keys', 'indexes', 'constraints'].forEach((panelKey) => {
        (this.tab.formData[panelKey] || []).forEach((item) => {
          item.columns = (item.columns || []).filter((column) => column.name !== name);
        });
      });
      this.tab.formData.keys = (this.tab.formData.keys || []).filter((item) => item.columns?.length);
      (this.tab.formData.foreignKeys || []).forEach((foreignKey) => {
        foreignKey.relation = (foreignKey.relation || []).filter((relation) => relation.name !== name);
      });
    },
    deleteSelected() {
      const row = this.rows[this.selectedRowIndex];
      const anotherRowOwnsName = this.rows.some((item) => item.key !== row.key && item.name === row.name);
      if (!anotherRowOwnsName) {
        this.removeColumnReferences(row.name);
      }
      this.rows.splice(this.selectedRowIndex, 1);
      if (this.rows.length) {
        this.selectRow(Math.min(this.selectedRowIndex, this.rows.length - 1));
        return;
      }
      this.selectedRowIndex = -1;
      this.tab.selectedIndex = -1;
      this.tab.selectedNode = null;
    },
    moveSelected(offset) {
      const targetIndex = this.selectedRowIndex + offset;
      if (targetIndex < 0 || targetIndex >= this.rows.length) {
        return;
      }
      const [row] = this.rows.splice(this.selectedRowIndex, 1);
      this.rows.splice(targetIndex, 0, row);
      this.selectRow(targetIndex);
      this.syncPrimaryColumnOrder();
    },
    syncPrimaryColumnOrder() {
      if (!this.primaryKey?.columns?.length) {
        return;
      }
      const order = new Map(this.rows.map((row, index) => [row.name, index]));
      this.primaryKey.columns.sort(
        (left, right) => (order.get(left.name) ?? Number.MAX_SAFE_INTEGER) - (order.get(right.name) ?? Number.MAX_SAFE_INTEGER)
      );
    },
    columnError(row, fieldName) {
      if (fieldName === 'name') {
        if (!String(row.name || '').trim()) {
          return this.$t('table-editor-column-name-required');
        }
        if (this.duplicateNames.has(String(row.name).trim())) {
          return this.$t('table-editor-column-name-duplicate');
        }
      }
      if (fieldName === 'columnType' && !row.columnType) {
        return this.$t('table-editor-column-type-required');
      }
      const requiredError = validatePanelItem(this.panelSchema, row).find((error) => error.field === fieldName);
      if (requiredError) {
        return this.$t('sso-field-required', [requiredError.label || fieldName]);
      }
      return '';
    },
    rowError(row) {
      const fieldNames = ['name', 'columnType', 'length', 'numericPrecision', 'numericScale', 'datePrecision'];
      for (const fieldName of fieldNames) {
        const error = this.columnError(row, fieldName);
        if (error) {
          return error;
        }
      }
      const requiredError = validatePanelItem(this.panelSchema, row)[0];
      return requiredError ? this.$t('sso-field-required', [requiredError.label || requiredError.field]) : '';
    }
  }
};
</script>

<style scoped lang="less">
.table-editor-columns-panel {
  display: flex;
  flex: 1;
  width: 100%;
  min-width: 0;
  min-height: 0;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
  padding: 16px 20px 20px;
  background: var(--bg-primary);
  container-type: inline-size;
}

.columns-toolbar {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  justify-content: space-between;
  min-width: 0;
  gap: 12px;
  padding: 0;
}

.columns-toolbar__actions {
  display: flex;
  min-width: 0;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.column-add-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  line-height: 20px;
}

.column-add-button :deep(.anticon) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin: 0 !important;
  line-height: 1;
  vertical-align: 0;
}

.column-add-button :deep(.anticon svg) {
  display: block;
}

.column-add-button :deep(.anticon + span) {
  margin-left: 0;
}

.column-add-button__label {
  line-height: 20px;
}

.columns-toolbar__tools {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 12px;
}

.columns-toolbar__count {
  color: var(--text-tertiary);
  font-size: 13px;
  white-space: nowrap;
}

.columns-toolbar__search {
  width: 220px;
}

.columns-table-wrap {
  flex: 1;
  width: 100%;
  min-height: 0;
  overflow: auto;
  border: 1px solid var(--border-light);
  border-radius: 8px;
}

.columns-table {
  width: 100%;
  min-width: 1060px;
  border-collapse: separate;
  border-spacing: 0;
  table-layout: fixed;
  color: var(--text-primary);
}

.columns-table th {
  position: sticky;
  top: 0;
  z-index: 2;
  height: 44px;
  padding: 10px 12px;
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

.columns-table td {
  height: 52px;
  padding: 8px 12px;
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-primary);
  vertical-align: middle;
}

.columns-table__row:hover td {
  background: var(--bg-secondary);
}

.columns-table__row--selected td {
  background: var(--bg-hover);
}

.columns-table__row--selected td:first-child {
  box-shadow: inset 3px 0 var(--primary-color);
}

.columns-table :deep(.ant-input),
.columns-table :deep(.ant-select-selector) {
  min-height: 32px;
  border-radius: 6px !important;
  font-size: 13px;
}

.columns-table :deep(.ant-select) {
  width: 100%;
}

.column-order {
  width: 44px;
  color: var(--text-tertiary);
  text-align: center !important;
}

.column-error-indicator {
  display: inline-flex;
  width: 18px;
  height: 18px;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--error-color);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
}

.column-name {
  width: 160px;
}

.column-type {
  width: 160px;
}

.column-length {
  width: 92px;
}

.column-precision {
  width: 164px;
}

.column-check {
  width: 60px;
  text-align: center !important;
}

.column-default {
  width: 148px;
}

.column-comment {
  width: 148px;
}

.column-actions {
  width: 72px;
  text-align: center !important;
}

.cell-edit-trigger {
  display: block;
  width: 100%;
  min-width: 0;
  min-height: 32px;
  overflow: hidden;
  padding: 0;
  border: 0;
  background: transparent;
  color: var(--text-primary);
  cursor: text;
  font-size: 13px;
  line-height: 32px;
  text-align: left;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.cell-edit-trigger:hover,
.cell-edit-trigger:focus-visible {
  color: var(--primary-color);
}

.cell-edit-trigger:disabled {
  color: var(--text-tertiary);
  cursor: default;
}

.cell-edit-trigger:disabled:hover {
  color: var(--text-tertiary);
}

.cell-edit-trigger:focus-visible {
  border-radius: 4px;
  outline: 1px solid var(--primary-color);
  outline-offset: 2px;
}

.cell-edit-trigger--mono {
  font-family: ui-monospace, Menlo, Monaco, Consolas, 'Liberation Mono', monospace;
}

.cell-empty {
  color: var(--text-tertiary);
}

.type-parameter-editor {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 8px;
}

.single-type-parameter-editor {
  display: flex;
  min-width: 0;
  align-items: center;
}

.single-type-parameter-editor :deep(.ant-input) {
  min-width: 52px;
  padding-right: 6px;
  padding-left: 6px;
}

.type-parameter-editor__item {
  display: flex;
  min-width: 0;
  flex: 1 1 0;
  align-items: center;
  gap: 4px;
}

.type-parameter-editor__item > span {
  flex: 0 0 auto;
  color: var(--text-tertiary);
  font-size: 11px;
  white-space: nowrap;
}

.type-parameter-editor__item :deep(.ant-input) {
  min-width: 40px;
  padding-right: 6px;
  padding-left: 6px;
}

.column-default-editor {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 6px;
}

.column-default-editor :deep(.ant-select) {
  min-width: 82px;
}

.column-details-button {
  height: 30px;
  padding: 0 4px;
  color: var(--text-secondary);
}

.column-details-button:hover {
  color: var(--text-primary);
}

.column-cell--error :deep(.ant-input),
.column-cell--error :deep(.ant-select-selector) {
  border-color: var(--error-color) !important;
}

.columns-table__empty {
  height: 160px !important;
  color: var(--text-secondary);
  text-align: center;
}

.column-details-modal-body {
  max-height: calc(80vh - 180px);
  overflow-y: auto;
}

.column-details-modal-body :deep(.table-editor-form-panel) {
  max-width: none;
  padding: 8px 0;
}

.column-details-modal-body :deep(.table-editor-form-grid) {
  gap: 16px 32px;
}

.column-details-modal-body :deep(.table-editor-form-grid > div > div > div:first-child),
.column-details-modal-body :deep(.create-table-item--nested > div:first-child) {
  box-sizing: border-box;
  min-width: 216px !important;
  padding-right: 24px;
}

.column-details-modal-body :deep(.create-table-item--nested) {
  margin-top: 16px !important;
  margin-left: -216px !important;
}

@media (max-width: 1024px) {
  .columns-toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .columns-toolbar__actions {
    flex-wrap: nowrap;
    overflow-x: auto;
  }

  .columns-toolbar__tools {
    width: 100%;
  }

  .columns-toolbar__search {
    flex: 1;
    width: auto;
  }
}

@media (max-width: 768px) {
  .table-editor-columns-panel {
    padding: 12px 16px 16px;
  }
}

@container (max-width: 1000px) {
  .columns-table {
    min-width: 880px;
  }

  .column-name {
    width: 150px;
  }

  .column-type {
    width: 150px;
  }

  .column-length {
    width: 84px;
  }

  .column-precision {
    width: 150px;
  }

  .column-check {
    width: 56px;
  }

  .column-default {
    width: 124px;
  }

  .column-comment,
  .column-comment-cell {
    display: none;
  }

  .column-actions {
    width: 64px;
  }
}
</style>
