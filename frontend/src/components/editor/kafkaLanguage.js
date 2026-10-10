export const kafkaLanguage = {
  configuration: {
    comments: { lineComment: '#' },
    wordPattern: /[\w.-]+/g,
    autoClosingPairs: [
      { open: '"', close: '"' },
      { open: "'", close: "'" }
    ]
  },
  tokens: {
    tokenizer: {
      root: [
        [/#.*$/, 'comment'],
        [/kafka-(?:topics|console-consumer)(?:\.sh)?\b/, 'keyword'],
        [/--[a-z][a-z-]*/, 'attribute.name'],
        [/'/, 'string', '@singleString'],
        [/"/, 'string', '@doubleString'],
        [/\\(?:\r?\n|.)/, 'string.escape'],
        [/\b\d+\b/, 'number'],
        [/[;=]/, 'delimiter'],
        [/[^\s;'"\\=]+/, 'identifier']
      ],
      singleString: [
        [/[^']+/, 'string'],
        [/'/, 'string', '@pop']
      ],
      doubleString: [
        [/[^"\\]+/, 'string'],
        [/\\./, 'string.escape'],
        [/"/, 'string', '@pop']
      ]
    }
  }
};
