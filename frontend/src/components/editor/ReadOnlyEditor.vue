<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import { mapState } from 'vuex';
import { applySqlEditorLanguage, resolveSqlEditorLanguage } from './sqlLanguage';

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
    fontWeight: {
      type: [Number, String],
      default: 'bold'
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
          this.monacoEditor.getModel().setValue(this.text);
          this.applyLanguage();
        } else {
          const language = await this.resolveLanguage();
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.readOnlyEditor, {
              value: this.text, // The editor 's value
              language,
              fontSize: 14,
              fontWeight: this.fontWeight,
              scrollBeyondLastLine: false,
              readOnly: true,
              theme: 'vs', // Editor theme: vs, hc-black, or vs-dark; more options in the official docs.
              minimap: {
                enabled: false
              },
              scrollbar: {
                vertical: 'auto',
                verticalScrollbarSize: 5,
                horizontalScrollbarSize: 8,
                alwaysConsumeMouseWheel: true
              },
              overviewRulerLanes: 0,
              hideCursorInOverviewRuler: true,
              automaticLayout: true,
              autoIndent: true // Auto Indent
            })
          );
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
      this.viewportHeight = Math.max(minimumHeight, viewportHeight - editorTop - 20);
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
  <div class="read-only-editor-wrapper" :style="{ border: borderStyle }">
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

:deep(.monaco-scrollable-element > .scrollbar) {
  border-radius: 1em;
  background-color: rgba(50, 50, 50, 0.1);
}

:deep(.monaco-scrollable-element > .scrollbar > .slider) {
  border-radius: 1em;
  background-color: rgba(50, 50, 50, 0.3) !important;
}
</style>
