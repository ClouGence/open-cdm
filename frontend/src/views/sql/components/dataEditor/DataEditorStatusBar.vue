<script>
import { defineComponent } from 'vue';
import { LockOutlined } from '@ant-design/icons-vue';

export default defineComponent({
  name: 'DataEditorStatusBar',
  components: {
    LockOutlined
  },
  props: {
    selectionStats: {
      type: Object,
      default: null
    },
    changeBreakdown: {
      type: Object,
      default: () => ({ added: 0, modified: 0, deleted: 0, total: 0 })
    },
    isReadOnly: {
      type: Boolean,
      default: false
    },
    isTransposed: {
      type: Boolean,
      default: false
    },
    totalRowsLoaded: {
      type: Number,
      default: 0
    },
    visibleColCount: {
      type: Number,
      default: 0
    },
    totalColCount: {
      type: Number,
      default: 0
    }
  },
  emits: ['preview-dml'],
  computed: {
    selectionLabel() {
      if (!this.selectionStats) return '';
      const { rowCount, colCount } = this.selectionStats;
      return `${rowCount}R × ${colCount}C`;
    },
    aggregateLabel() {
      const s = this.selectionStats;
      if (!s) return '';
      if (s.numericCount > 1) {
        return `Count: ${s.nonNullCount}  |  Sum: ${s.sum}  |  Avg: ${s.avg}  |  Min: ${s.min}  |  Max: ${s.max}`;
      }
      if (s.cellCount > 1) {
        return `Count: ${s.nonNullCount}`;
      }
      return '';
    },
    dimensionsLabel() {
      const hidden = this.totalColCount - this.visibleColCount;
      if (hidden > 0) {
        return `${this.totalRowsLoaded} rows · ${this.visibleColCount}/${this.totalColCount} cols`;
      }
      return `${this.totalRowsLoaded} rows · ${this.visibleColCount} cols`;
    },
    addedText() {
      return `+${this.changeBreakdown.added}`;
    },
    modifiedText() {
      return `~${this.changeBreakdown.modified}`;
    },
    deletedText() {
      return `-${this.changeBreakdown.deleted}`;
    },
    transposeBadge() {
      return 'Transposed';
    }
  }
});
</script>

<template>
  <div class="dg-status-bar">
    <div class="dg-status-left">
      <span class="dg-status-item">{{ dimensionsLabel }}</span>
      <template v-if="selectionLabel">
        <span class="dg-status-sep" />
        <span class="dg-status-item dg-status-selection">{{ selectionLabel }}</span>
      </template>
      <template v-if="aggregateLabel">
        <span class="dg-status-sep" />
        <span class="dg-status-item dg-status-agg">{{ aggregateLabel }}</span>
      </template>
    </div>

    <div class="dg-status-center">
      <button v-if="changeBreakdown.total > 0" type="button" class="dg-change-summary" @click="$emit('preview-dml')">
        <span v-if="changeBreakdown.added > 0" class="dg-chg-add">{{ addedText }}</span>
        <span v-if="changeBreakdown.modified > 0" class="dg-chg-mod">{{ modifiedText }}</span>
        <span v-if="changeBreakdown.deleted > 0" class="dg-chg-del">{{ deletedText }}</span>
      </button>
    </div>

    <div class="dg-status-right">
      <span v-if="isTransposed" class="dg-status-pill">{{ transposeBadge }}</span>
      <span v-if="isReadOnly" class="dg-status-pill is-readonly">
        <LockOutlined />
        <span>{{ $t('zhi-du') }}</span>
      </span>
    </div>
  </div>
</template>

<style scoped lang="less">
.dg-status-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  height: 28px;
  flex-shrink: 0;
  padding: 0 12px;
  border-top: 1px solid var(--border-light, #e5e7eb);
  background: var(--bg-secondary, #f8fafc);
  color: var(--text-secondary, #6b7280);
  font-size: 11px;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  user-select: none;
}

.dg-status-left,
.dg-status-center,
.dg-status-right {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.dg-status-left {
  flex: 1;
  overflow: hidden;
  white-space: nowrap;
}

.dg-status-item {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  line-height: 1;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dg-status-selection {
  color: var(--text-primary, #1f2329);
  font-weight: 600;
}

.dg-status-agg {
  color: var(--text-secondary, #4b5563);
}

.dg-status-sep {
  width: 1px;
  height: 12px;
  background: var(--border-light, #d1d5db);
  flex-shrink: 0;
}

.dg-change-summary {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 2px 8px;
  border-radius: 10px;
  border: 1px solid var(--border-light, #d1d5db);
  background: var(--bg-primary, #ffffff);
  cursor: pointer;
  line-height: 1;

  span {
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    font-size: 11px;
    line-height: 1;
    font-weight: 600;
  }

  &:hover {
    border-color: var(--primary-color, #1677ff);
  }
}

.dg-chg-add {
  color: #16a34a;
}

.dg-chg-mod {
  color: #d97706;
}

.dg-chg-del {
  color: #dc2626;
}

.dg-status-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  height: 18px;
  padding: 0 6px;
  border-radius: 4px;
  background: rgba(107, 114, 128, 0.1);
  color: var(--text-secondary, #4b5563);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  line-height: 1;

  span {
    font-family: inherit;
    font-size: 11px;
    line-height: 1;
  }

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 11px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
  }

  &.is-extractor {
    color: var(--primary-color, #3ecf8e);
    background: rgba(62, 207, 142, 0.12);
  }
}
</style>
