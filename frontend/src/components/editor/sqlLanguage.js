import { getLanguage } from '@/utils/tools';
import { getPluginResourceUrl } from '@/utils/pluginResource';

const keywordCache = {};
const registeredLanguages = {};

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

export function getDsLanguageCapability(settings, dsType) {
  return getDsSetting(settings, dsType)?.language || null;
}

export function getBaseEditorLanguage(dsType, fallbackLanguage = 'sql') {
  return dsType ? getLanguage(dsType) : fallbackLanguage || 'sql';
}

export async function resolveSqlEditorLanguage(monaco, dsType, settings, fallbackLanguage = 'sql') {
  const baseLanguage = getBaseEditorLanguage(dsType, fallbackLanguage);
  const keywordResource = getDsLanguageCapability(settings, dsType)?.keywordResource;
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
  registerKeywordLanguage(monaco, languageId, keywords);
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
    const response = await fetch(getPluginResourceUrl(resource, { format: 'text' }), {
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

function registerKeywordLanguage(monaco, languageId, keywords) {
  if (registeredLanguages[languageId]) {
    return;
  }
  registeredLanguages[languageId] = true;

  monaco.languages.register({ id: languageId });
  monaco.languages.setMonarchTokensProvider(languageId, {
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
  });
}
