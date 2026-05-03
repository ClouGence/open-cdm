import * as monaco from 'monaco-editor';

// 标记是否已注册
let isRegistered = false;

/**
 * 注册 MongoDB 语言定义
 * 包含语法高亮、自动补全等配置
 */
export function registerMongoDBLanguage() {
  // 避免重复注册
  if (isRegistered) {
    return;
  }
  isRegistered = true;

  // 注册 MongoDB 语言
  monaco.languages.register({ id: 'mongodb' });

  // 定义 MongoDB 主题规则（扩展默认主题）
  monaco.editor.defineTheme('vs-mongodb', {
    base: 'vs',
    inherit: true,
    rules: [
      // 关键字：蓝色加粗（如 db, use, show, var, let, const）
      { token: 'keyword.mongodb', foreground: '0000FF', fontStyle: 'bold' },
      // MongoDB 内置方法：棕色（如 getCollection, find, insertOne）
      { token: 'predefined.mongodb', foreground: '795E26', fontStyle: 'bold' },
      // 数据类型：青色（如 ObjectId, ISODate）
      { token: 'type.mongodb', foreground: '267F99', fontStyle: 'bold' },
      // MongoDB 操作符：紫色（如 $match, $set, $inc）
      { token: 'operator.mongodb', foreground: 'AF00DB', fontStyle: 'bold' },
      // 字符串：红色
      { token: 'string.mongodb', foreground: 'A31515' },
      { token: 'string.escape.mongodb', foreground: 'EE0000' },
      // 数字：深绿色
      { token: 'number.mongodb', foreground: '098658' },
      { token: 'number.float.mongodb', foreground: '098658' },
      { token: 'number.hex.mongodb', foreground: '098658' },
      // 注释：绿色
      { token: 'comment.mongodb', foreground: '008000', fontStyle: 'italic' },
      // 变量：深蓝色
      { token: 'variable.mongodb', foreground: '001080' },
      // 普通函数调用：棕色
      { token: 'function.mongodb', foreground: '795E26' },
      // 标识符：黑色
      { token: 'identifier.mongodb', foreground: '000000' }
    ],
    colors: {}
  });

  // 定义暗色主题
  monaco.editor.defineTheme('vs-dark-mongodb', {
    base: 'vs-dark',
    inherit: true,
    rules: [
      { token: 'keyword.mongodb', foreground: '569CD6', fontStyle: 'bold' },
      { token: 'predefined.mongodb', foreground: 'DCDCAA', fontStyle: 'bold' },
      { token: 'type.mongodb', foreground: '4EC9B0', fontStyle: 'bold' },
      { token: 'operator.mongodb', foreground: 'C586C0', fontStyle: 'bold' },
      { token: 'string.mongodb', foreground: 'CE9178' },
      { token: 'string.escape.mongodb', foreground: 'D7BA7D' },
      { token: 'number.mongodb', foreground: 'B5CEA8' },
      { token: 'number.float.mongodb', foreground: 'B5CEA8' },
      { token: 'number.hex.mongodb', foreground: 'B5CEA8' },
      { token: 'comment.mongodb', foreground: '6A9955', fontStyle: 'italic' },
      { token: 'variable.mongodb', foreground: '9CDCFE' },
      { token: 'function.mongodb', foreground: 'DCDCAA' },
      { token: 'identifier.mongodb', foreground: 'D4D4D4' }
    ],
    colors: {}
  });

  // 设置 MongoDB 语法高亮规则
  monaco.languages.setMonarchTokensProvider('mongodb', {
    defaultToken: '',
    tokenPostfix: '.mongodb',
    ignoreCase: true,

    brackets: [
      { open: '[', close: ']', token: 'delimiter.array' },
      { open: '{', close: '}', token: 'delimiter.curly' },
      { open: '(', close: ')', token: 'delimiter.parenthesis' }
    ],

    keywords: [
      'db',
      'use',
      'show',
      'collections',
      'dbs',
      'databases',
      'true',
      'false',
      'null',
      'undefined',
      'function',
      'return',
      'var',
      'let',
      'const',
      'if',
      'else',
      'for',
      'while',
      'switch',
      'case',
      'break',
      'continue'
    ],

    // MongoDB 方法
    builtinFunctions: [
      'find',
      'findOne',
      'findOneAndUpdate',
      'findOneAndDelete',
      'findOneAndReplace',
      'insertOne',
      'insertMany',
      'updateOne',
      'updateMany',
      'deleteOne',
      'deleteMany',
      'replaceOne',
      'aggregate',
      'count',
      'countDocuments',
      'estimatedDocumentCount',
      'distinct',
      'createIndex',
      'createIndexes',
      'dropIndex',
      'dropIndexes',
      'getIndexes',
      'listIndexes',
      'drop',
      'renameCollection',
      'getCollection',
      'getCollectionNames',
      'getCollectionInfos',
      'createCollection',
      'getName',
      'getMongo',
      'getSiblingDB',
      'stats',
      'explain',
      'limit',
      'skip',
      'sort',
      'toArray',
      'forEach',
      'map',
      'pretty',
      'hint',
      'maxTimeMS',
      'batchSize',
      'hasNext',
      'next'
    ],

    // MongoDB 操作符
    operators: [
      '$match',
      '$group',
      '$project',
      '$sort',
      '$limit',
      '$skip',
      '$unwind',
      '$lookup',
      '$graphLookup',
      '$out',
      '$merge',
      '$addFields',
      '$replaceRoot',
      '$count',
      '$facet',
      '$bucket',
      '$bucketAuto',
      '$sample',
      '$set',
      '$unset',
      '$eq',
      '$ne',
      '$gt',
      '$gte',
      '$lt',
      '$lte',
      '$in',
      '$nin',
      '$and',
      '$or',
      '$not',
      '$nor',
      '$exists',
      '$type',
      '$regex',
      '$all',
      '$elemMatch',
      '$size',
      '$expr',
      '$sum',
      '$avg',
      '$max',
      '$min',
      '$push',
      '$addToSet',
      '$first',
      '$last',
      '$inc',
      '$mul',
      '$rename',
      '$setOnInsert',
      '$currentDate',
      '$pop',
      '$pull',
      '$pullAll'
    ],

    // MongoDB 数据类型
    typeKeywords: [
      'ObjectId',
      'ISODate',
      'Date',
      'NumberLong',
      'NumberInt',
      'NumberDecimal',
      'Timestamp',
      'UUID',
      'BinData',
      'DBRef',
      'MinKey',
      'MaxKey'
    ],

    tokenizer: {
      root: [
        // 数据类型构造器
        [/\b(?:ObjectId|ISODate|Date|NumberLong|NumberInt|NumberDecimal|Timestamp|UUID|BinData|DBRef|MinKey|MaxKey)\b/, 'type'],

        // MongoDB 操作符（以 $ 开头）
        [
          /\$[a-zA-Z_]\w*/,
          {
            cases: {
              '@operators': 'operator',
              '@default': 'variable'
            }
          }
        ],

        // 函数调用
        [
          /[a-zA-Z_]\w*(?=\s*\()/,
          {
            cases: {
              '@builtinFunctions': 'predefined',
              '@keywords': 'keyword',
              '@default': 'function'
            }
          }
        ],

        // 标识符
        [
          /[a-zA-Z_]\w*/,
          {
            cases: {
              '@keywords': 'keyword',
              '@builtinFunctions': 'predefined',
              '@typeKeywords': 'type',
              '@default': 'identifier'
            }
          }
        ],

        // 空白字符
        [/[ \t\r\n]+/, ''],

        // 注释
        [/\/\/.*$/, 'comment'],
        [/\/\*/, 'comment', '@comment'],

        // 字符串
        [/"([^"\\]|\\.)*$/, 'string.invalid'],
        [/'([^'\\]|\\.)*$/, 'string.invalid'],
        [/"/, 'string', '@string_double'],
        [/'/, 'string', '@string_single'],

        // 数字
        [/\d+\.\d+([eE][-+]?\d+)?/, 'number.float'],
        [/0[xX][0-9a-fA-F]+/, 'number.hex'],
        [/\d+/, 'number'],

        // 分隔符和操作符
        [/[{}()[\]]/, '@brackets'],
        [/[;,.]/, 'delimiter'],
        [/[<>=!~?:&|+\-*/^%]+/, 'operator']
      ],

      comment: [
        [/[^/*]+/, 'comment'],
        [/\*\//, 'comment', '@pop'],
        [/[/*]/, 'comment']
      ],

      string_double: [
        [/[^\\"]+/, 'string'],
        [/\\./, 'string.escape'],
        [/"/, 'string', '@pop']
      ],

      string_single: [
        [/[^\\']+/, 'string'],
        [/\\./, 'string.escape'],
        [/'/, 'string', '@pop']
      ]
    }
  });

  // 设置语言配置
  monaco.languages.setLanguageConfiguration('mongodb', {
    comments: {
      lineComment: '//',
      blockComment: ['/*', '*/']
    },
    brackets: [
      ['{', '}'],
      ['[', ']'],
      ['(', ')']
    ],
    autoClosingPairs: [
      { open: '{', close: '}' },
      { open: '[', close: ']' },
      { open: '(', close: ')' },
      { open: '"', close: '"' },
      { open: "'", close: "'" }
    ],
    surroundingPairs: [
      { open: '{', close: '}' },
      { open: '[', close: ']' },
      { open: '(', close: ')' },
      { open: '"', close: '"' },
      { open: "'", close: "'" }
    ]
  });
}
