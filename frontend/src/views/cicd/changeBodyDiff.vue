<template>
  <div ref="container" class="monaco-container"></div>
</template>

<script>
import * as monaco from 'monaco-editor';
import { mapState } from 'vuex';
import { resolveSqlEditorLanguage } from '@/components/editor/sqlLanguage';
import { SQL_EDITOR_SCROLLBAR, SQL_EDITOR_TYPOGRAPHY } from '@/components/editor/sqlEditorTypography';

export default {
  name: 'MonacoDiff',
  props: {
    original: {
      type: String,
      required: true
    },
    modified: {
      type: String,
      required: true
    },
    language: {
      type: String,
      default: 'sql'
    },
    dsType: {
      type: String,
      default: ''
    },
    theme: {
      type: String,
      default: 'vs'
    }
  },
  computed: {
    ...mapState(['dmGlobalSetting', 'globalDsSetting'])
  },
  watch: {
    original(value) {
      if (this.originalModel && this.originalModel.getValue() !== value) {
        this.originalModel.setValue(value);
      }
    },
    modified(value) {
      if (this.modifiedModel && this.modifiedModel.getValue() !== value) {
        this.modifiedModel.setValue(value);
      }
    }
  },
  async mounted() {
    this.editor = monaco.editor.createDiffEditor(this.$refs.container, {
      theme: this.theme,
      ...SQL_EDITOR_TYPOGRAPHY,
      scrollbar: SQL_EDITOR_SCROLLBAR,
      automaticLayout: true,
      readOnly: true
    });

    const language = await resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), this.language);
    this.originalModel = monaco.editor.createModel(this.original, language);
    this.modifiedModel = monaco.editor.createModel(this.modified, language);

    this.editor.setModel({
      original: this.originalModel,
      modified: this.modifiedModel
    });
  },
  methods: {
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
    }
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.dispose();
    }
    if (this.originalModel) {
      this.originalModel.dispose();
    }
    if (this.modifiedModel) {
      this.modifiedModel.dispose();
    }
  }
};
</script>

<style scoped>
.monaco-container {
  width: 100%;
  height: 100%;
}
</style>
