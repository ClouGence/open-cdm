<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import { mapState } from 'vuex';
import { applySqlEditorLanguage, resolveSqlEditorLanguage } from './sqlLanguage';
import { SQL_EDITOR_SCROLLBAR, SQL_EDITOR_TYPOGRAPHY } from './sqlEditorTypography';

const DEFAULT_LINE_HEIGHT = 22;
const DEFAULT_VERTICAL_PADDING = 25;

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
    fitViewport: {
      type: Boolean,
      default: false
    },
    viewportBottomOffset: {
      type: Number,
      default: 20
    },
    virtualScrollMode: {
      type: Boolean,
      default: false
    },
    lineNumberStart: {
      type: Number,
      default: 1
    },
    contentPadding: {
      type: Number,
      default: 0
    }
  },
  watch: {
    text(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.createEditor();
      }
    },
    dsType() {
      this.applyLanguage();
    },
    virtualScrollMode(newVal) {
      this.updateScrollbarMode(newVal);
    },
    lineNumberStart() {
      this.updateLineNumberStart();
    }
  },
  data() {
    return {
      monacoEditor: null,
      viewportHeight: 0
    };
  },
  mounted() {
    this.createEditor();
    if (this.fitViewport) {
      this.$nextTick(() => {
        this.updateViewportHeight();
        window.addEventListener('resize', this.updateViewportHeight);
      });
    }
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting']),
    height() {
      let targetHeight;
      if (!this.maxHeight) {
        const arr = this.text ? this.text.split('\n') : '';
        targetHeight = arr.length * DEFAULT_LINE_HEIGHT + DEFAULT_VERTICAL_PADDING;
        if (arr.length > 25) {
          targetHeight = 25 * DEFAULT_LINE_HEIGHT + DEFAULT_VERTICAL_PADDING;
        }
        if (arr.length < 5) {
          targetHeight = 5 * DEFAULT_LINE_HEIGHT;
        }
      } else {
        targetHeight = this.maxHeight;
      }
      if (this.fitViewport && this.viewportHeight) {
        if (this.virtualScrollMode) {
          return this.viewportHeight;
        }
        return Math.min(targetHeight, this.viewportHeight);
      }
      return targetHeight;
    },
    borderStyle() {
      return this.border > 0 ? `${this.border}px solid #ccc` : 'none';
    }
  },
  methods: {
    async createEditor() {
      if (this.text) {
        if (this.monacoEditor) {
          const viewState = this.monacoEditor.saveViewState();
          this.monacoEditor.getModel().setValue(this.text);
          if (viewState) {
            this.monacoEditor.restoreViewState(viewState);
          }
          this.applyLanguage();
        } else {
          const language = await this.resolveLanguage();
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.readOnlyEditor, {
              value: this.text, // The editor 's value
              language,
              ...SQL_EDITOR_TYPOGRAPHY,
              scrollBeyondLastLine: false,
              readOnly: true,
              domReadOnly: true,
              contextmenu: false,
              theme: 'vs', // Editor theme: vs, hc-black, or vs-dark; more options in the official docs.
              padding: {
                top: this.contentPadding,
                bottom: this.contentPadding
              },
              minimap: {
                enabled: false
              },
              lineNumbers: this.lineNumberOption(),
              scrollbar: {
                ...SQL_EDITOR_SCROLLBAR,
                vertical: this.virtualScrollMode ? 'hidden' : 'auto',
                handleMouseWheel: !this.virtualScrollMode,
                alwaysConsumeMouseWheel: !this.virtualScrollMode
              },
              overviewRulerLanes: 0,
              hideCursorInOverviewRuler: true,
              automaticLayout: true,
              autoIndent: true // Auto Indent
            })
          );
          this.monacoEditor.onDidScrollChange((event) => {
            if (!event.scrollTopChanged) {
              return;
            }
            const viewportHeight = this.monacoEditor.getLayoutInfo().height;
            const remainingHeight = this.monacoEditor.getScrollHeight() - event.scrollTop - viewportHeight;
            if (remainingHeight <= DEFAULT_LINE_HEIGHT * 2) {
              this.$emit('reach-bottom');
            }
          });
        }
        this.$nextTick(() => {
          this.updateViewportHeight();
          if (this.monacoEditor) {
            this.monacoEditor.layout();
          }
        });
      }
    },
    updateViewportHeight() {
      if (!this.fitViewport || !this.$el) {
        return;
      }
      const viewportHeight = document.documentElement.clientHeight || window.innerHeight;
      const editorTop = this.$el.getBoundingClientRect().top;
      const minimumHeight = 5 * DEFAULT_LINE_HEIGHT;
      this.viewportHeight = Math.max(minimumHeight, viewportHeight - editorTop - this.viewportBottomOffset);
      this.$nextTick(() => {
        if (this.monacoEditor) {
          this.monacoEditor.layout();
          this.$emit('viewport-line-count-change', this.getVisibleLineCount());
        }
      });
    },
    resolveLanguage() {
      return resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), this.language);
    },
    applyLanguage() {
      return applySqlEditorLanguage(monaco, this.monacoEditor, this.dsType, this.getDsSettings(), this.language);
    },
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
    },
    updateScrollbarMode(virtualScrollMode) {
      this.monacoEditor?.updateOptions({
        scrollbar: {
          ...SQL_EDITOR_SCROLLBAR,
          vertical: virtualScrollMode ? 'hidden' : 'auto',
          handleMouseWheel: !virtualScrollMode,
          alwaysConsumeMouseWheel: !virtualScrollMode
        }
      });
    },
    updateLineNumberStart() {
      this.monacoEditor?.updateOptions({
        lineNumbers: this.lineNumberOption()
      });
    },
    lineNumberOption() {
      const start = Math.max(1, Number(this.lineNumberStart) || 1);
      return start === 1 ? 'on' : (lineNumber) => String(start + lineNumber - 1);
    },
    getVisibleLineCount() {
      if (!this.monacoEditor) {
        const editorHeight = this.$refs.readOnlyEditor?.clientHeight || (this.fitViewport ? this.viewportHeight : 0);
        if (editorHeight > 0) {
          return Math.max(1, Math.floor(editorHeight / DEFAULT_LINE_HEIGHT));
        }
        return 25;
      }
      const layout = this.monacoEditor.getLayoutInfo();
      const lineHeight = this.monacoEditor.getOption(monaco.editor.EditorOption.lineHeight);
      return Math.max(1, Math.floor(layout.height / lineHeight));
    },
    preventCommandPalette(event) {
      const key = event.key.toLowerCase();
      const isCommandPaletteShortcut = event.key === 'F1' || ((event.metaKey || event.ctrlKey) && event.shiftKey && key === 'p');
      if (!isCommandPaletteShortcut) {
        return;
      }
      event.preventDefault();
      event.stopImmediatePropagation();
    }
  },
  beforeUnmount() {
    if (this.fitViewport) {
      window.removeEventListener('resize', this.updateViewportHeight);
    }
    if (this.monacoEditor) {
      this.monacoEditor.dispose();
    }
  }
};
</script>

<template>
  <div class="read-only-editor-wrapper" :style="{ border: borderStyle }" @keydown.capture="preventCommandPalette">
    <div class="read-only-editor" ref="readOnlyEditor" :style="`height: ${height}px;`"></div>
  </div>
</template>

<style scoped lang="less">
.read-only-editor-wrapper {
  position: relative;
  width: 100%;
  overflow: hidden;
}

.read-only-editor {
  width: 100%;
}

:deep(.message) {
  display: none;
}

:deep(.below) {
  display: none;
}
</style>
