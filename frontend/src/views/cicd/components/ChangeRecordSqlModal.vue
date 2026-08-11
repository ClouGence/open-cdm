<template>
  <CCModal
    :model-value="modelValue"
    :title="modalTitle"
    :width="1120"
    footer-hide
    @update:model-value="handleVisibleChange"
    @on-cancel="handleVisibleChange(false)"
  >
    <div class="change-record-sql-modal">
      <Spin v-if="loading" fix />

      <template v-if="mode === 'sql'">
        <div v-if="sqlContent" class="change-record-sql-preview" @wheel="handleSqlPreviewWheel">
          <ReadOnlyEditor
            ref="sqlPreviewEditor"
            :key="record?.changeId"
            :text="sqlContent"
            :max-height="520"
            :ds-type="record?.dsType"
            :border="0"
            :font-weight="400"
            :content-padding="12"
            virtual-scroll-mode
            :line-number-start="sqlPreviewStartLine"
            @viewport-line-count-change="handleSqlPreviewViewportChange"
          />
          <input
            v-if="sqlPreviewInitialized && sqlPreviewMaxStartLine > 1"
            v-model.number="sqlPreviewStartLine"
            class="change-record-sql-scrollbar"
            type="range"
            min="1"
            :max="sqlPreviewMaxStartLine"
            step="1"
            :aria-label="$t('ticket-sql-virtual-scrollbar')"
            aria-orientation="vertical"
            @input="scheduleSqlPreview"
          />
        </div>
        <CCEmptyContent v-else-if="sqlPreviewInitialized" :content="emptyMessage" />
      </template>

      <template v-else>
        <Collapse v-if="diffItems.length" v-model="activeDiffPanels" accordion @on-change="handleDiffPanelChange">
          <Panel v-for="(item, index) in diffItems" :key="item.contentName || index" :name="index.toString()">
            {{ item.contentName || '-' }}
            <template #content>
              <div class="change-record-diff-editor">
                <ChangeBodyDiff
                  v-if="item.loaded"
                  :original="item.oldBody || ''"
                  :modified="item.newBody || ''"
                  language="sql"
                  :ds-type="record?.dsType"
                />
                <CCEmptyContent v-else loading :content="$t('bian-geng-nei-rong-fen-xi-zhong')" />
              </div>
            </template>
          </Panel>
        </Collapse>
        <CCEmptyContent v-else-if="diffInitialized" :content="emptyMessage" />
      </template>
    </div>
  </CCModal>
</template>

<script>
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CCEmptyContent from '@/components/widgets/CCEmptyContent';
import ChangeBodyDiff from '../changeBodyDiff.vue';

export default {
  name: 'ChangeRecordSqlModal',
  components: { ChangeBodyDiff, ReadOnlyEditor, CCEmptyContent },
  props: {
    modelValue: { type: Boolean, required: true },
    record: { type: Object, default: null },
    mode: {
      type: String,
      default: 'sql',
      validator(value) {
        return ['sql', 'diff'].includes(value);
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      loading: false,
      requestSequence: 0,
      sqlContent: '',
      sqlPreviewStartLine: 1,
      sqlPreviewTotalLines: 1,
      sqlPreviewLineCount: 30,
      sqlPreviewInitialized: false,
      sqlPreviewTimer: null,
      diffItems: [],
      diffInitialized: false,
      activeDiffPanels: []
    };
  },
  computed: {
    modalTitle() {
      if (this.mode === 'diff') {
        return this.$t('bian-geng-diff');
      }
      return this.$t('sql-bian-geng-nei-rong');
    },
    emptyMessage() {
      return this.record?.remark || this.$t('wu-bian-geng-nei-rong');
    },
    sqlPreviewMaxStartLine() {
      return Math.max(1, this.sqlPreviewTotalLines - this.sqlPreviewLineCount + 1);
    }
  },
  watch: {
    modelValue(visible) {
      if (visible) {
        this.open();
        return;
      }
      this.reset();
    }
  },
  beforeUnmount() {
    this.reset();
  },
  methods: {
    handleVisibleChange(visible) {
      this.$emit('update:modelValue', visible);
    },
    async open() {
      this.reset();
      if (!this.record?.changeId) {
        return;
      }
      if (this.mode === 'diff') {
        await this.loadDiffItems();
        return;
      }
      await this.loadSqlPreview();
    },
    reset() {
      this.requestSequence += 1;
      if (this.sqlPreviewTimer) {
        window.clearTimeout(this.sqlPreviewTimer);
        this.sqlPreviewTimer = null;
      }
      this.loading = false;
      this.sqlContent = '';
      this.sqlPreviewStartLine = 1;
      this.sqlPreviewTotalLines = 1;
      this.sqlPreviewLineCount = 30;
      this.sqlPreviewInitialized = false;
      this.diffItems = [];
      this.diffInitialized = false;
      this.activeDiffPanels = [];
    },
    async loadSqlPreview() {
      const editor = this.$refs.sqlPreviewEditor;
      this.sqlPreviewLineCount = Math.min(200, editor?.getVisibleLineCount() || this.sqlPreviewLineCount);
      const requestSequence = ++this.requestSequence;
      this.loading = true;
      try {
        const res = await this.$services.dmCicdChangeSqlPreview({
          data: {
            changeId: this.record.changeId,
            startLine: this.sqlPreviewStartLine,
            lineCount: this.sqlPreviewLineCount
          }
        });
        if (requestSequence !== this.requestSequence || !res.success) {
          return;
        }
        this.sqlPreviewStartLine = res.data?.startLine || 1;
        this.sqlPreviewTotalLines = res.data?.totalLines || 1;
        this.sqlContent = res.data?.content || '';
        this.sqlPreviewInitialized = true;
      } finally {
        if (requestSequence === this.requestSequence) {
          this.loading = false;
        }
      }
    },
    async loadDiffItems() {
      const requestSequence = ++this.requestSequence;
      this.loading = true;
      try {
        const res = await this.$services.dmCicdChangeSqlPreview({
          data: {
            changeId: this.record.changeId,
            startLine: 1,
            lineCount: 1
          }
        });
        if (requestSequence !== this.requestSequence || !res.success) {
          return;
        }
        this.diffItems = (res.data?.itemList || []).map((item) => ({
          ...item,
          loaded: typeof item.oldBody === 'string' || typeof item.newBody === 'string'
        }));
        this.diffInitialized = true;
      } finally {
        if (requestSequence === this.requestSequence) {
          this.loading = false;
        }
      }
    },
    async handleDiffPanelChange(name) {
      const selected = Array.isArray(name) ? name[0] : name;
      if (selected === undefined || selected === null || selected === '') {
        return;
      }
      const index = Number(selected);
      const item = this.diffItems[index];
      if (!item || item.loaded) {
        return;
      }
      const requestSequence = this.requestSequence;
      const res = await this.$services.dmCicdChangeSqlPreview({
        data: {
          changeId: this.record.changeId,
          contentName: item.contentName,
          startLine: 1,
          lineCount: 1
        }
      });
      if (requestSequence !== this.requestSequence || !res.success || !res.data?.itemList?.length) {
        return;
      }
      this.diffItems.splice(index, 1, { ...res.data.itemList[0], loaded: true });
    },
    scheduleSqlPreview() {
      if (this.sqlPreviewTimer) {
        window.clearTimeout(this.sqlPreviewTimer);
      }
      this.sqlPreviewTimer = window.setTimeout(() => this.loadSqlPreview(), 120);
    },
    handleSqlPreviewViewportChange(lineCount) {
      if (!lineCount || lineCount === this.sqlPreviewLineCount) {
        return;
      }
      this.sqlPreviewLineCount = lineCount;
      this.scheduleSqlPreview();
    },
    handleSqlPreviewWheel(event) {
      if (!this.sqlPreviewInitialized) {
        return;
      }
      event.preventDefault();
      const step = Math.max(1, Math.floor(this.sqlPreviewLineCount / 3));
      const delta = event.deltaY > 0 ? step : -step;
      this.sqlPreviewStartLine = Math.max(1, Math.min(this.sqlPreviewMaxStartLine, this.sqlPreviewStartLine + delta));
      this.scheduleSqlPreview();
    }
  }
};
</script>

<style lang="less" scoped>
.change-record-sql-modal {
  position: relative;
  min-height: 520px;
}

.change-record-sql-preview {
  position: relative;
  min-height: 520px;
  padding-right: 18px;
}

.change-record-sql-scrollbar {
  position: absolute;
  z-index: 3;
  top: 8px;
  right: 3px;
  width: 14px;
  height: calc(100% - 16px);
  margin: 0;
  writing-mode: vertical-lr;
  direction: ltr;
  appearance: none;
  background: transparent;
  cursor: pointer;

  &::-webkit-slider-runnable-track {
    width: 6px;
    height: 100%;
    border-radius: 3px;
    background: var(--bg-secondary);
  }

  &::-webkit-slider-thumb {
    width: 8px;
    height: 28px;
    border: 0;
    border-radius: 4px;
    appearance: none;
    background: var(--text-tertiary);
  }
}

.change-record-diff-editor {
  height: 480px;
  min-height: 0;
}

:deep(.ivu-collapse) {
  border: 0;
  background: transparent;
}

:deep(.ivu-collapse > .ivu-collapse-item) {
  border-top: 1px solid var(--border-light);
}

:deep(.ivu-collapse > .ivu-collapse-item:first-child) {
  border-top: 0;
}

:deep(.ivu-collapse-content) {
  padding: 0;
}

:deep(.ivu-collapse-content > .ivu-collapse-content-box) {
  padding: 8px 0 0;
}

@media (max-width: 767px) {
  .change-record-sql-modal,
  .change-record-sql-preview {
    min-height: 420px;
  }

  .change-record-diff-editor {
    height: 400px;
  }
}
</style>
