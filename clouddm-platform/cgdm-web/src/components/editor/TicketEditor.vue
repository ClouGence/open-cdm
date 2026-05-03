<script>
import * as monaco from 'monaco-editor';
import { MySQL, PostgresSQL, RedisSQL, StarRocksSQL } from '@/components/editor/core';
import { getLanguage } from '@/utils/tools';
import { markRaw, nextTick } from 'vue';

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
  mounted() {
    this.createEditor();
  },
  methods: {
    createEditor() {
      if (!this.monacoEditor) {
        // 使用markRaw防止Monaco Editor实例被Vue3响应式系统包装
        this.monacoEditor = markRaw(
          monaco.editor.create(this.$refs.ticketEditor, {
            value: this.text, // 编辑器的值
            language: getLanguage(this.dataSourceType),
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
      this.registerCompletion(getLanguage(this.dataSourceType));
      this.setParser();
    },
    registerCompletion(lang) {
      const providerItem = monaco.languages.registerCompletionItemProvider(lang, {
        triggerCharacters: [' ', '.', '`', '/'],
        provideCompletionItems: (model, position) => {
          this.sortText = 0;
          let suggestions = [];

          const { lineNumber, column } = position;

          const textUntilPosition = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: lineNumber,
            endColumn: column
          });

          const syntaxSuggestions = this.currentParser.getSuggestionAtCaretPosition(textUntilPosition, position);

          if (syntaxSuggestions) {
            const { keywords } = syntaxSuggestions;

            if (keywords.length) {
              suggestions = suggestions.concat(this.getSQLSuggest(keywords));
            }
          }

          return {
            suggestions
          };
        }
      });
    },
    setParser() {
      switch (this.dsType) {
        case 'Redis':
          this.currentParser = new RedisSQL();
          break;
        case 'Mysql':
        case 'TiDB':
          this.currentParser = new MySQL();
          break;
        case 'Oracle':
        case 'PostgreSQL':
        case 'Greenplum':
        case 'SQLServer':
          this.currentParser = new PostgresSQL();
          break;
        case 'StarRocks':
          this.currentParser = new StarRocksSQL();
          break;
        default:
          this.currentParser = new MySQL();
      }
    },
    getSQLSuggest(keywords) {
      const list = keywords.map((key) => ({
        label: key,
        kind: monaco.languages.CompletionItemKind.Keyword,
        detail: `[${this.$t('guan-jian-zi')}]`,
        sortText: `${this.sortText++}`.padStart(8, '0'),
        insertText: `${key}`
      }));

      return list;
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
