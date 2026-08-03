<script>
import * as monaco from 'monaco-editor';
import { getLanguage } from '@/utils/tools';
import { markRaw, nextTick } from 'vue';
import { mapState } from 'vuex';
import { applySqlEditorLanguage, resolveSqlEditorLanguage } from './sqlLanguage';

export default {
  name: 'TicketEditor',
  props: {
    dataSourceType: {
      type: String,
      default: 'sql'
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    virtualScrollMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      monacoEditor: null,
      dsType: this.dataSourceType
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting'])
  },
  watch: {
    dataSourceType(newVal) {
      this.dsType = newVal;
      this.applyLanguage();
    },
    readOnly(newVal) {
      this.monacoEditor?.updateOptions({ readOnly: newVal });
    },
    virtualScrollMode(newVal) {
      this.updateScrollbarMode(newVal);
    }
  },
  mounted() {
    this.createEditor();
  },
  methods: {
    async createEditor() {
      if (!this.monacoEditor) {
        const language = await this.resolveLanguage();
        // Use mark Raw to prevent the example of Monaco Editor from being packaged by the Vue3 Response System
        this.monacoEditor = markRaw(
          monaco.editor.create(this.$refs.ticketEditor, {
            value: this.text, // The editor 's value
            language,
            fontSize: 14,
            fontWeight: 'bold',
            scrollBeyondLastLine: false,
            theme: 'vs', // Editor theme: vs, hc-black, or vs-dark; more options in the official docs.
            minimap: {
              enabled: false
            },
            lineNumbers: 'on',
            scrollbar: this.scrollbarOptions(this.virtualScrollMode),
            automaticLayout: true,
            readOnly: this.readOnly,
            autoIndent: true // Auto Indent
          })
        );
      }
    },
    resolveLanguage() {
      return resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), getLanguage(this.dsType));
    },
    applyLanguage() {
      return applySqlEditorLanguage(monaco, this.monacoEditor, this.dsType, this.getDsSettings(), getLanguage(this.dsType));
    },
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
    },
    scrollbarOptions(virtualScrollMode) {
      return {
        vertical: virtualScrollMode ? 'hidden' : 'auto',
        handleMouseWheel: !virtualScrollMode,
        alwaysConsumeMouseWheel: !virtualScrollMode
      };
    },
    updateScrollbarMode(virtualScrollMode) {
      this.monacoEditor?.updateOptions({
        scrollbar: this.scrollbarOptions(virtualScrollMode)
      });
    },
    getSql() {
      if (this.monacoEditor) {
        try {
          // Get values in nextTick to avoid Vue3 response system loop dependence
          return this.monacoEditor.getValue();
        } catch (error) {
          return '';
        }
      }
      return '';
    },
    // GetSQL, a different version, recommended for use in Vue3
    async getSqlAsync() {
      if (this.monacoEditor) {
        try {
          await nextTick();
          return this.monacoEditor.getValue();
        } catch (error) {
          return '';
        }
      }
      return '';
    },
    setSql(sql, lineNumberStart = 1) {
      if (this.monacoEditor) {
        this.monacoEditor.updateOptions({
          lineNumbers: this.lineNumberOption(lineNumberStart)
        });
        this.monacoEditor.setValue(sql);
      }
    },
    lineNumberOption(lineNumberStart) {
      const start = Math.max(1, Number(lineNumberStart) || 1);
      return start === 1 ? 'on' : (lineNumber) => String(start + lineNumber - 1);
    },
    getVisibleLineCount() {
      if (!this.monacoEditor) {
        return 30;
      }
      const layout = this.monacoEditor.getLayoutInfo();
      const lineHeight = this.monacoEditor.getOption(monaco.editor.EditorOption.lineHeight);
      return Math.max(1, Math.floor(layout.height / lineHeight));
    }
  },
  beforeUnmount() {
    if (this.monacoEditor) {
      this.monacoEditor.dispose();
    }
  }
};
</script>

<template>
  <div class="ticket-editor" ref="ticketEditor" style="height: 100%"></div>
</template>

<style scoped lang="less">
.ticket-editor {
  width: 100%;
  height: 100%;
  min-height: 0;
}

:deep(.message) {
  display: none;
}

:deep(.below) {
  display: none;
}
</style>
