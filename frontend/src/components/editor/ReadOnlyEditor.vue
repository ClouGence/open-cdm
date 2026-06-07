<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import { mapState } from 'vuex';
import { applySqlEditorLanguage, resolveSqlEditorLanguage } from './sqlLanguage';

const DEFAULT_LINE_HEIGHT = 22;
const DEFAULT_VERTICAL_PADDING = 10;

export default {
  name: 'ReadOnlyEditor',
  props: {
    maxHeight: Number,
    text: {
      type: String,
      default: ''
    },
    language: {
      type: String,
      default: 'sql'
    },
    dsType: {
      type: String,
      default: ''
    },
    border: {
      type: Number,
      default: 1
    },
    collapsible: {
      type: Boolean,
      default: false
    },
    collapsedMaxLines: {
      type: Number,
      default: 20
    },
    expandText: {
      type: String,
      default: '展开查看'
    }
  },
  watch: {
    text(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.expanded = false;
        this.createEditor();
      }
    },
    dsType() {
      this.applyLanguage();
    }
  },
  data() {
    return {
      monacoEditor: null,
      expanded: false,
      contentHeight: 0,
      lineHeight: DEFAULT_LINE_HEIGHT,
      contentSizeDisposable: null
    };
  },
  mounted() {
    this.createEditor();
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting']),
    lineCount() {
      return Math.max(1, this.text ? this.text.split('\n').length : 1);
    },
    verticalPadding() {
      if (!this.contentHeight || !this.lineHeight) {
        return DEFAULT_VERTICAL_PADDING;
      }
      return Math.max(DEFAULT_VERTICAL_PADDING, this.contentHeight - this.lineCount * this.lineHeight);
    },
    collapsedHeight() {
      const maxLines = Math.max(1, this.collapsedMaxLines);
      return maxLines * this.lineHeight + this.verticalPadding;
    },
    actualContentHeight() {
      if (this.contentHeight) {
        return Math.max(this.lineHeight + this.verticalPadding, this.contentHeight);
      }
      return this.lineCount * this.lineHeight + this.verticalPadding;
    },
    needCollapse() {
      return this.collapsible && !this.maxHeight && this.actualContentHeight > this.collapsedHeight;
    },
    height() {
      if (this.collapsible && !this.maxHeight) {
        return Math.ceil(this.needCollapse && !this.expanded ? this.collapsedHeight : this.actualContentHeight);
      }
      if (!this.maxHeight) {
        const arr = this.text ? this.text.split('\n') : '';
        return arr.length > 25 ? 25 * 22 + 25 : arr.length < 5 ? 5 * 22 : arr.length * 22 + 25;
      } else {
        return this.maxHeight;
      }
    },
    borderStyle() {
      return this.border > 0 ? `${this.border}px solid #ccc` : 'none';
    }
  },
  methods: {
    async createEditor() {
      if (this.text) {
        if (this.monacoEditor) {
          this.monacoEditor.getModel().setValue(this.text);
          this.applyLanguage();
          this.refreshEditorMetrics();
        } else {
          const language = await this.resolveLanguage();
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.readOnlyEditor, {
              value: this.text, // 编辑器的值
              language,
              fontSize: 14,
              fontWeight: 'bold',
              scrollBeyondLastLine: false,
              readOnly: true,
              theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
              minimap: {
                enabled: false
              },
              scrollbar: {
                vertical: this.collapsible ? 'hidden' : 'auto',
                alwaysConsumeMouseWheel: !this.collapsible
              },
              overviewRulerLanes: 0,
              hideCursorInOverviewRuler: true,
              automaticLayout: true,
              autoIndent: true // 自动缩进
            })
          );
          this.contentSizeDisposable = this.monacoEditor.onDidContentSizeChange(() => {
            this.refreshEditorMetrics();
          });
          this.refreshEditorMetrics();
        }
      }
    },
    refreshEditorMetrics() {
      if (!this.monacoEditor) {
        return;
      }
      this.lineHeight = this.monacoEditor.getOption(monaco.editor.EditorOption.lineHeight) || DEFAULT_LINE_HEIGHT;
      this.contentHeight = this.monacoEditor.getContentHeight();
      this.$nextTick(() => {
        if (this.monacoEditor) {
          this.monacoEditor.layout();
        }
      });
    },
    handleExpand() {
      this.expanded = true;
      this.$nextTick(() => {
        if (this.monacoEditor) {
          this.monacoEditor.layout();
        }
      });
    },
    handleWheel(event) {
      if (this.needCollapse && !this.expanded) {
        event.preventDefault();
        event.stopPropagation();
      }
    },
    resolveLanguage() {
      return resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), this.language);
    },
    applyLanguage() {
      return applySqlEditorLanguage(monaco, this.monacoEditor, this.dsType, this.getDsSettings(), this.language);
    },
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
    }
  },
  beforeUnmount() {
    if (this.contentSizeDisposable) {
      this.contentSizeDisposable.dispose();
      this.contentSizeDisposable = null;
    }
    if (this.monacoEditor) {
      this.monacoEditor.dispose();
    }
  }
};
</script>

<template>
  <div
    class="read-only-editor-wrapper"
    :class="{ collapsible, collapsed: needCollapse && !expanded }"
    :style="{ border: borderStyle }"
    @wheel="handleWheel"
  >
    <div class="read-only-editor" ref="readOnlyEditor" :style="`height: ${height}px;`"></div>
    <button v-if="needCollapse && !expanded" type="button" class="read-only-editor-expand" @click="handleExpand">
      {{ expandText }}
    </button>
  </div>
</template>

<style scoped lang="less">
.read-only-editor-wrapper {
  position: relative;
  width: 100%;
  overflow: hidden;

  &.collapsed {
    padding-bottom: 0;
  }
}

.read-only-editor {
  width: 100%;
}

.read-only-editor-expand {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
  height: 34px;
  border: 0;
  border-top: 1px solid #e8eaec;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.18), #fff 46%);
  color: #2d8cf0;
  cursor: pointer;
  font-size: 14px;
  line-height: 34px;
  text-align: center;
}

:deep(.message) {
  display: none;
}

:deep(.below) {
  display: none;
}
</style>
