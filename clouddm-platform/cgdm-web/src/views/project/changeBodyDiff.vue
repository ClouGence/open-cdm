<template>
  <div ref="container" class="monaco-container"></div>
</template>

<script>
import * as monaco from 'monaco-editor';

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
    theme: {
      type: String,
      default: 'vs'
    }
  },
  mounted() {
    this.editor = monaco.editor.createDiffEditor(this.$refs.container, {
      theme: this.theme,
      automaticLayout: true,
      readOnly: true
    });

    const originalModel = monaco.editor.createModel(this.original, this.language);
    const modifiedModel = monaco.editor.createModel(this.modified, this.language);

    this.editor.setModel({
      original: originalModel,
      modified: modifiedModel
    });
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
