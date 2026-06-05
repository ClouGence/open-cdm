<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';
import { mapState } from 'vuex';
import { resolveSqlEditorLanguage } from './sqlLanguage';

export default {
  name: 'ReadOnlyDiffEditor',
  props: {
    maxHeight: Number,
    originalSql: {
      type: String,
      default: ''
    },
    modifiedSql: {
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
    }
  },
  watch: {
    originalSql(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.createEditor();
      }
    },
    modifiedSql(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.createEditor();
      }
    },
    dsType() {
      this.createEditor();
    }
  },
  data() {
    return {
      monacoEditor: null
    };
  },
  mounted() {
    this.createEditor();
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting']),
    borderStyle() {
      return this.border > 0 ? `${this.border}px solid #ccc` : 'none';
    }
  },
  methods: {
    async createEditor() {
      if (this.originalSql && this.modifiedSql) {
        const language = await this.resolveLanguage();
        const originalModel = monaco.editor.createModel(this.originalSql, language);
        const modifiedModel = monaco.editor.createModel(this.modifiedSql, language);
        if (this.monacoEditor) {
          const oldModel = this.monacoEditor.getModel();
          this.monacoEditor.setModel({
            original: originalModel,
            modified: modifiedModel
          });
          oldModel?.original?.dispose();
          oldModel?.modified?.dispose();
        } else {
          this.monacoEditor = markRaw(
            monaco.editor.createDiffEditor(this.$refs.readOnlyEditor, {
              language: this.language,
              fontSize: 14,
              fontWeight: 'bold',
              scrollBeyondLastLine: false,
              renderSideBySide: true,
              splitViewDefaultRatio: 0.5, // always split into two equal panes
              enableSplitViewResizing: false, // lock the 50/50 split
              renderSideBySideInlineBreakpoint: 0, // never collapse to inline view because of width
              useInlineViewWhenSpaceIsLimited: false, // keep two panes even on narrow screens
              readOnly: true,
              theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
              minimap: {
                enabled: false
              },
              automaticLayout: true,
              autoIndent: true // 自动缩进
            })
          );

          this.monacoEditor.setModel({
            original: originalModel,
            modified: modifiedModel
          });
        }
      }
    },
    resolveLanguage() {
      return resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), this.language);
    },
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
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
  <div>
    <div class="flex">
      <div class="flex-1 font-bold">{{ $t('xiu-gai-qian') }}</div>
      <div class="flex-1 font-bold">{{ $t('xiu-gai-hou') }}</div>
    </div>
    <div class="read-only-editor" ref="readOnlyEditor" :style="`height: 400px; border: ${borderStyle};`"></div>
  </div>
</template>

<style scoped lang="less">
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
