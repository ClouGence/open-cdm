export const esLanguage = {
  configuration: {
    comments: { lineComment: '#', blockComment: ['/*', '*/'] },
    brackets: [
      ['{', '}'],
      ['[', ']']
    ],
    autoClosingPairs: [
      { open: '{', close: '}' },
      { open: '[', close: ']' },
      { open: '"', close: '"', notIn: ['string'] }
    ],
    wordPattern: /[^\s"{}[\],:?&=/]+/g
  },
  tokens: {
    tokenizer: {
      root: [
        [/^\s*(?:GET|POST|PUT|DELETE|HEAD|OPTIONS)\b/, { cases: { '@eos': 'keyword', '@default': { token: 'keyword', next: '@header' } } }],
        [/#.*$|\/\/.*$/, 'comment'],
        [/\/\*/, 'comment', '@comment'],
        [/[{[]/, '@brackets', '@json'],
        [/\s+/, 'white'],
        [/\S+/, 'invalid']
      ],
      header: [
        [/#.*$|\/\/.*$/, 'comment', '@pop'],
        [/\s+$/, 'white', '@pop'],
        [/\s+/, 'white'],
        [/[?&=]$/, 'delimiter', '@pop'],
        [/[?&]/, 'delimiter'],
        [/[\w_]+(?==)/, 'attribute.name'],
        [/=/, 'delimiter'],
        [/[^\s?&=]+$/, 'string', '@pop'],
        [/[^\s?&=]+/, 'string'],
        [/$/, '', '@pop']
      ],
      comment: [
        [/[^*]+/, 'comment'],
        [/\*\//, 'comment', '@pop'],
        [/\*/, 'comment']
      ],
      json: [
        [/"(?:[^"\\]|\\.)*"(?=\s*:)/, 'attribute.name'],
        [/"/, 'string', '@string'],
        [/-?(?:0|[1-9]\d*)(?:\.\d+)?(?:[eE][+-]?\d+)?/, 'number'],
        [/\b(?:true|false|null)\b/, 'keyword'],
        [/[{[]/, '@brackets', '@push'],
        [/[}\]]/, '@brackets', '@pop'],
        [/[:,]/, 'delimiter'],
        [/\s+/, 'white'],
        [/./, 'invalid']
      ],
      string: [
        [/[^"\\]+/, 'string'],
        [/\\(?:["\\/bfnrt]|u[0-9a-fA-F]{4})/, 'string.escape'],
        [/\\./, 'string.escape.invalid'],
        [/"/, 'string', '@pop']
      ]
    }
  }
};
