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
    }
  },
  mounted() {
    this.createEditor();
  },
  methods: {
    async createEditor() {
      if (!this.monacoEditor) {
        const language = await this.resolveLanguage();
        // 使用markRaw防止Monaco Editor实例被Vue3响应式系统包装
        this.monacoEditor = markRaw(
          monaco.editor.create(this.$refs.ticketEditor, {
            value: this.text, // 编辑器的值
            language,
            fontSize: 14,
            fontWeight: 'bold',
            scrollBeyondLastLine: false,
            theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
            minimap: {
              enabled: false
            },
            automaticLayout: true,
            autoIndent: true // 自动缩进
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
    getSql() {
      if (this.monacoEditor) {
        try {
          // 在nextTick中获取值，避免Vue3响应式系统的循环依赖
          return this.monacoEditor.getValue();
        } catch (error) {
          console.error('Monaco Editor getValue error:', error);
          return '';
        }
      }
      return '';
    },
    // 异步版本的getSql，推荐在Vue3中使用
    async getSqlAsync() {
      if (this.monacoEditor) {
        try {
          await nextTick();
          return this.monacoEditor.getValue();
        } catch (error) {
          console.error('Monaco Editor getValue error:', error);
          return '';
        }
      }
      return '';
    },
    setSql(sql) {
      if (this.monacoEditor) {
        try {
          this.monacoEditor.setValue(sql);
        } catch (error) {
          console.error('Monaco Editor setValue error:', error);
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
  <div class="ticket-editor" ref="ticketEditor" style="height: 100%"></div>
</template>

<style scoped lang="less">
.ticket-editor {
  width: 100%;
}

:deep(.message) {
  display: none;
}

:deep(.below) {
  display: none;
}
</style>
