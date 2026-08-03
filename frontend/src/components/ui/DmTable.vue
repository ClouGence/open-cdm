<template>
  <a-table
    class="dm-table"
    :class="{ 'dm-table--stripe': stripe }"
    :columns="antdColumns"
    :data-source="dataSource"
    :loading="loading"
    :size="tableSize"
    :bordered="border"
    :pagination="false"
    :scroll="scroll"
    :row-key="rowKey"
    :row-selection="rowSelection"
    :locale="tableLocale"
  >
    <template #bodyCell="{ column, record, index }">
      <DmTableRenderCell
        v-if="column.__legacyRender"
        :render-cell="column.__legacyRender"
        :row="record"
        :column="column.__legacyColumn"
        :index="index"
      />
      <slot v-else-if="column.__slot" :name="column.__slot" :row="record" :index="index" />
      <template v-else-if="column.dataIndex !== undefined && column.dataIndex !== null">
        {{ record[column.dataIndex] }}
      </template>
    </template>
    <template v-if="$slots.empty" #emptyText>
      <slot name="empty" />
    </template>
  </a-table>
</template>

<script>
import { h } from 'vue';
import { convertTableColumns } from '@/utils/convertTableColumns';

const DmTableRenderCell = {
  name: 'DmTableRenderCell',
  props: {
    renderCell: {
      type: Function,
      required: true
    },
    row: {
      type: Object,
      required: true
    },
    column: {
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  render() {
    return this.renderCell(h, {
      row: this.row,
      column: this.column,
      index: this.index
    });
  }
};

export default {
  name: 'DmTable',
  components: { DmTableRenderCell },
  props: {
    columns: {
      type: Array,
      default: () => []
    },
    data: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: 'small'
    },
    border: {
      type: Boolean,
      default: false
    },
    stripe: {
      type: Boolean,
      default: false
    },
    locale: {
      type: Object,
      default: () => ({})
    },
    scroll: {
      type: Object,
      default: undefined
    },
    rowKey: {
      type: [String, Function],
      default: (_, index) => index
    },
    rowSelection: {
      type: Object,
      default: undefined
    }
  },
  computed: {
    antdColumns() {
      return convertTableColumns(this.columns);
    },
    dataSource() {
      return this.data;
    },
    tableSize() {
      if (this.size === 'large') {
        return 'middle';
      }
      return this.size === 'small' ? 'small' : 'middle';
    },
    tableLocale() {
      return {
        emptyText: this.locale?.emptyText || this.$t('zan-wu-shu-ju')
      };
    }
  }
};
</script>
