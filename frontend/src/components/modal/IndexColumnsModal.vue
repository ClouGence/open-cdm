<template>
  <a-modal
    :visible="visible"
    :title="$t('table-editor-included-columns')"
    :width="900"
    :mask-closable="false"
    wrap-class-name="index-columns-modal"
    @cancel="handleCancel"
  >
    <div class="index-columns-table-wrap">
      <table class="index-columns-table">
        <thead>
          <tr>
            <th class="index-columns-table__order">{{ $t('table-editor-order') }}</th>
            <th v-for="childSchema in schema.children" :key="childSchema.field">
              {{ childSchema.type === 'Columns' ? $t('table-editor-fields') : childSchema.titleI18N }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(column, rowIndex) in draftColumns"
            :key="column.key"
            :class="{ 'index-columns-table__row--selected': rowIndex === selectedRowIndex }"
            @click="selectedRowIndex = rowIndex"
          >
            <td class="index-columns-table__order">{{ rowIndex + 1 }}</td>
            <td v-for="childSchema in schema.children" :key="childSchema.field">
              <a-select
                v-if="childSchema.type === 'Columns'"
                v-model:value="column[childSchema.field]"
                :placeholder="$t('qing-xuan-ze-lie')"
                :disabled="readOnly || childSchema.readOnly"
                allow-clear
                show-search
                style="width: 100%"
              >
                <a-select-option
                  v-for="tableColumn in tableColumns"
                  :key="`${tabId}-${tableColumn.key}`"
                  :value="tableColumn.name"
                  :disabled="isColumnOptionDisabled(tableColumn.name, rowIndex)"
                >
                  {{ tableColumn.name }}
                </a-select-option>
              </a-select>
              <a-input
                v-else-if="childSchema.type === 'Input'"
                v-model:value="column[childSchema.field]"
                :disabled="readOnly || childSchema.readOnly"
              />
              <a-select
                v-else-if="childSchema.type === 'Options'"
                v-model:value="column[childSchema.field]"
                :disabled="readOnly || childSchema.readOnly"
                allow-clear
                show-search
                style="width: 100%"
              >
                <a-select-option v-for="option in childSchema.options" :key="option.value" :value="option.value">
                  {{ option.label }}
                </a-select-option>
              </a-select>
              <span v-else>{{ column[childSchema.field] || $t('table-editor-empty-value') }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="index-columns-actions">
      <a-button :disabled="readOnly" :aria-label="$t('xin-zeng')" @click="addColumn">
        <PlusOutlined />
      </a-button>
      <a-button :disabled="readOnly || selectedRowIndex < 0" :aria-label="$t('shan-chu')" @click="deleteSelectedColumn">
        <MinusOutlined />
      </a-button>
    </div>
    <template #footer>
      <a-button @click="handleCancel">{{ $t('qu-xiao') }}</a-button>
      <a-button type="primary" :disabled="readOnly" @click="handleConfirm">{{ $t('que-ding') }}</a-button>
    </template>
  </a-modal>
</template>

<script>
import dayjs from 'dayjs';
import { MinusOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { cloneDeep as deepClone } from '@/utils/lodash';

export default {
  name: 'IndexColumnsModal',
  components: {
    MinusOutlined,
    PlusOutlined
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    schema: {
      type: Object,
      required: true
    },
    value: {
      type: Array,
      default: () => []
    },
    tableColumns: {
      type: Array,
      default: () => []
    },
    tabId: {
      type: [String, Number],
      default: ''
    },
    readOnly: {
      type: Boolean,
      default: false
    }
  },
  emits: ['cancel', 'confirm'],
  data() {
    return {
      draftColumns: [],
      selectedRowIndex: -1
    };
  },
  watch: {
    visible: {
      handler(visible) {
        if (!visible) {
          return;
        }
        this.draftColumns = deepClone(this.value);
        if (!this.draftColumns.length) {
          this.draftColumns.push(this.createColumn());
        }
        this.selectedRowIndex = this.draftColumns.length - 1;
      },
      immediate: true
    }
  },
  methods: {
    createColumn() {
      const column = {
        key: `${dayjs().valueOf()}-${this.draftColumns.length}`
      };
      (this.schema.children || []).forEach((childSchema) => {
        column[childSchema.field] = deepClone(childSchema.defaultVal);
      });
      return column;
    },
    addColumn() {
      this.draftColumns.push(this.createColumn());
      this.selectedRowIndex = this.draftColumns.length - 1;
    },
    deleteSelectedColumn() {
      if (this.selectedRowIndex < 0) {
        return;
      }
      this.draftColumns.splice(this.selectedRowIndex, 1);
      if (!this.draftColumns.length) {
        this.selectedRowIndex = -1;
        return;
      }
      this.selectedRowIndex = Math.min(this.selectedRowIndex, this.draftColumns.length - 1);
    },
    isColumnOptionDisabled(columnName, rowIndex) {
      return this.draftColumns.some((column, index) => index !== rowIndex && column.name === columnName);
    },
    handleCancel() {
      this.$emit('cancel');
    },
    handleConfirm() {
      this.$emit('confirm', deepClone(this.draftColumns));
    }
  }
};
</script>

<style scoped lang="less">
:global(.index-columns-modal .ant-modal-body) {
  padding: 24px;
}

.index-columns-table-wrap {
  min-height: 240px;
  overflow: auto;
}

.index-columns-table {
  width: 100%;
  min-width: 640px;
  border-collapse: separate;
  border-spacing: 0;
  table-layout: fixed;
}

.index-columns-table th,
.index-columns-table td {
  height: 48px;
  padding: 8px 12px;
  border-bottom: 1px solid var(--border-light);
  text-align: left;
  vertical-align: middle;
}

.index-columns-table th {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  font-weight: 500;
}

.index-columns-table__order {
  width: 64px;
  color: var(--text-tertiary);
  text-align: center !important;
}

.index-columns-table__row--selected td {
  background: var(--bg-hover);
}

.index-columns-actions {
  display: flex;
  gap: 8px;
  margin-top: 16px;
}
</style>
