import Editor from '@/components/editor/index.vue';
import { monaco } from '@/components/editor/monaco';
import { esLanguage } from '@/components/editor/esLanguage';
import { getBaseEditorLanguage, getDsSetting, resolveSqlEditorLanguage } from '@/components/editor/sqlLanguage';
import { getLanguage } from '@/utils/tools';
import { separatePort, WS_TYPE } from '@/utils';
import { normalizeDsSupportNameGroups } from '@/utils/datasourceSupport';
import { requestWebSocket } from '@/services/socket';

jest.mock('@/components/editor/monaco', () => ({
  monaco: {
    languages: {
      register: jest.fn(),
      setLanguageConfiguration: jest.fn(),
      setMonarchTokensProvider: jest.fn(),
      registerCompletionItemProvider: jest.fn(() => ({ dispose: jest.fn() }))
    }
  }
}));
jest.mock('monaco-editor/esm/vs/platform/actions/common/actions', () => ({}));
jest.mock('@/services/socket', () => ({ requestWebSocket: jest.fn() }));
jest.mock('@/utils/logger', () => ({ debug: jest.fn() }));
jest.mock('@/i18n', () => ({ global: { t: (key) => key } }));

function editorContext(dsType) {
  const context = {
    currentTab: {
      dsType,
      sessionId: `${dsType}-session`,
      support: { language: { supported: true, supports: ['COMPLETE', 'VALIDATE', 'SPLIT', 'FORMAT'] }, format: { conf: 'Allow' } }
    },
    languageRequestVersion: 0,
    completionItemProviderList: [],
    languageMaxRequestBytes: 1024 * 1024
  };
  Object.entries(Editor.methods).forEach(([name, method]) => {
    context[name] = method.bind(context);
  });
  return context;
}

describe('REST DSL product integration', () => {
  it('shares the REST tokenizer while retaining separate product settings and names', async () => {
    for (const dsType of ['OpenSearch', 'ElasticSearch']) {
      expect(getLanguage(dsType)).toBe('es');
      expect(getBaseEditorLanguage(dsType)).toBe('es');
      expect(await resolveSqlEditorLanguage(monaco, dsType, {})).toBe('es');
      expect(separatePort(dsType)).toBe(false);
    }
    expect(monaco.languages.register).toHaveBeenCalledTimes(1);
    expect(monaco.languages.setMonarchTokensProvider).toHaveBeenCalledWith('es', esLanguage.tokens);
    expect(monaco.languages.setLanguageConfiguration).toHaveBeenCalledWith('es', esLanguage.configuration);
    expect(getLanguage('MySQL')).toBe('mysql');
    expect(separatePort('MySQL')).toBe(true);

    const settings = { OpenSearch: { product: 'os' }, ElasticSearch: { product: 'es' } };
    expect(getDsSetting(settings, 'OpenSearch')).toBe(settings.OpenSearch);
    expect(getDsSetting({ ElasticSearch: settings.ElasticSearch }, 'OpenSearch')).toBeNull();
    const groups = normalizeDsSupportNameGroups([
      [],
      [],
      [
        { dsKey: 'ElasticSearch', displayName: 'ElasticSearch' },
        { dsKey: 'OpenSearch', displayName: 'OpenSearch' }
      ]
    ]);
    expect(groups[0][1]).toMatchObject({ dsKey: 'OpenSearch', displayName: 'OpenSearch', displayGroupIndex: 2 });
  });

  it.each(['OpenSearch', 'ElasticSearch', 'Hana'])('%s leaves statement boundaries to the server', (dsType) => {
    const context = editorContext(dsType);
    const text = 'GET /demo/_search\n{"query":{"match":{"name":"a;b"}}}\n;\nGET /demo/_count';
    expect(context.findSqlFragmentRanges(text)).toEqual([{ startOffset: 0, endOffset: text.length }]);

    const model = { getVersionId: () => 2 };
    context.monacoEditor = { getModel: () => model, getPosition: () => ({ lineNumber: 4, column: 1 }) };
    context.splitModelVersionId = 1;
    context.findLocalStatementAtPosition = jest.fn();
    context.clearCurrentStatementDecorations = jest.fn();
    context.updateCurrentSqlStatementDecoration();
    expect(context.findLocalStatementAtPosition).not.toHaveBeenCalled();
    expect(context.clearCurrentStatementDecorations).toHaveBeenCalled();
    expect(context.getCurrentSqlTarget()).toEqual({ sql: '', position: null });
  });

  it('retains SQL fallback and quoted semicolon handling', () => {
    const context = editorContext('MySQL');
    const text = "SELECT 'a;b'; SELECT 2;";
    const ranges = context.findSqlFragmentRanges(text);
    expect(ranges.map(({ startOffset, endOffset }) => text.slice(startOffset, endOffset))).toEqual(["SELECT 'a;b';", ' SELECT 2;']);
    context.monacoEditor = { getModel: () => ({ getVersionId: () => 2 }), getPosition: () => ({ lineNumber: 1, column: 1 }) };
    context.findLocalStatementAtPosition = jest.fn(() => null);
    context.clearCurrentStatementDecorations = jest.fn();
    context.updateCurrentSqlStatementDecoration();
    expect(context.findLocalStatementAtPosition).toHaveBeenCalled();
  });

  it.each(['OpenSearch', 'ElasticSearch'])('%s accepts current server ranges and rejects stale ones', (dsType) => {
    const context = editorContext(dsType);
    let version = 1;
    context.monacoEditor = { getModel: () => ({ getVersionId: () => version }), getPosition: () => null };
    const range = { start: { lineNumber: 3, columnNumber: 0 }, end: { lineNumber: 3, columnNumber: 16 } };
    context.activeStatement = { sql: 'GET /demo/_count', range };
    context.activeStatementModelVersionId = 1;
    context.toSelectionFromLanguageRange = jest.fn(() => 'selection');
    expect(context.getCurrentSqlTarget()).toEqual({ sql: 'GET /demo/_count', position: 'selection' });
    expect(context.toSelectionFromLanguageRange).toHaveBeenCalledWith(range);
    version++;
    expect(context.getCurrentSqlTarget()).toEqual({ sql: '', position: null });
  });

  it('recognizes the product from the instance node before tab metadata is populated', () => {
    const context = editorContext(undefined);
    context.currentTab.node = { INSTANCE: { attr: { dsType: 'OpenSearch' } } };
    expect(context.usesServerStatementRanges()).toBe(true);
  });

  it('localizes language service failures without replacing server messages', () => {
    const context = editorContext('OpenSearch');
    context.$t = jest.fn(() => 'Language service request failed');
    context.showLanguageServiceError = jest.fn();
    expect(context.handleLanguageServiceStatus({ success: false })).toBe(true);
    expect(context.$t).toHaveBeenCalledWith('query-language-request-failed');
    expect(context.showLanguageServiceError).toHaveBeenLastCalledWith('Language service request failed');
    context.handleLanguageServiceStatus({ success: false, msg: 'Permission denied' });
    expect(context.showLanguageServiceError).toHaveBeenLastCalledWith('Permission denied');
    context.showLanguageServiceError.mockClear();
    expect(context.handleLanguageServiceStatus({ success: true })).toBe(false);
    expect(context.showLanguageServiceError).not.toHaveBeenCalled();
  });

  it('isolates completion providers for ES and OpenSearch models sharing the same language', async () => {
    const contexts = ['ElasticSearch', 'OpenSearch'].map(editorContext);
    const models = contexts.map(() => ({ getValueInRange: () => 'GET /' }));
    contexts.forEach((context, index) => {
      context.monacoEditor = { getModel: () => models[index] };
      context.getDelayedBackendCompletionSuggest = jest.fn(async () => [{ label: context.currentTab.dsType }]);
      context.updateCompletionIconMap = jest.fn();
      context.scheduleSuggestIconRender = jest.fn();
      context.registerCompletion('es');
    });
    const providers = monaco.languages.registerCompletionItemProvider.mock.calls.map(([, provider]) => provider);
    const position = { lineNumber: 1, column: 6 };
    for (let index = 0; index < contexts.length; index++) {
      expect(await providers[index].provideCompletionItems(models[1 - index], position)).toEqual({ suggestions: [] });
      expect(contexts[index].getDelayedBackendCompletionSuggest).not.toHaveBeenCalled();
      expect(await providers[index].provideCompletionItems(models[index], position)).toEqual({
        suggestions: [{ label: contexts[index].currentTab.dsType }]
      });
    }
  });

  it.each(['COMPLETE', 'VALIDATE', 'SPLIT', 'FORMAT'])('routes %s by the original session, not the shared language ID', (languageType) => {
    const context = editorContext('OpenSearch');
    context.getLanguageRequestLevels = () => [{ category: 'INSTANCE', name: 'os-instance' }];
    const request = { sqlText: 'GET /demo/_search', requestVersion: 1, startPosition: { lineNumber: 1, columnNumber: 0 } };
    context.requestLanguageWebSocket(languageType, request);
    expect(requestWebSocket).toHaveBeenLastCalledWith({
      type: WS_TYPE.WS_REQ_LANGUAGE,
      responseType: WS_TYPE.WS_RES_LANGUAGE,
      object: {
        languageType,
        requestId: expect.any(String),
        sessionId: 'OpenSearch-session',
        levels: context.getLanguageRequestLevels(),
        basicCodeLine: 1,
        basicCodeColumn: 0,
        request: { sqlText: request.sqlText, requestVersion: 1 }
      }
    });
  });

  it.each(['unchanged', 'edited', 'session-changed'])('formats via the server and guards %s state', async (state) => {
    const context = editorContext('OpenSearch');
    let version = 1;
    const sqlText = 'GET /demo/_search\n{"query":{"match_all":{}}}';
    const formatted = 'GET /demo/_search\n{\n    "query": {"match_all": {}}\n}';
    const range = { startLineNumber: 1, startColumn: 1, endLineNumber: 2, endColumn: 27 };
    const model = { getValue: () => sqlText, getFullModelRange: () => range, getVersionId: () => version, isDisposed: () => false };
    context.monacoEditor = { getModel: () => model, pushUndoStop: jest.fn(), executeEdits: jest.fn() };
    context.isLanguageFragmentTooLarge = () => false;
    context.handleLanguageServiceStatus = () => false;
    context.handleLanguageServiceMessage = () => false;
    context.requestLanguageWebSocket = jest.fn(async (type, request) => {
      if (state === 'edited') version++;
      if (state === 'session-changed') context.currentTab.sessionId = 'new-session';
      return { result: { requestVersion: request.requestVersion, sqlText: formatted } };
    });
    await context.formatSql();
    expect(context.requestLanguageWebSocket).toHaveBeenCalledWith('FORMAT', expect.objectContaining({ sqlText }));
    if (state === 'unchanged') {
      expect(context.monacoEditor.executeEdits).toHaveBeenCalledWith('sql-format', [{ range, text: formatted, forceMoveMarkers: true }]);
      expect(context.monacoEditor.pushUndoStop).toHaveBeenCalledTimes(2);
    } else {
      expect(context.monacoEditor.executeEdits).not.toHaveBeenCalled();
    }
  });
});
