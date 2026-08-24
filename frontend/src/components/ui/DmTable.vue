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
      <DmTableOverflowCell
        v-else-if="column.__tooltip && column.dataIndex !== undefined && column.dataIndex !== null"
        :content="record[column.dataIndex]"
        :max-width="column.__tooltipMaxWidth"
      />
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
import { h, resolveComponent } from 'vue';
import { convertTableColumns } from '@/utils/convertTableColumns';

const DmTableOverflowCell = {
  name: 'DmTableOverflowCell',
  props: {
    content: {
      type: [String, Number],
      default: ''
    },
    maxWidth: {
      type: Number,
      default: 250
    }
  },
  data() {
    return {
      tooltipDisabled: true
    };
  },
  methods: {
    updateOverflow() {
      const content = this.$refs.content;
      if (!content) {
        return;
      }
      this.tooltipDisabled = content.scrollWidth <= content.clientWidth;
    }
  },
  mounted() {
    this.$nextTick(this.updateOverflow);
  },
  updated() {
    this.$nextTick(this.updateOverflow);
  },
  render() {
    const Poptip = resolveComponent('Poptip');
    return h(
      Poptip,
      {
        transfer: true,
        trigger: 'hover',
        placement: 'top',
        content: String(this.content ?? ''),
        disabled: this.tooltipDisabled,
        width: this.maxWidth,
        wordWrap: true,
        padding: '8px 12px',
        class: 'dm-table-cell-tooltip'
      },
      {
        default: () =>
          h(
            'span',
            {
              ref: 'content',
              class: 'dm-table-cell-tooltip-content',
              style: {
                display: 'block',
                width: '100%',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap'
              },
              onMouseenter: this.updateOverflow
            },
            this.content
          ),
        content: () =>
          h(
            'div',
            {
              style: {
                maxHeight: '240px',
                overflow: 'auto',
                whiteSpace: 'pre-wrap',
                wordBreak: 'break-all',
                cursor: 'text',
                userSelect: 'text'
              }
            },
            this.content
          )
      }
    );
  }
};

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
  components: { DmTableRenderCell, DmTableOverflowCell },
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

<style scoped>
.dm-table-cell-tooltip {
  display: block;
  width: 100%;
}

.dm-table-cell-tooltip :deep(.ivu-poptip-rel) {
  display: block;
}
</style>
