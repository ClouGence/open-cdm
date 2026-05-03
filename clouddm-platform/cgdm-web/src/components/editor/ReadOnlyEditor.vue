<script>
import * as monaco from 'monaco-editor';
import { markRaw } from 'vue';

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
    border: {
      type: Number,
      default: 1
    }
  },
  watch: {
    text(newVal, oldVal) {
      if (newVal && newVal !== oldVal) {
        this.createEditor();
      }
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
    height() {
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
    createEditor() {
      if (this.text) {
        if (this.monacoEditor) {
          this.monacoEditor.getModel().setValue(this.text);
        } else {
          this.monacoEditor = markRaw(
            monaco.editor.create(this.$refs.readOnlyEditor, {
              value: this.text, // 编辑器的值
              language: this.language,
              fontSize: 14,
              fontWeight: 'bold',
              scrollBeyondLastLine: false,
              readOnly: true,
              theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
              minimap: {
                enabled: false
              },
              automaticLayout: true,
              autoIndent: true // 自动缩进
            })
          );
        }
      }
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
  <div class="read-only-editor" ref="readOnlyEditor" :style="`height: ${height}px; border: ${borderStyle};`"></div>
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
