import { getLanguage } from '@/utils/tools';
import { getPluginResourceUrl } from '@/utils/pluginResource';
import { kafkaLanguage } from './kafkaLanguage';

const keywordCache = {};
const registeredLanguages = new Set();
const languageDefinitions = { kafka: kafkaLanguage };

export function getDsSetting(settings, dsType) {
  if (!dsType || !settings) {
    return null;
  }
  if (settings[dsType]) {
    return settings[dsType];
  }
  const normalizedKey = Object.keys(settings).find((key) => key.toLowerCase() === `${dsType}`.toLowerCase());
  return normalizedKey ? settings[normalizedKey] : null;
}

export function getBaseEditorLanguage(dsType, fallbackLanguage = 'sql') {
  return dsType ? getLanguage(dsType) : fallbackLanguage || 'sql';
}

export async function resolveSqlEditorLanguage(monaco, dsType, settings, fallbackLanguage = 'sql', languageCapability = null) {
  const baseLanguage = getBaseEditorLanguage(dsType, fallbackLanguage);
  const definition = languageDefinitions[baseLanguage];
  if (definition) {
    registerLanguage(monaco, baseLanguage, definition);
    return baseLanguage;
  }
  const keywordResource = languageCapability?.keywordResource;
  if (!keywordResource) {
    return baseLanguage;
  }

  const keywords = await loadKeywordResource(keywordResource);
  if (!keywords.length) {
    return baseLanguage;
  }

  const languageId = `cgdm-sql-${String(dsType)
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')}`;
  registerLanguage(monaco, languageId, createKeywordLanguage(keywords));
  return languageId;
}

export async function applySqlEditorLanguage(monaco, editor, dsType, settings, fallbackLanguage = 'sql') {
  const model = editor?.getModel?.();
  if (!model) {
    return;
  }
  const language = await resolveSqlEditorLanguage(monaco, dsType, settings, fallbackLanguage);
  monaco.editor.setModelLanguage(model, language);
}

async function loadKeywordResource(resource) {
  if (keywordCache[resource]) {
    return keywordCache[resource];
  }

  try {
    const response = await fetch(getPluginResourceUrl(resource), {
      credentials: 'include'
    });
    if (!response.ok) {
      keywordCache[resource] = [];
      return keywordCache[resource];
    }
    const text = await response.text();
    keywordCache[resource] = text
      .split(/\r?\n/)
      .map((line) => line.trim())
      .filter((line) => line && !line.startsWith('#'));
  } catch {
    keywordCache[resource] = [];
  }
  return keywordCache[resource];
}

function registerLanguage(monaco, languageId, definition) {
  if (registeredLanguages.has(languageId)) {
    return;
  }

  monaco.languages.register({ id: languageId });
  if (definition.configuration) {
    monaco.languages.setLanguageConfiguration(languageId, definition.configuration);
  }
  monaco.languages.setMonarchTokensProvider(languageId, definition.tokens);
  registeredLanguages.add(languageId);
}

function createKeywordLanguage(keywords) {
  return {
    tokens: {
      ignoreCase: true,
      keywords,
      tokenizer: {
        root: [
          [/[a-zA-Z_][\w$]*/, { cases: { '@keywords': 'keyword', '@default': 'identifier' } }],
          [/--.*$/, 'comment'],
          [/#.*$/, 'comment'],
          [/\/\*/, 'comment', '@comment'],
          [/"([^"\\]|\\.)*$/, 'string.invalid'],
          [/'([^'\\]|\\.)*$/, 'string.invalid'],
          [/"/, 'string', '@doubleString'],
          [/'/, 'string', '@singleString'],
          [/[{}()[\]]/, '@brackets'],
          [/[;,.]/, 'delimiter'],
          [/\d+\.\d+([eE][+-]?\d+)?/, 'number.float'],
          [/\d+/, 'number'],
          [/[+\-*/%=<>!~|&]+/, 'operator']
        ],
        comment: [
          [/[^*/]+/, 'comment'],
          [/\*\//, 'comment', '@pop'],
          [/[*/]/, 'comment']
        ],
        doubleString: [
          [/[^\\"]+/, 'string'],
          [/\\./, 'string.escape'],
          [/"/, 'string', '@pop']
        ],
        singleString: [
          [/[^\\']+/, 'string'],
          [/\\./, 'string.escape'],
          [/'/, 'string', '@pop']
        ]
      }
    }
  };
}
