<script>
import { defineComponent } from 'vue';
import {
  VerticalRightOutlined,
  LeftOutlined,
  RightOutlined,
  ReloadOutlined,
  DownloadOutlined,
  VerticalLeftOutlined,
  MinusOutlined,
  PlusOutlined,
  RollbackOutlined,
  CopyOutlined,
  SwapOutlined,
  TableOutlined,
  SearchOutlined,
  PushpinOutlined,
  PushpinFilled,
  LockOutlined,
  HistoryOutlined,
  FilterOutlined,
  SortAscendingOutlined,
  DownOutlined,
  LoadingOutlined,
  CloseCircleFilled
} from '@ant-design/icons-vue';

const LABELS = {
  OF: 'of',
  WHERE: 'WHERE',
  ORDER_BY: 'ORDER BY',
  SEARCH_COLS: 'Search columns...',
  SHOW_ALL: 'Show All',
  PIN_LEFT: 'Pin Left',
  TRANSPOSE: 'Transpose',
  COLUMNS: 'Columns',
  REVERT_ALL: 'Revert All',
  FILTER_PLACEHOLDER: "e.g. id > 100 AND status = 'passed'",
  ORDER_PLACEHOLDER: 'e.g. id DESC',
  FIRST_PAGE: 'First Page',
  PREV_PAGE: 'Previous Page',
  PAGE_SIZE_TIP: 'Page Size',
  COUNT_ROWS: 'Count Total Rows',
  NEXT_PAGE: 'Next Page',
  LAST_PAGE: 'Last Page',
  RELOAD_PAGE: 'Reload Page (Cmd/Ctrl+R)',
  EXPORT_TIP: 'Export Data',
  FIND_TIP: 'Find in Page (Cmd/Ctrl+F)',
  HISTORY_TIP: 'Filter History'
};

export default defineComponent({
  name: 'DataEditorToolbar',
  components: {
    VerticalRightOutlined,
    LeftOutlined,
    RightOutlined,
    ReloadOutlined,
    DownloadOutlined,
    VerticalLeftOutlined,
    MinusOutlined,
    PlusOutlined,
    RollbackOutlined,
    CopyOutlined,
    SwapOutlined,
    TableOutlined,
    SearchOutlined,
    PushpinOutlined,
    PushpinFilled,
    LockOutlined,
    HistoryOutlined,
    FilterOutlined,
    SortAscendingOutlined,
    DownOutlined,
    LoadingOutlined,
    CloseCircleFilled
  },
  props: {
    tab: {
      type: Object,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    },
    createSQLLoading: {
      type: Boolean,
      default: false
    },
    isReadOnly: {
      type: Boolean,
      default: false
    },
    isEditing: {
      type: Boolean,
      default: false
    },
    pendingChangeCount: {
      type: Number,
      default: 0
    },
    hasSelection: {
      type: Boolean,
      default: false
    },
    isTransposed: {
      type: Boolean,
      default: false
    },
    findVisible: {
      type: Boolean,
      default: false
    },
    allColumns: {
      type: Array,
      default: () => []
    },
    hiddenColumnSet: {
      type: Object,
      default: () => ({})
    },
    pinnedColumnSet: {
      type: Object,
      default: () => ({})
    },
    filterHistory: {
      type: Array,
      default: () => []
    },
    exportState: {
      type: Object,
      default: null
    }
  },
  emits: [
    'fetch-page',
    'fetch-total',
    'page-size-change',
    'add-row',
    'clone-row',
    'delete-row',
    'revert-all',
    'submit',
    'export',
    'download-export',
    'reset-export',
    'toggle-transpose',
    'toggle-find',
    'toggle-column-visibility',
    'show-all-columns',
    'toggle-column-pin',
    'jump-to-column',
    'apply-filter-history'
  ],
  data() {
    return {
      LABELS,
      colSearch: '',
      colPopoverOpen: false
    };
  },
  computed: {
    maxPageSize() {
      const limit = Number(this.tab.rawTableData?.maxPageSize);
      return Number.isFinite(limit) && limit > 0 ? limit : null;
    },
    pageSizeLimitReason() {
      if (!this.maxPageSize) return '';
      return this.$t('data-editor-page-size-limit-tip', {
        label: this.$t('preference-online-max-record-count'),
        max: this.maxPageSize
      });
    },
    pageSizeOptions() {
      const presets = [1, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000];
      if (this.maxPageSize && this.maxPageSize <= 10000 && !presets.includes(this.maxPageSize)) {
        presets.push(this.maxPageSize);
        presets.sort((a, b) => a - b);
      }
      return presets.map((size) => ({
        key: String(size),
        size,
        disabled: Boolean(this.maxPageSize && size > this.maxPageSize)
      }));
    },
    pageRangeText() {
      const len = this.tab.rawTableData?.resultSet?.length || 0;
      if (!len) return '0-0';
      return `${this.tab.offset + 1}-${this.tab.offset + len}`;
    },
    totalText() {
      if (this.tab.total !== null && this.tab.total !== undefined) {
        return String(this.tab.total);
      }
      const len = this.tab.rawTableData?.resultSet?.length || 0;
      return `${this.tab.offset + len}${this.tab.hasNext ? '+' : ''}`;
    },
    filteredColumns() {
      const q = this.colSearch.trim().toLowerCase();
      if (!q) return this.allColumns;
      return this.allColumns.filter((c) => (c.column || '').toLowerCase().includes(q));
    },
    hiddenCount() {
      return Object.values(this.hiddenColumnSet || {}).filter(Boolean).length;
    }
  }
});
</script>

<template>
  <div class="dg-toolbar-wrapper">
    <!-- Row 1: DataGrip Flat Action Bar -->
    <div class="dg-bar dg-bar-primary">
      <!-- Pagination -->
      <div class="dg-bar-group">
        <button type="button" class="dg-icon-btn" :disabled="tab.isFirst || loading" :title="LABELS.FIRST_PAGE" @click="$emit('fetch-page', 0)">
          <VerticalRightOutlined />
        </button>
        <button
          type="button"
          class="dg-icon-btn"
          :disabled="tab.isFirst || loading"
          :title="LABELS.PREV_PAGE"
          @click="$emit('fetch-page', Math.max(tab.offset - tab.pageSize, 0))"
        >
          <LeftOutlined />
        </button>

        <div class="dg-pager-cluster">
          <a-dropdown :disabled="loading" trigger="click">
            <template #overlay>
              <a-menu size="small" v-model:selectedKeys="tab.selectedPageKeys" @click="$emit('page-size-change', $event)">
                <a-menu-item v-for="opt in pageSizeOptions" :key="opt.key" :disabled="opt.disabled">
                  <a-tooltip v-if="opt.disabled" placement="right" :title="pageSizeLimitReason">
                    <div class="dg-page-size-item">
                      <span>{{ opt.size }}</span>
                    </div>
                  </a-tooltip>
                  <div v-else class="dg-page-size-item">
                    <span>{{ opt.size }}</span>
                  </div>
                </a-menu-item>
              </a-menu>
            </template>
            <button type="button" class="dg-text-btn dg-mono-btn" :disabled="loading" :title="LABELS.PAGE_SIZE_TIP">
              <span class="dg-btn-label">{{ pageRangeText }}</span>
              <DownOutlined class="dg-caret" />
            </button>
          </a-dropdown>
          <span class="dg-of">{{ LABELS.OF }}</span>
          <button type="button" class="dg-text-btn dg-mono-btn" :disabled="loading" :title="LABELS.COUNT_ROWS" @click="$emit('fetch-total')">
            <span class="dg-btn-label">{{ totalText }}</span>
          </button>
        </div>

        <button
          type="button"
          class="dg-icon-btn"
          :disabled="!tab.hasNext || loading"
          :title="LABELS.NEXT_PAGE"
          @click="$emit('fetch-page', tab.offset + tab.pageSize)"
        >
          <RightOutlined />
        </button>
        <button type="button" class="dg-icon-btn" :disabled="!tab.hasNext || loading" :title="LABELS.LAST_PAGE" @click="$emit('fetch-page', -1)">
          <VerticalLeftOutlined />
        </button>
        <button type="button" class="dg-icon-btn" :disabled="loading" :title="LABELS.RELOAD_PAGE" @click="$emit('fetch-page', tab.offset)">
          <ReloadOutlined :spin="loading" />
        </button>
      </div>

      <span class="dg-sep" />

      <!-- Data Mutations -->
      <div class="dg-bar-group">
        <template v-if="isReadOnly">
          <span class="dg-readonly-pill">
            <LockOutlined />
            <span class="dg-btn-label">{{ $t('zhi-du') }}</span>
          </span>
        </template>
        <template v-else>
          <button
            type="button"
            class="dg-icon-btn"
            :disabled="loading || !hasSelection"
            :title="`${$t('fu-zhi-xuan-zhong-hang')} (Cmd/Ctrl+D)`"
            @click="$emit('clone-row')"
          >
            <CopyOutlined />
          </button>
          <button type="button" class="dg-icon-btn" :disabled="loading" :title="`${$t('xin-zeng-hang')} (Alt+Insert)`" @click="$emit('add-row')">
            <PlusOutlined />
          </button>
          <button
            type="button"
            class="dg-icon-btn"
            :disabled="loading || !hasSelection"
            :title="`${$t('shan-chu-hang')} (Cmd/Ctrl+Y)`"
            @click="$emit('delete-row')"
          >
            <MinusOutlined />
          </button>
          <button type="button" class="dg-icon-btn" :disabled="loading || !isEditing" :title="LABELS.REVERT_ALL" @click="$emit('revert-all')">
            <RollbackOutlined />
          </button>
          <button
            type="button"
            class="dg-icon-btn dg-submit-icon-btn"
            :class="{ 'is-active': isEditing && !loading }"
            :disabled="loading || !isEditing || createSQLLoading"
            :title="pendingChangeCount > 0 ? `${$t('ti-jiao')} (${pendingChangeCount}) (Cmd/Ctrl+Enter)` : `${$t('ti-jiao')} (Cmd/Ctrl+Enter)`"
            @click="$emit('submit')"
          >
            <LoadingOutlined v-if="createSQLLoading" spin />
            <svg v-else class="dg-submit-arrow-svg" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
              <path
                d="M8 2.25L2.75 7.75H6V13.75H10V7.75H13.25L8 2.25Z"
                stroke="currentColor"
                stroke-width="1.35"
                stroke-linejoin="round"
                stroke-linecap="round"
              />
            </svg>
          </button>
        </template>
      </div>

      <span class="dg-sep" />

      <!-- Export -->
      <div class="dg-bar-group">
        <button type="button" class="dg-icon-btn" :disabled="loading" :title="LABELS.EXPORT_TIP" @click="$emit('export')">
          <DownloadOutlined />
        </button>
        <template v-if="exportState">
          <div v-if="exportState.exporting" class="dg-export-status">
            <a-progress :percent="exportState.percent || 0" size="small" style="width: 96px" />
          </div>
          <div v-else-if="exportState.percent === 100 && exportState.downloadFile?.trackId" class="dg-export-status">
            <a class="dg-export-link" @click.prevent="$emit('download-export')">{{ $t('xia-zai') }}</a>
            <a class="dg-export-link is-muted" @click.prevent="$emit('reset-export')">{{ $t('fan-hui') }}</a>
          </div>
          <a-popover
            v-else-if="exportState.errorStatus === 'FAILED' && exportState.errorMessage"
            :content="exportState.errorMessage"
            placement="bottom"
          >
            <span class="dg-export-error">{{ $t('dao-chu-shi-bai') !== 'dao-chu-shi-bai' ? $t('dao-chu-shi-bai') : $t('shi-bai') }}</span>
          </a-popover>
        </template>
      </div>

      <!-- View Modes & Tools (Right side) -->
      <div class="dg-bar-group dg-bar-right">
        <button
          type="button"
          class="dg-icon-btn"
          :class="{ 'is-toggled': isTransposed }"
          :disabled="loading"
          :title="LABELS.TRANSPOSE"
          @click="$emit('toggle-transpose')"
        >
          <SwapOutlined />
        </button>

        <a-popover v-model:open="colPopoverOpen" trigger="click" placement="bottomRight" overlay-class-name="dg-cols-popover">
          <template #content>
            <div class="dg-cols-panel">
              <div class="dg-cols-head">
                <a-input v-model:value="colSearch" size="small" :placeholder="LABELS.SEARCH_COLS" allow-clear />
                <a-button size="small" type="link" @click="$emit('show-all-columns')">{{ LABELS.SHOW_ALL }}</a-button>
              </div>
              <div class="dg-cols-list">
                <div v-for="col in filteredColumns" :key="col.column" class="dg-col-item">
                  <a-checkbox :checked="!hiddenColumnSet[col.column]" @change="$emit('toggle-column-visibility', col.column)" />
                  <span class="dg-col-item-name" :title="col.columnType" @click="$emit('jump-to-column', col.column)">
                    {{ col.column }}
                  </span>
                  <button
                    type="button"
                    class="dg-pin-btn"
                    :class="{ 'is-pinned': Boolean(pinnedColumnSet[col.column]) }"
                    :title="LABELS.PIN_LEFT"
                    @click.stop="$emit('toggle-column-pin', col.column)"
                  >
                    <PushpinFilled v-if="pinnedColumnSet[col.column]" />
                    <PushpinOutlined v-else />
                  </button>
                </div>
              </div>
            </div>
          </template>
          <button
            type="button"
            class="dg-icon-btn"
            :class="{ 'is-toggled': hiddenCount > 0 || colPopoverOpen }"
            :disabled="loading"
            :title="`${LABELS.COLUMNS} (Cmd/Ctrl+F12)`"
          >
            <TableOutlined />
          </button>
        </a-popover>

        <button
          type="button"
          class="dg-icon-btn"
          :class="{ 'is-toggled': findVisible }"
          :disabled="loading"
          :title="LABELS.FIND_TIP"
          @click="$emit('toggle-find')"
        >
          <SearchOutlined />
        </button>
      </div>
    </div>

    <!-- Row 2: DataGrip Filter Criteria & ORDER BY Bar -->
    <div class="dg-bar dg-bar-filter">
      <div class="dg-filter-box">
        <span class="dg-filter-prefix">
          <FilterOutlined />
          <span class="dg-prefix-text">{{ LABELS.WHERE }}</span>
        </span>
        <input
          v-model="tab.condition"
          type="text"
          :placeholder="LABELS.FILTER_PLACEHOLDER"
          class="dg-clause-input"
          @keydown.enter="$emit('fetch-page', 0)"
        />
        <button
          v-if="tab.condition"
          type="button"
          class="dg-clear-btn"
          @click="
            tab.condition = '';
            $emit('fetch-page', 0);
          "
        >
          <CloseCircleFilled />
        </button>
        <a-dropdown v-if="filterHistory.length > 0" trigger="click" placement="bottomRight">
          <template #overlay>
            <a-menu @click="$emit('apply-filter-history', $event.key)">
              <a-menu-item v-for="item in filterHistory" :key="item">
                <span class="dg-history-item">{{ item }}</span>
              </a-menu-item>
            </a-menu>
          </template>
          <button type="button" class="dg-history-btn" :title="LABELS.HISTORY_TIP">
            <HistoryOutlined />
          </button>
        </a-dropdown>
      </div>

      <div class="dg-order-box">
        <span class="dg-filter-prefix">
          <SortAscendingOutlined />
          <span class="dg-prefix-text">{{ LABELS.ORDER_BY }}</span>
        </span>
        <input
          v-model="tab.orderBy"
          type="text"
          :placeholder="LABELS.ORDER_PLACEHOLDER"
          class="dg-clause-input"
          @keydown.enter="$emit('fetch-page', 0)"
        />
        <button
          v-if="tab.orderBy"
          type="button"
          class="dg-clear-btn"
          @click="
            tab.orderBy = '';
            $emit('fetch-page', 0);
          "
        >
          <CloseCircleFilled />
        </button>
      </div>

      <button type="button" class="dg-query-btn" :disabled="loading" @click="$emit('fetch-page', 0)">
        <span class="dg-btn-label">{{ $t('cha-xun') }}</span>
      </button>
    </div>
  </div>
</template>

<style scoped lang="less">
.dg-toolbar-wrapper {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  border-bottom: 1px solid var(--border-light, #e5e7eb);
  background: var(--bg-secondary, #f8fafc);
  user-select: none;
}

.dg-bar {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
}

.dg-bar-primary {
  flex-wrap: wrap;
  min-height: 34px;
  border-bottom: 1px solid var(--border-light, #eef0f3);
}

.dg-bar-filter {
  min-height: 34px;
  gap: 8px;
  background: var(--bg-primary, #ffffff);
}

.dg-bar-group {
  display: inline-flex;
  align-items: center;
  gap: 2px;
}

.dg-bar-right {
  margin-left: auto;
}

.dg-icon-btn {
  width: 26px;
  height: 26px;
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary, #1f2329);
  cursor: pointer;
  line-height: 1;
  transition:
    background-color 0.12s ease,
    border-color 0.12s ease,
    color 0.12s ease;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    line-height: 1;
    vertical-align: middle;
  }

  :deep(svg) {
    display: block;
    width: 14px;
    height: 14px;
  }

  &:hover:not(:disabled) {
    background: var(--bg-hover, rgba(0, 0, 0, 0.06));
  }

  &:active:not(:disabled) {
    background: var(--bg-select, rgba(0, 0, 0, 0.1));
  }

  &.is-toggled {
    color: var(--primary-color, #3ecf8e);
    border-color: rgba(62, 207, 142, 0.35);
    background: rgba(62, 207, 142, 0.12);
  }

  &:disabled {
    opacity: 0.34;
    cursor: not-allowed;
  }
}

.dg-pager-cluster {
  display: inline-flex;
  align-items: center;
  gap: 2px;
  margin: 0 2px;
}

.dg-text-btn {
  height: 26px;
  padding: 0 7px;
  margin: 0;
  box-sizing: border-box;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  border: 1px solid transparent;
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary, #1f2329);
  cursor: pointer;
  line-height: 1;
  transition:
    background-color 0.12s ease,
    border-color 0.12s ease,
    color 0.12s ease;

  &:hover:not(:disabled) {
    background: var(--bg-hover, rgba(0, 0, 0, 0.06));
  }

  &:active:not(:disabled) {
    background: var(--bg-select, rgba(0, 0, 0, 0.1));
  }

  &:disabled {
    opacity: 0.38;
    cursor: not-allowed;
  }
}

.dg-mono-btn .dg-btn-label {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 1;
  font-weight: 500;
}

.dg-btn-label {
  font-size: 12px;
  line-height: 1;
  display: inline-flex;
  align-items: center;
}

.dg-caret {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 9px;
  line-height: 1;
  color: var(--text-secondary, #6b7280);

  :deep(svg) {
    display: block;
    width: 9px;
    height: 9px;
  }
}

.dg-extractor-btn {
  max-width: 150px;
}

.dg-extractor-label {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  line-height: 1;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dg-of {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  line-height: 1;
  color: var(--text-secondary, #6b7280);
  padding: 0 2px;
}

.dg-sep {
  width: 1px;
  height: 16px;
  margin: 0 4px;
  background: var(--border-primary, #d1d5db);
  flex-shrink: 0;
}

.dg-readonly-pill {
  height: 24px;
  padding: 0 8px;
  border-radius: 4px;
  background: var(--bg-tertiary, #f3f4f6);
  border: 1px solid var(--border-light, #e5e7eb);
  color: var(--text-secondary, #6b7280);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  line-height: 1;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    line-height: 1;
  }
}

.dg-submit-icon-btn {
  .dg-submit-arrow-svg {
    display: block;
    width: 15px;
    height: 15px;
  }

  &.is-active {
    color: var(--primary-color, #3ecf8e);
    background: rgba(62, 207, 142, 0.12);
    border-color: rgba(62, 207, 142, 0.35);

    &:hover {
      background: rgba(62, 207, 142, 0.2);
    }
  }
}

.dg-filter-box,
.dg-order-box {
  display: flex;
  align-items: center;
  height: 26px;
  box-sizing: border-box;
  border: 1px solid var(--border-primary, #d1d5db);
  border-radius: 4px;
  background: var(--bg-card, #ffffff);
  padding: 0 6px 0 0;
  overflow: hidden;
  transition:
    border-color 0.15s ease,
    box-shadow 0.15s ease;

  &:hover:not(:focus-within) {
    border-color: var(--text-tertiary, #9ca3af);
  }

  &:focus-within {
    border-color: var(--primary-color, #3ecf8e);
    box-shadow: 0 0 0 2px rgba(62, 207, 142, 0.14);
  }
}

.dg-filter-box {
  flex: 1.6;
  min-width: 200px;
}

.dg-order-box {
  flex: 1;
  min-width: 150px;
}

.dg-filter-prefix {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 0 8px;
  margin-right: 6px;
  gap: 4px;
  background: var(--bg-secondary, #f8fafc);
  border-right: 1px solid var(--border-light, #eef0f3);
  color: var(--text-secondary, #6b7280);
  flex-shrink: 0;
  line-height: 1;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
    width: 12px;
    height: 12px;
  }
}

.dg-prefix-text {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  font-weight: 600;
  line-height: 1;
  color: var(--text-secondary, #6b7280);
}

.dg-clause-input {
  flex: 1;
  min-width: 0;
  height: 100%;
  margin: 0;
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  background: transparent !important;
  color: var(--text-primary, #1f2329);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 24px;
  padding: 0 2px;

  &:focus,
  &:focus-visible {
    border: none !important;
    outline: none !important;
    box-shadow: none !important;
  }

  &::placeholder {
    color: var(--text-tertiary, #9ca3af);
  }
}

.dg-clear-btn {
  width: 18px;
  height: 18px;
  border: none;
  background: transparent;
  color: var(--text-tertiary, #9ca3af);
  cursor: pointer;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

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

  &:hover {
    color: var(--text-secondary, #6b7280);
  }
}

.dg-query-btn {
  height: 26px;
  padding: 0 12px;
  border-radius: 4px;
  border: 1px solid var(--primary-button-bg, #3ecf8e);
  background: var(--primary-button-bg, #3ecf8e);
  color: #ffffff;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  flex-shrink: 0;
  transition: filter 0.12s ease;

  .dg-btn-label {
    color: #ffffff;
  }

  &:hover:not(:disabled) {
    filter: brightness(0.95);
  }

  &:disabled {
    opacity: 0.45;
    cursor: not-allowed;
  }
}

.dg-history-btn {
  width: 20px;
  height: 20px;
  border: none;
  border-radius: 3px;
  background: transparent;
  color: var(--text-secondary, #9ca3af);
  cursor: pointer;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
  }

  &:hover {
    background: var(--bg-hover, rgba(0, 0, 0, 0.05));
    color: var(--primary-color, #1677ff);
  }
}

.dg-history-item {
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 12px;
}

.dg-page-size-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 56px;
}

.dg-cols-panel {
  width: 260px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.dg-cols-head {
  display: flex;
  align-items: center;
  gap: 6px;
}

.dg-cols-list {
  max-height: 280px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.dg-col-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 3px 6px;
  border-radius: 4px;

  &:hover {
    background: var(--bg-secondary, #f3f4f6);
  }
}

.dg-col-item-name {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 12px;
  line-height: 18px;
  cursor: pointer;
}

.dg-pin-btn {
  width: 22px;
  height: 22px;
  border: none;
  border-radius: 4px;
  background: transparent;
  color: var(--text-secondary, #9ca3af);
  cursor: pointer;
  padding: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;

  :deep(.anticon) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    line-height: 1;
  }

  :deep(svg) {
    display: block;
  }

  &:hover,
  &.is-pinned {
    color: var(--primary-color, #1677ff);
  }
}

.dg-export-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-left: 6px;
  line-height: 1;

  :deep(.ant-progress) {
    margin: 0;
    line-height: 1;
  }
}

.dg-export-link {
  font-size: 12px;
  color: var(--primary-color, #13c27d);
  cursor: pointer;
  white-space: nowrap;

  &:hover {
    text-decoration: underline;
  }

  &.is-muted {
    color: var(--text-secondary, #6b7280);
  }
}

.dg-export-error {
  margin-left: 6px;
  font-size: 12px;
  color: #dc2626;
  cursor: pointer;
  white-space: nowrap;
}
</style>
