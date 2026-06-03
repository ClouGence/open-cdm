<template>
  <div ref="container" class="monaco-container"></div>
</template>

<script>
import * as monaco from 'monaco-editor';
import { mapState } from 'vuex';
import { resolveSqlEditorLanguage } from '@/components/editor/sqlLanguage';

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
  async mounted() {
    this.editor = monaco.editor.createDiffEditor(this.$refs.container, {
      theme: this.theme,
      automaticLayout: true,
      readOnly: true
    });

    const language = await resolveSqlEditorLanguage(monaco, this.dsType, this.getDsSettings(), this.language);
    const originalModel = monaco.editor.createModel(this.original, language);
    const modifiedModel = monaco.editor.createModel(this.modified, language);

    this.editor.setModel({
      original: originalModel,
      modified: modifiedModel
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
  }
};
</script>

<style scoped>
.monaco-container {
  width: 100%;
  height: 100%;
}
</style>
