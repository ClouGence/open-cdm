<script>
import * as monaco from 'monaco-editor';
import * as actions from 'monaco-editor/esm/vs/platform/actions/common/actions';
import { mapGetters, mapState } from 'vuex';
import { markRaw, toRaw } from 'vue';
import browseMixin from '@/mixins/browseMixin';
import { getLanguage } from '@/utils/tools';
import { getPluginResourceUrl } from '@/utils/pluginResource';
import { requestWebSocket } from '@/services/socket';
import { WS_TYPE } from '@/utils';
import { getDsSetting, resolveSqlEditorLanguage } from './sqlLanguage';

const LANGUAGE_COMPLETION_DELAY_MS = 200;
const LANGUAGE_SPLIT_DELAY_MS = 500;
const MAX_LANGUAGE_FRAGMENT_BYTES = 1024 * 1024;
const DS_LANGUAGE_ERROR = '10111';
const STATIC_KEYWORD_WEIGHT = 100;

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
      fontSizePanelExpanded: false,
      languageRequestVersion: 0,
      validateTimer: null,
      splitTimer: null,
      editorDisposableList: [],
      keywordResourceCache: {},
      diagnosticDecorationCollection: null,
      currentStatementOverlayElement: null,
      currentStatementOverlayFrame: null,
      splitStatements: [],
      splitModelVersionId: 0,
      activeSplitStatement: null,
      languageServiceErrorMessage: '',
      languageServiceErrorTimer: null,
      completionIconMap: {},
      suggestIconObserver: null,
      suggestIconFrame: null
    };
  },
  computed: {
    ...mapGetters(['getLevels', 'getLeafGroup', 'genQualifierText', 'removeQualifierText', 'getEditor']),
    ...mapState(['dmGlobalSetting', 'globalDsSetting'])
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.currentTab.language = await resolveSqlEditorLanguage(
        monaco,
        this.currentTab.dsType,
        this.getDsSettings(),
        getLanguage(this.currentTab.dsType)
      );
      this.defaultOpts.language = this.currentTab.language;
      this.defaultOpts.value = this.currentTab.text;
      this.defaultOpts.theme = 'vs';
      if (this.monacoEditor) {
        this.handleDispose();
      }
      this.monacoEditor = markRaw(monaco.editor.create(this.$refs.monacoEditor, this.defaultOpts));
      this.registerCompletion(this.defaultOpts.language);
      this.registerCommands();
      this.registerHover(this.defaultOpts.language);
      this.addActions();
      this.installSuggestIconRenderer();
      this.setEditorInstance(this.monacoEditor);
      this.installCurrentStatementOverlay();
      this.editorDisposableList.push(
        this.monacoEditor.onDidScrollChange(() => {
          this.renderCurrentStatementOverlay();
        }),
        this.monacoEditor.onDidLayoutChange(() => {
          this.renderCurrentStatementOverlay();
        })
      );
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
          this.clearCurrentStatementDecorations();
          return;
        }
        this.updateCurrentSqlStatementDecoration();
      });
      this.monacoEditor.onDidChangeModelContent(() => {
        this.invalidateSplitStatements();
        this.debounceSplitSql();
        this.debounceValidateSql();
        this.$emit('change', toRaw(this.monacoEditor.getValue()));
      });
      this.debounceSplitSql(0);
    },
    async validateSql() {
      const language = this.getDsLanguageCapability();
      if (this.isDsLanguageSupport('VALIDATE', language)) {
        const fragments = this.getLanguageFragments();
        if (!fragments.length) {
          this.applyBackendDiagnostics([]);
          return;
        }

        const model = this.monacoEditor.getModel();
        const versionId = model.getVersionId();
        const markers = [];
        for (const fragment of fragments) {
          if (this.isLanguageFragmentTooLarge(fragment.sqlText)) {
            continue;
          }

          let result = null;
          try {
            const response = await this.requestLanguageWebSocket('VALIDATE', this.buildLanguageRequest(null, fragment));
            if (this.handleLanguageServiceStatus(response)) {
              this.applyBackendDiagnostics([]);
              return;
            }
            if (this.handleLanguageServiceMessage(response)) {
              this.applyBackendDiagnostics([]);
              return;
            }
            result = response?.result;
          } catch {
            continue;
          }
          if (result) {
            markers.push(...(result.diagnostics || []).map((diagnostic) => this.toMonacoMarker(diagnostic)));
          }
        }

        if (model.isDisposed() || model.getVersionId() !== versionId) {
          return;
        }
        if (!markers.length) {
          this.applyBackendDiagnostics([]);
          return;
        }
        this.applyBackendDiagnostics(markers);
        return;
      }

      this.applyBackendDiagnostics([]);
    },
    debounceValidateSql() {
      if (this.validateTimer) {
        clearTimeout(this.validateTimer);
      }
      this.validateTimer = setTimeout(() => {
        this.validateSql().catch(() => {});
      }, 500);
    },
    clearAllDiagnostics() {
      const model = this.monacoEditor.getModel();
      monaco.editor.setModelMarkers(model, 'ds-language', []);
      monaco.editor.setModelMarkers(model, 'mysql', []);
      if (this.diagnosticDecorationCollection) {
        this.diagnosticDecorationCollection.clear();
      }
    },
    toMonacoMarker(diagnostic) {
      const range = diagnostic.range || {};
      const start = range.startPosition || {};
      const end = range.endPosition || {};
      const startLineNumber = Math.max(1, start.lineNumber || start.line || 1);
      const startColumn = Math.max(1, (start.columnNumber ?? start.column ?? 0) + 1);
      const endLineNumber = Math.max(startLineNumber, end.lineNumber || end.line || startLineNumber);
      const endColumn = Math.max(startColumn + 1, (end.columnNumber ?? end.column ?? start.columnNumber ?? start.column ?? 0) + 1);

      return {
        startLineNumber,
        startColumn,
        endLineNumber,
        endColumn,
        message: this.formatDiagnosticMessage(diagnostic.message || diagnostic.msg || diagnostic.errorMessage),
        severity: this.toMonacoMarkerSeverity(diagnostic.severity)
      };
    },
    formatDiagnosticMessage(message) {
      if (!message) {
        return 'SQL 语法错误';
      }

      let match = message.match(/^mismatched input '(.+)' expecting (.+)$/);
      if (match) {
        return `语法错误：不应出现 "${match[1]}"，此处应为 ${this.formatExpectedTokens(match[2])}。`;
      }

      match = message.match(/^extraneous input '(.+)' expecting (.+)$/);
      if (match) {
        return `语法错误：多余的输入 "${match[1]}"，此处应为 ${this.formatExpectedTokens(match[2])}。`;
      }

      match = message.match(/^missing (.+) at '(.+)'$/);
      if (match) {
        return `语法错误：在 "${match[2]}" 附近缺少 ${this.formatExpectedTokens(match[1])}。`;
      }

      match = message.match(/^no viable alternative at input (.+)$/);
      if (match) {
        return `语法错误：无法识别 "${match[1]}" 附近的 SQL 结构。`;
      }

      return message;
    },
    formatExpectedTokens(expected) {
      return expected
        .replace(/^\{|\}$/g, '')
        .split(',')
        .map((token) => this.formatExpectedToken(token.trim()))
        .filter(Boolean)
        .join(' 或 ');
    },
    formatExpectedToken(token) {
      const normalized = token.replace(/^'|'$/g, '');
      const tokenNameMap = {
        '<EOF>': '语句结束',
        EOF: '语句结束',
        '--': '"--" 注释',
        ';': '分号',
        ID: '标识符',
        IDENTIFIER: '标识符'
      };
      return tokenNameMap[normalized] || `"${normalized}"`;
    },
    applyBackendDiagnostics(markers) {
      const model = this.monacoEditor.getModel();
      monaco.editor.setModelMarkers(model, 'ds-language', markers);
      monaco.editor.setModelMarkers(model, 'mysql', []);

      if (this.diagnosticDecorationCollection) {
        this.diagnosticDecorationCollection.clear();
      }
    },
    clearBackendDiagnostics() {
      monaco.editor.setModelMarkers(this.monacoEditor.getModel(), 'ds-language', []);
      if (this.diagnosticDecorationCollection) {
        this.diagnosticDecorationCollection.clear();
      }
    },
    showNoExecutableSqlError(message) {
      this.showLanguageServiceError(message);
    },
    toMonacoMarkerSeverity(severity) {
      const severityMap = {
        ERROR: monaco.MarkerSeverity.Error,
        WARNING: monaco.MarkerSeverity.Warning,
        INFO: monaco.MarkerSeverity.Info,
        HINT: monaco.MarkerSeverity.Hint
      };
      return severityMap[severity] || monaco.MarkerSeverity.Error;
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
        provideCompletionItems: async (model, position) => {
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
          if (this.isDsLanguageSupport('COMPLETE')) {
            suggestions = await this.getDelayedBackendCompletionSuggest(model, position);
          } else {
            suggestions = suggestions.concat(await this.getFallbackKeywordSuggest());
          }

          this.updateCompletionIconMap(suggestions);
          this.scheduleSuggestIconRender();
          return {
            suggestions
          };
        }
      });
      this.completionItemProviderList.push(providerItem);
    },
    updateCompletionIconMap(suggestions) {
      const iconMap = {};
      suggestions.forEach((item) => {
        const label = this.getCompletionLabelText(item.label);
        if (!label) {
          return;
        }
        if (item?.icon) {
          iconMap[label] = item.icon;
        }
      });
      this.completionIconMap = iconMap;
    },
    installSuggestIconRenderer() {
      if (this.suggestIconObserver) {
        this.suggestIconObserver.disconnect();
      }

      this.suggestIconObserver = new MutationObserver(() => {
        this.scheduleSuggestIconRender();
      });
      this.suggestIconObserver.observe(document.body, {
        childList: true,
        subtree: true,
        attributes: true,
        attributeFilter: ['class', 'aria-selected', 'style']
      });
    },
    scheduleSuggestIconRender() {
      if (this.suggestIconFrame) {
        cancelAnimationFrame(this.suggestIconFrame);
      }
      this.suggestIconFrame = requestAnimationFrame(() => {
        this.suggestIconFrame = null;
        this.renderSuggestIcons();
      });
    },
    renderSuggestIcons() {
      document.querySelectorAll('.suggest-widget .monaco-list-row').forEach((row) => {
        const label = row.querySelector('.monaco-icon-label .label-name')?.textContent?.trim();
        const icon = label ? this.completionIconMap[label] : null;
        const iconEl = row.querySelector('.contents .main .suggest-icon');
        if (!iconEl) {
          return;
        }
        if (!icon || !/^[A-Z0-9_-]+$/.test(icon)) {
          row.classList.remove('cgdm-completion-row');
          row.removeAttribute('data-cgdm-icon');
          if (iconEl.hasAttribute('data-cgdm-icon')) {
            iconEl.classList.remove('cgdm-completion-icon');
            iconEl.removeAttribute('data-cgdm-icon');
            iconEl.innerHTML = '';
          }
          return;
        }

        iconEl.innerHTML = '';
        Array.from(iconEl.classList)
          .filter((className) => className === 'codicon' || className.startsWith('codicon-'))
          .forEach((className) => iconEl.classList.remove(className));

        const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        const use = document.createElementNS('http://www.w3.org/2000/svg', 'use');
        use.setAttributeNS('http://www.w3.org/1999/xlink', 'href', `#icon-svg-${icon}`);
        use.setAttribute('href', `#icon-svg-${icon}`);
        svg.setAttribute('aria-hidden', 'true');
        svg.setAttribute('class', 'cgdm-completion-svg');
        svg.setAttribute('style', 'width:16px;height:16px;display:block;');
        svg.appendChild(use);

        iconEl.appendChild(svg);
        row.classList.add('cgdm-completion-row');
        row.setAttribute('data-cgdm-icon', icon);
        iconEl.classList.add('cgdm-completion-icon');
        iconEl.setAttribute('data-cgdm-icon', icon);
      });
    },
    getDsLanguageCapability() {
      return this.getCurrentDsSetting()?.language || null;
    },
    isDsLanguageSupport(support, language = this.getDsLanguageCapability()) {
      if (!language?.supported) {
        return false;
      }
      if (Array.isArray(language.supports)) {
        return language.supports.includes(support);
      }
      const legacyField = {
        COMPLETE: 'completion',
        VALIDATE: 'validate',
        SPLIT: 'split'
      }[support];
      return legacyField ? !!language[legacyField] : false;
    },
    getCurrentDsSetting() {
      const dsType = this.currentTab?.dsType || this.currentTab?.node?.INSTANCE?.attr?.dsType;
      return getDsSetting(this.getDsSettings(), dsType);
    },
    getDsSettings() {
      return this.dmGlobalSetting?.dsSettingDef || this.globalDsSetting || {};
    },
    getLanguageRequestLevels() {
      const node = this.currentTab?.node;
      if (!node) {
        return [];
      }

      const configuredLevels = this.getCurrentDsSetting()?.categories?.levels;
      if (!Array.isArray(configuredLevels) || !configuredLevels.length) {
        return this.browseGenLevelsData(node);
      }

      return configuredLevels
        .map((level) => this.findNodeLevelKey(node, level))
        .filter((level) => level && node[level]?.id !== undefined && node[level]?.id !== null)
        .map((level) => node[level].id);
    },
    findNodeLevelKey(node, level) {
      if (node[level]) {
        return level;
      }
      return Object.keys(node).find((key) => key.toLowerCase() === `${level}`.toLowerCase());
    },
    debounceSplitSql(delay = LANGUAGE_SPLIT_DELAY_MS) {
      if (this.splitTimer) {
        clearTimeout(this.splitTimer);
      }
      this.splitTimer = setTimeout(() => {
        this.splitTimer = null;
        this.splitSql().catch(() => {});
      }, delay);
    },
    async splitSql() {
      const language = this.getDsLanguageCapability();
      if (!this.isDsLanguageSupport('SPLIT', language)) {
        this.invalidateSplitStatements();
        return;
      }

      const model = this.monacoEditor?.getModel();
      if (!model) {
        this.invalidateSplitStatements();
        return;
      }

      const sqlText = model.getValue();
      if (!sqlText.trim() || this.isLanguageFragmentTooLarge(sqlText)) {
        this.invalidateSplitStatements();
        return;
      }

      const modelVersionId = model.getVersionId();
      const request = this.buildLanguageRequest(null, {
        sqlText,
        startOffset: 0,
        startPosition: {
          lineNumber: 1,
          columnNumber: 0
        }
      });

      let response = null;
      try {
        response = await this.requestLanguageWebSocket('SPLIT', request);
      } catch {
        return;
      }
      if (model.isDisposed() || model.getVersionId() !== modelVersionId) {
        return;
      }
      if (this.handleLanguageServiceStatus(response) || this.handleLanguageServiceMessage(response)) {
        this.invalidateSplitStatements();
        return;
      }

      const result = response?.result;
      if (!result || result.requestVersion !== request.requestVersion) {
        return;
      }

      this.splitStatements = (result.statements || []).filter((statement) => statement?.sql?.trim() && statement?.range);
      this.splitModelVersionId = modelVersionId;
      this.updateCurrentSqlStatementDecoration();
    },
    invalidateSplitStatements() {
      this.splitStatements = [];
      this.splitModelVersionId = 0;
      this.activeSplitStatement = null;
      this.clearCurrentStatementDecorations();
    },
    clearCurrentStatementDecorations() {
      this.clearCurrentStatementOverlay();
      this.activeSplitStatement = null;
      this.emitExecutableSqlTargetChange();
    },
    updateCurrentSqlStatementDecoration() {
      const model = this.monacoEditor?.getModel();
      const position = this.monacoEditor?.getPosition();
      if (!model || !position || model.getVersionId() !== this.splitModelVersionId) {
        this.clearCurrentStatementDecorations();
        return;
      }

      const statement = this.findStatementAtPosition(position, model);
      if (!statement) {
        this.clearCurrentStatementDecorations();
        return;
      }

      this.activeSplitStatement = statement;
      this.applyCurrentStatementDecoration(statement.range);
      this.emitExecutableSqlTargetChange();
    },
    findStatementAtPosition(position, model = this.monacoEditor?.getModel()) {
      const exactStatement = this.splitStatements.find((statement) => this.positionInLanguageRange(position, statement.range, model));
      if (exactStatement || !model) {
        return exactStatement;
      }

      return this.findStatementBeforeTrailingPosition(position, model);
    },
    findStatementBeforeTrailingPosition(position, model) {
      const cursorOffset = model.getOffsetAt(position);
      const candidates = this.splitStatements
        .map((statement) => ({
          statement,
          range: this.toMonacoLanguageRange(statement.range, model)
        }))
        .filter(({ range }) => range && range.endLineNumber === position.lineNumber && range.endOffset <= cursorOffset)
        .sort((a, b) => b.range.endOffset - a.range.endOffset);

      const candidate = candidates[0];
      if (!candidate) {
        return null;
      }

      const trailingText = model.getValue().slice(candidate.range.endOffset, cursorOffset);
      return /^[;\t ]*$/.test(trailingText) ? candidate.statement : null;
    },
    positionInLanguageRange(position, range, model = this.monacoEditor?.getModel()) {
      const monacoRange = this.toMonacoLanguageRange(range, model);
      if (!monacoRange) {
        return false;
      }

      const cursorOffset = model?.getOffsetAt(position);
      if (cursorOffset === undefined) {
        return false;
      }

      if (cursorOffset < monacoRange.startOffset || cursorOffset >= monacoRange.endOffset) {
        return false;
      }
      return true;
    },
    applyCurrentStatementDecoration(range) {
      if (!this.toMonacoLanguageRange(range)) {
        this.clearCurrentStatementDecorations();
        return;
      }

      this.renderCurrentStatementOverlay();
    },
    installCurrentStatementOverlay() {
      this.disposeCurrentStatementOverlay();
      const editorElement = this.$refs.monacoEditor;
      if (!editorElement) {
        return;
      }

      const overlayElement = document.createElement('div');
      overlayElement.className = 'current-sql-statement-overlays';
      editorElement.appendChild(overlayElement);
      this.currentStatementOverlayElement = overlayElement;
    },
    clearCurrentStatementOverlay() {
      if (this.currentStatementOverlayFrame) {
        cancelAnimationFrame(this.currentStatementOverlayFrame);
        this.currentStatementOverlayFrame = null;
      }
      if (this.currentStatementOverlayElement) {
        this.currentStatementOverlayElement.innerHTML = '';
      }
    },
    disposeCurrentStatementOverlay() {
      this.clearCurrentStatementOverlay();
      if (this.currentStatementOverlayElement) {
        this.currentStatementOverlayElement.remove();
        this.currentStatementOverlayElement = null;
      }
    },
    renderCurrentStatementOverlay() {
      if (this.currentStatementOverlayFrame) {
        cancelAnimationFrame(this.currentStatementOverlayFrame);
      }
      this.currentStatementOverlayFrame = requestAnimationFrame(() => {
        this.currentStatementOverlayFrame = null;
        this.doRenderCurrentStatementOverlay();
      });
    },
    doRenderCurrentStatementOverlay() {
      const overlayElement = this.currentStatementOverlayElement;
      const model = this.monacoEditor?.getModel();
      if (!overlayElement || !model || !this.activeSplitStatement) {
        if (overlayElement) {
          overlayElement.innerHTML = '';
        }
        return;
      }

      const range = this.toCurrentStatementVisualRange(this.toMonacoLanguageRange(this.activeSplitStatement.range, model), model);
      if (!range) {
        overlayElement.innerHTML = '';
        return;
      }

      const box = this.getCurrentStatementOverlayBox(range, model);
      if (!box) {
        overlayElement.innerHTML = '';
        return;
      }

      overlayElement.innerHTML = '';
      const boxElement = document.createElement('div');
      boxElement.className = 'current-sql-statement-box';
      boxElement.style.top = `${box.top}px`;
      boxElement.style.left = `${box.left}px`;
      boxElement.style.width = `${box.width}px`;
      boxElement.style.height = `${box.height}px`;
      overlayElement.appendChild(boxElement);
    },
    getCurrentStatementOverlayBox(range, model) {
      const layoutInfo = this.monacoEditor.getLayoutInfo();
      const lineHeight = this.monacoEditor.getOption(monaco.editor.EditorOption.lineHeight);
      const scrollTop = this.monacoEditor.getScrollTop();
      const scrollLeft = this.monacoEditor.getScrollLeft();
      const contentLeft = layoutInfo.contentLeft;
      const startLine = range.startLineNumber;
      const endLine = range.endLineNumber;
      const top = this.monacoEditor.getTopForLineNumber(startLine) - scrollTop;
      const height = this.monacoEditor.getTopForLineNumber(endLine) - this.monacoEditor.getTopForLineNumber(startLine) + lineHeight;

      if (top + height < 0 || top > layoutInfo.height) {
        return null;
      }

      if (startLine === endLine) {
        const left = contentLeft + this.monacoEditor.getOffsetForColumn(startLine, range.startColumn) - scrollLeft;
        const right = contentLeft + this.monacoEditor.getOffsetForColumn(endLine, range.endColumn) - scrollLeft;
        return {
          top,
          left,
          width: Math.max(2, right - left),
          height
        };
      }

      let left = contentLeft + this.monacoEditor.getOffsetForColumn(startLine, range.startColumn) - scrollLeft;
      let right = left + 2;
      for (let line = startLine; line <= endLine; line++) {
        const lineStartColumn = line === startLine ? range.startColumn : 1;
        const lineEndColumn = line === endLine ? range.endColumn : model.getLineMaxColumn(line);
        const lineLeft = contentLeft + this.monacoEditor.getOffsetForColumn(line, lineStartColumn) - scrollLeft;
        const lineRight = contentLeft + this.monacoEditor.getOffsetForColumn(line, lineEndColumn) - scrollLeft;
        left = Math.min(left, lineLeft);
        right = Math.max(right, lineRight, lineLeft + 2);
      }

      const width = Math.max(2, right - left);
      return {
        top,
        left,
        width,
        height
      };
    },
    toCurrentStatementVisualRange(range, model) {
      if (!range || !model) {
        return null;
      }

      const text = model.getValue().slice(range.startOffset, range.endOffset);
      const leadingWhitespace = text.match(/^\s*/)?.[0].length || 0;
      const trailingWhitespace = text.match(/\s*$/)?.[0].length || 0;
      const startOffset = Math.min(range.endOffset, range.startOffset + leadingWhitespace);
      const endOffset = Math.max(startOffset, range.endOffset - trailingWhitespace);
      const startPosition = model.getPositionAt(startOffset);
      const endPosition = model.getPositionAt(endOffset);

      return {
        startLineNumber: startPosition.lineNumber,
        startColumn: startPosition.column,
        endLineNumber: endPosition.lineNumber,
        endColumn: startPosition.lineNumber === endPosition.lineNumber ? Math.max(startPosition.column + 1, endPosition.column) : endPosition.column,
        startOffset,
        endOffset
      };
    },
    toMonacoLanguageRange(range, model = this.monacoEditor?.getModel()) {
      if (!range || !model) {
        return null;
      }

      const start = range.startPosition || {};
      const end = range.endPosition || {};
      const startLineNumber = Math.max(1, start.lineNumber || start.line || 1);
      const startColumn = Math.max(1, (start.columnNumber ?? start.column ?? 0) + 1);
      const endLineNumber = Math.max(startLineNumber, end.lineNumber || end.line || startLineNumber);
      const rawEndColumn = end.columnNumber ?? end.column ?? start.columnNumber ?? start.column ?? 0;
      const endColumn = Math.max(startLineNumber === endLineNumber ? startColumn + 1 : 1, rawEndColumn + 1);
      const startPosition = {
        lineNumber: Math.min(startLineNumber, model.getLineCount()),
        column: Math.min(startColumn, model.getLineMaxColumn(Math.min(startLineNumber, model.getLineCount())))
      };
      const endLine = Math.min(endLineNumber, model.getLineCount());
      const endPosition = {
        lineNumber: endLine,
        column: Math.min(Math.max(1, endColumn), model.getLineMaxColumn(endLine))
      };

      return {
        startLineNumber: startPosition.lineNumber,
        startColumn: startPosition.column,
        endLineNumber: endPosition.lineNumber,
        endColumn: endPosition.column,
        startOffset: model.getOffsetAt(startPosition),
        endOffset: model.getOffsetAt(endPosition)
      };
    },
    isLanguageFragmentTooLarge(sqlText) {
      if (!sqlText) {
        return false;
      }
      return new TextEncoder().encode(sqlText).length > MAX_LANGUAGE_FRAGMENT_BYTES;
    },
    getCurrentLanguageFragment(position = this.monacoEditor?.getPosition(), endAtPosition = false) {
      const model = this.monacoEditor?.getModel();
      if (!model || !position) {
        return {
          sqlText: (this.monacoEditor?.getValue() || '').trimEnd(),
          startOffset: 0,
          startPosition: {
            lineNumber: 1,
            columnNumber: 0
          }
        };
      }

      const text = model.getValue();
      const cursorOffset = model.getOffsetAt(position);
      const range = this.findSqlFragmentRange(text, cursorOffset);
      const startPosition = model.getPositionAt(range.startOffset);
      const endOffset = endAtPosition ? cursorOffset : range.endOffset;
      return {
        sqlText: text.slice(range.startOffset, endOffset).trimEnd(),
        startOffset: range.startOffset,
        startPosition: {
          lineNumber: startPosition.lineNumber,
          columnNumber: startPosition.column - 1
        }
      };
    },
    getLanguageFragments() {
      const model = this.monacoEditor?.getModel();
      if (!model) {
        return [];
      }

      const text = model.getValue();
      const ranges = this.findSqlFragmentRanges(text);
      return ranges
        .map((range) => {
          const sqlText = text.slice(range.startOffset, range.endOffset).trimEnd();
          const startPosition = model.getPositionAt(range.startOffset);
          return {
            sqlText,
            startOffset: range.startOffset,
            startPosition: {
              lineNumber: startPosition.lineNumber,
              columnNumber: startPosition.column - 1
            }
          };
        })
        .filter((fragment) => fragment.sqlText.trim());
    },
    findSqlFragmentRange(text, cursorOffset) {
      const ranges = this.findSqlFragmentRanges(text);
      return (
        ranges.find((range) => cursorOffset >= range.startOffset && cursorOffset <= range.endOffset) || {
          startOffset: 0,
          endOffset: text.length
        }
      );
    },
    findSqlFragmentRanges(text) {
      const ranges = [];
      let startOffset = 0;
      let quote = null;
      let lineComment = false;
      let blockComment = false;

      for (let i = 0; i < text.length; i++) {
        const char = text[i];
        const next = text[i + 1];

        if (lineComment) {
          if (char === '\n') {
            lineComment = false;
          }
          continue;
        }

        if (blockComment) {
          if (char === '*' && next === '/') {
            blockComment = false;
            i++;
          }
          continue;
        }

        if (quote) {
          if (char === quote) {
            if ((quote === "'" || quote === '"') && next === quote) {
              i++;
            } else if (text[i - 1] !== '\\') {
              quote = null;
            }
          }
          continue;
        }

        if (char === '-' && next === '-') {
          lineComment = true;
          i++;
          continue;
        }

        if (char === '#') {
          lineComment = true;
          continue;
        }

        if (char === '/' && next === '*') {
          blockComment = true;
          i++;
          continue;
        }

        if (char === "'" || char === '"' || char === '`') {
          quote = char;
          continue;
        }

        if (char !== ';') {
          continue;
        }

        ranges.push({
          startOffset,
          endOffset: i + 1
        });
        startOffset = i + 1;
      }

      if (startOffset < text.length) {
        ranges.push({
          startOffset,
          endOffset: text.length
        });
      }

      return ranges.length ? ranges : [{ startOffset: 0, endOffset: text.length }];
    },
    buildLanguageRequest(position = this.monacoEditor?.getPosition(), languageFragment = null) {
      const fragment = languageFragment || this.getCurrentLanguageFragment(position, true);
      const cursorLineNumber = position ? Math.max(1, position.lineNumber - fragment.startPosition.lineNumber + 1) : 1;
      const cursorColNumber = position
        ? position.lineNumber === fragment.startPosition.lineNumber
          ? Math.max(0, position.column - 1 - fragment.startPosition.columnNumber)
          : Math.max(0, position.column - 1)
        : 0;
      return {
        sqlText: fragment.sqlText,
        requestVersion: ++this.languageRequestVersion,
        startPosition: fragment.startPosition,
        cursorLineNumber,
        cursorColNumber
      };
    },
    requestLanguageWebSocket(languageType, request) {
      const { startPosition, ...languageRequest } = request;
      return requestWebSocket({
        type: WS_TYPE.WS_REQ_LANGUAGE,
        responseType: WS_TYPE.WS_RES_LANGUAGE,
        object: {
          languageType,
          requestId: `${Date.now()}-${Math.random().toString(16).slice(2)}`,
          levels: this.getLanguageRequestLevels(),
          basicCodeLine: startPosition.lineNumber,
          basicCodeColumn: startPosition.columnNumber,
          request: languageRequest
        }
      });
    },
    async getBackendCompletionSuggest(position) {
      const request = this.buildLanguageRequest(position);
      const fallbackSuggestions = await this.getFallbackKeywordSuggest();
      if (this.isLanguageFragmentTooLarge(request.sqlText)) {
        return fallbackSuggestions;
      }
      try {
        const response = await this.requestLanguageWebSocket('COMPLETE', request);
        if (this.handleLanguageServiceStatus(response)) {
          return fallbackSuggestions;
        }
        if (this.handleLanguageServiceMessage(response)) {
          return fallbackSuggestions;
        }
        const result = response?.result;
        if (result) {
          const backendSuggestions = (result.items || []).map((item) => this.toMonacoCompletionItem(item));
          return this.mergeCompletionSuggestions(fallbackSuggestions, backendSuggestions);
        }
      } catch {
        // ignore language-service failures and use local fallback.
      }
      return fallbackSuggestions;
    },
    handleLanguageServiceStatus(response) {
      if (response?.success === true) {
        return false;
      }
      this.showLanguageServiceError(
        response?.msg || response?.result?.msg || response?.entities?.[0]?.message || response?.code || '语言服务请求失败'
      );
      return true;
    },
    handleLanguageServiceMessage(response) {
      if (response?.resultType !== 'Message') {
        return false;
      }

      const entity = response.entities?.[0];
      const message = entity?.message;
      if (message) {
        this.showLanguageServiceError(message);
      }
      return true;
    },
    showLanguageServiceError(message) {
      if (!message) {
        return;
      }
      this.languageServiceErrorMessage = message;
      if (this.languageServiceErrorTimer) {
        clearTimeout(this.languageServiceErrorTimer);
      }
      this.languageServiceErrorTimer = setTimeout(() => {
        this.languageServiceErrorMessage = '';
        this.languageServiceErrorTimer = null;
      }, 2400);
    },
    async getDelayedBackendCompletionSuggest(model, position) {
      const versionId = model.getVersionId();
      await this.waitForLanguageCompletion();
      if (model.isDisposed() || model.getVersionId() !== versionId) {
        return [];
      }
      return this.getBackendCompletionSuggest(position);
    },
    waitForLanguageCompletion() {
      return new Promise((resolve) => {
        setTimeout(resolve, LANGUAGE_COMPLETION_DELAY_MS);
      });
    },
    async getFallbackKeywordSuggest() {
      const language = this.getDsLanguageCapability();
      if (!language?.keywordResource) {
        return [];
      }

      const keywords = await this.loadKeywordResource(language.keywordResource);
      return this.getSQLSuggest(keywords).map((item) => ({
        ...item,
        additionalTextEdits: undefined
      }));
    },
    async loadKeywordResource(resource) {
      if (this.keywordResourceCache[resource]) {
        return this.keywordResourceCache[resource];
      }

      try {
        const response = await fetch(getPluginResourceUrl(resource, { format: 'text' }), {
          credentials: 'include'
        });
        if (!response.ok) {
          return [];
        }
        const text = await response.text();
        const keywords = text
          .split(/\r?\n/)
          .map((line) => line.trim())
          .filter((line) => line && !line.startsWith('#'));
        this.keywordResourceCache[resource] = keywords;
        return keywords;
      } catch {
        return [];
      }
    },
    toMonacoCompletionItem(item) {
      const type = item.umiType || item.kind;
      return {
        label: this.toCompletionLabel(item.label, this.getCompletionTypeText(type)),
        kind: this.toMonacoCompletionKind(item.kind, item.umiType),
        sortText: item.sortText || this.toCompletionSortText(item.weight, item.label),
        insertText: item.insertText || item.label,
        umiType: item.umiType,
        icon: item.icon,
        weight: item.weight
      };
    },
    mergeCompletionSuggestions(staticSuggestions, backendSuggestions) {
      const suggestions = new Map();
      staticSuggestions.forEach((item) => {
        suggestions.set(this.completionSuggestionKey(item), item);
      });
      backendSuggestions.forEach((item) => {
        const key = this.completionSuggestionKey(item);
        suggestions.set(key, {
          ...(suggestions.get(key) || {}),
          ...item
        });
      });
      return Array.from(suggestions.values()).sort((left, right) => this.compareCompletionSuggestions(left, right));
    },
    completionSuggestionKey(item) {
      return `${item.kind || ''}:${this.getCompletionLabelText(item.label).toUpperCase()}`;
    },
    toCompletionSortText(weight, label) {
      if (Number.isFinite(weight)) {
        const rank = `${Math.max(0, 1000000 - weight)}`.padStart(7, '0');
        return `${rank}_${this.getCompletionLabelText(label)}`;
      }
      return `${this.sortText++}`.padStart(8, '0');
    },
    compareCompletionSuggestions(left, right) {
      const leftSort = left.sortText || '';
      const rightSort = right.sortText || '';
      if (leftSort !== rightSort) {
        return leftSort.localeCompare(rightSort);
      }
      return this.getCompletionLabelText(left.label).localeCompare(this.getCompletionLabelText(right.label));
    },
    toCompletionLabel(label, typeText) {
      const text = this.getCompletionLabelText(label);
      if (!typeText) {
        return text;
      }
      return {
        label: text,
        description: typeText
      };
    },
    getCompletionLabelText(label) {
      if (label && typeof label === 'object') {
        return `${label.label || ''}`;
      }
      return `${label || ''}`;
    },
    getCompletionTypeText(kind) {
      const typeMap = {
        KEYWORD: this.$t('guan-jian-zi'),
        DATABASE: this.$t('shu-ju-ku'),
        Database: this.$t('shu-ju-ku'),
        CATALOG: 'Catalog',
        Catalog: 'Catalog',
        ExternalCatalog: 'Catalog',
        SCHEMA: 'Schema',
        Schema: 'Schema',
        ExternalSchema: 'Schema',
        TABLE: this.$t('biao'),
        Table: this.$t('biao'),
        ExternalTable: this.$t('biao'),
        Materialized: this.$t('biao'),
        VIEW: this.$t('shi-tu'),
        View: this.$t('shi-tu'),
        COLUMN: this.$t('lie'),
        Column: this.$t('lie'),
        FUNCTION: this.$t('han-shu'),
        Function: this.$t('han-shu'),
        PROCEDURE: 'Procedure',
        Procedure: 'Procedure',
        SNIPPET: this.$t('mo-ban'),
        TEXT: 'Text'
      };
      return typeMap[kind] || '';
    },
    toMonacoCompletionKind(kind, umiType) {
      const umiKindMap = {
        Catalog: monaco.languages.CompletionItemKind.Module,
        ExternalCatalog: monaco.languages.CompletionItemKind.Module,
        Schema: monaco.languages.CompletionItemKind.Module,
        ExternalSchema: monaco.languages.CompletionItemKind.Module,
        Table: monaco.languages.CompletionItemKind.Struct,
        ExternalTable: monaco.languages.CompletionItemKind.Struct,
        Materialized: monaco.languages.CompletionItemKind.Struct,
        View: monaco.languages.CompletionItemKind.Interface,
        Column: monaco.languages.CompletionItemKind.Field,
        Function: monaco.languages.CompletionItemKind.Function,
        Procedure: monaco.languages.CompletionItemKind.Function
      };
      if (umiKindMap[umiType]) {
        return umiKindMap[umiType];
      }
      const kindMap = {
        KEYWORD: monaco.languages.CompletionItemKind.Keyword,
        DATABASE: monaco.languages.CompletionItemKind.Module,
        CATALOG: monaco.languages.CompletionItemKind.Module,
        SCHEMA: monaco.languages.CompletionItemKind.Module,
        TABLE: monaco.languages.CompletionItemKind.Struct,
        VIEW: monaco.languages.CompletionItemKind.Interface,
        COLUMN: monaco.languages.CompletionItemKind.Field,
        FUNCTION: monaco.languages.CompletionItemKind.Function,
        PROCEDURE: monaco.languages.CompletionItemKind.Function,
        SNIPPET: monaco.languages.CompletionItemKind.Snippet,
        TEXT: monaco.languages.CompletionItemKind.Text
      };
      return kindMap[kind] || monaco.languages.CompletionItemKind.Text;
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
    formatSql() {},
    getCurrentSqlTarget() {
      const model = this.monacoEditor?.getModel();
      const position = this.monacoEditor?.getPosition();
      if (!model) {
        return {
          sql: '',
          position: null
        };
      }

      if (this.activeSplitStatement && model.getVersionId() === this.splitModelVersionId) {
        return {
          sql: this.activeSplitStatement.sql,
          position: this.toSelectionFromLanguageRange(this.activeSplitStatement.range)
        };
      }

      if (!position) {
        return {
          sql: model.getValue(),
          position: new monaco.Selection(1, 1, 1, 1)
        };
      }

      const text = model.getValue();
      const cursorOffset = model.getOffsetAt(position);
      const range = this.findSqlFragmentRange(text, cursorOffset);
      const startPosition = model.getPositionAt(range.startOffset);
      return {
        sql: text.slice(range.startOffset, range.endOffset),
        position: new monaco.Selection(startPosition.lineNumber, startPosition.column, startPosition.lineNumber, startPosition.column)
      };
    },
    toSelectionFromLanguageRange(range) {
      const monacoRange = this.toMonacoLanguageRange(range);
      if (!monacoRange) {
        return new monaco.Selection(1, 1, 1, 1);
      }
      return new monaco.Selection(monacoRange.startLineNumber, monacoRange.startColumn, monacoRange.startLineNumber, monacoRange.startColumn);
    },
    getExecutableSqlTargetState() {
      const model = this.monacoEditor?.getModel();
      const selection = this.monacoEditor?.getSelection();
      if (!model || !selection) {
        return {
          canRun: false,
          hasSelection: false,
          hasStatement: false
        };
      }

      const hasSelection =
        selection.selectionStartLineNumber !== selection.positionLineNumber || selection.selectionStartColumn !== selection.positionColumn;
      if (hasSelection) {
        return {
          canRun: !!model.getValueInRange(selection).trim(),
          hasSelection: true,
          hasStatement: false
        };
      }

      const hasStatement = !!this.activeSplitStatement && model.getVersionId() === this.splitModelVersionId;
      return {
        canRun: hasStatement,
        hasSelection: false,
        hasStatement
      };
    },
    emitExecutableSqlTargetChange() {
      this.$emit('executable-sql-target-change', this.getExecutableSqlTargetState());
    },
    getCurrentSql() {
      return this.getCurrentSqlTarget().sql;
    },
    setFontSize(size) {
      if (this.monacoEditor) {
        this.monacoEditor.updateOptions({ fontSize: size });
        this.monacoEditorFountCss = `font-size-${size}`;
      }
    },
    getSQLSuggest(keywords) {
      const list = keywords.map((key) => ({
        label: this.toCompletionLabel(key, this.$t('guan-jian-zi')),
        kind: monaco.languages.CompletionItemKind.Keyword,
        sortText: this.toCompletionSortText(STATIC_KEYWORD_WEIGHT, key),
        weight: STATIC_KEYWORD_WEIGHT,
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
              label: this.toCompletionLabel(`${child.title} (${tableKey})`, this.$t('lie')),
              kind: monaco.languages.CompletionItemKind.Enum,
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
            label: this.toCompletionLabel(table.objName, leafKey.i18n),
            kind: monaco.languages.CompletionItemKind.Method,
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
      if (this.validateTimer) {
        clearTimeout(this.validateTimer);
        this.validateTimer = null;
      }
      if (this.splitTimer) {
        clearTimeout(this.splitTimer);
        this.splitTimer = null;
      }
      if (this.diagnosticDecorationCollection) {
        this.diagnosticDecorationCollection.clear();
        this.diagnosticDecorationCollection = null;
      }
      if (this.languageServiceErrorTimer) {
        clearTimeout(this.languageServiceErrorTimer);
        this.languageServiceErrorTimer = null;
      }
      if (this.suggestIconFrame) {
        cancelAnimationFrame(this.suggestIconFrame);
        this.suggestIconFrame = null;
      }
      if (this.suggestIconObserver) {
        this.suggestIconObserver.disconnect();
        this.suggestIconObserver = null;
      }
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
      this.editorDisposableList.forEach((disposable) => {
        disposable.dispose();
      });
      this.editorDisposableList = [];
      this.disposeCurrentStatementOverlay();
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
      <div class="language-service-error" v-if="languageServiceErrorMessage">
        {{ languageServiceErrorMessage }}
      </div>
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
  position: relative;
}
.monaco-editor-content {
  height: 100%;
  width: 100%;
  position: relative;
}

:deep(.current-sql-statement-overlays) {
  position: absolute;
  inset: 0;
  z-index: 5;
  overflow: hidden;
  pointer-events: none;
}

:deep(.current-sql-statement-box) {
  position: absolute;
  box-sizing: border-box;
  border: 1px solid rgba(47, 143, 73, 0.9);
}

:deep(.monaco-editor .view-overlays .current-line) {
  border: none;
  background: rgba(83, 139, 183, 0.045);
}

:deep(.monaco-editor .margin-view-overlays .current-line-margin) {
  border: none;
  background: rgba(83, 139, 183, 0.035);
}

:deep(.suggest-widget .monaco-list .monaco-list-row > .contents > .main) {
  width: 100%;
}

:deep(.suggest-widget .monaco-list .monaco-list-row > .contents > .main > .right) {
  margin-left: auto;
  padding-left: 16px;
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
}

:deep(.suggest-widget .monaco-list .monaco-list-row > .contents > .main > .right > .details-label) {
  margin-left: 0;
  text-align: right;
}

:deep(.suggest-widget .cgdm-completion-icon::before) {
  display: none !important;
  content: '' !important;
}

:deep(.suggest-widget .cgdm-completion-row .codicon) {
  display: none !important;
}

:deep(.suggest-widget .cgdm-completion-row .cgdm-completion-icon) {
  display: inline-flex !important;
}

:deep(.suggest-widget .cgdm-completion-row .monaco-icon-label::before) {
  display: none !important;
  content: '' !important;
}

:deep(.suggest-widget .cgdm-completion-svg) {
  width: 16px;
  height: 16px;
  display: block;
}

:global(.suggest-widget .cgdm-completion-icon::before) {
  display: none !important;
  content: '' !important;
}

:global(.suggest-widget .cgdm-completion-row .codicon) {
  display: none !important;
}

:global(.suggest-widget .cgdm-completion-row .cgdm-completion-icon) {
  display: inline-flex !important;
}

:global(.suggest-widget .cgdm-completion-row .monaco-icon-label::before) {
  display: none !important;
  content: '' !important;
}

:global(.suggest-widget .cgdm-completion-svg) {
  width: 16px;
  height: 16px;
  display: block;
}

.monaco-editor .font-size-buttons {
  position: absolute;
  top: 3px;
  right: 17px;
  z-index: 10;
  display: flex;
  align-items: flex-start;
  gap: 5px;
}
.language-service-error {
  max-width: 360px;
  padding: 4px 8px;
  border: 1px solid #ffccc7;
  border-radius: 4px;
  background: #fff2f0;
  color: #cf1322;
  font-size: 12px;
  line-height: 18px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
