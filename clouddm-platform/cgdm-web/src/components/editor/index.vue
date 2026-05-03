<script>
import * as monaco from 'monaco-editor';
import * as actions from 'monaco-editor/esm/vs/platform/actions/common/actions';
import { mapGetters } from 'vuex';
import { markRaw, toRaw } from 'vue';
import browseMixin from '@/mixins/browseMixin';
import { getQuick } from '@/components/editor/snippets/quick';
import { getFunction } from '@/components/editor/snippets/functions';
import { format } from 'sql-formatter';
import { getLanguage } from '@/utils/tools';
import { MySQL, RedisSQL, StarRocksSQL, PostgresSQL } from './core';
import { registerMongoDBLanguage } from './languages/mongodb';

export default {
  name: 'Editor',
  props: {
    onRun: Function,
    completionData: Object,
    currentTab: {
      type: Object,
      default: () => {}
    },
    setEditorInstance: Function,
    rdbObjectDetail: Function,
    storeQueryTabs: Function
  },
  mixins: [browseMixin],
  data() {
    return {
      language: 'sql',
      currentSql: null,
      currentPosition: {},
      currentDecoration: null,
      flinkParser: null,
      currentParser: null,
      defaultOpts: {
        value: '', // 编辑器的值
        language: 'mysql',
        fontSize: 12,
        fontWeight: 'bold',
        tabSize: 4,
        lineNumbersMinChars: 3,
        scrollBeyondLastLine: false,
        theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
        minimap: {
          enabled: false
        },
        automaticLayout: true,
        autoIndent: true // 自动缩进
      },
      monacoEditor: null,
      monacoEditorFountCss: 'font-size-14',
      completionItemProviderList: [],
      commandList: [],
      actionList: [],
      hoverProviderList: [],
      fontSizePanelExpanded: false
    };
  },
  computed: {
    ...mapGetters(['getLevels', 'getLeafGroup', 'genQualifierText', 'removeQualifierText', 'getEditor'])
  },
  mounted() {
    registerMongoDBLanguage();
    this.init();
  },
  methods: {
    getFormatterSql() {
      switch (this.currentTab.dsType) {
        case 'MySql':
        case 'TiDB':
          return 'mysql';
        case 'Oracle':
          return 'plsql';
        case 'SQLServer':
          return 'tsql';
        case 'PostgreSQL':
        case 'Greenplum':
          return 'postgresql';
        case 'Db2':
          return 'db2';
        case 'MongoDB':
          return 'sql';
        default:
          return 'sql';
      }
    },
    setParser() {
      switch (this.currentTab.dsType) {
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
        case 'MongoDB':
          // MongoDB 使用 JavaScript 语法，暂不需要 SQL parser
          this.currentParser = null;
          break;
        default:
          this.currentParser = new MySQL();
      }
    },
    init() {
      this.currentTab.language = getLanguage(this.currentTab.dsType);
      this.defaultOpts.language = this.currentTab.language;
      this.defaultOpts.value = this.currentTab.text;
      // MongoDB 使用自定义主题以支持语法高亮
      if (this.currentTab.dsType === 'MongoDB') {
        this.defaultOpts.theme = 'vs-mongodb';
      } else {
        this.defaultOpts.theme = 'vs';
      }
      if (this.monacoEditor) {
        this.handleDispose();
      }
      this.monacoEditor = markRaw(monaco.editor.create(this.$refs.monacoEditor, this.defaultOpts));
      this.registerCompletion(this.defaultOpts.language);
      this.registerCommands();
      this.registerHover(this.defaultOpts.language);
      this.addActions();
      this.setEditorInstance(this.monacoEditor);
      this.monacoEditor.onMouseDown((event) => {
        const decorations = this.monacoEditor.getLineDecorations(event.target.position.lineNumber);
        if (decorations) {
          let glyphMarginDecoration;
          decorations.forEach((decoration) => {
            if (
              decoration &&
              decoration.options &&
              decoration.options.glyphMarginClassName &&
              decoration.options.glyphMarginClassName.includes('glyph-margin-rule')
            ) {
              glyphMarginDecoration = decoration;
            }
          });

          if (glyphMarginDecoration) {
            console.log(glyphMarginDecoration);
            console.log(glyphMarginDecoration.options.description);
          }
        }
      });
      this.monacoEditor.onDidPaste((e) => {
        console.log('onDidPaste', e);
      });
      this.monacoEditor.onDidChangeCursorSelection((e) => {
        // console.log('onDidChangeCursorSelection', e);
        const { selection } = e;
        const { startLineNumber, startColumn, endLineNumber, endColumn } = selection;
        if (startLineNumber !== endLineNumber || startColumn !== endColumn) {
          if (this.currentDecoration) {
            this.currentDecoration.clear();
            this.currentDecoration = null;
          }
        }
      });
      this.monacoEditor.onDidChangeModelContent(() => {
        // this.validateSql();
        this.$emit('change', toRaw(this.monacoEditor.getValue()));
      });
      this.setParser();
    },
    validateSql() {
      if (!this.currentParser) {
        return;
      }
      const allSql = this.monacoEditor.getValue();
      const sqlSlices = this.currentParser.splitSQLByStatement(allSql);
      console.log('validate sql', sqlSlices);
      const markers = [];
      if (sqlSlices) {
        sqlSlices.forEach((sql) => {
          const errors = this.currentParser.validate(sql.text);
          if (errors) {
            errors.forEach((error) => {
              markers.push({
                startLineNumber: error.startLine,
                startColumn: error.startCol,
                endLineNumber: error.endLine,
                endColumn: error.endCol,
                message: error.message, // 提示文案
                severity: monaco.MarkerSeverity.Error // 提示的类型
              });
            });
          }
        });

        monaco.editor.setModelMarkers(this.monacoEditor.getModel(), 'mysql', markers);
      }
    },
    handleSetDecorations(position) {
      const decorations = [
        {
          range: new monaco.Range(1, 1, 1, 1),
          options: {
            glyphMarginHoverMessage: {
              value: this.$t('gui-ze-zu-sai')
            },
            description: this.$t('zu-sai-xiang-qing'),
            isWholeLine: true,
            inlineClassName: 'line-rule-failure',
            glyphMarginClassName: 'glyph-margin-rule-failure'
          }
        },
        {
          range: new monaco.Range(3, 1, 3, 1),
          options: {
            glyphMarginHoverMessage: {
              value: this.$t('gui-ze-ti-shi')
            },
            description: this.$t('ti-shi-xiang-qing'),
            isWholeLine: true,
            inlineClassName: 'line-rule-suggest',
            glyphMarginClassName: 'glyph-margin-rule-suggest'
          }
        }
      ];
      this.monacoEditor.createDecorationsCollection(decorations);
    },
    // 去除无用的右键菜单
    removeUnUseMenuItems() {
      const menus = actions.MenuRegistry._menuItems;
      const contextMenuEntry = Array.from(menus, ([key, value]) => ({ key, value })).find((entry) => entry.key.id === 'EditorContext');

      const removableIds = [
        'editor.action.clipboardCutAction',
        'editor.action.clipboardCopyAction',
        'editor.action.clipboardPasteAction',
        'editor.action.refactor',
        'editor.action.sourceAction',
        'editor.action.revealDefinition',
        'editor.action.revealDeclaration',
        'editor.action.goToTypeDefinition',
        'editor.action.goToImplementation',
        'editor.action.goToReferences',
        'editor.action.formatDocument',
        'editor.action.formatSelection',
        'editor.action.changeAll',
        'editor.action.rename',
        'editor.action.quickOutline',
        'editor.action.quickCommand',
        'Peek'
      ];
      const removeById = (list, ids) => {
        let node = list._first;
        do {
          const shouldRemove = ids.includes(node.element?.command?.id);
          if (shouldRemove) {
            list._remove(node);
          }
          // eslint-disable-next-line no-cond-assign
        } while ((node = node.next));
      };
      removeById(contextMenuEntry.value, removableIds);
    },
    // 右键菜单
    addActions() {
      this.removeUnUseMenuItems();
      this.monacoEditor.addAction({
        id: 'run',
        label: this.$t('yun-hang'),
        keybindings: [monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter],
        contextMenuGroupId: 'navigation',
        run: () => {
          this.onRun('run');
        }
      });
      this.monacoEditor.addAction({
        id: 'formatter',
        label: this.$t('ge-shi-hua'),
        keybindings: [monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyL],
        contextMenuGroupId: 'navigation',
        run: () => {
          this.formatSql();
        }
      });
    },
    registerCommands() {
      monaco.editor.registerCommand('editor.action.triggerTableSuggest', (_, ...args) => {
        if (args[1] === 'TABLE') {
          this.rdbObjectDetail(args[0], { selected: false, expand: false });
        }
      });
    },
    registerCompletion(lang) {
      const providerItem = monaco.languages.registerCompletionItemProvider(lang, {
        triggerCharacters: [' ', '.', '`', '/', '$'],
        provideCompletionItems: (model, position) => {
          console.log('registerCompletion', position);
          this.sortText = 0;
          let suggestions = [];

          const { lineNumber, column } = position;

          const textUntilPosition = toRaw(model).getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: lineNumber,
            endColumn: column
          });

          console.log(textUntilPosition);
          if (textUntilPosition === '/') {
            suggestions = suggestions.concat(this.getQuickSuggest(position));
          } else if (this.currentTab.dsType === 'MongoDB') {
            // MongoDB 特殊处理：提供函数建议
            suggestions = suggestions.concat(this.getFunctionSuggest(this.currentTab.dsType));
            suggestions = suggestions.concat(this.getQuickSuggest(position));
          } else if (this.currentParser) {
            const syntaxSuggestions = toRaw(this.currentParser).getSuggestionAtCaretPosition(textUntilPosition, toRaw(position));
            console.log('syntaxSuggestions', syntaxSuggestions);

            if (syntaxSuggestions) {
              const { keywords, syntax } = syntaxSuggestions;

              if (syntax.length) {
                syntax.forEach((item) => {
                  if (item.syntaxContextType === 'table') {
                    suggestions = suggestions.concat(this.getTableSuggest('TABLE'));
                  }
                  if (item.syntaxContextType === 'key') {
                    suggestions = suggestions.concat(this.getTableSuggest('KEY'));
                  }
                  if (item.syntaxContextType === 'column') {
                    suggestions = suggestions.concat(this.getColumnSuggest(item));
                  }
                  if (item.syntaxContextType === 'function') {
                    suggestions = suggestions.concat(this.getFunctionSuggest(this.currentTab.dsType));
                  }
                });
              }

              if (keywords.length) {
                suggestions = suggestions.concat(this.getSQLSuggest(keywords));
              }
            }
          }

          return {
            suggestions
          };
        }
      });
      this.completionItemProviderList.push(providerItem);
    },
    registerHover(lang) {
      const providerItem = monaco.languages.registerHoverProvider(lang, {
        provideHover: (model, position) => {
          const word = model.getWordAtPosition(position);
          if (word && word.word) {
            const table =
              this.completionData[this.currentTab.node.key][this.currentTab.leafType] &&
              this.completionData[this.currentTab.node.key][this.currentTab.leafType][word.word];
            let value = `|  ${this.$t('lie-ming')}   | ${this.$t('shu-xing')}  |\n|  ----  | ----  |`;
            if (table && table.columnList && table.columnList.length) {
              table.columnList.forEach((column) => {
                value += `\n| ${column.title}  | ${column.tips} |`;
              });

              return {
                contents: [
                  {
                    value
                  }
                ]
              };
            }
          }
        }
      });
      this.hoverProviderList.push(providerItem);
    },
    formatSql() {
      const selectionRange = this.monacoEditor.getSelection();
      let range = selectionRange;
      let sql = toRaw(this.monacoEditor.getModel()).getValueInRange(selectionRange);
      if (!sql) {
        sql = toRaw(this.monacoEditor).getValue();
        range = this.monacoEditor.getModel().getFullModelRange();
      }
      const formatterSql = format(sql, {
        language: this.getFormatterSql(),
        tabWidth: 2,
        keywordCase: 'upper',
        expressionWidth: 100
      });

      this.monacoEditor.executeEdits(null, [
        {
          text: formatterSql,
          range
        }
      ]);

      this.monacoEditor.pushUndoStop();
    },
    getCurrentSql() {
      const position = this.monacoEditor.getPosition();
      const allSql = this.monacoEditor.getValue();

      if (!this.currentParser) {
        // MongoDB 或其他没有 parser 的情况，返回全部内容
        return allSql;
      }

      const sqlSlice = this.currentParser.getSuggestionAtCaretPosition(allSql, position);
      let currentSql = null;

      if (sqlSlice) {
        currentSql = sqlSlice.currentSql;
      }

      return currentSql;
    },
    setFontSize(size) {
      if (this.monacoEditor) {
        this.monacoEditor.updateOptions({ fontSize: size });
        this.monacoEditorFountCss = `font-size-${size}`;
      }
    },
    // 获取 SQL 语法提示
    getQuickSuggest(position) {
      return getQuick(this.currentTab.dsType).map((quick) => ({
        label: quick.label,
        kind: monaco.languages.CompletionItemKind.Function,
        // insertText: 'if(${1:logical_expression}, ${2:value_if_true}, ${3:value_if_false})',
        sortText: `${this.sortText++}`.padStart(8, '0'),
        // eslint-disable-next-line no-template-curly-in-string
        insertText: quick.insertText,
        detail: quick.detail,
        insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
        additionalTextEdits: [
          {
            range: {
              startLineNumber: position.lineNumber,
              endLineNumber: position.lineNumber,
              startColumn: position.column - 1,
              endColumn: position.column
            }
          }
        ]
      }));
    },
    getFunctionSuggest(position) {
      console.log(getFunction(this.currentTab.dsType));
      return getFunction(this.currentTab.dsType).map((f) => ({
        label: f,
        kind: monaco.languages.CompletionItemKind.Function,
        // insertText: 'if(${1:logical_expression}, ${2:value_if_true}, ${3:value_if_false})',
        sortText: `${this.sortText++}`.padStart(8, '0'),
        // eslint-disable-next-line no-template-curly-in-string
        insertText: `${f}(\${1:})`,
        detail: `[${this.$t('han-shu')}]`,
        insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
        additionalTextEdits: [
          {
            range: {
              startLineNumber: position.lineNumber,
              endLineNumber: position.lineNumber,
              startColumn: position.column - 1,
              endColumn: position.column
            }
          }
        ]
      }));
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
    getColumnSuggest(snippet) {
      const list = [];
      const { node, leafType } = this.currentTab;
      const { wordRanges } = snippet;
      const tableListObj = this.completionData[node.key][leafType];
      Object.keys(tableListObj).forEach((tableKey) => {
        const table = tableListObj[tableKey];
        if (table.columnList && table.columnList.length) {
          table.columnList.forEach((child) => {
            list.push({
              label: `${child.title} (${tableKey})`,
              kind: monaco.languages.CompletionItemKind.Enum,
              detail: `[${this.$t('lie')}]${child.tips}`,
              sortText: `${this.sortText++}`.padStart(8, '0'),
              insertText: this.genQualifierText(node.INSTANCE.attr.dsType, child.title)
            });
          });
        }
      });

      return list;
    },
    getTableSuggest(leafType = this.currentTab.leafType) {
      const { node } = this.currentTab;
      const list = [];
      let leafKey = {};
      this.currentTab.leafGroup.forEach((leaf) => {
        if (leaf.type === leafType) {
          leafKey = leaf;
        }
      });
      if (leafKey.type && this.completionData[node.key] && this.completionData[node.key][leafKey.type]) {
        Object.values(this.completionData[this.currentTab.node.key][leafKey.type]).forEach((table) => {
          list.push({
            label: table.objName,
            kind: monaco.languages.CompletionItemKind.Method,
            detail: `[${leafKey.i18n}]`,
            sortText: `${this.sortText++}`.padStart(8, '0'),
            insertText: this.genQualifierText(node.INSTANCE.attr.dsType, table.objName),
            command: {
              id: 'editor.action.triggerTableSuggest',
              title: 'triggerTableSuggest',
              arguments: [table.objName, leafKey.type]
            }
          });
        });
      }

      return list;
    },
    handleDispose() {
      this.completionItemProviderList.forEach((provider) => {
        provider.dispose();
      });
      this.hoverProviderList.forEach((provider) => {
        provider.dispose();
      });
      this.commandList.forEach((command) => {
        command.dispose();
      });
      this.actionList.forEach((action) => {
        action.dispose();
      });
      this.monacoEditor.dispose();
    }
  },
  beforeUnmount() {
    this.handleDispose();
  },
  watch: {
    'currentTab.key': {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.init();
        }
      }
    }
  }
};
</script>

<template>
  <div class="monaco-editor">
    <div class="font-size-buttons" @mouseenter="fontSizePanelExpanded = true" @mouseleave="fontSizePanelExpanded = false">
      <ButtonGroup>
        <Button size="small" v-if="fontSizePanelExpanded" @click="setFontSize(12)">
          {{ $t('zi-hao-1') }}
        </Button>
        <Button size="small" v-if="fontSizePanelExpanded" @click="setFontSize(16)">
          {{ $t('zi-hao-2') }}
        </Button>
        <Button size="small" v-if="fontSizePanelExpanded" @click="setFontSize(20)">
          {{ $t('zi-hao-3') }}
        </Button>
        <Button size="small" v-if="!fontSizePanelExpanded">T</Button>
      </ButtonGroup>
    </div>
    <div :class="`${monacoEditorFountCss} monaco-editor-content`" ref="monacoEditor"></div>
  </div>
</template>

<style scoped lang="less">
:deep(.font-size-12) {
  .view-line {
    * {
      font-size: 12px;
    }
  }
}
:deep(.font-size-16) {
  .view-line {
    * {
      font-size: 16px;
    }
  }
}
:deep(.font-size-20) {
  .view-line {
    * {
      font-size: 20px;
    }
  }
}

.monaco-editor {
  height: 250px;
  width: 100%;
}
.monaco-editor-content {
  height: 100%;
  width: 100%;
  position: relative;
}

.monaco-editor .font-size-buttons {
  position: absolute;
  top: 3px;
  right: 17px;
  z-index: 10;
  display: flex;
  gap: 5px;
}
</style>
